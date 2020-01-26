package com.pearadox.scout_5414;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DraftSettingsActivity extends AppCompatActivity {
    String TAG = "DraftSettingsActivity";        // This CLASS name
    public static final String  PowerCell_PREF_LEVEL1 =  "prefPowerCell_L1";
    public static final String  PowerCell_PREF_LEVEL2 =  "prefPowerCell_L2";
    public static final String  PowerCell_PREF_LEVEL3 =  "prefPowerCell_L3";

    public static final String  PANELS_PREF_LEVEL1 = "prefPanel_L1";
    public static final String  PANELS_PREF_LEVEL2 = "prefPanel_L2";
    public static final String  PANELS_PREF_LEVEL3 = "prefPanel_L3";
    public static final String  PANELS_PREF_DROP   = "prefPanel_Drop";

    public static final String  CLIMB_PREF_LIFT1 =  "prefClimb_lift1";
    public static final String  CLIMB_PREF_LIFT2 =  "prefClimb_lift2";
    public static final String  CLIMB_PREF_LIFTED = "prefClimb_lifted";
    public static final String  CLIMB_PREF_HAB0 =   "prefClimb_HAB0";
    public static final String  CLIMB_PREF_HAB1 =   "prefClimb_HAB1";
    public static final String  CLIMB_PREF_HAB2 =   "prefClimb_HAB2";
    public static final String  CLIMB_PREF_HAB3 =   "prefClimb_HAB3";

    public static final String  WEIGHT_PREF_CLIMB =       "prefWeight_climb";
    public static final String  WEIGHT_PREF_CUBESSWITCH = "prefWeight_PowerCell";
    public static final String  WEIGHT_PREF_CUBESSCALE =  "prefWeight_panels";

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

