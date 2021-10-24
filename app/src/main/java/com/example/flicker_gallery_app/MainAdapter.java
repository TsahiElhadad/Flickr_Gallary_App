package com.example.flicker_gallery_app;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;

// Main adapter class for RecyclerView
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    // Initialize variables
    private ArrayList<MainData> dataArrayList;
    private Activity activity;

    // Constructor
    public MainAdapter(ArrayList<MainData> dataArrayList, Activity activity) {
        this.dataArrayList = dataArrayList;
        this.activity = activity;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize item View and bind it to ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(view);
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        // Initialize main data
        MainData mainData = dataArrayList.get(position);
        // Load and set image on image view
        Glide.with(activity).load(mainData.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        // For opening second activity when click on image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), SecondActivity.class);
                // Add data to intent for second activity
                intent.putExtra("image", mainData.getImage());
                intent.putExtra("owner", mainData.getOwner());
                intent.putExtra("date_upload", mainData.getDate_upload());
                intent.putExtra("title", mainData.getTitle());
                intent.putExtra("description", mainData.getDescription());
                intent.putExtra("views", mainData.getViews());
                intent.putExtra("height", mainData.getHeight_s());
                intent.putExtra("width", mainData.getWidth_s());

                activity.startActivity(intent); // start Intent
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Initialize variable
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get id of image_view
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
