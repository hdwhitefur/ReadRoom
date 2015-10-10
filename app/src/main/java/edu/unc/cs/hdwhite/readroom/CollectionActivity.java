package edu.unc.cs.hdwhite.readroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    ArrayAdapter strAdapter;
    String[] items= {"Moby Dick", "The Marvalous Adventures of Matthew", "Alien The Book, The Movie, The Book", "To Kill a Mockingbird"};;
    ListView resultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ArrayAdapter strAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        ListView resultList = (ListView) findViewById(R.id.resultList);
        resultList.setAdapter(strAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
