package edu.unc.cs.hdwhite.readroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
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

//woopsy
public class CollectionActivity extends AppCompatActivity {
    private final String DT = "Debug";
    private String filename = "/collection";
    ArrayList<Book> books = new ArrayList<Book>();
    ArrayAdapter<Book> bookAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        bookAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, books);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bookAdapter);
        bookAdapter.setNotifyOnChange(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        filename = getFilesDir().toString() + filename;
    }

    @Override
    protected void onResume() {
        super.onResume();
        readStorage();
        if (getIntent().hasExtra("books")) {
            Log.d(DT, "Adding books");
            addBooks((ArrayList<Book>) getIntent().getSerializableExtra("books"));
            getIntent().removeExtra("books");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeStorage();
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
        readStorage();
        for (Book b : newBooks) {
            bookAdapter.add(b);
        }
    }
    public void removeBooks(View v)
    {
        ArrayList<Book> toRemove = getChecked();
        for (int i = books.size() - 1; i >= 0; i--) {
            Book temp = books.get(i);
            if (toRemove.contains(temp)) {
                bookAdapter.remove(temp);
                toRemove.remove(temp);
            }
        }
    }

    public ArrayList<Book> getChecked() {
        ArrayList<Book> checkedBooks = new ArrayList<Book>();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                checkedBooks.add(books.get(i));
                listView.setItemChecked(i, false);
            }
        }
        return checkedBooks;
    }

    public void readStorage(View v) {
        readStorage();
    }

    public void readStorage() {
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
        writeStorage();
    }

    public void writeStorage() {
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
