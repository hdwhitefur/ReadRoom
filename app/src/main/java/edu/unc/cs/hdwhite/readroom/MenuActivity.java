package edu.unc.cs.hdwhite.readroom;
//Hugh White 2015

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
            try {
                return connectDownload(new URL(urls[0]));
            } catch (Exception e) {
                Log.d(DT, "Caught exception");
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Book> connectDownload(URL url) throws IOException {
            InputStream in = null;
            try {
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
            Reader reader = new InputStreamReader(in, "UTF-8");
            String testChar = "" + (char) reader.read();
            for(int i = 0; i < 50000; i++){
                char current = (char) reader.read();
                if (current != 0) {
                    System.out.print(current);
                } else{
                    i = 50000;
                }
            }
            return null;
        }
    }
}
