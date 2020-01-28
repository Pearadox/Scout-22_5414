package com.pearadox.scout_5414;

import java.io.Serializable;

public class matchData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= AUTO ================
    private String match;                   // Match ID (e.g., Qualifying) and '00' - match #)
    private String team_num;                // Team Number (e.g., '5414')
                                            // *** Pre-Game **
    private int     pre_cells_carried;      // Carry how many PowerCell(s)
    private String  pre_startPos;           // Start Position
    private int     pre_PlayerSta;          // Player Station (1-3)

                                            // ---- AFTER Start ----
    private boolean auto_mode;              // Do they have Autonomous mode?
    private boolean auto_leftSectorLine;    // Did they leave Sector Line
    private boolean auto_Dump;              // Did they Dump balls to partner?
    private boolean auto_Collect;           // Did they collect more Power Cells?
    private boolean auto_CollectFloor;      // Collect from Floor?
    private boolean auto_CollectRobot;      // Collect from a Robot?
    private boolean auto_CollectTrench;     // Collect from Trench?
    private boolean auto_CollectSGboundary; // Collect from SG boundary?
    private int     auto_Low;               // # Low Goal balls
    private int     auto_High;              // # High Goal balls
    private boolean auto_conInner;          // Consistent Inner Goal scored?
    private boolean auto_ShootUnder;        // Shoot from Under Power Port
    private boolean auto_ShootLine;         // Shoot from Sector Line
    private boolean auto_ShootFtrench;      // Shoot from in Front of Trench

    private String  auto_comment;           // Auto comment

    // ============== TELE =================
    private int     tele_Low;               // # Low Goal balls
    private int     tele_High;              // # High Goal balls
    private boolean tele_conInner;          // Consistent Inner Goal scored?
    private boolean tele_CPspin;            // Control Panel Spin
    private boolean tele_CPcolor;           // Control Panel Color
    private boolean tele_ShootUnder;        // Shoot from Under Power Port
    private boolean tele_ShootLine;         // Shoot from Sector Line
    private boolean tele_ShootFtrench;      // Shoot from in Front of Trench
    private boolean tele_ShootBtrench;      // Shoot from in Back of Trench


    private boolean tele_PowerCell_floor;   // Did they pick up PowerCell from floor
    private boolean tele_PowerCell_LoadSta; // Did they get PowerCell from Loading Station

    private boolean tele_Climbed;           // Did they Climb?
    private boolean tele_UnderSG;           // Parked under Shield Generator
    private boolean tele_got_lift;          // Did they get lifted to higher HAB Level
    private boolean tele_lifted;            // Did they lift a robot to higher HAB Level
    private int     tele_liftedNum;         // How many lifted?
    private int     tele_Hang_num;          // End - How many on Bar (0-3)
    private boolean tele_Balanced;          // SG is Balanced

    private int     tele_num_Penalties;     // How many penalties received?
    private int     tele_num_Dropped;       // How many Panels dropped?
    private String  tele_comment;           // Tele comment

    // ============= Final  ================
    private boolean final_lostParts;         // Did they lose parts
    private boolean final_lostComms;         // Did they lose communication
    private boolean final_defLast30;         // Did they play Defense in Last 30 seconds?
    private boolean final_defense_good;      // Was their overall Defense Good (bad=false)
    private boolean final_def_Block;         // Did they use Blocking Defense
    private boolean final_def_TrenchInt;     // Did they block the Trench
    /*=============================================================================*/
    private String  final_comment;           // Final comment
    private String  final_studID;            // Student doing the scouting
    private String  final_dateTime;          // Date & Time data was saved

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//  Constructor


    public matchData(String match, String team_num, int pre_cells_carried, String pre_startPos, int pre_PlayerSta, boolean auto_mode, boolean auto_leftSectorLine, boolean auto_Dump, boolean auto_Collect, boolean auto_CollectFloor, boolean auto_CollectRobot, boolean auto_CollectTrench, boolean auto_CollectSGboundary, int auto_Low, int auto_High, boolean auto_conInner, boolean auto_ShootUnder, boolean auto_ShootLine, boolean auto_ShootFtrench, String auto_comment, int tele_Low, int tele_High, boolean tele_conInner, boolean tele_CPspin, boolean tele_CPcolor, boolean tele_ShootUnder, boolean tele_ShootLine, boolean tele_ShootFtrench, boolean tele_ShootBtrench, boolean tele_PowerCell_floor, boolean tele_PowerCell_LoadSta, boolean tele_Climbed, boolean tele_UnderSG, boolean tele_got_lift, boolean tele_lifted, int tele_liftedNum, int tele_Hang_num, boolean tele_Balanced, int tele_num_Penalties, int tele_num_Dropped, String tele_comment, boolean final_lostParts, boolean final_lostComms, boolean final_defLast30, boolean final_defense_good, boolean final_def_Block, boolean final_def_TrenchInt, String final_comment, String final_studID, String final_dateTime) {
        this.match = match;
        this.team_num = team_num;
        this.pre_cells_carried = pre_cells_carried;
        this.pre_startPos = pre_startPos;
        this.pre_PlayerSta = pre_PlayerSta;
        this.auto_mode = auto_mode;
        this.auto_leftSectorLine = auto_leftSectorLine;
        this.auto_Dump = auto_Dump;
        this.auto_Collect = auto_Collect;
        this.auto_CollectFloor = auto_CollectFloor;
        this.auto_CollectRobot = auto_CollectRobot;
        this.auto_CollectTrench = auto_CollectTrench;
        this.auto_CollectSGboundary = auto_CollectSGboundary;
        this.auto_Low = auto_Low;
        this.auto_High = auto_High;
        this.auto_conInner = auto_conInner;
        this.auto_ShootUnder = auto_ShootUnder;
        this.auto_ShootLine = auto_ShootLine;
        this.auto_ShootFtrench = auto_ShootFtrench;
        this.auto_comment = auto_comment;
        this.tele_Low = tele_Low;
        this.tele_High = tele_High;
        this.tele_conInner = tele_conInner;
        this.tele_CPspin = tele_CPspin;
        this.tele_CPcolor = tele_CPcolor;
        this.tele_ShootUnder = tele_ShootUnder;
        this.tele_ShootLine = tele_ShootLine;
        this.tele_ShootFtrench = tele_ShootFtrench;
        this.tele_ShootBtrench = tele_ShootBtrench;
        this.tele_PowerCell_floor = tele_PowerCell_floor;
        this.tele_PowerCell_LoadSta = tele_PowerCell_LoadSta;
        this.tele_Climbed = tele_Climbed;
        this.tele_UnderSG = tele_UnderSG;
        this.tele_got_lift = tele_got_lift;
        this.tele_lifted = tele_lifted;
        this.tele_liftedNum = tele_liftedNum;
        this.tele_Hang_num = tele_Hang_num;
        this.tele_Balanced = tele_Balanced;
        this.tele_num_Penalties = tele_num_Penalties;
        this.tele_num_Dropped = tele_num_Dropped;
        this.tele_comment = tele_comment;
        this.final_lostParts = final_lostParts;
        this.final_lostComms = final_lostComms;
        this.final_defLast30 = final_defLast30;
        this.final_defense_good = final_defense_good;
        this.final_def_Block = final_def_Block;
        this.final_def_TrenchInt = final_def_TrenchInt;
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

    public int getPre_cells_carried() {
        return pre_cells_carried;
    }

    public void setPre_cells_carried(int pre_cells_carried) {
        this.pre_cells_carried = pre_cells_carried;
    }

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

    public boolean isAuto_leftSectorLine() {
        return auto_leftSectorLine;
    }

    public void setAuto_leftSectorLine(boolean auto_leftSectorLine) {
        this.auto_leftSectorLine = auto_leftSectorLine;
    }

    public boolean isAuto_Dump() {
        return auto_Dump;
    }

    public void setAuto_Dump(boolean auto_Dump) {
        this.auto_Dump = auto_Dump;
    }

    public boolean isAuto_Collect() {
        return auto_Collect;
    }

    public void setAuto_Collect(boolean auto_Collect) {
        this.auto_Collect = auto_Collect;
    }

    public boolean isAuto_CollectFloor() {
        return auto_CollectFloor;
    }

    public void setAuto_CollectFloor(boolean auto_CollectFloor) {
        this.auto_CollectFloor = auto_CollectFloor;
    }

    public boolean isAuto_CollectRobot() {
        return auto_CollectRobot;
    }

    public void setAuto_CollectRobot(boolean auto_CollectRobot) {
        this.auto_CollectRobot = auto_CollectRobot;
    }

    public boolean isAuto_CollectTrench() {
        return auto_CollectTrench;
    }

    public void setAuto_CollectTrench(boolean auto_CollectTrench) {
        this.auto_CollectTrench = auto_CollectTrench;
    }

    public boolean isAuto_CollectSGboundary() {
        return auto_CollectSGboundary;
    }

    public void setAuto_CollectSGboundary(boolean auto_CollectSGboundary) {
        this.auto_CollectSGboundary = auto_CollectSGboundary;
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

    public boolean isAuto_conInner() {
        return auto_conInner;
    }

    public void setAuto_conInner(boolean auto_conInner) {
        this.auto_conInner = auto_conInner;
    }

    public boolean isAuto_ShootUnder() {
        return auto_ShootUnder;
    }

    public void setAuto_ShootUnder(boolean auto_ShootUnder) {
        this.auto_ShootUnder = auto_ShootUnder;
    }

    public boolean isAuto_ShootLine() {
        return auto_ShootLine;
    }

    public void setAuto_ShootLine(boolean auto_ShootLine) {
        this.auto_ShootLine = auto_ShootLine;
    }

    public boolean isAuto_ShootFtrench() {
        return auto_ShootFtrench;
    }

    public void setAuto_ShootFtrench(boolean auto_ShootFtrench) {
        this.auto_ShootFtrench = auto_ShootFtrench;
    }

    public String getAuto_comment() {
        return auto_comment;
    }

    public void setAuto_comment(String auto_comment) {
        this.auto_comment = auto_comment;
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

    public boolean isTele_conInner() {
        return tele_conInner;
    }

    public void setTele_conInner(boolean tele_conInner) {
        this.tele_conInner = tele_conInner;
    }

    public boolean isTele_CPspin() {
        return tele_CPspin;
    }

    public void setTele_CPspin(boolean tele_CPspin) {
        this.tele_CPspin = tele_CPspin;
    }

    public boolean isTele_CPcolor() {
        return tele_CPcolor;
    }

    public void setTele_CPcolor(boolean tele_CPcolor) {
        this.tele_CPcolor = tele_CPcolor;
    }

    public boolean isTele_ShootUnder() {
        return tele_ShootUnder;
    }

    public void setTele_ShootUnder(boolean tele_ShootUnder) {
        this.tele_ShootUnder = tele_ShootUnder;
    }

    public boolean isTele_ShootLine() {
        return tele_ShootLine;
    }

    public void setTele_ShootLine(boolean tele_ShootLine) {
        this.tele_ShootLine = tele_ShootLine;
    }

    public boolean isTele_ShootFtrench() {
        return tele_ShootFtrench;
    }

    public void setTele_ShootFtrench(boolean tele_ShootFtrench) {
        this.tele_ShootFtrench = tele_ShootFtrench;
    }

    public boolean isTele_ShootBtrench() {
        return tele_ShootBtrench;
    }

    public void setTele_ShootBtrench(boolean tele_ShootBtrench) {
        this.tele_ShootBtrench = tele_ShootBtrench;
    }

    public boolean isTele_PowerCell_floor() {
        return tele_PowerCell_floor;
    }

    public void setTele_PowerCell_floor(boolean tele_PowerCell_floor) {
        this.tele_PowerCell_floor = tele_PowerCell_floor;
    }

    public boolean isTele_PowerCell_LoadSta() {
        return tele_PowerCell_LoadSta;
    }

    public void setTele_PowerCell_LoadSta(boolean tele_PowerCell_LoadSta) {
        this.tele_PowerCell_LoadSta = tele_PowerCell_LoadSta;
    }

    public boolean isTele_Climbed() {
        return tele_Climbed;
    }

    public void setTele_Climbed(boolean tele_Climbed) {
        this.tele_Climbed = tele_Climbed;
    }

    public boolean isTele_UnderSG() {
        return tele_UnderSG;
    }

    public void setTele_UnderSG(boolean tele_UnderSG) {
        this.tele_UnderSG = tele_UnderSG;
    }

    public boolean isTele_got_lift() {
        return tele_got_lift;
    }

    public void setTele_got_lift(boolean tele_got_lift) {
        this.tele_got_lift = tele_got_lift;
    }

    public boolean isTele_lifted() {
        return tele_lifted;
    }

    public void setTele_lifted(boolean tele_lifted) {
        this.tele_lifted = tele_lifted;
    }

    public int getTele_liftedNum() {
        return tele_liftedNum;
    }

    public void setTele_liftedNum(int tele_liftedNum) {
        this.tele_liftedNum = tele_liftedNum;
    }

    public int getTele_Hang_num() {
        return tele_Hang_num;
    }

    public void setTele_Hang_num(int tele_Hang_num) {
        this.tele_Hang_num = tele_Hang_num;
    }

    public boolean isTele_Balanced() {
        return tele_Balanced;
    }

    public void setTele_Balanced(boolean tele_Balanced) {
        this.tele_Balanced = tele_Balanced;
    }

    public int getTele_num_Penalties() {
        return tele_num_Penalties;
    }

    public void setTele_num_Penalties(int tele_num_Penalties) {
        this.tele_num_Penalties = tele_num_Penalties;
    }

    public int getTele_num_Dropped() {
        return tele_num_Dropped;
    }

    public void setTele_num_Dropped(int tele_num_Dropped) {
        this.tele_num_Dropped = tele_num_Dropped;
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

    public boolean isFinal_defLast30() {
        return final_defLast30;
    }

    public void setFinal_defLast30(boolean final_defLast30) {
        this.final_defLast30 = final_defLast30;
    }

    public boolean isFinal_defense_good() {
        return final_defense_good;
    }

    public void setFinal_defense_good(boolean final_defense_good) {
        this.final_defense_good = final_defense_good;
    }

    public boolean isFinal_def_Block() {
        return final_def_Block;
    }

    public void setFinal_def_Block(boolean final_def_Block) {
        this.final_def_Block = final_def_Block;
    }

    public boolean isFinal_def_TrenchInt() {
        return final_def_TrenchInt;
    }

    public void setFinal_def_TrenchInt(boolean final_def_TrenchInt) {
        this.final_def_TrenchInt = final_def_TrenchInt;
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


//   GLF 1/27/20
// End of Getters/Setters

}