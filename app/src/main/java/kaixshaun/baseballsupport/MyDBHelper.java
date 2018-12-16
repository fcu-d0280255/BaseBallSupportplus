package kaixshaun.baseballsupport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by LIN on 2017/6/20.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "baseballsupport.db";
    private static int VERSION = 12;

    public static final String CREATE_Game_TABLE = "CREATE TABLE IF NOT EXISTS Game (_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,GameName TEXT,HomeTeamID TEXT NOT NULL,AwayTeamID TEXT NOT NULL,HomeScore INTEGER,AwayScore INTEGER)";
    public static final String CREATE_Team_TABLE = "CREATE TABLE IF NOT EXISTS Team (_id INTEGER PRIMARY KEY AUTOINCREMENT,TeamID TEXT NOT NULL,TeamName TEXT)";
    public static final String CREATE_Inning_TABLE = "CREATE TABLE IF NOT EXISTS Inning(_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,Inning  INTEGER,TopHalf  INTEGER,BottonHalf  INTEGER);";
    public static final String CREATE_BattingOrder_TABLE = "CREATE TABLE IF NOT EXISTS BattingOrder (_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,TeamID TEXT NOT NULL,Back INTEGER,_No INTEGER,Rule INTEGER)";
    public static final String CREATE_Teammate_TABLE = "CREATE TABLE IF NOT EXISTS Teammate (_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,TeamID TEXT NOT NULL,Back INTEGER,S_B TEXT)";
    public static final String CREATE_Record_TABLE = "CREATE TABLE IF NOT EXISTS Record(_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,TeamID TEXT NOT NULL,Back INTEGER,Inning  INTEGER,Round  INTEGER,_No  INTEGER,Base TEXT,Situation TEXT,Flyto  INTEGER,Out  INTEGER,RBI  INTEGER,Notes TEXT)";
    public static final String CREATE_FinalData_TABLE = "CREATE TABLE IF NOT EXISTS FinalData(_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,TeamID TEXT NOT NULL,Back  INTEGER,_No  INTEGER,Rule  INTEGER,PA INTEGER,BA REAL,OBP REAL,Hit INTEGER,Walk INTEGER,Error INTEGER,RBI INTEGER)";
    public static final String CREATE_TeamRecord_TABLE = "CREATE TABLE IF NOT EXISTS TeamRecord(_id INTEGER PRIMARY KEY AUTOINCREMENT,GameID TEXT NOT NULL,TeamID TEXT NOT NULL,TotalHit  INTEGER,TotalWalk  INTEGER,TotalError  INTEGER)";


    public MyDBHelper(Context context) {
        super(context, DBName, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_Game_TABLE);
        db.execSQL(CREATE_Team_TABLE);
        db.execSQL(CREATE_Inning_TABLE);
        db.execSQL(CREATE_BattingOrder_TABLE);
        db.execSQL(CREATE_Teammate_TABLE);
        db.execSQL(CREATE_Record_TABLE);
        db.execSQL(CREATE_FinalData_TABLE);
        db.execSQL(CREATE_TeamRecord_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.v("onUpgrade",oldVersion+">>>"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS Game");
        db.execSQL("DROP TABLE IF EXISTS Team");
        db.execSQL(" DROP TABLE IF EXISTS Inning");
        db.execSQL("DROP TABLE IF EXISTS BattingOrder");
        db.execSQL("DROP TABLE IF EXISTS Teammate");
        db.execSQL(" DROP TABLE IF EXISTS Record");
        db.execSQL(" DROP TABLE IF EXISTS FinalData");
        db.execSQL(" DROP TABLE IF EXISTS TeamRecord ");
        onCreate(db);

    }


}
