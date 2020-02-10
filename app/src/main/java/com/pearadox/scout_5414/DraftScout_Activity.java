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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import static android.util.Log.e;
import static android.util.Log.i;
import static com.pearadox.scout_5414.R.id.progressBar1;

public class DraftScout_Activity extends AppCompatActivity {

    String TAG = "DraftScout_Activity";        // This CLASS name
    Boolean is_Resumed = false;
    int start = 0;          // Start Position for matches (0=ALL)
    int numObjects = 0; int numProcessed = 0;
    int minMatches = 99;         // minimum # of matches collected
    int numPicks = 24;              // # of picks to show for Alliance Picks (actually get from Preferences)
    /*Shared Prefs-Scored Power-Cell*/ public String Cell_Dump  = ""; public String PowerCell_L0  = ""; public String PowerCell_L1  = ""; public String PowerCell_L2  = ""; public String PowerCell_L3  = ""; public String PowerCell_L4  = "";
                                    public String PowerCell_C0  = ""; public String PowerCell_C1  = ""; public String PowerCell_C2  = ""; public String PowerCell_C3  = ""; public String PowerCell_C4  = ""; public String PowerCell_C5  = "";
    /*Shared Prefs-C.P.*/ public String panel_L1 = ""; public String panel_L2  = "";
    /*Shared Prefs-Climbing*/  public String climbSG = ""; public String parkSG = ""; public String Balanced = ""; public String climbLift1 = ""; public String climbLift2 = "";  public String climbLifted = ""; public String climbHang0 = ""; public String climbHang1 = ""; public String climbHang2 = ""; public String climbHang3 = "";
    /*Weight factors*/ public String wtClimb = ""; public String wtPowerCell = ""; public String wtCP = "";
    ImageView imgStat_Load;
    TextView txt_EventName, txt_NumTeams, txt_Formula, lbl_Formula, txt_LoadStatus, txt_SelNum;
    Spinner spinner_numMatches;
    ListView lstView_Teams;
    TextView TeamData, BA, Stats, Stats2;
    Button btn_Match, btn_Pit, btn_Default;
    ProgressBar pbSpinner;
    RadioGroup radgrp_Sort;
    RadioButton radio_Climb, radio_PowerCell, radio_Weight, radio_Team, radio_pGt1, radio_cGt1, radio_ControlPanel;
    //    Button btn_Up, btn_Down, btn_Delete;
    public ArrayAdapter<String> adaptTeams;
    public static String[] numMatch = new String[]             // Num. of Matches to process
            {"ALL","Last","Last 2","Last 3","Last 4"};
    //    ArrayList<String> draftList = new ArrayList<String>();
    static final ArrayList<HashMap<String, String>> draftList = new ArrayList<HashMap<String, String>>();
    public int teamSelected = -1;
    public static String sortType = "";
    private ProgressDialog progress;
    String tNumb = "";
    String tn = "";
    String Viz_URL = "";
    String teamNum=""; String teamName = "";
    String tmRank = "";
    String tmRScore = "";
    String tmWLT = "";
    String tmOPR = "";
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();

    String SectLin = "";
    String Dumped = "";
    String autoCellLow = "";
    String autoCellUnder = "";
    String autoCellLine = "";
    String autoCellFrontCP = "";
    String AconUnderNum = "";
    String AconLineNum = "";
    String AconFrontNum = "";
    String telePowerCellL0 = "";
    String telePowerCellL1 = "";
    String telePowerCellL2 = "";
    String telePowerCellL3 = "";
    String telePowerCellL4 = "";
    String autoCollectFloor = "";
    String autoCollectRobot = "";
    String autoCollectCP = "";
    String autoCollectTrench = "";
    String autoCollectBoundary = "";
    String teleCollectFloor = "";
    String teleCollectRobot = "";
    String teleCollectCP = "";
    String teleCollectTrench = "";
    String teleCollectBoundary = "";
    String teleCollectLoadSta = "";
    String TconUnderNum = "";
    String TconLineNum = "";
    String TconFrontNum = "";
    String TconBackNum = "";
    String CPspinNum = "";
    String CPcolorNum = "";
    String climb = "";
    String park = "";
    String level = "";
    String climb_Hang0 = ""; String climb_Hang1 = ""; String climb_Hang2 = ""; String climb_Hang3 = "";
    String liftOne = "";
    String liftTwo = "";
    String gotLifted = "";
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
        Log.e(TAG, "B4 - " + sortType);
        if (savedInstanceState != null) {
            Log.e(TAG, "Are we ever getting called? " + is_Resumed);
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            String sortType = prefs.getString("Sort", "");
        } else {
    //            sortType = "Team#";
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
        pbSpinner =  (ProgressBar) findViewById(progressBar1);

        pfDatabase = FirebaseDatabase.getInstance();
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data

        RadioGroup radgrp_Sort = findViewById(R.id.radgrp_Sort);
        for(int i = 0; i < radgrp_Sort.getChildCount(); i++){        // turn them all OFF
           radgrp_Sort.getChildAt(i).setEnabled(false);
        }
        radgrp_Sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
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
                    case "Cell":
                        sortType = "Cell";
//                      Log.w(TAG, "Cube sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getSCORE_PowerCellScore(), c2.getSCORE_PowerCellScore());
                            }
                        });
                        Collections.reverse(team_Scores);   // Descending
                        showFormula("Cell");              // update the formula
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
                    case "C.P.":
                        sortType = "C.P.";
//                        Log.w(TAG, "C.P. sort");
                        Collections.sort(team_Scores, new Comparator<Scores>() {
                            @Override
                            public int compare(Scores c1, Scores c2) {
                                return Float.compare(c1.getSCORE_panelsScore(), c2.getSCORE_panelsScore());
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
                        txt_Formula.setText(" ");       // set formulat to blank
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
                txt_SelNum.setText(String.valueOf(pos+1));      // Sort Position
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

        Cell_Dump    = sharedPref.getString("prefPowerCell_Dump", "0.5");   // Dump
        PowerCell_L0 = sharedPref.getString("prefPowerCell_L0", "1.0");     // Low
        PowerCell_L1 = sharedPref.getString("prefPowerCell_L1", "2.0");     // Under
        PowerCell_L2 = sharedPref.getString("prefPowerCell_L2", "3.0");     // Line
        PowerCell_L3 = sharedPref.getString("prefPowerCell_L3", "3.0");     // CP Front
        PowerCell_L4 = sharedPref.getString("prefPowerCell_L4", "3.0");     // CP Back
        PowerCell_C0 = sharedPref.getString("prefPowerCell_C0", "1.0");     // Floor
        PowerCell_C1 = sharedPref.getString("prefPowerCell_C1", "2.0");     // Robot
        PowerCell_C2 = sharedPref.getString("prefPowerCell_C2", "1.5");     // C.P.
        PowerCell_C3 = sharedPref.getString("prefPowerCell_C3", "1.5");     // Trench
        PowerCell_C4 = sharedPref.getString("prefPowerCell_C4", "1.5");     // Boundary
        PowerCell_C5 = sharedPref.getString("prefPowerCell_C5", "1.0");     // LoadSta

        panel_L1 = sharedPref.getString("prefPanel_L1", "1.0");            // C.P. Spin
        panel_L2 = sharedPref.getString("prefPanel_L2", "2.0");            // C.P. Color

        climbSG     = sharedPref.getString("prefClimb_climb", "1.5");
        parkSG      = sharedPref.getString("prefClimb_park", "0.5");
        Balanced    = sharedPref.getString("prefClimb_Balanced", "2.5");
        climbLift1  = sharedPref.getString("prefClimb_lift1", "1.5");
        climbLift2  = sharedPref.getString("prefClimb_lift2", "2.0");
        climbLifted = sharedPref.getString("prefClimb_lifted", "0.3");
        climbHang0  = sharedPref.getString("prefClimb_Hang0", "-1.0");
        climbHang1  = sharedPref.getString("prefClimb_Hang1", "1.0");
        climbHang2  = sharedPref.getString("prefClimb_Hang2", "2.5");
        climbHang3  = sharedPref.getString("prefClimb_Hang3", "5.0");

        wtClimb  = sharedPref.getString("prefWeight_climb", "3.0");
        wtPowerCell  = sharedPref.getString("prefWeight_PowerCell", "2.0");
        wtCP = sharedPref.getString("prefWeight_panel", "1.0");

        numPicks = Integer.parseInt(sharedPref.getString("prefAlliance_num", "24"));


    }

    private String showFormula(String typ) {
        Log.i(TAG, "** showFormula **  " + typ);
        String form = "";
        getprefs();         // make sure Prefs are up to date
        switch (typ) {
            case "Climb":
                form = "(" + climbSG+ "*Climbed + " + parkSG +" *Parked" + Balanced + "*Balanced) +((" + climbHang0 + "*Hang0) + " + climbHang1 + "*Hang1) + (" + climbHang2 + "*Hang2) + (" + climbHang3 + "*Hang3)) ✚ " +"((Lift1*" + climbLift1 + ") + " +"(Lift2*" + climbLift2 + ") + (WasLifted*" + climbLifted + ")) / # matches";
                lbl_Formula.setTextColor(Color.parseColor("#4169e1"));      // blue
                txt_Formula.setText(form);
                break;
            case "Cell":
                form = "((" + Cell_Dump +"* Dump) + (" + PowerCell_L0 +"* Low) + (" + PowerCell_L1 +"* cellUnder) + (" + PowerCell_L2 + "* cellLine) + (" + PowerCell_L3 + "* cellFrontCP)) /# matches";
                lbl_Formula.setTextColor(Color.parseColor("#ee00ee"));      // magenta
                txt_Formula.setText(form);
                break;
            case "C.P.":
                form = "((" + panel_L1 +"* spinCP) + (" + panel_L2 + "* colorCP)) " + "/# matches";
                lbl_Formula.setTextColor(Color.parseColor("#00ff00"));      /// green
                txt_Formula.setText(form);
                break;
            case "Combined":
                form = "((" + wtClimb + "*climbScore) + (" + wtPowerCell + "*PowerCellScore) + (" + wtCP + "*panelScore)) / #matches";
                lbl_Formula.setTextColor(Color.parseColor("#ff0000"));      // red
                txt_Formula.setText(form);
                break;
            default:                //
                Log.e(TAG, "*** Invalid Type " + typ);
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
        public void onNothingSelected (AdapterView < ? > parent){
            // Do nothing.
        }
    }

public void Toast_Msg(String choice, Integer minimum) {
    Toast toast = Toast.makeText(getBaseContext(), "\nCannot show '" + choice + "' some teams have " + minimum + " matches \n " , Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();

}



    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonDefault_Click(View view) {
        // Reload _ALL_ the Preference defaults
        Log.i(TAG, ">>>>> buttonDefault_Click" );
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
        String teamHash="";
        final String[] URL = {""};  Viz_URL = "";
        FirebaseStorage storage = FirebaseStorage.getInstance();

        if (teamSelected >= 0) {
            draftList.get(teamSelected);
            temp = draftList.get(teamSelected);
            teamHash = temp.get("team");
            teamNum = teamHash.substring(0, 4);
            teamName = teamHash.substring(5, teamHash.indexOf("("));  // UP TO # MATCHES
            Log.e(TAG, "****** team  '" + teamName + "' ");
//            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://pearadox-2020.appspot.com/images/" + Pearadox.FRC_Event).child("robot_" + teamNum.trim() + ".png");
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
        Log.d(TAG,">>>>>>>>  launchVizPit " + team + " " + name + " " + imgURL);      // ** DEBUG **
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
                Log.i(TAG, "***** Matches Loaded. # = "  + Pearadox.Matches_Data.size());
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
                new String[] {"team","BA","Stats","Stats2"},
                new int[] {R.id.TeamData,R.id.BA, R.id.Stats, R.id.Stats2}
        );

        draftList.clear();
        String totalScore="";
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
                case "Cell":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_PowerCellScore()) + "]";
                    break;
                case "Combined":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_combinedScore()) + "]";
                    break;
                case "C.P.":
                    totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_panelsScore()) + "]";
                    break;
                case "Team#":
                    totalScore=" ";
                    break;
                default:                //
                    Log.e(TAG, "Invalid Sort - " + sortType);
            }

            temp.put("team", tn + "-" + score_inst.getTeamName() + "  (" + mdNumMatches + ")  " +  totalScore);
//            temp.put("BA", "Rank=" + teams[i].rank + "  " + teams[i].record + "   OPR=" + String.format("%3.1f", (teams[i].opr)) + "    ↑ " + String.format("%3.1f", (teams[i].touchpad)) + "   kPa=" + String.format("%3.1f", (teams[i].pressure)));
            temp.put("BA","Rank=" + tmRank + "   Score=" + tmRScore + "   WLT=" + tmWLT + "   OPR=" + tmOPR);
                    temp.put("Stats", "Auto ≠"  + SectLin + " ▼" + Dumped + "   ◯◉ " + autoCellLow + " U" + autoCellUnder + " L" + autoCellLine + " F" + autoCellFrontCP + " ✿ U" + AconUnderNum + " L"  + AconLineNum + " F"  + AconFrontNum + "   ◯↑ F" + autoCollectFloor + " R" + autoCollectRobot + " C" + autoCollectCP + " T" + autoCollectTrench + " B" + autoCollectBoundary  + "\nTele ◯◉ " + telePowerCellL0 + " U" + telePowerCellL1 + " L" + telePowerCellL2 + "  F" + telePowerCellL3 + "  B" + telePowerCellL4 + " ✿ U" + TconUnderNum + " L"  + TconLineNum + " F"  + TconFrontNum + " B"  + TconBackNum + "   ◯↑ F" + teleCollectFloor + " R" + teleCollectRobot + " C" + teleCollectCP + " T" + teleCollectTrench + " B" + teleCollectBoundary + " L" + teleCollectLoadSta);
            temp.put("Stats2",  "☢ ¹" + CPspinNum + " ²"  + CPcolorNum + "  Climb ♺" + climb + " 円" + park  + " ⚖" + level + "   Hangs ₀" + climb_Hang0 + " ₁" + climb_Hang1 + " ₂" + climb_Hang2 + " ₃" + climb_Hang3 + "    ↕One " + liftOne + "  ↕Two " + liftTwo + "    Was↑ " + gotLifted);
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
            startActivity(help_intent);  	// Show Help
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
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-Draft"  + "_" + sortType + ".JPG";
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

        String tName = ""; String totalScore=""; String DS = "";
        String underScore = new String(new char[30]).replace("\0", "_");    // string of 'x' underscores
        String blanks = new String(new char[50]).replace("\0", " ");        // string of 'x' blanks
        String pound = new String(new char[50]).replace("\0", "#");        // string of 'x' Pound
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
        if (numPicks > 24) {
            DS = "";                    // Use Single Space
        }else {
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
            bW.write(Pearadox.FRC_ChampDiv + "-" + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  COMBINED  " + underScore +  "\n" + DS);
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
                Log.e(TAG, ">>>>  teamName '" + tName + "' ");
                totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_combinedScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i+1) +") " + tNumb + "-" + tName + "\t  (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ")   " +  totalScore + " \t");
                bW.write( "\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n" + (char)12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  Cell  " + underScore +  "\n \n");
            //  Switch sort
            sortType = "Cell";
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
                bW.write(String.format("%2d", i+1) + ") " + tNumb + "-" + tName  + "\t (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ") " +  totalScore + "\t");
                bW.write("TcellBackCPAuto ₁" + autoCellUnder + " ₂" + autoCellLine + " ₃" + autoCellFrontCP + "  Tele ₁" + telePowerCellL1 + " ₂" + telePowerCellL2+ " ₃" + telePowerCellL3 + "\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n" + (char)12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  PANELS  " + underScore +  "\n" + DS);
            //  C.P. sort
            sortType = "C.P.";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getSCORE_panelsScore(), c2.getSCORE_panelsScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_panelsScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i+1) +") " + tNumb + "-" + tName + "\t  (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ")  " +  totalScore);
//                bW.write( "☢ Auto  ₁" + sandPanelL1 + " ₂" + sandPanelL2 + " ₃" + sandPanelL3 + "  Tele  ₁" + telePanL1 + " ₂" + telePanL2 + " ₃" + telePanL3 + "  ▼ " + panDropped + "\n" + DS);
            } // end For # teams
            bW.write(" \n" + "\n" + (char)12);        // NL & FF
            //=====================================================================

            bW.write(Pearadox.FRC_ChampDiv + " - " + Pearadox.FRC_EventName +  "\n");
            bW.write(underScore + "  CLIMB  " + underScore +  "\n \n");
            //  Scale sort
            sortType = "Climb";
            Collections.sort(team_Scores, new Comparator<Scores>() {
                @Override
                public int compare(Scores c1, Scores c2) {
                    return Float.compare(c1.getSCORE_climbScore(), c2.getSCORE_climbScore());
                }
            });
            Collections.reverse(team_Scores);   // Descending
            loadTeams();
            for (int i = 0; i < numPicks; i++) {    // load by sorted scores
                score_inst = team_Scores.get(i);
                tNumb = score_inst.getTeamNum();
                tName = score_inst.getTeamName();
                tName = tName + blanks.substring(0, (36 - tName.length()));
                totalScore = "[" + String.format("%3.2f", score_inst.getSCORE_climbScore()) + "]";
                teamData(tNumb);   // Get Team's Match Data
                bW.write(String.format("%2d", i+1) +") " + tNumb + " - " + tName + "\t  (" + String.format("%2d",(Integer.parseInt(mdNumMatches))) + ") " +  totalScore + " \t");
                bW.write(" HAB ₀" + climb_Hang0 + " ₁" + climb_Hang1 + " ₂" + climb_Hang2  + " ₃" + climb_Hang3 + "\n" + DS);
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
        pickList();     // generate Picklist for app
    }

    private void pickList() {
        Log.w(TAG, "$$$$  pickList  $$$$ ");
// ======================================================================================
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
                bW.write(temp +  "\n");

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
        int base = 0; int dump = 0; int spinCP = 0; int colorCP = 0;
        int colFloor = 0; int colRobot = 0; int colCP = 0; int colTrench = 0; int colBoundary = 0;
        int TcolFloor = 0; int TcolRobot = 0; int TcolCP = 0; int TcolTrench = 0; int TcolBoundary = 0; int TcolLoadSta = 0;
        int cellL0 = 0;int cellUnder = 0; int cellLine = 0; int cellFrontCP =0; int TcellL0 = 0; int TcellUnder = 0; int TcellLine = 0; int TcellFrontCP = 0; int TcellBackCP = 0; int TpanL1 = 0; int TpanL2 = 0; int TpanL3 = 0;
        int climbed=0,parked=0, bal=0, climbH0= 0; int climbH1 = 0; int climbH2 = 0; int climbH3 = 0; int lift1Num = 0; int lift2Num = 0; int liftedNum = 0;
        int conUnder = 0; int conLine = 0; int conFront = 0;   int TconUnder = 0; int TconLine = 0; int TconFront = 0; int TconBack = 0;
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
                if (match_inst.isAuto_leftSectorLine()) {
                    base++;
                }
                if (match_inst.isAuto_Dump()) {
                    dump++;
                }
                cellL0 = cellL0 + match_inst.getAuto_Low();
                cellUnder = cellUnder + match_inst.getAuto_HighClose();
                cellLine = cellLine + match_inst.getAuto_HighLine();
                cellFrontCP = cellFrontCP + match_inst.getAuto_HighFrontCP();
                if (match_inst.isAuto_conInnerClose()) {
                    conUnder++;
                }
                if (match_inst.isAuto_conInnerLine()) {
                    conLine++;
                }
                if (match_inst.isAuto_conInnerFrontCP()) {
                    conFront++;
                }

                if (match_inst.isAuto_CollectFloor()) {         //**  Collect Auto
                    colFloor++;                                 //**
                }                                               //**
                if (match_inst.isAuto_CollectRobot()) {         //**
                    colRobot++;                                 //**
                }                                               //**
                if (match_inst.isAuto_CollectCP()) {            //**
                    colCP++;                                    //**
                }                                               //**
                if (match_inst.isAuto_CollectTrench()) {        //**
                    colTrench++;                                //**
                }                                               //**
                if (match_inst.isAuto_CollectSGboundary()) {    //**
                    colBoundary++;                              //**
                }                                               //*********

                // *************************************************
                // ******************** TeleOps ********************
                // *************************************************
                TcellL0 = TcellL0 + match_inst.getTele_Low();
                TcellUnder = TcellUnder + match_inst.getTele_HighClose();
                TcellLine = TcellLine + match_inst.getTele_HighLine();
                TcellFrontCP = TcellFrontCP + match_inst.getTele_HighFrontCP();
                TcellBackCP = TcellBackCP + match_inst.getTele_HighBackCP();
                if (match_inst.isTele_conInnerClose()) {
                    TconUnder++;
                }
                if (match_inst.isTele_conInnerLine()) {
                    TconLine++;
                }
                if (match_inst.isTele_conInnerFrontCP()) {
                    TconFront++;
                }
                if (match_inst.isTele_conInnerBackCP()) {
                    TconBack++;
                }

                if (match_inst.isTele_PowerCell_floor()) {      //**  Collect Tele
                    TcolFloor++;                                //**
                }                                               //**
                if (match_inst.isTele_PowerCell_Robot()) {      //**
                    TcolRobot++;                                //**
                }                                               //**
                if (match_inst.isTele_PowerCell_CP()) {         //**
                    TcolCP++;                                   //**
                }                                               //**
                if (match_inst.isTele_PowerCell_Trench()) {     //**
                    TcolTrench++;                               //**
                }                                               //**
                if (match_inst.isTele_PowerCell_Boundary()) {   //**
                    TcolBoundary++;                             //**
                }                                               //**
                if (match_inst.isTele_PowerCell_LoadSta()) {    //**
                    TcolLoadSta++;                              //**
                }                                               //*********

                if (match_inst.isTele_CPspin()) {
                    spinCP++;
                }
                if (match_inst.isTele_CPcolor()) {
                    colorCP++;
                }

                if (match_inst.isTele_Climbed()) {
                    climbed++;
                }
                if (match_inst.isTele_UnderSG()) {
                    parked++;
                }
                if (match_inst.isTele_Balanced()) {
                    bal++;
                }

                int endHang = match_inst.getTele_Hang_num();        // end # Hanging
                switch (endHang) {
                    case 0:         // Zero
                        climbH0++;
                        break;
                    case 1:         // Level 1
                        climbH1++;
                        break;
                    case 2:         // Level 2
                        climbH2++;
                        break;
                    case 3:         // Level 3
                        climbH3++;
                        break;
                    default:                // ????
                        e(TAG, "*** Error - bad Hang Level indicator  ***");
                }

                if (match_inst.isTele_lifted()) {
                    lift1Num++;
                }
// ToDo - Lift 2
                //                if (match_inst.isTele_lift_two()) {
//                    lift2Num++;
//                }
                if (match_inst.isTele_got_lift()) {
                    liftedNum++;
                }
//                Log.w(TAG, "Accum. matches = " + numMatches);
            } //End if teams equal
        } // End For _ALL_ matches

//        Log.w(TAG, "####### Total Matches/Team = " + numMatches);
        mdNumMatches = String.valueOf(numMatches);
        if (numMatches < minMatches) {
            minMatches = numMatches;
            Log.e(TAG, team + " >>>>>>>>>>  Min. matches changed = " + minMatches);
        }
        if (numMatches > 0) {
            SectLin = String.valueOf(base);
            Dumped = String.valueOf(dump); 
            autoCellLow = String.valueOf(cellL0);
            autoCellUnder = String.valueOf(cellUnder);
            autoCellLine = String.valueOf(cellLine);
            autoCellFrontCP = String.valueOf(cellFrontCP);
            AconUnderNum = String.valueOf(conUnder);
            AconLineNum = String.valueOf(conLine);
            AconFrontNum = String.valueOf(conFront);
            TconUnderNum = String.valueOf(TconUnder);
            TconLineNum = String.valueOf(TconLine);
            TconFrontNum = String.valueOf(TconFront);
            TconBackNum = String.valueOf(TconBack);
            telePowerCellL0 = String.valueOf(TcellL0);
            telePowerCellL1 = String.valueOf(TcellUnder);
            telePowerCellL2 = String.valueOf(TcellLine);
            telePowerCellL3 = String.valueOf(TcellFrontCP);
            telePowerCellL4 = String.valueOf(TcellBackCP);
            autoCollectFloor = String.valueOf(colFloor);
            autoCollectRobot = String.valueOf(colRobot);
            autoCollectCP = String.valueOf(colCP);
            autoCollectTrench = String.valueOf(colTrench);
            autoCollectBoundary = String.valueOf(colBoundary);
            teleCollectFloor = String.valueOf(TcolFloor);
            teleCollectRobot = String.valueOf(TcolRobot);
            teleCollectCP = String.valueOf(TcolCP);
            teleCollectTrench = String.valueOf(TcolTrench);
            teleCollectBoundary = String.valueOf(TcolBoundary);
            teleCollectLoadSta = String.valueOf(TcolLoadSta);
            CPspinNum = String.valueOf(spinCP);
            CPcolorNum = String.valueOf(colorCP);
            climb = String.valueOf(climbed);
            park = String.valueOf(parked);
            level = String.valueOf(bal);
            climb_Hang0 = String.valueOf(climbH0);
            climb_Hang1 = String.valueOf(climbH1);
            climb_Hang2 = String.valueOf(climbH2);
            climb_Hang3 = String.valueOf(climbH3);
            liftOne = String.valueOf(lift1Num);
            liftTwo = String.valueOf(lift2Num);
            gotLifted = String.valueOf(liftedNum);
        } else {
            SectLin  = "0";
            Dumped = "0";
            autoCellLow = "0";
            autoCellUnder = "0";
            autoCellLine = "0";
            autoCellFrontCP = "0";
            AconUnderNum = "0";
            AconLineNum = "0";
            AconFrontNum = "0";
            TconUnderNum = "0";
            TconLineNum = "0";
            TconFrontNum = "0";
            TconBackNum = "0";
            telePowerCellL0 = "0";
            telePowerCellL1 = "0";
            telePowerCellL2 = "0";
            telePowerCellL3 = "0";
            telePowerCellL4 = "0";
            autoCollectFloor = "0";
            autoCollectRobot = "0";
            autoCollectCP = "0";
            autoCollectTrench = "0";
            autoCollectBoundary = "0";
            teleCollectFloor = "0";
            teleCollectRobot = "0";
            teleCollectCP = "0";
            teleCollectTrench = "0";
            teleCollectBoundary = "0";
            teleCollectLoadSta = "0";
            CPspinNum = "0";
            CPcolorNum = "0";
            climb = "0";
            park = "0";
            level = "0";
            climb_Hang0 = "0"; climb_Hang1 = "0"; climb_Hang2 = "0"; climb_Hang3 = "0";
            liftOne = "0";
            liftTwo = "0";
            gotLifted = "0";
        }
        //============================
        float climbScore = 0; float cellScored = 0; float cellCollect = 0; float PowerCellScore = 0; float combinedScore = 0; float ControlPanelScore = 0;
//        Log.e(TAG, team + " "+ climbs + " "+ lift1Num + " "+ lift2Num + " " + platNum +  " " + liftedNum + " / " + numMatches);
        if (numMatches > 0) {
            climbScore = (float) ((climbed * Float.parseFloat(climbSG) + (parked * Float.parseFloat(parkSG)) + (bal * Float.parseFloat(Balanced))) + ((climbH0 * Float.parseFloat(climbHang0)) + (climbH1 * Float.parseFloat(climbHang1)) + (climbH2 * Float.parseFloat(climbHang2)) + (climbH3 * Float.parseFloat(climbHang3)) + (lift1Num * Float.parseFloat(climbLift1)) + (lift2Num * Float.parseFloat(climbLift2)) + (liftedNum * Float.parseFloat(climbLifted))) / numMatches);

            cellScored = ((dump * Float.parseFloat(Cell_Dump)) + ((cellL0+TcellL0) * Float.parseFloat(PowerCell_L0)) + ((cellUnder+TcellUnder) * Float.parseFloat(PowerCell_L1)) + ((cellLine+TcellLine) * Float.parseFloat(PowerCell_L2)) + ((cellFrontCP+TcellFrontCP) * Float.parseFloat(PowerCell_L3)) + ((TcellBackCP) * Float.parseFloat(PowerCell_L4)))  / numMatches;
            cellCollect = ( (colFloor * Float.parseFloat(PowerCell_C0))  + (colRobot * Float.parseFloat(PowerCell_C1))  + (colCP * Float.parseFloat(PowerCell_C2)) + (colTrench * Float.parseFloat(PowerCell_C3)) + (colBoundary * Float.parseFloat(PowerCell_C4)) + (TcolLoadSta * Float.parseFloat(PowerCell_C4)));
            PowerCellScore = cellScored + cellCollect;

            ControlPanelScore = (float) (((spinCP * 1.0) + (colorCP * 2.0) ) / numMatches);

            combinedScore = (((climbScore * Float.parseFloat(wtClimb) + (PowerCellScore * Float.parseFloat(wtPowerCell)) + (ControlPanelScore * Float.parseFloat(wtCP)))) / numMatches);
        } else {
            PowerCellScore = 0;
            ControlPanelScore = 0;
            climbScore = 0;
            combinedScore = 0;
        }
        String tNumber="";
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
        }
    }

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
                radio_Team = findViewById(R.id.radio_Team);
                radio_Team.performClick();         // "force" radio button click  (so it works 1st time)
                for(int i = 0; i < radgrp_Sort.getChildCount(); i++){        // turn them all ON
                    radgrp_Sort.getChildAt(i).setEnabled(true);
                }
                btn_Match.setEnabled(true);
                btn_Match.setVisibility(View.VISIBLE);
                btn_Pit.setEnabled(true);
                btn_Pit.setVisibility(View.VISIBLE);
                pbSpinner.setVisibility(View.INVISIBLE);
//                setProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });

        // ======================================================================================
        sortType = "Team#";          // Attempt to "force" correct sort 1st time
        Collections.sort(team_Scores, Scores.teamComp);
        loadTeams();    // load for 1st time in Team# order
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
            curScrTeam.setSCORE_PowerCellScore((float) 0);        // Cell
            curScrTeam.setSCORE_combinedScore((float) 0);     // Combined
            curScrTeam.setSCORE_panelsScore((float) 0);       // C.P.
            team_Scores.add(i, curScrTeam);
        } // end For
        Log.w(TAG, "#Scores = " + team_Scores.size());
        if (sortType.matches("") || sortType.matches("Team#")) {       // if 1st time
            sortType = "Team#";
        } else {
//            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//            String sortType = prefs.getString("Sort", "");
//
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
                case "Cell":
                    radio_PowerCell = findViewById(R.id.radio_PowerCell);
                    radio_PowerCell.performClick();         // "force" radio button click
                    break;
                case "Combined":
                    radio_Weight = findViewById(R.id.radio_Weight);
                    radio_Weight.performClick();         // "force" radio button click
                    break;
                case "C.P.":
                    radio_ControlPanel = findViewById(R.id.radio_ControlPanel);
                    radio_ControlPanel.performClick();         // "force" radio button click
                    break;
                default:                //
                    Log.e(TAG, "*** Invalid Type " + sortType);
            }
        }
    }

//###################################################################
//###################################################################
//###################################################################
@Override
public void onStart() {
    super.onStart();
    Log.v(TAG, "onStart");
    sortType = "Team#";     // Make 'Team#' the default
    initScores();
    addMD_VE_Listener(pfMatchData_DBReference);        // Load _ALL_ Matches
    }


@Override
public void onResume() {
    super.onResume();
    Log.v(TAG, "****> onResume <**** " + sortType);
    is_Resumed = true;
    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    String sortType = prefs.getString("Sort", "");
    Log.d(TAG, "Resume Prefs >> " + sortType);
    initScores();           // make sure it sorts by _LAST_ radio button
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause  "  + sortType);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Sort", sortType);
        editor.commit();        // keep sort type
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

//