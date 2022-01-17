package com.pearadox.scout_5414;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.Gravity;
import android.view.Menu;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

// Debug & Messaging
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import static android.app.PendingIntent.getActivity;

public class ScoutMaster_Activity extends AppCompatActivity {

    static String TAG = "ScoutMaster_Activity";      // This CLASS name
    ArrayAdapter<String> adapter_typ;
    public String typSelected = " ";
    ArrayAdapter<String> adapter_Num;
    public String NumSelected = " ";
    public int matchSelected = 0;
    public int our_matches = 0;                 // Matches for 5414
    public String matchID = "T00";              // Type + #
    public String batt_Stat = "00";             // Battery Status (
    ListView listView_Matches;
    ArrayList<String> matchList = new ArrayList<String>();
    ArrayAdapter<String> adaptMatch;
    public String next_Match = " ";             // List of next Matches for 5414
    Button btn_Start, btn_Next;
    TextView txt_EventName, txt_MatchID, txt_NextMatch;
    TextView txt_teamR1, txt_teamR2, txt_teamR3, txt_teamB1, txt_teamB2, txt_teamB3;
    TextView txt_teamR1_Name, txt_teamR2_Name, txt_teamR3_Name, txt_teamB1_Name, txt_teamB2_Name, txt_teamB3_Name;
    TextView txt_scoutR1, txt_scoutR2, txt_scoutR3, txt_scoutB1, txt_scoutB2, txt_scoutB3;
    TextView txt_BattR1, txt_BattR2, txt_BattR3, txt_BattB1, txt_BattB2, txt_BattB3;
    ImageView imgStat_R1, imgStat_R2, imgStat_R3, imgStat_B1, imgStat_B2, imgStat_B3;
    ImageView imgBatt_R1, imgBatt_R2, imgBatt_R3, imgBatt_B1, imgBatt_B2, imgBatt_B3;
    ImageView imgBT_R1, imgBT_R2, imgBT_R3, imgBT_B1, imgBT_B2, imgBT_B3;
    private FirebaseDatabase pfDatabase;
    private DatabaseReference pfStudent_DBReference;
    private DatabaseReference pfDevice_DBReference;
    private DatabaseReference pfTeam_DBReference;
    private DatabaseReference pfMatch_DBReference;
    private DatabaseReference pfCur_Match_DBReference;
    public static String[] signedStudents = new String[] {" ", " ", " ", " ", " ", " "};
    public static String[] btArray = new String[] {" ", " ", " ", " ", " ", " "};
    String MY_UUID = "";
    String team_num, team_name, team_loc;
    p_Firebase.teamsObj team_inst = new p_Firebase.teamsObj();
    ArrayList<p_Firebase.teamsObj> teams = new ArrayList<p_Firebase.teamsObj>();
    String date, time, mtype, match, r1, r2, r3, b1, b2, b3;
    p_Firebase.matchObj match_inst = new p_Firebase.matchObj();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_master);

        Log.i(TAG, "******* Scout Master  *******");
        matchID = "";
        txt_EventName = (TextView) findViewById(R.id.txt_EventName);
        txt_EventName.setText(Pearadox.FRC_EventName);          // Event Name
        txt_MatchID = (TextView) findViewById(R.id.txt_MatchID);
        txt_MatchID.setText(" ");
        txt_NextMatch = (TextView) findViewById(R.id.txt_NextMatch);
        txt_NextMatch.setText("");
        listView_Matches = (ListView) findViewById(R.id.listView_Matches);
        adaptMatch = new ArrayAdapter<String>(this, R.layout.match_list_layout, matchList);
        listView_Matches.setAdapter(adaptMatch);
        adaptMatch.notifyDataSetChanged();
        txt_BattR1 = (TextView) findViewById(R.id.txt_BattR1);
        txt_BattR2 = (TextView) findViewById(R.id.txt_BattR2);
        txt_BattR3 = (TextView) findViewById(R.id.txt_BattR3);
        txt_BattB1 = (TextView) findViewById(R.id.txt_BattB1);
        txt_BattB2 = (TextView) findViewById(R.id.txt_BattB2);
        txt_BattB3 = (TextView) findViewById(R.id.txt_BattB3);
        txt_BattR1.setText(" ");  txt_BattR2.setText(" ");  txt_BattR3.setText(" ");
        txt_BattB1.setText(" ");  txt_BattB2.setText(" ");  txt_BattB3.setText(" ");

        pfDatabase = FirebaseDatabase.getInstance();
        pfTeam_DBReference = pfDatabase.getReference("teams/" + Pearadox.FRC_Event);    // Team data from Firebase D/B
        pfStudent_DBReference = pfDatabase.getReference("students");                    // List of Students
        pfDevice_DBReference = pfDatabase.getReference("devices");                      // List of Students
        pfMatch_DBReference = pfDatabase.getReference("matches/" + Pearadox.FRC_Event); // List of Students
        pfCur_Match_DBReference = pfDatabase.getReference("current-match");             // _THE_ current Match
        clearTeamData();
        clearDevData();

        Button btn_Start = (Button) findViewById(R.id.btn_Start);   // Listner defined in Layout XML
//        button_View.setOnClickListener(buttonStart_Click);
        Button btn_Next = (Button) findViewById(R.id.btn_Next);   // Listner defined in Layout XML
//        button_View.setOnClickListener(buttonNext_Click);


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        listView_Matches.setOnItemClickListener(new AdapterView.OnItemClickListener()	{
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {
                Log.w(TAG,"*** listView_Matches ***   Item Selected: " + pos);
                matchSelected = pos;
                listView_Matches.setSelector(android.R.color.holo_blue_light);
        		/* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
        		int blnk = matchList.get(matchSelected).indexOf(" ");          // 1st blank after MatchID
                Log.w(TAG,"@@@  blnk= " + blnk + " \n \n ");
                matchID = matchList.get(matchSelected).substring(0,blnk);      // GLF 4/19/18  (World's 103 matches!!) 5/2 any length
                Log.w(TAG,"@@@   MatchID: " + matchID);
                txt_MatchID = (TextView) findViewById(R.id.txt_MatchID);
                txt_MatchID.setText(matchID);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void findBluetooth() {
        Log.w(TAG,"*** findBluetooth *** ");
//        ImageView imgBT_R1 = (ImageView) findViewById(R.id.imgBT_R1);
//        ImageView imgBT_R2 = (ImageView) findViewById(R.id.imgBT_R2);
//        ImageView imgBT_R3 = (ImageView) findViewById(R.id.imgBT_R3);
//        ImageView imgBT_B1 = (ImageView) findViewById(R.id.imgBT_B1);
//        ImageView imgBT_B2 = (ImageView) findViewById(R.id.imgBT_B2);
//        ImageView imgBT_B3 = (ImageView) findViewById(R.id.imgBT_B3);
//        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                Log.w(TAG, ">>> Device: " + deviceName + "  Addr: " + deviceHardwareAddress);
//                for(int i=0 ; i < 6 ; i++) {
//                    if (btArray[i].equals(deviceHardwareAddress)) {
//                        Log.w(TAG, ">>> SCOUT: " + deviceName);
//                        switch (deviceName) {
//                            case "Red-1":
//                                imgBT_R1.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
//                            break;
//                            case "Red-2":
//                                imgBT_R2.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
//                            break;
//                            case "Red-3":
//                                imgBT_R3.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
//                            break;
//                            case "Blue-1":
//                                imgBT_B1.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
//                            break;
//                            case "Blue-2":
//                                imgBT_B2.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
//                            break;
//                            case ("Blue-3"):
//                            case ("Gale's Tablet"):         // *** DEBUG!! ***
//                                imgBT_B3.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth));
//                            break;
//                            default:                //
//                                Log.w(TAG, "DEV not a Scout  '" + deviceName + "' ");
//                                break;
//                        }
//                    }
//                } //end FOR
//
//            } //end FOR
//        } //end IF
//
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.scoutmaster_menu, menu);
        return true;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonStart_Click(View view) {
        Log.w(TAG, " Start Button Click  " + matchID);
        String key = "0";
        pfCur_Match_DBReference.child(key).child("cur_match").setValue(matchID);
        getTeams();         // Get the teams for match selected
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void buttonNext_Click(View view) {
        Log.i(TAG, " Next Button Click  " + matchSelected + " " + listView_Matches.getCount());
        if (matchSelected + 1 < listView_Matches.getCount()) {      // +1 since 1st is Zero
            String key = "0";
            pfCur_Match_DBReference.child(key).child("cur_match").setValue("");  // set to null
            pfCur_Match_DBReference.child(key).child("r1").setValue("");
            pfCur_Match_DBReference.child(key).child("r2").setValue("");
            pfCur_Match_DBReference.child(key).child("r3").setValue("");
            pfCur_Match_DBReference.child(key).child("b1").setValue("");
            pfCur_Match_DBReference.child(key).child("b2").setValue("");
            pfCur_Match_DBReference.child(key).child("b3").setValue("");
            clearTeamData();

            matchSelected = matchSelected + 1;                        // increment selection
            Log.w(TAG, "}}}}}}     matchSelected = " + matchSelected);
            listView_Matches.setSelection(matchSelected);
            listView_Matches.setSelector(android.R.color.holo_blue_light);
            int blnk = matchList.get(matchSelected).indexOf(" ");          // 1st blank after MatchID
            matchID = matchList.get(matchSelected).substring(0,blnk);         // GLF 4/19  (103 matches!!)  5/2 any length
            Log.w(TAG, "<<<<<  matchID  >>>>>> '" + matchID + "'");
            txt_MatchID = (TextView) findViewById(R.id.txt_MatchID);
            txt_MatchID.setText(matchID);

            pfCur_Match_DBReference.child(key).child("cur_match").setValue(matchID);
            getTeams();         // Get the teams for match selected
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast toast = Toast.makeText(getBaseContext(), "That's the end of Matches", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }


    private void clearTeamData() {
        Log.i(TAG, "$$$$$  Clear Team Data");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        txt_teamR1.setText(" ");
        txt_teamR2.setText(" ");
        txt_teamR3.setText(" ");
        txt_teamB1.setText(" ");
        txt_teamB2.setText(" ");
        txt_teamB3.setText(" ");
        txt_teamR1_Name.setText(" ");
        txt_teamR2_Name.setText(" ");
        txt_teamR3_Name.setText(" ");
        txt_teamB1_Name.setText(" ");
        txt_teamB2_Name.setText(" ");
        txt_teamB3_Name.setText(" ");
    }

    private void clearDevData() {
        Log.i(TAG, "$$$$$  Clear Device Data");
        txt_scoutR1 = (TextView) findViewById(R.id.txt_scoutR1);
        txt_scoutR2 = (TextView) findViewById(R.id.txt_scoutR2);
        txt_scoutR3 = (TextView) findViewById(R.id.txt_scoutR3);
        txt_scoutB1 = (TextView) findViewById(R.id.txt_scoutB1);
        txt_scoutB2 = (TextView) findViewById(R.id.txt_scoutB2);
        txt_scoutB3 = (TextView) findViewById(R.id.txt_scoutB3);
        txt_scoutR1.setText(" ");
        txt_scoutR2.setText(" ");
        txt_scoutR3.setText(" ");
        txt_scoutB1.setText(" ");
        txt_scoutB2.setText(" ");
        txt_scoutB3.setText(" ");
    }

    private void getTeams() {
        Log.i(TAG, "$$$$$  getTeams");
        Log.w(TAG, ">>>>>  Match = '" + matchID + "'");
        txt_teamR1 = (TextView) findViewById(R.id.txt_teamR1);
        txt_teamR2 = (TextView) findViewById(R.id.txt_teamR2);
        txt_teamR3 = (TextView) findViewById(R.id.txt_teamR3);
        txt_teamB1 = (TextView) findViewById(R.id.txt_teamB1);
        txt_teamB2 = (TextView) findViewById(R.id.txt_teamB2);
        txt_teamB3 = (TextView) findViewById(R.id.txt_teamB3);
        txt_teamR1_Name = (TextView) findViewById(R.id.txt_teamR1_Name);
        txt_teamR2_Name = (TextView) findViewById(R.id.txt_teamR2_Name);
        txt_teamR3_Name = (TextView) findViewById(R.id.txt_teamR3_Name);
        txt_teamB1_Name = (TextView) findViewById(R.id.txt_teamB1_Name);
        txt_teamB2_Name = (TextView) findViewById(R.id.txt_teamB2_Name);
        txt_teamB3_Name = (TextView) findViewById(R.id.txt_teamB3_Name);
        int z = matchID.length();
        if (z >= 3) {                       // GLF 4/19   (103 teams!!)
            Log.i(TAG, "   Q U E R Y   '" + matchID + "'");
            String child = "match";
            String key = matchID;
            Query query = pfMatch_DBReference.orderByChild(child).equalTo(key);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.w(TAG, "%%%  ChildAdded");
                    System.out.println(dataSnapshot.getValue());
                    p_Firebase.matchObj mobj = dataSnapshot.getValue(p_Firebase.matchObj.class);
//                    System.out.println("Match: " + mobj.getMatch());
//                    System.out.println("Type: " + mobj.getMtype());
//                    System.out.println("Date: " + mobj.getDate());
//                    System.out.println("R1: " + mobj.getR1());
//                    System.out.println("B3: " + mobj.getB3());
                    teams.clear();          // empty the list
                    String tn = mobj.getR1();
                    findTeam(tn);
                    tn = mobj.getR2();
                    findTeam(tn);
                    tn = mobj.getR3();
                    findTeam(tn);
                    tn = mobj.getB1();
                    findTeam(tn);
                    tn = mobj.getB2();
                    findTeam(tn);
                    tn = mobj.getB3();
                    findTeam(tn);
                    Log.w(TAG, ">>>> # team instances = " + teams.size());  //** DEBUG
                    // Put the teams for this match on screen
                    String key = "0";   // Since only 1, key is zero
                    team_inst = teams.get(0);
                    txt_teamR1.setText(team_inst.getTeam_num());
                    txt_teamR1_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("r1").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(1);
                    txt_teamR2.setText(team_inst.getTeam_num());
                    txt_teamR2_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("r2").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(2);
                    txt_teamR3.setText(team_inst.getTeam_num());
                    txt_teamR3_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("r3").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(3);
                    txt_teamB1.setText(team_inst.getTeam_num());
                    txt_teamB1_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("b1").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(4);
                    txt_teamB2.setText(team_inst.getTeam_num());
                    txt_teamB2_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("b2").setValue(team_inst.getTeam_num());
                    team_inst = teams.get(5);
                    txt_teamB3.setText(team_inst.getTeam_num());
                    txt_teamB3_Name.setText(team_inst.getTeam_name());
                    pfCur_Match_DBReference.child(key).child("b3").setValue(team_inst.getTeam_num());
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
                }
            });
        } else {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast.makeText(getBaseContext(), "** Invalid Match ID!! ** ", Toast.LENGTH_LONG).show();
        }
    }

    private void findTeam(String tnum) {
        Log.i(TAG, "$$$$$  findTeam " + tnum);
        boolean found = false;
        for (int i = 0; i < Pearadox.numTeams; i++) {        // check each team entry
            if (Pearadox.team_List.get(i).getTeam_num().equals(tnum)) {
                team_inst = Pearadox.team_List.get(i);
                teams.add(team_inst);
//                Log.w(TAG, "===  Team " + team_inst.getTeam_num() + " " + team_inst.getTeam_name() + " " + team_inst.getTeam_loc());
                found = true;
                break;  // found it!
            }
        }  // end For
        if (!found) {
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            Toast.makeText(getBaseContext(),"** Team '" + tnum + "' from Matches table _NOT_ found in Team list  ** ", Toast.LENGTH_LONG).show();
            p_Firebase.teamsObj team_dummy = new p_Firebase.teamsObj();
            teams.add(team_dummy);
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    private class type_OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
//        public void onItemSelected(AdapterView<?> parent,
//                                   View view, int pos, long id) {
//            typSelected = parent.getItemAtPosition(pos).toString();
//            Log.w(TAG, ">>>>>  '" + typSelected + "'");
//            switch (typSelected) {
//                case "Practice":        // Practice round
//                    matchID = "X";
//                    break;
//                case "Qualifying":        // Qualifying round
//                    matchID = "Q";
//                    break;
//                case "Playoff":        // Playoff round
//                    matchID = "P";
//                    break;
//                default:                // ????
//                    Log.e(TAG, "*** Error - bad TYPE indicator  ***");
//            }
//        }
//        public void onNothingSelected(AdapterView<?> parent) {
//            // Do nothing.
//        }
//    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    private class mNum_OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
//        public void onItemSelected(AdapterView<?> parent,
//                                   View view, int pos, long id) {
//            NumSelected = parent.getItemAtPosition(pos).toString();
//            Log.w(TAG, ">>>>>  '" + NumSelected + "'");
//            matchID = matchID + NumSelected;
//        }
//        public void onNothingSelected(AdapterView<?> parent) {
//            // Do nothing.
//        }
//    }


    public void FindDevItem() {
        Log.w(TAG, "%%%%  FindDevItem  %%%%");
        pfDevice_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "Device onDataChange  %%%%");
                txt_scoutR1 = (TextView) findViewById(R.id.txt_scoutR1);
                txt_scoutR2 = (TextView) findViewById(R.id.txt_scoutR2);
                txt_scoutR3 = (TextView) findViewById(R.id.txt_scoutR3);
                txt_scoutB1 = (TextView) findViewById(R.id.txt_scoutB1);
                txt_scoutB2 = (TextView) findViewById(R.id.txt_scoutB2);
                txt_scoutB3 = (TextView) findViewById(R.id.txt_scoutB3);
                ImageView imgStat_R1 = (ImageView) findViewById(R.id.imgStat_R1);
                ImageView imgStat_R2 = (ImageView) findViewById(R.id.imgStat_R2);
                ImageView imgStat_R3 = (ImageView) findViewById(R.id.imgStat_R3);
                ImageView imgStat_B1 = (ImageView) findViewById(R.id.imgStat_B1);
                ImageView imgStat_B2 = (ImageView) findViewById(R.id.imgStat_B2);
                ImageView imgStat_B3 = (ImageView) findViewById(R.id.imgStat_B3);
                int numDevs = 0;
                String device = "";
                String  studname = "";
                String  status = ""; String  btUUID = "";
                p_Firebase.devicesObj dev_Obj = new p_Firebase.devicesObj();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    dev_Obj = iterator.next().getValue(p_Firebase.devicesObj.class);
                    device = dev_Obj.getDev_name();
                    studname = dev_Obj.getStud_id();
                    status = dev_Obj.getPhase();
                    btUUID = dev_Obj.getBtUUID();
                    batt_Stat = dev_Obj.getBatt_stat();
//                    Log.w(TAG, "Battery Status = '" + batt_Stat + "'  \n");
                    set_BattStatus(device);
                    Log.w(TAG, "%%%%  " + studname + " is logged onto " + device + " at Phase '" + status + "' ");
                    numDevs++;
//                    if (studname.length() > 2) {
                        switch (device) {
                            case "Scout Master":         // Scout Master
                                // only interested in Scouts
                                MY_UUID = btUUID;  // for Bluetooth communications
                                break;
                            case ("Red-1"):             //#Red or Blue Scout
                                signedStudents[0] = studname;
                                btArray[0] = btUUID;
                                txt_scoutR1.setText(signedStudents[0]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_R1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.w(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Red-2"):             //#
                                signedStudents[1] = studname;
                                btArray[1] = btUUID;
                                txt_scoutR2.setText(signedStudents[1]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_R2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.w(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Red-3"):             //#
                                signedStudents[2] = studname;
                                btArray[2] = btUUID;
                                txt_scoutR3.setText(signedStudents[2]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_R3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.w(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Blue-1"):            //#
                                signedStudents[3] = studname;
                                btArray[3] = btUUID;
                                txt_scoutB1.setText(signedStudents[3]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_B1.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.w(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Blue-2"):            //#
                                signedStudents[4] = studname;
                                btArray[4] = btUUID;
                                txt_scoutB2.setText(signedStudents[4]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_B2.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.w(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case ("Blue-3"):            //#####
                                signedStudents[5] = studname;
                                btArray[5] = btUUID;
                                txt_scoutB3.setText(signedStudents[5]);
                                switch(status) {
                                    case ("Auto"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_red));
                                        break;
                                    case ("Tele"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_yellow));
                                        break;
                                    case ("Final"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_green));
                                        break;
                                    case ("Saved"):
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_all));
                                        break;
                                    default:                //
                                        imgStat_B3.setImageDrawable(getResources().getDrawable(R.drawable.traffic_light_off));
                                        Log.w(TAG, "***** Unknown Device Status ***** -> " + status);
                                }
                                break;
                            case "Visualizer":          // Visualizer
                                // only interested in Scouts
                                break;
                            default:                //
                                Log.w(TAG, "*** Error DEV = " + device);
                        } // End Switch - Device
//                    }
                } // End While
                Log.w(TAG, "*****  # of devices = " + numDevs);
                Log.e(TAG, "@@@ BT = " + btArray[0] + "  " + btArray[1] + "  "  + btArray[2] + "  "  + btArray[3] + "  "  + btArray[4] + "  "  + btArray[5]);
                findBluetooth();  // Find Bluetooth connected devices
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
            }
        });
    }


    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private void set_BattStatus(String device) {
//        Log.w(TAG, "####  set_BattStatus  ####  " + device + "  " + batt_Stat.length());
        txt_BattR1 = (TextView) findViewById(R.id.txt_BattR1);
        txt_BattR2 = (TextView) findViewById(R.id.txt_BattR2);
        txt_BattR3 = (TextView) findViewById(R.id.txt_BattR3);
        txt_BattB1 = (TextView) findViewById(R.id.txt_BattB1);
        txt_BattB2 = (TextView) findViewById(R.id.txt_BattB2);
        txt_BattB3 = (TextView) findViewById(R.id.txt_BattB3);
        ImageView imgBatt_R1 = (ImageView) findViewById(R.id.imgBatt_R1);
        ImageView imgBatt_R2 = (ImageView) findViewById(R.id.imgBatt_R2);
        ImageView imgBatt_R3 = (ImageView) findViewById(R.id.imgBatt_R3);
        ImageView imgBatt_B1 = (ImageView) findViewById(R.id.imgBatt_B1);
        ImageView imgBatt_B2 = (ImageView) findViewById(R.id.imgBatt_B2);
        ImageView imgBatt_B3 = (ImageView) findViewById(R.id.imgBatt_B3);

        int bat_level = 0;
        if (batt_Stat.length() > 1) {
            bat_level = Integer.parseInt(batt_Stat);
        } else {
            bat_level = 0;
        }
        int rng = 0;
        if (isBetween(bat_level, 90, 100)) {
            rng = 1;
        }
        else if (isBetween(bat_level, 71, 89)) {
            rng = 2;
        }
        else if (isBetween(bat_level, 51, 69)) {
            rng = 3;
        }
        else if (isBetween(bat_level, 31, 49)) {
            rng = 4;
        }
        else if (isBetween(bat_level, 11, 29)) {
            rng = 5;
        }
        else if (bat_level < 11) {
            rng = 6;
        }
        switch (device) {
            case ("Red-1"):             //#Red or Blue Scout
                txt_BattR1.setText(batt_Stat + "%");
                switch (rng) {
                    case 1:
                        imgBatt_R1.setImageDrawable(getResources().getDrawable(R.drawable.batt_100));
                    break;
                    case 2:
                        imgBatt_R1.setImageDrawable(getResources().getDrawable(R.drawable.batt_80));
                        break;
                    case 3:
                        imgBatt_R1.setImageDrawable(getResources().getDrawable(R.drawable.batt_60));
                        break;
                    case 4:
                        imgBatt_R1.setImageDrawable(getResources().getDrawable(R.drawable.batt_40));
                        break;
                    case 5:
                        imgBatt_R1.setImageDrawable(getResources().getDrawable(R.drawable.batt_20));
                        break;
                    case 6:
                        imgBatt_R1.setImageDrawable(getResources().getDrawable(R.drawable.batt_0));
                        break;
                    default:
                 } // End Switch - percent
                break;
            case ("Red-2"):
                txt_BattR2.setText(batt_Stat + "%");
                switch (rng) {
                    case 1:
                        imgBatt_R2.setImageDrawable(getResources().getDrawable(R.drawable.batt_100));
                        break;
                    case 2:
                        imgBatt_R2.setImageDrawable(getResources().getDrawable(R.drawable.batt_80));
                        break;
                    case 3:
                        imgBatt_R2.setImageDrawable(getResources().getDrawable(R.drawable.batt_60));
                        break;
                    case 4:
                        imgBatt_R2.setImageDrawable(getResources().getDrawable(R.drawable.batt_40));
                        break;
                    case 5:
                        imgBatt_R2.setImageDrawable(getResources().getDrawable(R.drawable.batt_20));
                        break;
                    case 6:
                        imgBatt_R2.setImageDrawable(getResources().getDrawable(R.drawable.batt_0));
                        break;
                    default:
                } // End Switch - percent
                break;
            case ("Red-3"):
                txt_BattR3.setText(batt_Stat + "%");
                switch (rng) {
                    case 1:
                        imgBatt_R3.setImageDrawable(getResources().getDrawable(R.drawable.batt_100));
                        break;
                    case 2:
                        imgBatt_R3.setImageDrawable(getResources().getDrawable(R.drawable.batt_80));
                        break;
                    case 3:
                        imgBatt_R3.setImageDrawable(getResources().getDrawable(R.drawable.batt_60));
                        break;
                    case 4:
                        imgBatt_R3.setImageDrawable(getResources().getDrawable(R.drawable.batt_40));
                        break;
                    case 5:
                        imgBatt_R3.setImageDrawable(getResources().getDrawable(R.drawable.batt_20));
                        break;
                    case 6:
                        imgBatt_R3.setImageDrawable(getResources().getDrawable(R.drawable.batt_0));
                        break;
                    default:
                } // End Switch - percent
                break;
            case ("Blue-1"):
                txt_BattB1.setText(batt_Stat + "%");
                switch (rng) {
                    case 1:
                        imgBatt_B1.setImageDrawable(getResources().getDrawable(R.drawable.batt_100));
                        break;
                    case 2:
                        imgBatt_B1.setImageDrawable(getResources().getDrawable(R.drawable.batt_80));
                        break;
                    case 3:
                        imgBatt_B1.setImageDrawable(getResources().getDrawable(R.drawable.batt_60));
                        break;
                    case 4:
                        imgBatt_B1.setImageDrawable(getResources().getDrawable(R.drawable.batt_40));
                        break;
                    case 5:
                        imgBatt_B1.setImageDrawable(getResources().getDrawable(R.drawable.batt_20));
                        break;
                    case 6:
                        imgBatt_B1.setImageDrawable(getResources().getDrawable(R.drawable.batt_0));
                        break;
                    default:
                } // End Switch - percent
                break;
            case ("Blue-2"):
                txt_BattB2.setText(batt_Stat + "%");
                switch (rng) {
                    case 1:
                        imgBatt_B2.setImageDrawable(getResources().getDrawable(R.drawable.batt_100));
                        break;
                    case 2:
                        imgBatt_B2.setImageDrawable(getResources().getDrawable(R.drawable.batt_80));
                        break;
                    case 3:
                        imgBatt_B2.setImageDrawable(getResources().getDrawable(R.drawable.batt_60));
                        break;
                    case 4:
                        imgBatt_B2.setImageDrawable(getResources().getDrawable(R.drawable.batt_40));
                        break;
                    case 5:
                        imgBatt_B2.setImageDrawable(getResources().getDrawable(R.drawable.batt_20));
                        break;
                    case 6:
                        imgBatt_B2.setImageDrawable(getResources().getDrawable(R.drawable.batt_0));
                        break;
                    default:
                } // End Switch - percent
                break;
            case ("Blue-3"):
                txt_BattB3.setText(batt_Stat + "%");
                switch (rng) {
                    case 1:
                        imgBatt_B3.setImageDrawable(getResources().getDrawable(R.drawable.batt_100));
                        break;
                    case 2:
                        imgBatt_B3.setImageDrawable(getResources().getDrawable(R.drawable.batt_80));
                        break;
                    case 3:
                        imgBatt_B3.setImageDrawable(getResources().getDrawable(R.drawable.batt_60));
                        break;
                    case 4:
                        imgBatt_B3.setImageDrawable(getResources().getDrawable(R.drawable.batt_40));
                        break;
                    case 5:
                        imgBatt_B3.setImageDrawable(getResources().getDrawable(R.drawable.batt_20));
                        break;
                    case 6:
                        imgBatt_B3.setImageDrawable(getResources().getDrawable(R.drawable.batt_0));
                        break;
                    default:
                } // End Switch - percent
                break;
            default:                //
                Log.w(TAG, "*** Error DEV = " + device);
        } // End Switch - Device

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void loadMatches() {
        Log.i(TAG, "###  loadMatches  ###");

        addMatchSched_VE_Listener(pfMatch_DBReference.orderByChild("match"));
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void addMatchSched_VE_Listener(final Query pfMatch_DBReference) {
        pfMatch_DBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "******* Firebase retrieve Match Schedule  *******");
                txt_NextMatch = (TextView) findViewById(R.id.txt_NextMatch);
                matchList.clear();
                next_Match = "";
                our_matches = 0;
                p_Firebase.matchObj match_inst = new p_Firebase.matchObj();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();   /*get the data children*/
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    match_inst = iterator.next().getValue(p_Firebase.matchObj.class);
//                    Log.w(TAG,"      " + match_inst.getMatch());
//                    matchList.add(match_inst.getMatch() + "  Time: " + match_inst.getTime() + "  " + match_inst.getMtype());
                    matchList.add(match_inst.getMatch() + "  " + match_inst.getMtype());
                    // Create the list of _OUR_ matches across the top
                    if (match_inst.getR1().matches("5414")) {
                        next_Match = next_Match + match_inst.getMatch() + " ";
                        our_matches ++;
                    }
                    if (match_inst.getR2().matches("5414")) {
                        next_Match = next_Match + match_inst.getMatch() + " ";
                        our_matches ++;
                    }
                    if (match_inst.getR3().matches("5414")) {
                        next_Match = next_Match + match_inst.getMatch() + " ";
                        our_matches ++;
                    }
                    if (match_inst.getB1().matches("5414")) {
                        next_Match = next_Match + match_inst.getMatch() + " ";
                        our_matches ++;
                    }
                    if (match_inst.getB2().matches("5414")) {
                        next_Match = next_Match + match_inst.getMatch() + " ";
                        our_matches ++;
                    }
                    if (match_inst.getB3().matches("5414")) {
                        next_Match = next_Match + match_inst.getMatch() + " ";
                        our_matches ++;
                    }
                }
                Log.w(TAG, "### Matches ###  : " + matchList.size() + "    Ours=" + our_matches);
                if (matchList.size() > 0) {
                    if (our_matches > 12) {
                        txt_NextMatch.setTextSize((float) 10.0);
                    }
                    txt_NextMatch.setText(next_Match);
                    Pearadox.our_Matches = next_Match;
                    String key = "0";   // Since only 1, key is zero
                    pfCur_Match_DBReference.child(key).child("our_matches").setValue(next_Match);  // Store it in Firebase for Scouts
                    listView_Matches = (ListView) findViewById(R.id.listView_Matches);
                    adaptMatch = new ArrayAdapter<String>(ScoutMaster_Activity.this, R.layout.match_list_layout, matchList);
                    listView_Matches.setAdapter(adaptMatch);
                    adaptMatch.notifyDataSetChanged();
                } else {
                    txt_NextMatch.setText("*** There are _NO_ matches stored in FireBase ***");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*listener failed or was removed for security reasons*/
                throw databaseError.toException();
            }
        });
        }


//###################################################################
//###################################################################
//###################################################################
//###################################################################
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        FindDevItem();  // Get devices that are logged on

        loadMatches();  // Find all matches for this event
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
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
    }
}
