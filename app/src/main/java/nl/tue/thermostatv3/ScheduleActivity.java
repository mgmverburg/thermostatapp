package nl.tue.thermostatv3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This activity controls the Schedule screen of the Main menu
 */

public class ScheduleActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    Button targetTemp;
    Button curTemp;

    TextView statusText;

    Thread retrieve;
    Thread set;

    double targetTempValue;
    double curTempValue;
    double dayTemp;
    double nightTemp;

    String time;
    String day;
    String type;

    Boolean foundSwitch = false;
    Boolean vacationMode = false;

    String nextSwitch = "00:00";
    String[] timeSplit;
    ArrayList<String> times = new ArrayList<String>();
    String nextSwitchTemp = "";
    int nextSwitchTempHours = 0;
    int nextSwitchTempMinutes = 0;
    WeekProgram wpg;
    ArrayList<Switch> switches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);

        // Add components
        targetTemp = (Button) findViewById(R.id.button);
        curTemp = (Button) findViewById(R.id.button2);

        Button plusButton = (Button) findViewById(R.id.plusButton);

        statusText = (TextView) findViewById(R.id.statusText);

        Button rightB = (Button) findViewById(R.id.rightB);
        Button midB = (Button) findViewById(R.id.midB);
        Button leftB = (Button) findViewById(R.id.leftB);

        plusButton.setVisibility(View.INVISIBLE);

        //Set heatingsystem address
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/39";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        // get values from server
        retrieve = new Thread(new Runnable() {
            public void run() {
                try {
                    targetTempValue = Double.parseDouble(HeatingSystem.get("targetTemperature"));
                    curTempValue = Double.parseDouble(HeatingSystem.get("currentTemperature"));
                    dayTemp = Double.parseDouble(HeatingSystem.get("dayTemperature"));
                    nightTemp = Double.parseDouble(HeatingSystem.get("nightTemperature"));
                    vacationMode = HeatingSystem.getVacationMode();
                    time = HeatingSystem.get("time");
                    day = HeatingSystem.get("day");
                } catch (Exception e) {
                    System.err.println("Error from getdata" + e);
                }
            }
        });
        retrieve.start();

        //display values
        set = new Thread(new Runnable() {
            public void run() {
                //Update text
                try {
                    Thread.sleep(1000);
                    curTemp.post(new Runnable() {
                        public void run() {
                            curTemp.setText(curTempValue + " \u2103 inside now");
                        }
                    });
                    targetTemp.post(new Runnable() {
                        public void run() {
                            targetTemp.setText(targetTempValue + " \u2103");
                        }
                    });

                    if(!vacationMode){
                        if(dayTemp == targetTempValue) {
                            statusText.post(new Runnable() {
                                @Override
                                public void run() {
                                    type = "day";
                                    String nextSwitch = findNextSwitch();
                                    statusText.setText("Current: Day temperature, switches to night at " + nextSwitch);
                             }
                            });
                        } else if(nightTemp == targetTempValue){
                            statusText.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     type = "night";
                                     String nextSwitch = findNextSwitch();
                                     statusText.setText("Current: Night temperature, switches to day at " + nextSwitch);
                                 }
                             });
                        } else if(!vacationMode) {
                            statusText.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     type = "manual";
                                     String nextSwitch = findNextSwitch();
                                     statusText.setText("Current: Manual, switches at " + nextSwitch);
                                 }
                             });
                        }
                    } else{
                        statusText.post(new Runnable(){
                            @Override
                            public void run() {
                                statusText.setText("Current: Vacation mode enabled");
                            }
                        });
                    }
                } catch(Exception e){
                    System.err.println("Error from getdata" + e);
                }
            }
        });
        set.start();

        // Layout switching buttons
        leftB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleActivity.this, ScheduleActivity.class));
            }
        });

        midB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleActivity.this, MainActivity.class));
            }
        });

        rightB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleActivity.this, VacationActivity.class));
            }
        });

        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.schedule_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //Returns next enabled switch
    private String findNextSwitch(){
        timeSplit = time.split(":");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        wpg = HeatingSystem.getWeekProgram();
                        switches = wpg.getSwitches(day);

                        //Add all enabled switches to arraylist times
                        for(int i = 0; i<10; i++) {
                            if (switches.get(i).getState()) {
                                if(type == "day"){
                                    if(!switches.get(i).getType().equals(type)){
                                        times.add(switches.get(i).getTime());
                                    }
                                }
                                if(type == "night"){
                                    if(!switches.get(i).getType().equals(type)){
                                        times.add(switches.get(i).getTime());
                                    }
                                }
                                if(type == "manual"){
                                    times.add(switches.get(i).getTime());
                                }
                            }
                        }

                        //get next switch
                        for(int i = 0; i<times.size(); i++) {
                            String[] tempSplit = times.get(i).split(":");
                            if (Integer.parseInt(tempSplit[0]) > Integer.parseInt(timeSplit[0])) {
                                if (nextSwitchTemp.equals("")) {
                                    nextSwitchTemp = times.get(i);
                                    nextSwitchTempHours = Integer.parseInt(tempSplit[0]);
                                    nextSwitchTempMinutes = Integer.parseInt(tempSplit[1]);
                                } else if (Integer.parseInt(tempSplit[0]) < nextSwitchTempHours) {
                                    nextSwitchTemp = times.get(i);
                                    nextSwitchTempHours = Integer.parseInt(tempSplit[0]);
                                    nextSwitchTempMinutes = Integer.parseInt(tempSplit[1]);
                                } else if (Integer.parseInt(tempSplit[0]) == nextSwitchTempHours && Integer.parseInt(tempSplit[1]) < nextSwitchTempMinutes) {
                                    nextSwitchTemp = times.get(i);
                                    nextSwitchTempHours = Integer.parseInt(tempSplit[0]);
                                    nextSwitchTempMinutes = Integer.parseInt(tempSplit[1]);
                                }
                            } else if(Integer.parseInt(tempSplit[0]) == Integer.parseInt(timeSplit[0]) && Integer.parseInt(tempSplit[1]) > Integer.parseInt(timeSplit[1])) {
                                if (nextSwitchTemp.equals("")) {
                                    nextSwitchTemp = times.get(i);
                                    nextSwitchTempHours = Integer.parseInt(tempSplit[0]);
                                    nextSwitchTempMinutes = Integer.parseInt(tempSplit[1]);
                                } else if (Integer.parseInt(tempSplit[0]) < nextSwitchTempHours) {
                                    nextSwitchTemp = times.get(i);
                                    nextSwitchTempHours = Integer.parseInt(tempSplit[0]);
                                    nextSwitchTempMinutes = Integer.parseInt(tempSplit[1]);
                                } else if (Integer.parseInt(tempSplit[0]) == nextSwitchTempHours && Integer.parseInt(tempSplit[1]) < nextSwitchTempMinutes) {
                                    nextSwitchTemp = times.get(i);
                                    nextSwitchTempHours = Integer.parseInt(tempSplit[0]);
                                    nextSwitchTempMinutes = Integer.parseInt(tempSplit[1]);
                                }
                            }
                        }
                        if(!nextSwitchTemp.equals("")) {
                            nextSwitch = nextSwitchTemp;
                            foundSwitch = true;
                        } else{
                            foundSwitch = true;
                        }
                    } catch(Exception e) {
                        System.err.println("Error from getdata" + e);
                        //to prevent infinite loop
                        foundSwitch = true;
                    }
                }
            }).start();
        while(!foundSwitch) {

        }
        return nextSwitch;
    }

    private void addDrawerItems() {
        String[] osArray = { "Home", "Week program", "Day/night temperature", "Settings"};
        mAdapter = new MySimpleArrayAdapter(this, osArray);
        mDrawerList.setAdapter(mAdapter);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.text1);
                String buttonString = (String)textView.getText();
                if (buttonString.startsWith("Home") ){
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                } else if (buttonString.startsWith("Week program")) {
                    Intent intent = new Intent(view.getContext(), WeekOverview.class);
                    startActivity(intent);
                } else if (buttonString.startsWith("Day/night temperature")) {
                    Intent intent = new Intent(view.getContext(), DayNight.class);
                    startActivity(intent);
                } else if (buttonString.startsWith("Settings")) {
                    Intent intent = new Intent(view.getContext(), Settings.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_thermostat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

