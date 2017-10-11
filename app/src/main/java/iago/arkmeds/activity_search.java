package iago.arkmeds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class activity_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
    public final static String EXTRA_MESSAGE = ".iago.arkmeds.MESSAGE";

    public void startQuery(View view){
        Intent intent = new Intent(this, activity_MovieList.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String queryText = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, queryText);
        startActivity(intent);
    }
}
