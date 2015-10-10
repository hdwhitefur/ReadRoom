package edu.unc.cs.hdwhite.readroom;
//Hugh White 2015

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private final String DT = "Debug";
    private final String API_KEY = "AIzaSyBoAMsaRVBfRj_lrPyPD-CGq8k9sXdomjM";
    ArrayList<Book> queriedBooks = new ArrayList<Book>();
    ArrayAdapter<Book> bookAdapter;
    ListView resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bookAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, queriedBooks);
        resultList = (ListView) findViewById(R.id.resultList);
        resultList.setAdapter(bookAdapter);
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
        String request = "https://www.googleapis.com/books/v1/volumes?q="
                + query + "&key=" + API_KEY;
        Log.d(DT, "Request " + request);
        new downloadBookInfo().execute(request);

    }

    private class downloadBookInfo extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            Log.d(DT, "Starting background task");
            try {
                return connectDownload(new URL(urls[0]));
            } catch (Exception e) {
                Log.d(DT, "Caught exception");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            for (Book b : books) {
                bookAdapter.add(b);
            }
        }

        private ArrayList<Book> connectDownload(URL url) throws IOException {
            InputStream in = null;
            try {
                Log.d(DT, "Starting connection");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DT, "Connected with response " + response);
                in = conn.getInputStream();
                return processInputStream(in);
            } finally {
                in.close();
            }
        }

        private ArrayList<Book> processInputStream(InputStream in) throws IOException {
            String output = "";
            Reader reader = new InputStreamReader(in, "UTF-8");
            char currChar = (char) reader.read();
            while (currChar != 65535) {
                output = output + currChar;
                currChar = (char) reader.read();
            }
            Log.d(DT, "Output created");
            try {
                return processJSONFromString(output);
            } catch (Exception e) {
                Log.d(DT, "Caught exception");
                e.printStackTrace();
            }
            return null;
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
                Log.d(DT, "New book: " + books.get(i).toString());
            }
            return books;
        }
    }
}
