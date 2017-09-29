package ua.com.bestjokes.bestjokesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressDialog pDialog;
    private ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> messagesList;

    private String url = "http://192.168.22.135:8080/BestJokesServer-1.0-SNAPSHOT/";
    private String uriGetAll = "getAllMsgs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMsgActivity.class);
                startActivity(intent);
                finish();
            }
        });

        messagesList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.chart_list);

        new GetContacts().execute();

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Read data from server...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + uriGetAll);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray messages = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject c = messages.getJSONObject(i);

                        String id = c.getString("id");
                        String message = c.getString("message");
                        String timestamp = c.getString("timestamp");

                        // tmp hash map for single contact
                        HashMap<String, String> msgFromJson = new HashMap<>();

                        // adding each child node to HashMap key => value
                        msgFromJson.put("id", "#" + id + ":");
                        msgFromJson.put("message", message);
                        msgFromJson.put("timestamp", Helper.getDate(Long.valueOf(timestamp)));

                        // adding contact to contact list
                        messagesList.add(msgFromJson);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, messagesList,
                    R.layout.list_item, new String[]{"id", "message",
                    "timestamp"}, new int[]{R.id.id,
                    R.id.message, R.id.timestamp});

            listView.setAdapter(adapter);
        }
    }
}
