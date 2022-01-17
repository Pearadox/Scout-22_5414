package com.pearadox.scout_5414;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DraftSettingsActivity extends AppCompatActivity {
    String TAG = "DraftSettingsActivity";        // This CLASS name
                                                                                    // @@@@ Shoot
    public static final String  PowerCell_PREF_Dump   =  "prefPowerCell_Dump";      // Dump
    public static final String  PowerCell_PREF_LEVEL0 =  "prefPowerCell_L0";        // Low
    public static final String  PowerCell_PREF_LEVEL1 =  "prefPowerCell_L1";        // Under
    public static final String  PowerCell_PREF_LEVEL2 =  "prefPowerCell_L2";        // Line
    public static final String  PowerCell_PREF_LEVEL3 =  "prefPowerCell_L3";        // CP Front
    public static final String  PowerCell_PREF_LEVEL4 =  "prefPowerCell_L4";        // CP Back
                                                                                    //**** Collection
    public static final String  PowerCell_PREF_CLEVEL0 = "prefPowerCell_C0";        // Floor
    public static final String  PowerCell_PREF_CLEVEL1 = "prefPowerCell_C1";        // Robot
    public static final String  PowerCell_PREF_CLEVEL2 = "prefPowerCell_C2";        // C.P.
    public static final String  PowerCell_PREF_CLEVEL3 = "prefPowerCell_C3";        // Trench
    public static final String  PowerCell_PREF_CLEVEL4 = "prefPowerCell_C4";        // Boundary
    public static final String  PowerCell_PREF_CLEVEL5 = "prefPowerCell_C5";        // LoadSta

    public static final String  PANELS_PREF_LEVEL1 = "prefPanel_L1";                // C.P. Spin
    public static final String  PANELS_PREF_LEVEL2 = "prefPanel_L2";                // C.P. Color

    public static final String  CLIMB_PREF_CLIMB =  "prefClimb_climb";
    public static final String  CLIMB_PREF_PARK  =  "prefClimb_park";
    public static final String  CLIMB_PREF_BAL   =  "prefClimb_Balanced";
    public static final String  CLIMB_PREF_LIFT1 =  "prefClimb_lift1";
    public static final String  CLIMB_PREF_LIFT2 =  "prefClimb_lift2";
    public static final String  CLIMB_PREF_LIFTED = "prefClimb_lifted";
    public static final String  CLIMB_PREF_HANG0 =   "prefClimb_Hang0";
    public static final String  CLIMB_PREF_HANG1 =   "prefClimb_Hang1";
    public static final String  CLIMB_PREF_HANG2 =   "prefClimb_Hang2";
    public static final String  CLIMB_PREF_HANG3 =   "prefClimb_Hang3";

    public static final String  WEIGHT_PREF_CLIMB =       "prefWeight_climb";
    public static final String  WEIGHT_PREF_CUBESSWITCH = "prefWeight_PowerCell";
    public static final String  WEIGHT_PREF_CUBESSCALE =  "prefWeight_panel";

    public static final String  ALLIANCE_PICKS_NUM =   "prefAlliance_num";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new CubeSettingsFrag())
                .commit();
        Log.e(TAG, "**** Draft Scout Settings  **** ");
        Log.w(TAG, " \n  \n");
    }
}


