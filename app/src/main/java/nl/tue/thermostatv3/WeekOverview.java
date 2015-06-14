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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.util.ArrayList;

/**
 * Created by s148494 on 8-6-2015.
 */
public class WeekOverview extends ActionBarActivity  {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private MySimpleArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private TextView text;
    RadioGroup groupWeek;
    RadioButton rbSelected;
    TextView textSwitches[];
    WeekProgram wpg;
    ArrayList<Switch> switches;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_overview);

        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.week_overview);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Set heatingsystem address
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/39";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        //Put textviews into array
        textSwitches = new TextView[10];
        textSwitches[0] = (TextView)findViewById(R.id.switch1);
        textSwitches[1] = (TextView)findViewById(R.id.switch2);
        textSwitches[2] = (TextView)findViewById(R.id.switch3);
        textSwitches[3] = (TextView)findViewById(R.id.switch4);
        textSwitches[4] = (TextView)findViewById(R.id.switch5);
        textSwitches[5] = (TextView)findViewById(R.id.switch6);
        textSwitches[6] = (TextView)findViewById(R.id.switch7);
        textSwitches[7] = (TextView)findViewById(R.id.switch8);
        textSwitches[8] = (TextView)findViewById(R.id.switch9);
        textSwitches[9] = (TextView)findViewById(R.id.switch10);

        //Radiogroup listener
        //Gets all switches of the selected day from the server.
        groupWeek = (RadioGroup)findViewById(R.id.radiogroup_week);
        groupWeek.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelected = (RadioButton)findViewById(checkedId);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            wpg = HeatingSystem.getWeekProgram();
                            String selectedDay = (String)rbSelected.getText();
                            String day = toDay(selectedDay);
                            switches = wpg.getSwitches(day);
                            textSwitches[0].post(new Runnable(){
                                public void run() {
                                    //Add this to other textviews
                                    String time =  switches.get(1).getTime();
                                    String state = (switches.get(1).getState()) ? "On" : "Off";
                                    String type = switches.get(1).getType();
                                    textSwitches[0].setText(type + " - Time: " + time + "  - State: " + state);
                                }
                            });
                            textSwitches[1].post(new Runnable(){
                                public void run() {
                                    textSwitches[1].setText(switches.get(1).getTime());
                                }
                            });
                            textSwitches[2].post(new Runnable() {
                                public void run() {
                                    textSwitches[2].setText(switches.get(2).getTime());
                                }
                            });
                            textSwitches[3].post(new Runnable() {
                                public void run() {
                                    textSwitches[3].setText(switches.get(3).getTime());
                                }
                            });
                            textSwitches[4].post(new Runnable() {
                                public void run() {
                                    textSwitches[4].setText(switches.get(4).getTime());
                                }
                            });
                            textSwitches[5].post(new Runnable() {
                                public void run() {
                                    textSwitches[5].setText(switches.get(5).getTime());
                                }
                            });
                            textSwitches[6].post(new Runnable() {
                                public void run() {
                                    textSwitches[6].setText(switches.get(6).getTime());
                                }
                            });
                            textSwitches[7].post(new Runnable() {
                                public void run() {
                                    textSwitches[7].setText(switches.get(7).getTime());
                                }
                            });
                            textSwitches[8].post(new Runnable() {
                                public void run() {
                                    textSwitches[8].setText(switches.get(8).getTime());
                                }
                            });
                            textSwitches[9].post(new Runnable() {
                                public void run() {
                                    textSwitches[9].setText(switches.get(9).getTime());
                                }
                            });

                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

    }

    //Converts day abbreviaton to full day name
    public String toDay(String day){
        switch(day){
            case "Mon": day = "Monday"; break;
            case "Tue": day = "Tuesday"; break;
            case "Wed": day = "Wednesday"; break;
            case "Thu": day = "Thursday"; break;
            case "Fri": day = "Friday"; break;
            case "Sat": day = "Saturday"; break;
            case "Sun": day = "Sunday"; break;
            default: day = "Monday"; break;
        }
        return day;
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


}