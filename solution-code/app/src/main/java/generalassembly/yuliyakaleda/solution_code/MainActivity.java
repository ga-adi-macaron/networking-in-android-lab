package generalassembly.yuliyakaleda.solution_code;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String API_KEY = "35s8a6nuab3wydbn99nb777h";
    private static final String API_END_POINT = "http://api.walmartlabs.com/v1/search";
    private static final String QUERY_URL = API_END_POINT + "?apiKey=" + API_KEY + "&query=";
    private static final String URL_TEA = QUERY_URL + "tea";
    private static final String URL_CEREAL = QUERY_URL + "cereal";
    private static final String URL_CHOCOLATE = QUERY_URL + "chocolate";

    private List<String> mItems;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mItems = new ArrayList<>(); // initialize as an empty list - will be populated by AsyncTasks
        mAdapter = new WalmartItemRecyclerView(mItems);
        recyclerView.setAdapter(mAdapter);

        // Set up buttons
        Button cerealButton = (Button) findViewById(R.id.cereal);
        Button chocolateButton = (Button) findViewById(R.id.chocolate);
        Button teaButton = (Button) findViewById(R.id.tea);

        cerealButton.setOnClickListener(this);
        teaButton.setOnClickListener(this);
        chocolateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Check for network connectivity before trying to make API call
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            switch (v.getId()) {
                case R.id.cereal:
                    makeWalmartApiall(URL_CEREAL);
                    break;
                case R.id.chocolate:
                    makeWalmartApiall(URL_CHOCOLATE);
                    break;
                case R.id.tea:
                    makeWalmartApiall(URL_TEA);
                    break;
            }
        } else {
            Toast.makeText(this, R.string.network_connection_check, Toast.LENGTH_LONG).show();
        }
    }

    private void makeWalmartApiall(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyUserOfProblem();
                Log.d(TAG, "Http request failed: " + call.request().url().toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    notifyUserOfProblem();
                    Log.d(TAG, "Response unsuccessful: " + response.toString());
                    return;
                }

                // Clear out existing items from list
                mItems.clear();

                // Get new items & add to list
                String json = response.body().string();
                try {
                    JSONObject root = new JSONObject(json);
                    JSONArray items = root.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String name = item.getString("name");
                        mItems.add(name);
                    }
                } catch (JSONException e) {
                    notifyUserOfProblem();
                    Log.d(TAG, "Cannot parse JSON data: " + json);
                    e.printStackTrace();
                } finally {

                    // Tell the adapter that mItems was updated.
                    // Remember that this code inside onResponse() is still running on a
                    // background thread, and you can only modify views from the UI thread!
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private void notifyUserOfProblem() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "There was a problem retrieving network data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
