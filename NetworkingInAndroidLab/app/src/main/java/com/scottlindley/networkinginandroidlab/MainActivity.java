package com.scottlindley.networkinginandroidlab;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String
            TEA_QUERY = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=hcqn2bnrqmb8tzxt2ejjyphq",
            CHOCOLATE_QUERY = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=hcqn2bnrqmb8tzxt2ejjyphq",
            CEREAL_QUERY = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=hcqn2bnrqmb8tzxt2ejjyphq";

    private RecyclerView mRecyclerView;
    private Button mChocolateBtn, mTeaBtn, mCerealBtn;
    private List<Item> mItems;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chocolate_button:
                performGetRequest(CHOCOLATE_QUERY);
                break;
            case R.id.cereal_button:
                performGetRequest(CEREAL_QUERY);
                break;
            case R.id.tea_button:
                performGetRequest(TEA_QUERY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChocolateBtn = (Button)findViewById(R.id.chocolate_button);
        mCerealBtn = (Button)findViewById(R.id.cereal_button);
        mTeaBtn = (Button)findViewById(R.id.tea_button);

        mItems = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(new RecyclerAdapter(mItems));

        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info!=null && info.isConnected()){
            mCerealBtn.setOnClickListener(this);
            mChocolateBtn.setOnClickListener(this);
            mTeaBtn.setOnClickListener(this);
        }else{
            Toast.makeText(this, "No network connection found", Toast.LENGTH_SHORT).show();
        }
    }

    private void performGetRequest (String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("Unexpected code: "+response);
                }
                try{
                    JSONObject rootObject = new JSONObject(response.body().string());
                    JSONArray itemsArray = (JSONArray)rootObject.get("items");
                    mItems.removeAll(mItems);
                    for(int i=0; i<itemsArray.length(); i++){
                        JSONObject jObject = (JSONObject)itemsArray.get(i);
                        mItems.add(new Item(
                                jObject.getString("name"), "Price: $"+jObject.getString("salePrice")));
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }


}
