package com.example.notuygulamam.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notuygulamam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {
    EditText editText,editText2;
    Button button2;
    FirebaseAuth auth;
    TextView hesapvarText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        tanimla();
    }

    public void tanimla()
    {
        editText=(EditText)findViewById(R.id.editText);
        editText2=(EditText)findViewById(R.id.editText2);
        button2=(Button)findViewById(R.id.button2);
        hesapvarText=(TextView) findViewById(R.id.hesapvarText);
        auth=FirebaseAuth.getInstance();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=editText.getText().toString();
                String pass=editText2.getText().toString();
                if(!email.equals("")&& !pass.equals("")){
                    editText.setText("");
                    editText2.setText("");
                    kayitOl(email,pass);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Bilgileri boş giremezsiniz",Toast.LENGTH_LONG).show();
                }

            }
        });

        hesapvarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KayitOlActivity.this, GirisActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void kayitOl(String email,String pass)
    {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseDatabase= FirebaseDatabase.getInstance();
                    reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map=new HashMap();
                    map.put("resim","null");
                    map.put("kullaniciAdi","null");
                    map.put("okul","null");
                    map.put("bolum","null");
                    map.put("sınıf","null");

                    reference.setValue(map);

                    Intent intent=new Intent(KayitOlActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.i("yenikayit",""+reference);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Kayıt olma sırasında problem yaşandı",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
