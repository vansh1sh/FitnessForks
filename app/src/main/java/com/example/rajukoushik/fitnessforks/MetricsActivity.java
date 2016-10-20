package com.example.rajukoushik.fitnessforks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MetricsActivity extends AppCompatActivity {

    public static final String METRIC_URL = "http://192.168.43.137:8000/api/post/user_details/";
    private EditText editTextAge;
    private EditText editTextHeight;
    private EditText editTextWeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);

        editTextAge = (EditText) findViewById(R.id.editTextUsername);
        editTextHeight = (EditText) findViewById(R.id.editTextFirstName);
        editTextWeight= (EditText) findViewById(R.id.editTextLastName);

    }

    public void clickingNext(View view) {
        addMetrics();
    }

    public void addMetrics()
    {
        boolean result = false;
        HttpClient hc = new DefaultHttpClient();
        String message;
        final String age = editTextAge.getText().toString().trim();
        final String height = editTextHeight.getText().toString().trim();
        final String weight = editTextWeight.getText().toString().trim();



        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.43.137:8000/api/post/user_details/";
            JSONObject jsonBody = new JSONObject();
            PrefManger prefManger =new PrefManger(MetricsActivity.this);
            String tempToken = prefManger.getToken();
            Log.e("shjg",tempToken);

            jsonBody.put("token",tempToken);

            jsonBody.put("date_of_birth", age);
            jsonBody.put("height", height);
            jsonBody.put("weight", weight);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return mRequestBody.getBytes();
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);

            Intent intent = new Intent(this, WorkActivity.class);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
