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

import org.thermostatapp.util.*;

/**
 * Created by s143243 on 9-6-2015.
 */
public class DayNight extends ActionBarActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    double vtempDay = 20;
    double vtempNight = 10;
    double lowerBound = 5;
    double upperBound = 30;

    TextView tempDay;
    TextView tempNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Menu
        setContentView(R.layout.day_night);

        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.DayNight);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Set heatingsystem address
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/39";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        //Declare buttons/textviews
        Button bPlusDay = (Button)findViewById(R.id.bPlusDay);
        Button bPlusNight = (Button)findViewById(R.id.bPlusNight);
        Button bMinusDay = (Button)findViewById(R.id.bMinusDay);
        Button bMinusNight = (Button)findViewById(R.id.bMinusNight);
        tempDay = (TextView)findViewById(R.id.tempDay);
        tempNight = (TextView)findViewById(R.id.tempNight);

        //Give textViews values from server
        new Thread(new Runnable() {
            public void run() {
                try {
                    vtempDay = Double.parseDouble(HeatingSystem.get("dayTemperature"));
                    vtempNight = Double.parseDouble(HeatingSystem.get("nightTemperature"));
                    tempDay.post(new Runnable() {
                        public void run() {
                            tempDay.setText(vtempDay + " \u2103");
                        }
                    });
                    tempNight.post(new Runnable() {
                        public void run() {
                            tempNight.setText(vtempNight + " \u2103");
                        }
                    });
                } catch (Exception e) {
                    System.err.println("Error from getdata" + e);
                }
            }
        }).start();

        //Button listeners
        bPlusDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            vtempDay = (double)Math.round((vtempDay + 0.1)*10)/10;
                            HeatingSystem.put("dayTemperature", Double.toString(vtempDay));
                            tempDay.post(new Runnable() {
                                public void run() {
                                    tempDay.setText(vtempDay + " \u2103");
                                }
                            });
                        } catch (Exception e) {
                            vtempDay = vtempDay - 0.1;
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        bMinusDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            vtempDay = (double)Math.round((vtempDay - 0.1)*10)/10;
                            HeatingSystem.put("dayTemperature", Double.toString(vtempDay));
                            tempDay.post(new Runnable() {
                                public void run() {
                                    tempDay.setText(vtempDay + " \u2103");
                                }
                            });
                        } catch (Exception e) {
                            vtempDay = vtempDay - 0.1;
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        bPlusNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            vtempNight = (double)Math.round((vtempNight + 0.1)*10)/10;
                            HeatingSystem.put("nightTemperature", Double.toString(vtempNight));
                            tempNight.post(new Runnable() {
                                public void run() {
                                    tempNight.setText(vtempNight + " \u2103");
                                }
                            });
                        } catch (Exception e) {
                            vtempNight = vtempNight - 0.1;
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        bMinusNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            vtempNight = (double)Math.round((vtempNight - 0.1)*10)/10;
                            HeatingSystem.put("nightTemperature", Double.toString(vtempNight));
                            tempNight.post(new Runnable() {
                                public void run() {
                                    tempNight.setText(vtempNight + " \u2103");
                                }
                            });
                        } catch (Exception e) {
                            vtempNight = vtempNight - 0.1;
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

    }

    private void addDrawerItems() {
        String[] osArray = { "Home", "Week program", "Day/night temperature", "Settings", "Help" };
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

                } else if (buttonString.startsWith("Settings")) {

                } else {   //then it must be the help button

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

