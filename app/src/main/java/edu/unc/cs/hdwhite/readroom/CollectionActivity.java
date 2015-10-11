package edu.unc.cs.hdwhite.readroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    ArrayList<Book> books = new ArrayList<Book>();
    ArrayAdapter<Book> bookAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        bookAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, books);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bookAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addBooks((ArrayList<Book>) getIntent().getSerializableExtra("books"));
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

    public void addBooks(ArrayList<Book> newBooks) {
        for (Book b : newBooks) {
            books.add(b);
        }
    }


}
