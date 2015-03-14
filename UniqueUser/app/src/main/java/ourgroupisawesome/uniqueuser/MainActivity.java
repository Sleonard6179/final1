package ourgroupisawesome.uniqueuser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.w3c.dom.Text;
import java.net.UnknownHostException;


public class MainActivity extends Activity
        implements ConnectionCallbacks, OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "My freaking App";
    public String Blah;
    public String pName;
    public String pName3;
    public String pName4;
    public TextView textview;
    public TextView textView2;
    public String playername_text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        textView2 = (TextView) findViewById(R.id.GameStatusText);



        if (pName3 != null) {
        pName3 = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("name", "defaultStringIfNothingFound");
        pName4 = String.valueOf(pName3);
        TextView textView = (TextView) findViewById(R.id.playername_text);
        textView.setText(pName4);
        }
        else {
            TextView textView = (TextView) findViewById(R.id.playername_text);
            textView.setText("Shit");

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("   Welcome to: " +
                    " \n !...The Game...!");
            alert.setMessage("Enter Player Name");
// Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Editable pName = input.getText();
                    Blah = String.valueOf(pName);
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("name", Blah).commit();
        }
        Checkstatus checkStatus = new Checkstatus();
        checkStatus.execute();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setHideLoc(View view) {
        CheckUser checkuser = new CheckUser();
        checkuser.execute();}

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

    public void Getdist(View view) {
        Intent intent = new Intent(this, GetDist.class);
        startActivity(intent);
    }

    public class CheckUser extends AsyncTask<Void, Text, String> {
        public String doInBackground(Void...voids ) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://samuel:annette@ds043971.mongolab.com:43971/locations6179");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection GroupProject = db.getCollection("uniqueuser");


                    BasicDBObject insertdata = new BasicDBObject("name", pName4)
                            .append("account", "user")
                            .append("gamestatus", "R")
                            .append("dist", 1234)
                            .append("result", "hotter")
                            .append("xy", "xy");
                    GroupProject.insert(insertdata);


                    client.close();

                    return "everything worked";
            } catch (UnknownHostException e) {
                return "it didn't work";
            }

        }
    }



   public class Checkstatus extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://samuel:annette@ds043971.mongolab.com:43971/locations6179");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection user = db.getCollection("uniqueuser");
                /*List<DBObject> myList = null;*/
                DBCursor results = user.find(new BasicDBObject("Game_status", "inPlay"));
                /*myList = results.toArray();*/
                if (results.count() > 0) {

                    client.close();
                    return "Game in Play";
                }
                else {
                    /*BasicDBObject insertdata = new BasicDBObject("name", "sam");
                    user.insert(insertdata);
                    client.close();*/
                }
                return "Start new Game";

            } catch (UnknownHostException e) {
                return "it didn't work";
            }

        }
        @Override
        protected void onPostExecute(String result) {
            textView2.setText(result);
        }
    }
}