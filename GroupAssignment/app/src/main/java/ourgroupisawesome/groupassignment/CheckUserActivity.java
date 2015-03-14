package ourgroupisawesome.groupassignment;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;


public class CheckUserActivity extends Activity {

    public TextView playername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        playername = (TextView) findViewById(R.id.shit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_user, menu);
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
        CheckUser checkuser = new CheckUser();
        checkuser.execute();}

    public class CheckUser extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://samuel:annette@ds043971.mongolab.com:43971/locations6179");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection user = db.getCollection("GroupProj");
                /*List<DBObject> myList = null;*/
                DBCursor results = user.find(new BasicDBObject("name", "sam"));
                /*myList = results.toArray();*/
                if (results.hasNext()) {
                    client.close();
                    return "Player name already exists, Please choose another.";
                }
                else {
                    BasicDBObject insertdata = new BasicDBObject("name", "sam");
                    user.insert(insertdata);
                    client.close();
                }
                return "Your player name has been set";

            } catch (UnknownHostException e) {
                return "Unknown Host Exception";
            }

        }
        @Override
        protected void onPostExecute(String result) {
            playername.setText(result);
        }
    }

}
