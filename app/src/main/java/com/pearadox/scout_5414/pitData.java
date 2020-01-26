package com.pearadox.scout_5414;

import java.io.Serializable;

public class pitData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= Pit Scout Data ================
    public String pit_team = " ";                   // Team #
    public int pit_weight = 0;                      // Height (inches)
    public int pit_totWheels = 0;                   // Total # of wheels
    public int pit_numTrac = 0;                     // Num. of Traction wheels
    public int pit_numOmni = 0;                     // Num. of Omni wheels
    public int pit_numMecanum = 0;                  // Num. of Mecanum wheels
    public int pit_numPneumatic = 0;                // Num. of Pneumatic wheels
    public boolean pit_vision = false;              // presence of Vision Camera
    public boolean pit_pneumatics = false;          // presence of Pneumatics
    public boolean pit_climb = false;               // presence of a Climbing mechanism
    public boolean pit_spin = false;                // Ability to Spin # turns on Control Panel
    public boolean pit_color = false;               // Ability to Stop Wheel on color
    public boolean pit_PowerCellManip = false;      // presence of a way to pick up PowerCell from floor
    public boolean pit_undTrench = false;           // Ability to Drive under Control Panel (in Trench)
    public boolean pit_canLift = false;             // Ability to lift other robots
    public int pit_numLifted = 0;                   // Num. of robots can lift (1-2)
    public boolean pit_liftRamp = false;            // lift type Ramp
    public boolean pit_liftHook = false;            // lift type Hook
    public String pit_motor;                        // Type of Motor
    public String pit_lang;                         // Programming  Language
    public String pit_autoMode;                     // Autonomous Operatong Mode
    //                                              // Grid
    public boolean pit_climbL1 = false;             //   L1--M1--R1
    public boolean pit_climbL2 = false;             //   |    |   |
    public boolean pit_climbL3 = false;             //   |    |   |
    public boolean pit_climbM1 = false;             //   L2--M2--R2
    public boolean pit_climbM2 = false;             //   |    |   |
    public boolean pit_climbM3 = false;             //   |    |   |
    public boolean pit_climbR1 = false;             //   L3--M3--R3
    public boolean pit_climbR2 = false;             //
    public boolean pit_climbR3 = false;             //
    public boolean pit_deump = false;               // Can dump cells to partner
    public boolean pit_shootBot = false;            // Can Shoot into Bottom Port
    public boolean pit_shootOut = false;            // Can Shoot into Outer Port
    public boolean pit_shootInn = false;            // Can Shoot into Inner Port

    /* */
    public String pit_comment;                      // Comment(s)
    public String pit_scout = " ";                  // Student who collected the data
    public String  pit_dateTime;                    // Date & Time data was saved
    public String pit_photoURL;                     // URL of the robot photo in Firebase


    // ===========================================================================
    //  Constructor

    public pitData(String pit_team, int pit_weight, int pit_totWheels, int pit_numTrac, int pit_numOmni, int pit_numMecanum, int pit_numPneumatic, boolean pit_vision, boolean pit_pneumatics, boolean pit_climb, boolean pit_spin, boolean pit_color, boolean pit_PowerCellManip, boolean pit_undTrench, boolean pit_canLift, int pit_numLifted, boolean pit_liftRamp, boolean pit_liftHook, String pit_motor, String pit_lang, String pit_autoMode, String pit_comment, String pit_scout, String pit_dateTime, String pit_photoURL) {
        this.pit_team = pit_team;
        this.pit_weight = pit_weight;
        this.pit_totWheels = pit_totWheels;
        this.pit_numTrac = pit_numTrac;
        this.pit_numOmni = pit_numOmni;
        this.pit_numMecanum = pit_numMecanum;
        this.pit_numPneumatic = pit_numPneumatic;
        this.pit_vision = pit_vision;
        this.pit_pneumatics = pit_pneumatics;
        this.pit_climb = pit_climb;
        this.pit_spin = pit_spin;
        this.pit_color = pit_color;
        this.pit_PowerCellManip = pit_PowerCellManip;
        this.pit_undTrench = pit_undTrench;
        this.pit_canLift = pit_canLift;
        this.pit_numLifted = pit_numLifted;
        this.pit_liftRamp = pit_liftRamp;
        this.pit_liftHook = pit_liftHook;
        this.pit_motor = pit_motor;
        this.pit_lang = pit_lang;
        this.pit_autoMode = pit_autoMode;
        this.pit_comment = pit_comment;
        this.pit_scout = pit_scout;
        this.pit_dateTime = pit_dateTime;
        this.pit_photoURL = pit_photoURL;
    }

    // ===========================================================================
// Default constructor required for calls to
// DataSnapshot.getValue(teams.class)
public pitData() {
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// Getters & Setters

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPit_team() {
        return pit_team;
    }

    public void setPit_team(String pit_team) {
        this.pit_team = pit_team;
    }

    public int getPit_weight() {
        return pit_weight;
    }

    public void setPit_weight(int pit_weight) {
        this.pit_weight = pit_weight;
    }

    public int getPit_totWheels() {
        return pit_totWheels;
    }

    public void setPit_totWheels(int pit_totWheels) {
        this.pit_totWheels = pit_totWheels;
    }

    public int getPit_numTrac() {
        return pit_numTrac;
    }

    public void setPit_numTrac(int pit_numTrac) {
        this.pit_numTrac = pit_numTrac;
    }

    public int getPit_numOmni() {
        return pit_numOmni;
    }

    public void setPit_numOmni(int pit_numOmni) {
        this.pit_numOmni = pit_numOmni;
    }

    public int getPit_numMecanum() {
        return pit_numMecanum;
    }

    public void setPit_numMecanum(int pit_numMecanum) {
        this.pit_numMecanum = pit_numMecanum;
    }

    public int getPit_numPneumatic() {
        return pit_numPneumatic;
    }

    public void setPit_numPneumatic(int pit_numPneumatic) {
        this.pit_numPneumatic = pit_numPneumatic;
    }

    public boolean isPit_vision() {
        return pit_vision;
    }

    public void setPit_vision(boolean pit_vision) {
        this.pit_vision = pit_vision;
    }

    public boolean isPit_pneumatics() {
        return pit_pneumatics;
    }

    public void setPit_pneumatics(boolean pit_pneumatics) {
        this.pit_pneumatics = pit_pneumatics;
    }

    public boolean isPit_climb() {
        return pit_climb;
    }

    public void setPit_climb(boolean pit_climb) {
        this.pit_climb = pit_climb;
    }

    public boolean isPit_spin() {
        return pit_spin;
    }

    public void setPit_spin(boolean pit_spin) {
        this.pit_spin = pit_spin;
    }

    public boolean isPit_color() {
        return pit_color;
    }

    public void setPit_color(boolean pit_color) {
        this.pit_color = pit_color;
    }

    public boolean isPit_PowerCellManip() {
        return pit_PowerCellManip;
    }

    public void setPit_PowerCellManip(boolean pit_PowerCellManip) {
        this.pit_PowerCellManip = pit_PowerCellManip;
    }

    public boolean isPit_undTrench() {
        return pit_undTrench;
    }

    public void setPit_undTrench(boolean pit_undTrench) {
        this.pit_undTrench = pit_undTrench;
    }

    public boolean isPit_canLift() {
        return pit_canLift;
    }

    public void setPit_canLift(boolean pit_canLift) {
        this.pit_canLift = pit_canLift;
    }

    public int getPit_numLifted() {
        return pit_numLifted;
    }

    public void setPit_numLifted(int pit_numLifted) {
        this.pit_numLifted = pit_numLifted;
    }

    public boolean isPit_liftRamp() {
        return pit_liftRamp;
    }

    public void setPit_liftRamp(boolean pit_liftRamp) {
        this.pit_liftRamp = pit_liftRamp;
    }

    public boolean isPit_liftHook() {
        return pit_liftHook;
    }

    public void setPit_liftHook(boolean pit_liftHook) {
        this.pit_liftHook = pit_liftHook;
    }

    public String getPit_motor() {
        return pit_motor;
    }

    public void setPit_motor(String pit_motor) {
        this.pit_motor = pit_motor;
    }

    public String getPit_lang() {
        return pit_lang;
    }

    public void setPit_lang(String pit_lang) {
        this.pit_lang = pit_lang;
    }

    public String getPit_autoMode() {
        return pit_autoMode;
    }

    public void setPit_autoMode(String pit_autoMode) {
        this.pit_autoMode = pit_autoMode;
    }

    public String getPit_comment() {
        return pit_comment;
    }

    public void setPit_comment(String pit_comment) {
        this.pit_comment = pit_comment;
    }

    public String getPit_scout() {
        return pit_scout;
    }

    public void setPit_scout(String pit_scout) {
        this.pit_scout = pit_scout;
    }

    public String getPit_dateTime() {
        return pit_dateTime;
    }

    public void setPit_dateTime(String pit_dateTime) {
        this.pit_dateTime = pit_dateTime;
    }

    public String getPit_photoURL() {
        return pit_photoURL;
    }

    public void setPit_photoURL(String pit_photoURL) {
        this.pit_photoURL = pit_photoURL;
    }


// End of Getters & Setters
}
