package iago.arkmeds;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class activity_MovieList extends AppCompatActivity {


    private ListView completeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__movie_list);
        completeList = (ListView)findViewById(R.id.displayList);
        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra(activity_search.EXTRA_MESSAGE);
        new runInBackground().execute("http://www.omdbapi.com/?s=" + searchTerm +
                "&type=movie&r=json&apikey=422461bb");
    }


    //Conexão com servidor OMDBAPI que é feita em background
    // A URL passada como parâmetro define o tipo de busca, categoria para filme
    // tipo de resposta para JSON e chave de acesso
    public class runInBackground extends AsyncTask<String, String, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... urls) {
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
                JSONObject resultObject = new JSONObject(buffer.toString());
                JSONArray resultArray = resultObject.getJSONArray("Search");
                List<Movie> movieList = new ArrayList<>();
                for (int i=0; i<resultArray.length(); i++){
                    JSONObject movieObject = resultArray.getJSONObject(i);
                    Movie singleMovie = new Movie();
                    singleMovie.setName(movieObject.getString("Title"));
                    singleMovie.setYear(movieObject.getInt("Year"));
                    singleMovie.setImdbid(movieObject.getString("imdbID"));
                    movieList.add(singleMovie);
                }

                return movieList;

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
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
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            ArrayAdapter<Movie> adapter = new ArrayAdapter<Movie>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, result);
            completeList.setAdapter(adapter);


        }
    }
}