package com.pearadox.scout_5414;

import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class VisPit_Activity extends AppCompatActivity {

    String TAG = "VisPit_Activity";        // This CLASS name
    String tnum = "", tname = "", imgURL = "";
    TextView txt_team, txt_teamName;
    TextView txt_Ht, txt_TotWheels, txt_NumTrac, txt_NumOmni, txt_NumMecanum, txt_NumPneumatic, txt_LiftCap, txt_Scout, txt_Comments;
    TextView txt_DriveMotor, txt_ProgLang;
    TextView txt_autoMode, txt_CG;
    ImageView imgView_Robot, imgView_LARGE;                // Robot image
    CheckBox chkBox_Vision, chkBox_Pneumatics, chkBox_Climb, chkBox_Lift, chkBox_Hook, chkBox_Ramp;
    CheckBox chkBox_Low, chkBox_Under, chkBox_Line, chkBox_Front, chkBox_Back, chkBox_Spin, chkBox_Color, chkBox_CP_under;
    CheckBox chkBox_OffFloor, chkBox_Load, chkBox_Dump;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfPitData_DBReference;


    // ===================  Data Elements for Pit Scout object ===================
    public String teamSelected = " ";           // Team #
    public int weight = 0;                        // Height (lbs)
    public int totalWheels = 0;                 // Total # of wheels
    public int numTraction = 0;                 // Num. of Traction wheels
    public int numOmnis = 0;                    // Num. of Omni wheels
    public int numMecanums = 0;                 // Num. of Mecanum wheels
    public int numPneumatic = 0;                // Num. of Pneumatic wheels
    public boolean vision = false;              // presence of Vision Camera
    public boolean pneumatics = false;          // presence of Pneumatics
    public boolean climb = false;               // presence of a Climbing mechanism
    public boolean PowerCellManip = false;          // presence of a way to pick up PowerCell from floor
    public boolean floorPanel = false;          // can get Hatch Panel from floor
    public boolean floorPowerCell = false;          // can get PowerCell from floor
    public boolean canLift = false;             // Ability to lift other robots
    public int numLifted = 0;                   // Num. of robots can lift (1-2)
    public boolean liftRamp = false;            // lift type Ramp
    public boolean liftHook = false;            // lift type Hook
    public boolean leaveHAB2 = false;           // Can leave from HAB level 2
    public boolean endHAB2 = false;             // Can climb to HAB level 2
    public boolean endHAB3 = false;             // Can climb to HAB level 3
    public int speed = 0;                       // Speed (Ft. per Sec)
    public String motor;                        // Type of Motor
    public String lang;                         // Programming  Language
    public String autoMode;                       // Autonomous Operatong Mode
    /* */
    /* */
    public String comments = "";                // Comment(s)
    public String scout = "";                   // Student who collected the data
    public String pit_photoURL;                     // URL of the robot photo in Firebase

    // ===========================================================================
    pitData Pit_Data = new pitData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_pit);
        Log.i(TAG, "@@@@@@@@  VisPit_Activity started  @@@@@@@@");
        Bundle bundle = this.getIntent().getExtras();
        tnum = bundle.getString("team");
        tname = bundle.getString("name");
        imgURL = bundle.getString("url");
        Log.w(TAG, "\n >>>>>>>> " + tnum + " " + tname + " '" + imgURL + "' ");      // ** DEBUG **

        pfDatabase = FirebaseDatabase.getInstance();
        pfPitData_DBReference = pfDatabase.getReference("pit-data/" + Pearadox.FRC_Event); // Pit Scout Data
//        pfPitData_DBReference = pfDatabase.getReference("pit-data/");       // Pit Scout Data
        getTeam_Pit(tnum);
        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
        txt_team.setText(tnum);
        txt_teamName.setText(tname);
        if (imgURL.length() > 1) {
            Picasso.with(this).load(imgURL).into(imgView_Robot);
        } else {
            imgView_Robot.setImageDrawable(getResources().getDrawable(R.drawable.photo_missing));
        }
        txt_Ht = (TextView) findViewById(R.id.txt_Ht);
        txt_Scout = (TextView) findViewById(R.id.txt_Scout);
        txt_Comments = (TextView) findViewById(R.id.txt_Comments);
        txt_Ht.setText(" ");
        txt_Scout.setText(" ");
        txt_Comments.setText("***   No Pit data for this team   ***");

        // *****  If image selected, view full screen   *****
//        imgView_Robot.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            ImageView imgView_LARGE = (ImageView) findViewById(R.id.imgView_LARGE);
//            ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
//            imgView_Robot.setVisibility(View.INVISIBLE);
//            imgView_LARGE.setVisibility(View.VISIBLE);
//        }
//    });
//        imgView_LARGE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageView imgView_LARGE = (ImageView) findViewById(R.id.imgView_LARGE);
//                ImageView imgView_Robot = (ImageView) findViewById(R.id.imgView_Robot);
//                imgView_Robot.setVisibility(View.VISIBLE);
//                imgView_LARGE.setVisibility(View.INVISIBLE);
//            }
//        });
    }
// ===============================================================================
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
//                System.out.println(dataSnapshot.getValue());
//                System.out.println("\n \n ");
                pitData Pit_Data = dataSnapshot.getValue(pitData.class);
//                    System.out.println("Team: " + Pit_Data.getPit_team());
//                    System.out.println("Comment: " + Pit_Data.getPit_comment());
//                    System.out.println("\n \n ");
                txt_Ht = (TextView) findViewById(R.id.txt_Ht);
                txt_TotWheels = (TextView) findViewById(R.id.txt_TotWheels);
                txt_NumTrac = (TextView) findViewById(R.id.txt_NumTrac);
                txt_NumOmni = (TextView) findViewById(R.id.txt_NumOmni);
                txt_NumMecanum = (TextView) findViewById(R.id.txt_NumMecanum);
                txt_NumPneumatic = (TextView) findViewById(R.id.txt_NumPneumatic);
                chkBox_Climb = (CheckBox) findViewById(R.id.chkBox_Climb);
                chkBox_Vision = (CheckBox) findViewById(R.id.chkBox_Vision);
                chkBox_Pneumatics = (CheckBox) findViewById(R.id.chkBox_Pneumatics);
                chkBox_Lift = (CheckBox) findViewById(R.id.chkBox_Lift);
                chkBox_Dump = (CheckBox) findViewById(R.id.chkBox_Dump);
                txt_LiftCap = (TextView) findViewById(R.id.txt_LiftCap);
                chkBox_Ramp = (CheckBox) findViewById(R.id.chkBox_Ramp);
                chkBox_Hook = (CheckBox) findViewById(R.id.chkBox_Hook);
                chkBox_OffFloor = (CheckBox) findViewById(R.id.chkBox_OffFloor);
                chkBox_Load = (CheckBox) findViewById(R.id.chkBox_Load);
                txt_DriveMotor = (TextView) findViewById(R.id.txt_DriveMotor);
                txt_ProgLang = (TextView) findViewById(R.id.txt_ProgLang);
                txt_autoMode = (TextView) findViewById(R.id.txt_Mode);
                chkBox_Low = (CheckBox) findViewById(R.id.chkBox_Low);
                chkBox_Under = (CheckBox) findViewById(R.id.chkBox_Under);
                chkBox_Line = (CheckBox) findViewById(R.id.chkBox_Line);
                chkBox_Front = (CheckBox) findViewById(R.id.chkBox_Front);
                chkBox_Back = (CheckBox) findViewById(R.id.chkBox_Back);
                chkBox_Spin = (CheckBox) findViewById(R.id.chkBox_Spin);
                chkBox_Color = (CheckBox) findViewById(R.id.chkBox_Color);
                chkBox_CP_under = (CheckBox) findViewById(R.id.chkBox_CP_under);
                txt_CG = (TextView) findViewById(R.id.txt_CG);
                txt_Scout = (TextView) findViewById(R.id.txt_Scout);
                txt_Comments = (TextView) findViewById(R.id.txt_Comments);

                // ****  Start loading data  ****
                txt_Ht.setText(String.valueOf(Pit_Data.getPit_weight()));
                txt_TotWheels.setText(String.valueOf(Pit_Data.getPit_totWheels()));
                txt_NumTrac.setText(String.valueOf(Pit_Data.getPit_numTrac()));
                txt_NumOmni.setText(String.valueOf(Pit_Data.getPit_numOmni()));
                txt_NumMecanum.setText(String.valueOf(Pit_Data.getPit_numMecanum()));
                txt_NumPneumatic.setText(String.valueOf(Pit_Data.getPit_numPneumatic()));


                chkBox_Low.setChecked(Pit_Data.isPit_shootLow());
                chkBox_Under.setChecked(Pit_Data.isPit_shootUnder());
                chkBox_Line.setChecked(Pit_Data.isPit_shootLine());
                chkBox_Front.setChecked(Pit_Data.isPit_shootFront());
                chkBox_Back.setChecked(Pit_Data.isPit_shootBack());
                chkBox_Dump.setChecked(Pit_Data.pit_dump);
                chkBox_Spin.setChecked(Pit_Data.pit_spin);
                chkBox_Color.setChecked(Pit_Data.pit_color);
                chkBox_CP_under.setChecked(Pit_Data.pit_undTrench);

                txt_autoMode.setText(String.valueOf(Pit_Data.getPit_autoMode()));
                txt_ProgLang.setText(String.valueOf(Pit_Data.getPit_lang()));
                txt_DriveMotor.setText(String.valueOf(Pit_Data.getPit_motor()));

                String CG ="";
                if (Pit_Data.isPit_climberL1()) {
                    CG=CG+"L1 ";
                }
                if (Pit_Data.isPit_climberL2()) {
                    CG=CG+"L2 ";
                }
                if (Pit_Data.isPit_climberL3()) {
                    CG=CG+"L3 ";
                }
                if (Pit_Data.isPit_climberM1()) {
                    CG=CG+"M1 ";
                }
                if (Pit_Data.isPit_climberM2()) {
                    CG=CG+"M2 ";
                }
                if (Pit_Data.isPit_climberM3()) {
                    CG=CG+"M3 ";
                }
                if (Pit_Data.isPit_climberR1()) {
                    CG=CG+"R1 ";
                }
                if (Pit_Data.isPit_climberR2()) {
                    CG=CG+"R2 ";
                }
                if (Pit_Data.isPit_climberR3()) {
                    CG=CG+"R3 ";
                }
                txt_CG.setText(CG);

                chkBox_Climb.setChecked(Pit_Data.isPit_climber());
                chkBox_Vision.setChecked(Pit_Data.isPit_vision());
                chkBox_Pneumatics.setChecked(Pit_Data.isPit_pneumatics());
                chkBox_Lift.setChecked(Pit_Data.isPit_canLift());
                chkBox_OffFloor.setChecked(Pit_Data.isPit_PowerCellFloor());
                chkBox_Load.setChecked(Pit_Data.isPit_PowerCellLoad());


                if (Pit_Data.isPit_canLift()) {
                    txt_LiftCap.setVisibility(View.VISIBLE);
                    txt_LiftCap.setText(String.valueOf(Pit_Data.getPit_numLifted()));
                    chkBox_Hook.setVisibility(View.VISIBLE);
                    chkBox_Hook.setChecked(Pit_Data.isPit_liftHook());
                    chkBox_Ramp.setVisibility(View.VISIBLE);
                    chkBox_Ramp.setChecked(Pit_Data.isPit_liftRamp());
                } else {
                    txt_LiftCap.setVisibility(View.INVISIBLE);
                    chkBox_Ramp.setVisibility(View.INVISIBLE);
                    chkBox_Hook.setVisibility(View.INVISIBLE);
                }

                // Finally ...
                txt_Scout.setText(Pit_Data.getPit_scout());
                txt_Comments.setText(Pit_Data.getPit_comment());
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
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(TAG, "@@@  Options  @@@ " );
        Log.w(TAG, " \n  \n");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_screen) {
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-VizPit"  + "_" + tnum.trim() + ".JPG";
            Log.w(TAG, "File='" + filNam + "'");
            try {
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/" + filNam);
                View v1 = getWindow().getDecorView().getRootView();             // **\
                v1.setDrawingCacheEnabled(true);                                // ** \Capture screen
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());      // ** /  as bitmap
                v1.setDrawingCacheEnabled(false);                               // **/
                FileOutputStream fos = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                fos.flush();
                fos.close();
                bitmap.recycle();           //release memory
                MediaActionSound sound = new MediaActionSound();
                sound.play(MediaActionSound.SHUTTER_CLICK);
                Toast toast = Toast.makeText(getBaseContext(), "☢☢  Screen captured in Download/FRC5414  ☢☢", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } catch (Throwable e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //###################################################################
//###################################################################
//###################################################################
    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
   }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy ");
    }

}
