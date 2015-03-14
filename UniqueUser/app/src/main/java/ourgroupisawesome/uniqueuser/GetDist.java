package ourgroupisawesome.uniqueuser;

import android.app.Activity;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class GetDist extends Activity {
    public Location locationA;
    public Location locationB;
    public static double distanceh;
    public static double distances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_dist);


        Location dista = new Location("start");
        dista.setLatitude(47.000000);
        dista.setLongitude(-122.000000);

        Location distb = new Location("start");
        distb.setLatitude(48.000000);
        distb.setLongitude(-122.000000);

        distanceh = dista.distanceTo(distb);

        Location distc = new Location("start");
        distc.setLatitude(47.000000);
        distc.setLongitude(-122.000000);

        Location distd = new Location("start");
        distd.setLatitude(48.000000);
        distd.setLongitude(-123.000000);

        distances = distc.distanceTo(distd);

    if (distanceh < distances)
            Toast.makeText(getApplicationContext(), "yup", Toast.LENGTH_LONG).show();
    else
        Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_dist, menu);
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


}
