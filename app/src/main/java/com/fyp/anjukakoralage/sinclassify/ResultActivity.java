package com.fyp.anjukakoralage.sinclassify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<String> crimeList = new ArrayList<String>();
        String crime = "";
        String political = "";
        Map<String,List<String>> list = new HashMap<>();
        List<String> values;
        for(int i =0;i<addVal.length;i++){

            if(list.get(resultVal[i])!=null) {
                values = list.get(resultVal[i]);
            }else{
                values = new ArrayList<>();
            }
            values.add(addVal[i]);
            list.put(resultVal[i],values);
        }
        for(Map.Entry entry:list.entrySet()){
            if (entry.getKey().equals("crime")){
                txtCrime.append(entry.getValue().toString());
            }
            else if (entry.getKey().equals("politics")){
                txtPolitic.append(entry.getValue().toString());
            }
        }

    }
}

