package com.wrdijkstra.wrdcontentuser2;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends ListActivity {

    private boolean updateLabel = false;
    private EditText etId;
    private EditText etLabel;
    private Button   btUpdate;
    private ListView lvDbContent;
    private ArrayList<String> results = new ArrayList<>();
    private ContentObserver contentObserver = new ContentObserver(null) {
                                                    @Override
                                                    public void onChange(final boolean selfChange) {
                                                        super.onChange(selfChange);

                                                        getListView().post(new Runnable() {
                                                            public void run() {
                                                                openAndQueryDatabase();
                                                                displayResultList();
                                                                updateLabelText();
                                                            }
                                                        });
                                                    }
                                                };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvDbContent = (ListView)findViewById(android.R.id.list);
        lvDbContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                               public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                                                   String item = (String)adapter.getItemAtPosition(position);
                                                   Matcher matcher = Pattern.compile("\\d+").matcher(item);
                                                   matcher.find();
                                                   int entryId = Integer.valueOf(matcher.group());

                                                   if (isIdValid(entryId)==true) {
                                                       String entryLabel = getLabel(entryId);
                                                       EditText etId = (EditText)findViewById(R.id.etId);
                                                       EditText etLabel = (EditText)findViewById(R.id.etLabel);

                                                       etId.setText(String.valueOf(entryId));
                                                       etLabel.setText(entryLabel);
                                                   }
                                               }
                                           });

        etId = (EditText)findViewById(R.id.etId);
        etLabel = (EditText)findViewById(R.id.etLabel);
        btUpdate = (Button)findViewById(R.id.btUpdate);
        etId.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            btUpdate.setEnabled(s.toString().trim().length()!=0);
                                            if (s.toString().trim().length()==0) {
                                                btUpdate.setEnabled(false);

                                                if (updateLabel == true) {
                                                    etLabel.setText("");
                                                }
                                            }
                                            else {
                                                btUpdate.setEnabled(true);
                                                updateLabelText();
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

        etId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View v, boolean hasFocus) {
                                                updateLabel = false;
                                            }
                                        });

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
            results.clear();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(WrdContentContract.Counters._ID));
                String label = cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LABEL));
                String locked = cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LOCKED));
                int count = cursor.getInt(cursor.getColumnIndex(WrdContentContract.Counters._COUNT));

                results.add("[" + Integer.toString(id) + "] "+ label + ", " + locked + ", " + Integer.toString(count));
            } while (cursor.moveToNext());
        }

        resolver.registerContentObserver(WrdContentContract.Counters.CONTENT_URI,false,contentObserver);
        cursor.close();
    }

    public void updateDatabase(View v) {
        EditText etId = (EditText)findViewById(R.id.etId);
        EditText etLabel = (EditText)findViewById(R.id.etLabel);
        int id = Integer.parseInt(etId.getText().toString());
        String label = etLabel.getText().toString();
        ContentValues updateEntry = new ContentValues();

        updateEntry.put(WrdContentContract.Counters._ID,id);
        updateEntry.put(WrdContentContract.Counters.LABEL,label);
        if (isIdValid(id)==true) {
            getContentResolver().update(WrdContentContract.Counters.CONTENT_URI, updateEntry, WrdContentContract.Counters._ID + " = ?", new String[]{String.valueOf(id)});
        }
        else {
            getContentResolver().insert(WrdContentContract.Counters.CONTENT_URI, updateEntry);
        }

        etId.setText("");
        etLabel.setText("");
    }

    private void updateLabelText() {
        // Update label field, depending on ID field
        if (etId.getText().toString().trim().length()==0) {
            // ID field empty ->  clear label field if pre-filled before
            if (updateLabel == true) {
                etLabel.setText("");
            }
        }
        else {
            // Update label field only if empty, or if pre-filled before
            if ((etLabel.getText().toString().trim().length() == 0) || (updateLabel == true)) {
                int id = Integer.parseInt(etId.getText().toString());
                updateLabel = true;
                if (isIdValid(id) == true) {
                    // Valid ID -> display corresponding Label
                    etLabel.setText(getLabel(id));
                }
                else {
                    // Invalid ID -> clear pre-filled Label field
                    etLabel.setText("");
                }
            }
        }
    }

    private boolean isIdValid ( int id ) {
        Cursor cursor = getContentResolver().query(WrdContentContract.Counters.CONTENT_URI, null, WrdContentContract.Counters._ID + " = ?", new String[]{String.valueOf(id)}, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    private String getLabel ( int id ) {
        Cursor cursor = getContentResolver().query(WrdContentContract.Counters.CONTENT_URI, null, WrdContentContract.Counters._ID + " = ?", new String[]{String.valueOf(id)}, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(WrdContentContract.Counters.LABEL));
        }
        else {
            return null;
        }
    }
}

