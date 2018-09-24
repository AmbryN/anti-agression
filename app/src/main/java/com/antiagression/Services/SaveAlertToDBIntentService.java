package com.antiagression.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antiagression.Classes.Alert;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SaveAlertToDBIntentService extends IntentService {

    public SaveAlertToDBIntentService() {
        super("SaveAlertToDBIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String alertAsJson = intent.getStringExtra("alert");
            Gson gson = new Gson();
            Alert alert = gson.fromJson(alertAsJson, Alert.class);

            final ArrayList<String> alertRelevantInfoForDB = alert.getRelevantInfoForDB();

            String urlString = "http://192.168.1.20/antiAgressionBackEnd/saveAlertData.php";

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, urlString,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("date", alertRelevantInfoForDB.get(0));
                    params.put("longitude", alertRelevantInfoForDB.get(1));
                    params.put("latitude", alertRelevantInfoForDB.get(2));
                    params.put("address", alertRelevantInfoForDB.get(3));

                    return params;
                }
            };

            postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));

            queue.add(postRequest);


        }
    }

}
