package com.wrdijkstra.wrdcontentuser2;

import com.wrdijkstra.wrdcontentuser2.WrdContentContract;


import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    private ArrayList<String> results = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openAndQueryDatabase();
        displayResultList();
    }

    private void displayResultList() {
        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, results));
        getListView().setTextFilterEnabled(true);

    }

    private void openAndQueryDatabase() {
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(WrdContentContract.Counters.CONTENT_URI, null, null, null, WrdContentContract.Counters.SORT_ORDER_DEFAULT);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(WrdContentContract.Counters._ID));
                String label = cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LABEL));
                String locked = cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LOCKED));
                int count = cursor.getInt(cursor.getColumnIndex(WrdContentContract.Counters._COUNT));

                results.add("[" + Integer.toString(id) + "] "+ label + ", " + locked + ", " + Integer.toString(count));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}

