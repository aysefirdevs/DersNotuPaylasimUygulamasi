package com.example.notuygulamam.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class GirisActivity extends AppCompatActivity {

    public EditText input_email,input_password;
    public Button button_login;
    public FirebaseAuth auth;
    public TextView hesapYok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        tanimla();
    }

    public void tanimla()
    {
        input_email=(EditText)findViewById(R.id.input_email);
        input_password=(EditText)findViewById(R.id.input_password);
        button_login=(Button) findViewById(R.id.button_login);
        auth=FirebaseAuth.getInstance();
        hesapYok=(TextView)findViewById(R.id.hesapYok);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=input_email.getText().toString();
                String pass=input_password.getText().toString();
                if(!email.equals("")&& !pass.equals(""))
                {
                    sistemeGiris(email,pass);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Boş girilemez.",Toast.LENGTH_LONG).show();
                }
            }
        });
        hesapYok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(GirisActivity.this,KayitOlActivity.class); //bizi ana aktivitiye göndercek giriş yapınca
                startActivity(intent);
                finish();
            }
        });
    }
    public void sistemeGiris(String email,String pass)
    {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent =new Intent(GirisActivity.this,MainActivity.class); //bizi ana aktivitiye göndercek giriş yapınca
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"E-posta veya şifrenizi kontrol edin.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
