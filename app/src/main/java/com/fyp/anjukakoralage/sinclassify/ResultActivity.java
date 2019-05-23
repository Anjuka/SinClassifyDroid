package com.fyp.anjukakoralage.sinclassify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ResultActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Bundle bundleAdd, bundleResult;
    private FirebaseAuth firebaseAuth;
    private CoordinatorLayout coordinatorLayout;

    private TextView txtCrime;
    private TextView txtPolitic;
    private TextView txtBusiness;
    private TextView txtSports;
    private TextView txtreligion;
    private static final String TAG = "Something";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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


        txtCrime = findViewById(R.id.txtCrime);
        txtPolitic = findViewById(R.id.txtPolitic);
        txtBusiness = findViewById(R.id.txtBusiness);
        txtSports = findViewById(R.id.txtSports);
        txtreligion = findViewById(R.id.txtreligion);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String[] addVal = bundle.getStringArray("addList");
        String[] resultVal = bundle.getStringArray("resultList");
        for (int i = 0; i < addVal.length; i++) {
            if (resultVal[i].equalsIgnoreCase("crime")) {
                txtCrime.append("\u2022 " + addVal[i] + "\n");
            } else if (resultVal[i].equalsIgnoreCase("politics")) {
                txtPolitic.append("\u2022 " + addVal[i] + "\n");
            } else if (resultVal[i].equalsIgnoreCase("business")) {
                txtBusiness.append("\u2022 " + addVal[i] + "\n");
            } else if (resultVal[i].equalsIgnoreCase("sports")) {
                txtSports.append("\u2022 " + addVal[i] + "\n");
            } else if (resultVal[i].equalsIgnoreCase("religion")) {
                txtreligion.append("\u2022 " + addVal[i] + "\n");
            }
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Saved Successfully", Snackbar.LENGTH_LONG);
            snackbar.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

