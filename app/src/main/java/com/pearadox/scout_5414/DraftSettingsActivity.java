package com.pearadox.scout_5414;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DraftSettingsActivity extends AppCompatActivity {
    String TAG = "DraftSettingsActivity";        // This CLASS name
                                                                            // @@@@ Shoot
    public static final String  Cargo_PREF_LEVEL0 =  "prefCargo_L0";        // Lower
    public static final String  Cargo_PREF_LEVEL1 =  "prefCargo_L1";        // Upper
                                                                            //**** Collection
    public static final String  Cargo_PREF_CLEVEL0 = "prefCargo_C0";        // Floor
    public static final String  Cargo_PREF_CLEVEL1 = "prefCargo_C1";        // Terminal

    public static final String  CLIMB_PREF_CLIMB =  "prefClimb_climb";      // Climb
    public static final String  CLIMB_PREF_HANG0 =  "prefClimb_Hang0";      // None
    public static final String  CLIMB_PREF_HANG1 =  "prefClimb_Hang1";      // Low
    public static final String  CLIMB_PREF_HANG2 =  "prefClimb_Hang2";      // Mid
    public static final String  CLIMB_PREF_HANG3 =  "prefClimb_Hang3";      // High
    public static final String  CLIMB_PREF_HANG4 =  "prefClimb_Hang4";      // Traversal

    public static final String  WEIGHT_PREF_CLIMB =  "prefWeight_climb";    // Combined - Climb
    public static final String  WEIGHT_PREF_CARGO = "prefWeight_Cargo";     // Combined - Cargo
    public static final String  WEIGHT_PREF_PEN   = "prefWeight_Penalty";   // Combined - Penalties
    public static final String  WEIGHT_PREF_COMM  = "prefWeight_Comms";     // Combined - Lost Comms

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


