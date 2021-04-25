package com.evan.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ListDataActivity extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        mListView = findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        Map<Integer, Vector<String>> buffer = new HashMap();
        Vector<String> tmpVec = new Vector<String>();
        String tmp;

        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            tmp = data.getString(0);
            tmpVec.clear();
            tmpVec.add(data.getString(1));
            tmpVec.add(data.getString(2));
            buffer.put(Integer.parseInt(tmp),tmpVec);

            tmp = "ID: "+tmp+"\nUsername: "
                    + tmpVec.get(0)+"\nPassword: "
                    + tmpVec.get(1);

            listData.add(tmp);
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                int itemID = getId(name);
                Vector<String> tmpVec2 = new Vector<String>();

                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    tmpVec2 = buffer.get(itemID);
                    editScreenIntent.putExtra("name",tmpVec2.get(0));
                    editScreenIntent.putExtra("pass",tmpVec2.get(1));
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    private int getId(String str){
        String res = "-1";
        char ch;
        int i = 4;

        while (true) {
            ch = str.charAt(i);
            if(ch=='\n') {
                res = str.substring(4,i);
                break;
            }
            ++i;
        }

        return Integer.parseInt(res);
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
