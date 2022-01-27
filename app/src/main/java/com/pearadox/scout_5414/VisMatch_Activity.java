package com.pearadox.scout_5414;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.ToneGenerator;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.io.File;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;

import static android.util.Log.e;

public class VisMatch_Activity extends AppCompatActivity {
    String TAG = "VisMatch_Activity";        // This CLASS name
    String tnum = "";
    String tname = "";
    int start = 0;          // Start Position for matches (0=ALL)
    int numObjects = 0; int numProcessed = 0;
    Spinner spinner_numMatches;
    String underScore = new String(new char[60]).replace("\0", "_");  // string of 'x' underscores
    String matches = "";  String match_id = "";
    TextView txt_team, txt_teamName, txt_NumMatches, txt_Matches;
    TextView txt_auto_leftSectorLine, txt_StartingBalls, txt_noAuton, txt_Ss_PowerCellScored;
    TextView txt_Tele_PowerCellScored;
    TextView txt_HabLvl, txt_Lift1NUM, txt_Lift2NUM, txt_WasLiftedNUM;
    TextView txt_AutonFloor, txt_AutonTrench, txt_AutonControlP, txt_AutonBoundary , txt_AutonURobot;
    TextView txt_TeleFloor, txt_TelePlaSta, txt_TeleTrench, txt_TeleCP, txt_TeleBoundary, txt_TeleRobot;
    TextView txt_TeleCPSpin, txt_TeleCPColor;
    TextView txt_TeleClimb, txt_TelePark, txt_TeleBalance;
    // ToDo - TextViews for PowerCell/Panels 2nd & 3rd
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    TextView txt_StartPositions;
    TextView txt_Pos;
    public static String[] numMatch = new String[]             // Num. of Matches to process
            {"ALL","Last","Last 2","Last 3","Last 4","Last 5"};
    StackedBarChart teleBarChart; StackedBarChart autonBarChart;
    //----------------------------------
    int numleftSectorLine = 0; int numleftSectorLine2 = 0; int noAuton = 0; int precellsCarried = 0; int precell_0 = 0; int precell_1 = 0; int precell_2 = 0; int precell_3 = 0;
    int auto_B1 = 0; int auto_B2 = 0; int auto_B3 = 0; int auto_B4 = 0; int auto_B5 = 0; int auto_B6 = 0;
    // NOTE: _ALL_ external mentions of Playere Sta. (PS) were changed to Loading Sta. (LS) so as to NOT be confused with Player Control Sta. (Driver)
    int auto_Ps1 = 0; int auto_Ps2 = 0; int auto_Ps3 = 0;
    int auton_Floor= 0; int auton_Trench = 0; int auton_ControlP = 0; int auton_Boundary = 0; int auton_Robot = 0;
    int tele_PowerCellFloor = 0; int tele_PowerCellPlasta = 0; int tele_PowerCellTrench = 0; int tele_PowerCellCP = 0; int tele_PowerCellBoundary = 0; int tele_PowerCellRobot = 0;
    int tele_CPSpin = 0; int tele_CPColor = 0;
    int tele_Climb = 0; int tele_Park = 0; int tele_Balance = 0;
    int climbH0= 0; int climbH1 = 0; int climbH2 = 0; int climbH3 = 0; int lift1Num = 0; int liftedNum = 0;
    int auto_Low = 0; int auto_HighClose = 0; int auto_HighLine = 0; int auto_HighFrontCP =0;
    int Tele_Low = 0; int Tele_HighClose = 0; int Tele_HighLine = 0; int Tele_HighFrontCP = 0;  int Tele_HighBackCP = 0;
    int TpanL1 = 0; int TpanL2 = 0; int TpanL3 = 0;
    int numMatches = 0; int panL1 = 0; int panL2 = 0; int panL3 = 0;
    String auto_Comments = "";
    //----------------------------------
    int numTeleClimbSuccess = 0; int LiftNm = 0; int WasLifted = 0;
    String tele_Comments = "";
    //----------------------------------
    int final_LostComm = 0; int final_LostParts = 0; int final_DefGood = 0; int final_TrenchInt = 0;  int final_DefDump = 0; int final_NumPen = 0;
    TextView txt_final_LostComm, txt_final_LostParts, txt_final_DefGood, txt_final_TrenchInt, txt_final_DefDump, txt_final_NumPen;
    String final_Comments = "";
    //----------------------------------

    matchData match_inst = new matchData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vis_match);
        Log.i(TAG, "@@@@  VisMatch_Activity started  @@@@");
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("team");
        String param2 = bundle.getString("name");
//        Log.w(TAG, param1);      // ** DEBUG **
        tnum = param1;      // Save Team #
//        Log.w(TAG, param2);      // ** DEBUG **
        tname = param2;      // Save Team #


        txt_Matches = (TextView) findViewById(R.id.txt_Matches);
        txt_team = (TextView) findViewById(R.id.txt_team);
        txt_teamName = (TextView) findViewById(R.id.txt_teamName);
        txt_NumMatches = (TextView) findViewById(R.id.txt_NumMatches);
        Spinner spinner_numMatches = (Spinner) findViewById(R.id.spinner_numMatches);
        ArrayAdapter adapter_Matches = new ArrayAdapter<String>(this, R.layout.robonum_list_layout, numMatch);
        adapter_Matches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_numMatches.setAdapter(adapter_Matches);
        spinner_numMatches.setSelection(0, false);
        spinner_numMatches.setOnItemSelectedListener(new numMatches_OnItemSelectedListener());
        /*  Auto  */
        txt_auto_leftSectorLine = (TextView) findViewById(R.id.txt_auto_leftSectorLine);
//        txt_Ss_leftSectorLine2 = (TextView) findViewById(R.id.txt_Ss_leftSectorLine2);
        txt_HabLvl = (TextView) findViewById(R.id.txt_HabLvl);
        txt_noAuton = (TextView) findViewById(R.id.txt_noAuton);
        txt_StartingBalls = (TextView) findViewById(R.id.txt_StartingBalls);
        txt_Ss_PowerCellScored = (TextView) findViewById(R.id.txt_Ss_PowerCellScored);
        txt_AutonFloor = (TextView) findViewById(R.id.txt_AutonFloor);
        txt_AutonTrench = (TextView) findViewById(R.id.txt_AutonTrench);
        txt_AutonControlP = (TextView) findViewById(R.id.txt_AutonControlP);
        txt_AutonBoundary = (TextView) findViewById(R.id.txt_AutonBoundary);
        txt_AutonURobot = (TextView) findViewById(R.id.txt_AutonURobot);
        txt_TeleFloor = (TextView) findViewById(R.id.txt_TeleFloor);
        txt_TelePlaSta = (TextView) findViewById(R.id.txt_TelePlaSta);
        txt_TeleTrench = (TextView) findViewById(R.id.txt_TeleTrench);
        txt_TeleCP = (TextView) findViewById(R.id.txt_TeleCP);
        txt_TeleBoundary = (TextView) findViewById(R.id.txt_TeleBoundary);
        txt_TeleRobot = (TextView) findViewById(R.id.txt_TeleRobot);

        txt_TeleCPSpin = (TextView) findViewById(R.id.txt_TeleCPSpin);
        txt_TeleCPColor = (TextView) findViewById(R.id.txt_TeleCPColor);
        txt_TeleClimb = (TextView) findViewById(R.id.txt_TeleClimb);
        txt_TelePark = (TextView) findViewById(R.id.txt_TelePark);
        txt_TeleBalance = (TextView) findViewById(R.id.txt_TeleBalanced);
        txt_StartPositions = (TextView) findViewById(R.id.txt_StartPositions);
        txt_Pos = (TextView) findViewById(R.id.txt_Pos);
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_AutoComments.setTextSize(12);       // normal
        txt_AutoComments.setMovementMethod(new ScrollingMovementMethod());
        /*  Tele  */
        txt_Tele_PowerCellScored = (TextView) findViewById(R.id.txt_Tele_PowerCellScored);
        txt_Lift1NUM = (TextView) findViewById(R.id.txt_Lift1NUM);
        txt_Lift2NUM = (TextView) findViewById(R.id.txt_Lift2NUM);
        txt_WasLiftedNUM = (TextView) findViewById(R.id.txt_WasLiftedNUM);
        teleBarChart = (StackedBarChart) findViewById(R.id.teleBarChart);
        autonBarChart = (StackedBarChart) findViewById(R.id.autonBarChart);
        txt_TeleComments = (TextView) findViewById(R.id.txt_TeleComments);
        txt_TeleComments.setMovementMethod(new ScrollingMovementMethod());

        /*  Final  */
        txt_FinalComments = (TextView) findViewById(R.id.txt_FinalComments);
        txt_FinalComments.setMovementMethod(new ScrollingMovementMethod());

        txt_final_LostComm = (TextView) findViewById(R.id.txt_final_LostComm);
        txt_final_LostParts = (TextView) findViewById(R.id.txt_final_LostParts);
        txt_final_DefGood = (TextView) findViewById(R.id.txt_final_DefGood);
        txt_final_TrenchInt = (TextView) findViewById(R.id.txt_final_TrenchInt);
        txt_final_DefDump = (TextView) findViewById(R.id.txt_final_DefDump);
        txt_final_NumPen = (TextView) findViewById(R.id.txt_final_NumPen);

        txt_team.setText(tnum);
        txt_teamName.setText(tname);    // Get real

        numObjects = Pearadox.Matches_Data.size();
//        Log.w(TAG, "Objects = " + numObjects);
        txt_NumMatches.setText(String.valueOf(numObjects));

        init_Values();
        numProcessed = numObjects;
        start = 0;
        getMatch_Data();
    }
    // ================================================================
    private void getMatch_Data() {
        for (int i = start; i < numObjects; i++) {
//            Log.w(TAG, "In for loop!   " + i);
            match_inst = Pearadox.Matches_Data.get(i);      // Get instance of Match Data
            match_id = match_inst.getMatch();
            matches = matches + match_inst.getMatch() + "  ";   // cumulative list of matches

            if (match_inst.isAuto_mode()) {
                noAuton++;
            }
            if (match_inst.isAuto_leftTarmac()) {
                numleftSectorLine++;
            }

            if (match_inst.getAuto_comment().length() > 1) {
                auto_Comments = auto_Comments + match_inst.getMatch() + "-" + match_inst.getAuto_comment() + "\n" + underScore  + "\n" ;
            }

            // Pre-Start
            String pos = match_inst.getPre_startPos().trim();
//            Log.w(TAG, "Start Pos. " + pos);
            // ToDo - new Start Pos
            switch (pos) {
                case "Left":
                    auto_B1++;
                    break;
                case ("Left Middle"):
                    auto_B2++;
                    break;
                case "Middle":
                    auto_B3++;
                    break;
                case "Right Middle":
                    auto_B4++;
                    break;
                case "Right":
                    auto_B5++;
                    break;
                case "No Show":
                    auto_B6++;
                    break;
                default:                //
                    Log.e(TAG, "***  Invalid Start Position!!!  ***" );
            }

            int PlayerStat = match_inst.getPre_PlayerSta();
//            Log.w(TAG, "Player Station. " + PlayerStat);
            switch (PlayerStat) {
                case 1:
                    auto_Ps1++;
                    break;
                case 2:
                    auto_Ps2++;
                    break;
                case 3:
                    auto_Ps3++;
                    break;
                default:                //
                    Log.e(TAG, "***  Invalid Player Station!!!  ***" );
            }

            int BallStart = match_inst.getPre_cargo_carried();
//            Log.w(TAG, "Pre-Cells." + BallStart);
            switch (BallStart) {
                case 0:
                    precell_0++;
                case 1:
                    precell_1++;
                case 2:
                    precell_2++;
                case 3:
                    precell_3++;
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            //@@@@@@@@@@@@@@@@@@@@@@  Autonomous  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            if (match_inst.isAuto_CollectFloor()) {
                auton_Floor++;
            }



            auto_Low = auto_Low + match_inst.getAuto_Low();

            AutoStackBar(i+1, match_id, (float)auto_Low , (float)auto_HighClose, (float)auto_HighLine, (float)auto_HighFrontCP);

            // *************************************************
            // ******************** TeleOps ********************
            // *************************************************
            if (match_inst.isTele_Cargo_floor()){
                tele_PowerCellFloor++;
            }
            if (match_inst.isTele_Cargo_term()) {
                tele_PowerCellPlasta++;
            }

            if (match_inst.isTele_Climbed()) {
                tele_Climb++;
            }


            Tele_Low = Tele_Low + match_inst.getTele_Low();
            Tele_HighClose = Tele_HighClose + match_inst.getTele_High();

            TeleStackBar(i+1, match_id, (float)Tele_Low , (float)Tele_HighClose);

            if (match_inst.getTele_comment().length() > 1) {
                tele_Comments = tele_Comments + match_inst.getMatch() + "-" + match_inst.getTele_comment() + "\n" + underScore  + "\n" ;
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
            // Final elements
            if (match_inst.isFinal_lostComms()) {
                final_LostComm++;
            }
            if (match_inst.isFinal_lostParts()) {
                final_LostParts++;
            }
            if (match_inst.isFinal_defense_good()) {
                final_DefGood++;
            }
            // Todo Rocket Int.
//            if (match_inst.isFinal_def_TrenchInt()) {
//                final_TrenchInt++;
//            }
            if (match_inst.isFinal_def_Block()) {
                final_DefDump++;
            }
            if (match_inst.getTele_num_Penalties() > 0) {
                final_NumPen = final_NumPen + match_inst.getTele_num_Penalties();
            }

//            Log.w(TAG, "Final Comment = " + match_inst.getFinal_comment() + "  " + match_inst.getFinal_comment().length());
            if (match_inst.getFinal_comment().length() > 1) {
                final_Comments = final_Comments + match_inst.getMatch() + "-" + match_inst.getFinal_comment() + "\n" + underScore  + "\n" ;
            }
        } //end For


// ================================================================
// ======  Now start displaying all the data we collected  ========
// ================================================================
        txt_auto_leftSectorLine = (TextView) findViewById(R.id.txt_auto_leftSectorLine);
        txt_HabLvl = (TextView) findViewById(R.id.txt_HabLvl);
        txt_noAuton = (TextView) findViewById(R.id.txt_noAuton);
        txt_StartingBalls = (TextView) findViewById(R.id.txt_StartingBalls);
        txt_Ss_PowerCellScored = (TextView) findViewById(R.id.txt_Ss_PowerCellScored);
        txt_Tele_PowerCellScored = (TextView) findViewById(R.id.txt_Tele_PowerCellScored);
        txt_AutonFloor = (TextView) findViewById(R.id.txt_AutonFloor);
        txt_AutonTrench = (TextView) findViewById(R.id.txt_AutonTrench);
        txt_AutonControlP = (TextView) findViewById(R.id.txt_AutonControlP);
        txt_AutonBoundary = (TextView) findViewById(R.id.txt_AutonBoundary);
        txt_AutonURobot = (TextView) findViewById(R.id.txt_AutonURobot);
        txt_TeleFloor = (TextView) findViewById(R.id.txt_TeleFloor);
        txt_TelePlaSta = (TextView) findViewById(R.id.txt_TelePlaSta);
        txt_TeleTrench = (TextView) findViewById(R.id.txt_TeleTrench);
        txt_TeleCP = (TextView) findViewById(R.id.txt_TeleCP);
        txt_TeleBoundary = (TextView) findViewById(R.id.txt_TeleBoundary);
        txt_TeleRobot = (TextView) findViewById(R.id.txt_TeleRobot);
        txt_TeleCPSpin = (TextView) findViewById(R.id.txt_TeleCPSpin);
        txt_TeleCPColor = (TextView) findViewById(R.id.txt_TeleCPColor);
        txt_TeleClimb = (TextView) findViewById(R.id.txt_TeleClimb);
        txt_TelePark = (TextView) findViewById(R.id.txt_TelePark);
        txt_TeleBalance = (TextView) findViewById(R.id.txt_TeleBalanced);
        txt_StartPositions = (TextView) findViewById(R.id.txt_StartPositions);
        txt_Pos = (TextView) findViewById(R.id.txt_Pos);
        txt_Matches.setText(matches);
        txt_auto_leftSectorLine.setText(String.valueOf(numleftSectorLine));
        txt_noAuton.setText(String.valueOf(noAuton));
//        Log.w(TAG, "Ratio of Placed to Attempted Gears in Auto = " + auto_SwCubesPlaced + "/" + auto_SwCubesAttempted);
        String carScored = "⚪" + String.format("%-3s", auto_Low) + " U" + String.format("%-3s", auto_HighClose) + " L" + String.format("%-3s", auto_HighLine) + " F" + String.format("%-3s", auto_HighFrontCP);
        txt_Ss_PowerCellScored.setText(carScored);
        String startingBalls = "⁰" + String.format("%-3s", precell_0) + " ¹" + String.format("%-3s", precell_1) + " ²" + String.format("%-3s", precell_2) + " ³" + String.format("%-3s", precell_3);
        txt_StartingBalls.setText(startingBalls);
        String telePowerCell = "⚪" + String.format("%-3s", Tele_Low) + " U" + String.format("%-3s", Tele_HighClose) + " L" + String.format("%-3s", Tele_HighLine) + " F" + String.format("%-3s", Tele_HighFrontCP) + " B" + String.format("%-3s", Tele_HighBackCP);
        txt_Tele_PowerCellScored.setText(telePowerCell);
        String teleHatchPanel = "¹" + String.valueOf(TpanL1) + " ²" + String.valueOf(TpanL2) + " ³" + String.valueOf(TpanL3);
        String HabEnd = "⁰"+ String.valueOf(climbH0) + " ¹" + String.valueOf(climbH1) + " ²" + String.valueOf(climbH2) + " ³" + String.valueOf(climbH3);
        txt_HabLvl.setText(HabEnd);
        txt_AutonFloor.setText(String.valueOf(auton_Floor));
        txt_AutonTrench.setText(String.valueOf(auton_Trench));
        txt_AutonBoundary.setText(String.valueOf(auton_Boundary));
        txt_AutonControlP.setText(String.valueOf(auton_ControlP));
        txt_AutonURobot.setText(String.valueOf(auton_Robot));
        txt_TeleFloor.setText(String.valueOf(tele_PowerCellFloor));
        txt_TelePlaSta.setText(String.valueOf(tele_PowerCellPlasta));
        txt_TeleTrench.setText(String.valueOf(tele_PowerCellTrench));
        txt_TeleCP.setText(String.valueOf(tele_PowerCellCP));
        txt_TeleBoundary.setText(String.valueOf(tele_PowerCellBoundary));
        txt_TeleRobot.setText(String.valueOf(tele_PowerCellRobot));
        txt_TeleCPSpin.setText(String.valueOf(tele_CPSpin));
        txt_TeleCPColor.setText(String.valueOf(tele_CPColor));
        txt_TeleClimb.setText(String.valueOf(tele_Climb));
        txt_TelePark.setText(String.valueOf(tele_Park));
        txt_TeleBalance.setText(String.valueOf(tele_Balance));
        String StartPositions = String.format("%-3s", Integer.toString(auto_B1)) + "  " + String.format("%-3s", Integer.toString(auto_B2)) + "  " + String.format("%-3s", Integer.toString(auto_B3)) + "   " + String.format("%-3s", Integer.toString(auto_B4)) + "   " + String.format("%-3s", Integer.toString(auto_B5)) + "  " + auto_B6;
        txt_StartPositions.setText(String.valueOf(StartPositions));
        txt_Pos.setText(String.format("%-3s", Integer.toString(auto_Ps1)) + "  " + String.format("%-3s", Integer.toString(auto_Ps2)) + "  " + String.format("%-3s", Integer.toString(auto_Ps3)));

        txt_AutoComments.setText(auto_Comments);

        // ==============================================
        // Display Tele elements
        txt_Lift1NUM.setText(String.valueOf(LiftNm));
        txt_WasLiftedNUM.setText(String.valueOf(WasLifted));

        txt_TeleComments.setText(tele_Comments);

        // ==============================================
        // Display Final elements
        txt_final_LostComm.setText(String.valueOf(final_LostComm));
        txt_final_LostParts.setText(String.valueOf(final_LostParts));
        txt_final_DefGood.setText(String.valueOf(final_DefGood));
        txt_final_DefDump.setText(String.valueOf(final_DefDump));
        txt_final_TrenchInt.setText(String.valueOf(final_TrenchInt));
        txt_final_NumPen.setText(String.valueOf(final_NumPen));

        txt_FinalComments.setText(final_Comments);


        autonBarChart.startAnimation();
        teleBarChart.startAnimation();

    }

    //******************************
    private void init_Values() {
        noAuton = 0;
        numleftSectorLine = 0;
        numleftSectorLine2 = 0;
        precell_0 = 0; precell_1 = 0; precell_2 = 0; precell_3 = 0;
        auto_Ps1 = 0;
        auto_Ps2 = 0;
        auto_Ps3 = 0;
        auto_Low = 0; auto_HighClose = 0; auto_HighLine = 0; auto_HighFrontCP = 0;
        Tele_Low = 0; Tele_HighClose = 0; Tele_HighLine = 0; Tele_HighFrontCP = 0;  Tele_HighBackCP = 0;
        panL1 = 0; panL2 = 0; panL3 = 0; TpanL1 = 0; TpanL2 = 0; TpanL3 = 0;
        auton_Floor= 0; auton_Trench = 0; auton_ControlP = 0; auton_Boundary = 0; auton_Robot = 0;
        tele_PowerCellFloor = 0; tele_PowerCellPlasta = 0; tele_PowerCellTrench = 0; tele_PowerCellCP = 0; tele_PowerCellBoundary = 0; tele_PowerCellRobot = 0;
        tele_CPSpin = 0; tele_CPColor = 0;
        tele_Climb = 0; tele_Park = 0; tele_Balance = 0;
        numTeleClimbSuccess = 0;
        lift1Num = 0;
        liftedNum = 0;
        LiftNm = 0;
        WasLifted = 0;
        auto_B1 = 0; auto_B2 = 0; auto_B3 = 0; auto_B4 = 0; auto_B5 = 0; auto_B6 = 0;
        auto_Ps1 = 0; auto_Ps2 = 0; auto_Ps3 = 0;
        climbH0 = 0; climbH1 = 0; climbH2 = 0; climbH3 = 0;
        auto_Comments = "";
        tele_Comments = "";
        final_Comments = "";
        matches = "";
        final_LostComm = 0;
        final_LostParts = 0;
        final_DefGood = 0;
        final_TrenchInt = 0;
        final_DefDump = 0;
        final_NumPen = 0;
        autonBarChart.clearChart();
        teleBarChart.clearChart();
    }


    //===========================================================================================
    public class numMatches_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            String num = " ";
            num = parent.getItemAtPosition(pos).toString();
            Log.w(TAG, ">>>>> NumMatches '" + num + "'");
            switch (num) {
                case "Last":
                    start = numObjects - 1;     //
                    numProcessed = 1;
                    break;
                case "Last 2":
                    if (numObjects > 2) {
                        start = numObjects - 2;     //
                        numProcessed = 2;
                        break;
                    } else {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                        Toast toast = Toast.makeText(getBaseContext(), "***  This team only has " + numObjects +  " match(s) ***", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                case "Last 3":
                    if (numObjects > 2) {
                        start = numObjects - 3;     //
                        numProcessed = 3;
                        break;
                    } else {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                        Toast toast = Toast.makeText(getBaseContext(), "***  This team only has " + numObjects +  " match(s) ***", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                case "Last 4":
                    if (numObjects > 3) {
                        start = numObjects - 4;     //
                        numProcessed = 4;
                        break;
                    } else {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                        Toast toast = Toast.makeText(getBaseContext(), "***  This team only has " + numObjects +  " match(s) ***", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                case "Last 5":
                    if (numObjects > 4) {
                        start = numObjects - 5;     //
                        numProcessed = 5;
                        break;
                    } else {
                        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                        Toast toast = Toast.makeText(getBaseContext(), "***  This team only has " + numObjects +  " match(s) ***", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                case "ALL":
                    start = 0;                  // Start at beginning
                    numProcessed = numObjects;
                    break;
                default:                //
                    Log.e(TAG, "Invalid Sort - " + start);
            }
//            Log.w(TAG, "Start = " + num );
            init_Values();
            getMatch_Data();
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
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
//        Log.e(TAG, "@@@  Options  @@@ " );
//        Log.w(TAG, " \n  \n");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_screen) {
            String filNam = Pearadox.FRC_Event.toUpperCase() + "-VizMatch"  + "_" + tnum.trim() + ".JPG";
//            Log.w(TAG, "File='" + filNam + "'");
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



    private void sayLeaving() {
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_AutoComments.setTextSize(20);
        txt_AutoComments.setText("**** Exiting " + TAG + " ****" );
    }


    private void AutoStackBar(int num, String match, float low, float under, float line, float front) {
        Log.d(TAG, "@@@  AutoStackBar  @@@ " + num + " " + match + " " + low + " " + under + " " + front);
        StackedBarChart autonBarChart = (StackedBarChart) findViewById(R.id.autonBarChart);

        switch (num) {
            case 1:
                StackedBarModel s1 = new StackedBarModel(match);
                s1.addBar(new BarModel(low, 0xFFff0000));    // Low
                s1.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s1.addBar(new BarModel(line, 0xFF006400));    // Line
                s1.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s1);
                break;
            case 2:
                StackedBarModel s2 = new StackedBarModel(match);
                s2.addBar(new BarModel(low, 0xFFff0000));    // Low
                s2.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s2.addBar(new BarModel(line, 0xFF006400));   // Line
                s2.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s2);
                break;
            case 3:
                StackedBarModel s3 = new StackedBarModel(match);
                s3.addBar(new BarModel(low, 0xFFff0000));    // Low
                s3.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s3.addBar(new BarModel(line, 0xFF006400));    // Line
                s3.addBar(new BarModel(front, 0xFF800080));    // Front

                autonBarChart.addBar(s3);
                break;
            case 4:
                StackedBarModel s4 = new StackedBarModel(match);
                s4.addBar(new BarModel(low, 0xFFff0000));    // Low
                s4.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s4.addBar(new BarModel(line, 0xFF006400));    // Line
                s4.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s4);
                break;
            case 5:
                StackedBarModel s5 = new StackedBarModel(match);
                s5.addBar(new BarModel(low, 0xFFff0000));    // Low
                s5.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s5.addBar(new BarModel(line, 0xFF006400));    // Line
                s5.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s5);
                break;

            case 6:
                StackedBarModel s6 = new StackedBarModel(match);
                s6.addBar(new BarModel(low, 0xFFff0000));    // Low
                s6.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s6.addBar(new BarModel(line, 0xFF006400));    // Line
                s6.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s6);
                break;

            case 7:
                StackedBarModel s7 = new StackedBarModel(match);
                s7.addBar(new BarModel(low, 0xFFff0000));    // Low
                s7.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s7.addBar(new BarModel(line, 0xFF006400));    // Line
                s7.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s7);
                break;

            case 8:
                StackedBarModel s8 = new StackedBarModel(match);
                s8.addBar(new BarModel(low, 0xFFff0000));    // Low
                s8.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s8.addBar(new BarModel(line, 0xFF006400));    // Line
                s8.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s8);
                break;

            case 9:
                StackedBarModel s9 = new StackedBarModel(match);
                s9.addBar(new BarModel(low, 0xFFff0000));    // Low
                s9.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s9.addBar(new BarModel(line, 0xFF006400));    // Line
                s9.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s9);
                break;

            case 10:
                StackedBarModel s10 = new StackedBarModel(match);
                s10.addBar(new BarModel(low, 0xFFff0000));    // Low
                s10.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s10.addBar(new BarModel(line, 0xFF006400));    // Line
                s10.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s10);
                break;

            case 11:
                StackedBarModel s11 = new StackedBarModel(match);
                s11.addBar(new BarModel(low, 0xFFff0000));    // Low
                s11.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s11.addBar(new BarModel(line, 0xFF006400));    // Line
                s11.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s11);
                break;

            case 12:
                StackedBarModel s12 = new StackedBarModel(match);
                s12.addBar(new BarModel(low, 0xFFff0000));    // Low
                s12.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s12.addBar(new BarModel(line, 0xFF006400));    // Line
                s12.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s12);
                break;

            case 13:
                StackedBarModel s13 = new StackedBarModel(match);
                s13.addBar(new BarModel(low, 0xFFff0000));    // Low
                s13.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s13.addBar(new BarModel(line, 0xFF006400));    // Line
                s13.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s13);
                break;

            case 14:
                StackedBarModel s14 = new StackedBarModel(match);
                s14.addBar(new BarModel(low, 0xFFff0000));    // Low
                s14.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s14.addBar(new BarModel(line, 0xFF006400));    // Line
                s14.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s14);
                break;

            case 15:
                StackedBarModel s15 = new StackedBarModel(match);
                s15.addBar(new BarModel(low, 0xFFff0000));    // Low
                s15.addBar(new BarModel(under, 0xFF0000ff));    // Under
                s15.addBar(new BarModel(line, 0xFF006400));    // Line
                s15.addBar(new BarModel(front, 0xFF800080));    // Front
                autonBarChart.addBar(s15);
                break;

            default:                //
                Log.e(TAG, "Chart can only handle 15 - " + num);

        }}

    private void TeleStackBar(int num, String match, float low, float under) {
        StackedBarChart teleBarChart = (StackedBarChart) findViewById(R.id.teleBarChart);

        switch (num) {
            case 1:
                StackedBarModel s1 = new StackedBarModel(match);
                s1.addBar(new BarModel(low, 0xFFff0000));    // Low
                s1.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s1.addBar(new BarModel(line, 0xFF006400));    // Line
//                s1.addBar(new BarModel(front, 0xFF800080));    // Front
//                s1.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s1);
                break;
            case 2:
                StackedBarModel s2 = new StackedBarModel(match);
                s2.addBar(new BarModel(low, 0xFFff0000));    // Low
                s2.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s2.addBar(new BarModel(line, 0xFF006400));   // Line
//                s2.addBar(new BarModel(front, 0xFF800080));    // Front
//                s2.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s2);
                break;
            case 3:
                StackedBarModel s3 = new StackedBarModel(match);
                s3.addBar(new BarModel(low, 0xFFff0000));    // Low
                s3.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s3.addBar(new BarModel(line, 0xFF006400));    // Line
//                s3.addBar(new BarModel(front, 0xFF800080));    // Front
//                s3.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s3);
                break;
            case 4:
                StackedBarModel s4 = new StackedBarModel(match);
                s4.addBar(new BarModel(low, 0xFFff0000));    // Low
                s4.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s4.addBar(new BarModel(line, 0xFF006400));    // Line
//                s4.addBar(new BarModel(front, 0xFF800080));    // Front
//                s4.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s4);
                break;
            case 5:
                StackedBarModel s5 = new StackedBarModel(match);
                s5.addBar(new BarModel(low, 0xFFff0000));    // Low
                s5.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s5.addBar(new BarModel(line, 0xFF006400));    // Line
//                s5.addBar(new BarModel(front, 0xFF800080));    // Front
//                s5.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s5);
                break;
            case 6:
                StackedBarModel s6 = new StackedBarModel(match);
                s6.addBar(new BarModel(low, 0xFFff0000));    // Low
                s6.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s6.addBar(new BarModel(line, 0xFF006400));    // Line
//                s6.addBar(new BarModel(front, 0xFF800080));    // Front
//                s6.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s6);
                break;
            case 7:
                StackedBarModel s7 = new StackedBarModel(match);
                s7.addBar(new BarModel(low, 0xFFff0000));    // Low
                s7.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s7.addBar(new BarModel(line, 0xFF006400));   // Line
//                s7.addBar(new BarModel(front, 0xFF800080));    // Front
//                s7.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s7);
                break;
            case 8:
                StackedBarModel s8 = new StackedBarModel(match);
                s8.addBar(new BarModel(low, 0xFFff0000));    // Low
                s8.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s8.addBar(new BarModel(line, 0xFF006400));    // Line
//                s8.addBar(new BarModel(front, 0xFF800080));    // Front
//                s8.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s8);
                break;
            case 9:
                StackedBarModel s9 = new StackedBarModel(match);
                s9.addBar(new BarModel(low, 0xFFff0000));    // Low
                s9.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s9.addBar(new BarModel(line, 0xFF006400));    // Line
//                s9.addBar(new BarModel(front, 0xFF800080));    // Front
//                s9.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s9);
                break;
            case 10:
                StackedBarModel s10 = new StackedBarModel(match);
                s10.addBar(new BarModel(low, 0xFFff0000));    // Low
                s10.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s10.addBar(new BarModel(line, 0xFF006400));    // Line
//                s10.addBar(new BarModel(front, 0xFF800080));    // Front
//                s10.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s10);
                break;
            case 11:
                StackedBarModel s11 = new StackedBarModel(match);
                s11.addBar(new BarModel(low, 0xFFff0000));    // Low
                s11.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s11.addBar(new BarModel(line, 0xFF006400));    // Line
//                s11.addBar(new BarModel(front, 0xFF800080));    // Front
//                s11.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s11);
                break;
            case 12:
                StackedBarModel s12 = new StackedBarModel(match);
                s12.addBar(new BarModel(low, 0xFFff0000));    // Low
                s12.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s12.addBar(new BarModel(line, 0xFF006400));    // Line
//                s12.addBar(new BarModel(front, 0xFF800080));    // Front
//                s12.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s12);
                break;
            case 13:
                StackedBarModel s13 = new StackedBarModel(match);
                s13.addBar(new BarModel(low, 0xFFff0000));    // Low
                s13.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s13.addBar(new BarModel(line, 0xFF006400));    // Line
//                s13.addBar(new BarModel(front, 0xFF800080));    // Front
//                s13.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s13);
                break;
            case 14:
                StackedBarModel s14 = new StackedBarModel(match);
                s14.addBar(new BarModel(low, 0xFFff0000));    // Low
                s14.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s14.addBar(new BarModel(line, 0xFF006400));    // Line
//                s14.addBar(new BarModel(front, 0xFF800080));    // Front
//                s14.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s14);
                break;
            case 15:
                StackedBarModel s15 = new StackedBarModel(match);
                s15.addBar(new BarModel(low, 0xFFff0000));    // Low
                s15.addBar(new BarModel(under, 0xFF0000ff));    // Under
//                s15.addBar(new BarModel(line, 0xFF006400));    // Line
//                s15.addBar(new BarModel(front, 0xFF800080));    // Front
//                s15.addBar(new BarModel(back, 0xFFff8300));    // Back
                teleBarChart.addBar(s15);
                break;

            default:                //
                Log.e(TAG, "Chart can only handle 15 - " + num);
        }
    }


    //###################################################################
//###################################################################
//###################################################################
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        sayLeaving();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
    }

}
// This is a test 2
