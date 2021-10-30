package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    protected static final String CLASS_NAME = "ChatDatabaseHelper";

    static protected int VERSION_NUM = 2;
    public static String DATABASE_NAME = "Messages.db";
    public static String TABLE_NAME = "MESSAGES";

    public static String KEY_ID = "id";
    public static String KEY_MESSAGE = "message";


    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*Create a table with:
         *  * a column `id` of auto-incrementing ints
         *  * a column `MESSAGE` as strings
         */
        Log.i(CLASS_NAME, "Calling onCreate");

        String create = "CREATE TABLE " + TABLE_NAME +
                " ( " + KEY_ID + " INTEGER PRIMARY KEY autoincrement, "
                + KEY_MESSAGE + " text not null);";
        // Autoincrement is only allowed on an INTEGER PRIMARY KEY
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion="
                                  + oldVersion + " newVersion=" + newVersion);
        Log.i(CLASS_NAME, "Upgrading database from version " + oldVersion
                                + " to " + newVersion);

        String cmd = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(cmd);
        onCreate(db);
    }
}
