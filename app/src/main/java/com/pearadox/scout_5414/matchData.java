package com.pearadox.scout_5414;

import java.io.Serializable;

public class matchData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= AUTO ================
    private String match;                   // Match ID (e.g., Qualifying) and '00' - match #)
    private String team_num;                // Team Number (e.g., '5414')
                                            // *** Pre-Game **
    private int     pre_cargo_carried;      // Carry how many Cargo(s)
    private String  pre_startPos;           // Start Position
    private int     pre_PlayerSta;          // Player Station (1-3)

                                            // ---- AFTER Start ----
    private boolean auto_mode;              // Do they have Autonomous mode?
    private boolean auto_leftTarmac;        // Did they leave Tarmac
    private boolean auto_CollectFloor;      // Collect from Floor?
    private boolean auto_CollectTerm;       // Collect from Terminal?
    private boolean auto_Human;             // Score by Human Player?
    private int     auto_Low;               // # Low Goal balls
    private int     auto_High;              // # High Goal balls

    private String  auto_comment;           // Auto comment

    // ============== TELE =================
    private boolean tele_Cargo_floor;       // Did they pick up Cargo from floor
    private boolean tele_Cargo_term;        // Did they get Cargo from Terminal
    private int     tele_Low;               // # Low Goal balls
    private int     tele_High;              // # High Goal balls

    private boolean tele_Climbed;           // Did they Climb?
    private String  tele_HangarLevel;       // Climbed to Hangar Level 'x'

    private int     tele_num_Penalties;     // How many penalties received?
    private String  tele_comment;           // Tele comment

    // ============= Final  ================
    private boolean final_lostParts;         // Did they lose parts
    private boolean final_lostComms;         // Did they lose communication
    private String  final_defense;           // Their overall Defense
    /*=============================================================================*/
    private String  final_comment;           // Final comment
    private String  final_studID;            // Student doing the scouting
    private String  final_dateTime;          // Date & Time data was saved

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//  Constructor

    public matchData(String match, String team_num, int pre_cargo_carried, String pre_startPos, int pre_PlayerSta, boolean auto_mode, boolean auto_leftTarmac, boolean auto_CollectFloor, boolean auto_CollectTerm, boolean auto_Human, int auto_Low, int auto_High, String auto_comment, boolean tele_Cargo_floor, boolean tele_Cargo_term, int tele_Low, int tele_High, boolean tele_Climbed, String tele_HangarLevel, int tele_num_Penalties, String tele_comment, boolean final_lostParts, boolean final_lostComms, String final_defense, String final_comment, String final_studID, String final_dateTime) {
        this.match = match;
        this.team_num = team_num;
        this.pre_cargo_carried = pre_cargo_carried;
        this.pre_startPos = pre_startPos;
        this.pre_PlayerSta = pre_PlayerSta;
        this.auto_mode = auto_mode;
        this.auto_leftTarmac = auto_leftTarmac;
        this.auto_CollectFloor = auto_CollectFloor;
        this.auto_CollectTerm = auto_CollectTerm;
        this.auto_Human = auto_Human;
        this.auto_Low = auto_Low;
        this.auto_High = auto_High;
        this.auto_comment = auto_comment;
        this.tele_Cargo_floor = tele_Cargo_floor;
        this.tele_Cargo_term = tele_Cargo_term;
        this.tele_Low = tele_Low;
        this.tele_High = tele_High;
        this.tele_Climbed = tele_Climbed;
        this.tele_HangarLevel = tele_HangarLevel;
        this.tele_num_Penalties = tele_num_Penalties;
        this.tele_comment = tele_comment;
        this.final_lostParts = final_lostParts;
        this.final_lostComms = final_lostComms;
        this.final_defense = final_defense;
        this.final_comment = final_comment;
        this.final_studID = final_studID;
        this.final_dateTime = final_dateTime;
    }

    //
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// Default constructor required for calls to
// DataSnapshot.getValue(matchData.class)
public matchData() {

}

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// Getters & Setters

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getTeam_num() {
        return team_num;
    }

    public void setTeam_num(String team_num) {
        this.team_num = team_num;
    }

    public int getPre_cargo_carried() {
        return pre_cargo_carried;
    }

    public void setPre_cargo_carried(int pre_cargo_carried) {
        this.pre_cargo_carried = pre_cargo_carried; }

    public String getPre_startPos() {
        return pre_startPos;
    }

    public void setPre_startPos(String pre_startPos) {
        this.pre_startPos = pre_startPos;
    }

    public int getPre_PlayerSta() {
        return pre_PlayerSta;
    }

    public void setPre_PlayerSta(int pre_PlayerSta) {
        this.pre_PlayerSta = pre_PlayerSta;
    }

    public boolean isAuto_mode() {
        return auto_mode;
    }

    public void setAuto_mode(boolean auto_mode) {
        this.auto_mode = auto_mode;
    }

    public boolean isAuto_leftTarmac() {
        return auto_leftTarmac;
    }

    public void setAuto_leftTarmac(boolean auto_leftTarmac) {
        this.auto_leftTarmac = auto_leftTarmac;
    }

    public boolean isAuto_CollectFloor() {
        return auto_CollectFloor;
    }

    public void setAuto_CollectFloor(boolean auto_CollectFloor) {
        this.auto_CollectFloor = auto_CollectFloor;
    }

    public boolean isAuto_CollectTerm() {
        return auto_CollectTerm;
    }

    public void setAuto_CollectTerm(boolean auto_CollectTerm) {
        this.auto_CollectTerm = auto_CollectTerm;
    }

    public int getAuto_Low() {
        return auto_Low;
    }

    public void setAuto_Low(int auto_Low) {
        this.auto_Low = auto_Low;
    }

    public int getAuto_High() {
        return auto_High;
    }

    public void setAuto_High(int auto_High) {
        this.auto_High = auto_High;
    }

    public String getAuto_comment() {
        return auto_comment;
    }

    public void setAuto_comment(String auto_comment) {
        this.auto_comment = auto_comment;
    }

    public boolean isTele_Cargo_floor() {
        return tele_Cargo_floor;
    }

    public void setTele_Cargo_floor(boolean tele_Cargo_floor) {
        this.tele_Cargo_floor = tele_Cargo_floor;
    }

    public boolean isTele_Cargo_term() {
        return tele_Cargo_term;
    }

    public void setTele_Cargo_term(boolean tele_Cargo_term) {
        this.tele_Cargo_term = tele_Cargo_term;
    }

    public int getTele_Low() {
        return tele_Low;
    }

    public void setTele_Low(int tele_Low) {
        this.tele_Low = tele_Low;
    }

    public int getTele_High() {
        return tele_High;
    }

    public void setTele_High(int tele_High) {
        this.tele_High = tele_High;
    }

    public boolean isTele_Climbed() {
        return tele_Climbed;
    }

    public void setTele_Climbed(boolean tele_Climbed) {
        this.tele_Climbed = tele_Climbed;
    }

    public String getTele_HangarLevel() {
        return tele_HangarLevel;
    }

    public void setTele_HangarLevel(String tele_HangarLevel) {
        this.tele_HangarLevel = tele_HangarLevel;
    }

    public boolean isAuto_Human() {
        return auto_Human;
    }

    public void setAuto_Human(boolean auto_Human) {
        this.auto_Human = auto_Human;
    }

    public int getTele_num_Penalties() {
        return tele_num_Penalties;
    }

    public void setTele_num_Penalties(int tele_num_Penalties) {
        this.tele_num_Penalties = tele_num_Penalties;
    }

    public String getTele_comment() {
        return tele_comment;
    }

    public void setTele_comment(String tele_comment) {
        this.tele_comment = tele_comment;
    }

    public boolean isFinal_lostParts() {
        return final_lostParts;
    }

    public void setFinal_lostParts(boolean final_lostParts) {
        this.final_lostParts = final_lostParts;
    }

    public boolean isFinal_lostComms() {
        return final_lostComms;
    }

    public void setFinal_lostComms(boolean final_lostComms) {
        this.final_lostComms = final_lostComms;
    }

    public String getFinal_defense() {
        return final_defense;
    }

    public void setFinal_defense(String final_defense) {
        this.final_defense = final_defense;
    }

    public String getFinal_comment() {
        return final_comment;
    }

    public void setFinal_comment(String final_comment) {
        this.final_comment = final_comment;
    }

    public String getFinal_studID() {
        return final_studID;
    }

    public void setFinal_studID(String final_studID) {
        this.final_studID = final_studID;
    }

    public String getFinal_dateTime() {
        return final_dateTime;
    }

    public void setFinal_dateTime(String final_dateTime) {
        this.final_dateTime = final_dateTime;
    }

    //   GLF 1/31/22
// End of Getters/Setters

}