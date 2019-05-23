package com.fyp.anjukakoralage.sinclassify;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fyp.anjukakoralage.sinclassify.util.ApplicationConstants;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REARMISION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    private FirebaseAuth firebaseAuth;
    private JSONArray textArrayJ;
    private JSONObject saveObject1;
    private Bundle bundle;
    private String[] predict;

    private CoordinatorLayout coordinatorLayout;
    private Button btnImport, btnClear, btnclassify;
    private EditText txtInput;
    private String fulltext;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        bundle =new Bundle();

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REARMISION_REQUEST_STORAGE);
        }

        btnImport = (Button) findViewById(R.id.btnImport);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnclassify = (Button) findViewById(R.id.btnclassify);
        txtInput = (EditText) findViewById(R.id.txtInput);

        ArrayList<String> sentenceList = new ArrayList<String>();

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtInput.setText("");
                txtInput.setHint(R.string.boxText);
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();

            }
        });

        btnclassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textArrayJ = new JSONArray();
                saveObject1 = new JSONObject();
                fulltext = txtInput.getText().toString();
                String reg = "[\\u002E | \\u00A0]";
                predict = fulltext.split(".\n");


                for (int i = 0; i < predict.length; i++) {
                    textArrayJ.put(predict[i]);
                }
                System.out.println("Splited data" + textArrayJ);
                try {
                    saveObject1.put("predict", textArrayJ);
                    System.out.println(saveObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (fulltext.equalsIgnoreCase("")) {
                    requestFail();
                } else {


                    showPDialog();


                    classifyAPI();
                }
            }
        });
    }

    private void classifyAPI() {

        String URL = "https://sinclassify-238005.appspot.com/api/predict";

        JsonObjectRequest SaveDetails = new JsonObjectRequest(Request.Method.POST, URL, saveObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                hidePDialog();
                try {
                    if (!response.getString("prediction").isEmpty()) {

                        JSONArray jsonArray = response.has("prediction") ? response.getJSONArray("prediction") : new JSONArray();
                        String[] responseArray = new String[jsonArray.length()];
                        for (int i =0; i<jsonArray.length();i++){
                            responseArray[i] = (String)jsonArray.get(i);
                        }

                        Snackbar.make(findViewById(android.R.id.content), ApplicationConstants.SAVED_SUCCESS, Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        bundle.putStringArray("addList", predict);
                        bundle.putStringArray("resultList", responseArray);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else {
                        hidePDialog();
                        Snackbar.make(findViewById(android.R.id.content), ApplicationConstants.SAVE_FAILED, Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hidePDialog();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_GENERAL, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hidePDialog();
                if (volleyError instanceof NetworkError) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_NETWORK, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (volleyError instanceof TimeoutError) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_NETWORK_TIMEOUT, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_GENERAL, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        SaveDetails.setRetryPolicy(new DefaultRetryPolicy(25000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(SaveDetails);
    }


    private void requestFail() {
        Toast.makeText(this, "Please import or paste data...", Toast.LENGTH_SHORT).show();
    }

    //read content text file
    private String readText(String input) {
        File file = new File(Environment.getExternalStorageDirectory(), input);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    //select file from storage
    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void showPDialog() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);
                }
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                txtInput.setText(readText(path));

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REARMISION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
    }
}
