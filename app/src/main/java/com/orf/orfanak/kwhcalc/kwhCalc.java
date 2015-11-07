package com.orf.orfanak.kwhcalc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class kwhCalc extends ActionBarActivity {

    float fWatts=0;
    float fHours=0;
    float kwh;
    double daily_cost;
    float kwh_cost;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kwh_calc);

        //Handling Ads
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view) {

                        //Obtaining the type of the electrical device
                        EditText device = (EditText) findViewById(R.id.editText);
                        String string_device = device.getText().toString();

                        // Obtaining Watts of the electrical device as provided by user
                        EditText text_watts = (EditText) findViewById(R.id.editText2);
                        String string_watts = text_watts.getText().toString();

                        /*
                        Turning watts string to integer
                        catch an error if something goes wrong
                        */
                        try {
                            fWatts = Float.parseFloat(string_watts);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Could not parse ERROR " + nfe);
                        }
                        Log.i("The value of fWatts", string_watts);

                        // Obtaining hours that the electrical device functions per day as provided by user
                        EditText text_hours = (EditText) findViewById(R.id.editText4);
                        String string_hours = text_hours.getText().toString();

                        /*
                        Turning hours string to float
                        catch an error if something goes wrong
                        */
                        try {
                            fHours = Float.parseFloat(string_hours);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Could not parse ERROR" + nfe);
                        }
                        Log.i("The value of fHours", string_hours);

                        //Calculating kWh
                        kwh = (float) (fWatts / 1000.0 * fHours);

                        //Value of kwh presented to user on screen
                        TextView textV_kwh = (TextView) findViewById(R.id.textView6);
                        String text_kwh = Float.toString(kwh);
                        textV_kwh.setText((text_kwh+"kwh"));

                        //Obtaining the cost of kwh as provided by user
                        EditText text_kwh_cost = (EditText) findViewById(R.id.editText3);
                        String string_kwh_cost = text_kwh_cost.getText().toString();


                        /*
                        Turning kwh cost to float
                        catch an error if something goes wrong
                        */
                        try {
                            kwh_cost = Float.parseFloat(string_kwh_cost);

                        } catch (NumberFormatException nfe) {
                            System.out.println("Could not parse ERROR" + nfe);
                        }
                        Log.i("The value of kwh_cost", string_kwh_cost);


                        //Calculating daily cost in euros
                        daily_cost = (double) (kwh * kwh_cost);
                        String string_daily_cost = String.format("%.02f", daily_cost);

                        //Daily cost in euros presented to user on screen
                        TextView textV_dailyCost = (TextView) findViewById(R.id.textView8);
                        textV_dailyCost.setText(NumberFormat.getCurrencyInstance().format(daily_cost));
                        //textV_dailyCost.setText((string_daily_cost+"€"));


                        //String to be written in file output

                            String dataOutput = string_device + ";" + string_watts + ";" + string_hours + ";" + string_kwh_cost + ";" + text_kwh + ";" + string_daily_cost + ";";

                        //Create a stream and a file to save data in

                        FileOutputStream fOut=null;

                            File myFile = new File("/sdcard/kwh.txt");
                        try {
                            myFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            fOut = new FileOutputStream(myFile,true);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        OutputStreamWriter osw = new OutputStreamWriter(fOut);

                            /*
                            writing to file with the following format
                            device;watts;hours;cost of kwh;kwh per day;daily cost
                            */
                            try {
                                try {
                                    osw.append(dataOutput);
                                    osw.append("\n\r");
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                osw.flush();
                                osw.close();
                                Toast.makeText(getBaseContext(),"Data Saved to kwh.txt", Toast.LENGTH_LONG).show();
                            } catch (IOException e){
                                e.printStackTrace();
                            }

                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kwh_calc, menu);
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
