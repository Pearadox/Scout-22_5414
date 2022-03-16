package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MatchScoutActivity extends AppCompatActivity {

    String TAG = "MatchScout_Activity";      // This CLASS name
    boolean onStart = false;
    /* Header Sect. */  TextView txt_EventName, txt_dev, txt_stud, txt_Match, txt_MyTeam, txt_TeamName, txt_NextMatch;
                        EditText editTxt_Team, editTxt_Match;
    /* Pre-Match */     RadioGroup radgrp_startPiece; RadioButton radio_startNone, radio_start1, radio_start2, radio_Pick;
                        Spinner spinner_startPos;
    /* After Start */   CheckBox checkbox_noAUTO, checkbox_leftTarmac, checkbox_aquiredCargo;
                        CheckBox chkBox_PU_Cargo_floor, chkBox_PU_Cargo_terminal, chkBox_HumanPlayer;
    /* Upper Hub */     TextView txt_UpperHub, txt_MissedUpper; Button btn_UpperHubPlus, btn_UpperHubMinus, btn_MissedUpperMinus, btn_MissedUpperPlus;
    /* Lower Hub */     TextView txt_Lower, txt_MissedLower; Button btn_LowerMinus, btn_LowerPlus, btn_MissedLowerMinus, btn_MissedLowerPlus;
    /* Last Sect. */    EditText editText_autoComment;

    protected Vibrator vibrate;
    long[] once = { 0, 100 };
    long[] twice = { 0, 100, 400, 100 };
    long[] thrice = { 0, 100, 400, 100, 400, 100 };
    public static final int ZERO = 0;
    boolean NoShow = false;
    public static String device = " ";
    private Button button_GoToTeleopActivity, button_GoToArenaLayoutActivity, button_dropMinus, button_dropPlus;
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    String key = null;      // key for Devices Firebase
    ArrayAdapter<String> adapter_autostartpos;
    private final static int MY_REQUEST_CODE = 99;
    String tarmacPos = "";

    // ===================  Autonomous Elements for Match Scout Data object ===================
    // Declare & initialize
    public String matchID               = "T00";    // Type + #
    public String tn                    = "";       // Team #
    public int PlayerSta                = 0;        // Player Station

    public int cargo_carried            = -1;       // #Cargo carried
    public String  startPos             = " ";      // Start Position
    // ---- AFTER Start ----
    public boolean noAuto               = false;    // Do they have Autonomous mode?
    public boolean leftTarmac           = false;    // Did they leave Tarmac
    public boolean acquCargo            = false;    // Did they acquire Cargo
    private boolean CollectFloor        = false;    // Collect from Floor?
    private boolean CollectTerminal     = false;    // Collect from Terminal?
    private boolean Human               = false;    // Score by Human?
    private int     Low                 = 0;        // # Low Goal balls
    private int     High                = 0;        // # High Goal balls
    private int     MissedLow           = 0;        // # Missed Low Goal balls
    private int     MissedHigh          = 0;        // # Missed High Goal balls

    /* */
    public String autoComment = " ";        // Comment
    public static String studID = " ";

// ===========================================================================



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Match Scout >>");
        onStart = false;
        setContentView(R.layout.activity_match_scout);
        Bundle bundle = this.getIntent().getExtras();
        device = bundle.getString("dev");
        studID = bundle.getString("stud");
        Log.w(TAG, device + " " + studID);      // ** DEBUG **
        String ps = device.substring(device.length() - 1);
        PlayerSta = Integer.valueOf(ps);
//
        tn = bundle.getString("tnum");
        Pearadox.MatchData_Saved = false;    // Set flag to show need to saved
        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_EventName.setText(Pearadox.FRC_EventName);          // Event Name

        pfDatabase = FirebaseDatabase.getInstance();
        pfTeam_DBReference = pfDatabase.getReference("teams");              // Tteam data from Firebase D/B
//        pfStudent_DBReference = pfDatabase.getReference("students");        // List of Students
        pfMatch_DBReference = pfDatabase.getReference("matches");           // List of Matches
        pfCur_Match_DBReference = pfDatabase.getReference("current-match"); // _THE_ current Match
        pfDevice_DBReference = pfDatabase.getReference("devices");          // List of Devices
        updateDev("Auto");      // Update 'Phase' for stoplight indicator in ScoutMaster

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;
        int pct = (int) (batteryPct * 100);
        String batpct = String.valueOf(pct);
        Log.w(TAG, "Battery=" + batteryPct + "  " + batpct);      // ** DEBUG **
        switch (Pearadox.FRC514_Device) {
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
            default:                //
                Log.d(TAG, "DEV = NULL");
        }
        Log.w(TAG, "batt_stat=" + key + "  " + batpct);      // ** DEBUG **
        if (Pearadox.is_Network) {
            pfDevice_DBReference.child(key).child("batt_stat").setValue(batpct);
        }

        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_stud);
        txt_Match = (TextView) findViewById(R.id.txt_Match);
        txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
        txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
        editTxt_Match = (EditText) findViewById(R.id.editTxt_Match);
        editTxt_Team = (EditText) findViewById(R.id.editTxt_Team);
        editText_autoComment = (EditText) findViewById(R.id.editText_autoComment);
        ImageView imgScoutLogo = (ImageView) findViewById(R.id.imageView_MS);
        txt_dev.setText(device);
        txt_stud.setText(studID);
        txt_Match.setText("");
        if (Pearadox.is_Network && Pearadox.numTeams > 0) {      // is Internet available and Teams there?
            txt_MyTeam.setText("");
            editTxt_Match.setVisibility(View.INVISIBLE);
            editTxt_Match.setEnabled(false);
            editTxt_Team.setVisibility(View.INVISIBLE);
            editTxt_Team.setEnabled(false);
        } else {
            Log.d(TAG, "No Internet for Match & Team  " + Pearadox.numTeams);
            editTxt_Match.setVisibility(View.VISIBLE);
            editTxt_Match.setEnabled(true);
            editTxt_Match.requestFocus();        // Don't let EditText mess up layout!!
            editTxt_Match.setFocusable(true);
            editTxt_Match.setFocusableInTouchMode(true);
            editTxt_Match.requestFocus();        // Don't let EditText mess up layout!!
            txt_Match.setText("Q");         // Default to qualifying
            editTxt_Team.setVisibility(View.VISIBLE);
            editTxt_Team.setEnabled(true);
            editTxt_Team.setFocusable(true);
            editTxt_Team.setFocusableInTouchMode(true);
            txt_MyTeam.setVisibility(View.GONE);
            txt_TeamName.setVisibility(View.GONE);


// ******************
            editTxt_Match.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d(TAG, " editTxt_Match listener; Match = " + editTxt_Match.getText());
                    if (editTxt_Match.getText().length() < 3) {
                        vibrate.vibrate(twice, -1);
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        Toast.makeText(getBaseContext(), "*** Match number must be at least 3 characters  *** ", Toast.LENGTH_LONG).show();
                    } else {
                        matchID = "Q" + (String.valueOf(editTxt_Match.getText()));
                        editTxt_Team.requestFocus();    // Go to Team# next
                    }
                    Log.e(TAG, " Match ID = " + matchID);
                    editTxt_Match.setNextFocusDownId(R.id.editTxt_Team);
                    editTxt_Team.requestFocus();    // Go to Team# next
                    return true;
                }
                return false;
            }
            });

            editTxt_Team.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Log.d(TAG, " editTxt_Team listener; Team = " + editTxt_Team.getText());
                        if (editTxt_Team.getText().length() < 2 || editTxt_Team.getText().length() > 4) {
                            vibrate.vibrate(twice, -1);
                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            Toast.makeText(getBaseContext(), "*** Team number must be at least 2 characters and no more than 4  *** ", Toast.LENGTH_LONG).show();
                        } else {
                            tn = (String.valueOf(editTxt_Team.getText()));
                        }
                        Log.e(TAG, " Team # = " + tn);
                        return true;
                    }
                    return false;
                }
            });
        } //End if

        txt_TeamName.setText("");
        String devcol = device.substring(0, 3);
        Log.d(TAG, "color=" + devcol);
        if (devcol.equals("Red")) {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.red_scout));
        } else {
            imgScoutLogo.setImageDrawable(getResources().getDrawable(R.drawable.blue_scout));
        }

        checkbox_noAUTO             = (CheckBox) findViewById(R.id.checkbox_noAUTO);
        checkbox_leftTarmac         = (CheckBox) findViewById(R.id.checkbox_leftTarmac);
        checkbox_aquiredCargo       = (CheckBox) findViewById(R.id.checkbox_aquiredCargo);
        chkBox_PU_Cargo_floor       = (CheckBox) findViewById(R.id.chkBox_PU_Cargo_floor);
        chkBox_PU_Cargo_terminal    = (CheckBox) findViewById(R.id.chkBox_PU_Cargo_terminal);
        chkBox_HumanPlayer          = (CheckBox) findViewById(R.id.chkBox_HumanPlayer);
        txt_UpperHub                = (TextView) findViewById(R.id.txt_UpperHub);
        txt_Lower                   = (TextView) findViewById(R.id.txt_Lower);
        btn_UpperHubPlus            = (Button) findViewById(R.id.btn_UpperHubPlus);
        btn_UpperHubMinus           = (Button) findViewById(R.id.btn_UpperHubMinus);
        btn_LowerPlus               = (Button) findViewById(R.id.btn_LowerPlus);
        btn_LowerMinus              = (Button) findViewById(R.id.btn_LowerMinus);
        txt_MissedUpper             = (TextView) findViewById(R.id.txt_MissedUpper);
        txt_MissedLower             = (TextView) findViewById(R.id.txt_MissedLower);
        btn_MissedUpperPlus         = (Button) findViewById(R.id.btn_MissedUpperPlus);
        btn_MissedUpperMinus        = (Button) findViewById(R.id.btn_MissedUpperMinus);
        btn_MissedLowerPlus         = (Button) findViewById(R.id.btn_MissedLowerPlus);
        btn_MissedLowerMinus        = (Button) findViewById(R.id.btn_MissedLowerMinus);
        button_GoToTeleopActivity = (Button) findViewById(R.id.button_GoToTeleopActivity);
        button_GoToArenaLayoutActivity = (Button) findViewById(R.id.button_GoToArenaLayoutActivity);
        final Spinner spinner_startPos = (Spinner) findViewById(R.id.spinner_startPos);
        String[] autostartPos = getResources().getStringArray(R.array.auto_start_array);
        adapter_autostartpos = new ArrayAdapter<String>(this, R.layout.start_pos_list_layout, autostartPos);
        adapter_autostartpos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_startPos.setAdapter(adapter_autostartpos);
        spinner_startPos.setSelection(0, false);
        spinner_startPos.setOnItemSelectedListener(new startPosOnClickListener());



// Start Listeners
        // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
        // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑  Process _ALL_ the CheckBoxes  ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑
        // ☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑☑

        checkbox_noAUTO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             Log.w(TAG, "checkbox_noAUTO Listener");
                 if (buttonView.isChecked()) {
                     //checked
                     Log.w(TAG, "No Auto is checked.");
                     noAuto = true;
                     // ToDo - turn ON/OFF correct widgets
                     checkbox_leftTarmac.setChecked(false);
                     checkbox_leftTarmac.setEnabled(false);
                     if (!NoShow) {         // Leave message as is if No Show
                        editText_autoComment.setText("No Autonomous activity - didn't move");
                        autoComment = "No Autonomous activity - didn't move";
                     }

                 } else {
                     //not checked
                     Log.w(TAG, "No Auto is unchecked.");
                     noAuto = false;

                     checkbox_leftTarmac.setEnabled(true);
                     editText_autoComment.setText(" ");
                     autoComment = " ";
                 }
             }
         });

        checkbox_leftTarmac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.w(TAG, "checkbox_leftTarmac Listener");
            if (buttonView.isChecked()) {
                    leftTarmac = true;
                } else {
                    leftTarmac = false;
                }
            }
        });

        checkbox_aquiredCargo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "checkbox_aquiredCargo Listener");
                if (buttonView.isChecked()) {
                    acquCargo = true;
                } else {
                    acquCargo = false;
                }
            }
        });


        chkBox_PU_Cargo_floor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             Log.w(TAG, "chkBox_PU_Cargo_floor Listener");
                 if (buttonView.isChecked()) {
                     CollectFloor = true;
                 } else {
                     CollectFloor = false;
                 }
            }
        });


        chkBox_PU_Cargo_terminal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "chkBox_PU_Cargo_terminal Listener");
                if (buttonView.isChecked()) {
                    CollectTerminal = true;
                } else {
                    CollectTerminal = false;
                }
            }
        });


        chkBox_HumanPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG, "chkBox_HumanPlayer Listener");
                if (buttonView.isChecked()) {
                    Human = true;
                } else {
                    Human = false;
                }
            }
        });


//*****************************************************************
        btn_UpperHubPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                High++;
                Log.w(TAG, "UpperHubPlus = " + Integer.toString(High));      // ** DEBUG **
                txt_UpperHub.setText(Integer.toString(High));    // Perform action on click
            }
        });
        btn_UpperHubMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (High >= 1) {
                    High--;
                }
                Log.w(TAG, "OuterUnder = " + Integer.toString(High));      // ** DEBUG **
                txt_UpperHub.setText(Integer.toString(High));    // Perform action on click
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
//******* MISSED ***************
        btn_MissedUpperPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MissedHigh++;
                Log.w(TAG, "btn_MissedUpperPlus = " + Integer.toString(MissedHigh));      // ** DEBUG **
                txt_MissedUpper.setText(Integer.toString(MissedHigh));    // Perform action on click
            }
        });
        btn_MissedUpperMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MissedHigh >= 1) {      // Don't go negative
                    MissedHigh--;
                }
                Log.w(TAG, "btn_MissedUpperMinus = " + Integer.toString(MissedHigh));      // ** DEBUG **
                txt_MissedUpper.setText(Integer.toString(MissedHigh));    // Perform action on click
            }
        });

        btn_MissedLowerPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MissedLow++;
                Log.w(TAG, "Bottom = " + Integer.toString(MissedLow));      // ** DEBUG **
                txt_MissedLower.setText(Integer.toString(MissedLow));
            }
        });
        btn_MissedLowerMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MissedLow >= 1) {
                    MissedLow--;
                }
                Log.w(TAG, "Bottom = " + Integer.toString(MissedLow));      // ** DEBUG **
                txt_MissedLower.setText(Integer.toString(MissedLow));
            }
        });

        //========================================================================
        button_GoToTeleopActivity.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Log.w(TAG, "Clicked 'NEXT/TeleOps' Button  match=" + matchID);
            if (matchID.length() < 2) {     // Between matches??
                Toast.makeText(getBaseContext(), "*** Match has _NOT_ started; wait until you have a Team #  *** ", Toast.LENGTH_LONG).show();

            } else {        // It's OK - Match has started

                Log.e(TAG, "*** TeleOps - #Cargo=" + cargo_carried + "  Start=" + spinner_startPos.getSelectedItemPosition());
                // ToDo - check to see if ALL required fields entered (Start-pos, cargo, ....)
                    if ((noAuto==false) &&
                            ((cargo_carried < ZERO)  ||
                            (spinner_startPos.getSelectedItemPosition() == 0))) {  //Required fields

                        Toast.makeText(getBaseContext(), "\t*** Select _ALL_ required fields!  ***\n Starting Position, # Cargo ", Toast.LENGTH_LONG).show();
                        if (spinner_startPos.getSelectedItemPosition() == 0) {
                            spinner_startPos.performClick();
                        }
                    } else {

                        if (tn != null) {
                            updateDev("Tele");      // Update 'Phase' for stoplight indicator in ScoutMaster
                            storeAutoData();        // Put all the Autonomous data collected in Match object

                            Intent smast_intent = new Intent(MatchScoutActivity.this, TeleopScoutActivity.class);
                            Bundle SMbundle = new Bundle();
                            SMbundle.putString("tnum", tn);
                            smast_intent.putExtras(SMbundle);
                            startActivity(smast_intent);
                        } else {
                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            Toast.makeText(getBaseContext(), "*** Team # not entered  *** ", Toast.LENGTH_LONG).show();
                        }
                    }
            }
        }
    });

        button_GoToArenaLayoutActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Clicked Sidebar");
                Intent smast_intent = new Intent(MatchScoutActivity.this, ArenaLayoutActivity.class);
                Bundle SMbundle = new Bundle();
                SMbundle.putString("dev", device);              // Pass data
                SMbundle.putString("stud", studID);             //  to activity
                smast_intent.putExtras(SMbundle);
                startActivityForResult(smast_intent, MY_REQUEST_CODE);
        }
        });

        // *******************************************************************

        editText_autoComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w(TAG, "******  onTextChanged TextWatcher  ******" + s);
                autoComment = String.valueOf(s);
                Log.d(TAG, "AutoComment: " + editText_autoComment.getText() + " " + autoComment);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.w(TAG, "******  beforeTextChanged TextWatcher  ******");
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG, "******  onTextChanged TextWatcher  ******" + s );
                autoComment = String.valueOf(s);
                Log.d(TAG, "AutoComment: " + editText_autoComment.getText() + " " + autoComment);
            }
        });


        // === End of OnCreate ===
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public void RadioClick_Piece(View view) {
        Log.w(TAG, "@@ RadioClick_Piece @@");
        radgrp_startPiece = (RadioGroup) findViewById(R.id.radgrp_startPiece);
        int selectedId = radgrp_startPiece.getCheckedRadioButtonId();
//        Log.w(TAG, "*** Selected=" + selectedId);
        radio_Pick = (RadioButton) findViewById(selectedId);
        String value = radio_Pick.getText().toString();
        if (value.equals("None")) {        // None
            Log.w(TAG, "None");
            cargo_carried = 0;
        }
        if (value.equals("1")) {        // 1
            Log.w(TAG, "One");
            cargo_carried = 1;
        }
        if (value.equals("2")) {        // 2
            Log.w(TAG, "Two");
            cargo_carried = 2;
        }
        if (value.equals("3")) {        // 2
            Log.w(TAG, "Three");
            cargo_carried = 3;
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeAutoData() {
        Log.i(TAG, ">>>>  storeAutoData  <<<< " + studID);
        Pearadox.Match_Data.setMatch(matchID);
        Pearadox.Match_Data.setTeam_num(tn);
        Pearadox.Match_Data.setPre_PlayerSta(PlayerSta);
        Pearadox.Match_Data.setPre_cargo_carried(cargo_carried);
        Pearadox.Match_Data.setPre_startPos(startPos);

        Pearadox.Match_Data.setAuto_aquCargo(acquCargo);
        Pearadox.Match_Data.setAuto_CollectFloor(CollectFloor);
        Pearadox.Match_Data.setAuto_CollectTerm(CollectTerminal);
        Pearadox.Match_Data.setAuto_Human(Human);
        Pearadox.Match_Data.setAuto_mode(noAuto);
        Pearadox.Match_Data.setAuto_leftTarmac(leftTarmac);
        Pearadox.Match_Data.setAuto_Low(Low);
        Pearadox.Match_Data.setAuto_High(High);
        Pearadox.Match_Data.setAuto_MissedLow(MissedLow);
        Pearadox.Match_Data.setAuto_MissedHigh(MissedHigh);

        // --------------
        Pearadox.Match_Data.setAuto_comment(autoComment);
        Pearadox.Match_Data.setFinal_studID(device.substring(0,1) + device.substring(device.length()-1,device.length()) + "-"  + studID);
        Log.w(TAG, "*******  All done with AUTO setters!!");
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private void getMatch() {
        Log.d(TAG, "%%%%  getMatch  %%%%");
        txt_NextMatch = (TextView) findViewById(R.id.txt_NextMatch);
        txt_NextMatch.setText("");
        if (Pearadox.is_Network) {
            pfCur_Match_DBReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "Current Match - onDataChange  %%%%");
                    txt_Match = (TextView) findViewById(R.id.txt_Match);
                    txt_MyTeam = (TextView) findViewById(R.id.txt_MyTeam);
                    txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                    while (iterator.hasNext()) {
                        p_Firebase.curMatch match_Obj = iterator.next().getValue(p_Firebase.curMatch.class);
                        matchID = match_Obj.getCur_match();
                        Log.d(TAG, "***>  Current Match = " + matchID + " " + match_Obj.getR1() + " " + match_Obj.getB3());
                        if (matchID.length() < 3) {
//                        Log.d(TAG, "MatchID NULL");
                            txt_Match.setText(" ");
                            txt_MyTeam.setText(" ");
                            txt_TeamName.setText(" ");
                            updateDev("Auto");      // Update 'Phase' for stoplight indicator in ScoutMaster
                        } else {        // OK!!  Match has started
//                        Log.d(TAG, "Match started " + matchID);
                            txt_Match.setText(matchID);
//                        Log.d(TAG, "Device = " + Pearadox.FRC514_Device + " ->" + onStart);
                            txt_NextMatch.setText(match_Obj.getOur_matches());
                            switch (Pearadox.FRC514_Device) {          // Who am I?!?
                                case ("Red-1"):             //#Red or Blue Scout
                                    txt_MyTeam.setText(match_Obj.getR1());
                                    break;
                                case ("Red-2"):             //#
                                    txt_MyTeam.setText(match_Obj.getR2());
                                    break;
                                case ("Red-3"):             //#
                                    txt_MyTeam.setText(match_Obj.getR3());
                                    break;
                                case ("Blue-1"):            //#
                                    txt_MyTeam.setText(match_Obj.getB1());
                                    break;
                                case ("Blue-2"):            //#
                                    txt_MyTeam.setText(match_Obj.getB2());
                                    break;
                                case ("Blue-3"):            //#####
                                    txt_MyTeam.setText(match_Obj.getB3());
                                    break;
                                default:                //
                                    Log.d(TAG, "device is _NOT_ a Scout ->" + device);
                            }
                            tn = (String) txt_MyTeam.getText();
                            findTeam(tn);   // Find Team info
                            txt_TeamName.setText(team_inst.getTeam_name());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    /*listener failed or was removed for security reasons*/
                }
            });
        } else {
            Log.e(TAG, "***  No Internet for Matches  ***");

        }
    }

    private void findTeam(String tnum) {
        Log.i(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
//                Log.d(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        if (!found) {
            Log.e(TAG, "****** ERROR - Team _NOT_ found!! = " + tnum);
            txt_TeamName.setText("");
        }
    }
    private void updateDev(String phase) {     //
        Log.i(TAG, "#### updateDev #### " + phase);
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
                Log.d(TAG, "DEV = NULL" );
        }
             pfDevice_DBReference.child(key).child("phase").setValue(phase);
    }

    private class startPosOnClickListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            startPos = parent.getItemAtPosition(pos).toString();
            Log.d(TAG, ">>>>>  '" + startPos + "'");
            checkbox_noAUTO = (CheckBox) findViewById(R.id.checkbox_noAUTO);
            editText_autoComment = (EditText) findViewById(R.id.editText_autoComment);
            final Spinner spinner_startPos = (Spinner) findViewById(R.id.spinner_startPos);
            if (spinner_startPos.getSelectedItemPosition() == 7) {  //  No Show?
                Log.e(TAG, "### Team/robot is a No Show ###" );
                NoShow = true;
                editText_autoComment.setText("### Team/robot is a No Show ###");
                autoComment = "### Team/robot is a No Show ###";
                cargo_carried = 0;      //
                Log.d(TAG, "No Show" + editText_autoComment.getText() + " " + autoComment);
                ;
                checkbox_noAUTO.setChecked(true);
                // ToDo ????? - Do we want to turn off all other widgets?
            }
            if (spinner_startPos.getSelectedItemPosition() >= 1 && spinner_startPos.getSelectedItemPosition() <= 5 ) {
                checkbox_noAUTO.setChecked(false);                            // un-check if old value was NoShow
                NoShow = false;
            }
            if (spinner_startPos.getSelectedItemPosition() == 0) {          // reset to start
                checkbox_noAUTO.setChecked(false);                            // un-check if old value was NoShow
                NoShow = false;
            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
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
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == ArenaLayoutActivity.class.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null)
                tarmacPos = (data.getStringExtra("value"));
                spinner_startPos = (Spinner) findViewById(R.id.spinner_startPos);
                switch (tarmacPos) {
                    case ("1"):
                        spinner_startPos.setSelection(1);
                        break;
                    case ("2"):
                        spinner_startPos.setSelection(2);
                        break;
                    case ("3"):
                        spinner_startPos.setSelection(3);
                        break;
                    case ("4"):
                        spinner_startPos.setSelection(4);
                        break;
                    case ("5"):
                        spinner_startPos.setSelection(5);
                        break;
                    case ("6"):
                        spinner_startPos.setSelection(6);
                        break;
                    default:                //
                        Log.d(TAG, "****  Invalid Tarmac Position  ****");
                }
            }
//        }
    }


//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");

    onStart = true;
    getMatch();      // Get current match
    Log.d(TAG, "*** onStart  ->" + onStart);

    vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    if (vibrate == null) {
        Log.e(TAG, "No vibration service exists.");
    }
}

    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        onStart = false;
        if (Pearadox.MatchData_Saved) {
            // ???? - Clear all data back to original settings
            Log.d(TAG, "#### Data was saved in Final #### ");
            //Toast.makeText(getBaseContext(), "Data was saved in Final - probably should clear data and wait for next match", Toast.LENGTH_LONG).show();
            finish();
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
