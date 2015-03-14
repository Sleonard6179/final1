package ourgroupisawesome.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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

        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("name",Blah).commit();

        else {
        TextView textview = (TextView) findViewById(R.id.textView2);
        textview.setText("nope");
        }

        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("name",Blah).commit();