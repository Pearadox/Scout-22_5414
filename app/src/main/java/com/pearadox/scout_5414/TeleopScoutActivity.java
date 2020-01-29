package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import static android.view.View.VISIBLE;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {

    String TAG = "TeleopScoutActivity";      // This CLASS name
    /* Header Sect. */  TextView txt_dev, txt_stud, txt_match, txt_tnum;
    /* P/U Sect. */     CheckBox chkBox_PU_PowerCell_floor, chkBox_PowerCellLoadSta, chkBox_PU_Cell_Trench, chkBox_ContorlPanel;    /* Shoot Sect. */  Button btn_OuterClosePlus, btn_OuterCloseMinus;  TextView  txt_OuterClose;
                        RadioGroup  radgrp_END;      RadioButton  radio_Lift, radio_One, radio_Two, radio_Three, radio_Zero;
                        CheckBox chk_LiftedBy, chk_Lifted;  Spinner spinner_numRobots;
    /* Comment */       EditText editText_TeleComments;
    /* Last Sect. */    Button button_GoToFinalActivity, button_Number_PenaltiesPlus, button_Number_PenaltiesUndo;
                        TextView txt_Number_Penalties;
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private FirebaseDatabase  pfDatabase;
//    private DatabaseReference pfTeam_DBReference;
//    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
//    private DatabaseReference pfCur_Match_DBReference;
    String key = null;
    String tn  = " ";
    public static String[] carry = new String[]             // Num. of robots this robot can lift
            {" ","1","2"};

    // ===================  TeleOps Elements for Match Scout Data object ===================
    // Declare & initialize

    public boolean PowerCell_floor      = false;    // Did they pickup PowerCell off the ground?
    public boolean PowerCell_LoadSta    = false;    // Did they get PowerCell from Loading Station?
    public int     Low                  = 0;        // # Low Goal balls
    public int     HighClose            = 0;        // # High Goal balls - Close
    public int     HighLine             = 0;        // # High Goal balls - Line
    public int     HighFrontCP          = 0;        // # High Goal balls - Front CP
    public int     HighBackCP           = 0;        // # High Goal balls - Back CP
    public boolean conInnerClose        = false;    // Consistent Inner Goal scored Close?
    public boolean conInnerLine         = false;    // Consistent Inner Goal scored Con Line?
    public boolean conInnerFrontCP      = false;    // Consistent Inner Goal scored in Front of CP?
    public boolean conInnerBackCP       = false;    // Consistent Inner Goal scored in Back of CP?
    public boolean CPspin               = false;    // Control Panel Spin
    public boolean CPcolor              = false;    // Control Panel Color
    public boolean ShootUnder           = false;    // Shoot from Under Power Port
    public boolean ShootLine            = false;    // Shoot from Sector Line
    public boolean ShootFtrench         = false;    // Shoot from in Front of Trench
    public boolean ShootBtrench         = false;    // Shoot from in Back of Trench

    public boolean Climbed              = false;    // Did they Climb?
    public boolean UnderSG              = false;    // Parked under Shield Generator
    public int     liftedNum            = 0;        // How many lifted?
    public int     Hang_Num             = 99;       // End - How many on Bar (0-3)
    public boolean Balanced             = false;    // SG is Balanced
    public boolean got_lift             = false;    // Got Lifted by another robot
    public boolean lifted               = false;    // Got Lifted by another robot
    public int num_Penalties            = 0;        // How many penalties received?
    /* */
    public String  teleComment          = " ";    // Tele Comment
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
        chkBox_PU_PowerCell_floor  = (CheckBox) findViewById(R.id.chkBox_PU_PowerCell_floor);
        chkBox_PowerCellLoadSta = (CheckBox) findViewById(R.id.chkBox_PowerCellLoadSta);
        chkBox_PU_Cell_Trench   = (CheckBox) findViewById(R.id.chkBox_PU_Cell_Trench);
        chkBox_ContorlPanel   = (CheckBox) findViewById(R.id.chkBox_ContorlPanel);
        radio_Zero              = (RadioButton) findViewById(R.id.radio_Zero);
        radio_One               = (RadioButton) findViewById(R.id.radio_One);
        radio_Two               = (RadioButton) findViewById(R.id.radio_Two);
        radio_Three             = (RadioButton) findViewById(R.id.radio_Three);
        chk_LiftedBy            = (CheckBox) findViewById(R.id.chk_LiftedBy);
        chk_Lifted              = (CheckBox) findViewById(R.id.chk_Lifted);
        btn_OuterClosePlus      = (Button) findViewById(R.id.btn_OuterClosePlus);
        btn_OuterCloseMinus     = (Button) findViewById(R.id.btn_OuterCloseMinus);
        txt_OuterClose          = (TextView) findViewById(R.id.txt_OuterClose);
        button_Number_PenaltiesPlus = (Button) findViewById(R.id.button_Number_PenaltiesPlus);
        button_Number_PenaltiesUndo = (Button) findViewById(R.id.button_Number_PenaltiesUndo);
        button_GoToFinalActivity  = (Button)   findViewById(R.id.button_GoToFinalActivity);
        txt_Number_Penalties    = (TextView) findViewById(R.id.txt_Number_Penalties);
        final Spinner spinner_numRobots = (Spinner) findViewById(R.id.spinner_numRobots);
        ArrayAdapter adapter_Robs = new ArrayAdapter<String>(this, R.layout.robonum_list_layout, carry);
        adapter_Robs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_numRobots.setAdapter(adapter_Robs);
        spinner_numRobots.setSelection(0, false);
        spinner_numRobots.setOnItemSelectedListener(new TeleopScoutActivity.numRobots_OnItemSelectedListener());
        spinner_numRobots.setVisibility(View.GONE);

        pfDatabase                = FirebaseDatabase.getInstance();            // Firebase
        pfDevice_DBReference      = pfDatabase.getReference("devices");     // List of Devices

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
        Log.w(TAG, "###  Clicked Final  ### " + Hang_Num);
            if (Hang_Num < 4) {        // Gotta pick one!
                storeTeleData();                    // Put all the TeleOps data collected in Match object
                updateDev("Final");           // Update 'Phase' for stoplight indicator in ScoutMaster

                Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                Bundle SMbundle = new Bundle();
                SMbundle.putString("tnum", tn);
                smast_intent.putExtras(SMbundle);
                startActivity(smast_intent);
            } else {
                Log.e(TAG, "ERROR - did not select lift type");
                Toast.makeText(getBaseContext(), "*** End # Hanging _MUST_ be selected ***", Toast.LENGTH_LONG).show();
                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
            }
        }
    });

    chkBox_PU_PowerCell_floor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PU_PowerCell_floor Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"PU_PowerCell is checked.");
                PowerCell_floor = true;
            } else {  //not checked
                Log.w(TAG,"PU_PowerCell is unchecked.");
                PowerCell_floor = false;
            }
        }
    });
    chkBox_PowerCellLoadSta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PowerCellLoadSta Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"chkBox_PowerCellLoadSta is checked.");
                PowerCell_LoadSta = true;
            } else {  //not checked
                Log.w(TAG,"chkBox_PowerCellLoadSta is unchecked.");
                PowerCell_LoadSta = false;
            }
        }
    });

        btn_OuterClosePlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HighClose++;
                Log.w(TAG, "Outer = " + Integer.toString(HighClose));      // ** DEBUG **
                txt_OuterClose.setText(Integer.toString(HighClose));    // Perform action on click
            }
        });
        btn_OuterCloseMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HighClose >= 1) {
                    HighClose--;
                }
                Log.w(TAG, "Outer = " + Integer.toString(HighClose));      // ** DEBUG **
                txt_OuterClose.setText(Integer.toString(HighClose));    // Perform action on click
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==
    chk_LiftedBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chk_LiftedBy Listener");
                if (chk_LiftedBy.isChecked()) {
                    //checked
                Log.w(TAG,"LiftedBy is checked.");
                got_lift = true;
                chk_Lifted.setChecked(false);       // Can't be both!!
                lifted = false;
            }
            else {
                //not checked
                Log.w(TAG,"LiftedBy is unchecked.");
                got_lift = false;
            }
        }
    });

    chk_Lifted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chk_Lifted Listener");
            if (chk_Lifted.isChecked()) {
                //checked
                Log.w(TAG,"Lifted is checked.");
                got_lift = true;
                chk_LiftedBy.setChecked(false);       // Can't be both!!
                spinner_numRobots.setVisibility(VISIBLE);
                lifted = false;
            }
            else {
                //not checked
                Log.w(TAG,"Lifted is unchecked.");
                got_lift = false;
                spinner_numRobots.setVisibility(View.GONE);
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
        Log.w(TAG, "@@ RadioClick_Hanging @@");
        radgrp_END = (RadioGroup) findViewById(R.id.radgrp_END);
        int selectedId = radgrp_END.getCheckedRadioButtonId();
//        Log.w(TAG, "*** Selected=" + selectedId);
        radio_Lift = (RadioButton) findViewById(selectedId);
        String value = radio_Lift.getText().toString();
        if (value.equals("Not On")) {        // Not On?
            Log.w(TAG, "None");
            Hang_Num = 0;
        } else if (value.equals("One")){     // One?
            Log.w(TAG, "One");
            Hang_Num = 1;
        } else if (value.equals("Two")){     // Two
            Log.w(TAG, "Two");
            Hang_Num = 2;
        } else {                              // Three
            Log.w(TAG, "Three");
            Hang_Num = 3;
        }
    }

    public class numRobots_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            if (pos > 0) {
                liftedNum = Integer.parseInt(num);
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "Must specify # robots lifted!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            }
            Log.w(TAG, ">>>>> NumRobots '" + liftedNum + "'");
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }



    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeTeleData() {
        Log.w(TAG, ">>>>  storeTeleData  <<<<");
        // New Match Data Object *** GLF 1/26/20
        Pearadox.Match_Data.setTele_PowerCell_LoadSta(PowerCell_LoadSta);
        Pearadox.Match_Data.setTele_PowerCell_floor(PowerCell_floor);

        Pearadox.Match_Data.setTele_got_lift(got_lift);
        Pearadox.Match_Data.setTele_lifted(lifted);
        Pearadox.Match_Data.setTele_liftedNum(liftedNum);
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
                Log.w(TAG, "DEV = NULL" );
        }
        pfDevice_DBReference.child(key).child("phase").setValue(phase);
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