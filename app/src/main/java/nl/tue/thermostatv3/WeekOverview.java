package nl.tue.thermostatv3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    EditText fieldSwitches[];
    int i;
    String time;
    String type;
    String input;
    Boolean state;
    Boolean selected;
    Boolean bool = false;
    android.widget.Switch stateSwitches[];

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

        //Put textfields into array
        fieldSwitches = new EditText[10];
        fieldSwitches[0] = (EditText)findViewById(R.id.field1);
        fieldSwitches[1] = (EditText)findViewById(R.id.field2);
        fieldSwitches[2] = (EditText)findViewById(R.id.field3);
        fieldSwitches[3] = (EditText)findViewById(R.id.field4);
        fieldSwitches[4] = (EditText)findViewById(R.id.field5);
        fieldSwitches[5] = (EditText)findViewById(R.id.field6);
        fieldSwitches[6] = (EditText)findViewById(R.id.field7);
        fieldSwitches[7] = (EditText)findViewById(R.id.field8);
        fieldSwitches[8] = (EditText)findViewById(R.id.field9);
        fieldSwitches[9] = (EditText)findViewById(R.id.field10);

        //Put switches into array
        stateSwitches = new android.widget.Switch[10];
        stateSwitches[0] = (android.widget.Switch)findViewById(R.id.state1);
        stateSwitches[1] = (android.widget.Switch)findViewById(R.id.state2);
        stateSwitches[2] = (android.widget.Switch)findViewById(R.id.state3);
        stateSwitches[3] = (android.widget.Switch)findViewById(R.id.state4);
        stateSwitches[4] = (android.widget.Switch)findViewById(R.id.state5);
        stateSwitches[5] = (android.widget.Switch)findViewById(R.id.state6);
        stateSwitches[6] = (android.widget.Switch)findViewById(R.id.state7);
        stateSwitches[7] = (android.widget.Switch)findViewById(R.id.state8);
        stateSwitches[8] = (android.widget.Switch)findViewById(R.id.state9);
        stateSwitches[9] = (android.widget.Switch)findViewById(R.id.state10);

        //Get weekprogram from server
        new Thread(new Runnable(){
            public void run(){
                try {
                    wpg = HeatingSystem.getWeekProgram();
                } catch (Exception e){
                    System.err.println("Error from getdata" + e);
                }
            }
        }).start();

        //Radiogroup listener
        //Gets all switches of the selected day from the server.
        groupWeek = (RadioGroup)findViewById(R.id.radiogroup_week);
        groupWeek.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelected = (RadioButton) findViewById(checkedId);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (wpg != null) {
                            try {
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                switches = wpg.getSwitches(day);

                                //Set text
                                textSwitches[0].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(0).getTime();
                                        state = switches.get(0).getState();
                                        type = switches.get(0).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[0].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[0].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[0].setVisibility(View.VISIBLE);
                                        fieldSwitches[0].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[0].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[0].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[0].setChecked(true);
                                        } else {
                                            stateSwitches[0].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[1].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(1).getTime();
                                        state = switches.get(1).getState();
                                        type = switches.get(1).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[1].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[1].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[1].setVisibility(View.VISIBLE);
                                        fieldSwitches[1].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[1].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[1].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[1].setChecked(true);
                                        } else {
                                            stateSwitches[1].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[2].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(2).getTime();
                                        state = switches.get(2).getState();
                                        type = switches.get(2).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[2].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[2].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[2].setVisibility(View.VISIBLE);
                                        fieldSwitches[2].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[2].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[2].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[2].setChecked(true);
                                        } else {
                                            stateSwitches[2].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[3].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(3).getTime();
                                        state = switches.get(3).getState();
                                        type = switches.get(3).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[3].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[3].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[3].setVisibility(View.VISIBLE);
                                        fieldSwitches[3].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[3].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[3].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[3].setChecked(true);
                                        } else {
                                            stateSwitches[3].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[4].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(4).getTime();
                                        state = switches.get(4).getState();
                                        type = switches.get(4).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[4].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[4].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[4].setVisibility(View.VISIBLE);
                                        fieldSwitches[4].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[4].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[4].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[4].setChecked(true);
                                        } else {
                                            stateSwitches[4].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[5].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(5).getTime();
                                        state = switches.get(5).getState();
                                        type = switches.get(5).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[5].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[5].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[5].setVisibility(View.VISIBLE);
                                        fieldSwitches[5].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[5].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[5].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[5].setChecked(true);
                                        } else {
                                            stateSwitches[5].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[6].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(6).getTime();
                                        state = switches.get(6).getState();
                                        type = switches.get(6).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[6].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[6].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[6].setVisibility(View.VISIBLE);
                                        fieldSwitches[6].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[6].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[6].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[6].setChecked(true);
                                        } else {
                                            stateSwitches[6].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[7].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(7).getTime();
                                        state = switches.get(7).getState();
                                        type = switches.get(7).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[7].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[7].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[7].setVisibility(View.VISIBLE);
                                        fieldSwitches[7].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[7].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[7].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[7].setChecked(true);
                                        } else {
                                            stateSwitches[7].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[8].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(8).getTime();
                                        state = switches.get(8).getState();
                                        type = switches.get(8).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[8].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[8].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[8].setVisibility(View.VISIBLE);
                                        fieldSwitches[8].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[8].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[8].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[8].setChecked(true);
                                        } else {
                                            stateSwitches[8].setChecked(false);
                                        }
                                    }
                                });

                                //Set text
                                textSwitches[9].post(new Runnable() {
                                    public void run() {
                                        time = switches.get(9).getTime();
                                        state = switches.get(9).getState();
                                        type = switches.get(9).getType();
                                        type = typeToString(type);

                                        //Set type
                                        textSwitches[9].setText(type);

                                    }
                                });

                                //Put time in textfield and make it visible
                                fieldSwitches[9].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[9].setVisibility(View.VISIBLE);
                                        fieldSwitches[9].setText(time);
                                    }
                                });

                                //Make switch visible and set to right value
                                stateSwitches[9].post(new Runnable() {
                                    public void run() {
                                        stateSwitches[9].setVisibility(View.VISIBLE);
                                        if (state) {
                                            stateSwitches[9].setChecked(true);
                                        } else {
                                            stateSwitches[9].setChecked(false);
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                System.err.println("Error from getdata" + e);
                            }
                        }
                    }
                }).start();
            }
        });

        //Sends weekprogram to server on state change
        stateSwitches[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable() {
                    public void run() {
                        rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String) rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(0).getTime();
                        type = switches.get(0).getType();
                        wpg = wpg.setSwitch(wpg, day, 0, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable() {
                    public void run() {
                        rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String) rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(1).getTime();
                        type = switches.get(1).getType();
                        wpg = wpg.setSwitch(wpg, day, 1, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(2).getTime();
                        type = switches.get(2).getType();
                        wpg = wpg.setSwitch(wpg, day, 2, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(3).getTime();
                        type = switches.get(3).getType();
                        wpg = wpg.setSwitch(wpg, day, 3, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(4).getTime();
                        type = switches.get(4).getType();
                        wpg = wpg.setSwitch(wpg, day, 4, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(5).getTime();
                        type = switches.get(5).getType();
                        wpg = wpg.setSwitch(wpg, day, 5, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(6).getTime();
                        type = switches.get(6).getType();
                        wpg = wpg.setSwitch(wpg, day, 6, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(7).getTime();
                        type = switches.get(7).getType();
                        wpg = wpg.setSwitch(wpg, day, 7, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[8].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(8).getTime();
                        type = switches.get(8).getType();
                        wpg = wpg.setSwitch(wpg, day, 8, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        stateSwitches[9].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selected = isChecked;
                new Thread(new Runnable(){
                    public void run(){
                        rbSelected = (RadioButton)findViewById(groupWeek.getCheckedRadioButtonId());
                        String selectedDay = (String)rbSelected.getText();
                        String day = toDay(selectedDay);
                        time = switches.get(9).getTime();
                        type = switches.get(9).getType();
                        wpg = wpg.setSwitch(wpg, day, 9, selected, time, type);
                        try {
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });


        //Checks and sets time after pressing done or losing focus
        fieldSwitches[0].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[0].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[0].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[0].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(0).getState();
                                System.out.println(state);
                                type = switches.get(0).getType();
                                wpg = wpg.setSwitch(wpg, day, 0, state, input, type);
                                HeatingSystem.setWeekProgram(wpg);
                            } else {
                                fieldSwitches[0].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[0].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[0].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[0].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[0].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[0].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[0].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(0).getState();
                                System.out.println(state);
                                type = switches.get(0).getType();
                                wpg = wpg.setSwitch(wpg, day, 0, state, input, type);
                                HeatingSystem.setWeekProgram(wpg);
                            } else {
                                fieldSwitches[0].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[0].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[1].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[1].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[1].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[1].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(1).getState();
                                System.out.println(state);
                                type = switches.get(1).getType();
                                wpg = wpg.setSwitch(wpg, day, 1, state, input, type);
                                HeatingSystem.setWeekProgram(wpg);
                            } else {
                                fieldSwitches[1].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[1].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[1].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[1].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[1].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[1].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[1].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(1).getState();
                                System.out.println(state);
                                type = switches.get(1).getType();
                                wpg = wpg.setSwitch(wpg, day, 1, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[1].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[1].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[2].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[2].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[2].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[2].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(2).getState();
                                System.out.println(state);
                                type = switches.get(2).getType();
                                wpg = wpg.setSwitch(wpg, day, 2, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[2].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[2].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[2].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[2].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[2].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[2].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[2].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(2).getState();
                                System.out.println(state);
                                type = switches.get(2).getType();
                                wpg = wpg.setSwitch(wpg, day, 2, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[2].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[2].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[3].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[3].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[3].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[3].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(3).getState();
                                System.out.println(state);
                                type = switches.get(3).getType();
                                wpg = wpg.setSwitch(wpg, day, 3, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[3].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[3].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[3].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[3].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[3].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[3].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[3].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(3).getState();
                                System.out.println(state);
                                type = switches.get(3).getType();
                                wpg = wpg.setSwitch(wpg, day, 3, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[3].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[3].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[4].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[4].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[4].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[4].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(4).getState();
                                System.out.println(state);
                                type = switches.get(4).getType();
                                wpg = wpg.setSwitch(wpg, day, 4, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[4].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[4].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[4].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[4].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[4].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[4].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[4].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(4).getState();
                                System.out.println(state);
                                type = switches.get(4).getType();
                                wpg = wpg.setSwitch(wpg, day, 4, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[4].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[4].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[5].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[5].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[5].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[5].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(5).getState();
                                System.out.println(state);
                                type = switches.get(5).getType();
                                wpg = wpg.setSwitch(wpg, day, 5, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[5].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[5].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[5].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[5].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[5].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[5].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[5].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(5).getState();
                                System.out.println(state);
                                type = switches.get(5).getType();
                                wpg = wpg.setSwitch(wpg, day, 5, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[5].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[5].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[6].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[6].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[6].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[6].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(6).getState();
                                System.out.println(state);
                                type = switches.get(6).getType();
                                wpg = wpg.setSwitch(wpg, day, 6, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }
                            } else {
                                fieldSwitches[6].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[6].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[6].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[6].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[6].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[6].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[6].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(6).getState();
                                System.out.println(state);
                                type = switches.get(6).getType();
                                wpg = wpg.setSwitch(wpg, day, 6, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[6].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[6].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[7].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[7].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[7].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[7].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(7).getState();
                                System.out.println(state);
                                type = switches.get(7).getType();
                                wpg = wpg.setSwitch(wpg, day, 7, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[7].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[7].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[7].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[7].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[7].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[7].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[7].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(7).getState();
                                System.out.println(state);
                                type = switches.get(7).getType();
                                wpg = wpg.setSwitch(wpg, day, 7, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[7].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[7].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[8].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[8].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[8].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[8].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(8).getState();
                                System.out.println(state);
                                type = switches.get(8).getType();
                                wpg = wpg.setSwitch(wpg, day, 8, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[8].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[8].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[8].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[8].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[8].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[8].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[8].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(8).getState();
                                System.out.println(state);
                                type = switches.get(8).getType();
                                wpg = wpg.setSwitch(wpg, day, 8, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[8].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[8].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        fieldSwitches[9].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                i = actionId;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[9].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[9].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[9].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(9).getState();
                                System.out.println(state);
                                type = switches.get(9).getType();
                                wpg = wpg.setSwitch(wpg, day, 9, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[9].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[9].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        fieldSwitches[9].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If edittext loses focus
                if (!hasFocus) {
                    input = fieldSwitches[9].getText().toString();
                    new Thread(new Runnable() {
                        public void run() {
                            input = fieldSwitches[9].getText().toString();
                            //If input valid
                            if (verifyTimeInput(input)) {
                                fieldSwitches[9].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[9].setText(input);
                                    }
                                });
                                rbSelected = (RadioButton) findViewById(groupWeek.getCheckedRadioButtonId());
                                String selectedDay = (String) rbSelected.getText();
                                String day = toDay(selectedDay);
                                state = switches.get(9).getState();
                                System.out.println(state);
                                type = switches.get(9).getType();
                                wpg = wpg.setSwitch(wpg, day, 9, state, input, type);
                                try {
                                    HeatingSystem.setWeekProgram(wpg);
                                } catch (Exception e){
                                    System.err.println("Error from getdata" + e);
                                }                            } else {
                                fieldSwitches[9].post(new Runnable() {
                                    public void run() {
                                        fieldSwitches[9].setText("Use hh:mm");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }



    //Converts day abbreviation to full day name
    public String toDay(String day){
        String dayName = "";
        switch(day){
            case "MON": dayName = "Monday"; break;
            case "TUE": dayName = "Tuesday"; break;
            case "WED": dayName = "Wednesday"; break;
            case "THU": dayName = "Thursday"; break;
            case "FRI": dayName = "Friday"; break;
            case "SAT": dayName = "Saturday"; break;
            case "SUN": dayName = "Sunday"; break;
            default: dayName = "Monday"; break;
        }
        return dayName;
    }

    //Converts day to Day and night to Night
    public String typeToString(String type){
        if(type.equals("day")){
            type = "Day   ";
        } else {
            type = "Night";
        }
        return type;
    }

    //Checks if time input is valid
    //Accepted: hh:mm
    public Boolean verifyTimeInput(String time){
        Pattern pattern;
        Matcher matcher;

        final String TIME_24_HOURS_PATTERN = "([01][0-9]|2[0-3]):[0-5][0-9]";

        pattern = Pattern.compile(TIME_24_HOURS_PATTERN);
        matcher = pattern.matcher(time);
        return matcher.matches();
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


}