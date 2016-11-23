package com.example.rick.rickvergunst_pset4;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Initalize variables
    private DbManager dbManager;

    private ListView lv;
    private EditText et;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DbHelper._ID, DbHelper.desc, DbHelper.state
    };

    final int[] to = new int[] {
            R.id.id, R.id.desc, R.id.state
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiate the database manager and create a initial cursor
        dbManager = new DbManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        // Assign the edit text and list view
        lv = (ListView)findViewById(R.id.toDoList);
        et = (EditText)findViewById(R.id.editText);

        // Assign the adapter with the given cursor and list layout
        adapter = new SimpleCursorAdapter(this, R.layout.list_item_record, cursor, from, to, 0){
            @Override
            public View getView(int position, View conView, ViewGroup parent) {
                View view = super.getView(position, conView, parent);
                TextView stateText = (TextView) view.findViewById(R.id.state);
                if (stateText.getText().toString().equals("Done")) {
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                }
                return view;
            }
        };

        // Check whether the activity call came from an orientation change or restart
        if (savedInstanceState != null) {
            cursor = dbManager.fetch();
            adapter.changeCursor(cursor);

        }
        else {
            // If the adapter contains no data it will start with 3 items that function as a tutorial
            if (adapter.getCount() == 0) {
                String state = "Not Done";
                dbManager.insert("Click an item to change the state", state);
                dbManager.insert("Hold an item to delete it", state);
                dbManager.insert("Insert the description below and press the button to add an item", state);

                cursor = dbManager.fetch();
                adapter.changeCursor(cursor);
            }
        }

        // Set the adapter to the listview so the data gets passed to the listview
        lv.setAdapter(adapter);

        cursor = dbManager.fetch();
        adapter.changeCursor(cursor);

        // Create a listener for long clicks
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewID) {

                // It deletes the specific item and updates the listview
                dbManager.delete(viewID);

                Cursor cursor = dbManager.fetch();
                adapter.changeCursor(cursor);

                // Import to return because else it will register the regular click as well
                return true;
            }
        });

        // A listener for a click on an item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewID) {

                // Retrieves the information of the specific item
                TextView descTextView = (TextView) view.findViewById(R.id.desc);
                TextView stateTextView = (TextView) view.findViewById(R.id.state);

                String desc = descTextView.getText().toString();
                String state = stateTextView.getText().toString();

                // Check whether the item is currently checked as done or not done
                // Reverses the state and updates the background color
                if (state.equals("Not Done")) {
                    dbManager.update(viewID, desc, "Done");
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else {
                    dbManager.update(viewID, desc, "Not Done");
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }

                // Update the listview
                Cursor cursor = dbManager.fetch();
                adapter.changeCursor(cursor);
            }
        });

    }

    // Simple saved state that prevents the tutorial from being called if you change orientation with an empty to-do list
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("state", true);
    }

    // Handles the add button
    public void addToDo(View view) {

        // Retrieves the user input
        final String desc = et.getText().toString();
        final String state = "Not Done";

        // Inserts the information in the database
        dbManager.insert(desc, state);

        Cursor cursor = dbManager.fetch();
        adapter.changeCursor(cursor);

        // Clears the user input
        et.getText().clear();
    }

    // Clears the listview and database
    public void clearList(View view) {
        // Loop through the listview and delete every item from the db and lv
        for (int i = 0;i < lv.getCount(); i++) {
            View v = lv.getChildAt(i);
            TextView idText = (TextView) v.findViewById(R.id.id);
            long id = Long.parseLong(idText.getText().toString());
            dbManager.delete(id);
        }

        Cursor cursor = dbManager.fetch();
        adapter.changeCursor(cursor);
    }
}
