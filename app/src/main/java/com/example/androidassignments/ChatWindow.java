package com.example.androidassignments;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatWindow extends AppCompatActivity {

    private ArrayList<String> messageList;
    protected static String ACTIVITY_NAME = "ChatWindow";

    private ListView listView;
    private EditText messageEditText;
    private Button sendButton;
    private ChatAdapter messageAdapter;

    private SQLiteDatabase msgDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Log.i(ACTIVITY_NAME, "in onCreate");

        String KEY_ID = ChatDatabaseHelper.KEY_ID;
        String KEY_MESSAGE = ChatDatabaseHelper.KEY_MESSAGE;
        String TABLE_NAME = ChatDatabaseHelper.TABLE_NAME;

        messageList = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.listView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String msg = messageEditText.getText().toString();
            messageList.add(msg);

            // todo: verify this works with auto-increment id
            ContentValues cVals = new ContentValues();
            cVals.put(KEY_MESSAGE, msg);

            msgDB.insert(TABLE_NAME, "NullPlaceHolder", cVals);

            messageAdapter.notifyDataSetChanged();
            messageEditText.setText("");
        });

        // Add sql db
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        msgDB = dbHelper.getWritableDatabase();

        String retrieve_query = "SELECT *" +
                                "FROM " + TABLE_NAME;
        Cursor cursor = msgDB.rawQuery(retrieve_query, null);

        int col_index = cursor.getColumnIndex(KEY_MESSAGE);

        // Now answer me this, WHY TF is this not like a python generator?!?!
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String msg = cursor.getString(col_index);
            messageList.add(msg);
            Log.i(ACTIVITY_NAME, msg);
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count =" + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column " + i + "'s name: " + cursor.getColumnName(i));
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        // OnDestroy doesn't save the data correctly
        super.onDestroy();
        msgDB.close();
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }
        // lmao wtf is this garbage
        @Override
        public int getCount(){
            return messageList.size();
        }

        @Override
        public String getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
}