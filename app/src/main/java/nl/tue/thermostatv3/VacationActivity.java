package nl.tue.thermostatv3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import org.thermostatapp.util.HeatingSystem;

/**
 * This activity controls the Manual screen of the Main menu
 */

public class VacationActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    Thread retrieve;
    Thread set;

    Button curTemp;

    Handler handler;

    double targetTempValue;

    boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vacation_layout);

        // Add components
        curTemp = (Button) findViewById(R.id.button);
        final Button plusButton = (Button) findViewById(R.id.plusButton);
        final Button minButton = (Button) findViewById(R.id.minButton);
        final Button enableB = (Button) findViewById(R.id.enableB);

        final TextView statusText = (TextView) findViewById(R.id.statusText);

        Button rightB = (Button) findViewById(R.id.rightB);
        Button midB = (Button) findViewById(R.id.midB);
        Button leftB = (Button) findViewById(R.id.leftB);

        handler = new Handler();

        //Set heatingsystem address
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/39";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        // get values from server
        retrieve = new Thread(new Runnable() {
            public void run() {
                try {
                    targetTempValue = Double.parseDouble(HeatingSystem.get("targetTemperature"));
                    if(HeatingSystem.get("weekProgramState").equals("on")) {
                        state = true;
                    } else{
                        state = false;
                    }
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
                            curTemp.setText(targetTempValue + " \u2103");
                        }
                    });
                    if(!state){
                        statusText.post(new Runnable() {
                            public void run() {
                                statusText.setText("Vacation mode: OFF");
                            }
                        });
                        enableB.post(new Runnable() {
                            public void run() {
                                enableB.setText("Enable");
                            }
                        });
                        plusButton.post(new Runnable() {
                            public void run() {
                                plusButton.setEnabled(true);
                            }
                        });
                        minButton.post(new Runnable() {
                            public void run() {
                                minButton.setEnabled(true);
                            }
                        });
                    } else{
                        statusText.post(new Runnable() {
                            public void run() {
                                statusText.setText("Vacation mode: ON");
                            }
                        });
                        enableB.post(new Runnable() {
                            public void run() {
                                enableB.setText("Disable");
                            }
                        });
                        plusButton.post(new Runnable(){
                            public void run(){
                                plusButton.setEnabled(false);
                            }
                        });
                        minButton.post(new Runnable() {
                            public void run() {
                                minButton.setEnabled(false);
                            }
                        });
                    }
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


        // Enable Vacation-mode
        enableB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        if (state) {
                            try {
                                HeatingSystem.put("weekProgramState", "on");
                                statusText.post(new Runnable() {
                                    public void run() {
                                        statusText.setText("Vacation mode: OFF");
                                    }
                                });
                                enableB.post(new Runnable() {
                                    public void run() {
                                        enableB.setText("Enable");
                                    }
                                });
                                plusButton.post(new Runnable() {
                                    public void run() {
                                        plusButton.setEnabled(true);
                                    }
                                });
                                minButton.post(new Runnable() {
                                    public void run() {
                                        minButton.setEnabled(true);
                                    }
                                });
                                state = false;
                            } catch (Exception e) {
                                handler.post(new Runnable() { // This thread runs in the UI
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                System.err.println("Error from getdata" + e);
                            }
                        } else {
                            try {
                                HeatingSystem.put("currentTemperature", Double.toString(targetTempValue));
                                HeatingSystem.put("weekProgramState", "off");
                                statusText.post(new Runnable() {
                                    public void run() {
                                        statusText.setText("Vacation mode: ON");
                                    }
                                });
                                enableB.post(new Runnable() {
                                    public void run() {
                                        enableB.setText("Disable");
                                    }
                                });
                                plusButton.post(new Runnable(){
                                   public void run(){
                                       plusButton.setEnabled(false);
                                   }
                                });
                                minButton.post(new Runnable() {
                                    public void run() {
                                        minButton.setEnabled(false);
                                    }
                                });
                                state = true;
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
                    }
                }).start();
            }
        });

        // Change temperature
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
                            curTemp.post(new Runnable() {
                                public void run() {
                                    curTemp.setText(targetTempValue + " \u2103");
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
        // Behaviour for - button
        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            targetTempValue = (double)Math.round((targetTempValue - 0.1)*10)/10;
                            if(targetTempValue>30){
                                targetTempValue = 30;
                            }
                            HeatingSystem.put("currentTemperature", Double.toString(targetTempValue));
                            curTemp.post(new Runnable() {
                                public void run() {
                                    curTemp.setText(targetTempValue + " \u2103");
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



        // Layout switching buttons
        leftB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VacationActivity.this, ScheduleActivity.class));
            }
        });

        midB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VacationActivity.this, MainActivity.class));
            }
        });

        rightB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VacationActivity.this, VacationActivity.class));
            }
        });

        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.vacation_layout);
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

    // TODO Restore activity state if needed
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}

