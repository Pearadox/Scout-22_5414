package com.pearadox.scout_5414;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.ToneGenerator;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
import org.eazegraph.lib.models.BarModel;

import java.io.File;
import java.io.FileOutputStream;

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
    TextView txt_Tele_PowerCellScored, txt_Tele_hatchScored, txt_Tele_droppedHatch;
    TextView txt_HabLvl, txt_Lift1NUM, txt_Lift2NUM, txt_WasLiftedNUM;
    TextView txt_AutonFloor, txt_AutonTrench, txt_AutonControlP, txt_AutonBoundary , txtAutonURobot;
    TextView txt_TeleCargFloor, txt_TeleCargPlaSta, txt_TeleCargCorral, txt_TelePanFloor, txt_TelePanPlaSta; 
    // ToDo - TextViews for PowerCell/Panels 2nd & 3rd
    /* Comment Boxes */     TextView txt_AutoComments, txt_TeleComments, txt_FinalComments;
    TextView txt_StartPositions;
    TextView txt_Pos;
    public static String[] numMatch = new String[]             // Num. of Matches to process
            {"ALL","Last","Last 2","Last 3","Last 4","Last 5"};
    BarChart mBarChart;
    int BarPowerCell = 0;  int BarPanels = 0;  int LastPowerCell = 0;  int LastPanels = 0;
    //----------------------------------
    int numleftSectorLine = 0; int numleftSectorLine2 = 0; int noAuton = 0; int precellsCarried = 0; int precell_0 = 0; int precell_1 = 0; int precell_2 = 0; int precell_3 = 0;
    int auto_B1 = 0; int auto_B2 = 0; int auto_B3 = 0; int auto_B4 = 0; int auto_B5 = 0; int auto_B6 = 0;
    // NOTE: _ALL_ external mentions of Playere Sta. (PS) were changed to Loading Sta. (LS) so as to NOT be confused with Player Control Sta. (Driver)
    int auto_Ps1 = 0; int auto_Ps2 = 0; int auto_Ps3 = 0;
    int auton_Floor= 0; int auton_Trench = 0; int auton_ControlP = 0; int auton_Boundary = 0; int auton_Robot = 0;
    int tele_PowerCellFloor = 0; int tele_PowerCellPlasta = 0; int tele_PowerCellCorral = 0; int tele_PanFloor = 0; int tele_PanPlasta = 0;
    int climbH0= 0; int climbH1 = 0; int climbH2 = 0; int climbH3 = 0; int lift1Num = 0; int liftedNum = 0;
    int auto_Low = 0; int auto_HighClose = 0; int auto_HighLine = 0; int auto_HighFrontCP =0; int Tauto_Low = 0; int Tauto_HighClose = 0; int Tauto_HighLine = 0; int Tauto_HighFrontCP = 0; int TpanL1 = 0; int TpanL2 = 0; int TpanL3 = 0;
    int numMatches = 0; int panL1 = 0; int panL2 = 0; int panL3 = 0; int dropped=0; int Tdropped = 0;
    String auto_Comments = "";
    //----------------------------------
    int numTeleClimbSuccess = 0; int LiftNm = 0; int WasLifted = 0;
    String tele_Comments = "";
    //----------------------------------
    int final_LostComm = 0; int final_LostParts = 0; int final_DefGood = 0; int final_DefBlock = 0;  int final_DefSwitch = 0; int final_NumPen = 0;
    TextView txt_final_LostComm, txt_final_LostParts, txt_final_DefGood, txt_final_DefBlock, txt_final_BlkSwtch, txt_final_NumPen, txt_final_DefLast30;
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
// ToDo -  findViews PowerCell/Panels 2nd & 3rd
        txt_AutonFloor = (TextView) findViewById(R.id.txt_AutonFloor);
        txt_AutonTrench = (TextView) findViewById(R.id.txt_AutonTrench);
        txt_AutonControlP = (TextView) findViewById(R.id.txt_AutonControlP);
        txt_AutonBoundary = (TextView) findViewById(R.id.txt_AutonBoundary);
        txtAutonURobot = (TextView) findViewById(R.id.txtAutonURobot);
        txt_TeleCargFloor = (TextView) findViewById(R.id.txt_TeleCargFloor);
        txt_TeleCargPlaSta = (TextView) findViewById(R.id.txt_TeleCargPlaSta);
        txt_TeleCargCorral = (TextView) findViewById(R.id.txt_TeleCargCorral);
        txt_TelePanFloor = (TextView) findViewById(R.id.txt_TelePanFloor);
        txt_TelePanPlaSta = (TextView) findViewById(R.id.txt_TelePanPlaSta);
        txt_Tele_droppedHatch = (TextView) findViewById(R.id.txt_Tele_droppedHatch);
        txt_StartPositions = (TextView) findViewById(R.id.txt_StartPositions);
        txt_Pos = (TextView) findViewById(R.id.txt_Pos);
        txt_AutoComments = (TextView) findViewById(R.id.txt_AutoComments);
        txt_AutoComments.setTextSize(12);       // normal
        txt_AutoComments.setMovementMethod(new ScrollingMovementMethod());
        /*  Tele  */
        txt_Tele_PowerCellScored = (TextView) findViewById(R.id.txt_Tele_PowerCellScored);
        txt_Tele_hatchScored = (TextView) findViewById(R.id.txt_Tele_hatchScored);
        txt_Lift1NUM = (TextView) findViewById(R.id.txt_Lift1NUM);
        txt_Lift2NUM = (TextView) findViewById(R.id.txt_Lift2NUM);
        txt_WasLiftedNUM = (TextView) findViewById(R.id.txt_WasLiftedNUM);
//        mBarChart = (BarChart) findViewById(R.id.teleBarChart);
        txt_TeleComments = (TextView) findViewById(R.id.txt_TeleComments);
        txt_TeleComments.setMovementMethod(new ScrollingMovementMethod());

        /*  Final  */
        txt_FinalComments = (TextView) findViewById(R.id.txt_FinalComments);
        txt_FinalComments.setMovementMethod(new ScrollingMovementMethod());

        txt_final_LostComm = (TextView) findViewById(R.id.txt_final_LostComm);
        txt_final_LostParts = (TextView) findViewById(R.id.txt_final_LostParts);
        txt_final_DefGood = (TextView) findViewById(R.id.txt_final_DefGood);
        txt_final_DefBlock = (TextView) findViewById(R.id.txt_final_DefBlock);
        txt_final_BlkSwtch = (TextView) findViewById(R.id.txt_final_BlkSwtch);
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
        BarPowerCell = 0; BarPanels = 0; LastPowerCell = 0;  LastPanels = 0;
        for (int i = start; i < numObjects; i++) {
//            Log.w(TAG, "In for loop!   " + i);
            match_inst = Pearadox.Matches_Data.get(i);      // Get instance of Match Data
            match_id = match_inst.getMatch();
            matches = matches + match_inst.getMatch() + "  ";   // cumulative list of matches

            if (match_inst.isAuto_mode()) {
                noAuton++;
            }
            if (match_inst.isAuto_leftSectorLine()) {
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

            int BallStart = match_inst.getPre_cells_carried();
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

            if (match_inst.isAuto_CollectTrench() ) {
                auton_Trench++;
            }

            if (match_inst.isAuto_CollectCP()) {
                auton_ControlP++;
            }

            if (match_inst.isAuto_CollectSGboundary()) {
                auton_Boundary++;
            }

            if (match_inst.isAuto_CollectRobot()) {
                auton_Robot++;
            }

            // *************************************************
            // ******************** TeleOps ********************
            // *************************************************

            int endHAB = match_inst.getTele_Hang_num();        // end HAB Level
            switch (endHAB) {
                case 0:         // Not On
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
                    e(TAG, "*** Error - bad HAB Level indicator  ***");
            }

            LiftNm = LiftNm + match_inst.getTele_liftedNum();

            if (match_inst.isTele_got_lift()) {
                WasLifted++;
            }
//                if (match_inst.isTele_lift_two()) {
//                    lift2Num++;
//                }
            if (match_inst.isTele_got_lift()) {
                liftedNum++;
            }

            // ToDo - figure out why bar chart is accumulating??????
            BarPowerCell = (auto_Low + auto_HighClose + auto_HighLine + auto_HighFrontCP + Tauto_Low + Tauto_HighLine + Tauto_HighFrontCP) - LastPowerCell;
            BarPanels = (panL1 + panL2 + panL3 + TpanL1 + TpanL2 + TpanL3) - LastPanels;
//            mBarChart.addBar(new BarModel(BarPowerCell, 0xffff0000));       // PowerCell
//            Log.w(TAG, i + " @@@@@@@@ PowerCell=" + BarPowerCell + "   Panels=" + BarPanels + "  " + match_id);
//            Log.e(TAG, "    CL1=" + auto_Low + " CL2=" + auto_HighLine + " CL3=" + auto_HighFrontCP + "    TcL1=" + Tauto_Low + " TcL2=" + Tauto_HighLine + " TcL3=" + Tauto_HighFrontCP + "  Last=" + LastPowerCell);
//            Log.e(TAG, "    PL1=" + panL1 + " PL2=" + panL2 + " PL3=" + panL3 + "    TpL1=" + TpanL1 + " TpL2=" + TpanL2 + " TpL3=" + TpanL3 + "  Last=" + LastPanels +"\n");

//            mBarChart.addBar(new BarModel( BarPanels,  0xff08457e));       // Panels
            LastPowerCell = LastPowerCell + BarPowerCell;
            LastPanels = LastPanels + BarPanels;
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
            if (match_inst.isFinal_def_Block()) {
                final_DefBlock++;
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
        txt_Tele_hatchScored = (TextView) findViewById(R.id.txt_Tele_hatchScored);
        txt_Tele_droppedHatch = (TextView) findViewById(R.id.txt_Tele_droppedHatch);
        txt_AutonFloor = (TextView) findViewById(R.id.txt_AutonFloor);
        txt_AutonTrench = (TextView) findViewById(R.id.txt_AutonTrench);
        txt_AutonControlP = (TextView) findViewById(R.id.txt_AutonControlP);
        txt_AutonBoundary = (TextView) findViewById(R.id.txt_AutonBoundary);
        txtAutonURobot = (TextView) findViewById(R.id.txtAutonURobot);
        txt_TeleCargFloor = (TextView) findViewById(R.id.txt_TeleCargFloor);
        txt_TeleCargPlaSta = (TextView) findViewById(R.id.txt_TeleCargPlaSta);
        txt_TeleCargCorral = (TextView) findViewById(R.id.txt_TeleCargCorral);
        txt_TelePanFloor = (TextView) findViewById(R.id.txt_TelePanFloor);
        txt_TelePanPlaSta = (TextView) findViewById(R.id.txt_TelePanPlaSta);
        txt_StartPositions = (TextView) findViewById(R.id.txt_StartPositions);
        txt_Pos = (TextView) findViewById(R.id.txt_Pos);
        txt_Matches.setText(matches);
        txt_auto_leftSectorLine.setText(String.valueOf(numleftSectorLine));
        txt_noAuton.setText(String.valueOf(noAuton));
//        Log.w(TAG, "Ratio of Placed to Attempted Gears in Auto = " + auto_SwCubesPlaced + "/" + auto_SwCubesAttempted);
        String carScored = "⚫" + String.format("%-3s", auto_Low) + " U" + String.format("%-3s", auto_HighClose) + " L" + String.format("%-3s", auto_HighLine) + " F" + String.format("%-3s", auto_HighFrontCP);
        txt_Ss_PowerCellScored.setText(carScored);
        String startingBalls = "⁰" + String.format("%-3s", auto_Low) + " ¹" + String.format("%-3s", auto_Low) + " ²" + String.format("%-3s", auto_Low) + " ³" + String.format("%-3s", auto_Low);
        txt_StartingBalls.setText(startingBalls);
        String telePowerCell = "¹" + String.valueOf(Tauto_Low) + " ²" + String.valueOf(Tauto_HighLine) + " ³" + String.valueOf(Tauto_HighFrontCP);
        txt_Tele_PowerCellScored.setText(telePowerCell);
        String teleHatchPanel = "¹" + String.valueOf(TpanL1) + " ²" + String.valueOf(TpanL2) + " ³" + String.valueOf(TpanL3);
        txt_Tele_hatchScored.setText(teleHatchPanel);
        txt_Tele_droppedHatch.setText(String.valueOf(Tdropped));
        String HabEnd = "⁰"+ String.valueOf(climbH0) + " ¹" + String.valueOf(climbH1) + " ²" + String.valueOf(climbH2) + " ³" + String.valueOf(climbH3);
        txt_AutonFloor.setText(String.valueOf(auton_Floor));
        txt_AutonTrench.setText(String.valueOf(auton_Trench));
        txt_AutonBoundary.setText(String.valueOf(auton_Boundary));
        txt_AutonControlP.setText(String.valueOf(auton_ControlP));
        txtAutonURobot.setText(String.valueOf(auton_Robot));
        txt_TeleCargFloor.setText(String.valueOf(tele_PowerCellFloor));
        txt_TeleCargPlaSta.setText(String.valueOf(tele_PowerCellPlasta));
        txt_TeleCargCorral.setText(String.valueOf(tele_PowerCellCorral));
        txt_TelePanFloor.setText(String.valueOf(tele_PanFloor));
        txt_TelePanPlaSta.setText(String.valueOf(tele_PanPlasta));
        txt_HabLvl.setText(HabEnd);
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
        txt_final_BlkSwtch.setText(String.valueOf(final_DefSwitch));
        txt_final_DefBlock.setText(String.valueOf(final_DefBlock));
        txt_final_NumPen.setText(String.valueOf(final_NumPen));

        txt_FinalComments.setText(final_Comments);


//        mBarChart.startAnimation();

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
        auto_Low = 0; auto_HighClose = 0; auto_HighLine = 0; auto_HighFrontCP = 0; Tauto_Low = 0; Tauto_HighLine = 0; Tauto_HighFrontCP = 0;
        panL1 = 0; panL2 = 0; panL3 = 0; TpanL1 = 0; TpanL2 = 0; TpanL3 = 0;
        auton_Floor= 0; auton_Trench = 0; auton_ControlP = 0; auton_Boundary = 0; auton_Robot = 0;
        tele_PowerCellFloor = 0; tele_PowerCellPlasta = 0; tele_PowerCellCorral = 0; tele_PanFloor = 0; tele_PanPlasta = 0;
        numTeleClimbSuccess = 0;
        lift1Num = 0;
        liftedNum = 0;
        LiftNm = 0;
        WasLifted = 0;
        auto_B1 = 0; auto_B2 = 0; auto_B3 = 0; auto_B4 = 0; auto_B5 = 0; auto_B6 = 0;
        auto_Ps1 = 0; auto_Ps2 = 0; auto_Ps3 = 0;
        climbH0 = 0; climbH1 = 0; climbH2 = 0; climbH3 = 0;
        dropped = 0; Tdropped =0;
        auto_Comments = "";
        tele_Comments = "";
        final_Comments = "";
        matches = "";
        final_LostComm = 0;
        final_LostParts = 0;
        final_DefGood = 0;
        final_DefBlock = 0;
        final_NumPen = 0;
        BarPowerCell = 0; BarPanels = 0; LastPowerCell = 0;  LastPanels = 0;
//        mBarChart.clearChart();
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
