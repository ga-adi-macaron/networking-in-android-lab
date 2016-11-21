package com.colinbradley.networkinginandroidlab;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    List<String> mList;
    RecyclerView mRecyclerView;
    Adapter mAdapter;
    Button mChocolate, mCereal, mTea;
    public static final String BASE_URL = "http://api.walmartlabs.com/v1/search?format=json&apiKey=dqkkey26jxxrhnzh7wgbtquk&query=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mCereal = (Button)findViewById(R.id.cereal_button);
        mChocolate = (Button)findViewById(R.id.chocolate_button);
        mTea = (Button)findViewById(R.id.tea_button);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info != null && info.isConnected()) {
                    String query = "";
                    switch (view.getId()) {
                        case R.id.cereal_button:
                            query = "cereal";
                            break;
                        case R.id.chocolate_button:
                            query = "chocolate";
                            break;
                        case R.id.tea_button:
                            query = "tea";
                            break;
                    }
                    getResponse(BASE_URL + query);
                }else {
                    Toast.makeText(MainActivity.this, "NO CONNECTION :(", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mCereal.setOnClickListener(listener);
        mChocolate.setOnClickListener(listener);
        mTea.setOnClickListener(listener);


    }

    private void getResponse(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("UH OH SOMETHING WENT WRONG :(");
                }

                try {
                    JSONObject body = new JSONObject(response.body().string());
                    JSONArray itemsArray = body.getJSONArray("items");
                    mList = new ArrayList<>();
                    for (int i = 0; i < itemsArray.length(); i++){
                        mList.add(itemsArray.getJSONObject(i).getString("name"));
                    }
                    mAdapter = new Adapter(mList);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
