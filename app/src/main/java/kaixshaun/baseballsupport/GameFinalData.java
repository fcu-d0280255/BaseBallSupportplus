package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class GameFinalData extends AppCompatActivity {

    TextView awayteamview,hometeamview,awayteamnameview,hometeamnameview;
    String gameid,awayteamid,hometeamid;
    Cursor awayteamrecording,hometeamrecording,awayteamorder,hometeamorder,gamescore_c;
    BaseballDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_final_data);

        setid();
        Log.v("gamid=>",gameid);
        Log.v("awayteamid=>",awayteamid);
        Log.v("hometeamid=>",hometeamid);


        db = new BaseballDB(this);

        awayteamorder = db.selestorder(gameid,awayteamid);
        hometeamorder = db.selestorder(gameid,hometeamid);

        caculatefinaldata(awayteamorder,gameid,awayteamid,hometeamid);
        caculatefinaldata(hometeamorder,gameid,hometeamid,awayteamid);



        awayteamview = (TextView)findViewById(R.id.awayteamrecording);
        hometeamview = (TextView)findViewById(R.id.hometeamrecording);
        awayteamnameview = (TextView)findViewById(R.id.awayteamname);
        hometeamnameview = (TextView)findViewById(R.id.hometeamname);

        gamescore_c = db.selectgame(gameid);
        String[] gamescorenames = gamescore_c.getColumnNames();
        gamescore_c.moveToFirst();

        awayteamrecording = db.selectfinalrecord(gameid,awayteamid);
        Cursor teamname_c = db.selectteamname(awayteamid);
        String[] tempnames =teamname_c.getColumnNames();
        teamname_c.moveToFirst();
        showfinaldata(awayteamrecording,awayteamview);
        awayteamnameview.setText(teamname_c.getString(teamname_c.getColumnIndex(tempnames[0])) + "  分數:" +gamescore_c.getInt(gamescore_c.getColumnIndex(gamescorenames[6])));


        hometeamrecording = db.selectfinalrecord(gameid,hometeamid);
        teamname_c = db.selectteamname(hometeamid);
        tempnames =teamname_c.getColumnNames();
        teamname_c.moveToFirst();
        showfinaldata(hometeamrecording,hometeamview);
        hometeamnameview.setText(teamname_c.getString(teamname_c.getColumnIndex(tempnames[0])) + "  分數: " +gamescore_c.getInt(gamescore_c.getColumnIndex(gamescorenames[5])));


    }



    private void setid() {

        Intent intent = getIntent();
        gameid = intent.getStringExtra(PlayRecording.GameID);
        awayteamid = intent.getStringExtra(PlayRecording.AwayTeamID);
        hometeamid = intent.getStringExtra(PlayRecording.HomeTeamID);
    }

    private void showfinaldata(Cursor c, TextView show){

        String temp ="";
        String[] names;

        DecimalFormat df = new DecimalFormat("#.##");

        c.moveToFirst();
        names = c.getColumnNames();


        for(int i = 0; i < c.getCount(); i ++){

            temp = temp + c.getInt(c.getColumnIndex(names[3])) + "號 " +
                    c.getInt(c.getColumnIndex(names[4])) + "棒   守位:" +
                    c.getInt(c.getColumnIndex(names[5])) + "    " +
                    c.getInt(c.getColumnIndex(names[6])) + "打席  打擊率: " +
                    df.format(c.getFloat(c.getColumnIndex(names[7]))) + "  上壘率:" +
                    df.format(c.getFloat(c.getColumnIndex(names[8]))) + "  安打數:" +
                    c.getInt(c.getColumnIndex(names[9])) + "    保送: " +
                    c.getInt(c.getColumnIndex(names[10])) + "   失誤: " +
                    c.getInt(c.getColumnIndex(names[11])) + "次  打點: " +
                    c.getInt(c.getColumnIndex(names[12])) + "分\n\n";
            c.moveToNext();
        }
        show.setText(temp);
    }

    private void caculatefinaldata( Cursor c, String gameid, String teamid,String anotherteamid){

        int back,order,rule,pa,hit,walk,error,rbi,temppa;
        float ba,obp;
        String[] names,tempnames;
        Cursor tempc;

        names = c.getColumnNames();
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            back = c.getInt(c.getColumnIndex(names[3]));
            order = c.getInt(c.getColumnIndex(names[4]));
            rule = c.getInt(c.getColumnIndex(names[5]));

            tempc = db.selectpa(gameid, teamid, back);
            pa = tempc.getCount();

            tempc = db.selecthit(gameid, teamid, back);
            hit = tempc.getCount();
            if(pa==0)
                temppa=1;
            else
                temppa = pa;
            ba = (float) hit / temppa;

            int D,K;
            tempc = db.selectdiedD(gameid, teamid, back);
            D = tempc.getCount();
            tempc = db.selectdiedK(gameid, teamid, back);
            K =tempc.getCount();
            obp =(float) (pa-D-K)/temppa;
            if(obp <= 0)
                obp = 0;

            tempc = db.selectwalk(gameid, teamid, back);
            walk = tempc.getCount();

            tempc = db.selecterror(gameid, anotherteamid, rule);
            error = tempc.getCount();

            tempc = db.selectrbi(gameid, teamid, back);
            rbi = 0;
            tempc.moveToFirst();
            tempnames = tempc.getColumnNames();

            for (int j = 0; j < tempc.getCount(); j++) {

                rbi = rbi + tempc.getInt(tempc.getColumnIndex(tempnames[0]));
                tempc.moveToNext();
            }

            db.insertfinaldata(gameid, teamid, back, order, rule, pa, ba, obp, hit, walk, error, rbi);

            c.moveToNext();

        }
    }

     /*awayteamview = (TextView)findViewById(R.id.awayteamrecording);
        hometeamview = (TextView)findViewById(R.id.hometeamrecording);

        awayteamrecording = db.selectrecording(gameid,awayteamid);
        hometeamrecording = db.selectrecording(gameid,hometeamid);

        awayteamrecording.moveToFirst();
        names = awayteamrecording.getColumnNames();
        for(int i = 0; i < awayteamrecording.getCount(); i ++){

            awayteamshow = awayteamshow+awayteamrecording.getInt(awayteamrecording.getColumnIndex(names[3])) + " 號  " +
                    awayteamrecording.getInt(awayteamrecording.getColumnIndex(names[4])) + " 上  " +
                    awayteamrecording.getInt(awayteamrecording.getColumnIndex(names[6])) + " 棒  " +
                    awayteamrecording.getString(awayteamrecording.getColumnIndex(names[7])) + "  飛去" +
                    awayteamrecording.getInt(awayteamrecording.getColumnIndex(names[8])) + "   " +
                    awayteamrecording.getInt(awayteamrecording.getColumnIndex(names[9])) + " out   得" +
                    awayteamrecording.getInt(awayteamrecording.getColumnIndex(names[10])) + " 分\n";
            awayteamrecording.moveToNext();
        }
        awayteamview.setText(awayteamshow);

        hometeamrecording.moveToFirst();
        names = hometeamrecording.getColumnNames();
        for(int i = 0; i < hometeamrecording.getCount(); i ++){

            hometeamshow = hometeamshow+hometeamrecording.getInt(hometeamrecording.getColumnIndex(names[3])) + " 號  " +
                    hometeamrecording.getInt(hometeamrecording.getColumnIndex(names[4])) + " 上  " +
                    hometeamrecording.getInt(hometeamrecording.getColumnIndex(names[6])) + " 棒  " +
                    hometeamrecording.getString(hometeamrecording.getColumnIndex(names[7])) + "  飛去" +
                    hometeamrecording.getInt(hometeamrecording.getColumnIndex(names[8])) + "   " +
                    hometeamrecording.getInt(hometeamrecording.getColumnIndex(names[9])) + " out   得" +
                    hometeamrecording.getInt(hometeamrecording.getColumnIndex(names[10])) + " 分\n";
            hometeamrecording.moveToNext();
        }
        hometeamview.setText(hometeamshow);*/
}
