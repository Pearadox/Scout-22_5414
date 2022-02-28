package com.pearadox.scout_5414;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static android.util.Log.e;
import static android.util.Log.i;
import static com.pearadox.scout_5414.R.id.progressBar1;

public class DraftScout_Activity extends AppCompatActivity {

    String TAG = "DraftScout_Activity";        // This CLASS name
    Boolean is_Resumed = false;
    Boolean restart = false;
    int start = 0;                  // Start Position for matches (0=ALL)
    int numObjects = 0;
    int numProcessed = 0;
    int minMatches = 99;            // minimum # of matches collected
    int numPicks = 24;              // # of picks to show for Alliance Picks (actually get from Preferences)
    /*Shared Prefs-Scored Cargo*/ 
    public String Cargo_L0 = "";    // Lower
    public String Cargo_L1 = "";    // Upper
    public String Cargo_C0 = "";    // Floor
    public String Cargo_C1 = "";    // Terminal
    /*Shared Prefs-Climbing*/
    public String climbClimb = "";
    public String climbHang0 = "";
    public String climbHang1 = "";
    public String climbHang2 = "";
    public String climbHang3 = "";
    public String climbHang4 = "";
    /*Weight factors*/ 
    public String wtClimb = "";
    public String wtCargo = "";
    public String wtPenalty = "";
    public String wtLostComm = "";

    ImageView imgStat_Load;
    TextView txt_EventName, txt_NumTeams, txt_Formula, lbl_Formula, txt_LoadStatus, txt_SelNum;
    Spinner spinner_numMatches;
    ListView lstView_Teams;
    TextView TeamData, BA, Stats, Stats2;
    Button btn_Match, btn_Pit, btn_Default;
    ProgressBar pbSpinner;
    RadioGroup radgrp_Sort;
    RadioButton radio_Climb, radio_Cargo, radio_Weight, radio_Team, radio_pGt1, radio_cGt1, radio_ControlPanel;
    //    Button btn_Up, btn_Down, btn_Delete;
    public ArrayAdapter<String> adaptTeams;
    public static String[] numMatch = new String[]             // Num. of Matches to process
            {"ALL", "Last", "Last 2", "Last 3", "Last 4"};
    //    ArrayList<String> draftList = new ArrayList<String>();
    static final ArrayList<HashMap<String, String>> draftList = new ArrayList<HashMap<String, String>>();
    public int teamSelected = -1;
    public static String sortType = "";
    private ProgressDialog progress;
    String tNumb = "";
    String tn = "";
    String Viz_URL = "";
    String teamNum = "";
    String teamName = "";
    String tmRank = "";
    String tmRScore = "";
    String tmWLT = "";
    String tmOPR = "";
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();

    String Tarmac = "";
    String autoLow = "";
    String autoUpper = "";
    String aLowMiss = "";
    String aHighMiss = "";
    String lowPercentA ="";
    String upPercentA ="";
    String teleCargoL0 = "";
    String teleCargoL1 = "";
    String tLowMiss = "";
    String tHighMiss = "";
    String lowPercentT ="";
    String upPercentT ="";
    String autoCollectFloor = "";
    String autoCollectTerm = "";
    String teleCollectFloor = "";
    String teleCollectTerm = "";
    String climb = "";
    String climb_Hang0 = "";
    String climb_Hang1 = "";
    String climb_Hang2 = "";
    String climb_Hang3 = "";
    String climb_Hang4 = "";
    String Penalties = "";
    String LostComms = "";
    String Tipped = "";
    String mdNumMatches = "";

    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfMatchData_DBReference;
    FirebaseStorage storage;
    StorageReference storageRef;
    matchData match_inst = new matchData();
    // -----  Array of Match Data Objects for Draft Scout
    public static ArrayList<matchData> All_Matches = new ArrayList<matchData>();
    String load_team, load_name;

    //===========================   SCORES object that is used for Comparator SORT  ==================================
    public static class Scores {
        private String teamNum;
        private String teamName;
        private String scrRank;
        private String scrRScore;
        private String scrWLT;
        private String scrOPR;
        private float SCORE_PowerCellScore;
        private float SCORE_panelsScore;
        private float SCORE_climbScore;
        private float SCORE_combinedScore;

        public Scores() {
        }

// ** Constuctor **

        public Scores(String teamNum, String teamName, String scrRank, String scrRScore, String scrWLT, String scrOPR, float SCORE_PowerCellScore, float SCORE_panelsScore, float SCORE_climbScore, float SCORE_combinedScore) {
            this.teamNum = teamNum;
            this.teamName = teamName;
            this.scrRank = scrRank;
            this.scrRScore = scrRScore;
            this.scrWLT = scrWLT;
            this.scrOPR = scrOPR;
            this.SCORE_PowerCellScore = SCORE_PowerCellScore;
            this.SCORE_panelsScore = SCORE_panelsScore;
            this.SCORE_climbScore = SCORE_climbScore;
            this.SCORE_combinedScore = SCORE_combinedScore;
        }

// ** Getters/Setters **

        public String getTeamNum() {
            return teamNum;
        }

        public void setTeamNum(String teamNum) {
            this.teamNum = teamNum;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getScrRank() {
            return scrRank;
        }

        public void setScrRank(String scrRank) {
            this.scrRank = scrRank;
        }

        public String getScrRScore() {
            return scrRScore;
        }

        public void setScrRScore(String scrRScore) {
            this.scrRScore = scrRScore;
        }

        public String getScrWLT() {
            return scrWLT;
        }

        public void setScrWLT(String scrWLT) {
            this.scrWLT = scrWLT;
        }

        public String getScrOPR() {
            return scrOPR;
        }

        public void setScrOPR(String scrOPR) {
            this.scrOPR = scrOPR;
        }

        public float getSCORE_PowerCellScore() {
            return SCORE_PowerCellScore;
        }

        public void setSCORE_PowerCellScore(float SCORE_PowerCellScore) {
            this.SCORE_PowerCellScore = SCORE_PowerCellScore;
        }

        public float getSCORE_panelsScore() {
            return SCORE_panelsScore;
        }

        public void setSCORE_panelsScore(float SCORE_panelsScore) {
            this.SCORE_panelsScore = SCORE_panelsScore;
        }

        public float getSCORE_climbScore() {
            return SCORE_climbScore;
        }

        public void setSCORE_climbScore(float SCORE_climbScore) {
            this.SCORE_climbScore = SCORE_climbScore;
        }

        public float getSCORE_combinedScore() {
            return SCORE_combinedScore;
        }

        public void setSCORE_combinedScore(float SCORE_combinedScore) {
            this.SCORE_combinedScore = SCORE_combinedScore;
        }

        public static Comparator<Scores> getTeamComp() {
            return teamComp;
        }

        public static void setTeamComp(Comparator<Scores> teamComp) {
            Scores.teamComp = teamComp;
        }

        public static Comparator<Scores> getClimbComp() {
            return climbComp;
        }

        public static void setClimbComp(Comparator<Scores> climbComp) {
            Scores.climbComp = climbComp;
        }


// ** END - Getters/Setters **

        public static Comparator<Scores> teamComp = new Comparator<Scores>() {
            public int compare(Scores t1, Scores t2) {
                String TeamNum1 = t1.getTeamNum();
                String TeamNum2 = t2.getTeamNum();
                //ascending order
                return TeamNum1.compareTo(TeamNum2);
                //descending order
                //return TeamNum2.compareTo(TeamNum1);
            }
        };
        public static Comparator<Scores> climbComp = new Comparator<Scores>() {
            public int compare(Scores s1, Scores s2) {
                float climbNum1 = s1.getSCORE_climbScore();
                float climbNum2 = s2.getSCORE_climbScore();
                /*For ascending order*/
                //return climbNum1-climbNum2;
                /*For descending order*/
                return (int) (climbNum2 - climbNum1);
            }
        };

    }

    //==========================
    public ArrayList<Scores> team_Scores = new ArrayList<Scores>();
    Scores score_inst = new Scores();

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_draft_scout);
        Log.i(TAG, "@@@@@ DraftScout_Activity  @@@@@");

        Log.e(TAG, "B4 - " + sortType + "  R="+ restart);
        if (restart) {
            Log.e(TAG, "Are we ever getting called? " + is_Resumed);
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            String sortType = prefs.getString("Sort", "");
        } else {
            sortType = "Team#";
        }
        Log.e(TAG, "After - " + sortType);
        getprefs();         // Get multiplier values from Preferences

        txt_EventName = findViewById(R.id.txt_EventName);
        txt_NumTeams = findViewById(R.id.txt_NumTeams);
        txt_Formula = findViewById(R.id.txt_Formula);
        lbl_Formula = findViewById(R.id.lbl_Formula);
        txt_LoadStatus = findViewById(R.id.txt_LoadStatus);
        txt_SelNum = findViewById(R.id.txt_SelNum);
        txt_SelNum.setText("");
        lstView_Teams = findViewById(R.id.lstView_Teams);
        txt_EventName.setText(Pearadox.FRC_EventName);              // Event Name
        txt_NumTeams.setText(String.valueOf(Pearadox.numTeams));    // # of Teams
        txt_Formula.setText(" ");
        btn_Match = findViewById(R.id.btn_Match);
        btn_Pit = findViewById(R.id.btn_Pit);
        btn_Match.setEnabled(false);
        btn_Match.setVisibility(View.INVISIBLE);
        btn_Pit.setEnabled(false);
        btn_Pit.setVisibility(View.INVISIBLE);
        pbSpinner = (ProgressBar) findViewById(progressBar1);

        pfDatabase = FirebaseDatabase.getInstance();
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data

        initScores();

        RadioGroup radgrp_Sort = findViewById(R.id.radgrp_Sort);
        for (int i = 0; i < radgrp_Sort.getChildCount(); i++) {        // turn them all OFF
            radgrp_Sort.getChildAt(i).setEnabled(false);
        }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        radgrp_Sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, "@@ RadioClick_Sort @@");
                minMatches = 99;    // Reset for new search
                txt_SelNum = findViewById(R.id.txt_SelNum);
                txt_SelNum.setText("");
                teamSelected = -1;
                radio_Team = findViewById(checkedId);
                String value = radio_Team.getText().toString();
//                Log.w(TAG, "RadioSort -  '" + value + "'");
                switch (value) {
                    case "Climb":
//                        Log.w(TAG, "Climb sort");
                        sortType = "Climb";
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getSCORE_climbScore(), c2.getSCORE_climbScore());
                            }
                        });
                        Collections.reverse(team_Scores);
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Cargo":
                        sortType = "Cargo";
//                      Log.w(TAG, "Cargo sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getSCORE_PowerCellScore(), c2.getSCORE_PowerCellScore());
                            }
                        });
                        Collections.reverse(team_Scores);       // Descending
                        showFormula("Cargo");               // update the formula
                        loadTeams();
                        break;
                    case "Combined":
//                Log.w(TAG, "Combined sort");
                        sortType = "Combined";
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getSCORE_combinedScore(), c2.getSCORE_combinedScore());
                            }
                        });
                        Collections.reverse(team_Scores);   // Descending
                        showFormula(sortType);              // update the formula
                        loadTeams();
                        break;
                    case "Team#":
//                Log.w(TAG, "Team# sort");
                        sortType = "Team#";
                        Collections.sort(team_Scores, Scores.teamComp);
                        lbl_Formula.setTextColor(Color.parseColor("#ffffff"));
                        txt_Formula.setText(" ");       // set formula to blank
                        loadTeams();
                        break;
                    default:                //
                        Log.e(TAG, "*** Invalid Sort " + value);
                }
            }
        });


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        lstView_Teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                Log.w(TAG, "*** lstView_Teams ***   Item Selected: " + pos);
                teamSelected = pos;
                lstView_Teams.setSelector(android.R.color.holo_blue_light);
                /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
//                tnum = draftList.get(teamSelected).substring(0,4);
                txt_SelNum = findViewById(R.id.txt_SelNum);
                txt_SelNum.setText(String.valueOf(pos + 1));      // Sort Position
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }

    private void getprefs() {
        Log.i(TAG, "** getprefs **");

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        Cargo_L0 = sharedPref.getString("prefCargo_L0", "1.0");             // Low
        Cargo_L1 = sharedPref.getString("prefCargo_L1", "3.0");             // Upper
        Cargo_C0 = sharedPref.getString("prefCargo_C0", "1.5");             // Floor
        Cargo_C1 = sharedPref.getString("prefCargo_C1", "1.0");             // Terminal

        climbClimb = sharedPref.getString("prefClimb_climb", "1.0");        // Climbed
        climbHang0 = sharedPref.getString("prefClimb_Hang0", "-2.0");       //** None
        climbHang1 = sharedPref.getString("prefClimb_Hang1", "1.0");        //** Low
        climbHang2 = sharedPref.getString("prefClimb_Hang2", "2.5");        //** Mid
        climbHang3 = sharedPref.getString("prefClimb_Hang3", "5.0");        //** High
        climbHang4 = sharedPref.getString("prefClimb_Hang4", "7.0");        //** Traversal

        wtClimb    = sharedPref.getString("prefWeight_climb", "3.0");
        wtCargo    = sharedPref.getString("prefWeight_Cargo", "2.0");
        wtPenalty  = sharedPref.getString("prefWeight_Penalty", "-1.5");    // Penalties
        wtLostComm = sharedPref.getString("prefWeight_Comms", "-2.5");      // Lost Comms

        numPicks = Integer.parseInt(sharedPref.getString("prefAlliance_num", "24"));


    }

    private String showFormula(String typ) {
        Log.i(TAG, "** showFormula **  " + typ);
        String form = "";
        getprefs();         // make sure Prefs are up to date
        switch (typ) {
            case "Climb":
                form = "((" + climbClimb + " * Climbs)  ✚  ((" + climbHang0 + "*Hang0)(" + climbHang1 + "*Hang1) + (" + climbHang2 + " * Hang2) + (" + climbHang3 + " * Hang3) + (" + climbHang4 + " * Hang4))) / #Matches";
                lbl_Formula.setTextColor(Color.parseColor("#4169e1"));      // blue
                txt_Formula.setText(form);
                break;
            case "Cargo":
                form = "( (" + Cargo_L0 + "* (aLow + tLow)) + (" + Cargo_L1 + "* (aUpper + tUpper))   ✚  ";
                form = form + " ( " + Cargo_C0 + "*(aFloor + tFloor) +" + Cargo_C1 +"*(aTerm + tTerm)" + " ) /#Matches";
                lbl_Formula.setTextColor(Color.parseColor("#ee00ee"));      // magenta
                txt_Formula.setText(form);
                break;
            case "Combined":
                form = "((" + wtClimb + " * climbScore) ✚ (" + wtCargo + " * CargoScore) ✚ (" + wtPenalty + " * #Penalties) ✚ (" + wtLostComm + " * #LostComms)) / #Matches";
                lbl_Formula.setTextColor(Color.parseColor("#ff0000"));      // red
                txt_Formula.setText(form);
                break;
            default:                //
                Log.e(TAG, "*** Invalid Button Type " + typ);
        }
        return typ;
    }

    //===========================================================================================
    public class numMatches_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Boolean goodToGo = false;
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> NumMatches '" + num + "'  " + minMatches);
            switch (num) {
                case "Last":
                    if (minMatches >= 1) {
                        start = numObjects - 1;     //
                        numProcessed = 1;
                        goodToGo = true;
                    } else {
                        Toast_Msg(num, minMatches);
                        goodToGo = false;
                    }
                    break;
                case "Last 2":
                    if (minMatches >= 2) {
                        start = numObjects - 2;     //
                        numProcessed = 2;
                        goodToGo = true;
                    } else {
                        Toast_Msg(num, minMatches);
                        goodToGo = false;
                    }
                    break;
                case "Last 3":
//                    Log.d(TAG, "@@@ Last 3 @@@" );
                    if (minMatches >= 3) {
                        start = numObjects - 3;     //
                        numProcessed = 3;
                        goodToGo = true;
                    } else {
                        Toast_Msg(num, minMatches);
                        goodToGo = false;
                    }
                    break;
                case "Last 4":
//                    Log.d(TAG, "@@@ Last 4 @@@" );
                    if (minMatches >= 4) {
                        start = numObjects - 4;     //
                        numProcessed = 4;
                        goodToGo = true;
                    } else {
                        Toast_Msg(num, minMatches);
                        goodToGo = false;
                    }
                    break;
                case "ALL":
                    start = 0;                  // Start at beginning
                    numProcessed = numObjects;
                    goodToGo = true;
                    break;
                default:                //
                    Log.e(TAG, "Invalid number of matches - " + start);
            }
            Log.e(TAG, "*** Start=" + start + "  #Proc=" + numProcessed + "  " + goodToGo);
            if (goodToGo) {
                Log.e(TAG, "We are good To Go!! - Start=" + start + " #Proc=" + numProcessed);
//            init_Values();
//            getMatch_Data();
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    public void Toast_Msg(String choice, Integer minimum) {
        Toast toast = Toast.makeText(getBaseContext(), "\nCannot show '" + choice + "' some teams have " + minimum + " matches \n ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonDefault_Click(View view) {
        // Reload _ALL_ the Preference defaults
        Log.i(TAG, ">>>>> buttonDefault_Click");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Toast toast = Toast.makeText(getBaseContext(), "Default Settings have been reset", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        showFormula(sortType);              // update the formula
        loadTeams();                        // reload based on default
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonMatch_Click(View view) {
        Log.i(TAG, ">>>>> buttonMatch_Click  " + teamSelected);
        HashMap<String, String> temp = new HashMap<String, String>();
        String teamHash;
        setProgressBarIndeterminateVisibility(true);
//        pbSpinner = (ProgressBar) findViewById(progressBar1);
//        pbSpinner.setVisibility(View.VISIBLE);
        if (teamSelected >= 0) {
            draftList.get(teamSelected);
            temp = draftList.get(teamSelected);
            teamHash = temp.get("team");
//        Log.w(TAG, "teamHash: '" + teamHash + "' \n ");
            load_team = teamHash.substring(0, 4);
            load_name = teamHash.substring(5, teamHash.indexOf("("));  // UP TO # MATCHES
//        Log.w(TAG, ">>>team & name: '" + load_team + "'  [" + load_name +"]");
            addMatchData_Team_Listener(pfMatchData_DBReference);        // Load Matches for _THIS_ selected team
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Team selected for Match Data ★★★★", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        pbSpinner.setVisibility(View.INVISIBLE);
        setProgressBarIndeterminateVisibility(false);
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonPit_Click(View view) {
        Log.i(TAG, ">>>>> buttonPit_Click  " + teamSelected);
        HashMap<String, String> temp = new HashMap<String, String>();
        String teamHash = "";
        final String[] URL = {""};
        Viz_URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();

        if (teamSelected >= 0) {
            draftList.get(teamSelected);
            temp = draftList.get(teamSelected);
            teamHash = temp.get("team");
            teamNum = teamHash.substring(0, 4);
            teamName = teamHash.substring(5, teamHash.indexOf("("));  // UP TO # MATCHES
            Log.e(TAG, "****** team  '" + teamName + "' ");
//            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://pearadox-2022.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + teamNum.trim() + ".png");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.w(TAG, "@@@@@@@@  Getting Photo!   URI=" + uri);
                    URL[0] = uri.toString();
                    Viz_URL = URL[0];
                    Log.w(TAG, "*GOOD* URL=" + Viz_URL);
                    launchVizPit(teamNum, teamName, Viz_URL);   // do it here to WAIT for FB retrieval
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "ERR= '" + exception + "'");
                    Viz_URL = "";
                    Log.w(TAG, "*BAD* URL=" + Viz_URL + "' ");
                    launchVizPit(teamNum, teamName, Viz_URL);
                }
            });
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Team selected for Pit Data ★★★★", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void launchVizPit(String team, String name, String imgURL) {
        Log.d(TAG, ">>>>>>>>  launchVizPit " + team + " " + name + " " + imgURL);      // ** DEBUG **
        Intent pit_intent = new Intent(DraftScout_Activity.this, VisPit_Activity.class);
        Bundle VZbundle = new Bundle();
        VZbundle.putString("team", team);        // Pass data to activity
        VZbundle.putString("name", name);        // Pass data to activity
        VZbundle.putString("url", imgURL);       // Pass data to activity
        pit_intent.putExtras(VZbundle);
        startActivity(pit_intent);               // Start Visualizer for Pit Data

    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMatchData_Team_Listener(final DatabaseReference pfMatchData_DBReference) {
        pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "<<<< addMatchData_Team_Listener >>>> Match Data for team " + load_team);
                Pearadox.Matches_Data.clear();
                matchData mdobj = new matchData();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    mdobj = iterator.next().getValue(matchData.class);
                    if (mdobj.getTeam_num().matches(load_team)) {
                        Pearadox.Matches_Data.add(mdobj);
                    }
                }
                Log.i(TAG, "***** Matches Loaded. # = " + Pearadox.Matches_Data.size());
                if (Pearadox.Matches_Data.size() > 0) {
                    Intent match_intent = new Intent(DraftScout_Activity.this, VisMatch_Activity.class);
                    Bundle VZbundle = new Bundle();
                    VZbundle.putString("team", load_team);          // Pass data to activity
                    VZbundle.putString("name", load_name);          // Pass data to activity
                    match_intent.putExtras(VZbundle);
                    startActivity(match_intent);                    // Start Visualizer for Match Data
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), "★★★★  There is _NO_ Match Data for Team " + load_team + "  ★★★★", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }

    private void loadTeams() {
        Log.w(TAG, "@@@@  loadTeams started  @@@@  " + team_Scores.size() + " Type=" + sortType);
//        pbSpinner.setVisibility(View.VISIBLE);

        SimpleAdapter adaptTeams = new SimpleAdapter(
                this,
                draftList,
                R.layout.draft_list_layout,
                new String[]{"team", "BA", "Stats", "Stats2", "Stats3"},
                new int[]{R.id.TeamData, R.id.BA, R.id.Stats, R.id.Stats2, R.id.Stats3}
        );

        draftList.clear();
        String totalScore = "";
        minMatches = 99;    // Reset for new search
        for (int i = 0; i < team_Scores.size(); i++) {    // load by sorted scores
            score_inst = team_Scores.get(i);
//            Log.w(TAG, i +" team=" + score_inst.getTeamNum());
            HashMap<String, String> temp = new HashMap<String, String>();
            tn = score_inst.getTeamNum();
            tmRank = score_inst.getScrRank();
            tmRScore = score_inst.getScrRScore();
            tmWLT = score_inst.scrWLT;
            tmOPR = score_inst.getScrOPR();

            // ToDo - use start & NumObjects
            teamData(tn);   // Get Team's Match Data
            switch (sortType) {
                case "Climb":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_climbScore()) + "]";
                    break;
                case "Cargo":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_PowerCellScore()) + "]";
                    break;
                case "Combined":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_combinedScore()) + "]";
                    break;
                case "Team#":
                    totalScore = " ";
                    break;
                default:                //
                    Log.e(TAG, "Invalid Sort - " + sortType);
            }

            // Todo - add Missed & shooting %
            temp.put("Stats3", "Climb ╪" + climb  + "   Hangs ₀" + climb_Hang0 + " ₁" + climb_Hang1 + " ₂" + climb_Hang2 + " ₃" + climb_Hang3 + " ₃" + climb_Hang4 + "    ⚑" + Penalties + " ⚡" + LostComms + "  ◥ " + Tipped);
            temp.put("Stats2", "Tele ◯L" + teleCargoL0 + " U" + teleCargoL1 + "  ⊗L" + tLowMiss + " U" + tHighMiss  + "   L%=" + lowPercentT + "  U%=" + upPercentT + "   ◯↑ F" + teleCollectFloor + " T" + teleCollectTerm );
            temp.put("Stats", "Auto ≠" + Tarmac + "   ◯L" + autoLow + " U" + autoUpper + "  ⊗L" + aLowMiss + " U" + aHighMiss + "   L%=" + lowPercentA + "  U%=" + upPercentA + "   ◯↑ F" + autoCollectFloor + " T" + autoCollectTerm );
            temp.put("team", tn + "-" + score_inst.getTeamName() + "  (" + mdNumMatches + ")  " + totalScore);
            temp.put("BA", "Rank=" + tmRank + "   Score=" + tmRScore + "   WLT=" + tmWLT + "   OPR=" + tmOPR);
            draftList.add(temp);
        } // End For
        Log.w(TAG, "### Teams ###  : " + draftList.size());
        lstView_Teams.setAdapter(adaptTeams);
        adaptTeams.notifyDataSetChanged();
    }

    // ****************************************************************************************8
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draft, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(TAG, "@@@  Options  @@@ " + sortType);
        Log.w(TAG, " \n  \n");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DraftSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_help) {
            Intent help_intent = new Intent(this, HelpActivity.class);
            startActivity(help_intent);    // Show Help
            return true;
        }
        if (id == R.id.action_picks) {
            Log.e(TAG, "Picks");
            Toast toast = Toast.makeText(getBaseContext(), "\n Generating Alliance Picks file - Please wait ... \n ", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            txt_Formula.setText("Generating Alliance Picks file - Please wait ...");
            alliance_Picks();
            return true;
        }
        if (id == R.id.action_screen) {
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-Draft" + "_" + sortType + ".JPG";
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

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void alliance_Picks() {
        Log.e(TAG, "@@@  alliance_Picks  @@@ ");
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
//        Toast toast = Toast.makeText(getBaseContext(), "\n Generating Alliance Picks file - Please wait ... \n ", Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        toast.show();

        String tName = "";
        String totalScore = "";
        String DS = "";
        String underScore = new String(new char[30]).replace("\0", "_");    // string of 'x' underscores
        String blanks = new String(new char[50]).replace("\0", " ");        // string of 'x' blanks
        String pound = new String(new char[50]).replace("\0", "#");        // string of 'x' Pound
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss a").format(new Date());

// ======================================================================================
        sortType = "Combined";          // Attempt to "force" correct sort 1st time
        Collections.sort(team_Scores, new Comparator<Scores>() {
            @Override
            public int compare(Scores c1, Scores c2) {
                return Float.compare(c1.getSCORE_combinedScore(), c2.getSCORE_combinedScore());
            }
        });
        Collections.reverse(team_Scores);   // Descending
        loadTeams();
// ======================================================================================
        // ToDo - Convert to Async task
        if (numPicks > team_Scores.size()) {
//            Log.w(TAG, "******>> numPick changed to: " + team_Scores.size());
            numPicks = team_Scores.size();      // Use max (prevent Error when # teams < 'numPicks')
        }
        if (numPicks > 24) {            //**** Try to keep on one page!
            DS = "";                    // Use Single Space
        } else {
            DS = "\n";                  // Use Double Space on anything less than 24
        }
        try {
            String destFile = Pearadox.FRC_ChampDiv + "_Alliance-Picks" + ".txt";
            File prt = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/" + destFile);
//            Log.e(TAG, " path = " + prt);
//            BufferedWriter bW;
//            bW = new BufferedWriter(new FileWriter(prt, false));    // true = Append to existing file
            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(prt), "UTF-8"
            ));
            bW.write(Pearadox.FRC_ChampDiv + "-" + Pearadox.FRC_EventName +  "   (" + timeStamp +")\n");
            bW.write(underScore + "  COMBINED  " + underScore + "\n" + DS);
            //  Combined sort
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getSCORE_combinedScore(), c2.getSCORE_combinedScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));     // Pad the name to line up Tabs (\t)
//                Log.e(TAG, ">>>>  teamName '" + tName + "' ");
                totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_combinedScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i + 1) + ") " + tNumb + "-" + tName + "\t  (" + String.format("%2d", (Integer.parseInt(mdNumMatches))) + ")   " + totalScore + " \t");
                bW.write("\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n" + (char) 12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "   (" + timeStamp +")\n");
            bW.write(underScore + "  CARGO  " + underScore + "\n \n");
            //  Cargo sort
            sortType = "Cargo";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getSCORE_PowerCellScore(), c2.getSCORE_PowerCellScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_PowerCellScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                // ToDo - check format & add misses & %
                bW.write(String.format("%2d", i + 1) + ") " + tNumb + "-" + tName + "\t (" + String.format("%2d", (Integer.parseInt(mdNumMatches))) + ") " + totalScore + "\t");
                bW.write("Auto◯L" + autoLow + " U" + autoUpper + " ⊗L" + aLowMiss + " U" + aHighMiss + " L%=" + lowPercentA.substring(0,4) + " U%=" + upPercentA.substring(0,4) + "  Tele◯L" + teleCargoL0 + " U" + teleCargoL1 + " ⊗L" + tLowMiss + " U" + tHighMiss + "  L%=" + lowPercentT.substring(0,4) + "  U%=" + upPercentT.substring(0,4) );
                bW.write("\n" + DS);
            } // end For #
            bW.write(" \n" + "\n" + (char) 12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "   (" + timeStamp +")\n");
            bW.write(underScore + "  CLIMB  " + underScore + "\n \n");
            //  Climb sort
            sortType = "Climb";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getSCORE_climbScore(), c2.getSCORE_climbScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            // ToDo - check format
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_climbScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i + 1) + ") " + tNumb + " - " + tName + "\t  (" + String.format("%2d", (Integer.parseInt(mdNumMatches))) + ") " + totalScore + " \t");
                bW.write("╪" + climb  );
                bW.write("  Hang ⊗" + climb_Hang0 + " ➊" + climb_Hang1 + " ➋" + climb_Hang2 + " ➌" + climb_Hang3  + " ➍" + climb_Hang4);
                bW.write("\n" + DS);
            } // end For # teams
//            bW.write(" \n" + "\n" + (char)12);        // NL & FF
            //=====================================================================

            bW.write(" \n" + "\n");        // NL
            //=====================================================================

            bW.flush();
            bW.close();
            Toast toast2 = Toast.makeText(getBaseContext(), "*** '" + Pearadox.FRC_Event + "' Alliance Picks file (" + numPicks + " teams) written to SD card [Download/FRC5414] ***", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast2.show();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " not found in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        pickList();     // generate Picklist for PickList app
    }

    // ======================================================================================
    private void pickList() {
        Log.w(TAG, "$$$$  pickList  $$$$ ");
       sortType = "Combined";          //
        Collections.sort(team_Scores, new Comparator<Scores>() {
            @Override
            public int compare(Scores c1, Scores c2) {
                return Float.compare(c1.getSCORE_combinedScore(), c2.getSCORE_combinedScore());
            }
        });
        Collections.reverse(team_Scores);   // Descending
        loadTeams();
//        Log.w(TAG,"Size=" + draftList.size());
// ======================================================================================
        HashMap<String, String> temp = new HashMap<String, String>();
        try {
            String destFile = Pearadox.FRC_ChampDiv.toUpperCase() + "_Pick-List" + ".txt";
            File prt = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/" + destFile);
            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(prt), "UTF-8"
            ));

            for (int i = 0; i < draftList.size(); i++) {    // load by sorted scores
                temp = draftList.get(i);
                bW.write(temp + "\n");

            } //end FOR
            //**********************************************
            bW.write(" \n" + "\n");        // NL
            bW.flush();
            bW.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " not found in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
    private void teamData(String team) {
//        Log.i(TAG, "$$$$  teamData  $$$$ " + team);
        int base = 0;
        int colFloor = 0;
        int colTerminal = 0;
        int TcolFloor = 0;
        int TcolTerminal = 0;
        int aLow = 0;
        int aUpper = 0;
        int aMissedLow = 0;
        int aMissedUpper = 0;
        int tLow = 0;
        int tUpper = 0;
        int tMissedLow = 0;
        int tMissedUpper = 0;
        int pctLower = 0;
        int pctUpper = 0;
        int climbed = 0;
        int climbH0 = 0;
        int climbH1 = 0;
        int climbH2 = 0;
        int climbH3 = 0;
        int climbH4 = 0;
        int pen = 0;
        int lightning = 0;
        int tipOver = 0;
        int numMatches = 0;

//        Log.d(TAG, ">>>>>>> All_Matches " + All_Matches.size());
        for (int i = 0; i < All_Matches.size(); i++) {
            match_inst = All_Matches.get(i);      // Get instance of Match Data
//            Log.e(TAG, i + " ##### FOR   Q" + match_inst.getMatch() + "  Team=" + team);
            if (match_inst.getTeam_num().matches(team)) {
                numMatches++;
//                Log.w(TAG, "Team Match " + team);
                // New Match Data Object *** GLF 1/27/20
                // ******************** Autonomous ********************
                if (match_inst.isAuto_leftTarmac()) {
                    base++;
                }

                if (match_inst.isAuto_CollectFloor()) {         //**  Collect Auto
                    colFloor++;                                 //**
                }                                               //**
                if (match_inst.isAuto_CollectTerm()) {          //**
                    colTerminal++;                              //**
                }                                               //**
                aLow = aLow + match_inst.getAuto_Low();
                aUpper = aUpper + match_inst.getAuto_High();
                aMissedLow = aMissedLow + match_inst.getAuto_MissedLow();
                aMissedUpper = aMissedUpper + match_inst.getAuto_MissedHigh();
                Float BatAvg;
                Log.e(TAG, "AutoLow   Score" + aLow  +"  Miss" +aMissedLow);
                if ((aLow + aMissedLow) > 0) {
                    BatAvg = (float)aLow / (aLow + aMissedLow);  // Made ÷ Attempts
                    Log.e(TAG, "Low%= " + BatAvg);
                    if (BatAvg == 1.0f) {
                        lowPercentA = "1.00";
                    } else {
                        lowPercentA = String.format("%.3f", BatAvg);
                    }
                } else {
                    lowPercentA = "0.00";
                }
                if ((aUpper + aMissedUpper) > 0) {
                    BatAvg = (float)aUpper / (aUpper + aMissedUpper);  // Made ÷ Attempts
                    if (BatAvg == 1.0f) {
                        upPercentA = "1.00";
                    } else {
                        upPercentA =String.format("%.3f", BatAvg);
                    }
                } else {
                    upPercentA = "0.00";
                }

                // *************************************************
                // ******************** TeleOps ********************
                // *************************************************
                tLow = tLow + match_inst.getTele_Low();
                tUpper = tUpper + match_inst.getTele_High();
                tMissedLow = tMissedLow + match_inst.getTele_MissedLow();
                tMissedUpper = tMissedUpper + match_inst.getTele_MissedHigh();
                // ToDo - percentage
                if ((tLow + tMissedLow) > 0) {        // avoid divide by 0
                    BatAvg = (float)tLow / (tLow + tMissedLow);  // Made ÷ Attempts
                    if (BatAvg == 1.0f) {       // all this to get 3 digits!!
                        lowPercentT = "1.00";
                    } else {
                        Log.e(TAG, "TeleLow%= " + BatAvg +" L" + tLow  +"  M" +tMissedLow);
                        lowPercentT = String.format("%.3f", BatAvg);
                    }
                } else {
                    Log.e(TAG, "Default TeleLow = .000");
                    lowPercentT = "0.00";
                }
                if ((tUpper + tMissedUpper) > 0) {
                    BatAvg = (float)tUpper / (tUpper + tMissedUpper);    // Made ÷ Attempts
                    if (BatAvg == 1.0f) {
                        upPercentT = "1.00";
                    } else {
                        Log.e(TAG, "TeleHigh%= " + BatAvg+" L" + tUpper  +"  M" +tMissedUpper);
                        upPercentT = String.format("%.3f", BatAvg);
                    }
                } else {
                    upPercentT = "0.00";
                }

                if (match_inst.isTele_Cargo_floor()) {          //**  Collect Tele
                    TcolFloor++;                                //**
                }                                               //**
                 if (match_inst.isTele_Cargo_term()) {          //**
                    TcolTerminal++;                             //**
                }                                               //*********

                if (match_inst.isTele_Climbed()) {
                    climbed++;
                }
                switch (match_inst.getTele_HangarLevel()) {
                    case "None":
                        climbH0++;
                        break;
                    case "Low":
                        climbH1++;
                        break;
                    case "Mid":
                        climbH2++;
                        break;
                    case "High":
                        climbH3++;
                        break;
                    case "Traversal":
                        climbH4++;
                        break;
                    default:                // ????
                        Log.e(TAG, "*** Error - bad Hangar Level  ***");
                } // end Switch
                pen = pen + match_inst.getTele_num_Penalties();
                if (match_inst.isFinal_lostComms()) {
                    lightning++;
                }
                if (match_inst.isFinal_tipped()) {
                    tipOver++;
                }
//                Log.w(TAG, "Accum. matches = " + numMatches);
            } //End if teams equal
        } // End For _ALL_ matches

//        Log.w(TAG, "####### Total Matches/Team = " + numMatches);
        mdNumMatches = String.valueOf(numMatches);
        if (numMatches < minMatches) {
            minMatches = numMatches;
//            Log.e(TAG, team + " >>>>>>>>>>  Min. matches changed = " + minMatches);
        }
        if (numMatches > 0) {
            Tarmac = String.valueOf(base);
            autoLow = String.valueOf(aLow);
            autoUpper = String.valueOf(aUpper);
            aLowMiss = String.valueOf(aMissedLow);
            aHighMiss = String.valueOf(aMissedUpper);
            teleCargoL0 = String.valueOf(tLow);
            teleCargoL1 = String.valueOf(tUpper);
            tLowMiss = String.valueOf(tMissedLow);
            tHighMiss = String.valueOf(tMissedUpper);
//            lowPercentA = String.valueOf(pctLower);
//            upPercentA = String.valueOf(pctUpper);
            autoCollectFloor = String.valueOf(colFloor);
            teleCollectFloor = String.valueOf(TcolFloor);
            autoCollectTerm = String.valueOf(colTerminal);
            teleCollectTerm = String.valueOf(TcolTerminal);
            climb = String.valueOf(climbed);
            climb_Hang0 = String.valueOf(climbH0);
            climb_Hang1 = String.valueOf(climbH1);
            climb_Hang2 = String.valueOf(climbH2);
            climb_Hang3 = String.valueOf(climbH3);
            climb_Hang4 = String.valueOf(climbH4);
            Penalties = String.valueOf(pen);
            LostComms = String.valueOf(lightning);
            Tipped = String.valueOf(tipOver);
        } else {
            Tarmac = "0";
            autoLow = "0";
            autoUpper = "0";
            aLowMiss = "0";
            aHighMiss = "0";
            teleCargoL0 = "0";
            teleCargoL1 = "0";
            tLowMiss = "0";
            tHighMiss = "0";
            lowPercentA = "0.00";
            upPercentA = "0.00";
            autoCollectFloor = "0";
            autoCollectTerm = "0";
            teleCollectFloor = "0";
            teleCollectTerm = "0";
            climb = "0";
            climb_Hang0 = "0";
            climb_Hang1 = "0";
            climb_Hang2 = "0";
            climb_Hang3 = "0";
            climb_Hang4 = "0";
            Penalties = "0";
            LostComms = "0";
            Tipped = "0";
        }
        //============================
        float climbScore = 0;
        float cellScored = 0;
        float cellCollect = 0;
        float PowerCellScore = 0;
        float combinedScore = 0;
        float ControlPanelScore = 0;
        if (numMatches > 0) {
            climbScore = ( (climbed * Float.parseFloat(climbClimb)) + (climbH0 * Float.parseFloat(climbHang0))+ (climbH1 * Float.parseFloat(climbHang1)) + (climbH2 * Float.parseFloat(climbHang2)) + (climbH3 * Float.parseFloat(climbHang3)) + ((climbH4 * Float.parseFloat(climbHang4))) ) / (float)numMatches;
//            Log.e(TAG, team + " Climb ♺=" + (climbed*Float.parseFloat(climbClimb)) + " 円=" + (parked*Float.parseFloat(parkSG)) + " ⚖=" + (bal*Float.parseFloat(Balanced)) + " ✚" + " ₀=" + (climbH0*Float.parseFloat(climbHang0)) + " ₁=" + (climbH1*Float.parseFloat(climbHang1)) + " ₂=" + (climbH2*Float.parseFloat(climbHang2)) + " ₃=" + (climbH3*Float.parseFloat(climbHang3)) + " ✚ ↨₁=" + (lift1Num*Float.parseFloat(climbLift1))+ " ↨₂=" + (lift2Num*Float.parseFloat(climbLift2))+ " ↑=" + (gotLiftedNum*Float.parseFloat(climbLifted)) + "  / " + numMatches + " ==[ " + climbScore + "]");

            cellScored = ( ((aLow + tLow) * Float.parseFloat(Cargo_L0)) + ((aUpper + tUpper) * Float.parseFloat(Cargo_L1)) ) / (float)numMatches;
            cellCollect = ( ((colFloor+ TcolFloor) * Float.parseFloat(Cargo_C0)) + ((colTerminal+ TcolTerminal) * Float.parseFloat(Cargo_C0))) / (float)numMatches;
            PowerCellScore = cellScored + cellCollect;
//            Log.e(TAG, team + " Cells " + cellScored +  " ✚ " + cellCollect + "  / " + numMatches + " ==[ " + PowerCellScore + "]");

            combinedScore = (((climbScore * Float.parseFloat(wtClimb) + (PowerCellScore * Float.parseFloat(wtCargo)) + (pen * Float.parseFloat(wtPenalty)) + (lightning * Float.parseFloat(wtLostComm))  ) / (float)numMatches));
        } else {
            PowerCellScore = 0;
            ControlPanelScore = 0;
            climbScore = 0;
            combinedScore = 0;
        }
        String tNumber = "";
        for (int i = 0; i < team_Scores.size(); i++) {    // load by sorted scores
            Scores score_data = new Scores();
            score_data = team_Scores.get(i);
            tNumber = score_data.getTeamNum();
            if (score_data.getTeamNum().matches(team)) {
//                Log.w(TAG, "score team=" + score_data.getTeamNum());
                score_data.setSCORE_climbScore(climbScore);           // Save
                score_data.setSCORE_PowerCellScore(PowerCellScore);   //  all
                score_data.setSCORE_combinedScore(combinedScore);     //   scores
                score_data.setSCORE_panelsScore(ControlPanelScore);   //
            }
        }  // end For
    } // end teamData



    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMD_VE_Listener(final DatabaseReference pfMatchData_DBReference) {
        pfMatchData_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "<<<< getFB_Data >>>> _ALL_ Match Data ");
                ImageView imgStat_Load = findViewById(R.id.imgStat_Load);
                RadioGroup radgrp_Sort = findViewById(R.id.radgrp_Sort);
                txt_LoadStatus = findViewById(R.id.txt_LoadStatus);
                All_Matches.clear();
                matchData mdobj = new matchData();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    mdobj = iterator.next().getValue(matchData.class);
                    All_Matches.add(mdobj);
                }
                Log.e(TAG, "addMD_VE *****  Matches Loaded. # = "  + All_Matches.size());
                Button btn_Match = findViewById(R.id.btn_Match);
                Button btn_Pit = findViewById(R.id.btn_Pit);
                imgStat_Load.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                txt_LoadStatus.setText(All_Matches.size() + " Matches");
                for(int i = 0; i < radgrp_Sort.getChildCount(); i++){        // turn them all ON
                    radgrp_Sort.getChildAt(i).setEnabled(true);
                }
//              radio_Team = findViewById(R.id.radio_Team);
//              radio_Team.performClick();         // "force" radio button click  (so it works 1st time)
                btn_Match.setEnabled(true);
                btn_Match.setVisibility(View.VISIBLE);
                btn_Pit.setEnabled(true);
                btn_Pit.setVisibility(View.VISIBLE);
//                sortType = "Team#";          // Attempt to "force" correct sort 1st time
                Collections.sort(team_Scores, Scores.teamComp);
                loadTeams();    // load

                pbSpinner.setVisibility(View.INVISIBLE);
//                setProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });

        // ======================================================================================
    }

    private void initScores() {
        Log.i(TAG, " ## initScores ##  " + is_Resumed);
//        Log.d(TAG, "Start to Load team scores '"  + sortType + "'");
        team_Scores.clear();
        for (int i = 0; i < Pearadox.numTeams; i++) {
            Scores curScrTeam = new Scores();       // instance of Scores object
            team_inst = Pearadox.team_List.get(i);
            curScrTeam.setTeamNum(team_inst.getTeam_num());
            curScrTeam.setTeamName(team_inst.getTeam_name());
            curScrTeam.setScrRank(team_inst.getTeam_rank());
            curScrTeam.setScrRScore(team_inst.getTeam_rScore());
            curScrTeam.setScrWLT(team_inst.getTeam_WLT());
            curScrTeam.setScrOPR(team_inst.getTeam_OPR());
//            Log.w(TAG, curScrTeam.getTeamNum() + "  " + curScrTeam.getTeamName());
            curScrTeam.setSCORE_climbScore((float) 0);        //Climb
            curScrTeam.setSCORE_PowerCellScore((float) 0);    // Cargo
            curScrTeam.setSCORE_combinedScore((float) 0);     // Combined
            curScrTeam.setSCORE_panelsScore((float) 0);       // C.P.
            team_Scores.add(i, curScrTeam);
        } // end For
        Log.w(TAG, "#Scores = " + team_Scores.size());
//        if (sortType.matches("") || sortType.matches("Team#")) {       // if 1st time
//            sortType = "Team#";
//        } else {
            // ToDONE - Load teams according to Radio Button (VisMatch return messes it up)
            Log.e(TAG, "Leave scores alone '"  + sortType + "'");
            radgrp_Sort = findViewById(R.id.radgrp_Sort);
            radgrp_Sort.setActivated(true);
            radgrp_Sort.setSelected(true);
            switch (sortType) {
                case "Climb":
                    radio_Climb = findViewById(R.id.radio_Climb);
                    radio_Climb.performClick();         // "force" radio button click
                    break;
                case "Cargo":
                    radio_Cargo = findViewById(R.id.radio_Cargo);
                    radio_Cargo.performClick();         // "force" radio button click
                    break;
                case "Combined":
                    radio_Weight = findViewById(R.id.radio_Weight);
                    radio_Weight.performClick();         // "force" radio button click
                    break;
                case "Team#":
                    radio_Team = findViewById(R.id.radio_Team);
                    radio_Team.performClick();         // "force" radio button click
                    break;
                default:                //
                    Log.e(TAG, "*** Invalid Type " + sortType);
            }
//        } // End IF SortTyp
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");
    if (!restart) {
        addMD_VE_Listener(pfMatchData_DBReference);        // Load _ALL_ Matches
    }
    }

@Override
public void onResume() {
    super.onResume();
    Log.v(TAG, "****> onResume <**** " + sortType);
    is_Resumed = true;
    }

@Override
public void onRestart() {
    super.onRestart();
    Log.v(TAG, "****> onRestart <**** " + sortType);
    restart = true;
    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    String sortType = prefs.getString("Sort", "");
    Log.d(TAG, "Restart Prefs >> " + sortType);
}

@Override
public void onPause() {
    super.onPause();
    Log.v(TAG, "onPause  "  + sortType);
    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("Sort", sortType);
    editor.commit();        // keep sort type
    Log.d(TAG, "Pause Prefs >> " + sortType);
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
    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("Sort", "");
    editor.commit();        // reset sort type
    }
}

//