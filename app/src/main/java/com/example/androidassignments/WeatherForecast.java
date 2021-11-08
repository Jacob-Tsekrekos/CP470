package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {

    protected TextView currTempText;
    protected TextView minTempText;
    protected TextView maxTempText;
    protected TextView cityText;
    protected ProgressBar progressBar;
    protected ImageView weatherImage;
    public static final String ACTIVITY_NAME = "WeatherForecast";
    protected String city = "ottawa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        currTempText = findViewById(R.id.currTempText);
        minTempText = findViewById(R.id.minTempText);
        maxTempText = findViewById(R.id.maxTempText);
        cityText = findViewById(R.id.cityText);

//        city = "waterloo";
        setCityText();

        weatherImage = findViewById(R.id.weatherImage);

        ForecastQuery fq = new ForecastQuery();
        fq.execute();
    }

    private void setCityText(){
        String capitalized = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
        String weather_in = getString(R.string.weather_in);
        String city_part = weather_in + " " + capitalized;
        cityText.setText(city_part);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        protected final String weather_uri =
                "https://api.openweathermap.org/data/2.5/weather?" +
                        "q=%s,ca&APPID=ac2473659cc3d6a85073e07ad24e589d&mode=xml&units=metric";
        public String min;
        public String max;
        public String curr;

        public Bitmap picture;

        protected String doInBackground(String ... args){
            InputStream in = null;
            XmlPullParser parser = Xml.newPullParser();
            try {
                // Get connection to uri
                String weather_uri_2 = String.format(weather_uri, city);
                in = getConnection(weather_uri_2);

                // SET UP PARSER
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);

                parseXml(parser);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (in != null)
                        in.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

	        return "";
        }

        private InputStream getConnection(String url) {
            InputStream in = null;
            try {
                URL uri = new URL(url);
                HttpsURLConnection conn = (HttpsURLConnection) uri.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                in = conn.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return in;
        }

        public void parseXml(XmlPullParser parser) throws IOException, XmlPullParserException {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() == XmlPullParser.START_TAG){
                    if (parser.getName().equals("temperature")){
                        curr = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    } else if (parser.getName().equals("weather")){
                        String iconName =  parser.getAttributeValue(null, "icon");
                        String fileName = iconName + ".png";

                        File file = getBaseContext().getFileStreamPath(fileName);
                        if (!file.exists()){ // download the icon from the url
                            Log.i(ACTIVITY_NAME, "'"+fileName + "' was not found. Downloading...");
                            String iconUrl =  "https://openweathermap.org/img/w/" + fileName;
                            picture = downloadIcon(iconUrl, fileName);
                            saveIcon(picture, fileName); // throws ioexception, may not close file??

                        } else { // load the saved icon
                            Log.i(ACTIVITY_NAME, "'" + fileName + "' was found. Loading from local storage.");
                            FileInputStream fh = openFileInput(fileName);
                            picture = BitmapFactory.decodeStream(fh);
                        }
                        publishProgress(100);
                    }
                }
                parser.next();
            }
        }

        public void saveIcon(Bitmap icon, String fileName) throws IOException {
            FileOutputStream outputStream = openFileOutput( fileName, Context.MODE_PRIVATE);
            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }

        public Bitmap downloadIcon (String url_path, String fileName) {
            HttpsURLConnection connection = null;
            Bitmap p = null;
            try {
                URL url = new URL(url_path);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == 200)
                    p = BitmapFactory.decodeStream(connection.getInputStream());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return p;
        }

        @Override
        protected void onPostExecute(String a){
            progressBar.setVisibility(View.INVISIBLE);
            weatherImage.setImageBitmap(picture);
            currTempText.setText(curr + "\u00b0C");
            minTempText.setText(min + "\u00b0C");
            maxTempText.setText(max + "\u00b0C");
        }
        @Override
        protected void onProgressUpdate(Integer... value){
            progressBar.setProgress(value[0]);
        }
    }
}