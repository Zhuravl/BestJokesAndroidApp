package ua.com.bestjokes.bestjokesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMsgActivity extends AppCompatActivity {

    private String url = "http://192.168.22.135:8080/BestJokesServer-1.0-SNAPSHOT/";
    private String uriAdd = "addMsg";
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addBtn = (Button) findViewById(R.id.addButton);
        addBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.newMsgField);
                text = editText.getText().toString();

                editText.getText().clear();


                new PostMsg().execute();
                Snackbar.make(v, "Your message has been pushed to the server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(AddMsgActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private class PostMsg extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler handler = new HttpHandler();
            handler.makeServiceCall(url + uriAdd + "?message=" + text);

            return null;
        }
    }
}
