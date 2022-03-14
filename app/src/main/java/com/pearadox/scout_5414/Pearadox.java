package com.pearadox.scout_5414;

//******************************************************
// GLOBAL Variables

import java.util.ArrayList;

public class Pearadox {

    public static boolean  is_Network; 								// Internet available?
    public static String FRC_Event;                                 // FIRST Event Code (e.g., txwa)
    public static String FRC_EventName;                             // FIRST Event Name (e.g., 'Hub City')
    public static String FRC_ChampDiv;                              // FIRST Championshio Division (e.g., 'Einstein')
    public static String FRC514_Device;                             // Device ID
    public static int maxTeams = 300; 								// Maximum # of Teams per event (increase for Worlds)  GLF 4/9
    public static int maxStudents = 100; 						    // Maximum # of Students
    public static ArrayList<p_Firebase.eventObj> eventList = new ArrayList<p_Firebase.eventObj>();      // Event objects
    public static String[] comp_List = new String[8];               // Events list (array of just Names)
    public static int num_Events = 0; 						        // Actual # of Events/Competitions
    public static ArrayList<p_Firebase.teamsObj> team_List = new ArrayList<p_Firebase.teamsObj>();
    public static int numTeams = 0; 						        // Actual # of Teams
    public static ArrayList<p_Firebase.students> stud_Lst = new ArrayList<p_Firebase.students>();
    public static String[] student_List = new String[maxStudents];  // Student list (array of just Names)
    public static int numStudents = 0; 						        // # of Students
    public static ArrayList<p_Firebase.devicesObj> dev_List = new ArrayList<p_Firebase.devicesObj>();   // BT Device List
    public static int numBTdevices = 0; 						    // # of Bluetooth Devices
    public static String Student_ID;                                // Student Name
    public static String our_Matches = "";                          // List of all matches for 5414
    public static String[] matches = new String[]
            {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};

//*********************************************************
// Inter-Activity for saving data
    public static boolean  MatchData_Saved; 			// Data from Match saved to disk and Firebase



// Java Object _SHARED_ by Auto, Tele & Final
    public static matchData Match_Data = new matchData();

// -----  Array of Match Data Objects for Match Data Visualizer
public static ArrayList<matchData> Matches_Data = new ArrayList<matchData>();


}
