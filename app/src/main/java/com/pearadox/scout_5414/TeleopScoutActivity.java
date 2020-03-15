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
    /* P/U Sect. */     CheckBox chkBox_PU_PowerCell_floor, chkBox_PowerCellLoadSta, chkBox_PU_Cell_Trench, chkBox_ControlPanel, chkBox_PU_Cell_Boundary, chkBox_GotCell_Robot;
    /* Shoot Sect. */   TextView  txt_OuterClose; Button btn_OuterClosePlus, btn_OuterCloseMinus;  CheckBox checkbox_OuterCloseConsistent;
                        TextView  txt_OuterLine; Button btn_OuterLineMinus, btn_OuterLinePlus;  CheckBox checkbox_OuterLineConsistent;
                        TextView  txt_OuterFrontCP; Button btn_OuterFrontCPMinus, btn_OuterFrontCPPlus;  CheckBox checkbox_OuterFrontCPConsistent;
                        TextView  txt_OuterBackCP; Button btn_OuterBackCPMinus, btn_OuterBackCPPlus;  CheckBox checkbox_OuterBackConsistent;
                        TextView  txt_Bottom; Button btn_BottomMinus, btn_BottomPlus;
                        CheckBox checkbox_CPspin, checkbox_CPcolor;
    /* Climb Sect. */   CheckBox chk_Climbed, chk_Balanced, chk_UnderSG;
                        CheckBox chk_LiftedBy, chk_Lifted; Spinner spinner_numRobots;
                        RadioGroup  radgrp_END;      RadioButton  radio_Lift, radio_One, radio_Two, radio_Three, radio_Zero;
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
    public boolean PowerCell_CP         = false;    // Did they pick up PowerCell from Control Panel
    public boolean PowerCell_Trench     = false;    // Did they get PowerCell from Loading Station
    public boolean PowerCell_Boundary   = false;    // Did they get PowerCell from SG boundary?
    public boolean PowerCell_Robot      = false;    // Get from a Robot?
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

    public boolean Climbed              = false;    // Did they Climb?
    public boolean UnderSG              = false;    // Parked under Shield Generator
    public int     liftedNum            = 0;        // How many lifted?
    public int     Hang_Num             = 99;       // End - How many on Bar (0-3)
    public boolean Balanced             = false;    // SG is Balanced
    public boolean got_lift             = false;    // Got Lifted by another robot
    public boolean lifted               = false;    // Did they lift a robot
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
        chkBox_PU_PowerCell_floor = (CheckBox) findViewById(R.id.chkBox_PU_PowerCell_floor);
        chkBox_PowerCellLoadSta = (CheckBox) findViewById(R.id.chkBox_PowerCellLoadSta);
        chkBox_PU_Cell_Trench   = (CheckBox) findViewById(R.id.chkBox_PU_Cell_Trench);
        chkBox_ControlPanel     = (CheckBox) findViewById(R.id.chkBox_ControlPanel);
        chkBox_PU_Cell_Boundary = (CheckBox) findViewById(R.id.chkBox_PU_Cell_Boundary);
        chkBox_GotCell_Robot    = (CheckBox) findViewById(R.id.chkBox_GotCell_Robot);
        checkbox_CPspin         = (CheckBox) findViewById(R.id.checkbox_CPspin);
        checkbox_CPcolor        = (CheckBox) findViewById(R.id.checkbox_CPcolor);
        chk_Climbed             = (CheckBox) findViewById(R.id.chk_Climbed);
        chk_Balanced            = (CheckBox) findViewById(R.id.chk_Balanced);
        chk_UnderSG             = (CheckBox) findViewById(R.id.chk_UnderSG);
        radio_Zero              = (RadioButton) findViewById(R.id.radio_Zero);
        radio_One               = (RadioButton) findViewById(R.id.radio_One);
        radio_Two               = (RadioButton) findViewById(R.id.radio_Two);
        radio_Three             = (RadioButton) findViewById(R.id.radio_Three);
        chk_LiftedBy            = (CheckBox) findViewById(R.id.chk_LiftedBy);
        chk_Lifted              = (CheckBox) findViewById(R.id.chk_Lifted);
        btn_OuterClosePlus      = (Button) findViewById(R.id.btn_OuterClosePlus);
        btn_OuterCloseMinus     = (Button) findViewById(R.id.btn_OuterCloseMinus);
        btn_OuterLinePlus           = (Button) findViewById(R.id.btn_OuterLinePlus);
        btn_OuterLineMinus          = (Button) findViewById(R.id.btn_OuterLineMinus);
        btn_OuterFrontCPPlus        = (Button) findViewById(R.id.btn_OuterFrontCPPlus);
        btn_OuterFrontCPMinus       = (Button) findViewById(R.id.btn_OuterFrontCPMinus);
        btn_OuterBackCPPlus        = (Button) findViewById(R.id.btn_OuterBackCPPlus);
        btn_OuterBackCPMinus       = (Button) findViewById(R.id.btn_OuterBackCPMinus);
        btn_BottomPlus              = (Button) findViewById(R.id.btn_BottomPlus);
        btn_BottomMinus             = (Button) findViewById(R.id.btn_BottomMinus);
        txt_OuterClose          = (TextView) findViewById(R.id.txt_OuterClose);
        txt_OuterLine               = (TextView) findViewById(R.id.txt_OuterLine);
        txt_OuterFrontCP            = (TextView) findViewById(R.id.txt_OuterFrontCP);
        txt_OuterBackCP            = (TextView) findViewById(R.id.txt_OuterBackCP);
        txt_Bottom                  = (TextView) findViewById(R.id.txt_Bottom);
        checkbox_OuterCloseConsistent = (CheckBox) findViewById(R.id.checkbox_OuterCloseConsistent);
        checkbox_OuterLineConsistent = (CheckBox) findViewById(R.id.checkbox_OuterLineConsistent);
        checkbox_OuterFrontCPConsistent = (CheckBox) findViewById(R.id.checkbox_OuterFrontCPConsistent);
        checkbox_OuterBackConsistent = (CheckBox) findViewById(R.id.checkbox_OuterBackConsistent);
        button_Number_PenaltiesPlus = (Button) findViewById(R.id.button_Number_PenaltiesPlus);
        button_Number_PenaltiesUndo = (Button) findViewById(R.id.button_Number_PenaltiesUndo);
        button_GoToFinalActivity  = (Button)   findViewById(R.id.button_GoToFinalActivity);
        txt_Number_Penalties    = (TextView) findViewById(R.id.txt_Number_Penalties);
        spinner_numRobots = (Spinner) findViewById(R.id.spinner_numRobots);
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
    chkBox_PU_Cell_Trench.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PU_Cell_Trench Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"chkBox_PU_Cell_Trench is checked.");
                PowerCell_Trench = true;
            } else {  //not checked
                Log.w(TAG,"chkBox_PU_Cell_Trench is unchecked.");
                PowerCell_Trench = false;
            }
        }
    });
    chkBox_ControlPanel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        Log.w(TAG, "chkBox_ControlPanel Listener");
        if (buttonView.isChecked()) {
            Log.w(TAG,"chkBox_ControlPanel is checked.");
            PowerCell_CP = true;
        } else {  //not checked
            Log.w(TAG,"chkBox_ControlPanel is unchecked.");
            PowerCell_CP = false;
        }
    }
    });
        chkBox_PU_Cell_Boundary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chkBox_PU_Cell_Boundary Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"chkBox_PU_Cell_Boundary is checked.");
                PowerCell_Boundary = true;
            } else {  //not checked
                Log.w(TAG,"chkBox_PU_Cell_Boundary is unchecked.");
                PowerCell_Boundary = false;
            }
        }
    });
        chkBox_GotCell_Robot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_GotCell_Robot Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"chkBox_GotCell_Robot is checked.");
                    PowerCell_Robot = true;
                } else {  //not checked
                    Log.w(TAG,"chkBox_GotCell_Robot is unchecked.");
                    PowerCell_Robot = false;
                }
            }
        });


        //============================================================================
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

        btn_OuterLinePlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HighLine++;
                Log.w(TAG, "OuterLine = " + Integer.toString(HighLine));      // ** DEBUG **
                txt_OuterLine.setText(Integer.toString(HighLine));
            }
        });
        btn_OuterLineMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HighLine >= 1) {
                    HighLine--;
                }
                Log.w(TAG, "OuterLine = " + Integer.toString(HighLine));      // ** DEBUG **
                txt_OuterLine.setText(Integer.toString(HighLine));
            }
        });

        btn_OuterFrontCPPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "btn_OuterFrontCPPlus Listener");
                HighFrontCP++;
                Log.w(TAG, "OuterFrontCP = " + Integer.toString(HighFrontCP));      // ** DEBUG **
                txt_OuterFrontCP.setText(Integer.toString(HighFrontCP));
            }
        });
        btn_OuterFrontCPMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "btn_OuterFrontCPMinus Listener");
                if (HighFrontCP >= 1) {
                    HighFrontCP--;
                }
                Log.w(TAG, "OuterFrontCP = " + Integer.toString(HighFrontCP));      // ** DEBUG **
                txt_OuterFrontCP.setText(Integer.toString(HighFrontCP));
            }
        });

        btn_OuterBackCPPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HighBackCP++;
                Log.w(TAG, "OuterBackCP = " + Integer.toString(HighBackCP));      // ** DEBUG **
                txt_OuterBackCP.setText(Integer.toString(HighBackCP));
            }
        });
        btn_OuterBackCPMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HighBackCP >= 1) {
                    HighBackCP--;
                }
                Log.w(TAG, "OuterBackCP = " + Integer.toString(HighBackCP));      // ** DEBUG **
                txt_OuterBackCP.setText(Integer.toString(HighBackCP));
            }
        });

        btn_BottomPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Low++;
                Log.w(TAG, "Bottom = " + Integer.toString(Low));      // ** DEBUG **
                txt_Bottom.setText(Integer.toString(Low));
            }
        });
        btn_BottomMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Low >= 1) {
                    Low--;
                }
                Log.w(TAG, "Bottom = " + Integer.toString(Low));      // ** DEBUG **
                txt_Bottom.setText(Integer.toString(Low));
            }
        });

        checkbox_OuterCloseConsistent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "checkbox_OuterCloseConsistent Listener");
                if (buttonView.isChecked()) {
                    conInnerClose = true;
                } else {
                    conInnerClose = false;
                }
                Log.d(TAG, "OuterCloseConsistent " + conInnerClose);
            }
        });
        checkbox_OuterLineConsistent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "checkbox_OuterLineConsistent Listener");
                if (buttonView.isChecked()) {
                    conInnerLine = true;
                } else {
                    conInnerLine = false;
                }
                Log.d(TAG, "OuterLineConsistent " + conInnerLine);
            }
        });
        checkbox_OuterFrontCPConsistent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "checkbox_OuterCloseConsistent Listener");
                if (buttonView.isChecked()) {
                    conInnerFrontCP = true;
                } else {
                    conInnerFrontCP = false;
                }
                Log.d(TAG, "OuterFrontCPConsistent " + conInnerFrontCP);
            }
        });
        checkbox_OuterBackConsistent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "checkbox_OuterBackConsistent Listener");
                if (buttonView.isChecked()) {
                    conInnerBackCP = true;
                } else {
                    conInnerBackCP = false;
                }
                Log.d(TAG, "OuterBackConsistent " + conInnerBackCP);
            }
        });

        checkbox_CPspin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "checkbox_CPspin Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"checkbox_CPspin is checked.");
                    CPspin = true;
                } else {  //not checked
                    Log.w(TAG,"checkbox_CPspin is unchecked.");
                    CPspin = false;
                }
            }
        });
        checkbox_CPcolor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "checkbox_CPcolor Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"checkbox_CPcolor is checked.");
                    CPcolor = true;
                } else {  //not checked
                    Log.w(TAG,"checkbox_CPcolor is unchecked.");
                    CPcolor = false;
                }
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
            chk_UnderSG.setEnabled(false);
            chk_UnderSG.setChecked(false);
            UnderSG = false;
            chk_LiftedBy.setChecked(false);       // Can't be both!!
            chk_LiftedBy.setEnabled(false);
            radio_Zero.setEnabled(false);
            got_lift = false;
        } else {  //not checked
            Log.w(TAG,"Climbed is unchecked.");
            Climbed = false;
            chk_UnderSG.setEnabled(true);
            chk_LiftedBy.setEnabled(true);
            radio_Zero.setEnabled(true);
        }
        }
    });

    chk_Balanced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            Log.w(TAG, "chk_Balanced Listener");
            if (buttonView.isChecked()) {
                Log.w(TAG,"Balanced is checked.");

                Balanced = true;
            } else {  //not checked
                Log.w(TAG,"Balanced is unchecked.");
                Balanced = false;
            }
        }
    });

        chk_UnderSG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chk_UnderSG Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG, "UnderSG is checked.");
                    UnderSG = true;
                    Climbed = false;
                    Balanced = false;
                    chk_Climbed.setEnabled(false);
                    chk_Climbed.setChecked(false);
                    chk_LiftedBy.setEnabled(false);
                    chk_LiftedBy.setChecked(false);
                    chk_Lifted.setEnabled(false);
                    chk_Lifted.setChecked(false);
                    chk_Balanced.setEnabled(false);
                    chk_Balanced.setChecked(false);

                } else {  //not checked
                    Log.w(TAG, "UnderSG is unchecked.");
                    UnderSG = false;
                    chk_Climbed.setEnabled(true);
                    chk_LiftedBy.setEnabled(true);
                    chk_Lifted.setEnabled(true);
                    chk_Balanced.setEnabled(true);
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
                    chk_UnderSG.setChecked(false);
                    chk_UnderSG.setEnabled(false);      // Can't also be 'Parked'
                    UnderSG = false;
                    chk_Climbed.setEnabled(false);
                    Climbed = false;
                } else {
                    //not checked
                    Log.w(TAG,"LiftedBy is unchecked.");
                    if (!chk_Climbed.isChecked()) {
                        got_lift = false;
                        chk_UnderSG.setEnabled(true);
                        chk_Climbed.setEnabled(true);
                    }
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
                lifted = true;
                chk_LiftedBy.setChecked(false);       // Can't be both!!
                spinner_numRobots.setVisibility(VISIBLE);
                got_lift = false;
                chk_UnderSG.setEnabled(false);      // Can't also be 'Parked'
            } else {
                //not checked
                Log.w(TAG,"Lifted is unchecked.");
                lifted = false;
                spinner_numRobots.setVisibility(View.GONE);
                chk_UnderSG.setEnabled(true);
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
        Log.d(TAG, "*** Selected=" + selectedId + " Lift="+ liftedNum);
        radio_Lift = (RadioButton) findViewById(selectedId);
        String value = radio_Lift.getText().toString();
        if (value.equals("None")) {             // Not On?
            Log.w(TAG, "None");
            Hang_Num = 0;
            chk_UnderSG.setEnabled(true);
            chk_Balanced.setEnabled(false);
        } else if (value.equals("One")){        // One?
            Log.w(TAG, "One");
            if (liftedNum > 0) {
                Toast toast = Toast.makeText(getBaseContext(), "Lifted robots='" + liftedNum + "' Please correct # Hanging.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                Hang_Num = 1;
            }
            chk_UnderSG.setEnabled(false);
            chk_Balanced.setEnabled(true);
        } else if (value.equals("Two")){        // Two
            Log.w(TAG, "Two");
            if (liftedNum > 1) {
                Toast toast = Toast.makeText(getBaseContext(), "Lifted robots='" + liftedNum + "' Please correct # Hanging.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                Hang_Num = 2;
            }
            chk_UnderSG.setEnabled(false);
            chk_Balanced.setEnabled(true);
        } else {                                // Three
            Log.w(TAG, "Three");
            if (!UnderSG) {
                Log.d(TAG, "Hanging=3");
                Hang_Num = 3;
                UnderSG = false;
                chk_UnderSG.setChecked(false);
                chk_UnderSG.setEnabled(false);
                chk_Balanced.setEnabled(true);
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "'Under SG' is checked;  pick # Hanging less than 3!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            }
        }
    }

    public class numRobots_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Log.w(TAG, "###  numRobots_OnItemSelectedListener  ###");
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            if (pos > 0) {
                liftedNum = Integer.parseInt(num);
                spinner_numRobots = (Spinner) findViewById(R.id.spinner_numRobots);
                switch (liftedNum) {
                    case 1:
                        Log.w(TAG, "** 1 ** Robots Lifted '" + liftedNum + "'  Hang=" + Hang_Num);
                        if ((Hang_Num == 99) || (Hang_Num < 2)) {
                            Toast toast = Toast.makeText(getBaseContext(), "'# Hanging less than 2!  Please correct.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                        break;
                    case 2:
                        Log.w(TAG, "** 2 ** Robots Lifted '" + liftedNum + "' Hang=" + Hang_Num);
                        if ((Hang_Num == 99) || (Hang_Num < 3)) {
//                            spinner_numRobots.setSelection(3, false);
                            Toast toast = Toast.makeText(getBaseContext(), "'# Hanging less than 3!  Please correct.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                        break;
                }
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
        Pearadox.Match_Data.setTele_PowerCell_CP(PowerCell_CP);
        Pearadox.Match_Data.setTele_PowerCell_Trench(PowerCell_Trench);
        Pearadox.Match_Data.setTele_PowerCell_Boundary(PowerCell_Boundary);
        Pearadox.Match_Data.setTele_PowerCell_Robot(PowerCell_Robot);
        Pearadox.Match_Data.setTele_Low(Low);
        Pearadox.Match_Data.setTele_HighClose(HighClose);
        Pearadox.Match_Data.setTele_HighLine(HighLine);
        Pearadox.Match_Data.setTele_HighFrontCP(HighFrontCP);
        Pearadox.Match_Data.setTele_HighBackCP(HighBackCP);
        Pearadox.Match_Data.setTele_conInnerClose(conInnerClose);
        Pearadox.Match_Data.setTele_conInnerLine(conInnerLine);
        Pearadox.Match_Data.setTele_conInnerFrontCP(conInnerFrontCP);
        Pearadox.Match_Data.setTele_conInnerBackCP(conInnerBackCP);
        Pearadox.Match_Data.setTele_CPspin(CPspin);
        Pearadox.Match_Data.setTele_CPcolor(CPcolor);
        Pearadox.Match_Data.setTele_Climbed(Climbed);
        Pearadox.Match_Data.setTele_UnderSG(UnderSG);
        Pearadox.Match_Data.setTele_Hang_num(Hang_Num);
        Pearadox.Match_Data.setTele_Balanced(Balanced);
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