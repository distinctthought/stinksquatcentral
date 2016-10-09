package com.example.woof.stinksquatcentral;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PastLaunches extends AppCompatActivity {
        // These ArrayLists will contain strings taken from the SpaceX website.
        // The strings specify past launch dates, customers, launch sites and vehicles.
        public ArrayList<String> dates = new ArrayList<String>();
        public ArrayList<String> customers = new ArrayList<String>();
        public static ArrayList<String> sites = new ArrayList<String>();
        public static ArrayList<String> vehicles = new ArrayList<String>();

        // Ignore this, pretty sure we can delete it. Will investigate further.
        public final static String RESULT = "com.example.woof.stinksquatcentral.RESULT";

        // When this activity (PastLaunches.java is a class that extends AppCompatActivity) starts...
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // Display past_launches.xml
            super.onCreate(savedInstanceState);
            setContentView(R.layout.past_launches);
            // Get information from the SpaceX website. Scroll down to find the pingSpaceX() function for more info.
            pingSpaceX();
            // Apparently in android, you can't connect to the net in the main thread.
            // A thread is basically a pathway for a process to take place.
            // If there are multiple threads, processes can occur simultaneously and independently.
            // For example, when you download a program, you can still use your computer for other stuff.
            // This is because the download is occurring in a separate thread.
            // BUT! Since we need the information from the pingSpaceX() function (which starts a new thread) before we can continue...
            // We have to make the main thread pause. Thread.sleep(500) makes it pause for 500 milliseconds.
            // Since the thread can be interrupted during the half second pause, it's necessary to nest the sleep inside a "try/catch" block.
            // "try/catch" is a system that allows your code to programatically handle errors that occur during computation.
            // In this case, all I have the program do is print the error message, but more robust options are possible.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Information taken from the SpaceX manifest needs to be parsed. Find the parseData() function for more info. (scroll down)
            parseData();
            //After data is parsed, access the "dates" TextField (whose id was defined in past_launches.xml) and set its text to the list of dates of past launches.
            //Note that the "dates" here refers to the TextView, not the ArrayList<String>
            TextView text0 = (TextView)findViewById(R.id.dates);
            String datd = "Date: ";
            //For each element of the ArrayList "dates", add the date to the string which will define the TextField text.
            //Note that the "dates" here refers to the ArrayList<String>, not the TextView. Confusing...sorry, I'll change one of the names later.
            for(int i = 0; i < dates.size(); i++) {
                datd = datd + "\r\n" + dates.get(i);
            }
            text0.setText(datd);

            //Now do the same thing with the "customers" TextView.
            TextView text1 = (TextView)findViewById(R.id.customers);
            String datc = "Customer: ";
            //Using "dates" as the size isn't by accident. Even future launches specify customers, so for just the past launches, we want to make sure we get the same number of customers as dates.
            for(int i = 0; i < dates.size(); i++) {
                datc = datc + "\r\n" + customers.get(i);
            }
            text1.setText(datc);
        }

        void pingSpaceX() {
            // PingSite() is the constructor for an inner class I made to reference the website in a new thread. Scroll down to find private class PingSite.
            new PingSite().execute();
        }

        // These are setter functions which just modify the ArrayList<Strings> declared as instance variables when called.
        void setDates(ArrayList<String> d) {
            dates = d;
        }

        void setCustomers(ArrayList<String> c) {
            customers = c;
        }

        void parseData() {
            //A temporary list.
            ArrayList<String> holder0 = new ArrayList<String>();
            int dSize = dates.size();
            //The lines read from the website are messy. Fo each line, eliminate all characters except for the dates themselves.
            for(int i = 0; i < dSize; i++) {
                String temp = dates.get(i);
                int endInd = temp.indexOf("</span>");
                temp = temp.substring(endInd-10, endInd);
                if(temp.contains(">")) {
                    temp = temp.substring(1,10);
                }
                if(temp.contains(">")) {
                    temp = temp.substring(1,9);
                }
                //System.out.println(temp);
                //Add the parsed line to the temporary list.
                holder0.add(temp);
            }
            //Set the real list equal to the temporary one.
            dates = holder0;

            //Follow the same procedure for the customers/launch sites/vehicles.
            ArrayList<String> holder1 = new ArrayList<String>();
            int cSize = customers.size();
            for(int i = 0; i < cSize; i++) {
                String temp = customers.get(i);
                int begInd = temp.indexOf("<span>");
                begInd = begInd + 6;
                int endInd = temp.indexOf("</span>");
                temp = temp.substring(begInd, endInd);
                //System.out.println(temp);
                holder1.add(temp);
            }
            customers = holder1;

            //The following code works, I just haven't used it yet.
            /*int sSize = sites.size();
            for(int i = 0; i < sSize; i++) {
                String temp = sites.get(i);
                int begInd = temp.indexOf("<span>");
                begInd = begInd + 6;
                int endInd = temp.indexOf("</span>");
                temp = temp.substring(begInd, endInd);
                //System.out.println(temp);
            }*/
        }

    //A class that extends the AsyncTask class starts its own thread.
    private class PingSite extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
        //doInBackground is a function of AsyncTask that must be overridden. It specifies what the second thread should do.
        protected ArrayList<String> doInBackground(ArrayList<String>... urls) {
            //The URL class treats a URL like a file.
            URL url;
            //The URL we're interested in the spacex manifest. Opening it needs to be nested in a try/catch statement because it's possible that opening the website will fail.
            try {
                url = new URL("http://www.spacex.com/missions");
                //A buffered reader can read information from a stream. In this case, the stream is the url.openStream()
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                //Keep reading information from the website until there's nothing left to read.
                String inLine;
                while ((inLine = in.readLine()) != null) {
                    //The key words like "date-display-single" are how the spacex manifest source code identifies dates/customers/launch sites/vehicles.
                    //If we see those key words, we know the information is important, so we save it to the proper list.
                    if (inLine.contains("date-display-single")) {
                        dates.add(inLine);
                    }
                    if (inLine.contains("td class=\"customer\"")) {
                        customers.add(inLine = in.readLine());
                    }
                    if (inLine.contains("td class=\"launch-site\"")) {
                        sites.add(inLine = in.readLine());
                    }
                    if (inLine.contains("td class=\"vehicle\"")) {
                        inLine = in.readLine();
                        inLine = in.readLine();
                        vehicles.add(inLine = in.readLine());
                    }
                }
                //Close the url stream. Always important to do!
                in.close();

            // Catch clause specifies what should be done if we fail to find the url. Once again, it just prints the error.
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Use the setter functions to make sure the instance variables of the outer class get saved properly. 
            PastLaunches.this.setDates(dates);
            PastLaunches.this.setCustomers(customers);
            return null;
        }
    }
}
