package com.ezequielc.networking_in_android_lab;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String CEREAL_URL = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=k6u2srf6huv44a45a2pyu9w6";
    public static final String CHOCOLATE_URL = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=k6u2srf6huv44a45a2pyu9w6";
    public static final String TEA_URL = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=k6u2srf6huv44a45a2pyu9w6";
    RecyclerView mRecyclerView;
    Button mCerealButton, mChocolateButton, mTeaButton;
    List<Products> mProductsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button) findViewById(R.id.cereal_button);
        mChocolateButton = (Button) findViewById(R.id.chocolate_button);
        mTeaButton = (Button) findViewById(R.id.tea_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mProductsList = new ArrayList<>();
        mRecyclerView.setAdapter(new ProductRVAdapter(mProductsList));

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            Toast.makeText(this, "Connected to Network", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cereal_button:
                        performGetRequest(CEREAL_URL);
                        break;
                    case R.id.chocolate_button:
                        performGetRequest(CHOCOLATE_URL);
                        break;
                    case R.id.tea_button:
                        performGetRequest(TEA_URL);
                        break;
                }
            }
        };

        mCerealButton.setOnClickListener(onClickListener);
        mChocolateButton.setOnClickListener(onClickListener);
        mTeaButton.setOnClickListener(onClickListener);
    }

    private void performGetRequest(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code: " + response);
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject results = new JSONObject(responseBody);
                    JSONArray itemArr = results.getJSONArray("items");
                    mProductsList.removeAll(mProductsList);
                    for (int i = 0; i < itemArr.length(); i++) {
                        JSONObject objects = (JSONObject) itemArr.get(i);
                        mProductsList.add(new Products(objects.getString("name")));
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
