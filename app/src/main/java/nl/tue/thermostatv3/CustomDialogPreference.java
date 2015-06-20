package nl.tue.thermostatv3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.WeekProgram;

/**
 * Created by Stan on 20-6-2015.
 */

public class CustomDialogPreference extends DialogPreference
{
    public CustomDialogPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onClick()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Reset week program?");
        dialog.setMessage("This action will replace all your switches by the default values. You cannot undo this action. Are you sure you want to continue?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Reset", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                new Thread(new Runnable(){
                    public void run(){
                        try {
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            wpg.setDefault();
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e){
                            System.err.println("Error from getdata" + e);
                        }
                    }
                }).start();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dlg, int which)
            {
                dlg.cancel();
            }
        });

        AlertDialog al = dialog.create();
        al.show();
    }
}