package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameRecording extends AppCompatActivity {

    BaseballDB db;
    TextView atv, atmv, htv, htmv;
    String gameid, awayteamid, hometeamid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_recording);

        db = new BaseballDB(this);

        atv = (TextView) findViewById(R.id.awayteamview);
        atmv = (TextView) findViewById(R.id.awayteamateview);
        htv = (TextView) findViewById(R.id.hometeamview);
        htmv = (TextView) findViewById(R.id.hometeamateview);

        Intent intent = getIntent();
        gameid = intent.getStringExtra(Home_Team_Bench_List.GameID);
        awayteamid = intent.getStringExtra(Home_Team_Bench_List.AwayTeamID);
        hometeamid = intent.getStringExtra(Home_Team_Bench_List.HomeTeamID);

        String atvtemp = null, atmvtemp = null, htvtemp = null, htmvtemp = null;
        String[] names;

        //印出先攻order
        Cursor awayteamname_c = db.selsectteam(awayteamid);
        awayteamname_c.moveToFirst();

        names = awayteamname_c.getColumnNames();

        atvtemp = awayteamname_c.getString(awayteamname_c.getColumnIndex(names[2])) + "\n";

        Cursor awayteamorder_c = db.selestorder(gameid, awayteamid);
        awayteamorder_c.moveToFirst();

        names = awayteamorder_c.getColumnNames();

        for (int i = 0; i < awayteamorder_c.getCount(); i++) {

            atvtemp = atvtemp + awayteamorder_c.getString(awayteamorder_c.getColumnIndex(names[4])) + " "
                    + awayteamorder_c.getString(awayteamorder_c.getColumnIndex(names[3])) + " "
                    + awayteamorder_c.getString(awayteamorder_c.getColumnIndex(names[5])) + "\n";
            awayteamorder_c.moveToNext();
        }
        atv.setText(atvtemp);

        //印出先攻名單
        Cursor awayteammate_c = db.selectteamate(gameid, awayteamid);
        awayteammate_c.moveToFirst();

        names = awayteammate_c.getColumnNames();

        for (int i = 0; i < awayteammate_c.getCount(); i++) {

            atmvtemp = atmvtemp + awayteammate_c.getString(awayteammate_c.getColumnIndex(names[3])) + "  "
                    + awayteammate_c.getString(awayteammate_c.getColumnIndex(names[4])) + "\n";
            awayteammate_c.moveToNext();
        }
        atmv.setText(atmvtemp);

        //印出後攻order
        Cursor hometeamname_c = db.selsectteam(hometeamid);
        hometeamname_c.moveToFirst();

        names = hometeamname_c.getColumnNames();

        htvtemp = hometeamname_c.getString(hometeamname_c.getColumnIndex(names[2])) + "\n";

        Cursor hometeamorder_c = db.selestorder(gameid, hometeamid);
        hometeamorder_c.moveToFirst();

        names = hometeamorder_c.getColumnNames();

        for (int i = 0; i < hometeamorder_c.getCount(); i++) {

            htvtemp = htvtemp + hometeamorder_c.getString(hometeamorder_c.getColumnIndex(names[4])) + " "
                    + hometeamorder_c.getString(hometeamorder_c.getColumnIndex(names[3])) + " "
                    + hometeamorder_c.getString(hometeamorder_c.getColumnIndex(names[5])) + "\n";
            hometeamorder_c.moveToNext();
        }
        htv.setText(htvtemp);

        //印出後攻名單
        Cursor hometeammate_c = db.selectteamate(gameid, hometeamid);
        hometeammate_c.moveToFirst();

        names = hometeammate_c.getColumnNames();

        for (int i = 0; i < hometeammate_c.getCount(); i++) {

            htmvtemp = htmvtemp + hometeammate_c.getString(hometeammate_c.getColumnIndex(names[3])) + " "
                    + hometeammate_c.getString(hometeammate_c.getColumnIndex(names[4])) + "\n";
            hometeammate_c.moveToNext();

            htmv.setText(htmvtemp);


        }
    }
}
