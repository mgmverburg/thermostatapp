package nl.tue.thermostatv3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.thermostatapp.util.HeatingSystem;


/**
 * This activity controls the Manual screen of the Main menu
 */
public class MainActivity extends ActionBarActivity  {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    double targetTempValue;
    double curTempValue;

    SeekBar tempBar;

    Button targetTemp;
    Button curTemp;

    Handler handler;

    Thread retrieve;
    Thread set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);

        // Adding components
        targetTemp = (Button) findViewById(R.id.button);
        curTemp = (Button) findViewById(R.id.button2);

        Button plusButton = (Button) findViewById(R.id.plusButton);
        Button minButton = (Button) findViewById(R.id.minButton);

        Button rightB = (Button) findViewById(R.id.rightB);
        Button midB = (Button) findViewById(R.id.midB);
        Button leftB = (Button) findViewById(R.id.leftB);

        tempBar = (SeekBar)findViewById(R.id.tempBar);

        handler = new Handler();

        //Set heatingsystem address
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/39";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        // get values from server
        retrieve = new Thread(new Runnable() {
            public void run() {
                try {
                    targetTempValue = Double.parseDouble(HeatingSystem.get("targetTemperature"));
                    curTempValue = Double.parseDouble(HeatingSystem.get("currentTemperature"));
                    tempBar.setProgress((int) targetTempValue * 10);
                } catch (Exception e) {
                    handler.post(new Runnable() { // This thread runs in the UI
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
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
                } catch(Exception e){
                    handler.post(new Runnable() { // This thread runs in the UI
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
                    System.err.println("Error from getdata" + e);
                }
            }
        });
        set.start();

        //Update current temp when it is clicked.
        curTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new Thread(new Runnable(){
                    public void run(){
                        try {
                            curTempValue = Double.parseDouble(HeatingSystem.get("currentTemperature"));
                            curTemp.post(new Runnable() {
                                public void run() {
                                    curTemp.setText(curTempValue  + " \u2103 inside now");
                                }
                            });
                        } catch (Exception e) {
                            handler.post(new Runnable() { // This thread runs in the UI
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            });
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        // Behaviour for + button
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            targetTempValue = (double)Math.round((targetTempValue + 0.1)*10)/10;
                            if(targetTempValue>30){
                                targetTempValue = 30;
                            }
                            HeatingSystem.put("currentTemperature", Double.toString(targetTempValue));
                            curTempValue = Double.parseDouble(HeatingSystem.get("currentTemperature"));
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
                            tempBar.setProgress((int) targetTempValue * 10);
                        } catch (Exception e) {
                            handler.post(new Runnable() { // This thread runs in the UI
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            });
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        // Behaviour for - button
        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            targetTempValue = (double)Math.round((targetTempValue - 0.1)*10)/10;
                            if(targetTempValue<5){
                                targetTempValue = 5;
                            }
                            HeatingSystem.put("currentTemperature", Double.toString(targetTempValue));
                            curTempValue = Double.parseDouble(HeatingSystem.get("currentTemperature"));
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
                            tempBar.setProgress((int) targetTempValue * 10);
                        } catch (Exception e) {
                            handler.post(new Runnable() { // This thread runs in the UI
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            });
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        //Seekbar change
        tempBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new Thread(new Runnable() {
                    public void run() {
                        targetTempValue = (double)tempBar.getProgress() / 10;
                        if (targetTempValue < 5) {
                            targetTempValue = 5;
                            tempBar.setProgress(50);
                        }
                        try {
                            HeatingSystem.put("currentTemperature", Double.toString(targetTempValue));
                            curTempValue = Double.parseDouble(HeatingSystem.get("currentTemperature"));
                        } catch (Exception e) {
                            handler.post(new Runnable() { // This thread runs in the UI
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            });
                            System.err.println("Error from getdata" + e);
                        }
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
                    }
                }).start();
            }
        });

        // Layout switching buttons
        leftB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
            }
        });

        midB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        rightB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VacationActivity.class));
            }
        });


        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.main_menu);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        //mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //@Override
        //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //  Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
        //  }
        //});
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

        //noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        //    return true;
        //}

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}