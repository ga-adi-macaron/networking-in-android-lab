package drsdrs.networkinginandroidlab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "52awaqsf44g67m33ywnc8c8d";

    private static String mUrl = "http://api.walmartlabs.com/v1/search?apiKey=" + API_KEY + "&query=";

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cerealButton = (Button) findViewById(R.id.cereal_button);
        Button teaButton = (Button) findViewById(R.id.tea_button);
        Button chocolateButton = (Button) findViewById(R.id.chocolate_button);

        //Set up recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForItems("cereal");
            }
        });

        teaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForItems("tea");
            }
        });

        chocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForItems("chocolate");
            }
        });
    }

    public void searchForItems(String search) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // the connection is available

            String searchURL = mUrl + search;
            WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
            walmartAsyncTask.execute(searchURL);

        } else {
            // the connection is not available
        }
    }

    public class WalmartAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0])
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                JSONArray items = json.getJSONArray("items");

                ArrayList<String> itemNames = new ArrayList<>();

                for (int i = 0; i < items.length(); i++) {

                    JSONObject item = items.getJSONObject(i);
                    String name = item.getString("name");

                    itemNames.add(name);
                }

                WalmartItemRVAdapter itemAdapter = new WalmartItemRVAdapter(itemNames);
                mRecyclerView.setAdapter(itemAdapter);



                JSONObject args = json.getJSONObject("args");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
