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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class TeleopScoutActivity extends Activity {

    String TAG = "TeleopScoutActivity";      // This CLASS name
    /* Header Sect. */  TextView txt_dev, txt_stud, txt_match, txt_tnum;
    /* L Rocket */      CheckBox chk_LeftRocket_LPan1,chk_LeftRocket_LPan2,chk_LeftRocket_LPan3, chk_LeftRocket_LCarg1,chk_LeftRocket_LCarg2,chk_LeftRocket_LCarg3;
                        CheckBox chk_LeftRocket_RPan1,chk_LeftRocket_RPan2,chk_LeftRocket_RPan3, chk_LeftRocket_RCarg1,chk_LeftRocket_RCarg2,chk_LeftRocket_RCarg3;
    /* PowerCellShip */     CheckBox chk_PowerCellLPan1,chk_PowerCellLPan2,chk_PowerCellLPan3, chk_PowerCellLCarg1,chk_PowerCellLCarg2,chk_PowerCellLCarg3;
                        CheckBox chk_PowerCellRPan1,chk_PowerCellRPan2,chk_PowerCellRPan3, chk_PowerCellRCarg1,chk_PowerCellRCarg2,chk_PowerCellRCarg3;
                        CheckBox chk_PowerCellEndLPanel,chk_PowerCellEndRPanel,chk_PowerCellEndLPowerCell,chk_PowerCellEndRPowerCell;
    /* R Rocket */      CheckBox chk_RghtRocket_LPan1,chk_RghtRocket_LPan2,chk_RghtRocket_LPan3, chk_RghtRocket_LCarg1,chk_RghtRocket_LCarg2,chk_RghtRocket_LCarg3;
                        CheckBox chk_RghtRocket_RPan1,chk_RghtRocket_RPan2,chk_RghtRocket_RPan3, chk_RghtRocket_RCarg1,chk_RghtRocket_RCarg2,chk_RghtRocket_RCarg3;
    /* Comment */       EditText editText_TeleComments;
    /* P/U Sect. */     CheckBox chkBox_PU_PowerCell_floor, chkBox_PowerCellPlayerSta, chkBox_Corral, chkBox_PU_Panel_floor, chkBox_PanelPlayerSta;
    /* HAB */           RadioGroup  radgrp_HAB;      RadioButton  radio_Lift, radio_One, radio_Two, radio_Three, radio_Zero;
                        CheckBox chk_LiftedBy, chk_Lifted;
    /* Last Sect. */    Button button_GoToFinalActivity, button_Number_PenaltiesPlus, button_Number_PenaltiesUndo, btn_DropPlus, btn_DropMinus;
                        TextView txt_Number_Penalties, txt_Num_Dropped;
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private FirebaseDatabase  pfDatabase;
//    private DatabaseReference pfTeam_DBReference;
//    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
//    private DatabaseReference pfCur_Match_DBReference;
    String key = null;
    String tn  = " ";

    // ===================  TeleOps Elements for Match Scout Data object ===================
    // Declare & initialize

// ToDo - remove all rocket widgets
    public boolean LeftRocket_LPan1   = false;  // L-Rocket L-Panel#1
    public boolean LeftRocket_LPan2   = false;  // L-Rocket L-Panel#2
    public boolean LeftRocket_LPan3   = false;  // L-Rocket L-Panel#3
    public boolean LeftRocket_RPan1   = false;  // L-Rocket R-Panel#1
    public boolean LeftRocket_RPan2   = false;  // L-Rocket R-Panel#2
    public boolean LeftRocket_RPan3   = false;  // L-Rocket R-Panel#3
    public boolean LeftRocket_LCarg1  = false; // L-Rocket L-PowerCell#1
    public boolean LeftRocket_LCarg2  = false; // L-Rocket L-PowerCell#2
    public boolean LeftRocket_LCarg3  = false; // L-Rocket L-PowerCell#3
    public boolean LeftRocket_RCarg1  = false; // L-Rocket R-PowerCell#1
    public boolean LeftRocket_RCarg2  = false; // L-Rocket R-PowerCell#2
    public boolean LeftRocket_RCarg3  = false; // L-Rocket R-PowerCell#3

    public boolean PowerCellLPan1         = false; // PowerCell L-Panel#1
    public boolean PowerCellLPan2         = false; // PowerCell L-Panel#2
    public boolean PowerCellLPan3         = false; // PowerCell L-Panel#3
    public boolean PowerCellRPan1         = false; // PowerCell R-Panel#1
    public boolean PowerCellRPan2         = false; // PowerCell R-Panel#2
    public boolean PowerCellRPan3         = false; // PowerCell R-Panel#3
    public boolean PowerCellLCarg1        = false; // PowerCell L-PowerCell#1
    public boolean PowerCellLCarg2        = false; // PowerCell L-PowerCell#2
    public boolean PowerCellLCarg3        = false; // PowerCell L-PowerCell#3
    public boolean PowerCellRCarg1        = false; // PowerCell R-PowerCell#1
    public boolean PowerCellRCarg2        = false; // PowerCell R-PowerCell#2
    public boolean PowerCellRCarg3        = false; // PowerCell R-PowerCell#3
    public boolean PowerCellEndLPanel     = false; // PowerCell End L-Panel#1
    public boolean PowerCellEndLPowerCell     = false; // PowerCell End L-PowerCell#1
    public boolean PowerCellEndRPanel     = false; // PowerCell End R-Panel#1
    public boolean PowerCellEndRPowerCell     = false; // PowerCell End R-PowerCell#1


    public boolean RghtRocket_LPan1   = false;  // R-Rocket L-Panel#1
    public boolean RghtRocket_LPan2   = false;  // R-Rocket L-Panel#2
    public boolean RghtRocket_LPan3   = false;  // R-Rocket L-Panel#3
    public boolean RghtRocket_RPan1   = false;  // R-Rocket R-Panel#1
    public boolean RghtRocket_RPan2   = false;  // R-Rocket R-Panel#2
    public boolean RghtRocket_RPan3   = false;  // R-Rocket R-Panel#3
    public boolean RghtRocket_LCarg1  = false;  // R-Rocket L-PowerCell#1
    public boolean RghtRocket_LCarg2  = false;  // R-Rocket L-PowerCell#2
    public boolean RghtRocket_LCarg3  = false;  // R-Rocket L-PowerCell#3
    public boolean RghtRocket_RCarg1  = false;  // R-Rocket R-PowerCell#1
    public boolean RghtRocket_RCarg2  = false;  // R-Rocket R-PowerCell#2
    public boolean RghtRocket_RCarg3  = false;  // R-Rocket R-PowerCell#3

    public boolean PowerCell_floor        = false;  // Did they pickup PowerCell off the ground?
    public boolean PowerCell_playSta      = false;  // Did they pickup PowerCell from Player Station?
    public boolean PowerCell_Corral       = false;  // Did they pickup PowerCell from Corral?
    public boolean panel_floor        = false;  // Did they pickup panel off the ground?
    public boolean panel_playSta      = false;  // Did they pickup panel off the ground?
    public int end_HAB_Level          = 99;     // HAB Level
    public boolean got_lift           = false;  // Got Lifted by another robot
    public boolean lifted             = false;  // Got Lifted by another robot
    public int num_Penalties          = 0;      // How many penalties received?
    public int num_Dropped            = 0;      // How many Panels dropped?
    /* */
    public String  teleComment        = " ";    // Tele Comment
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
        // Left Rocket
        chk_LeftRocket_LPan1    = (CheckBox) findViewById(R.id.chk_LeftRocket_LPan1);
        chk_LeftRocket_LPan2    = (CheckBox) findViewById(R.id.chk_LeftRocket_LPan2);
        chk_LeftRocket_LPan3    = (CheckBox) findViewById(R.id.chk_LeftRocket_LPan3);
        chk_LeftRocket_RPan1    = (CheckBox) findViewById(R.id.chk_LeftRocket_RPan1);
        chk_LeftRocket_RPan2    = (CheckBox) findViewById(R.id.chk_LeftRocket_RPan2);
        chk_LeftRocket_RPan3    = (CheckBox) findViewById(R.id.chk_LeftRocket_RPan3);
        chk_LeftRocket_LCarg1   = (CheckBox) findViewById(R.id.chk_LeftRocket_LCarg1);
        chk_LeftRocket_LCarg2   = (CheckBox) findViewById(R.id.chk_LeftRocket_LCarg2);
        chk_LeftRocket_LCarg3   = (CheckBox) findViewById(R.id.chk_LeftRocket_LCarg3);
        chk_LeftRocket_RCarg1   = (CheckBox) findViewById(R.id.chk_LeftRocket_RCarg1);
        chk_LeftRocket_RCarg2   = (CheckBox) findViewById(R.id.chk_LeftRocket_RCarg2);
        chk_LeftRocket_RCarg3   = (CheckBox) findViewById(R.id.chk_LeftRocket_RCarg3);
        // PowerCell Ship
        chk_PowerCellLPan1          = (CheckBox) findViewById(R.id.chk_PowerCellLPan1);
        chk_PowerCellLPan2          = (CheckBox) findViewById(R.id.chk_PowerCellLPan2);
        chk_PowerCellLPan3          = (CheckBox) findViewById(R.id.chk_PowerCellLPan3);
        chk_PowerCellRPan1          = (CheckBox) findViewById(R.id.chk_PowerCellRPan1);
        chk_PowerCellRPan2          = (CheckBox) findViewById(R.id.chk_PowerCellRPan2);
        chk_PowerCellRPan3          = (CheckBox) findViewById(R.id.chk_PowerCellRPan3);
        chk_PowerCellLCarg1         = (CheckBox) findViewById(R.id.chk_PowerCellLCarg1);
        chk_PowerCellLCarg2         = (CheckBox) findViewById(R.id.chk_PowerCellLCarg2);
        chk_PowerCellLCarg3         = (CheckBox) findViewById(R.id.chk_PowerCellLCarg3);
        chk_PowerCellRCarg1         = (CheckBox) findViewById(R.id.chk_PowerCellRCarg1);
        chk_PowerCellRCarg2         = (CheckBox) findViewById(R.id.chk_PowerCellRCarg2);
        chk_PowerCellRCarg3         = (CheckBox) findViewById(R.id.chk_PowerCellRCarg3);
        chk_PowerCellEndLPanel      = (CheckBox) findViewById(R.id.chk_PowerCellEndLPanel);
        chk_PowerCellEndRPanel      = (CheckBox) findViewById(R.id.chk_PowerCellEndRPanel);
        chk_PowerCellEndLPowerCell      = (CheckBox) findViewById(R.id.chk_PowerCellEndLPowerCell);
        chk_PowerCellEndRPowerCell      = (CheckBox) findViewById(R.id.chk_PowerCellEndRPowerCell);
        // Right Rocket
        chk_RghtRocket_LPan1    = (CheckBox) findViewById(R.id.chk_RghtRocket_LPan1);
        chk_RghtRocket_LPan2    = (CheckBox) findViewById(R.id.chk_RghtRocket_LPan2);
        chk_RghtRocket_LPan3    = (CheckBox) findViewById(R.id.chk_RghtRocket_LPan3);
        chk_RghtRocket_RPan1    = (CheckBox) findViewById(R.id.chk_RghtRocket_RPan1);
        chk_RghtRocket_RPan2    = (CheckBox) findViewById(R.id.chk_RghtRocket_RPan2);
        chk_RghtRocket_RPan3    = (CheckBox) findViewById(R.id.chk_RghtRocket_RPan3);
        chk_RghtRocket_LCarg1   = (CheckBox) findViewById(R.id.chk_RghtRocket_LCarg1);
        chk_RghtRocket_LCarg2   = (CheckBox) findViewById(R.id.chk_RghtRocket_LCarg2);
        chk_RghtRocket_LCarg3   = (CheckBox) findViewById(R.id.chk_RghtRocket_LCarg3);
        chk_RghtRocket_RCarg1   = (CheckBox) findViewById(R.id.chk_RghtRocket_RCarg1);
        chk_RghtRocket_RCarg2   = (CheckBox) findViewById(R.id.chk_RghtRocket_RCarg2);
        chk_RghtRocket_RCarg3   = (CheckBox) findViewById(R.id.chk_RghtRocket_RCarg3);

        editText_TeleComments   = (EditText) findViewById(R.id.editText_teleComments);
        chkBox_PU_PowerCell_floor   = (CheckBox) findViewById(R.id.chkBox_PU_PowerCell_floor);
        chkBox_PowerCellPlayerSta   = (CheckBox) findViewById(R.id.chkBox_PowerCellPlayerSta);
        chkBox_Corral           = (CheckBox) findViewById(R.id.chkBox_Corral);
        chkBox_PU_Panel_floor   = (CheckBox) findViewById(R.id.chkBox_PU_Panel_floor);
        chkBox_PanelPlayerSta   = (CheckBox) findViewById(R.id.chkBox_PanelPlayerSta);
        radio_Zero              = (RadioButton) findViewById(R.id.radio_Zero);
        radio_One               = (RadioButton) findViewById(R.id.radio_One);
        radio_Two               = (RadioButton) findViewById(R.id.radio_Two);
        radio_Three             = (RadioButton) findViewById(R.id.radio_Three);
        chk_LiftedBy            = (CheckBox) findViewById(R.id.chk_LiftedBy);
        chk_Lifted              = (CheckBox) findViewById(R.id.chk_Lifted);
        button_Number_PenaltiesPlus = (Button) findViewById(R.id.button_Number_PenaltiesPlus);
        button_Number_PenaltiesUndo = (Button) findViewById(R.id.button_Number_PenaltiesUndo);
        btn_DropPlus            = (Button) findViewById(R.id.btn_DropPlus);
        btn_DropMinus           = (Button) findViewById(R.id.btn_DropMinus);
        button_GoToFinalActivity  = (Button)   findViewById(R.id.button_GoToFinalActivity);
        txt_Number_Penalties    = (TextView) findViewById(R.id.txt_Number_Penalties);
        txt_Num_Dropped         = (TextView) findViewById(R.id.txt_Num_Dropped);

        pfDatabase                = FirebaseDatabase.getInstance();            // Firebase
        pfDevice_DBReference      = pfDatabase.getReference("devices");     // List of Devices

        if (Pearadox.Match_Data.isSand_mode()) {
            Toast toast = Toast.makeText(getBaseContext(), "\n\n*** No Autonomous was set - Watch for any scoring in TeleOps ***\n\n", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,  100);
            tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
            radio_Zero.setChecked(true);        // didn't move - so NOT on
            end_HAB_Level = 0;
        }
        carry_over_chks();              // Carry-over check boxes from Autonomous


// *****************************************************************************************
// *****************************************************************************************


    button_GoToFinalActivity.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        Log.w(TAG, "###  Clicked Final  ### " + end_HAB_Level);
            if (end_HAB_Level < 4) {        // Gotta pick one!
                storeTeleData();                    // Put all the TeleOps data collected in Match object
                updateDev("Final");           // Update 'Phase' for stoplight indicator in ScoutMaster

                Intent smast_intent = new Intent(TeleopScoutActivity.this, FinalActivity.class);
                Bundle SMbundle = new Bundle();
                SMbundle.putString("tnum", tn);
                smast_intent.putExtras(SMbundle);
                startActivity(smast_intent);
            } else {
                Log.e(TAG, "ERROR - did not select lift type");
                Toast.makeText(getBaseContext(), "*** End HAB level _MUST_ be selected ***", Toast.LENGTH_LONG).show();
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
    chkBox_PowerCellPlayerSta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PowerCellPlayerSta Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"chkBox_PowerCellPlayerSta is checked.");
                PowerCell_playSta = true;
            } else {  //not checked
                Log.w(TAG,"chkBox_PowerCellPlayerSta is unchecked.");
                PowerCell_playSta = false;
            }
        }
    });
    chkBox_Corral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_Corral Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"chkBox_Corral is checked.");
                PowerCell_Corral = true;
            } else {  //not checked
                Log.w(TAG,"chkBox_Corral is unchecked.");
                PowerCell_Corral = false;
            }
        }
    });

    chkBox_PU_Panel_floor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PU_Panel_floor Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"PU_Panel is checked.");
                panel_floor = true;
            } else {  //not checked
                Log.w(TAG,"PU_Panel is unchecked.");
                panel_floor = false;
            }
        }
    });
        chkBox_PanelPlayerSta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_PanelPlayerSta Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"panel_playSta is checked.");
                    panel_playSta = true;
                } else {  //not checked
                    Log.w(TAG,"panel_playSta is unchecked.");
                    panel_playSta = false;
                }
            }
        });

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
                //chkBox_Platform.setChecked(false);       // Have to be on platform to get lifted!
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
                lifted = false;
            }
            else {
                //not checked
                Log.w(TAG,"Lifted is unchecked.");
                got_lift = false;
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

        btn_DropPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                num_Dropped++;
                Log.w(TAG, "Dropped = " + Integer.toString(num_Dropped));      // ** DEBUG **
                txt_Num_Dropped.setText(Integer.toString(num_Dropped));
            }
        });
        btn_DropMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (num_Dropped >= 1) {     // Don't go below zero
                    num_Dropped--;
                }
                Log.w(TAG, "Dropped = " + Integer.toString(num_Dropped));      // ** DEBUG **
                txt_Num_Dropped.setText(Integer.toString(num_Dropped));
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




    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑  Process _ALL_ the CheckBoxes  ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_LPan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        if (chk_LeftRocket_LPan1.isChecked()) {     //checked
            LeftRocket_LPan1 = true;
        }
        else {          //not checked
            LeftRocket_LPan1 = false;
        }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_LPan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        if (chk_LeftRocket_LPan2.isChecked()) {     //checked
            LeftRocket_LPan2 = true;
        }
        else {          //not checked
            LeftRocket_LPan2 = false;
        }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_LPan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        if (chk_LeftRocket_LPan3.isChecked()) {     //checked
            LeftRocket_LPan3 = true;
        }
        else {          //not checked
            LeftRocket_LPan3 = false;
        }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_RPan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_RPan1.isChecked()) {     //checked
                LeftRocket_RPan1 = true;
            }
            else {          //not checked
                LeftRocket_RPan1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_RPan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_RPan2.isChecked()) {     //checked
                LeftRocket_RPan2 = true;
            }
            else {          //not checked
                LeftRocket_RPan2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_RPan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_RPan3.isChecked()) {     //checked
                LeftRocket_RPan3 = true;
            }
            else {          //not checked
                LeftRocket_RPan3 = false;
            }
        }
    });

    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_LCarg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        if (chk_LeftRocket_LCarg1.isChecked()) {     //checked
            LeftRocket_LCarg1 = true;
        }
        else {          //not checked
            LeftRocket_LCarg1 = false;
        }
    }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_LCarg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        if (chk_LeftRocket_LCarg2.isChecked()) {     //checked
            LeftRocket_LCarg2 = true;
        }
        else {          //not checked
            LeftRocket_LCarg2 = false;
        }
    }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_LCarg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_LCarg3.isChecked()) {     //checked
                LeftRocket_LCarg3 = true;
            }
            else {          //not checked
                LeftRocket_LCarg3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_RCarg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_RCarg1.isChecked()) {     //checked
                LeftRocket_RCarg1 = true;
            }
            else {          //not checked
                LeftRocket_RCarg1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_RCarg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_RCarg2.isChecked()) {     //checked
                LeftRocket_RCarg2 = true;
            }
            else {          //not checked
                LeftRocket_RCarg2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_LeftRocket_RCarg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_LeftRocket_RCarg3.isChecked()) {     //checked
                LeftRocket_RCarg3 = true;
            }
            else {          //not checked
                LeftRocket_RCarg3 = false;
            }
        }
    });

    // RIGHT Rocket
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_LPan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_LPan1.isChecked()) {     //checked
                RghtRocket_LPan1 = true;
            }
            else {          //not checked
                RghtRocket_LPan1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_LPan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_LPan2.isChecked()) {     //checked
                RghtRocket_LPan2 = true;
            }
            else {          //not checked
                RghtRocket_LPan2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_LPan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_LPan3.isChecked()) {     //checked
                RghtRocket_LPan3 = true;
            }
            else {          //not checked
                RghtRocket_LPan3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_RPan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_RPan1.isChecked()) {     //checked
                RghtRocket_RPan1 = true;
            }
            else {          //not checked
                RghtRocket_RPan1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_RPan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_RPan2.isChecked()) {     //checked
                RghtRocket_RPan2 = true;
            }
            else {          //not checked
                RghtRocket_RPan2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_RPan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_RPan3.isChecked()) {     //checked
                RghtRocket_RPan3 = true;
            }
            else {          //not checked
                RghtRocket_RPan3 = false;
            }
        }
    });

    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_LCarg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_LCarg1.isChecked()) {     //checked
                RghtRocket_LCarg1 = true;
            }
            else {          //not checked
                RghtRocket_LCarg1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_LCarg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_LCarg2.isChecked()) {     //checked
                RghtRocket_LCarg2 = true;
            }
            else {          //not checked
                RghtRocket_LCarg2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_LCarg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_LCarg3.isChecked()) {     //checked
                RghtRocket_LCarg3 = true;
            }
            else {          //not checked
                RghtRocket_LCarg3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_RCarg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_RCarg1.isChecked()) {     //checked
                RghtRocket_RCarg1 = true;
            }
            else {          //not checked
                RghtRocket_RCarg1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_RCarg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_RCarg2.isChecked()) {     //checked
                RghtRocket_RCarg2 = true;
            }
            else {          //not checked
                RghtRocket_RCarg2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_RghtRocket_RCarg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_RghtRocket_RCarg3.isChecked()) {     //checked
                RghtRocket_RCarg3 = true;
            }
            else {          //not checked
                RghtRocket_RCarg3 = false;
            }
        }
    });

// PowerCell Ship
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellLPan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellLPan1.isChecked()) {     //checked
                PowerCellLPan1 = true;
            }
            else {          //not checked
                PowerCellLPan1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellLPan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellLPan2.isChecked()) {     //checked
                PowerCellLPan2 = true;
            }
            else {          //not checked
                PowerCellLPan2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellLPan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellLPan3.isChecked()) {     //checked
                PowerCellLPan3 = true;
            }
            else {          //not checked
                PowerCellLPan3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellRPan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellRPan1.isChecked()) {     //checked
                PowerCellRPan1 = true;
            }
            else {          //not checked
                PowerCellRPan1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellRPan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellRPan2.isChecked()) {     //checked
                PowerCellRPan2 = true;
            }
            else {          //not checked
                PowerCellRPan2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellRPan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellRPan3.isChecked()) {     //checked
                PowerCellRPan3 = true;
            }
            else {          //not checked
                PowerCellRPan3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellLCarg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellLCarg1.isChecked()) {     //checked
                PowerCellLCarg1 = true;
            }
            else {          //not checked
                PowerCellLCarg1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellLCarg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellLCarg2.isChecked()) {     //checked
                PowerCellLCarg2 = true;
            }
            else {          //not checked
                PowerCellLCarg2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellLCarg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellLCarg3.isChecked()) {     //checked
                PowerCellLCarg3 = true;
            }
            else {          //not checked
                PowerCellLCarg3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellRCarg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellRCarg1.isChecked()) {     //checked
                PowerCellRCarg1 = true;
            }
            else {          //not checked
                PowerCellRCarg1 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellRCarg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellRCarg2.isChecked()) {     //checked
                PowerCellRCarg2 = true;
            }
            else {          //not checked
                PowerCellRCarg2 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellRCarg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellRCarg3.isChecked()) {     //checked
                PowerCellRCarg3 = true;
            }
            else {          //not checked
                PowerCellRCarg3 = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellEndLPanel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellEndLPanel.isChecked()) {     //checked
                PowerCellEndLPanel = true;
            }
            else {          //not checked
                PowerCellEndLPanel = false;
            }
        }
    });
        // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellEndRPanel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellEndRPanel.isChecked()) {     //checked
                PowerCellEndRPanel = true;
            }
            else {          //not checked
                PowerCellEndRPanel = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellEndLPowerCell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellEndLPowerCell.isChecked()) {     //checked
                PowerCellEndLPowerCell = true;
            }
            else {          //not checked
                PowerCellEndLPowerCell = false;
            }
        }
    });
    // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
    chk_PowerCellEndRPowerCell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if (chk_PowerCellEndRPowerCell.isChecked()) {     //checked
                PowerCellEndRPowerCell = true;
            }
            else {          //not checked
                PowerCellEndRPowerCell = false;
            }
        }
    });



        // === End of OnCreate ===
    }



    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    // Carry-over check boxes from Autonomous to Tele
    private void carry_over_chks() {
        Log.w(TAG, "$$$ carry_over_chks $$$");
        // Left Rocket
        chk_LeftRocket_LPan1.setChecked(Pearadox.Match_Data.isSand_LeftRocket_LPan1());
        chk_LeftRocket_LPan2.setChecked(Pearadox.Match_Data.isSand_LeftRocket_LPan2());
        chk_LeftRocket_LPan3.setChecked(Pearadox.Match_Data.isSand_LeftRocket_LPan3());
        chk_LeftRocket_RPan1.setChecked(Pearadox.Match_Data.isSand_LeftRocket_RPan1());
        chk_LeftRocket_RPan2.setChecked(Pearadox.Match_Data.isSand_LeftRocket_RPan2());
        chk_LeftRocket_RPan3.setChecked(Pearadox.Match_Data.isSand_LeftRocket_RPan3());
        chk_LeftRocket_LCarg1.setChecked(Pearadox.Match_Data.isSand_LeftRocket_LCarg1());
        chk_LeftRocket_LCarg2.setChecked(Pearadox.Match_Data.isSand_LeftRocket_LCarg2());
        chk_LeftRocket_LCarg3.setChecked(Pearadox.Match_Data.isSand_LeftRocket_LCarg3());
        chk_LeftRocket_RCarg1.setChecked(Pearadox.Match_Data.isSand_LeftRocket_RCarg1());
        chk_LeftRocket_RCarg2.setChecked(Pearadox.Match_Data.isSand_LeftRocket_RCarg2());
        chk_LeftRocket_RCarg3.setChecked(Pearadox.Match_Data.isSand_LeftRocket_RCarg3());
        // PowerCell Ship
        chk_PowerCellLPan1.setChecked(Pearadox.Match_Data.isSand_PowerCellLPan1());
        chk_PowerCellLPan2.setChecked(Pearadox.Match_Data.isSand_PowerCellLPan2());
        chk_PowerCellLPan3.setChecked(Pearadox.Match_Data.isSand_PowerCellLPan3());
        chk_PowerCellRPan1.setChecked(Pearadox.Match_Data.isSand_PowerCellRPan1());
        chk_PowerCellRPan2.setChecked(Pearadox.Match_Data.isSand_PowerCellRPan2());
        chk_PowerCellRPan3.setChecked(Pearadox.Match_Data.isSand_PowerCellRPan3());
        chk_PowerCellLCarg1.setChecked(Pearadox.Match_Data.isSand_PowerCellLCarg1());
        chk_PowerCellLCarg2.setChecked(Pearadox.Match_Data.isSand_PowerCellLCarg2());
        chk_PowerCellLCarg3.setChecked(Pearadox.Match_Data.isSand_PowerCellLCarg3());
        chk_PowerCellRCarg1.setChecked(Pearadox.Match_Data.isSand_PowerCellRCarg1());
        chk_PowerCellRCarg2.setChecked(Pearadox.Match_Data.isSand_PowerCellRCarg2());
        chk_PowerCellRCarg3.setChecked(Pearadox.Match_Data.isSand_PowerCellRCarg3());
        chk_PowerCellEndLPanel.setChecked(Pearadox.Match_Data.isSand_PowerCellEndLPanel());
        chk_PowerCellEndRPanel.setChecked(Pearadox.Match_Data.isSand_PowerCellEndRPanel());
        chk_PowerCellEndLPowerCell.setChecked(Pearadox.Match_Data.isSand_PowerCellEndLPowerCell());
        chk_PowerCellEndRPowerCell.setChecked(Pearadox.Match_Data.isSand_PowerCellEndRPowerCell());
        // Right Rocket
        chk_RghtRocket_LPan1.setChecked(Pearadox.Match_Data.isSand_RghtRocket_LPan1());
        chk_RghtRocket_LPan2.setChecked(Pearadox.Match_Data.isSand_RghtRocket_LPan2());
        chk_RghtRocket_LPan3.setChecked(Pearadox.Match_Data.isSand_RghtRocket_LPan3());
        chk_RghtRocket_RPan1.setChecked(Pearadox.Match_Data.isSand_RghtRocket_RPan1());
        chk_RghtRocket_RPan2.setChecked(Pearadox.Match_Data.isSand_RghtRocket_RPan2());
        chk_RghtRocket_RPan3.setChecked(Pearadox.Match_Data.isSand_RghtRocket_RPan3());
        chk_RghtRocket_LCarg1.setChecked(Pearadox.Match_Data.isSand_RghtRocket_LCarg1());
        chk_RghtRocket_LCarg2.setChecked(Pearadox.Match_Data.isSand_RghtRocket_LCarg2());
        chk_RghtRocket_LCarg3.setChecked(Pearadox.Match_Data.isSand_RghtRocket_LCarg3());
        chk_RghtRocket_RCarg1.setChecked(Pearadox.Match_Data.isSand_RghtRocket_RCarg1());
        chk_RghtRocket_RCarg2.setChecked(Pearadox.Match_Data.isSand_RghtRocket_RCarg2());
        chk_RghtRocket_RCarg3.setChecked(Pearadox.Match_Data.isSand_RghtRocket_RCarg3());
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Lifted(View view) {
        Log.w(TAG, "@@ RadioClick_Lifted @@");
        radgrp_HAB = (RadioGroup) findViewById(R.id.radgrp_HAB);
        int selectedId = radgrp_HAB.getCheckedRadioButtonId();
//        Log.w(TAG, "*** Selected=" + selectedId);
        radio_Lift = (RadioButton) findViewById(selectedId);
        String value = radio_Lift.getText().toString();
        if (value.equals("Not On")) {        // Not On?
            Log.w(TAG, "Not On");
            end_HAB_Level = 0;
        } else if (value.equals("One")){     // One?
            Log.w(TAG, "One");
            end_HAB_Level = 1;
        } else if (value.equals("Two")){     // Two
            Log.w(TAG, "Two");
            end_HAB_Level = 2;
        } else {                              // Three
            Log.w(TAG, "Three");
            end_HAB_Level = 3;
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeTeleData() {
        Log.w(TAG, ">>>>  storeTeleData  <<<<");
        // New Match Data Object *** GLF 1/20/19
        Pearadox.Match_Data.setTele_LeftRocket_LPan1(LeftRocket_LPan1);
        Pearadox.Match_Data.setTele_LeftRocket_LPan2(LeftRocket_LPan2);
        Pearadox.Match_Data.setTele_LeftRocket_LPan3(LeftRocket_LPan3);
        Pearadox.Match_Data.setTele_LeftRocket_RPan1(LeftRocket_RPan1);
        Pearadox.Match_Data.setTele_LeftRocket_RPan2(LeftRocket_RPan2);
        Pearadox.Match_Data.setTele_LeftRocket_RPan3(LeftRocket_RPan3);
        Pearadox.Match_Data.setTele_LeftRocket_LCarg1(LeftRocket_LCarg1);
        Pearadox.Match_Data.setTele_LeftRocket_LCarg2(LeftRocket_LCarg2);
        Pearadox.Match_Data.setTele_LeftRocket_LCarg3(LeftRocket_LCarg3);
        Pearadox.Match_Data.setTele_LeftRocket_RCarg1(LeftRocket_RCarg1);
        Pearadox.Match_Data.setTele_LeftRocket_RCarg2(LeftRocket_RCarg2);
        Pearadox.Match_Data.setTele_LeftRocket_RCarg3(LeftRocket_RCarg3);

        Pearadox.Match_Data.setTele_PowerCellLPan1(PowerCellLPan1);
        Pearadox.Match_Data.setTele_PowerCellLPan2(PowerCellLPan2);
        Pearadox.Match_Data.setTele_PowerCellLPan3(PowerCellLPan3);
        Pearadox.Match_Data.setTele_PowerCellRPan1(PowerCellRPan1);
        Pearadox.Match_Data.setTele_PowerCellRPan2(PowerCellRPan2);
        Pearadox.Match_Data.setTele_PowerCellRPan3(PowerCellRPan3);
        Pearadox.Match_Data.setTele_PowerCellLCarg1(PowerCellLCarg1);
        Pearadox.Match_Data.setTele_PowerCellLCarg2(PowerCellLCarg2);
        Pearadox.Match_Data.setTele_PowerCellLCarg3(PowerCellLCarg3);
        Pearadox.Match_Data.setTele_PowerCellRCarg1(PowerCellRCarg1);
        Pearadox.Match_Data.setTele_PowerCellRCarg2(PowerCellRCarg2);
        Pearadox.Match_Data.setTele_PowerCellRCarg3(PowerCellRCarg3);
        Pearadox.Match_Data.setTele_PowerCellEndLPanel(PowerCellEndLPanel);
        Pearadox.Match_Data.setTele_PowerCellEndRPanel(PowerCellEndRPanel);
        Pearadox.Match_Data.setTele_PowerCellEndLPowerCell(PowerCellEndLPowerCell);
        Pearadox.Match_Data.setTele_PowerCellEndRPowerCell(PowerCellEndRPowerCell);

        Pearadox.Match_Data.setTele_RghtRocket_LPan1(RghtRocket_LPan1);
        Pearadox.Match_Data.setTele_RghtRocket_LPan2(RghtRocket_LPan2);
        Pearadox.Match_Data.setTele_RghtRocket_LPan3(RghtRocket_LPan3);
        Pearadox.Match_Data.setTele_RghtRocket_RPan1(RghtRocket_RPan1);
        Pearadox.Match_Data.setTele_RghtRocket_RPan2(RghtRocket_RPan2);
        Pearadox.Match_Data.setTele_RghtRocket_RPan3(RghtRocket_RPan3);
        Pearadox.Match_Data.setTele_RghtRocket_LCarg1(RghtRocket_LCarg1);
        Pearadox.Match_Data.setTele_RghtRocket_LCarg2(RghtRocket_LCarg2);
        Pearadox.Match_Data.setTele_RghtRocket_LCarg3(RghtRocket_LCarg3);
        Pearadox.Match_Data.setTele_RghtRocket_RCarg1(RghtRocket_RCarg1);
        Pearadox.Match_Data.setTele_RghtRocket_RCarg2(RghtRocket_RCarg2);
        Pearadox.Match_Data.setTele_PowerCellEndLPanel(PowerCellEndLPanel);
        Pearadox.Match_Data.setTele_PowerCellEndRPanel(PowerCellEndRPanel);
        Pearadox.Match_Data.setTele_PowerCellEndLPowerCell(PowerCellEndLPowerCell);
        Pearadox.Match_Data.setTele_PowerCellEndRPowerCell(PowerCellEndRPowerCell);

        Pearadox.Match_Data.setTele_PowerCell_floor(PowerCell_floor);
        Pearadox.Match_Data.setTele_PowerCell_Corral(PowerCell_Corral);
        Pearadox.Match_Data.setTele_PowerCell_playSta(PowerCell_playSta);
        Pearadox.Match_Data.setTele_Panel_floor(panel_floor);
        Pearadox.Match_Data.setTele_Panel_playSta(panel_playSta);
        Pearadox.Match_Data.setTele_level_num(end_HAB_Level);
        Pearadox.Match_Data.setTele_got_lift(got_lift);
        Pearadox.Match_Data.setTele_num_Penalties(num_Penalties);
        Pearadox.Match_Data.setTele_num_Dropped(num_Dropped);
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