package com.example.amitgupta10.customapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.amitgupta10.customapp.View.CircularImageView;

/**
 * Created by amit.gupta10 on 25/02/17.
 */
public class MainActivity extends Activity {

    public final static String UserID  = "UserId";
    public final static String Title  = "Title";
    public final static String PicUri  = "PicUri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        CircularImageView circularImageView = (CircularImageView) findViewById(R.id.uploadPhoto);
        String picUri = getIntent().getExtras().getString(PicUri);
        circularImageView.setImageURI(Uri.parse(picUri));

        TextView userName = (TextView) findViewById(R.id.user_name);
        String title = getIntent().getExtras().getString(Title);
        userName.setText(title);

        TextView userId = (TextView) findViewById(R.id.pull_id_from_facebook);
        String id = getIntent().getExtras().getString(UserID);
        userId.setText(id);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
