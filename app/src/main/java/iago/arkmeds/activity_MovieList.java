package iago.arkmeds;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class activity_MovieList extends AppCompatActivity {


    private TextView resultDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__movie_list);
        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra(activity_search.EXTRA_MESSAGE);
        resultDisplay = (TextView) findViewById(R.id.textViewItem);
        new runInBackground().execute("http://www.omdbapi.com/?s=" + searchTerm +
                "&type=movie&r=json&apikey=422461bb");
    }


    //Conexão com servidor OMDBAPI que é feita em background
    // A URL passada como parâmetro define o tipo de busca, categoria para filme
    // tipo de resposta para JSON e chave de acesso
    public class runInBackground extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String currentLine = "";
                while ((currentLine = reader.readLine()) != null) {
                    buffer.append(currentLine);
                }
                return buffer.toString();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultDisplay.setText(result);
        }
    }
}