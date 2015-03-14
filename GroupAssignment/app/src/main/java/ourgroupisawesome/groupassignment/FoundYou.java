package ourgroupisawesome.groupassignment;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FoundYou extends Activity
        implements ConnectionCallbacks, OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    public TextView SearchText;
    protected Location sLastLocation;
    protected TextView sLatitudeText;
    protected TextView sLongitudeText;
    protected String sShare;
    protected static final String TAG = "My freaking App";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_you);
        buildGoogleApiClient();
        sLatitudeText = (TextView) findViewById((R.id.search_latitude_text));
        sLongitudeText = (TextView) findViewById((R.id.search_longitude_text));

        SearchText = (TextView) findViewById(R.id.Search_Text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_found_you, menu);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());


    }

    @Override
    public void onConnected(Bundle connectionHint) {
        sLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (sLastLocation != null) {
            sLatitudeText.setText(String.valueOf(sLastLocation.getLatitude()));
            sLongitudeText.setText(String.valueOf(sLastLocation.getLongitude()));
            sShare = "I'm totally hanging at " + String.valueOf(sLastLocation.getLatitude())
                    + " Degrees Latitude and " + String.valueOf(sLastLocation.getLongitude())
                    + " Degrees Longitude";
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
            sShare = "No Connection Bro ";
        }
    }

    public void Seek(View view) {
        SearchHere searchHere = new SearchHere();
        searchHere.execute();
    }

    public class SearchHere extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://samuel:annette@ds043971.mongolab.com:43971/locations6179");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection MyLatLong = db.getCollection("GroupProj");

                SimpleDateFormat hideTime = new SimpleDateFormat(" EEE MMM dd ' at approximately ' hh:mm a " );
                String timeS = hideTime.format(new Date());

                if (sLastLocation != null) {
                    BasicDBObject LastLocation = new BasicDBObject();
                    LastLocation.put("search_latitude", String.valueOf(sLastLocation.getLatitude()));
                    LastLocation.put("search_longitude", String.valueOf(sLastLocation.getLongitude()));
                    LastLocation.put("search_Time", String.valueOf(timeS));

                    MyLatLong.insert(LastLocation);

                    client.close();
                    return "On" + LastLocation.put("search_Time", String.valueOf(timeS))+ "\n" +" You searched the location "
                            + String.valueOf(sLastLocation.getLatitude())+ " degrees lat. and "
                            + String.valueOf(sLastLocation.getLongitude()) + " degrees long. for 'USERx'";
                } else {
                    client.close();
                    return "Check to see if you location is turned on.";
                }
            } catch (UnknownHostException e) {
                return "Unknown Host Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            SearchText.setText(result);
        }
    }
}
