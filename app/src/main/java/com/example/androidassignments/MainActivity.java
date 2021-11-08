package com.example.androidassignments;

// todo: set isChecked to false!
// todo: set image to be the one captured
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
            startActivityForResult(intent, 10);
        });

        Button chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Log.i(ACTIVITY_NAME, "User tapped 'Start Chat'");
            Intent intent = new Intent(MainActivity.this, ChatWindow.class);
            startActivity(intent);
        });

        Button toolButton = (Button) findViewById(R.id.toolButton);
        toolButton.setOnClickListener(v -> {
            Log.i(ACTIVITY_NAME, "User tapped 'Start Test Toolbar'");
            Intent intent = new Intent(MainActivity.this, TestToolbar.class);
            startActivity(intent);
        });

        Button forecastButton = (Button) findViewById(R.id.forecastButton);
        forecastButton.setOnClickListener(v->{
            Log.i(ACTIVITY_NAME, "User tapped 'Weather Forecast'");
            Intent intent = new Intent(MainActivity.this, WeatherForecast.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        String msg = "Returned to MainActivity.onActivityResult";
        if (requestCode == 10)
            Log.i(ACTIVITY_NAME, msg);

        if (resultCode == Activity.RESULT_OK){
            String messagePassed = intent.getStringExtra("Response");
            String msg1 = "ListItemsActivity Passed: " + messagePassed;
            Toast.makeText(this, msg1, Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}