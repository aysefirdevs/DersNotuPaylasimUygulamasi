package com.example.notuygulamam.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notuygulamam.Fragments.OtherProfileFragment;
import com.example.notuygulamam.Models.KullaniciBilgileri;
import com.example.notuygulamam.R;
import com.example.notuygulamam.Utils.ChangeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
    }

    //layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlayout,parent,false);
        return new ViewHolder(view);
    }

    //viewlere setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
       // holder.usernameTextView.setText(userKeysList.get(position).toString());
        reference.child("Kullanicilar").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciBilgileri kl=dataSnapshot.getValue(KullaniciBilgileri.class);

                   Picasso.get().load(kl.getResim()).into(holder.userimage);
                   holder.usernameTextView.setText(kl.getKullaniciAdi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.userAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment fragment=new ChangeFragment(context);
                fragment.changeWithParameter(new OtherProfileFragment(),userKeysList.get(position));
            }
        });

    }

    //adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }

    //viewlerin tanımlanma işlemleri
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView usernameTextView;
        CircleImageView userimage;
        LinearLayout userAnaLayout;
        ViewHolder(View itemView)
        {
            super(itemView);
            usernameTextView = (TextView)itemView.findViewById(R.id.usernameTextView);
            userimage=(CircleImageView)itemView.findViewById(R.id.userimage);
            userAnaLayout=(LinearLayout)itemView.findViewById(R.id.userAnaLayout);
        }
    }

}
