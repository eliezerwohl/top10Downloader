package com.example.eliezerwohl.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;
    private int currentId;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("DATA_URL", feedUrl);
        outState.putInt("DATA_LIMIT", feedLimit);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        feedUrl = savedInstanceState.getString("DATA_URL");
        feedLimit = savedInstanceState.getInt("DATA_LIMIT");
        downloadUrl(String.format(feedUrl, feedLimit));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.XmlListView);
        // downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        downloadUrl(String.format(feedUrl, feedLimit));

    }

    @Override
    public void supportInvalidateOptionsMenu() {
        super.supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        if (feedLimit == 10) {
            menu.findItem(R.id.mnu10).setChecked(true);
        } else {
            menu.findItem(R.id.mnu25).setCheckable(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (currentId == id) {
            Log.d(TAG, "nothing");
            return true;
        } else {
            Log.d(TAG, "it's new");
            currentId = id;
            switch (id) {
                case R.id.mnuFree:
                    feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                    break;
                case R.id.mnuPaid:
                    feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                    break;
                case R.id.mnuSongs:
                    feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                    break;
                case R.id.mnu10:
                case R.id.mnu25:
                    if (!item.isChecked()) {
                        item.setChecked(true);
                        feedLimit = 35 - feedLimit;
                        Log.d("on optionItemSeleted:", item.getTitle() + " setting feedLimit to " + feedLimit);
                    } else {
                        Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + "feedLimit unchaged");
                    }
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
            downloadUrl(String.format(feedUrl, feedLimit));
            return true;
//        return super.onOptionsItemSelected(item);
        }

    }

    private void downloadUrl(String feedUrl) {
        Log.d(TAG, "downloadUrl: starting aysnc");
        DownloadData downloadData = new DownloadData();
        downloadData.execute(feedUrl);
        Log.d(TAG, "downloadUrl: done");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "OnPostExecute: parameter is " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);
            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record,
                    parseApplications.getApplications());
            listApps.setAdapter(feedAdapter);
//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
//                    MainActivity.this, R.layout.list_item, parseApplications.getApplications()
//            );
//            listApps.setAdapter(arrayAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "DoInbackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInbackground: error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: the response code was " + response);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "DOWNLOAD XML: INvalid URL" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "DOWNLOAD XML: IO Exepction reading data" + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "DOWNLOAD XML: security exception, need permission" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }
}
