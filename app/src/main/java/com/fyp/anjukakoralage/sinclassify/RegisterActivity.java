package com.fyp.anjukakoralage.sinclassify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout lllogin;
    private Button btnRegister;
    private EditText etEmail, etPassword, etRePassword, etUsername;
    private String email, password, repassword, name;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLocal();
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        lllogin = (LinearLayout) findViewById(R.id.lllogin);
        btnRegister = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etUsername = (EditText) findViewById(R.id.etUsername);

        lllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                repassword = etRePassword.getText().toString().trim();
                name = etUsername.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Name is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Email is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Password is empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(getApplicationContext(), "Re-enter your password",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (!repassword.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Password not match, Please re-enter",
                            Toast.LENGTH_LONG).show();
                    etPassword.setText("");
                    etRePassword.setText("");
                } else {
                    registerUser();
                }

            }
        });
    }

    private void registerUser() {

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registered Successfully",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Could not registered, Please try a gain",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());


        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My Lang", lang);
        editor.apply();

    }

    public void loadLocal() {
        SharedPreferences preferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My Lang", "");
        setLocale(language);
    }
}
