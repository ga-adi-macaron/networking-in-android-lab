package com.joelimyx.myapplication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    @BindView(R.id.button1) Button mButton1;
    Button mButton2;
    @BindView(R.id.button3) Button mButton3;
    public static final StringBuilder GET_URL_BUILDER = new StringBuilder("http://api.walmartlabs.com/v1/search?format=json&apiKey=xp8nz8badbdjrn2pb9r7bsub&query=");
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (networkInfo!=null && networkInfo.isConnected()){
                String temp="";
                switch (view.getId()){
                    case R.id.button1:
                        temp = "cereal";
                        break;
                    case R.id.button2:
                        temp = "tea";
                        break;
                    case R.id.button3:
                        temp = "chocolate";
                        break;
                }
                getResponse(GET_URL_BUILDER.toString()+temp);
            }else {
                Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
            }
        };
        mButton1.setOnClickListener(listener);
        mButton2.setOnClickListener(listener);
        mButton3.setOnClickListener(listener);
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
                    throw new IOException("BAD GET");
                }
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray array = json.getJSONArray("items");
                    List<String> list = new LinkedList<>();
                    for (int i = 0; i < array.length(); i++) {
                        list.add(array.getJSONObject(i).getString("name"));
                    }
                    mAdapter = new MainAdapter(list);
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
