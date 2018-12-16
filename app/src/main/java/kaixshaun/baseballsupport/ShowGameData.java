package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ShowGameData extends AppCompatActivity {

    TextView awayteamview,hometeamview,awayteamnameview,hometeamnameview;
    String gameid,awayteamid,hometeamid,gamename;
    Cursor awayteamrecording,hometeamrecording,game_c,gamescore_c;
    String[] tempgamenames;
    BaseballDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_game_data);

        db = new BaseballDB(this);
        setgamename();

        game_c = db.selectGameID(gamename);
        game_c.moveToFirst();
        gameid = game_c.getString(0);

        awayteamid = db.selectawayteamid(gameid);
        hometeamid = db.selecthometeamid(gameid);

        awayteamview = (TextView)findViewById(R.id.AwayteamData);
        hometeamview = (TextView)findViewById(R.id.HomeTeamData);
        awayteamnameview = (TextView)findViewById(R.id.AwayteamName);
        hometeamnameview = (TextView)findViewById(R.id.HomeTeamName);

        gamescore_c = db.selectgame(gameid);
        String[] gamescorenames = gamescore_c.getColumnNames();
        gamescore_c.moveToFirst();

        awayteamrecording = db.selectfinalrecord(gameid,awayteamid);
        Cursor teamname_c = db.selectteamname(awayteamid);
        String[] tempnames =teamname_c.getColumnNames();
        teamname_c.moveToFirst();
        showfinaldata(awayteamrecording,awayteamview);
        awayteamnameview.setText(teamname_c.getString(teamname_c.getColumnIndex(tempnames[0]))+ "  分數:" +gamescore_c.getInt(gamescore_c.getColumnIndex(gamescorenames[6])));


        hometeamrecording = db.selectfinalrecord(gameid,hometeamid);
        teamname_c = db.selectteamname(hometeamid);
        tempnames =teamname_c.getColumnNames();
        teamname_c.moveToFirst();
        showfinaldata(hometeamrecording,hometeamview);
        hometeamnameview.setText(teamname_c.getString(teamname_c.getColumnIndex(tempnames[0]))+ "  分數:" +gamescore_c.getInt(gamescore_c.getColumnIndex(gamescorenames[5])));
    }

    private void setgamename(){

        Intent intent = getIntent();
        gamename = intent.getStringExtra(ShowGameList.Choice);
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
}
