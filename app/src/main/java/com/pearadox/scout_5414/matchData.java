package com.pearadox.scout_5414;

import java.io.Serializable;

public class matchData implements Serializable {
    private static final long serialVersionUID = -54145414541400L;
    // ============= SAND ================
    private String match;                   // Match ID (e.g., Qualifying) and '00' - match #)
    private String team_num;                // Team Number (e.g., '5414')
                                            // *** Pre-Game **
    private boolean pre_PowerCell;              // Do they carry PowerCell
    private boolean pre_panel;              // Do they carry a panel
    private String  pre_startPos;           // Start Position
    private int     pre_PlayerSta;          // Player Station (1-3)

                                            // ---- AFTER Start ----
    private boolean sand_mode;              // Do they have Autonomous mode?
    private boolean sand_leftHAB;           // Did they leave HAB
    private boolean sand_leftHAB2;          // Did they start from Hab level 2
    private boolean sand_PU2ndPanel;        // Second game piece - Panel
    private boolean sand_PU2ndPowerCell;        // Second game piece - PowerCell
    private boolean sand_PU2ndPlSta;        // 2nd from Player Station
    private boolean sand_PU2ndCorral;       // 2nd from Corral
    private boolean sand_PU2ndFloor;        // 2nd from Floor
    private boolean sand_PU3rdPanel;        // Second game piece - Panel
    private boolean sand_PU3rdPowerCell;        // Second game piece - PowerCell
    private boolean sand_PU3rdPlSta;        // 3rd from Player Station
    private boolean sand_PU3rdCorral;       // 3rd from Corral
    private boolean sand_PU3rdFloor;        // 3rd from Floor
    private int     sand_num_Dropped;       // How many Panels dropped?
    private String  sand_comment;           // Auto comment

    private boolean sand_LeftRocket_LPan1;  // L-Rocket L-Panel#1
    private boolean sand_LeftRocket_LPan2;  // L-Rocket L-Panel#2
    private boolean sand_LeftRocket_LPan3;  // L-Rocket L-Panel#3
    private boolean sand_LeftRocket_RPan1;  // L-Rocket R-Panel#1
    private boolean sand_LeftRocket_RPan2;  // L-Rocket R-Panel#2
    private boolean sand_LeftRocket_RPan3;  // L-Rocket R-Panel#3
    private boolean sand_LeftRocket_LCarg1; // L-Rocket L-PowerCell#1
    private boolean sand_LeftRocket_LCarg2; // L-Rocket L-PowerCell#2
    private boolean sand_LeftRocket_LCarg3; // L-Rocket L-PowerCell#3
    private boolean sand_LeftRocket_RCarg1; // L-Rocket R-PowerCell#1
    private boolean sand_LeftRocket_RCarg2; // L-Rocket R-PowerCell#2
    private boolean sand_LeftRocket_RCarg3; // L-Rocket R-PowerCell#3

    private boolean sand_PowerCellLPan1;        // PowerCell L-Panel#1
    private boolean sand_PowerCellLPan2;        // PowerCell L-Panel#2
    private boolean sand_PowerCellLPan3;        // PowerCell L-Panel#3
    private boolean sand_PowerCellRPan1;        // PowerCell R-Panel#1
    private boolean sand_PowerCellRPan2;        // PowerCell R-Panel#2
    private boolean sand_PowerCellRPan3;        // PowerCell R-Panel#3
    private boolean sand_PowerCellLCarg1;       // PowerCell L-PowerCell#1
    private boolean sand_PowerCellLCarg2;       // PowerCell L-PowerCell#2
    private boolean sand_PowerCellLCarg3;       // PowerCell L-PowerCell#3
    private boolean sand_PowerCellRCarg1;       // PowerCell R-PowerCell#1
    private boolean sand_PowerCellRCarg2;       // PowerCell R-PowerCell#2
    private boolean sand_PowerCellRCarg3;       // PowerCell R-PowerCell#3
    private boolean sand_PowerCellEndLPanel;    // PowerCell End L-Panel#1
    private boolean sand_PowerCellEndLPowerCell;    // PowerCell End L-PowerCell#1
    private boolean sand_PowerCellEndRPanel;    // PowerCell End R-Panel#1
    private boolean sand_PowerCellEndRPowerCell;    // PowerCell End R-PowerCell#1

    private boolean sand_RghtRocket_LPan1;  // R-Rocket L-Panel#1
    private boolean sand_RghtRocket_LPan2;  // R-Rocket L-Panel#2
    private boolean sand_RghtRocket_LPan3;  // R-Rocket L-Panel#3
    private boolean sand_RghtRocket_RPan1;  // R-Rocket R-Panel#1
    private boolean sand_RghtRocket_RPan2;  // R-Rocket R-Panel#2
    private boolean sand_RghtRocket_RPan3;  // R-Rocket R-Panel#3
    private boolean sand_RghtRocket_LCarg1; // R-Rocket L-PowerCell#1
    private boolean sand_RghtRocket_LCarg2; // R-Rocket L-PowerCell#2
    private boolean sand_RghtRocket_LCarg3; // R-Rocket L-PowerCell#3
    private boolean sand_RghtRocket_RCarg1; // R-Rocket R-PowerCell#1
    private boolean sand_RghtRocket_RCarg2; // R-Rocket R-PowerCell#2
    private boolean sand_RghtRocket_RCarg3; // R-Rocket R-PowerCell#3

    // ============== TELE =================
    private boolean tele_LeftRocket_LPan1;  // L-Rocket L-Panel#1
    private boolean tele_LeftRocket_LPan2;  // L-Rocket L-Panel#2
    private boolean tele_LeftRocket_LPan3;  // L-Rocket L-Panel#3
    private boolean tele_LeftRocket_RPan1;  // L-Rocket R-Panel#1
    private boolean tele_LeftRocket_RPan2;  // L-Rocket R-Panel#2
    private boolean tele_LeftRocket_RPan3;  // L-Rocket R-Panel#3
    private boolean tele_LeftRocket_LCarg1; // L-Rocket L-PowerCell#1
    private boolean tele_LeftRocket_LCarg2; // L-Rocket L-PowerCell#2
    private boolean tele_LeftRocket_LCarg3; // L-Rocket L-PowerCell#3
    private boolean tele_LeftRocket_RCarg1; // L-Rocket R-PowerCell#1
    private boolean tele_LeftRocket_RCarg2; // L-Rocket R-PowerCell#2
    private boolean tele_LeftRocket_RCarg3; // L-Rocket R-PowerCell#3

    private boolean tele_PowerCellLPan1;        // PowerCell L-Panel#1
    private boolean tele_PowerCellLPan2;        // PowerCell L-Panel#2
    private boolean tele_PowerCellLPan3;        // PowerCell L-Panel#3
    private boolean tele_PowerCellRPan1;        // PowerCell R-Panel#1
    private boolean tele_PowerCellRPan2;        // PowerCell R-Panel#2
    private boolean tele_PowerCellRPan3;        // PowerCell R-Panel#3
    private boolean tele_PowerCellLCarg1;       // PowerCell L-PowerCell#1
    private boolean tele_PowerCellLCarg2;       // PowerCell L-PowerCell#2
    private boolean tele_PowerCellLCarg3;       // PowerCell L-PowerCell#3
    private boolean tele_PowerCellRCarg1;       // PowerCell R-PowerCell#1
    private boolean tele_PowerCellRCarg2;       // PowerCell R-PowerCell#2
    private boolean tele_PowerCellRCarg3;       // PowerCell R-PowerCell#3
    private boolean tele_PowerCellEndLPanel;    // PowerCell End L-Panel#1
    private boolean tele_PowerCellEndLPowerCell;    // PowerCell End L-PowerCell#1
    private boolean tele_PowerCellEndRPanel;    // PowerCell End R-Panel#1
    private boolean tele_PowerCellEndRPowerCell;    // PowerCell End R-PowerCell#1

    private boolean tele_RghtRocket_LPan1;  // R-Rocket L-Panel#1
    private boolean tele_RghtRocket_LPan2;  // R-Rocket L-Panel#2
    private boolean tele_RghtRocket_LPan3;  // R-Rocket L-Panel#3
    private boolean tele_RghtRocket_RPan1;  // R-Rocket R-Panel#1
    private boolean tele_RghtRocket_RPan2;  // R-Rocket R-Panel#2
    private boolean tele_RghtRocket_RPan3;  // R-Rocket R-Panel#3
    private boolean tele_RghtRocket_LCarg1; // R-Rocket L-PowerCell#1
    private boolean tele_RghtRocket_LCarg2; // R-Rocket L-PowerCell#2
    private boolean tele_RghtRocket_LCarg3; // R-Rocket L-PowerCell#3
    private boolean tele_RghtRocket_RCarg1; // R-Rocket R-PowerCell#1
    private boolean tele_RghtRocket_RCarg2; // R-Rocket R-PowerCell#2
    private boolean tele_RghtRocket_RCarg3; // R-Rocket R-PowerCell#3

    private boolean tele_PowerCell_floor;       // Did they pick up PowerCell from floor
    private boolean tele_PowerCell_playSta;     // Did they pick up PowerCell from Player Station
    private boolean tele_PowerCell_Corral;      // Did they pick up PowerCell from Corral
    private boolean tele_Panel_floor;       // Did they pick up Panel(s) from floor
    private boolean tele_Panel_playSta;     // Did they pick up Panel(s) from Player Station
    private boolean tele_got_lift;          // Did they get lifted to higher HAB Level
    private boolean tele_lifted;            // Did they lift a robot to higher HAB Level
    private int tele_liftedNum;             // How many lifted?
    private int     tele_level_num;         // Ended on What HAB Level (0-3)
    private int     tele_num_Penalties;     // How many penalties received?
    private int     tele_num_Dropped;       // How many Panels dropped?
    private String  tele_comment;           // Tele comment

    // ============= Final  ================
    private boolean final_lostParts;         // Did they lose parts
    private boolean final_lostComms;         // Did they lose communication
    private boolean final_puPowerCellDef;        // Did they P/U PowerCell on Defense
    private boolean final_defLast30;         // Did they play Defense in Last 30 seconds?
    private boolean final_defense_good;      // Was their overall Defense Good (bad=false)
    private boolean final_def_Block;         // Did they use Blocking Defense
    private boolean final_def_RocketInt;     // Did they block the Rocket
    /*=============================================================================*/
    private String  final_comment;           // Final comment
    private String  final_studID;            // Student doing the scouting
    private String  final_dateTime;          // Date & Time data was saved

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//  Constructor

    public matchData(String match, String team_num, boolean pre_PowerCell, boolean pre_panel, String pre_startPos, int pre_PlayerSta, boolean sand_mode, boolean sand_leftHAB, boolean sand_leftHAB2, boolean sand_PU2ndPanel, boolean sand_PU2ndPowerCell, boolean sand_PU2ndPlSta, boolean sand_PU2ndCorral, boolean sand_PU2ndFloor, boolean sand_PU3rdPanel, boolean sand_PU3rdPowerCell, boolean sand_PU3rdPlSta, boolean sand_PU3rdCorral, boolean sand_PU3rdFloor, int sand_num_Dropped, String sand_comment, boolean sand_LeftRocket_LPan1, boolean sand_LeftRocket_LPan2, boolean sand_LeftRocket_LPan3, boolean sand_LeftRocket_RPan1, boolean sand_LeftRocket_RPan2, boolean sand_LeftRocket_RPan3, boolean sand_LeftRocket_LCarg1, boolean sand_LeftRocket_LCarg2, boolean sand_LeftRocket_LCarg3, boolean sand_LeftRocket_RCarg1, boolean sand_LeftRocket_RCarg2, boolean sand_LeftRocket_RCarg3, boolean sand_PowerCellLPan1, boolean sand_PowerCellLPan2, boolean sand_PowerCellLPan3, boolean sand_PowerCellRPan1, boolean sand_PowerCellRPan2, boolean sand_PowerCellRPan3, boolean sand_PowerCellLCarg1, boolean sand_PowerCellLCarg2, boolean sand_PowerCellLCarg3, boolean sand_PowerCellRCarg1, boolean sand_PowerCellRCarg2, boolean sand_PowerCellRCarg3, boolean sand_PowerCellEndLPanel, boolean sand_PowerCellEndLPowerCell, boolean sand_PowerCellEndRPanel, boolean sand_PowerCellEndRPowerCell, boolean sand_RghtRocket_LPan1, boolean sand_RghtRocket_LPan2, boolean sand_RghtRocket_LPan3, boolean sand_RghtRocket_RPan1, boolean sand_RghtRocket_RPan2, boolean sand_RghtRocket_RPan3, boolean sand_RghtRocket_LCarg1, boolean sand_RghtRocket_LCarg2, boolean sand_RghtRocket_LCarg3, boolean sand_RghtRocket_RCarg1, boolean sand_RghtRocket_RCarg2, boolean sand_RghtRocket_RCarg3, boolean tele_LeftRocket_LPan1, boolean tele_LeftRocket_LPan2, boolean tele_LeftRocket_LPan3, boolean tele_LeftRocket_RPan1, boolean tele_LeftRocket_RPan2, boolean tele_LeftRocket_RPan3, boolean tele_LeftRocket_LCarg1, boolean tele_LeftRocket_LCarg2, boolean tele_LeftRocket_LCarg3, boolean tele_LeftRocket_RCarg1, boolean tele_LeftRocket_RCarg2, boolean tele_LeftRocket_RCarg3, boolean tele_PowerCellLPan1, boolean tele_PowerCellLPan2, boolean tele_PowerCellLPan3, boolean tele_PowerCellRPan1, boolean tele_PowerCellRPan2, boolean tele_PowerCellRPan3, boolean tele_PowerCellLCarg1, boolean tele_PowerCellLCarg2, boolean tele_PowerCellLCarg3, boolean tele_PowerCellRCarg1, boolean tele_PowerCellRCarg2, boolean tele_PowerCellRCarg3, boolean tele_PowerCellEndLPanel, boolean tele_PowerCellEndLPowerCell, boolean tele_PowerCellEndRPanel, boolean tele_PowerCellEndRPowerCell, boolean tele_RghtRocket_LPan1, boolean tele_RghtRocket_LPan2, boolean tele_RghtRocket_LPan3, boolean tele_RghtRocket_RPan1, boolean tele_RghtRocket_RPan2, boolean tele_RghtRocket_RPan3, boolean tele_RghtRocket_LCarg1, boolean tele_RghtRocket_LCarg2, boolean tele_RghtRocket_LCarg3, boolean tele_RghtRocket_RCarg1, boolean tele_RghtRocket_RCarg2, boolean tele_RghtRocket_RCarg3, boolean tele_PowerCell_floor, boolean tele_PowerCell_playSta, boolean tele_PowerCell_Corral, boolean tele_Panel_floor, boolean tele_Panel_playSta, boolean tele_got_lift, boolean tele_lifted, int tele_liftedNum, int tele_level_num, int tele_num_Penalties, int tele_num_Dropped, String tele_comment, boolean final_lostParts, boolean final_lostComms, boolean final_puPowerCellDef, boolean final_defLast30, boolean final_defense_good, boolean final_def_Block, boolean final_def_RocketInt, String final_comment, String final_studID, String final_dateTime) {
        this.match = match;
        this.team_num = team_num;
        this.pre_PowerCell = pre_PowerCell;
        this.pre_panel = pre_panel;
        this.pre_startPos = pre_startPos;
        this.pre_PlayerSta = pre_PlayerSta;
        this.sand_mode = sand_mode;
        this.sand_leftHAB = sand_leftHAB;
        this.sand_leftHAB2 = sand_leftHAB2;
        this.sand_PU2ndPanel = sand_PU2ndPanel;
        this.sand_PU2ndPowerCell = sand_PU2ndPowerCell;
        this.sand_PU2ndPlSta = sand_PU2ndPlSta;
        this.sand_PU2ndCorral = sand_PU2ndCorral;
        this.sand_PU2ndFloor = sand_PU2ndFloor;
        this.sand_PU3rdPanel = sand_PU3rdPanel;
        this.sand_PU3rdPowerCell = sand_PU3rdPowerCell;
        this.sand_PU3rdPlSta = sand_PU3rdPlSta;
        this.sand_PU3rdCorral = sand_PU3rdCorral;
        this.sand_PU3rdFloor = sand_PU3rdFloor;
        this.sand_num_Dropped = sand_num_Dropped;
        this.sand_comment = sand_comment;
        this.sand_LeftRocket_LPan1 = sand_LeftRocket_LPan1;
        this.sand_LeftRocket_LPan2 = sand_LeftRocket_LPan2;
        this.sand_LeftRocket_LPan3 = sand_LeftRocket_LPan3;
        this.sand_LeftRocket_RPan1 = sand_LeftRocket_RPan1;
        this.sand_LeftRocket_RPan2 = sand_LeftRocket_RPan2;
        this.sand_LeftRocket_RPan3 = sand_LeftRocket_RPan3;
        this.sand_LeftRocket_LCarg1 = sand_LeftRocket_LCarg1;
        this.sand_LeftRocket_LCarg2 = sand_LeftRocket_LCarg2;
        this.sand_LeftRocket_LCarg3 = sand_LeftRocket_LCarg3;
        this.sand_LeftRocket_RCarg1 = sand_LeftRocket_RCarg1;
        this.sand_LeftRocket_RCarg2 = sand_LeftRocket_RCarg2;
        this.sand_LeftRocket_RCarg3 = sand_LeftRocket_RCarg3;
        this.sand_PowerCellLPan1 = sand_PowerCellLPan1;
        this.sand_PowerCellLPan2 = sand_PowerCellLPan2;
        this.sand_PowerCellLPan3 = sand_PowerCellLPan3;
        this.sand_PowerCellRPan1 = sand_PowerCellRPan1;
        this.sand_PowerCellRPan2 = sand_PowerCellRPan2;
        this.sand_PowerCellRPan3 = sand_PowerCellRPan3;
        this.sand_PowerCellLCarg1 = sand_PowerCellLCarg1;
        this.sand_PowerCellLCarg2 = sand_PowerCellLCarg2;
        this.sand_PowerCellLCarg3 = sand_PowerCellLCarg3;
        this.sand_PowerCellRCarg1 = sand_PowerCellRCarg1;
        this.sand_PowerCellRCarg2 = sand_PowerCellRCarg2;
        this.sand_PowerCellRCarg3 = sand_PowerCellRCarg3;
        this.sand_PowerCellEndLPanel = sand_PowerCellEndLPanel;
        this.sand_PowerCellEndLPowerCell = sand_PowerCellEndLPowerCell;
        this.sand_PowerCellEndRPanel = sand_PowerCellEndRPanel;
        this.sand_PowerCellEndRPowerCell = sand_PowerCellEndRPowerCell;
        this.sand_RghtRocket_LPan1 = sand_RghtRocket_LPan1;
        this.sand_RghtRocket_LPan2 = sand_RghtRocket_LPan2;
        this.sand_RghtRocket_LPan3 = sand_RghtRocket_LPan3;
        this.sand_RghtRocket_RPan1 = sand_RghtRocket_RPan1;
        this.sand_RghtRocket_RPan2 = sand_RghtRocket_RPan2;
        this.sand_RghtRocket_RPan3 = sand_RghtRocket_RPan3;
        this.sand_RghtRocket_LCarg1 = sand_RghtRocket_LCarg1;
        this.sand_RghtRocket_LCarg2 = sand_RghtRocket_LCarg2;
        this.sand_RghtRocket_LCarg3 = sand_RghtRocket_LCarg3;
        this.sand_RghtRocket_RCarg1 = sand_RghtRocket_RCarg1;
        this.sand_RghtRocket_RCarg2 = sand_RghtRocket_RCarg2;
        this.sand_RghtRocket_RCarg3 = sand_RghtRocket_RCarg3;
        this.tele_LeftRocket_LPan1 = tele_LeftRocket_LPan1;
        this.tele_LeftRocket_LPan2 = tele_LeftRocket_LPan2;
        this.tele_LeftRocket_LPan3 = tele_LeftRocket_LPan3;
        this.tele_LeftRocket_RPan1 = tele_LeftRocket_RPan1;
        this.tele_LeftRocket_RPan2 = tele_LeftRocket_RPan2;
        this.tele_LeftRocket_RPan3 = tele_LeftRocket_RPan3;
        this.tele_LeftRocket_LCarg1 = tele_LeftRocket_LCarg1;
        this.tele_LeftRocket_LCarg2 = tele_LeftRocket_LCarg2;
        this.tele_LeftRocket_LCarg3 = tele_LeftRocket_LCarg3;
        this.tele_LeftRocket_RCarg1 = tele_LeftRocket_RCarg1;
        this.tele_LeftRocket_RCarg2 = tele_LeftRocket_RCarg2;
        this.tele_LeftRocket_RCarg3 = tele_LeftRocket_RCarg3;
        this.tele_PowerCellLPan1 = tele_PowerCellLPan1;
        this.tele_PowerCellLPan2 = tele_PowerCellLPan2;
        this.tele_PowerCellLPan3 = tele_PowerCellLPan3;
        this.tele_PowerCellRPan1 = tele_PowerCellRPan1;
        this.tele_PowerCellRPan2 = tele_PowerCellRPan2;
        this.tele_PowerCellRPan3 = tele_PowerCellRPan3;
        this.tele_PowerCellLCarg1 = tele_PowerCellLCarg1;
        this.tele_PowerCellLCarg2 = tele_PowerCellLCarg2;
        this.tele_PowerCellLCarg3 = tele_PowerCellLCarg3;
        this.tele_PowerCellRCarg1 = tele_PowerCellRCarg1;
        this.tele_PowerCellRCarg2 = tele_PowerCellRCarg2;
        this.tele_PowerCellRCarg3 = tele_PowerCellRCarg3;
        this.tele_PowerCellEndLPanel = tele_PowerCellEndLPanel;
        this.tele_PowerCellEndLPowerCell = tele_PowerCellEndLPowerCell;
        this.tele_PowerCellEndRPanel = tele_PowerCellEndRPanel;
        this.tele_PowerCellEndRPowerCell = tele_PowerCellEndRPowerCell;
        this.tele_RghtRocket_LPan1 = tele_RghtRocket_LPan1;
        this.tele_RghtRocket_LPan2 = tele_RghtRocket_LPan2;
        this.tele_RghtRocket_LPan3 = tele_RghtRocket_LPan3;
        this.tele_RghtRocket_RPan1 = tele_RghtRocket_RPan1;
        this.tele_RghtRocket_RPan2 = tele_RghtRocket_RPan2;
        this.tele_RghtRocket_RPan3 = tele_RghtRocket_RPan3;
        this.tele_RghtRocket_LCarg1 = tele_RghtRocket_LCarg1;
        this.tele_RghtRocket_LCarg2 = tele_RghtRocket_LCarg2;
        this.tele_RghtRocket_LCarg3 = tele_RghtRocket_LCarg3;
        this.tele_RghtRocket_RCarg1 = tele_RghtRocket_RCarg1;
        this.tele_RghtRocket_RCarg2 = tele_RghtRocket_RCarg2;
        this.tele_RghtRocket_RCarg3 = tele_RghtRocket_RCarg3;
        this.tele_PowerCell_floor = tele_PowerCell_floor;
        this.tele_PowerCell_playSta = tele_PowerCell_playSta;
        this.tele_PowerCell_Corral = tele_PowerCell_Corral;
        this.tele_Panel_floor = tele_Panel_floor;
        this.tele_Panel_playSta = tele_Panel_playSta;
        this.tele_got_lift = tele_got_lift;
        this.tele_lifted = tele_lifted;
        this.tele_liftedNum = tele_liftedNum;
        this.tele_level_num = tele_level_num;
        this.tele_num_Penalties = tele_num_Penalties;
        this.tele_num_Dropped = tele_num_Dropped;
        this.tele_comment = tele_comment;
        this.final_lostParts = final_lostParts;
        this.final_lostComms = final_lostComms;
        this.final_puPowerCellDef = final_puPowerCellDef;
        this.final_defLast30 = final_defLast30;
        this.final_defense_good = final_defense_good;
        this.final_def_Block = final_def_Block;
        this.final_def_RocketInt = final_def_RocketInt;
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

    public boolean isPre_PowerCell() {
        return pre_PowerCell;
    }

    public void setPre_PowerCell(boolean pre_PowerCell) {
        this.pre_PowerCell = pre_PowerCell;
    }

    public boolean isPre_panel() {
        return pre_panel;
    }

    public void setPre_panel(boolean pre_panel) {
        this.pre_panel = pre_panel;
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

    public boolean isSand_mode() {
        return sand_mode;
    }

    public void setSand_mode(boolean sand_mode) {
        this.sand_mode = sand_mode;
    }

    public boolean isSand_leftHAB() {
        return sand_leftHAB;
    }

    public void setSand_leftHAB(boolean sand_leftHAB) {
        this.sand_leftHAB = sand_leftHAB;
    }

    public boolean isSand_leftHAB2() {
        return sand_leftHAB2;
    }

    public void setSand_leftHAB2(boolean sand_leftHAB2) {
        this.sand_leftHAB2 = sand_leftHAB2;
    }

    public boolean isSand_PU2ndPanel() {
        return sand_PU2ndPanel;
    }

    public void setSand_PU2ndPanel(boolean sand_PU2ndPanel) {
        this.sand_PU2ndPanel = sand_PU2ndPanel;
    }

    public boolean isSand_PU2ndPowerCell() {
        return sand_PU2ndPowerCell;
    }

    public void setSand_PU2ndPowerCell(boolean sand_PU2ndPowerCell) {
        this.sand_PU2ndPowerCell = sand_PU2ndPowerCell;
    }

    public boolean isSand_PU2ndPlSta() {
        return sand_PU2ndPlSta;
    }

    public void setSand_PU2ndPlSta(boolean sand_PU2ndPlSta) {
        this.sand_PU2ndPlSta = sand_PU2ndPlSta;
    }

    public boolean isSand_PU2ndCorral() {
        return sand_PU2ndCorral;
    }

    public void setSand_PU2ndCorral(boolean sand_PU2ndCorral) {
        this.sand_PU2ndCorral = sand_PU2ndCorral;
    }

    public boolean isSand_PU2ndFloor() {
        return sand_PU2ndFloor;
    }

    public void setSand_PU2ndFloor(boolean sand_PU2ndFloor) {
        this.sand_PU2ndFloor = sand_PU2ndFloor;
    }

    public boolean isSand_PU3rdPanel() {
        return sand_PU3rdPanel;
    }

    public void setSand_PU3rdPanel(boolean sand_PU3rdPanel) {
        this.sand_PU3rdPanel = sand_PU3rdPanel;
    }

    public boolean isSand_PU3rdPowerCell() {
        return sand_PU3rdPowerCell;
    }

    public void setSand_PU3rdPowerCell(boolean sand_PU3rdPowerCell) {
        this.sand_PU3rdPowerCell = sand_PU3rdPowerCell;
    }

    public boolean isSand_PU3rdPlSta() {
        return sand_PU3rdPlSta;
    }

    public void setSand_PU3rdPlSta(boolean sand_PU3rdPlSta) {
        this.sand_PU3rdPlSta = sand_PU3rdPlSta;
    }

    public boolean isSand_PU3rdCorral() {
        return sand_PU3rdCorral;
    }

    public void setSand_PU3rdCorral(boolean sand_PU3rdCorral) {
        this.sand_PU3rdCorral = sand_PU3rdCorral;
    }

    public boolean isSand_PU3rdFloor() {
        return sand_PU3rdFloor;
    }

    public void setSand_PU3rdFloor(boolean sand_PU3rdFloor) {
        this.sand_PU3rdFloor = sand_PU3rdFloor;
    }

    public int getSand_num_Dropped() {
        return sand_num_Dropped;
    }

    public void setSand_num_Dropped(int sand_num_Dropped) {
        this.sand_num_Dropped = sand_num_Dropped;
    }

    public String getSand_comment() {
        return sand_comment;
    }

    public void setSand_comment(String sand_comment) {
        this.sand_comment = sand_comment;
    }

    public boolean isSand_LeftRocket_LPan1() {
        return sand_LeftRocket_LPan1;
    }

    public void setSand_LeftRocket_LPan1(boolean sand_LeftRocket_LPan1) {
        this.sand_LeftRocket_LPan1 = sand_LeftRocket_LPan1;
    }

    public boolean isSand_LeftRocket_LPan2() {
        return sand_LeftRocket_LPan2;
    }

    public void setSand_LeftRocket_LPan2(boolean sand_LeftRocket_LPan2) {
        this.sand_LeftRocket_LPan2 = sand_LeftRocket_LPan2;
    }

    public boolean isSand_LeftRocket_LPan3() {
        return sand_LeftRocket_LPan3;
    }

    public void setSand_LeftRocket_LPan3(boolean sand_LeftRocket_LPan3) {
        this.sand_LeftRocket_LPan3 = sand_LeftRocket_LPan3;
    }

    public boolean isSand_LeftRocket_RPan1() {
        return sand_LeftRocket_RPan1;
    }

    public void setSand_LeftRocket_RPan1(boolean sand_LeftRocket_RPan1) {
        this.sand_LeftRocket_RPan1 = sand_LeftRocket_RPan1;
    }

    public boolean isSand_LeftRocket_RPan2() {
        return sand_LeftRocket_RPan2;
    }

    public void setSand_LeftRocket_RPan2(boolean sand_LeftRocket_RPan2) {
        this.sand_LeftRocket_RPan2 = sand_LeftRocket_RPan2;
    }

    public boolean isSand_LeftRocket_RPan3() {
        return sand_LeftRocket_RPan3;
    }

    public void setSand_LeftRocket_RPan3(boolean sand_LeftRocket_RPan3) {
        this.sand_LeftRocket_RPan3 = sand_LeftRocket_RPan3;
    }

    public boolean isSand_LeftRocket_LCarg1() {
        return sand_LeftRocket_LCarg1;
    }

    public void setSand_LeftRocket_LCarg1(boolean sand_LeftRocket_LCarg1) {
        this.sand_LeftRocket_LCarg1 = sand_LeftRocket_LCarg1;
    }

    public boolean isSand_LeftRocket_LCarg2() {
        return sand_LeftRocket_LCarg2;
    }

    public void setSand_LeftRocket_LCarg2(boolean sand_LeftRocket_LCarg2) {
        this.sand_LeftRocket_LCarg2 = sand_LeftRocket_LCarg2;
    }

    public boolean isSand_LeftRocket_LCarg3() {
        return sand_LeftRocket_LCarg3;
    }

    public void setSand_LeftRocket_LCarg3(boolean sand_LeftRocket_LCarg3) {
        this.sand_LeftRocket_LCarg3 = sand_LeftRocket_LCarg3;
    }

    public boolean isSand_LeftRocket_RCarg1() {
        return sand_LeftRocket_RCarg1;
    }

    public void setSand_LeftRocket_RCarg1(boolean sand_LeftRocket_RCarg1) {
        this.sand_LeftRocket_RCarg1 = sand_LeftRocket_RCarg1;
    }

    public boolean isSand_LeftRocket_RCarg2() {
        return sand_LeftRocket_RCarg2;
    }

    public void setSand_LeftRocket_RCarg2(boolean sand_LeftRocket_RCarg2) {
        this.sand_LeftRocket_RCarg2 = sand_LeftRocket_RCarg2;
    }

    public boolean isSand_LeftRocket_RCarg3() {
        return sand_LeftRocket_RCarg3;
    }

    public void setSand_LeftRocket_RCarg3(boolean sand_LeftRocket_RCarg3) {
        this.sand_LeftRocket_RCarg3 = sand_LeftRocket_RCarg3;
    }

    public boolean isSand_PowerCellLPan1() {
        return sand_PowerCellLPan1;
    }

    public void setSand_PowerCellLPan1(boolean sand_PowerCellLPan1) {
        this.sand_PowerCellLPan1 = sand_PowerCellLPan1;
    }

    public boolean isSand_PowerCellLPan2() {
        return sand_PowerCellLPan2;
    }

    public void setSand_PowerCellLPan2(boolean sand_PowerCellLPan2) {
        this.sand_PowerCellLPan2 = sand_PowerCellLPan2;
    }

    public boolean isSand_PowerCellLPan3() {
        return sand_PowerCellLPan3;
    }

    public void setSand_PowerCellLPan3(boolean sand_PowerCellLPan3) {
        this.sand_PowerCellLPan3 = sand_PowerCellLPan3;
    }

    public boolean isSand_PowerCellRPan1() {
        return sand_PowerCellRPan1;
    }

    public void setSand_PowerCellRPan1(boolean sand_PowerCellRPan1) {
        this.sand_PowerCellRPan1 = sand_PowerCellRPan1;
    }

    public boolean isSand_PowerCellRPan2() {
        return sand_PowerCellRPan2;
    }

    public void setSand_PowerCellRPan2(boolean sand_PowerCellRPan2) {
        this.sand_PowerCellRPan2 = sand_PowerCellRPan2;
    }

    public boolean isSand_PowerCellRPan3() {
        return sand_PowerCellRPan3;
    }

    public void setSand_PowerCellRPan3(boolean sand_PowerCellRPan3) {
        this.sand_PowerCellRPan3 = sand_PowerCellRPan3;
    }

    public boolean isSand_PowerCellLCarg1() {
        return sand_PowerCellLCarg1;
    }

    public void setSand_PowerCellLCarg1(boolean sand_PowerCellLCarg1) {
        this.sand_PowerCellLCarg1 = sand_PowerCellLCarg1;
    }

    public boolean isSand_PowerCellLCarg2() {
        return sand_PowerCellLCarg2;
    }

    public void setSand_PowerCellLCarg2(boolean sand_PowerCellLCarg2) {
        this.sand_PowerCellLCarg2 = sand_PowerCellLCarg2;
    }

    public boolean isSand_PowerCellLCarg3() {
        return sand_PowerCellLCarg3;
    }

    public void setSand_PowerCellLCarg3(boolean sand_PowerCellLCarg3) {
        this.sand_PowerCellLCarg3 = sand_PowerCellLCarg3;
    }

    public boolean isSand_PowerCellRCarg1() {
        return sand_PowerCellRCarg1;
    }

    public void setSand_PowerCellRCarg1(boolean sand_PowerCellRCarg1) {
        this.sand_PowerCellRCarg1 = sand_PowerCellRCarg1;
    }

    public boolean isSand_PowerCellRCarg2() {
        return sand_PowerCellRCarg2;
    }

    public void setSand_PowerCellRCarg2(boolean sand_PowerCellRCarg2) {
        this.sand_PowerCellRCarg2 = sand_PowerCellRCarg2;
    }

    public boolean isSand_PowerCellRCarg3() {
        return sand_PowerCellRCarg3;
    }

    public void setSand_PowerCellRCarg3(boolean sand_PowerCellRCarg3) {
        this.sand_PowerCellRCarg3 = sand_PowerCellRCarg3;
    }

    public boolean isSand_PowerCellEndLPanel() {
        return sand_PowerCellEndLPanel;
    }

    public void setSand_PowerCellEndLPanel(boolean sand_PowerCellEndLPanel) {
        this.sand_PowerCellEndLPanel = sand_PowerCellEndLPanel;
    }

    public boolean isSand_PowerCellEndLPowerCell() {
        return sand_PowerCellEndLPowerCell;
    }

    public void setSand_PowerCellEndLPowerCell(boolean sand_PowerCellEndLPowerCell) {
        this.sand_PowerCellEndLPowerCell = sand_PowerCellEndLPowerCell;
    }

    public boolean isSand_PowerCellEndRPanel() {
        return sand_PowerCellEndRPanel;
    }

    public void setSand_PowerCellEndRPanel(boolean sand_PowerCellEndRPanel) {
        this.sand_PowerCellEndRPanel = sand_PowerCellEndRPanel;
    }

    public boolean isSand_PowerCellEndRPowerCell() {
        return sand_PowerCellEndRPowerCell;
    }

    public void setSand_PowerCellEndRPowerCell(boolean sand_PowerCellEndRPowerCell) {
        this.sand_PowerCellEndRPowerCell = sand_PowerCellEndRPowerCell;
    }

    public boolean isSand_RghtRocket_LPan1() {
        return sand_RghtRocket_LPan1;
    }

    public void setSand_RghtRocket_LPan1(boolean sand_RghtRocket_LPan1) {
        this.sand_RghtRocket_LPan1 = sand_RghtRocket_LPan1;
    }

    public boolean isSand_RghtRocket_LPan2() {
        return sand_RghtRocket_LPan2;
    }

    public void setSand_RghtRocket_LPan2(boolean sand_RghtRocket_LPan2) {
        this.sand_RghtRocket_LPan2 = sand_RghtRocket_LPan2;
    }

    public boolean isSand_RghtRocket_LPan3() {
        return sand_RghtRocket_LPan3;
    }

    public void setSand_RghtRocket_LPan3(boolean sand_RghtRocket_LPan3) {
        this.sand_RghtRocket_LPan3 = sand_RghtRocket_LPan3;
    }

    public boolean isSand_RghtRocket_RPan1() {
        return sand_RghtRocket_RPan1;
    }

    public void setSand_RghtRocket_RPan1(boolean sand_RghtRocket_RPan1) {
        this.sand_RghtRocket_RPan1 = sand_RghtRocket_RPan1;
    }

    public boolean isSand_RghtRocket_RPan2() {
        return sand_RghtRocket_RPan2;
    }

    public void setSand_RghtRocket_RPan2(boolean sand_RghtRocket_RPan2) {
        this.sand_RghtRocket_RPan2 = sand_RghtRocket_RPan2;
    }

    public boolean isSand_RghtRocket_RPan3() {
        return sand_RghtRocket_RPan3;
    }

    public void setSand_RghtRocket_RPan3(boolean sand_RghtRocket_RPan3) {
        this.sand_RghtRocket_RPan3 = sand_RghtRocket_RPan3;
    }

    public boolean isSand_RghtRocket_LCarg1() {
        return sand_RghtRocket_LCarg1;
    }

    public void setSand_RghtRocket_LCarg1(boolean sand_RghtRocket_LCarg1) {
        this.sand_RghtRocket_LCarg1 = sand_RghtRocket_LCarg1;
    }

    public boolean isSand_RghtRocket_LCarg2() {
        return sand_RghtRocket_LCarg2;
    }

    public void setSand_RghtRocket_LCarg2(boolean sand_RghtRocket_LCarg2) {
        this.sand_RghtRocket_LCarg2 = sand_RghtRocket_LCarg2;
    }

    public boolean isSand_RghtRocket_LCarg3() {
        return sand_RghtRocket_LCarg3;
    }

    public void setSand_RghtRocket_LCarg3(boolean sand_RghtRocket_LCarg3) {
        this.sand_RghtRocket_LCarg3 = sand_RghtRocket_LCarg3;
    }

    public boolean isSand_RghtRocket_RCarg1() {
        return sand_RghtRocket_RCarg1;
    }

    public void setSand_RghtRocket_RCarg1(boolean sand_RghtRocket_RCarg1) {
        this.sand_RghtRocket_RCarg1 = sand_RghtRocket_RCarg1;
    }

    public boolean isSand_RghtRocket_RCarg2() {
        return sand_RghtRocket_RCarg2;
    }

    public void setSand_RghtRocket_RCarg2(boolean sand_RghtRocket_RCarg2) {
        this.sand_RghtRocket_RCarg2 = sand_RghtRocket_RCarg2;
    }

    public boolean isSand_RghtRocket_RCarg3() {
        return sand_RghtRocket_RCarg3;
    }

    public void setSand_RghtRocket_RCarg3(boolean sand_RghtRocket_RCarg3) {
        this.sand_RghtRocket_RCarg3 = sand_RghtRocket_RCarg3;
    }

    public boolean isTele_LeftRocket_LPan1() {
        return tele_LeftRocket_LPan1;
    }

    public void setTele_LeftRocket_LPan1(boolean tele_LeftRocket_LPan1) {
        this.tele_LeftRocket_LPan1 = tele_LeftRocket_LPan1;
    }

    public boolean isTele_LeftRocket_LPan2() {
        return tele_LeftRocket_LPan2;
    }

    public void setTele_LeftRocket_LPan2(boolean tele_LeftRocket_LPan2) {
        this.tele_LeftRocket_LPan2 = tele_LeftRocket_LPan2;
    }

    public boolean isTele_LeftRocket_LPan3() {
        return tele_LeftRocket_LPan3;
    }

    public void setTele_LeftRocket_LPan3(boolean tele_LeftRocket_LPan3) {
        this.tele_LeftRocket_LPan3 = tele_LeftRocket_LPan3;
    }

    public boolean isTele_LeftRocket_RPan1() {
        return tele_LeftRocket_RPan1;
    }

    public void setTele_LeftRocket_RPan1(boolean tele_LeftRocket_RPan1) {
        this.tele_LeftRocket_RPan1 = tele_LeftRocket_RPan1;
    }

    public boolean isTele_LeftRocket_RPan2() {
        return tele_LeftRocket_RPan2;
    }

    public void setTele_LeftRocket_RPan2(boolean tele_LeftRocket_RPan2) {
        this.tele_LeftRocket_RPan2 = tele_LeftRocket_RPan2;
    }

    public boolean isTele_LeftRocket_RPan3() {
        return tele_LeftRocket_RPan3;
    }

    public void setTele_LeftRocket_RPan3(boolean tele_LeftRocket_RPan3) {
        this.tele_LeftRocket_RPan3 = tele_LeftRocket_RPan3;
    }

    public boolean isTele_LeftRocket_LCarg1() {
        return tele_LeftRocket_LCarg1;
    }

    public void setTele_LeftRocket_LCarg1(boolean tele_LeftRocket_LCarg1) {
        this.tele_LeftRocket_LCarg1 = tele_LeftRocket_LCarg1;
    }

    public boolean isTele_LeftRocket_LCarg2() {
        return tele_LeftRocket_LCarg2;
    }

    public void setTele_LeftRocket_LCarg2(boolean tele_LeftRocket_LCarg2) {
        this.tele_LeftRocket_LCarg2 = tele_LeftRocket_LCarg2;
    }

    public boolean isTele_LeftRocket_LCarg3() {
        return tele_LeftRocket_LCarg3;
    }

    public void setTele_LeftRocket_LCarg3(boolean tele_LeftRocket_LCarg3) {
        this.tele_LeftRocket_LCarg3 = tele_LeftRocket_LCarg3;
    }

    public boolean isTele_LeftRocket_RCarg1() {
        return tele_LeftRocket_RCarg1;
    }

    public void setTele_LeftRocket_RCarg1(boolean tele_LeftRocket_RCarg1) {
        this.tele_LeftRocket_RCarg1 = tele_LeftRocket_RCarg1;
    }

    public boolean isTele_LeftRocket_RCarg2() {
        return tele_LeftRocket_RCarg2;
    }

    public void setTele_LeftRocket_RCarg2(boolean tele_LeftRocket_RCarg2) {
        this.tele_LeftRocket_RCarg2 = tele_LeftRocket_RCarg2;
    }

    public boolean isTele_LeftRocket_RCarg3() {
        return tele_LeftRocket_RCarg3;
    }

    public void setTele_LeftRocket_RCarg3(boolean tele_LeftRocket_RCarg3) {
        this.tele_LeftRocket_RCarg3 = tele_LeftRocket_RCarg3;
    }

    public boolean isTele_PowerCellLPan1() {
        return tele_PowerCellLPan1;
    }

    public void setTele_PowerCellLPan1(boolean tele_PowerCellLPan1) {
        this.tele_PowerCellLPan1 = tele_PowerCellLPan1;
    }

    public boolean isTele_PowerCellLPan2() {
        return tele_PowerCellLPan2;
    }

    public void setTele_PowerCellLPan2(boolean tele_PowerCellLPan2) {
        this.tele_PowerCellLPan2 = tele_PowerCellLPan2;
    }

    public boolean isTele_PowerCellLPan3() {
        return tele_PowerCellLPan3;
    }

    public void setTele_PowerCellLPan3(boolean tele_PowerCellLPan3) {
        this.tele_PowerCellLPan3 = tele_PowerCellLPan3;
    }

    public boolean isTele_PowerCellRPan1() {
        return tele_PowerCellRPan1;
    }

    public void setTele_PowerCellRPan1(boolean tele_PowerCellRPan1) {
        this.tele_PowerCellRPan1 = tele_PowerCellRPan1;
    }

    public boolean isTele_PowerCellRPan2() {
        return tele_PowerCellRPan2;
    }

    public void setTele_PowerCellRPan2(boolean tele_PowerCellRPan2) {
        this.tele_PowerCellRPan2 = tele_PowerCellRPan2;
    }

    public boolean isTele_PowerCellRPan3() {
        return tele_PowerCellRPan3;
    }

    public void setTele_PowerCellRPan3(boolean tele_PowerCellRPan3) {
        this.tele_PowerCellRPan3 = tele_PowerCellRPan3;
    }

    public boolean isTele_PowerCellLCarg1() {
        return tele_PowerCellLCarg1;
    }

    public void setTele_PowerCellLCarg1(boolean tele_PowerCellLCarg1) {
        this.tele_PowerCellLCarg1 = tele_PowerCellLCarg1;
    }

    public boolean isTele_PowerCellLCarg2() {
        return tele_PowerCellLCarg2;
    }

    public void setTele_PowerCellLCarg2(boolean tele_PowerCellLCarg2) {
        this.tele_PowerCellLCarg2 = tele_PowerCellLCarg2;
    }

    public boolean isTele_PowerCellLCarg3() {
        return tele_PowerCellLCarg3;
    }

    public void setTele_PowerCellLCarg3(boolean tele_PowerCellLCarg3) {
        this.tele_PowerCellLCarg3 = tele_PowerCellLCarg3;
    }

    public boolean isTele_PowerCellRCarg1() {
        return tele_PowerCellRCarg1;
    }

    public void setTele_PowerCellRCarg1(boolean tele_PowerCellRCarg1) {
        this.tele_PowerCellRCarg1 = tele_PowerCellRCarg1;
    }

    public boolean isTele_PowerCellRCarg2() {
        return tele_PowerCellRCarg2;
    }

    public void setTele_PowerCellRCarg2(boolean tele_PowerCellRCarg2) {
        this.tele_PowerCellRCarg2 = tele_PowerCellRCarg2;
    }

    public boolean isTele_PowerCellRCarg3() {
        return tele_PowerCellRCarg3;
    }

    public void setTele_PowerCellRCarg3(boolean tele_PowerCellRCarg3) {
        this.tele_PowerCellRCarg3 = tele_PowerCellRCarg3;
    }

    public boolean isTele_PowerCellEndLPanel() {
        return tele_PowerCellEndLPanel;
    }

    public void setTele_PowerCellEndLPanel(boolean tele_PowerCellEndLPanel) {
        this.tele_PowerCellEndLPanel = tele_PowerCellEndLPanel;
    }

    public boolean isTele_PowerCellEndLPowerCell() {
        return tele_PowerCellEndLPowerCell;
    }

    public void setTele_PowerCellEndLPowerCell(boolean tele_PowerCellEndLPowerCell) {
        this.tele_PowerCellEndLPowerCell = tele_PowerCellEndLPowerCell;
    }

    public boolean isTele_PowerCellEndRPanel() {
        return tele_PowerCellEndRPanel;
    }

    public void setTele_PowerCellEndRPanel(boolean tele_PowerCellEndRPanel) {
        this.tele_PowerCellEndRPanel = tele_PowerCellEndRPanel;
    }

    public boolean isTele_PowerCellEndRPowerCell() {
        return tele_PowerCellEndRPowerCell;
    }

    public void setTele_PowerCellEndRPowerCell(boolean tele_PowerCellEndRPowerCell) {
        this.tele_PowerCellEndRPowerCell = tele_PowerCellEndRPowerCell;
    }

    public boolean isTele_RghtRocket_LPan1() {
        return tele_RghtRocket_LPan1;
    }

    public void setTele_RghtRocket_LPan1(boolean tele_RghtRocket_LPan1) {
        this.tele_RghtRocket_LPan1 = tele_RghtRocket_LPan1;
    }

    public boolean isTele_RghtRocket_LPan2() {
        return tele_RghtRocket_LPan2;
    }

    public void setTele_RghtRocket_LPan2(boolean tele_RghtRocket_LPan2) {
        this.tele_RghtRocket_LPan2 = tele_RghtRocket_LPan2;
    }

    public boolean isTele_RghtRocket_LPan3() {
        return tele_RghtRocket_LPan3;
    }

    public void setTele_RghtRocket_LPan3(boolean tele_RghtRocket_LPan3) {
        this.tele_RghtRocket_LPan3 = tele_RghtRocket_LPan3;
    }

    public boolean isTele_RghtRocket_RPan1() {
        return tele_RghtRocket_RPan1;
    }

    public void setTele_RghtRocket_RPan1(boolean tele_RghtRocket_RPan1) {
        this.tele_RghtRocket_RPan1 = tele_RghtRocket_RPan1;
    }

    public boolean isTele_RghtRocket_RPan2() {
        return tele_RghtRocket_RPan2;
    }

    public void setTele_RghtRocket_RPan2(boolean tele_RghtRocket_RPan2) {
        this.tele_RghtRocket_RPan2 = tele_RghtRocket_RPan2;
    }

    public boolean isTele_RghtRocket_RPan3() {
        return tele_RghtRocket_RPan3;
    }

    public void setTele_RghtRocket_RPan3(boolean tele_RghtRocket_RPan3) {
        this.tele_RghtRocket_RPan3 = tele_RghtRocket_RPan3;
    }

    public boolean isTele_RghtRocket_LCarg1() {
        return tele_RghtRocket_LCarg1;
    }

    public void setTele_RghtRocket_LCarg1(boolean tele_RghtRocket_LCarg1) {
        this.tele_RghtRocket_LCarg1 = tele_RghtRocket_LCarg1;
    }

    public boolean isTele_RghtRocket_LCarg2() {
        return tele_RghtRocket_LCarg2;
    }

    public void setTele_RghtRocket_LCarg2(boolean tele_RghtRocket_LCarg2) {
        this.tele_RghtRocket_LCarg2 = tele_RghtRocket_LCarg2;
    }

    public boolean isTele_RghtRocket_LCarg3() {
        return tele_RghtRocket_LCarg3;
    }

    public void setTele_RghtRocket_LCarg3(boolean tele_RghtRocket_LCarg3) {
        this.tele_RghtRocket_LCarg3 = tele_RghtRocket_LCarg3;
    }

    public boolean isTele_RghtRocket_RCarg1() {
        return tele_RghtRocket_RCarg1;
    }

    public void setTele_RghtRocket_RCarg1(boolean tele_RghtRocket_RCarg1) {
        this.tele_RghtRocket_RCarg1 = tele_RghtRocket_RCarg1;
    }

    public boolean isTele_RghtRocket_RCarg2() {
        return tele_RghtRocket_RCarg2;
    }

    public void setTele_RghtRocket_RCarg2(boolean tele_RghtRocket_RCarg2) {
        this.tele_RghtRocket_RCarg2 = tele_RghtRocket_RCarg2;
    }

    public boolean isTele_RghtRocket_RCarg3() {
        return tele_RghtRocket_RCarg3;
    }

    public void setTele_RghtRocket_RCarg3(boolean tele_RghtRocket_RCarg3) {
        this.tele_RghtRocket_RCarg3 = tele_RghtRocket_RCarg3;
    }

    public boolean isTele_PowerCell_floor() {
        return tele_PowerCell_floor;
    }

    public void setTele_PowerCell_floor(boolean tele_PowerCell_floor) {
        this.tele_PowerCell_floor = tele_PowerCell_floor;
    }

    public boolean isTele_PowerCell_playSta() {
        return tele_PowerCell_playSta;
    }

    public void setTele_PowerCell_playSta(boolean tele_PowerCell_playSta) {
        this.tele_PowerCell_playSta = tele_PowerCell_playSta;
    }

    public boolean isTele_PowerCell_Corral() {
        return tele_PowerCell_Corral;
    }

    public void setTele_PowerCell_Corral(boolean tele_PowerCell_Corral) {
        this.tele_PowerCell_Corral = tele_PowerCell_Corral;
    }

    public boolean isTele_Panel_floor() {
        return tele_Panel_floor;
    }

    public void setTele_Panel_floor(boolean tele_Panel_floor) {
        this.tele_Panel_floor = tele_Panel_floor;
    }

    public boolean isTele_Panel_playSta() {
        return tele_Panel_playSta;
    }

    public void setTele_Panel_playSta(boolean tele_Panel_playSta) {
        this.tele_Panel_playSta = tele_Panel_playSta;
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

    public int getTele_level_num() {
        return tele_level_num;
    }

    public void setTele_level_num(int tele_level_num) {
        this.tele_level_num = tele_level_num;
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

    public boolean isFinal_puPowerCellDef() {
        return final_puPowerCellDef;
    }

    public void setFinal_puPowerCellDef(boolean final_puPowerCellDef) {
        this.final_puPowerCellDef = final_puPowerCellDef;
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

    public boolean isFinal_def_RocketInt() {
        return final_def_RocketInt;
    }

    public void setFinal_def_RocketInt(boolean final_def_RocketInt) {
        this.final_def_RocketInt = final_def_RocketInt;
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


//   GLF 2/9/19
// End of Getters/Setters

}