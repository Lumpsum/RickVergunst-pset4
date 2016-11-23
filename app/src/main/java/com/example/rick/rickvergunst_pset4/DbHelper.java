package com.example.rick.rickvergunst_pset4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rick on 11/20/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    // Initialize the specific database information
    public static final String TableName = "Things";

    public static final String _ID = "_id";
    public static final String desc = "description";
    public static final String state = "state";

    static final String DbName = "ToDoThings";

    // Current version of the db
    static final int DbVersion = 2;

    // String to create the table
    private static final String createTable = "CREATE TABLE " + TableName + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + desc + " TEXT NOT NULL, " + state + " TEXT);";

    // Specific context of the activity where the helper is called
    public DbHelper (Context context) {
        super(context, DbName, null, DbVersion);
    }

    // Creates the db using the string
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    // Creates a new db if there is an update on the current db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table if exists " + TableName);
        onCreate(db);
    }
}


