package edu.unc.cs.hdwhite.readroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    private String filename = "/collection";
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
        filename = getFilesDir().toString() + filename;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra("books")) {
            addBooks((ArrayList<Book>) getIntent().getSerializableExtra("books"));
        }
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

    private void addBooks(ArrayList<Book> newBooks) {
        for (Book b : newBooks) {
            books.add(b);
        }
    }

    public void readStorage(View v) {
        ArrayList<Book> newBooks = new ArrayList<Book>();
        try {
            FileInputStream in = new FileInputStream(filename);
            ObjectInputStream objIn = new ObjectInputStream(in);
            newBooks = (ArrayList<Book>) objIn.readObject();
            objIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bookAdapter.clear();
        for (Book b : newBooks) {
            bookAdapter.add(b);
        }
    }

    public void writeStorage(View v) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(filename);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(books);
            objOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
