package com.wrdijkstra.wrdcontentuser;

import com.wrdijkstra.wrdcontentuser.WrdContentContract;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContentValues counter = new ContentValues();
        ContentValues counterUpdate = new ContentValues();
        Uri uri;
        String uriType;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver resolver = getContentResolver();

//        counter.put(WrdContentContract.Counters._ID, 54 );
//        counter.put(WrdContentContract.Counters.LABEL, "jo!" );
//        counter.put(WrdContentContract.Counters.LOCKED, "true" );
//        counter.put(WrdContentContract.Counters._COUNT, 112 );
//        resolver.insert(WrdContentContract.Counters.CONTENT_URI, counter);
//
//        counter.put(WrdContentContract.Counters._ID, 13 );
//        counter.put(WrdContentContract.Counters.LABEL, "huh?" );
//        counter.put(WrdContentContract.Counters.LOCKED, "true" );
//        counter.put(WrdContentContract.Counters._COUNT, 97 );
//        resolver.insert(WrdContentContract.Counters.CONTENT_URI, counter);
//
//        counterUpdate.put(WrdContentContract.Counters._ID, 13 );
//        counterUpdate.put(WrdContentContract.Counters._COUNT, 117 );
//        resolver.update(WrdContentContract.Counters.CONTENT_URI, counterUpdate, WrdContentContract.Counters._ID + "=?", new String[]{counterUpdate.getAsString(WrdContentContract.Counters._ID)});

        uri = WrdContentContract.Counters.CONTENT_URI;
        Log.d( "QUERY", "uri = " + uri.toString() );
        uriType = resolver.getType(WrdContentContract.Counters.CONTENT_URI);
        Log.d( "QUERY", "uri type = " + uriType );
        Cursor cursor  = resolver.query(WrdContentContract.Counters.CONTENT_URI, null, null, null, WrdContentContract.Counters.SORT_ORDER_DEFAULT );
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(WrdContentContract.Counters._ID));
                String label = cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LABEL));
                String locked = cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LOCKED));
                int count = cursor.getInt(cursor.getColumnIndex(WrdContentContract.Counters._COUNT));

                Log.d( "QUERY", "id = " + Integer.toString(id) + ", label = " + label + ", locked = " + locked + ", count = " + Integer.toString(count));
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
}
