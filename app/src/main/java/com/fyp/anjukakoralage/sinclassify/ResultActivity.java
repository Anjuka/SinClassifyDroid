package com.fyp.anjukakoralage.sinclassify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private Bundle bundleAdd, bundleResult;
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
}

