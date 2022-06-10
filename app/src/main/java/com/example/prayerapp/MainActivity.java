package com.example.prayerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prayerapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView mfajr, mdhuhr, masr, mmaghrib, misha, mlocation, mdate;
    EditText msearch;
    Button mfindbtn;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));

        mfajr = findViewById(R.id.fajr);
        mdhuhr = findViewById(R.id.dhuhr);
        masr = findViewById(R.id.asr);
        mmaghrib = findViewById(R.id.maghrib);
        misha = findViewById(R.id.isha);
        mlocation = findViewById(R.id.myloaction);
        mdate = findViewById(R.id.mydate);
        msearch = findViewById(R.id.searchloc);
        mfindbtn = findViewById(R.id.findbtn);
//        qiblaDirection = findViewById(R.id.qiblaDirection);

        mfindbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mloc = msearch.getText().toString().trim();

                if (mloc.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Location", Toast.LENGTH_SHORT).show();
                } else {
                    url = "https://muslimsalat.com/" + mloc + ".json?key=903b8a528a1f2770e7857a6d6e801463";
                    searchlocation();
                    closeKeyboard();
                }
            }
        });


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchlocation() {
        //volley library
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Get Location
                            String country = response.get("country").toString();
                            String state = response.get("state").toString();
                            String city = response.get("city").toString();
                            String location = country + ", " + state + ", " + city;

                            //Get date
                            String date = response.getJSONArray("items").getJSONObject(0).get("date_for").toString();

                            //Get Data
                            String fajr = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                            String dhuhr = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                            String asr = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                            String maghrib = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                            String isha = response.getJSONArray("items").getJSONObject(0).get("isha").toString();

                            //Set Data
                            mfajr.setText(fajr);
                            mdhuhr.setText(dhuhr);
                            masr.setText(asr);
                            mmaghrib.setText(maghrib);
                            misha.setText(isha);
                            mlocation.setText(location);
                            mdate.setText(date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Check Your Internet", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}