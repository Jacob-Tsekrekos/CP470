package com.example.androidassignments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTestToolbarBinding binding;

    protected static final String ACTIVITY_NAME = "Toolbar";
    protected String snack = "Something that you have written ;)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_toolbar);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, snack, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem mi){
        int itemId = mi.getItemId();

        switch(itemId){
            case R.id.action_1:
                Log.d(ACTIVITY_NAME, "Option 1 was selected");

                Snackbar.make(binding.fab, "You selected item 1", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.action_2:
                Log.d(ACTIVITY_NAME, "Option 2 was selected");

                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.go_back);
                    // Add the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.action_3:
                Log.d(ACTIVITY_NAME, "Option 3 was selected");
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    LayoutInflater inflater = getLayoutInflater();

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_signin, null);
                    builder.setView(layout);
                    // Add action buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // set the Snackbar message
                            EditText user_input = layout.findViewById(R.id.dialog_EditText);
                            snack = user_input.getText().toString();
                            Log.i(ACTIVITY_NAME, "Setting snack to '" + snack + "'");
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // LoginDialogFragment.this.getDialog().cancel();
                            // do nothing
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.action_4:
                Toast.makeText(this, "Version 1.0, by Jacob Tsekrekos", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_toolbar);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}