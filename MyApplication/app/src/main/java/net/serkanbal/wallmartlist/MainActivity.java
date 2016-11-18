package net.serkanbal.wallmartlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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

import static net.serkanbal.wallmartlist.R.id.recyclerview;

public class MainActivity extends AppCompatActivity {
    public static final String GET_ITEMNAME = "http://api.walmartlabs.com/v1/search?format=json&apiKey=q8cpgj4hmeha6w47w4ab74e2&query=";
    public List<ResultItem> mList = new ArrayList<>();
    public Button mButtonPS4, mButtonXboxOne, mButtonWiiU;
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonPS4 = (Button) findViewById(R.id.buttonps4);
        mButtonXboxOne = (Button) findViewById(R.id.buttonxboxone);
        mButtonWiiU = (Button) findViewById(R.id.buttonwiiu);

        mButtonPS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWallMartItemResults("PS4");
            }
        });

        mButtonXboxOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWallMartItemResults("Xbox+One");
            }
        });

        mButtonWiiU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWallMartItemResults("Wii+U");
            }
        });
    }

    private void getWallMartItemResults(String query) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(GET_ITEMNAME+query)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed: " + response);
                } else {
                    try {
                        mList.clear();
                        JSONObject newObj = new JSONObject(response.body().string());
                        JSONArray newArray = newObj.getJSONArray("items");
                        for (int i = 0; i < newArray.length(); i++) {
                            JSONObject nameObj = newArray.getJSONObject(i);
                            String actualName = nameObj.getString("name");
                            mList.add(new ResultItem(actualName));

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView = (RecyclerView) findViewById(recyclerview);

                                    LinearLayoutManager linearLayoutManager =
                                            new LinearLayoutManager(MainActivity.this,
                                            LinearLayoutManager.VERTICAL,false);

                                    mRecyclerView.setLayoutManager(linearLayoutManager);

                                    mAdapter =
                                            new SearchResultRecyclerViewAdapter(mList);
                                    mRecyclerView.setAdapter(mAdapter);

                                    mAdapter.notifyDataSetChanged();

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
