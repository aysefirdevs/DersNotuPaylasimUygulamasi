package com.example.notuygulamam.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notuygulamam.Models.KullaniciBilgileri;
import com.example.notuygulamam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileFragment extends Fragment {

    View view;
    String otherId, userId, kontrol;
    TextView userProfileNameText, userProfileOkulText, userProfileBolumText, userProfileSinifText, userProfileArkText;
    ImageView userProfileAddImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference_arkadaslik;
    CircleImageView userProfileImage;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tanimla();
        action();
        getArkadasText();
        return view;
    }

    public void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference_arkadaslik = firebaseDatabase.getReference().child("Arkadaslik_Istek");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        otherId = getArguments().getString("userid");
        userProfileNameText = (TextView) view.findViewById(R.id.userProfileNameText);
        userProfileOkulText = (TextView) view.findViewById(R.id.userProfileOkulText);
        userProfileBolumText = (TextView) view.findViewById(R.id.userProfileBolumText);
        userProfileSinifText = (TextView) view.findViewById(R.id.userProfileSinifText);
        userProfileArkText = (TextView) view.findViewById(R.id.userProfileArkText);
        userProfileAddImage = (ImageView) view.findViewById(R.id.userProfileAddImage);
     //   userProfileDeleteImage = (ImageView) view.findViewById(R.id.userProfileDeleteImage);
        userProfileImage = (CircleImageView) view.findViewById(R.id.userProfileImage);

        reference_arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    kontrol = "istek";

                    userProfileAddImage.setImageResource(R.drawable.arkadas_sil);
                } else {

                    userProfileAddImage.setImageResource(R.drawable.arkadas_ekle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherId)) {
                    kontrol="arkadas";
                    userProfileAddImage.setImageResource(R.drawable.social);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void action() {
        reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KullaniciBilgileri kl = dataSnapshot.getValue(KullaniciBilgileri.class);
                userProfileNameText.setText(kl.getKullaniciAdi());
                userProfileOkulText.setText("Okul: " + kl.getOkul());
                userProfileBolumText.setText("Bölüm: " + kl.getBolum());
                userProfileSinifText.setText("Sınıf: " + kl.getSınıf());
                if (!kl.getResim().equals("null")) {

                    Picasso.get().load(kl.getResim()).into(userProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userProfileAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol== "istek") {
                    arkadasIptalEt(otherId, userId);
                }else if(kontrol=="arkadas"){
                    arkadasTablosundanCikar(otherId,userId);
                }
                else {
                    arkadasEkle(otherId, userId);
                }
            }
        });

    }
    public void arkadasTablosundanCikar(final String otherId,final String userId)
    {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        userProfileAddImage.setImageResource(R.drawable.arkadas_ekle);
                        Toast.makeText(getContext(), "Arkadaşlıktan çıkarıldı.", Toast.LENGTH_SHORT).show();
                        getArkadasText();

                    }
                });
            }
        });
    }

    public void arkadasEkle(final String otherId, final String userId) {
        reference_arkadaslik.child(userId).child(otherId).child("tip").setValue("gönderildi")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            reference_arkadaslik.child(otherId).child(userId).child("tip").setValue("alındı")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                kontrol = "istek";
                                                Toast.makeText(getContext(), "Arkadaşlık isteği gönderildi."
                                                        , Toast.LENGTH_SHORT).show();
                                                userProfileAddImage.setImageResource(R.drawable.social);
                                            } else {
                                                Toast.makeText(getContext(), "Bir problem meydana geldi.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Bir problem meydana geldi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void arkadasIptalEt(final String otherId, final String userId) {
        reference_arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        userProfileAddImage.setImageResource(R.drawable.arkadas_ekle);
                        Toast.makeText(getContext(), "Arkadaşlık isteği iptal edildi.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    public void getArkadasText()
    {
      //  final List<String> arkList=new ArrayList<>();
      //  userProfileArkText.setText("0 Arkadaş");
        userProfileArkText.setText("0 Arkadaş");
       reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileArkText.setText(dataSnapshot.getChildrenCount() + " Arkadaş");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
