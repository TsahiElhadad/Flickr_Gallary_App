package com.example.flicker_gallery_app;

import  android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

// First screen - images from flickr
public class MainActivity extends AppCompatActivity {

    // Initialize Variables
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<MainData> dataArrayList = new ArrayList<>();
    MainAdapter adapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get id's of variables
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        // Initialize adapter
        adapter = new MainAdapter(dataArrayList, MainActivity.this);
        // Set layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set adapter
        recyclerView.setAdapter(adapter);

        // Get Data from flickr web by api
        getData(page);

        // Set on scroll bar listener
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check condition when reach last item position, increase page size
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    // Show progress bar
                    progressBar.setVisibility(View.VISIBLE);
                    // Call for more data
                    getData(page);
                }
            }
        });
    }

    // Function that request data from flickr api of last upload images
    private void getData(int page) {
        // Instantiate the requestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // set req with necessary queries
        String url = String.format("https://api.flickr.com/services/rest/?api_key=%1$s" +
                        "&method=%2$s" +
                        "&extras=%3$s" +
                        "&format=%4$s" +
                        "&nojsoncallback=%5$s" +
                        "&page=%6$s" +
                        "&per_page=%7$s",
                "aabca25d8cd75f676d3a74a72dcebf21",
                "flickr.photos.getRecent",
                "url_s,description,date_upload,views,geo,tags,media",
                "json",
                "1",
                String.valueOf(page),
                "20"
                );

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { // Handle with response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // Get photo array from json object
                            JSONArray photo_parse_json  = jsonObject.getJSONObject("photos").getJSONArray("photo");
                            // Parse the array to necessary data
                            parseResponse(photo_parse_json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getCause().toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Function that parse input jsonArray and set MainData variables.
    private void parseResponse(JSONArray jsonArray) {

        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            try {
                // Initialize json object
               JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Initialize main data
                MainData data = new MainData();
                // Set image url variable
                data.setImage(jsonObject.getString("url_s"));
                // Set owner variable
                data.setOwner(jsonObject.getString("owner"));
                // Set title variable
                data.setTitle(jsonObject.getString("title"));
                // Set dateupload variable
                data.setDate_upload(jsonObject.getString("dateupload"));
                // Set description variable
                data.setDescription(jsonObject.getJSONObject("description").getString("_content"));
                // Set views variable
                data.setViews(jsonObject.getInt("views"));

                data.setHeight_s(jsonObject.getInt("height_s"));

                data.setWidth_s(jsonObject.getInt("width_s"));

                // Add data to the array list
                dataArrayList.add(data);

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error in json Object");
            }

            // Initialize adapter
            adapter = new MainAdapter(dataArrayList, MainActivity.this);
            // Set adapter to recycle view
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}