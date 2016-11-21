package com.korbkenny.networkinglabkenny;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String GET_CEREAL_URL = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=kshb4tez4easan9u46ue4u8s";
    public static final String GET_CHOCOLATE_URL = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=kshb4tez4easan9u46ue4u8s";
    public static final String GET_TEA_URL = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=kshb4tez4easan9u46ue4u8s";
    Button mCereal, mChocolate, mTea;
    LinearLayout mButtons;
    RecyclerView mRecyclerView;
    ItemsListAdapter mAdapter;
    ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();

        mCereal = (Button) findViewById(R.id.cereal_button);
        mChocolate = (Button) findViewById(R.id.chocolate_button);
        mTea = (Button) findViewById(R.id.tea_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemsListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mButtons = (LinearLayout) findViewById(R.id.buttons);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            mCereal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performGetRequest(GET_CEREAL_URL);
                }
            });

            mChocolate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performGetRequest(GET_CHOCOLATE_URL);
                }
            });

            mTea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performGetRequest(GET_TEA_URL);
                }
            });
        }else{
            mButtons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "Not connected to the internet...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void performGetRequest(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("What");
                }

                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        JSONArray listOfProducts = object.getJSONArray("items");
                        mList.clear();
                        for (int i = 0; i < listOfProducts.length(); i++) {
                            mList.add(listOfProducts.getJSONObject(i).getString("name"));
                        }

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        });
    }


}
