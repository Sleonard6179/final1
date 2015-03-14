package ourgroupisawesome.groupassignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity
        implements ConnectionCallbacks, OnConnectionFailedListener {

    protected static final String TAG = "My freaking App";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected String mShare;
    private TextView textView;
    public String pName3;
    public String pName4;
    public String Blah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        buildGoogleApiClient();
        CheckUser2 checkuser = new CheckUser2();
        checkuser.execute();
        textView = (TextView) findViewById(R.id.myText);
        pName3 = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("name", "defaultStringIfNothingFound");
        pName4 = String.valueOf(pName3);
    }

    /*public void UserVerify(View view) {
        CheckUser2 checkuser = new CheckUser2();
        checkuser.execute();}*/

    public class CheckUser2 extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://samuel:annette@ds043971.mongolab.com:43971/locations6179");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection user = db.getCollection("uniqueuser");
                /*List<DBObject> myList = null;*/
                DBCursor results = user.find(new BasicDBObject("name", pName4));
                /*myList = results.toArray();*/
                if (results.count() < 1) {
                        DBCollection GroupProject = db.getCollection("uniqueuser");
                        BasicDBObject insertdata = new BasicDBObject("name", pName4)
                                .append("account", "user")
                                .append("gamestatus", "R")
                                .append("dist", 1234)
                                .append("result", "hotter")
                                .append("xy", "xy");
                        GroupProject.insert(insertdata);
                        client.close();
                        return String.valueOf(pName4) + "has Been Added";
                }
                else
                return " has been created";

            } catch (UnknownHostException e) {
                return "it didn't work";
            }

        }
        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
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
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            /*mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));*/
            /*mShare = "I'm totally hanging at " + String.valueOf(mLastLocation.getLatitude())
                    + " Degrees Latitude and " + String.valueOf(mLastLocation.getLongitude())
                    + " Degrees Longitude";*/
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
            /*mShare = "No Connection Bro ";*/
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

    public void CheckUser(View view) {
        Intent intent = new Intent(this, CheckUserActivity.class);
        startActivity(intent);
    }

    public void FoundYou(View view) {
        Intent intent = new Intent(this, FoundYou.class);
        startActivity(intent);
    }

    public void setHideLoc(View view) {
        PostLocation postlocation = new PostLocation();
        postlocation.execute();}

    public class PostLocation extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://samuel:annette@ds043971.mongolab.com:43971/locations6179");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection GroupProject = db.getCollection("GroupProj");

                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss MMM/dd/yyyy" );
                String now = time.format(new Date());
                SimpleDateFormat hideTime = new SimpleDateFormat(" EEE MMM dd ' at approximately ' hh:mm a " );
                String timeH = hideTime.format(new Date());

                if (mLastLocation != null) {
                    BasicDBObject LastLocation = new BasicDBObject();
                    LastLocation.put("hide_latitude", String.valueOf(mLastLocation.getLatitude()));
                    LastLocation.put("hide_longitude", String.valueOf(mLastLocation.getLongitude()));
                    LastLocation.put("Time", String.valueOf(timeH));
                    /*LastLocation.put("Time", String.valueOf(now));*/

                    GroupProject.insert(LastLocation);
                    client.close();

                    return "On" + LastLocation.put("Time", String.valueOf(timeH)) + ", 'USERx' just hid. "
                            +"\n" + "Go find them! ";

                } else {



                    client.close();
                    return "You have no XY turn your location on Brah!";
                }
            } catch (UnknownHostException e) {
                return "No body's home brah!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
          textView.setText(result);
        }

    }
}
