package edu.unc.cs.hdwhite.readroom;
//Hugh White 2015

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {
    private final String DT = "Debug";
    ArrayList<Book> queriedBooks = new ArrayList<Book>();
    ArrayAdapter<Book> bookAdapter;
    ListView resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        bookAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_multiple_choice, queriedBooks);
        resultList = (ListView) findViewById(R.id.resultList);
        resultList.setAdapter(bookAdapter);
        resultList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

    public void searchBook(View v) {
        Log.d(DT, "Beginning search");
        EditText queryText = ((EditText) findViewById(R.id.queryText));
        String query = queryText.getText().toString().replaceAll(" ", "+");
        Log.d(DT, "Searching for " + query);
        String request = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        Log.d(DT, "Request " + request);
        new downloadBookInfo().execute(request);
    }

    public void sendWishlist(View v) {
        ArrayList<Book> checkedBooks = getChecked();
        Intent intent = new Intent(this, WishlistActivity.class);
        intent.putExtra("books", checkedBooks);
        startActivity(intent);
    }

    public void sendCollection(View v) {
        ArrayList<Book> checkedBooks = getChecked();
        Intent intent = new Intent(this, CollectionActivity.class);
        intent.putExtra("books", checkedBooks);
        startActivity(intent);
    }

    public ArrayList<Book> getChecked() {
        ArrayList<Book> checkedBooks = new ArrayList<Book>();
        SparseBooleanArray checked = resultList.getCheckedItemPositions();
        for (int i = 0; i < resultList.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                checkedBooks.add(queriedBooks.get(i));
                resultList.setItemChecked(i, false);
            }
        }
        return checkedBooks;
    }

    private class downloadBookInfo extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            Log.d(DT, "Starting background task");
            try {
                return connectDownload(new URL(urls[0]));
            } catch (JSONException jse) {
                return null;
            } catch (Exception e) {
                Log.d(DT, "Caught exception");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books != null) {
                bookAdapter.clear();
                for (Book b : books) {
                    Log.d(DT, "Added " + b.toString());
                    bookAdapter.add(b);
                }
            }
        }

        private ArrayList<Book> connectDownload(URL url) throws IOException, JSONException {
            Log.d(DT, "Starting connection");
            URLConnection conn = url.openConnection();
            BufferedReader in2 = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String input;
            String output = "";
            while ((input = in2.readLine()) != null) output = output + input;
            return processJSONFromString(output);
        }

        private ArrayList<Book> processJSONFromString(String rawJSON) throws JSONException {
            ArrayList<Book> books = new ArrayList<Book>();
            JSONObject bookResults = new JSONObject(rawJSON);
            JSONArray JSONBooks = bookResults.getJSONArray("items");
            JSONObject activeBook;
            String title;
            String author;
            String dateString;
            int date = 0;
            JSONArray JSONAuthors;
            for (int i = 0; i < JSONBooks.length(); i++) {
                activeBook = JSONBooks.getJSONObject(i).getJSONObject("volumeInfo");

                title = activeBook.getString("title");
                if (activeBook.has("subtitle")) {
                    title = title + ": " + activeBook.getString("subtitle");
                }

                String rawAuthors = activeBook.getJSONArray("authors").toString();
                rawAuthors = rawAuthors.substring(2, rawAuthors.length() - 2);
                author = rawAuthors.replaceAll("\",\"", ", ");

                if (activeBook.has("publishedDate")) {
                    dateString = activeBook.getString("publishedDate").substring(0,4);
                    try {
                        date = Integer.parseInt(dateString);
                    } catch (NullPointerException e) {}
                }

                books.add(new Book(title, author, date));
            }
            return books;

        }

    }

}
