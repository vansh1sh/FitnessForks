package com.example.rajukoushik.fitnessforks;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity {

    //url -- image_url

    public int itemCounter = 0;

    public int getItemCounter()
    {
        return itemCounter;
    }

    public ArrayList<FoodObject> getCart()
    {
        return  cartList;
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private static ArrayList<FoodObject> dataset = new ArrayList<>();
    private static ArrayList<FoodObject> cartList = new ArrayList<>();
    static String  finalTimeDate;
    static TextView progressView;
    static TextView countText;

    private static ArrayList<String> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressView = (TextView) findViewById(R.id.textView7);
        countText =(TextView)findViewById(R.id.textMeal);
        HomeActivity haa = new HomeActivity();
        countText.setText("Select "+haa.getItemCount()+ " Meals");


        //card
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        //end of card

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Checkout for Payment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {


                //add date time dialog

                //end of date time dialog




                HomeActivity ha = new HomeActivity();

                Toast.makeText(MenuActivity.this, "Item Added to cart" ,
                        Toast.LENGTH_LONG).show();
                Log.i(LOG_TAG, " Clicked on Item " + position);
                cartList.add(((MyRecyclerViewAdapter) mAdapter).getDataObject(position));
                itemCounter = itemCounter + 1;

                int tempo = ha.getItemCount() - itemCounter ;

                        progressView.setText(""+tempo+" more to go");





                if(itemCounter == ha.getItemCount() )
                {

                    //post Items

                    boolean result = false;
                    HttpClient hc = new DefaultHttpClient();
                    String message;




                    try {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        RequestQueue requestQueue = Volley.newRequestQueue(MenuActivity.this);
                        String URL = "http://192.168.43.137:8000/api/post/order/";
                        JSONObject jsonBody = new JSONObject();
                        PrefManger prefManger =new PrefManger(MenuActivity.this);
                        String tempToken = prefManger.getToken();
                        Log.e("shjg",tempToken);

                        HomeActivity hact = new HomeActivity();


                        jsonBody.put("token",tempToken);

                        jsonBody.put("number_of_items",hact.getItemCount() );
                        jsonBody.put("price", 249*(hact.getItemCount()));

                        JSONObject arr = new JSONObject();

                        for(int i = 0 ; i <  hact.getItemCount() ; i ++) {
                            String array_label = String.valueOf(i+1);
                            JSONObject array_obj = new JSONObject();

                            array_obj.put("item_id", cartList.get(i).getId());


                            array_obj.put("expected_time", formattedDate+"+0530");

                            arr.put(array_label, array_obj);
                        }

                        jsonBody.put("food_items", arr);

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

                        Intent intent = new Intent(MenuActivity.this, CartActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    //end of post items
                    Intent intent = new Intent(MenuActivity.this, CartActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void postItems()
    {



    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


    private ArrayList<FoodObject> getDataSet() {

        /**
         ArrayList results = new ArrayList<FoodObject>();
         for (int index = 0; index < 20; index++) {
         FoodObject obj = new FoodObject("Some Primary Text " + index,
         "Secondary " + index);
         results.add(index, obj);
         }
         **/
        return this.dataset;
    }

    public void set_dataset(ArrayList<FoodObject> a) {
        this.dataset = a;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
