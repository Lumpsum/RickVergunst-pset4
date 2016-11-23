package com.example.rick.rickvergunst_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rick on 11/20/2016.
 */

public class DbManager {

    // Initialize the variables
    private DbHelper dbHelper;

    private Context context;

    private SQLiteDatabase db;

    // Assign the context
    public DbManager(Context c) {
        context = c;
    }

    // Creates a connection with the database
    public DbManager open() throws SQLException {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Closes the db
    public void close() {
        dbHelper.close();
    }

    // Function that handles insertion of new items
    public void insert(String desc, String state) {

        // Takes the given values and insert them in the database
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.desc, desc);
        contentValue.put(DbHelper.state, state);
        db.insert(DbHelper.TableName, null, contentValue);
    }

    // Cursor function to fetch all the elements that are inside the table
    public Cursor fetch() {

        // The asked columns
        String [] columns = new String[] {
                DbHelper._ID, DbHelper.desc, DbHelper.state
        };

        // Instance of the cursor
        Cursor cursor = db.query(DbHelper.TableName, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Function to update a certain record inside the table
    public int update(long _id, String desc, String state) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.desc, desc);
        contentValue.put(DbHelper.state, state);
        int i = db.update(DbHelper.TableName, contentValue, DbHelper._ID + " = " + _id, null);
        return i;
    }

    // Function to delete an element from the database
    public void delete(long _ID) {
        db.delete(DbHelper.TableName, DbHelper._ID + "=" + _ID, null);
    }

}
