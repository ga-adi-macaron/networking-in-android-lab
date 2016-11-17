package com.jonathanlieblich.myapplication;

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
    public static final String URL_START =
            "http://api.walmartlabs.com/v1/search?apiKey=s4x4jthz6j87wwmd5s3vuafd&query=";
    public static final String CEREAL = "cereal";
    public static final String CHOCOLATE = "chocolate";
    public static final String TEA = "tea";
    private Button mCereal, mChocolate, mTea;
    private RecyclerView mRecycler;
    private ProductViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCereal = (Button)findViewById(R.id.cereal);
        mChocolate= (Button)findViewById(R.id.chocolate);
        mTea = (Button)findViewById(R.id.tea);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.cereal:
                        getProductRequest(CEREAL);
                        break;
                    case R.id.chocolate:
                        getProductRequest(CHOCOLATE);
                        break;
                    case R.id.tea:
                        getProductRequest(TEA);
                        break;
                }
            }
        };
        mCereal.setOnClickListener(listener);
        mChocolate.setOnClickListener(listener);
        mTea.setOnClickListener(listener);
    }

    private void getProductRequest(String s) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL_START+s).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code: "+response);
                }

                try {
                    List<String> items1 = new ArrayList<>();
                    JSONObject prim = new JSONObject(response.body().string());
                    JSONArray products = prim.getJSONArray("items");
                    for(int i=0;i<products.length();i++) {
                        JSONObject item = products.getJSONObject(i);
                        String itemName = item.getString("name");
                        items1.add(itemName);
                    }
                    final List<String> items = items1;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecycler = (RecyclerView)findViewById(R.id.recycler_view);
                            LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                            mAdapter = new ProductViewAdapter(items);
                            mRecycler.setLayoutManager(layout);
                            mRecycler.setAdapter(mAdapter);
                        }
                    });
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
