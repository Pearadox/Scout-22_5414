package com.pearadox.scout_5414;

//import android.app.BuildConfig;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class PitScoutActivity extends AppCompatActivity {

    String TAG = "PitScout_Activity";      // This CLASS name
    TextView txt_EventName, txt_dev, txt_stud, txt_TeamName, txt_NumWheels;
    EditText editTxt_Team, txtEd_Weight, txtEd_Height, editText_Comments;
    Spinner spinner_Team, spinner_Traction, spinner_Omni, spinner_Mecanum, spinner_Pneumatic;
    Spinner spinner_Motor, spinner_Lang, spinner_autoMode;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter_Trac, adapter_Omni, adapter_Mac, adapter_Pneu ;
    ArrayAdapter<String> adapter_driveMotor, adapter_progLang,adapter_autoMode;
    CheckBox chkBox_Vision, chkBox_Pneumatics, chkBox_Climb, chkBox_EveryBot;
    CheckBox chkBox_OffFloor, chkBox_Terminal, chkBox_CanShoot;
    CheckBox chkBox_ShootLow, chkBox_LaunchPad, chkBox_Tarmac, chkBox_ShootRing, chkBox_ShootAny;
    RadioGroup  radgrp_END;      RadioButton  radio_Lift, radio_Zero, radio_One, radio_Two, radio_Three, radio_Four;

    Button btn_Save;
    Boolean OnOff= false;
    Uri currentImageUri, imageUri;
    private Uri FB_uri;
    File file = null;
    String mCurrentPhotoPath;
    String picname;
    AlertDialog alertbox;
    static final int REQUEST_TAKE_PHOTO = 1;
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;

    int MAX_ROBOT_WEIGHT =125;      // 2022 maximum weight
    public static String[] teams = new String[Pearadox.numTeams+1];  // Team list (array of just Team Names)
    public static String[] wheels = new String[]
            {"0","1","2","3","4","5","6", "7", "8"};
    public static String[] carry = new String[]             // Num. of robots this robot can lift
            {" ","1","2"};

    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfPitData_DBReference;
    FirebaseStorage storage;
    StorageReference storageRef;
    String pitPlace = "";  Boolean pitSD = false;   Boolean pitFB = false;
    String URL = "";
    public static String timeStamp = " ";
    Boolean imageOnFB = false;      // Does image already exist in Firebase
    boolean dataSaved = false;      // Make sure they save before exiting
    public Boolean Wt_entered = false;      // Weight entered
    public Boolean Ht_entered = false;      // Height entered

    // ===========================================================================
    // ===================  Data Elements for Pit Scout object ===================
    public String teamSelected = " ";           // Team #
    public boolean everyBot = false;            // EveryBot
    public int weight = 0;                      // Weight (lbs)
    public int height = 0;                      // Height (inches)
    public int totalWheels = 0;                 // Total # of wheels
    public int numTraction = 0;                 // Num. of Traction wheels
    public int numOmni = 0;                     // Num. of Omni wheels
    public int numMecanums = 0;                 // Num. of Mecanum wheels
    public int numPneumatic = 0;                // Num. of Pneumatic wheels
    public boolean vision = false;              // presence of Vision Camera
    public boolean pneumatics = false;          // presence of Pneumatics
    public boolean climb = false;               // presence of a Climbing mechanism
    public boolean CargoFloor = false;          // presence of a way to pick up Cargo from floor
    public boolean CargoTerminal = false;       // presence of a way to pick up Cargo from Terminal
    public String motor;                        // Type of Motor
    public String  HangarLev = "";              // What Level Climb
    public String lang;                         // Programming  Language
    public String autoMode;                     // Autonomous Operatong Mode
    public boolean canShoot = false;            // Has Shooter
    public boolean shootLow = false;            // Can Shoot into Lower Hub
    public boolean shootLP = false;             // Can Shoot from Launch Pad
    public boolean shootTarmac = false;         // Can Shoot from Tarmac
    public boolean shootRing= false;            // Can Shoot from Cargo Ring
    public boolean shootAnywhere= false;        // Can Shoot from Anywhere
    /* */
    public String comments;                     // Comment(s)
    public String scout = " ";                  // Student who collected the data
    private String  final_dateTime;             // Date & Time data was saved
    public String photoURL = "";                // URL of the robot photo in Firebase

// ===========================================================================
pitData Pit_Data = new pitData();
    pitData Pit_Load = new pitData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);
        Log.w(TAG, "<<<<<<   Pit Scout   >>>>>>");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        Log.w(TAG, param1 + " " + param2);     // ** DEBUG **
        scout = param2;                             // Scout of record
//
        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_EventName.setText(Pearadox.FRC_EventName);          // Event Name
        pfDatabase = FirebaseDatabase.getInstance();
        pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ImageView img_Photo = (ImageView) findViewById(R.id.img_Photo);
        ImageView imageView_numEnt = (ImageView) findViewById(R.id.imageView_numEnt);
        txt_dev = (TextView) findViewById(R.id.txt_Dev);
        txt_stud = (TextView) findViewById(R.id.txt_stud);
        txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
        txt_NumWheels = (TextView) findViewById(R.id.txt_NumWheels);
        txt_dev.setText(param1);
        txt_stud.setText(param2);
        txt_TeamName.setText(" ");
        txtEd_Weight = (EditText) findViewById(R.id.txtEd_Weight);
        txtEd_Height = (EditText) findViewById(R.id.txtEd_Height);
        Spinner spinner_Team = (Spinner) findViewById(R.id.spinner_Team);
        editTxt_Team = (EditText) findViewById(R.id.editTxt_Team);
        if (Pearadox.is_Network && Pearadox.numTeams > 0) {      // is Internet available & Teams present?
            loadTeams();
            txtEd_Weight.setEnabled(false);
            spinner_Team.setVisibility(View.VISIBLE);
            spinner_Team.setFocusable(true);
            spinner_Team.requestFocus();
            spinner_Team.requestFocusFromTouch();       // make team selection focus
            editTxt_Team.setVisibility(View.GONE);
            adapter = new ArrayAdapter<String>(this, R.layout.team_list_layout, teams);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Team.setAdapter(adapter);
            spinner_Team.setSelection(0, false);
            spinner_Team.setOnItemSelectedListener(new team_OnItemSelectedListener());

        } else {        // Have the user type in Team #
            editTxt_Team.setText("");
            editTxt_Team.setVisibility(View.VISIBLE);
            editTxt_Team.setEnabled(true);
            spinner_Team.setVisibility(View.GONE);
            editTxt_Team.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.w(TAG, " editTxt_Team listener; Team = " + editTxt_Team.getText());
                    if (editTxt_Team.getText().length() < 3 || editTxt_Team.getText().length() > 4) {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        Toast.makeText(getBaseContext(),"*** Team number must be at least 3 characters and no more than 4  *** ", Toast.LENGTH_LONG).show();
                    } else {
                        teamSelected = (String.valueOf(editTxt_Team.getText()));
                        chkForPhoto(teamSelected);      // see if photo already exists
                        return true;
                    }
                }
                return false;
                }
            });
        }

        chkBox_EveryBot = (CheckBox) findViewById(R.id.chkBox_EveryBot);
        Spinner spinner_Traction = (Spinner) findViewById(R.id.spinner_Traction);
        ArrayAdapter adapter_Trac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Trac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Traction.setAdapter(adapter_Trac);
        spinner_Traction.setSelection(0, false);
        spinner_Traction.setOnItemSelectedListener(new Traction_OnItemSelectedListener());
        Spinner spinner_Omni = (Spinner) findViewById(R.id.spinner_Omni);
        ArrayAdapter adapter_Omni = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Omni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Omni.setAdapter(adapter_Trac);
        spinner_Omni.setSelection(0, false);
        spinner_Omni.setOnItemSelectedListener(new Omni_OnItemSelectedListener());
        Spinner spinner_Mecanum = (Spinner) findViewById(R.id.spinner_Mecanum);
        ArrayAdapter adapter_Mac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Mac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Mecanum.setAdapter(adapter_Mac);
        spinner_Mecanum.setSelection(0, false);
        spinner_Mecanum.setOnItemSelectedListener(new Mecanum_OnItemSelectedListener());
        adapter_Mac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_Pneumatic = (Spinner) findViewById(R.id.spinner_Pneumatic);
        ArrayAdapter adapter_Pneu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheels);
        adapter_Mac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Pneumatic.setAdapter(adapter_Pneu);
        spinner_Pneumatic.setSelection(0, false);
        spinner_Pneumatic.setOnItemSelectedListener(new Pneumatic_OnItemSelectedListener());
        spinner_Motor = (Spinner) findViewById(R.id.spinner_Motor);
        String[] driveMotor = getResources().getStringArray(R.array.drive_motor_array);
        adapter_driveMotor = new ArrayAdapter<String>(this, R.layout.dev_list_layout, driveMotor);
        adapter_driveMotor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Motor.setAdapter(adapter_driveMotor);
        spinner_Motor.setSelection(0, false);
        spinner_Motor.setOnItemSelectedListener(new driveMotorOnClickListener());
        spinner_Lang = (Spinner) findViewById(R.id.spinner_Lang);
        String[] progLang = getResources().getStringArray(R.array.prog_lang_array);
        adapter_progLang = new ArrayAdapter<String>(this, R.layout.dev_list_layout, progLang);
        adapter_progLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Lang.setAdapter(adapter_progLang);
        spinner_Lang.setSelection(0, false);
        spinner_Lang.setOnItemSelectedListener(new progLangOnClickListener());
        spinner_autoMode = (Spinner) findViewById(R.id.spinner_autoMode);
        String[] operMode = getResources().getStringArray(R.array.auto_Mode_array);
        adapter_autoMode = new ArrayAdapter<String>(this, R.layout.dev_list_layout, operMode);
        adapter_autoMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_autoMode.setAdapter(adapter_autoMode);
        spinner_autoMode.setSelection(0, false);
        spinner_autoMode.setOnItemSelectedListener(new autoModeOnClickListener());
        chkBox_Vision = (CheckBox) findViewById(R.id.chkBox_Vision);
        chkBox_Pneumatics = (CheckBox) findViewById(R.id.chkBox_Pneumatics);
        chkBox_OffFloor = (CheckBox) findViewById(R.id.chkBox_OffFloor);
        chkBox_Terminal = (CheckBox) findViewById(R.id.chkBox_Terminal);
        chkBox_CanShoot = (CheckBox) findViewById(R.id.chkBox_CanShoot);
        chkBox_ShootLow = (CheckBox) findViewById(R.id.chkBox_ShootLow);
        chkBox_LaunchPad = (CheckBox) findViewById(R.id.chkBox_LaunchPad);
        chkBox_Tarmac = (CheckBox) findViewById(R.id.chkBox_Tarmac);
        chkBox_ShootRing = (CheckBox) findViewById(R.id.chkBox_ShootRing);
        chkBox_ShootAny = (CheckBox) findViewById(R.id.chkBox_ShootAny);
        chkBox_Climb = (CheckBox) findViewById(R.id.chkBox_Climb);
        editText_Comments = (EditText) findViewById(R.id.editText_Comments);
        radgrp_END = (RadioGroup) findViewById(R.id.radgrp_END);


//        editText_Comments.setFocusable(true);
//        editText_Comments.setClickable(true);

        Log.w(TAG, "Manuf.=" + manufacturer + "  Model=" + model);
        if (model.equals("K88")) {
            imageView_numEnt.setImageDrawable(getResources().getDrawable(R.drawable.k88num_enter));
        } else {
            imageView_numEnt.setImageDrawable(getResources().getDrawable(R.drawable.num_enter));
        }

//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //  hide the keyboard
        txtEd_Weight.clearFocus();  //
//        if (spinner_Team.getSelectedItemPosition() == 0) {
//            spinner_Team.performClick(); // Make them select team first!
//        }

//        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
//        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
        Toast toast = Toast.makeText(getBaseContext(), " \n *** Select a TEAM first before entering data *** \n", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        timeStamp = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss a").format(new Date());

//===============================================================================================================
        chkBox_Vision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
               Log.w(TAG, "chkBox_Vision Listener");
               if (buttonView.isChecked()) {
                   Log.w(TAG,"Vision is checked.");
                   vision = true;
               } else {
                   Log.w(TAG,"Vision is unchecked.");
                   vision = false;
               }
           }
       });
        chkBox_Pneumatics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_Pneumatics Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"Pneumatics is checked.");
                    pneumatics = true;
                } else {
                    Log.w(TAG,"Pneumatics is unchecked.");
                    pneumatics = false;
                }
            }
        });


        chkBox_OffFloor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_OffFloor Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"Off-floor is checked.");
                    CargoFloor = true;
                } else {
                    Log.w(TAG,"Off-floor is unchecked.");
                    CargoFloor = false;
            }
        }
        });

        chkBox_Terminal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_Terminal Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"LoadSta is checked.");
                    CargoTerminal = true;
                } else {
                    Log.w(TAG,"LoadSta is unchecked.");
                    CargoTerminal = false;
                }
            }
        });

        chkBox_CanShoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_CanShoot Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"CanShoot is checked.");
                    canShoot = true;
                } else {
                    Log.w(TAG,"CanShoot is unchecked.");
                    canShoot = false;
                }
            }
        });

        chkBox_EveryBot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_EveryBot Listener");
                radgrp_END = (RadioGroup) findViewById(R.id.radgrp_END);
                RadioButton radio_Two = (RadioButton) findViewById(R.id.radio_Two);
                if (buttonView.isChecked()) {
                    Log.w(TAG,"everyBot is checked.");
                    everyBot = true;
                    spinner_Motor.setSelection(1);              // CIM
                    spinner_Traction.setSelection(4);           // # Trac
                    txt_NumWheels.setText(String.valueOf(4));   // Total # of wheels
                    chkBox_Climb.setChecked(true);              // Can Climb
                    chkBox_OffFloor.setChecked(true);           // P/U from Floor
                    radio_Two.setChecked(true);                 // Mid climb
                    radgrp_END.check(R.id.radio_Two);           //
                    HangarLev = "Mid";                          //
                    spinner_Lang.setSelection(1);               // JAVA
                    chkBox_CanShoot.setChecked(false);          // Can't shoot upper
                    chkBox_ShootLow.setChecked(true);           // Shoot Lower Hub
                    spinner_autoMode.setSelection(1);           // Pgm Only
                    editText_Comments.setText("Everybot");      // Comments
                    comments = "Everybot";                      // Comments

                } else {
                    Log.w(TAG,"everyBot is unchecked.");
                    everyBot = false;
                    spinner_Motor.setSelection(0);              // CIM
                    spinner_Traction.setSelection(0);           // # Trac
                    txt_NumWheels.setText(String.valueOf(0));   // Total # of wheels
                    chkBox_Climb.setChecked(false);             // Can Climb
                    chkBox_OffFloor.setChecked(false);          // P/U from Floor
                    radio_Two.setChecked(false);                // No climb
                    radgrp_END.check(radgrp_END.getChildAt(0).getId());
                    HangarLev = "None";                         //
                    spinner_Lang.setSelection(0);               //
                    chkBox_CanShoot.setChecked(false);          // Can't shoot upper
                    chkBox_ShootLow.setChecked(false);          // Shoot Lower Hub
                    spinner_autoMode.setSelection(0);           // Auto Mode
                    editText_Comments.setText("");              // Comments
                    comments = "";                              // Comments
                }
            }
        });

        chkBox_Climb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.w(TAG, "chkBox_Climb Listener");
                if (buttonView.isChecked()) {
                    Log.w(TAG,"Climb is checked.");
                    climb = true;
                } else {
                    Log.w(TAG,"Climb is unchecked.");
                    climb = false;
                }
            }
        });


        chkBox_ShootLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.w(TAG,"Shoot Low is checked.");
                    shootLow = true;
                } else {
                    Log.w(TAG,"Shoot Low is unchecked.");
                    shootLow = false;
                }
            }
        });

        chkBox_LaunchPad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.w(TAG,"Shoot LaunchPad is checked.");
                    shootLP = true;
                } else {
                    Log.w(TAG,"Shoot LaunchPad is unchecked.");
                    shootLP = false;
                }
            }
        });

        chkBox_Tarmac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.w(TAG,"Shoot Tarmac is checked.");
                    shootTarmac = true;
                } else {
                    Log.w(TAG,"Shoot Tarmac is unchecked.");
                    shootTarmac = false;
                }
            }
        });

        chkBox_ShootRing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.w(TAG,"ShootRing is checked.");
                    shootRing = true;
                } else {
                    Log.w(TAG,"ShootRing is unchecked.");
                    shootRing = false;
                }
            }
        });

        chkBox_ShootAny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (buttonView.isChecked()) {
                    Log.w(TAG,"shootAnywhere is checked.");
                    shootAnywhere = true;
                } else {
                    Log.w(TAG,"shootAnywhere is unchecked.");
                    shootAnywhere = false;
                }
            }
        });





//=================================================================
        editText_Comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.w(TAG, "******  onTextChanged TextWatcher  ******" + s);
                comments = String.valueOf(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.w(TAG, "******  beforeTextChanged TextWatcher  ******");
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG, "******  onTextChanged TextWatcher  ******" + s );
                comments = String.valueOf(s);
            }
        });

        txtEd_Weight.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.w(TAG, "******  txtEd_Weight listener  ******  " + keyCode + "  " + event.getAction());

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.w(TAG, " txtEd_Weight = "  + txtEd_Weight.getText());

                    if (txtEd_Weight.getText().length() > 0) {
                        weight = Integer.valueOf(String.valueOf(txtEd_Weight.getText()));
                        if (weight > MAX_ROBOT_WEIGHT) {
                            txtEd_Weight.setText("");   // Reset 'BAD" weights
                            weight = 0;
                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                            Toast toast = Toast.makeText(getBaseContext(), " \n*****  Maximum Robot Weight is " + MAX_ROBOT_WEIGHT + "!  *****\n ", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else {
                            Wt_entered = true;
                            Log.w(TAG, "### Used the right key!!  ### " + Wt_entered);
                            editText_Comments.setFocusable(true);
                            editText_Comments.setClickable(true);
                            return true;
                        }
                    } else {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        Toast toast = Toast.makeText(getBaseContext(), " \n*****  Enter a valid Weight!  *****\n ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }
                return false;
            }
        });

        txtEd_Weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.w(TAG, "@@@ Weight - Lost Focus Listener @@@  '" + txtEd_Weight.getText() +"' " + weight +"  " + Wt_entered);
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (!Wt_entered) {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        Toast toast = Toast.makeText(getBaseContext(), "\n*** Please use the ➺| key and NOT the ▽ key ***\n                Please re-enter Weight", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }
            }
        });


        txtEd_Height.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.w(TAG, "******  txtEd_Height listener  ******  " + keyCode + "  " + event.getAction());

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.w(TAG, " txtEd_Height = "  + txtEd_Height.getText());

                    if (txtEd_Height.getText().length() > 0) {
                        height = Integer.valueOf(String.valueOf(txtEd_Height.getText()));
                        Ht_entered = true;
                    } else {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        Toast toast = Toast.makeText(getBaseContext(), " \n*****  Enter a valid Height!  *****\n ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }
                return false;
            }
        });


        txtEd_Height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.w(TAG, "@@@ Height - Lost Focus Listener @@@  '" + txtEd_Height.getText() +"' " + height +"  " + Ht_entered);
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (!Ht_entered) {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                        Toast toast = Toast.makeText(getBaseContext(), "\n*** Please use the ➺| key and NOT the ▽ key ***\n                Please re-enter Height", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }
            }
        });


/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
        btn_Save = (Button) findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "Save Button Listener");
                Log.e(TAG, "REQ'D  >>>>>   Wt=" + weight+ "  ☸=" + totalWheels + " ↑" + chkBox_Climb.isChecked() +"  Lang='"+ lang+ "'   Mode=" + autoMode);
                // Check for required fields
                if ((txtEd_Weight.length() > 0) &&                               // weight field length >0
                        ((weight > 0) &&(weight <= MAX_ROBOT_WEIGHT)) &&        // weight >0 & <_ Max
                        (totalWheels >= 4) &&                                   // at least 4 wheels
                        (spinner_autoMode.getSelectedItemPosition() > 0) &&     // Autonomous mode specified
                        (spinner_Lang.getSelectedItemPosition() > 0)) {        // Robot Prog. Language set

                    Spinner spinner_Team = (Spinner) findViewById(R.id.spinner_Team);
                    storePitData();           // Put all the Pit data collected in Pit object
                    dataSaved = true;
//                    if (Pearadox.is_Network) {      // is Internet available?
//                        spinner_Team.setSelection(0);       //Reset to NO selection
//                        txt_TeamName.setText(" ");
//                    }
                    finish();       // Exit  <<<<<<<<
                } else {
                    final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                    Toast toast = Toast.makeText(getBaseContext(), "*** Enter _ALL_ data (valid Weight, Wheels, Lang. & Auto Mode) before saving *** \n Wt=" + weight+ "  ☸=" + totalWheels + " ↑" + chkBox_Climb.isChecked()  +"  Lang='"+ lang+ "'   Mode=" + autoMode, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                onLaunchCamera();       // Start Camera
            default:
                break;
        }
        return false;
    }

    private File createImageFile() throws IOException {
//        String team = "5414";
//        String imageFileName = "robot_" + team + ".png";
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "robot_" + teamSelected;
//        String imageFileName = "robot_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
                File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void onLaunchCamera() {
        Log.w(TAG, "►►►►►  LaunchCamera  ◄◄◄◄◄");
        if (teamSelected.length() < 3) {        /// Make sure a Team is selected 1st
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "*** Select a TEAM first before taking photo ***", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {   // Ensure that there's a camera activity
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri photoURI = null;
                try {
                    photoURI = FileProvider.getUriForFile(PitScoutActivity.this,
                            "com.pearadox.scout_5414.provider",
                            createImageFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            Log.w(TAG, "Photo taken");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "*****  onActivityResult " + requestCode);
        ImageView img_Photo = (ImageView) findViewById(R.id.img_Photo);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.w(TAG, "requestCode = '" + requestCode + "'");
            imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            picname = "robot_" + teamSelected.trim() + ".png";
            Bitmap rotatedBitmap = null;
            try {
                InputStream ims = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (model.equals("K88")) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(-90);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    img_Photo.setImageBitmap(rotatedBitmap);
                    FB_uri = getImageUri(this, rotatedBitmap);
                } else {
                    img_Photo.setImageBitmap(BitmapFactory.decodeStream(ims));
                }
            } catch (FileNotFoundException e) {
                return;
            }

//            galleryAddPic();
//            File savedFile;
//            if(mCurrentPhotoPath == null){
//                savedFile= new File(imageUri.getPath());
//            }else{
//                savedFile = new File(mCurrentPhotoPath);
//            }

            //            String filename = "robot_" + teamSelected.trim() + ".png";
//            File directPhotos = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/" + Pearadox.FRC_Event + "/" + filename);
//
//            Log.w(TAG, "@@@ PHOTO EXISTS LOCALLY @@@ ");
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(directPhotos.getAbsolutePath(),bmOptions);
//            bitmap = Bitmap.createScaledBitmap(bitmap,img_Photo.getWidth(),img_Photo.getHeight(),true);
//            img_Photo.setImageBitmap(bitmap);

            if (!imageOnFB) {
                SaveToFirebase();
            } else {
                Log.w(TAG, "*** PHOTO EXISTS ON FIREBASE *** ");
            }
        }
    }

    private void SaveToFirebase() {
        Log.w(TAG, "$$$$$  SaveToFirebase  $$$$$");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://pearadox-2022.appspot.com/images/"+ Pearadox.FRC_Event).child(picname);
        UploadTask uploadTask = storageReference.putFile(FB_uri);
//        UploadTask uploadTask = storageReference.putFile(imageUri);

        // Now get the URL
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri downloadURL = taskSnapshot.getDownloadUrl();
                Uri downloadURL = taskSnapshot.getUploadSessionUri();
                photoURL = downloadURL.toString();
                Log.e(TAG, "#####  URL=" + photoURL  + " \n");
            }
        });
    }

    private void galleryAddPic() {
        /**
         * copy current image to Gallery
         */
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", picname);
        return Uri.parse(path);
    }
    public void encodeBitmapAndSave(Bitmap bitmap) {
        Log.w(TAG, "$$$$$  encodeBitmapAndSave $$$$$");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String picname = "robot_" + teamSelected.trim() + ".png";
        Log.w(TAG, "Photo = '" + picname + "'");
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);     // ByteArrayOutputStream
        byte[] data = baos.toByteArray();
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    public class team_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Log.w(TAG, "*****  team_OnItemSelectedListener " + pos);
            teamSelected = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>>  '" + teamSelected + "'");
            txt_TeamName = (TextView) findViewById(R.id.txt_TeamName);
            findTeam(teamSelected);
            txt_TeamName.setText(team_inst.getTeam_name());
            txtEd_Weight.setEnabled(true);

            chkForPhoto(teamSelected);              // see if photo already exists (SD card or Firebase)

            chkForData(teamSelected);               // see if data already exists (SD card or Firebase)

            // Check Firebase
            getTeam_Pit(teamSelected);

        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }


    // =============================================================================
    private void chkForData(String team) {
        Log.w(TAG, "*****  chkForData - team = " + team);
        pitPlace = ""; pitSD = false;  pitFB = false;
        // First check SD card
        String filename = team.trim() + ".dat";
        File directData = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/" + Pearadox.FRC_Event + "/" + filename);
        Log.w(TAG, "SD card Path = " + directData);
        if(directData.exists())  {
            if (pitPlace == "") {           // let Firebase take precedent
                pitPlace = "SD card";
            }
            pitSD = true;
            Log.w(TAG, "**** in 'chkForData'.   Place = '" + pitPlace + "'  " + pitSD + " " + pitFB + " \n");
        }
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getTeam_Pit(String team) {
        Log.i(TAG, "$$$$$  getTeam_Pit  $$$$$  " + team);

        String child = "pit_team";
        String key = team;      // Removed .trim()       GLF 3/31/2017
        Log.w(TAG, "   Q U E R Y  " + child + "  '" + key + "' \n ");
        Query query = pfPitData_DBReference.orderByChild(child).equalTo(key);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%%%%%%%%%%  ChildAdded");
                pitPlace = "Firebase";
                pitFB = true;
                Log.w(TAG, "**** in 'getTeam_Pit'.   Place = '" + pitPlace + "'  " + pitSD + " " + pitFB + " \n");
                System.out.println(dataSnapshot.getValue());
                System.out.println("\n \n ");
                Pit_Load = dataSnapshot.getValue(pitData.class);
                System.out.println("Team: " + Pit_Load.getPit_team());
                System.out.println("Comment: " + Pit_Load.getPit_comment());
                System.out.println("\n \n ");

                Log.w(TAG, "Place = '" + pitPlace + "'  " + pitSD + " " + pitFB + " \n");
                if (pitSD || pitFB) {
                    diag();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%  ChildChanged");
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.w(TAG, "%%%  ChildRemoved");
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.w(TAG, "%%%  ChildMoved");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "%%%  DatabaseError");
            }
        });
        Log.w(TAG, "Place = '" + pitPlace + "'  " + pitSD + " " + pitFB + " \n");
        if (pitSD || pitFB) {
            diag();
        }
    }

    private void diag() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("★★★  Pit Data exists on " + pitPlace + ".  ★★★  \n  Do you want to use that data and make changes (or add photo)? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        Spinner spinner_Traction = (Spinner) findViewById(R.id.spinner_Traction);
                        Spinner spinner_Omni = (Spinner) findViewById(R.id.spinner_Omni);
                        Spinner spinner_Mecanum = (Spinner) findViewById(R.id.spinner_Mecanum);
                        Spinner spinner_Pneumatic = (Spinner) findViewById(R.id.spinner_Pneumatic);

                        if (pitFB) {
                            // Already loaded data from Firebase into Pit_Load during 'getTeam_Pit'
                        } else {
                            File direct_pit = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/" + Pearadox.FRC_Event);
                            try {
                                Log.w(TAG, "   Dir:" + direct_pit + "/" + teamSelected.trim() + ".dat");
                                InputStream file = new FileInputStream(direct_pit + "/" + teamSelected.trim() + ".dat");
                                InputStream buffer = new BufferedInputStream(file);
                                ObjectInput input = new ObjectInputStream(buffer);
                                Pit_Load = (pitData) input.readObject();
                                Log.w(TAG, "#### Obect '" + Pit_Load.getPit_team() + "'  " + Pit_Load.getPit_scout());
                            } catch (FileNotFoundException e) {
                                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        // Now load the screen & variables
                        teamSelected = Pit_Load.getPit_team();
                        //  Height _NOT_ coming back?  Scouters _MUST_ use > keyboard key and NOT Exit
                        txtEd_Weight.setText(String.valueOf(Pit_Load.getPit_weight()));
                        weight = Integer.valueOf(String.valueOf(txtEd_Weight.getText()));
                        txt_NumWheels.setText(String.valueOf(Pit_Load.getPit_totWheels()));
                        totalWheels = Pit_Load.getPit_totWheels();
                        spinner_Traction.setSelection((Pit_Load.getPit_numTrac()));
                        spinner_Omni.setSelection((Pit_Load.getPit_numOmni()));
                        spinner_Mecanum.setSelection((Pit_Load.getPit_numMecanum()));
                        spinner_Pneumatic.setSelection((Pit_Load.getPit_numPneumatic()));

                        chkBox_Climb.setChecked(Pit_Load.isPit_climber());
                        chkBox_Vision.setChecked(Pit_Load.isPit_vision());
                        chkBox_Pneumatics.setChecked(Pit_Load.isPit_pneumatics());
                        chkBox_EveryBot.setChecked(Pit_Load.isPit_everyBot());
                        chkBox_OffFloor.setChecked(Pit_Load.isPit_cargoFloor());
                        chkBox_Terminal.setChecked(Pit_Load.isPit_cargoTerm());
                        
                        chkBox_ShootLow.setChecked(Pit_Load.isPit_shootLow());
                        chkBox_LaunchPad.setChecked(Pit_Load.isPit_shootLP());
                        chkBox_Tarmac.setChecked(Pit_Load.isPit_shootTarmac());
                        chkBox_ShootRing.setChecked(Pit_Load.isPit_shootRing());
                        chkBox_ShootAny.setChecked(Pit_Load.isPit_shootAnywhere());

                        String motr = Pit_Load.getPit_motor();
                        Log.w(TAG, "Motor = '" + motr + "'");
                        switch (motr) {
                            case ("CIM"):
                                spinner_Motor.setSelection(1);
                                break;
                            case ("Mini-CIM"):
                                spinner_Motor.setSelection(2);
                                break;
                            case ("775pro"):
                                spinner_Motor.setSelection(3);
                                break;
                            case ("NEO"):
                                spinner_Motor.setSelection(4);
                                break;
                            case ("Falcon 500"):
                                spinner_Motor.setSelection(5);
                                break;
                            default:
                                Log.w(TAG, "►►►►►  E R R O R  ◄◄◄◄◄");
                                break;
                        }
                        String pLang = Pit_Load.getPit_lang();
                        Log.w(TAG, "Lauguage = '" + pLang + "'");
                        switch (pLang) {
                            case ("JAVA"):
                                spinner_Lang.setSelection(1);
                                break;
                            case ("Kotlin"):
                                spinner_Lang.setSelection(2);
                                break;
                            case ("C++"):
                                spinner_Lang.setSelection(3);
                                break;
                            case ("LabView"):
                                spinner_Lang.setSelection(4);
                                break;
                            default:
                                Log.w(TAG, "►►►►►  E R R O R  ◄◄◄◄◄");
                                break;
                        }
                        String mode = Pit_Load.getPit_autoMode();
                        Log.w(TAG, "Mode = '" + mode + "'");
                        switch (mode) {
                            case ("Program Only"):
                                spinner_autoMode.setSelection(1);
                                break;
                            case ("Vision Only"):
                                spinner_autoMode.setSelection(2);
                                break;
                            case ("Hybrid (P+V)"):
                                spinner_autoMode.setSelection(3);
                                break;
                            default:
                                Log.w(TAG, "►►►►►  E R R O R  ◄◄◄◄◄");
                                break;
                        }

                        // Finally ...
                        scout = scout + " & " + Pit_Load.getPit_scout();    // Append original scout name
                        editText_Comments.setText(Pit_Load.getPit_comment());
                        photoURL = Pit_Load.pit_photoURL;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // nothing
                    }
                })
                .show();

    }

// =============================================================================
    private void chkForPhoto(String team) {
        Log.w(TAG, "*****  chkForPhoto - team = " + team);

        // First check SD card
        String filename = "robot_" + team.trim() + ".png";
        File directPhotos = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/images/" + Pearadox.FRC_Event + "/" + filename);
        Log.w(TAG, "SD card Path = " + directPhotos);
        ImageView img_Photo = (ImageView) findViewById(R.id.img_Photo);
        if(directPhotos.exists())  {
            Log.w(TAG, "@@@ PHOTO EXISTS LOCALLY @@@ ");
//            Bitmap imageBitmap = BitmapFactory.decodeFile(directPhotos.getAbsolutePath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(directPhotos.getAbsolutePath(),bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap,img_Photo.getWidth(),img_Photo.getHeight(),true);
            img_Photo.setImageBitmap(bitmap);

        } else {
//            if (Pearadox.is_Network) {      // is Internet available?   Commented out because 'tethered' show No internet
            Log.w(TAG, "### Checking on Firebase Images ### ");
//            }
            URL = "";
            img_Photo.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
            imageOnFB = false;

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://pearadox-2022.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + team.trim() + ".png");
            Log.e(TAG, "images/" + Pearadox.FRC_Event + "/robot_" + team.trim() + ".png" + "\n \n");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.e(TAG, "  uri: " + uri.toString());
                    ImageView img_Photo = (ImageView) findViewById(R.id.img_Photo);
                    URL = uri.toString();
                    if (URL.length() > 0) {
                        Picasso.with(PitScoutActivity.this).load(URL).into(img_Photo);
                        photoURL = URL;     // save URL in Pit object
                        imageOnFB = true;
                    } else {
                        img_Photo.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
                        imageOnFB = false;
                    }
                }
            });
        }
    }

    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */

    private class progLangOnClickListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            lang = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> Language  '" + lang + "' " + pos);
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    private class autoModeOnClickListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            autoMode = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> Oper.Mode  '" + autoMode + "' " + pos);
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    private class driveMotorOnClickListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            motor = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> Motor  '" + motor + "' " + pos);
            if (motor.equals("Falcon 500")) {
                Toast toast = Toast.makeText(getBaseContext(), "This motor has a reported set screw problem;\nAsk team if they have made suggested repairs.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    public class Traction_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numTraction = Integer.parseInt(num);
            Log.w(TAG, ">>>>> Traction '" + numTraction + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    public class Omni_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numOmni = Integer.parseInt(num);
            Log.w(TAG, ">>>>> Omni '" + numOmni + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    public class Mecanum_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numMecanums = Integer.parseInt(num);
            Log.w(TAG, ">>>>> Mecanum '" + numMecanums + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    public class Pneumatic_OnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            numPneumatic = Integer.parseInt(num);
            Log.w(TAG, ">>>>> Pneumatic '" + numPneumatic + "'");
            updateNumWhls();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    private void updateNumWhls() {
        Log.w(TAG, "######  updateNumWhls ###### T-O-M = " + numTraction + numOmni + numMecanums);
        int x = numTraction + numOmni + numMecanums + numPneumatic;
        txt_NumWheels.setText(String.valueOf(x));      // Total # of wheels
        totalWheels = x;
        if (x < 4){
            Toast.makeText(getBaseContext(), "Robot should have at least 4 wheels", Toast.LENGTH_LONG).show();
        }
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
        } else if (value.equals("Low")){        // One?
            Log.w(TAG, "Low");
            HangarLev = "Low";
        } else if (value.equals("Mid")){         // Two
            Log.w(TAG, "Mid");
            HangarLev = "Mid";
        } else if (value.equals("High")){       // Three
            Log.w(TAG, "High");
            HangarLev = "High";
        } else if (value.equals("Traversal")){  // Four
            Log.w(TAG, "Traversal");
            HangarLev = "Traversal";
        } else {                                // Invalid
            Log.e(TAG, "****  Invalid Hangar Level ****");
        }
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private void findTeam(String tnum) {
        Log.w(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
//                Log.w(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        if (!found) {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Log.e(TAG, "****** ERROR - Team _NOT_ found!! = " + tnum);
            txt_TeamName.setText(" ");
        }
    }

    private void loadTeams() {
        Log.w(TAG, "$$$$$  loadTeams $$$$$");
        int tNum = 0;
        teams[0] = " ";     // Make the 1st one BLANK for dropdown
        tNum ++;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // get each team entry
            team_inst = Pearadox.team_List.get(i);
            teams[i+1] = team_inst.getTeam_num();
            tNum ++;
        }  // end For
        Log.w(TAG, "# of teams = " + tNum);

    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storePitData() {
        Log.w(TAG, ">>>>  storePitData  <<<< " + teamSelected );

        Pit_Data.setPit_team(teamSelected);
        Pit_Data.setPit_everyBot(everyBot);
        Pit_Data.setPit_weight(weight);
        Pit_Data.setPit_height(height);
        Pit_Data.setPit_totWheels(totalWheels);
        Pit_Data.setPit_numTrac(numTraction);
        Pit_Data.setPit_numOmni(numOmni);
        Pit_Data.setPit_numMecanum(numMecanums);
        Pit_Data.setPit_numPneumatic(numPneumatic);
        Pit_Data.setPit_vision(vision);
        Pit_Data.setPit_pneumatics(pneumatics);
        Pit_Data.setPit_climber(climb);
        Pit_Data.setPit_cargoFloor(CargoFloor);
        Pit_Data.setPit_cargoTerm(CargoTerminal);
        Pit_Data.setPit_motor(motor);
        Pit_Data.setPit_hangarLevel(HangarLev);
        Pit_Data.setPit_canshoot(canShoot);
        Pit_Data.setPit_lang(lang);
        Pit_Data.setPit_autoMode(autoMode);
        Pit_Data.setPit_shootLow(shootLow);
        Pit_Data.setPit_shootLP(shootLP);
        Pit_Data.setPit_shootTarmac(shootTarmac);
        Pit_Data.setPit_shootRing(shootRing);
        Pit_Data.setPit_shootAnywhere(shootAnywhere);

         /* */
        Pit_Data.setPit_comment(comments);
        Pit_Data.setPit_dateTime(timeStamp);
        Pit_Data.setPit_scout(scout);
        Pit_Data.setPit_photoURL(photoURL);
// -----------------------------------------------
        saveDatatoSDcard();                 //Save locally
        if (Pearadox.is_Network) {          // is Internet available?
            String keyID = teamSelected;
            pfPitData_DBReference.child(keyID).setValue(Pit_Data);      // Store it to Firebase
            Log.e(TAG, ">>>>>  Pit data saved to Firebase <<<<<");
        } else {
            Toast toast = Toast.makeText(getBaseContext(), "*** Data _NOT_ stored to Firebase (only SD)!!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        private void saveDatatoSDcard() {
        Log.w(TAG, "@@@@  saveDatatoSDcard  @@@@");
        String filename = Pit_Data.getPit_team().trim() + ".dat";
        ObjectOutput out = null;
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/pit/" + Pearadox.FRC_Event + "/" + filename);
        Log.w(TAG, "SD card Path = " + directMatch);
        if(directMatch.exists())  {
            Log.w(TAG, "WARNING - Data for " + filename + " already exists!!");
//            Toast toast = Toast.makeText(getBaseContext(), "Data for " + filename + " already exists!!", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(directMatch));
            out.writeObject(Pit_Data);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// ################################################################
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);

            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit without saving?  All data will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        dataSaved = false;
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        Log.v(TAG, "onResume  \n");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        if (!dataSaved) {
            Log.w(TAG, "** Data _NOT_ saved!!  **");
            // Handled with Dialog box
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
        if (alertbox != null) {
            alertbox.dismiss();
            alertbox = null;
        }
    }
}
