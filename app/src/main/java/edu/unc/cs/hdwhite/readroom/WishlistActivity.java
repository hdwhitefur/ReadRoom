package edu.unc.cs.hdwhite.readroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    ArrayList<Book> books = new ArrayList<Book>();
    ArrayAdapter<Book> bookAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        bookAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, books);
        listView = (ListView) findViewById(R.id.listView2);
        bookAdapter.setNotifyOnChange(true);
        listView.setAdapter(bookAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addBooks((ArrayList<Book>) getIntent().getSerializableExtra("books"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wishlist, menu);
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

    public void removeBooks(View v)
    {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        //ArrayList<Book> selected = new ArrayList<Book>();
        for(int i = 0; i < checked.size()+1; i++){
            if(checked.get(i)) {
                books.remove(i);
            }

        }
        /*for (Book b : selected) {
            Log.d(DT, b.toString());
        }
        return selected;8*/
    }
}
