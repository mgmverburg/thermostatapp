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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This activity controls the Manual screen of the Main menu
 */

public class VacationActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    boolean on = false;
    int decimal = 0;
    int integ = 21;

    String STATE_ON = "state on";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Restore activity state
        if (savedInstanceState != null) {
            on = savedInstanceState.getBoolean(STATE_ON);
        } else {
            on = false;
        }
        setContentView(R.layout.vacation_layout);

        // Add components
        final Button button = (Button) findViewById(R.id.button);
        final Button plusButton = (Button) findViewById(R.id.plusButton);
        final Button minButton = (Button) findViewById(R.id.minButton);
        final Button enableB = (Button) findViewById(R.id.enableB);

        final TextView statusText = (TextView) findViewById(R.id.statusText);

        Button rightB = (Button) findViewById(R.id.rightB);
        Button midB = (Button) findViewById(R.id.midB);
        Button leftB = (Button) findViewById(R.id.leftB);

        // Enable Vacation-mode
        enableB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (on) {
                    statusText.setText("Vacation mode: OFF");
                    plusButton.setEnabled(true);
                    minButton.setEnabled(true);
                    enableB.setText("Enable");
                    on = false;
                } else if (!on) {
                    statusText.setText("Vacation mode: ON");
                    plusButton.setEnabled(false);
                    minButton.setEnabled(false);
                    enableB.setText("Disable");
                    on = true;
                }
            }
        });

        // Change temperature
        // Behaviour for + button
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (integ == 30) {
                    integ = integ;
                    decimal = decimal;
                    statusText.setText("Maximum Temperature!");
                    statusText.setVisibility(View.VISIBLE);

                    statusText.setText("Vacation mode: OFF");
                } else {
                    if (decimal == 9) {
                        decimal = 0;
                        integ++;
                    } else {
                        decimal++;
                    }

                    button.setText(integ + "." + decimal + " \u2103");
                } }
        });
        // Behaviour for - button
        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (integ == 5 && decimal == 0) {
                    integ = integ;
                    decimal = decimal;
                    statusText.setText("Minimum Temperature!");
                    statusText.setVisibility(View.VISIBLE);

                    statusText.setText("Vacation mode: OFF");
                } else {
                    if (decimal == 0) {
                        decimal = 9;
                        integ--;
                    } else {
                        decimal--;
                    }

                    button.setText(integ + "." + decimal + " \u2103");
                } }
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

    // TODO Restore activity state if needed
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_ON, on);
        super.onSaveInstanceState(savedInstanceState);
    }
}

