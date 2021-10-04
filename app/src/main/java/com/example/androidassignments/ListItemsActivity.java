package com.example.androidassignments;
// Note to marker: I made it so the checkbox unchecks if cancel is
//                 pressed, I hope that's ok
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    ImageButton imageButton;
    Switch switch2;
    CheckBox checkBox2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, 1);

            } catch (ActivityNotFoundException e) {
                // display error state to the user
                Toast.makeText(this, "Camera was not able to be accessed",
                        Toast.LENGTH_SHORT).show();
            }

        });

        switch2 = (Switch) findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener((CompoundButton switchView, boolean isChecked) -> {
            CharSequence text = (isChecked)?"Switch is On": "Switch is off";
            int duration = (isChecked)? Toast.LENGTH_SHORT: Toast.LENGTH_LONG;

            Toast.makeText(this, text, duration).show();
        });

        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox2.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (!isChecked)
                return;
            AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml

                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Response", "Here is my response");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // todo: figure out wtf this code should be
                            checkBox2.toggle();

                        }
                    })
                    .show();
        }));
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
        Object data = intent.getExtras().get("data");
        if (requestCode == 1 && data != null)
            imageButton.setImageBitmap((Bitmap) data);
    }
}