package com.example.notuygulamam.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.notuygulamam.Fragments.BildirimFragment;
import com.example.notuygulamam.Fragments.ProfilFragment;
import com.example.notuygulamam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.notuygulamam.Fragments.HomeFragment;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth auth;
    public FirebaseUser user;
    public FloatingActionButton fabMain, fabBirinci, fabikinci;
    private Animation fabacik, fabkapali, geridon, ileridon;
    private Boolean fabDurum = false;
    DatabaseReference ref;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        kontrol();
        loadFragment(new HomeFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setItemIconTintList(null);

        fabMain = findViewById(R.id.fabMain);
        fabBirinci = findViewById(R.id.fabBirinci);
        fabikinci = findViewById(R.id.fabikinci);
        fabacik = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fabacik);
        fabkapali = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fabkapali);
        geridon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.geridon);
        ileridon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ileridon);


        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabDurum) {
                    //tıklandığında kapansın
                    fabMain.startAnimation(geridon);
                    fabBirinci.startAnimation(fabkapali);
                    fabikinci.startAnimation(fabkapali);
                    fabBirinci.setClickable(false);
                    fabikinci.setClickable(false);
                    fabDurum = false;
                } else {
                    //tıklandığında açılsın
                    fabMain.startAnimation(ileridon);
                    fabBirinci.startAnimation(fabacik);
                    fabikinci.startAnimation(fabacik);
                    fabBirinci.setClickable(true);
                    fabikinci.setClickable(true);
                    fabDurum = true;
                    fabBirinci.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, NotEkleActivity.class);
                            startActivity(intent);
                        }
                    });
                    fabikinci.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ActivityImages.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
     /*   if(query.equals("PxMt2eDGj5O47OeIAXrvspmS0HR2")){
            fabikinci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(MainActivity.this,BosActivity.class);
                    startActivity(intent);

                }
            });
        } */
   /*  userId=user.getUid();
     ref= FirebaseDatabase.getInstance().getReference();
    //final Query query=ref.orderByChild("Kullanicilar").equalTo("PxMt2eDGj5O47OeIAXrvspmS0HR2");
        Query query = ref.child("Kullanicilar").child(userId).equalTo("PxMt2eDGj5O47OeIAXrvspmS0HR2");
     query.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(ref.equals("PxMt2eDGj5O47OeIAXrvspmS0HR2")==true) {
                 fabikinci.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(getApplicationContext(),"key aljndı",Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(MainActivity.this, BosActivity.class);
                         startActivity(intent);

                     }
                 });
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });

*/
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void kontrol() {
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, GirisActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new ProfilFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new BildirimFragment();
                    break;
                case R.id.navigation_exit:
                    cik();
                    return true;

            }
            return loadFragment(fragment);
        }
    };


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentlayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void cik() {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, GirisActivity.class);
        startActivity(intent);
        finish();
    }
}
