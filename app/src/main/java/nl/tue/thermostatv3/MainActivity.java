package nl.tue.thermostatv3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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


/**
 * This activity controls the Manual screen of the Main menu
 */
public class MainActivity extends ActionBarActivity  {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    static final String STATE_DECIMAL = "decimal temp";
    static final String STATE_INTEGER = "integer temp";

    int decimal = 0;
    int integ = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Restore activity state if needed
        if (savedInstanceState != null) {
            decimal = savedInstanceState.getInt(STATE_DECIMAL);
            integ = savedInstanceState.getInt(STATE_INTEGER);
        } else {
            decimal = 0;
            integ = 21;
        }
        setContentView(R.layout.main_menu);

        // Adding components
        final Button button = (Button) findViewById(R.id.button);
        final Button plusButton = (Button) findViewById(R.id.plusButton);
        Button minButton = (Button) findViewById(R.id.minButton);

        final TextView warningText = (TextView) findViewById(R.id.statusText);

        warningText.setVisibility(View.INVISIBLE);

        Button rightB = (Button) findViewById(R.id.rightB);
        Button midB = (Button) findViewById(R.id.midB);
        Button leftB = (Button) findViewById(R.id.leftB);

        SeekBar tempBar = (SeekBar)findViewById(R.id.tempBar);
        tempBar.setProgress(decimal);

        // Adding animations
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        // "Remove" unneeded components
        // NEED TO KEEP COMPONENTS IN LAYOUT IN ORDER TO MAINTAIN CORRECT LAYOUT RATIO'S
        //plusButton.setVisibility(View.INVISIBLE);
        //minButton.setVisibility(View.INVISIBLE);
        //warningText.setVisibility(View.INVISIBLE);

        // Starting conditions
        button.setText(integ + "." + decimal + " \u2103");

        // Behaviour for + button
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (integ == 30) {
                    integ = integ;
                    decimal = decimal;
                    warningText.setText("Maximum Temperature!");
                    warningText.setVisibility(View.VISIBLE);
                    warningText.startAnimation(animationFadeIn);
                    warningText.startAnimation(animationFadeOut);
                    warningText.setVisibility(View.INVISIBLE);
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
                    warningText.setText("Minimum Temperature!");
                    warningText.setVisibility(View.VISIBLE);
                    warningText.startAnimation(animationFadeIn);
                    warningText.startAnimation(animationFadeOut);
                    warningText.setVisibility(View.INVISIBLE);
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
                    Intent intent = new Intent(view.getContext(), DayNight.class);
                    startActivity(intent);
                } else if (buttonString.startsWith("Settings")) {
                    Intent intent = new Intent(view.getContext(), Settings.class);
                    startActivity(intent);
                } else {   //then it must be the help button

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

    // TODO Restore activity state if needed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_DECIMAL, decimal);
        savedInstanceState.putInt(STATE_INTEGER, integ);
        super.onSaveInstanceState(savedInstanceState);
    }
}