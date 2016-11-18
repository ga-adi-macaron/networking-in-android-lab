package com.justinwells.networkinglab;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {



    public static final String CEREAL_URL
            = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=mp9c3vfyp8z9z9up6bc746ny\n";
    public static final String CHOCOLATE_URL
            = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=mp9c3vfyp8z9z9up6bc746ny";
    public static final String TEA_URL
            = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=mp9c3vfyp8z9z9up6bc746ny\n";

    RecyclerView recyclerView;



    Button cerealButton, chocolateButton, teaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cerealButton = (Button) findViewById(R.id.button1);
            cerealButton.setOnClickListener(onClick);
        chocolateButton = (Button) findViewById(R.id.button2);
            chocolateButton.setOnClickListener(onClick);
        teaButton = (Button) findViewById(R.id.button3);
            teaButton.setOnClickListener(onClick);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    private boolean isConnected () {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        }

        Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        return false;

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WalmartAsyncTask task = new WalmartAsyncTask();
            
            if (isConnected()) {
                switch (view.getId()) {
                    case R.id.button1: task.execute(CEREAL_URL);
                        break;
                    case R.id.button2: task.execute(CHOCOLATE_URL);
                        break;
                    case R.id.button3: task.execute(TEA_URL);
                        break;
                }
            }
        }
    };

    public class WalmartAsyncTask extends AsyncTask<String, Void, List<CustomObject>> {

        //TODO: change the return type from void to a new class representing the API data
        @Override
        protected List<CustomObject> doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0])
                    .build();

            try {


                Response response = client.newCall(request).execute();

                String data = response.body().string();


                Gson gson = new Gson();
                SearchResults results =  gson.fromJson(data,SearchResults.class);

                return results.getList();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<CustomObject> list) {
            super.onPostExecute(list);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this
                            , LinearLayoutManager.VERTICAL
                            , false));
            CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);
            recyclerView.getAdapter().notifyDataSetChanged();


        }
    }

}
