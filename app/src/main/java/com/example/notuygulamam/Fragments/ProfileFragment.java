package com.example.notuygulamam.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.notuygulamam.Models.KullaniciBilgileri;
import com.example.notuygulamam.R;
import com.example.notuygulamam.Utils.ChangeFragment;
import com.example.notuygulamam.Utils.RandomName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    String imageURL;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    View view;
    EditText kullaniciAdi,input_okul,input_bolum,input_sinif;
    CircleImageView profile_image;
    Button bilgi_guncelle,bilgiArkadasButon,bilgiİstekButon;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    private static final int PICK_IMAGES_REQUEST=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        tanimla();
        bilgileriGetir();
        return view;
    }

    public void tanimla(){
        kullaniciAdi=(EditText)view.findViewById(R.id.kullaniciAdi);
        input_okul=(EditText)view.findViewById(R.id.input_okul);
        input_bolum=(EditText)view.findViewById(R.id.input_bolum);
        input_sinif=(EditText)view.findViewById(R.id.input_sinif);
        profile_image=(CircleImageView) view.findViewById(R.id.profile_image);
        bilgi_guncelle=(Button) view.findViewById(R.id.bilgi_guncelle);
        bilgiArkadasButon=(Button) view.findViewById(R.id.bilgiArkadasButon);
      //  bilgiİstekButon=(Button) view.findViewById(R.id.bilgiİstekButon);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        bilgi_guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });

        bilgiArkadasButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new ArkadaslarFragment());
            }
        });

      /*  bilgiİstekButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());
            }
        }); */



        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               galeriAc();
            }

        });

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Kullanicilar").child(user.getUid());
    }



    private void galeriAc(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,5);

    }

   public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==5 && resultCode== Activity.RESULT_OK){
             Uri filePath= data.getData();
            Log.i("profilresim",""+filePath);
             StorageReference ref= storageReference.child("profilresimleri").child(RandomName.getSaltString()+".jpg");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Profil fotoğrafı güncellendi.",Toast.LENGTH_LONG).show();
                        String isim=kullaniciAdi.getText().toString();
                        String egitim=input_okul.getText().toString();
                        String bolum=input_bolum.getText().toString();
                        String sinif=input_sinif.getText().toString();

                        reference=database.getReference().child("Kullanicilar").child(auth.getUid());
                        Map map=new HashMap();

                        map.put("kullaniciAdi",isim);
                        map.put("okul",egitim);
                        map.put("bolum",bolum);
                        map.put("sınıf",sinif);
                        map.put("resim",task.getResult().getMetadata().getReference().getDownloadUrl().toString()); //buraya bi kez daha bak sonra
                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                      ChangeFragment fragment = new ChangeFragment(getContext());
                                      fragment.change(new ProfileFragment());
                                   // Toast.makeText(getContext(),"bilgiler basariyla guncellendi..",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getContext(),"Bilgiler guncellenmedi...",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getContext(),"Profil fotoğrafı güncellenmedi.",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    public void bilgileriGetir()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciBilgileri kl=dataSnapshot.getValue(KullaniciBilgileri.class);
                kullaniciAdi.setText(kl.getKullaniciAdi());
                input_okul.setText(kl.getOkul());
                input_bolum.setText(kl.getBolum());
                input_sinif.setText(kl.getSınıf());
                imageURL=kl.getResim();

                if(!kl.getResim().equals("null"))
                {
                    Picasso.get()
                            .load(kl.getResim())
                            .into(profile_image);
                }


            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void guncelle()
    {
        String isim=kullaniciAdi.getText().toString();
        String egitim=input_okul.getText().toString();
        String bolum=input_bolum.getText().toString();
        String sinif=input_sinif.getText().toString();

        reference=database.getReference().child("Kullanicilar").child(auth.getUid());
        Map map=new HashMap();

        map.put("kullaniciAdi",isim);
        map.put("okul",egitim);
        map.put("bolum",bolum);
        map.put("sınıf",sinif);
        if(imageURL.equals("null")){
            map.put("resim","null");
        }
        else{
            map.put("resim",imageURL);
        }

        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ChangeFragment fragment = new ChangeFragment(getContext());
                    fragment.change(new ProfileFragment());
                    Toast.makeText(getContext(),"bilgiler basariyla guncellendi..",Toast.LENGTH_LONG).show();


                }
                else {
                    Toast.makeText(getContext(),"Bilgiler guncellenmedi...",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
