package com.fyp.anjukakoralage.sinclassify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ContactUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private CoordinatorLayout coordinatorLayout;

    private EditText etContactSubject;
    private EditText etContactBody;
    private Button btnCall;
    private Button btnEmail;
    private Button btnSend;
    private Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView ivUserImage = (ImageView) headerView.findViewById(R.id.ivUserImage);
        TextView tvSalutation = (TextView) headerView.findViewById(R.id.tvSalutation);

        tvSalutation.setText(firebaseAuth.getCurrentUser().getEmail());

        btnCall = findViewById(R.id.btnCall);
        btnEmail = findViewById(R.id.btnEmail);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);
        //etContactEmail = findViewById(R.id.etContactEmail);
        etContactSubject = findViewById(R.id.etContactSubject);
        etContactBody = findViewById(R.id.etContactBody);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etContactBody.setVisibility(View.VISIBLE);
                etContactSubject.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = firebaseAuth.getCurrentUser().getEmail();
                String sendEmail = "anjukako@gmail.com";
                String subject = etContactSubject.getText().toString();
                String body = "Hi I am " + email + " " + "\n" + etContactBody.getText().toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{sendEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT, body);
                i.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etContactBody.setText("");
                etContactSubject.setText("");
                etContactBody.setVisibility(View.INVISIBLE);
                etContactSubject.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);
                btnSend.setVisibility(View.INVISIBLE);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "0715170929";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent Home = new Intent(getApplicationContext(), MainActivity.class);
            Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Home);
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            Intent out = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(out);
        } else if (id == R.id.nav_link) {
            Intent resa = new Intent(getApplicationContext(), ResourcesActivity.class);
            startActivity(resa);
        }
        else if (id == R.id.nav_support) {
            Intent resa = new Intent(getApplicationContext(), SupportActivity.class);
            startActivity(resa);
        }
        else if (id == R.id.nav_contacUs) {
            Intent resa = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(resa);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
