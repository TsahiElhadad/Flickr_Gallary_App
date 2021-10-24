package com.example.flicker_gallery_app;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.Calendar;

// Selected image page
public class SecondActivity extends AppCompatActivity {
    ImageView selected_Image;
    ImageView exit_button;
    TextView selected_Title;
    TextView selected_Owner;
    TextView selected_Description;
    TextView selected_Views;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // Set on click listener for exit button to return main activity
        exit_button = (ImageView)findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Get image view by id
        selected_Image = (ImageView) findViewById(R.id.selectedImage);
        // Get text views by id
        selected_Title =(TextView) findViewById(R.id.title_text);
        selected_Owner = (TextView) findViewById(R.id.owner_text);
        selected_Description = (TextView) findViewById(R.id.description_text);
        selected_Views = (TextView) findViewById(R.id.views_text);

        // Get Intent which was set from main adapter
        Intent intent = getIntent();
        String image_url;
        Bundle extras = intent.getExtras();

        // Check if intent from first activity get error
        if(extras == null) {
            System.out.println("Error in intent get extras");
            image_url= null;
        } else {
            try {
                // Get source of height and width but they are too small
                // so prefer to display in full screen.
/*              int height = extras.getInt("height");
                int width = extras.getInt("width");
                selected_Image.getLayoutParams().height = height+300;
                selected_Image.getLayoutParams().width = width+300;*/

                // Get image url
                image_url = extras.getString("image");
                // Load image url into image view
                Glide.with(this).load(image_url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(selected_Image);
                // Get image title
                String title = extras.getString("title");
                int title_len = title.length();
                // Check for title length
                if (title_len == 0) {
                    selected_Title.setText("Untitled");
                } else if (title_len > 16) {
                    title = title.substring(0, 16);
                    title = title.concat("...");
                    selected_Title.setText(title);
                } else {
                    selected_Title.setText(title);
                }
                // Get image title
                String owner = extras.getString("owner");
                owner = "@" + owner;
                selected_Owner.setText(owner);
                // Get image description
                String description = extras.getString("description");
                // Check for description length
                if (description.length() > 23) {
                    description = description.substring(0, 23);
                    description = description.concat("...");
                }
                selected_Description.setText(description);
                // Get image description
                String date_upload = extras.getString("date_upload");
                Calendar mydate = Calendar.getInstance();
                mydate.setTimeInMillis(Long.parseLong(date_upload) * 1000);
                // Set format date
                selected_Views.setText(mydate.get(Calendar.DAY_OF_MONTH) + "." + mydate.get(Calendar.MONTH) + "." + mydate.get(Calendar.YEAR));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
