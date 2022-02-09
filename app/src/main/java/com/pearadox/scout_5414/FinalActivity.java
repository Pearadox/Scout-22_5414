package com.pearadox.scout_5414;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
//import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;


/**
 * Created by mlm.02000 on 2/5/2017.
 */


public class FinalActivity extends Activity {

    String TAG = "FinalActivity";      // This CLASS name
    TextView txt_robotnum;
    EditText editText_Comments;
    CheckBox chk_lostPart, chk_lostComm, chk_tipped;
    Button button_Saved;
    RadioGroup radioGroup_defense;
    RadioButton radioButton_def_bad, radioButton_def_avg, radioButton_def_good, radioButton_def_NA;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfMatchData_DBReference;
    String key = null;
    String tn = "";

// ===================  Final Elements for Match Scout Data object ===================
    // ToDo - add any remaining FINAL elements
    public boolean lost_Parts = false;                          // Did they lose parts?
    public boolean lost_Comms = false;                          // Did they lose communication?
    public boolean tipped = false;                              // Did they tipp/get tipped?
    public String final_defense = "";                           // overall Defense?
    public String driver = "";                                  // Driver skill

    public String final_studID = "";                            // set in Auto


    /* */
    public String finalComment = " ";
    public static String timeStamp = " ";


// ===========================================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Other Activity >>");
        setContentView(R.layout.activity_final);
        Bundle bundle = this.getIntent().getExtras();
        pfDatabase = FirebaseDatabase.getInstance();
        pfDevice_DBReference = pfDatabase.getReference("devices");              // List of Students
        pfMatchData_DBReference = pfDatabase.getReference("match-data/" + Pearadox.FRC_Event);    // Match Data


        tn = bundle.getString("tnum");
        Log.w(TAG, tn);      // ** DEBUG **


        txt_robotnum = (TextView) findViewById(R.id.txt_robotnum);
        txt_robotnum.setText(tn);

        timeStamp = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss a").format(new Date());
        Log.w(TAG, timeStamp);

        radioButton_def_bad = (RadioButton) findViewById(R.id.radioButton_def_bad);
        radioButton_def_avg = (RadioButton) findViewById(R.id.radioButton_def_avg);
        radioButton_def_good = (RadioButton) findViewById(R.id.radioButton_def_good);
        radioButton_def_NA = (RadioButton) findViewById(R.id.radioButton_def_NA);
        radioGroup_defense = (RadioGroup) findViewById(R.id.radioGroup_defense);
        chk_lostPart = (CheckBox) findViewById(R.id.chk_lostPart);
//        chk_lostPart.requestFocus();        // Don't let EditText mess up layout!!
        chk_lostComm = (CheckBox) findViewById(R.id.chk_lostComm);
        chk_tipped = (CheckBox) findViewById(R.id.chk_tipped);
        editText_Comments = (EditText) findViewById(R.id.editText_Comments);
        editText_Comments.setClickable(true);
        button_Saved = (Button) findViewById(R.id.button_Saved);

        radioButton_def_NA.setChecked(true);    // Default to NONE
        final_defense = "Not Observed (None)";

        button_Saved.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                updateDev("Saved");               // Update "traffic light" status for Scout Master
                storeFinalData();                       // Put all the Final data collected in Match object
                Pearadox.MatchData_Saved = true;        // Set flag to show saved
                // ToDo - Clear all data back to original settings

                finish();       // Exit

            }
        });

        editText_Comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s);
                finalComment = String.valueOf(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "******  beforeTextChanged TextWatcher  ******");
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "******  onTextChanged TextWatcher  ******" + s);
                finalComment = String.valueOf(s);
            }
        });
        chk_lostPart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG, "chk_lostPart Listener");
                    if (buttonView.isChecked()) {
                        //checked
                        Log.i(TAG, "TextBox is checked.");
                        lost_Parts = true;
                        Log.d(TAG, "Lost Parts = " + lost_Parts);
                    } else {
                        //not checked
                        Log.i(TAG, "TextBox is unchecked.");
                        lost_Parts = false;
                        Log.d(TAG, "Lost Parts = " + lost_Parts);

                    }
                }
            }
        );
        chk_lostComm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "chk_lostComm Listener");
                if (buttonView.isChecked()) {
                    //checked
                    Log.i(TAG, "TextBox is checked.");
                    lost_Comms = true;

                } else {
                    //not checked
                    Log.i(TAG, "TextBox is unchecked.");
                    lost_Comms = false;

                }
            }
        }
        );
        chk_tipped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             Log.i(TAG, "chk_tipped Listener");
             if (buttonView.isChecked()) {
                 Log.i(TAG, "Tipped is checked.");  //checked
                 tipped = true;
             } else {       //not checked
                 Log.i(TAG, "Tipped is unchecked.");
                 tipped = false;
             }
         }
     }
    );

}

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void storeFinalData() {
        Log.i(TAG, ">>>>  storeFinalData  <<<<");
        Log.w(TAG, timeStamp + " is the current date and time.");
        Pearadox.Match_Data.setFinal_lostParts(lost_Parts);
        Pearadox.Match_Data.setFinal_lostComms(lost_Comms);
        Pearadox.Match_Data.setFinal_tipped(tipped);
        Pearadox.Match_Data.setFinal_defense(final_defense);

         /* */
        Pearadox.Match_Data.setFinal_dateTime(timeStamp);
        Pearadox.Match_Data.setFinal_comment(finalComment);

        saveDatatoSDcard();     //Save locally

        String keyID = Pearadox.Match_Data.getMatch() + "-" + Pearadox.Match_Data.getTeam_num();
        pfMatchData_DBReference.child(keyID).setValue(Pearadox.Match_Data);
    }

    private void saveDatatoSDcard() {
        Log.i(TAG, "@@@@  saveDatatoSDcard  @@@@");
        String filename = Pearadox.Match_Data.getMatch() + "-" + Pearadox.Match_Data.getTeam_num() + ".dat";
        ObjectOutput out = null;
        File directMatch = new File(Environment.getExternalStorageDirectory() + "/download/FRC5414/match/" + Pearadox.FRC_Event + "/" + filename);
        Log.d(TAG, "SD card Path = " + directMatch);
        if(directMatch.exists())  {
            // Todo - Replace TOAST with Dialog Box  - "Do you really ..."
            Toast toast = Toast.makeText(getBaseContext(), "Data for " + filename + " already exists!!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(directMatch));
            out.writeObject(Pearadox.Match_Data);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RadioClick_Defense(View view) {
        Log.w(TAG, "@@ RadioClick_Scout @@");
        radioGroup_defense = (RadioGroup) findViewById(R.id.radioGroup_defense);
        int selectedId = radioGroup_defense.getCheckedRadioButtonId();
        radioButton_def_good = (RadioButton) findViewById(selectedId);
        String value = radioButton_def_good.getText().toString();
        Log.w(TAG, "RadioDefnse - Button '" + value + "'");
        switch (value) {
            case ("Bad"):
                Log.w(TAG, "Bad Defense");
                final_defense = "Bad";
                break;
            case ("Avg"):
                Log.w(TAG, "Avg Defense");
                final_defense = "Avg";
                break;
            case ("Good"):
                Log.w(TAG, "Good Defense");
                final_defense = "Good";
                break;
            case ("Not Observed (None)"):
                Log.w(TAG, "Not Observed (None)");
                final_defense = "Not Observed (None)";
                break;
            default:
                Log.e(TAG, "*** Invalide Defense setting ***");
        }
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
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


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder ab = new AlertDialog.Builder(FinalActivity.this);
                ab.setMessage("Do you want to exit without saving? All of your data will be lost!").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked

                    updateDev("Tele");         // Update "traffic light" status for Scout Master
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
//        AlertDialog alertbox = new AlertDialog.Builder(this)
//                .setMessage("Do you want to exit without saving? All of your data will be lost!")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                    // do something when the button is clicked
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                        finish();
//                        //close();
//
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                    // do something when the button is clicked
//                    public void onClick(DialogInterface arg0, int arg1) {
//                    }
//                })
//                .show();


    };



//###################################################################//
//###################################################################//
//###################################################################//
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
        Log.v(TAG, "OnDestroy");

    }
}

