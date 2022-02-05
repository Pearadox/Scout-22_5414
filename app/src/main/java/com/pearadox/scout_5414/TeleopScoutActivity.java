package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AlertDialog;

import static android.view.View.VISIBLE;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {

    String TAG = "TeleopScoutActivity";      // This CLASS name
    /* Header Sect. */  TextView txt_dev, txt_stud, txt_match, txt_tnum;
    /* P/U Sect. */     CheckBox chkBox_PU_Cargo_floor, chkBox_CargoTerminal;
    /* Shoot Sect. */   TextView  txt_UpperHub; Button btn_UpperHubPlus, btn_UpperHubMinus; ;
                        TextView  txt_Lower; Button btn_LowerMinus, btn_LowerPlus;
    /* Climb Sect. */   CheckBox chk_Climbed;
                        CheckBox chk_LiftedBy, chk_Lifted; Spinner spinner_numRobots;
                        RadioGroup  radgrp_END;      RadioButton  radio_Lift, radio_Zero, radio_One, radio_Two, radio_Three, radio_Four;
    /* Comment */       EditText editText_TeleComments;
    /* Last Sect. */    Button button_GoToFinalActivity, button_Number_PenaltiesPlus, button_Number_PenaltiesUndo;
                        TextView txt_Number_Penalties;
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private FirebaseDatabase  pfDatabase;
    private DatabaseReference pfDevice_DBReference;
    String key = null;
    String tn  = " ";

    // ===================  TeleOps Elements for Match Scout Data object ===================
    // Declare & initialize

    public boolean Cargo_floor      = false;        // Did they pickup Cargo off the ground?
    public boolean Cargo_Terminal   = false;        // Did they get Cargo from Loading Station?
    public int     Low              = 0;            // # Low Goal balls
    public int     HighHub          = 0;            // # High Goal balls

    public boolean Climbed          = false;        // Did they Climb?
    public String  HangarLev        = "";           // What Level Climb  
    public int num_Penalties        = 0;            // How many penalties received?
    /* */
    public String  teleComment          = " ";      // Tele Comment
    // ===========================================================================
    matchData match_cycle = new matchData();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "<< Teleop Scout >>");
        setContentView(R.layout.activity_teleop_scout);
        Bundle bundle = this.getIntent().getExtras();
        tn = bundle.getString("tnum");
        Log.w(TAG, tn);      // ** DEBUG **

        txt_tnum = (TextView) findViewById(R.id.txt_tnum);
        txt_tnum.setText(tn);

        editText_TeleComments   = (EditText) findViewById(R.id.editText_teleComments);
        chkBox_PU_Cargo_floor   = (CheckBox) findViewById(R.id.chkBox_PU_Cargo_floor);
        chkBox_CargoTerminal    = (CheckBox) findViewById(R.id.chkBox_CargoTerminal);
        chk_Climbed             = (CheckBox) findViewById(R.id.chk_Climbed);
        radio_Zero              = (RadioButton) findViewById(R.id.radio_Zero);
        radio_One               = (RadioButton) findViewById(R.id.radio_One);
        radio_Two               = (RadioButton) findViewById(R.id.radio_Two);
        radio_Three             = (RadioButton) findViewById(R.id.radio_Three);
        radio_Four              = (RadioButton) findViewById(R.id.radio_Four);
        btn_UpperHubPlus        = (Button) findViewById(R.id.btn_UpperHubPlus);
        btn_UpperHubMinus       = (Button) findViewById(R.id.btn_UpperHubMinus);
        btn_LowerPlus           = (Button) findViewById(R.id.btn_LowerPlus);
        btn_LowerMinus          = (Button) findViewById(R.id.btn_LowerMinus);
        txt_UpperHub            = (TextView) findViewById(R.id.txt_UpperHub);
        txt_Lower               = (TextView) findViewById(R.id.txt_Lower);
        txt_Number_Penalties    = (TextView) findViewById(R.id.txt_Number_Penalties);
        button_Number_PenaltiesPlus = (Button) findViewById(R.id.button_Number_PenaltiesPlus);
        button_Number_PenaltiesUndo = (Button) findViewById(R.id.button_Number_PenaltiesUndo);
        button_GoToFinalActivity    = (Button)   findViewById(R.id.button_GoToFinalActivity);

        pfDatabase                = FirebaseDatabase.getInstance();            // Firebase
        pfDevice_DBReference      = pfDatabase.getReference("devices");     // List of Devices

        radio_Zero.setChecked(true);        // Start with Climb radio button NONE
        HangarLev = "None";                 //   so they HAVE to change it (ChkBx starts off)

        if (Pearadox.Match_Data.isAuto_mode()) {
            Toast toast = Toast.makeText(getBaseContext(), "\n\n*** No Autonomous was set - Watch for any scoring in TeleOps ***\n\n", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,  100);
            tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
        }


// *****************************************************************************************
// *****************************************************************************************


    button_GoToFinalActivity.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        Log.e(TAG, "###  Clicked Final  ###  " + HangarLev + "  " + HangarLev.length());
            if (!PreReqs()) {
                // should have issued errors
            } else {  // OK
                storeTeleData();                    // Put all the TeleOps data collected in Match object
                updateDev("Final");           // Update 'Phase' for stoplight indicator in ScoutMaster

                Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                Bundle SMbundle = new Bundle();
                SMbundle.putString("tnum", tn);
                smast_intent.putExtras(SMbundle);
                startActivity(smast_intent);
            }
}
    });

    chkBox_PU_Cargo_floor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PU_Cargo_floor Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"PU_Cargo_floor is checked.");
                Cargo_floor = true;
            } else {  //not checked
                Log.w(TAG,"PU_Cargo_floor is unchecked.");
                Cargo_floor = false;
            }
        }
    });
    chkBox_CargoTerminal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_CargoTerminal Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"chkBox_CargoTerminal is checked.");
                Cargo_Terminal = true;
            } else {  //not checked
                Log.w(TAG,"chkBox_CargoTerminal is unchecked.");
                Cargo_Terminal = false;
            }
        }
    });


        //============================================================================
        btn_UpperHubPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HighHub++;
                Log.w(TAG, "Outer = " + Integer.toString(HighHub));      // ** DEBUG **
                txt_UpperHub.setText(Integer.toString(HighHub));    // Perform action on click
            }
        });
        btn_UpperHubMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HighHub >= 1) {
                    HighHub--;
                }
                Log.w(TAG, "Outer = " + Integer.toString(HighHub));      // ** DEBUG **
                txt_UpperHub.setText(Integer.toString(HighHub));    // Perform action on click
            }
        });


        btn_LowerPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Low++;
                Log.w(TAG, "Bottom = " + Integer.toString(Low));      // ** DEBUG **
                txt_Lower.setText(Integer.toString(Low));
            }
        });
        btn_LowerMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Low >= 1) {
                    Low--;
                }
                Log.w(TAG, "Bottom = " + Integer.toString(Low));      // ** DEBUG **
                txt_Lower.setText(Integer.toString(Low));
            }
        });



    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==
    chk_Climbed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        Log.w(TAG, "chk_Climbed Listener");
        if (buttonView.isChecked()) {
            Log.w(TAG,"Climbed is checked.");
            Climbed = true;
            radio_Zero.setChecked(false);       // Turn OFF 'None'
            radio_Zero.setClickable(false);     // Make it so they can't pick 'None'
        } else {  //not checked
            Log.w(TAG,"Climbed is unchecked.");
            Climbed = false;
            HangarLev = "None";
            radio_Zero.setClickable(true);      // Make it so they can pick 'None'
            radio_Zero.setChecked(true);        // Turn OFF 'None'
        }
    }
    });



        button_Number_PenaltiesPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                num_Penalties++;
                Log.w(TAG, "Penalties = " + Integer.toString(num_Penalties));      // ** DEBUG **
                txt_Number_Penalties.setText(Integer.toString(num_Penalties));    // Perform action on click
            }
        });
        button_Number_PenaltiesUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (num_Penalties >= 1) {
                    num_Penalties--;
                }
                Log.w(TAG, "Penalties = " + Integer.toString(num_Penalties));      // ** DEBUG **
                txt_Number_Penalties.setText(Integer.toString(num_Penalties));    // Perform action on click
            }
        });


        editText_TeleComments.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.w(TAG, "******  onTextChanged TextWatcher  ******" + s);
            teleComment = String.valueOf(s);
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.w(TAG, "******  beforeTextChanged TextWatcher  ******");
            // TODO Auto-generated method stub
        }
        @Override
        public void afterTextChanged(Editable s) {
            Log.w(TAG, "******  onTextChanged TextWatcher  ******" + s );
            teleComment = String.valueOf(s);
        }
    });


        // === End of OnCreate ===
    }



    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Hanging(View view) {
        Log.w(TAG, "@@ RadioClick_Hanging @@" );
        radgrp_END = (RadioGroup) findViewById(R.id.radgrp_END);
        int selectedId = radgrp_END.getCheckedRadioButtonId();
        Log.d(TAG, "*** Selected=" + selectedId);
        radio_Lift = (RadioButton) findViewById(selectedId);
        String value = radio_Lift.getText().toString();
        if (value.equals("None")) {             // Zero?
            Log.w(TAG, "None");
            HangarLev = "None";
            chk_Climbed.setChecked(false);
        } else if (value.equals("Low")){        // One?
            Log.w(TAG, "Low");
            HangarLev = "Low";
            chk_Climbed.setChecked(true);
       } else if (value.equals("Mid")){         // Two
            Log.w(TAG, "Mid");
            HangarLev = "Mid";
            chk_Climbed.setChecked(true);
        } else if (value.equals("High")){       // Three
            Log.w(TAG, "High");
            HangarLev = "High";
            chk_Climbed.setChecked(true);
        } else if (value.equals("Traversal")){  // Four
            Log.w(TAG, "Traversal");
            HangarLev = "Traversal";
            chk_Climbed.setChecked(true);
        } else {                                // Invalid
            Log.e(TAG, "****  Invalid Hangar Level ****");
        }
    }




    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeTeleData() {
        Log.w(TAG, ">>>>  storeTeleData  <<<<");
        // New Match Data Object *** GLF 1/26/22
        Pearadox.Match_Data.setTele_Cargo_term(Cargo_Terminal);
        Pearadox.Match_Data.setTele_Cargo_floor(Cargo_floor);
        Pearadox.Match_Data.setTele_Low(Low);
        Pearadox.Match_Data.setTele_High(HighHub);
        Pearadox.Match_Data.setTele_Climbed(Climbed);
        Pearadox.Match_Data.setTele_HangarLevel(HangarLev);
        Pearadox.Match_Data.setTele_num_Penalties(num_Penalties);
        // **
        Pearadox.Match_Data.setTele_comment(teleComment);
    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private void updateDev(String phase) {     //
        Log.w(TAG, "#### updateDev #### " + phase);
        switch (Pearadox.FRC514_Device) {
            case "Scout Master":         // Scout Master
                key = "0";
                break;
            case ("Red-1"):             //#Red or Blue Scout
                key = "1";
                break;
            case ("Red-2"):             //#
                key = "2";
                break;
            case ("Red-3"):             //#
                key = "3";
                break;
            case ("Blue-1"):            //#
                key = "4";
                break;
            case ("Blue-2"):            //#
                key = "5";
                break;
            case ("Blue-3"):            //#####
                key = "6";
                break;
            case "Visualizer":          // Visualizer
                key = "7";
                break;
            default:                //
                Log.e(TAG, "DEV = NULL" );
        }
        pfDevice_DBReference.child(key).child("phase").setValue(phase);
    }

    public boolean PreReqs () {
        Log.w(TAG, "@@@  PreReqs  @@@  " + HangarLev + "  " + HangarLev.length());
        if (chk_Climbed.isChecked()) {
            if (HangarLev.equals("Low") || HangarLev.equals("Mid") || HangarLev.equals("High") || HangarLev.equals("Traversal"))  {
                return true;
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "\n\n*** Climb was checked - Specify Hangar Level  ***\n", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                return false;
            }
        } else {  // Climb NOT checked
            if (!HangarLev.equals("None")) {
                //Log.e(TAG, "NOT checked"  + HangarLev);   // *** DEBUG ***
                Toast toast = Toast.makeText(getBaseContext(), "\n\n*** Climb was NOT checked - Set Hangar Level 'None'  ***\n", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                return false;
            }
            return true;
        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit without saving? All of your data will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        updateDev("Auto");           // Update 'Phase' for stoplight indicator in ScoutMaster
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }


//###################################################################
//###################################################################
//###################################################################
    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        if (Pearadox.MatchData_Saved) {
            // ToDo - Clear all data back to original settings
            Log.w(TAG, "#### Data was saved in Final #### ");
            //Toast.makeText(getBaseContext(), "Data was saved in Final - probably should Exit", Toast.LENGTH_LONG).show();

            finish();       // Exit

        }
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
    }

}
/*  This is for committing! */