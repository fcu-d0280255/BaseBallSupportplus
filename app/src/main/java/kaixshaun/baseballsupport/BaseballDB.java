package kaixshaun.baseballsupport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class BaseballDB {

    private SQLiteDatabase db;
    public MyDBHelper myDBHelper;

    public BaseballDB(Context context){
        if(db == null || !db.isOpen()) {
            myDBHelper = new MyDBHelper(context);
            db = myDBHelper.getWritableDatabase();
        }

    }



    public void close() {

        db.close();
    }
    //產生亂數編號
    public String CreatePassWord(){
        int[] word = new int[8];
        int mod;

        for(int i = 0; i < 8; i++){

            mod = (int)((Math.random()*7)%3);
            if(mod ==1){    //數字
                word[i]=(int)((Math.random()*10) + 48);
            }else if(mod ==2){  //大寫英文
                word[i] = (char)((Math.random()*26) + 65);
            }else{    //小寫英文
                word[i] = (char)((Math.random()*26) + 97);
            }
        }
        StringBuffer newPassword = new StringBuffer();
        for(int j = 0; j < 8; j++){
            newPassword.append((char)word[j]);
        }
        String rePassword = new String(newPassword);
        return rePassword;
    }


    //輸入新的賽局名稱
    public  String insertGamename(String gamename) {

        String gameid = new String(this.CreatePassWord());
        String hometeamid = new String(this.CreatePassWord());
        String awayteamid = new String(this.CreatePassWord());


        ContentValues cv = new ContentValues();

        cv.put("GameID",gameid);
        cv.put("GameName",gamename);
        cv.put("HomeTeamID",hometeamid);
        cv.put("AwayTeamID",awayteamid);
        cv.put("HomeScore",0);
        cv.put("AwayScore",0);

        db.insert("Game", null, cv);

        return gameid;
    }

    // 輸入新的後攻方隊名
    public String insertHometeamname(String gameid, String teamname) {

        Cursor c = db.rawQuery("select HomeTeamID from Game WHERE GameID =  '"+ gameid+"'", null);
        c.moveToFirst();
        String hometeamID = c.getString(0);
        ContentValues cv = new ContentValues();
        cv.put("TeamID", hometeamID);
        cv.put("TeamName",teamname);
        db.insert("Team", null, cv);

        return hometeamID;
    }

    // 輸入新的先攻方隊名
    public String insertAwayteamname(String gameid, String teamname) {

        Cursor c = db.rawQuery("select AwayTeamID from Game WHERE GameID = '"+ gameid+ "'", null);
        c.moveToFirst();
        String awayteamID = c.getString(0);
        ContentValues cv = new ContentValues();
        cv.put("TeamID", awayteamID);
        cv.put("TeamName",teamname);
        db.insert("Team", null, cv);

        return awayteamID;
    }

    //輸入隊員名單
    public void insertTeammate(String gameid, String teamid, int back, String s_b){

        ContentValues cv = new ContentValues();
        cv.put("GameID", gameid);
        cv.put("TeamID",teamid);
        cv.put("Back", back);
        cv.put("S_B", s_b);
        db.insert("Teammate", null, cv);
    }

    //輸入先發名單
    public void insertBattingorder(String gameid, String teamid, int back,int order, int rule){

        ContentValues cv = new ContentValues();
        cv.put("GameID", gameid);
        cv.put("TeamID", teamid);
        cv.put("Back", back);
        cv.put("_No", order);
        cv.put("Rule", rule);
        db.insert("BattingOrder", null, cv);
    }

    //輸入打擊紀錄
    public void insertRecord(String gameid, String teamid, int back, int inning, int round, int order, String base, String situation, int flyto, int out, int rbi,String notes){

        ContentValues cv = new ContentValues();
        cv.put("GameID", gameid);
        cv.put("TeamID", teamid);
        cv.put("Back", back);
        cv.put("Inning", inning);
        cv.put("Round", round);
        cv.put("_No", order);
        cv.put("Base", base);
        cv.put("Situation", situation);
        cv.put("Flyto", flyto);
        cv.put("Out", out);
        cv.put("RBI", rbi);
        cv.put("Notes", notes);
        db.insert("Record", null, cv);
    }

    public void insertfinaldata(String gameid, String teamid, int back, int order, int rule, int pa, float ba, float obp, int hit, int walk, int error, int rbi){

        ContentValues cv = new ContentValues();
        cv.put("GameID", gameid);
        cv.put("TeamID", teamid);
        cv.put("Back", back);
        cv.put("_No", order);
        cv.put("Rule", rule);
        cv.put("PA", pa);
        cv.put("BA", ba);
        cv.put("OBP", obp);
        cv.put("Hit", hit);
        cv.put("Walk", walk);
        cv.put("Error", error);
        cv.put("RBI", rbi);
        db.insert("FinalData", null, cv);
    }

     public void updatescore(String gameid,int awayscore,int homescore){

         db.execSQL("UPDATE Game SET HomeScore = "+ homescore +", AwayScore = " + awayscore + " WHERE GameID = '" + gameid +"'");
     }

    //刪除比賽
    //public boolean deletegame(String gameid)


     //刪除先發名單與隊名
    public boolean deleteStartingOrder(String gameid,String teamid){

        String battingorderwhere = "GameID = '" + gameid + "' AND TeamID =  '" + teamid + "'";
        String teammatewhere = "GameID = '" + gameid + "' AND TeamID =  '" + teamid + "' AND S_B = 'S' ";
        String teamnamewhere = "TeamID = '" + teamid +"'";

        long test1 = db.delete("BattingOrder", battingorderwhere, null);
        long test2 = db.delete("Teammate", teammatewhere, null);
        long test3 = db.delete("Team", teamnamewhere, null);

        return test1*test2*test3 > 0;
    }

    public boolean deleteBenchOrder(String gameid,String teamid){


        String teammate = "GameID = '" + gameid + "' AND TeamID =  '" + teamid + "' AND S_B = 'B' ";
        return db.delete("Teammate", teammate, null)>0;
    }

    public void deleteteamdata(String teamid){


        String whereteamid = "TeamID =  '" + teamid + "'";
        db.delete("Teammate", whereteamid, null);
        db.delete("BattingOrder", whereteamid, null);
            db.delete("Record", whereteamid, null);
        db.delete("FinalData", whereteamid, null);
        db.delete("Team", whereteamid, null);
}

    public void deletegame(String gameid){

        String wheregameid = "GameID =  '" + gameid + "'";
        Cursor c = db.rawQuery("select AwayTeamID from Game WHERE GameID = '"+ gameid+ "'", null);
        c.moveToFirst();
        db.rawQuery("DELETE FROM Team WHERE TeamID = '"+c.getString(0)+"'",null);

        c = db.rawQuery("select HomeTeamID from Game WHERE GameID = '"+ gameid+ "'", null);
        c.moveToFirst();
        db.rawQuery("DELETE FROM Team WHERE TeamID = '"+ c.getString(0) +"'",null);

        db.delete("Game", wheregameid, null);
        db.delete("BattingOrder", wheregameid, null);
        db.delete("Record", wheregameid, null);
        db.delete("FinalData", wheregameid, null);
    }


    public Cursor selsectteam(String teamid){

        Cursor c = db.rawQuery("select * from Team where TeamID = '" + teamid + "'",null);
        return c;
    }

    public Cursor selestorder(String gameid,String teamid){

        Cursor c = db.rawQuery("select * from BattingOrder where GameID = '" + gameid + "' AND TeamID = '" + teamid + "'",null);
        return c;
    }

    public Cursor selestorder(String teamid){

        Cursor c = db.rawQuery("select * from BattingOrder where TeamID = '" + teamid + "'",null);
        return c;
    }

    public Cursor selectteamate(String gameid, String teamid){

        Cursor c = db.rawQuery("select * from Teammate where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' " ,null);
        return c;
    }

    public Cursor selectrecording(String gameid, String teamid){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' " ,null);
        return c;
    }

    public Cursor selectrecording(String gameid, String teamid,int back){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back = '" + back +"'",null);
        return c;
    }

    public Cursor selectrecording(String teamid,int back){

        Cursor c = db.rawQuery("select * from Record where TeamID = '" + teamid + "' AND Back = '" + back +"'",null);
        return c;
    }

    public Cursor selectpa(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back ='" + back +"'" ,null);
        return c;
    }

    public Cursor selecthit(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back ='" + back +"' AND Situation = 'B'" ,null);
        return c;
    }

    public Cursor selectdiedK(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back ='" + back +"' AND Situation = 'K'" ,null);
        return c;
    }
    public Cursor selectdiedD(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back ='" + back +"' AND Situation = 'D'" ,null);
        return c;
    }

    public Cursor selectwalk(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back ='" + back +"' AND Situation = '保送'" ,null);
        return c;
    }

    public Cursor selecterror(String gameid, String teamid, int flyto){

        Cursor c = db.rawQuery("select * from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Flyto ='" + flyto +"' AND Situation = 'E'" ,null);
        return c;
    }

    public Cursor selectrbi(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select RBI from Record where GameID = '" + gameid + "' AND TeamID = '" + teamid + "' AND Back ='" + back +"' AND RBI > 0" ,null);
        return c;
    }

    public Cursor selectfinalrecord(String gameid, String teamid){

        Cursor c = db.rawQuery("select * from FinalData where GameID = '" + gameid + "' AND TeamID = '" + teamid + "'" ,null);
        return c;
    }

    public Cursor selectfinalrecord(String gameid, String teamid, int back){

        Cursor c = db.rawQuery("select * from FinalData where GameID = '" + gameid + "' AND TeamID = '" + teamid + "'AND Back ='" + back +"'" ,null);
        return c;
    }

    public Cursor selectfinalrecord(String teamid, int back){

        Cursor c = db.rawQuery("select * from FinalData where TeamID = '" + teamid + "'AND Back ='" + back +"'" ,null);
        return c;
    }

    public Cursor selectgamename(){

        Cursor c = db.rawQuery("select GameName from Game" ,null);
        return c;
    }

    public Cursor selectgame(String gameid){

        Cursor c = db.rawQuery("select * from Game where GameID = '"+ gameid +"'" ,null);
        return c;
    }

    public Cursor selectteamname(){

        Cursor c = db.rawQuery("select TeamName from Team" ,null);
        return c;
    }

    public Cursor selectteamname(String teamid){

        Cursor c = db.rawQuery("select TeamName from Team where TeamID = '" + teamid + "'" ,null);
        return c;
    }

    public Cursor selectteamid(String teamname){

        Cursor c = db.rawQuery("select TeamID from Team where TeamName = '" + teamname + "'" ,null);
        return  c;
    }

    public Cursor selectgameid(String teamid){

        Cursor c = db.rawQuery("select GameID from Game where HomeTeamID = '" + teamid +"' OR AwayTeamID = '" + teamid +"'" ,null);
        return c;
    }

    public Cursor selectGameID(String gamename){

        Cursor c = db.rawQuery("select GameID from Game where GameName = '"+ gamename +"'" ,null);
        return  c;
    }

    public String selectawayteamid(String gameid){

        Cursor c = db.rawQuery("select AwayTeamID from Game where GameID = '" + gameid + "'" ,null);
        c.moveToFirst();

        return  c.getString(0);
    }

    public String selecthometeamid(String gameid){

        Cursor c = db.rawQuery("select HomeTeamID from Game where GameID = '" + gameid + "'" ,null);
        c.moveToFirst();

        return  c.getString(0);
    }

}