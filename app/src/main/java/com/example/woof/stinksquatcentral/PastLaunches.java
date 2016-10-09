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

        public ArrayList<String> dates = new ArrayList<String>();
        public ArrayList<String> customers = new ArrayList<String>();
        public static ArrayList<String> sites = new ArrayList<String>();
        public static ArrayList<String> vehicles = new ArrayList<String>();

        public final static String RESULT = "com.example.woof.stinksquatcentral.RESULT";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.past_launches);
            pingSpaceX();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parseData();
            TextView text0 = (TextView)findViewById(R.id.dates);
            String datd = "Date: ";
            for(int i = 0; i < dates.size(); i++) {
                datd = datd + "\r\n" + dates.get(i);
            }
            text0.setText(datd);

            TextView text1 = (TextView)findViewById(R.id.customers);
            String datc = "Customer: ";
            for(int i = 0; i < dates.size(); i++) {
                datc = datc + "\r\n" + customers.get(i);
            }
            text1.setText(datc);
        }

        void pingSpaceX() {
            new PingSite().execute();
        }

        void setDates(ArrayList<String> d) {
            dates = d;
        }

        void setCustomers(ArrayList<String> c) {
            customers = c;
        }

        void parseData() {
            ArrayList<String> holder0 = new ArrayList<String>();
            int dSize = dates.size();
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
                holder0.add(temp);
            }
            dates = holder0;

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

    private class PingSite extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
        protected ArrayList<String> doInBackground(ArrayList<String>... urls) {
            URL url;
            int counter = 0;
            /*ArrayList<String> dates = new ArrayList<String>();
            ArrayList<String> customers = new ArrayList<String>();
            ArrayList<String> sites = new ArrayList<String>();
            ArrayList<String> vehicles = new ArrayList<String>();
            ArrayList<String> holder = new ArrayList<String>();*/

            try {
                url = new URL("http://www.spacex.com/missions");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String inLine;
                while ((inLine = in.readLine()) != null) {
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
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            PastLaunches.this.setDates(dates);
            PastLaunches.this.setCustomers(customers);
            return null;
        }
    }
}
