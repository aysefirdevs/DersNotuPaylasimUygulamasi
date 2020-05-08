package com.example.notuygulamam.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notuygulamam.Adapters.UserAdapter;
import com.example.notuygulamam.Models.KullaniciBilgileri;
import com.example.notuygulamam.R;
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

public class HomeFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeysList;
    RecyclerView userListRecycler;
    View view;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_home, container, false);
        tanimla();
        kullaniciGetir();
        return view;
    }

    public void tanimla()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        userKeysList=new ArrayList<>();
        userListRecycler=view.findViewById(R.id.userListRecycler);
        RecyclerView.LayoutManager mng= new GridLayoutManager(getContext(),2);
        userListRecycler.setLayoutManager(mng);
        userAdapter=new UserAdapter(userKeysList,getActivity(),getContext());
        userListRecycler.setAdapter(userAdapter);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

    }

    public void kullaniciGetir()
    {
        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("keyler",dataSnapshot.getKey()); //kullanıcıların tüm keylerini çekeceğiz.

                reference.child("Kullanicilar").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        KullaniciBilgileri kl=dataSnapshot.getValue(KullaniciBilgileri.class);
                        //kullanıcı hesabını kullanıcının hesabında görmüyoruz
                        //kullanıcı ismi olmayan kullanıcılar listede görünmüyor.
                        if(!kl.getKullaniciAdi().equals("null") && !dataSnapshot.getKey().equals(user.getUid()))
                        {
                            userKeysList.add(dataSnapshot.getKey());
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
