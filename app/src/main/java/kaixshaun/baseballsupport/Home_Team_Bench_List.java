package kaixshaun.baseballsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home_Team_Bench_List extends AppCompatActivity {

    public static final String HomeTeamID = "HomeTeamID";
    public static final String AwayTeamID = "AwayTeamID";
    public static final String GameID = "GameID";

    EditText h_t_b_1,h_t_b_2,h_t_b_3,h_t_b_4,h_t_b_5,h_t_b_6,h_t_b_7,h_t_b_8;
    EditText h_t_b_9,h_t_b_10,h_t_b_11,h_t_b_12,h_t_b_13,h_t_b_14,h_t_b_15;
    Button store_h_t_b_btn,startgame_btn;
    BaseballDB db;

    private String gameid, awayteamid, hometeamid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__team__bench__list);


        db =new BaseballDB(this);
        declare();
        gameid = setgameid();
        awayteamid = setawayteamid();
        hometeamid = sethometeamid();


        store_h_t_b_btn = (Button)findViewById(R.id.store_home_team_bench_list_btn);
        store_h_t_b_btn.setOnClickListener(store_hometeam_bench_list);

        startgame_btn = (Button)findViewById(R.id.StartGame_btn);
        startgame_btn.setOnClickListener(startgame);

    }

    private View.OnClickListener store_hometeam_bench_list = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            db.deleteBenchOrder(gameid,hometeamid);
            settingbench();
            Toast toast = Toast.makeText(Home_Team_Bench_List.this, "成功儲存", Toast.LENGTH_LONG);
            toast.show();
        }
    };

    private View.OnClickListener startgame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.putExtra(AwayTeamID, awayteamid);
            intent.putExtra(HomeTeamID, hometeamid);
            intent.putExtra(GameID, gameid);
            intent.setClass(Home_Team_Bench_List.this,PlayRecording.class);
            startActivity(intent);
            Home_Team_Bench_List.this.finish();
        }
    };

    //得到gameid
    private String setgameid(){

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Home_Team_List.GameID);
        return temp;
    }

    //得到awayteamid
    private String setawayteamid(){

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Home_Team_List.AwayTeamID);
        return temp;
    }

    private String sethometeamid(){

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Home_Team_List.HomeTeamID);
        return temp;
    }

    //得到背號
    private int turnback(EditText back){

        return Integer.parseInt(back.getText().toString());
    }

    //宣告edittext
    private void declare(){

        h_t_b_1 = (EditText)findViewById(R.id.home_team_bench_back_no1);
        h_t_b_2 = (EditText)findViewById(R.id.home_team_bench_back_no2);
        h_t_b_3 = (EditText)findViewById(R.id.home_team_bench_back_no3);
        h_t_b_4 = (EditText)findViewById(R.id.home_team_bench_back_no4);
        h_t_b_5 = (EditText)findViewById(R.id.home_team_bench_back_no5);
        h_t_b_6 = (EditText)findViewById(R.id.home_team_bench_back_no6);
        h_t_b_7 = (EditText)findViewById(R.id.home_team_bench_back_no7);
        h_t_b_8 = (EditText)findViewById(R.id.home_team_bench_back_no8);
        h_t_b_9 = (EditText)findViewById(R.id.home_team_bench_back_no9);
        h_t_b_10 = (EditText)findViewById(R.id.home_team_bench_back_no10);
        h_t_b_11 = (EditText)findViewById(R.id.home_team_bench_back_no11);
        h_t_b_12 = (EditText)findViewById(R.id.home_team_bench_back_no12);
        h_t_b_13 = (EditText)findViewById(R.id.home_team_bench_back_no13);
        h_t_b_14 = (EditText)findViewById(R.id.home_team_bench_back_no14);
        h_t_b_15 = (EditText)findViewById(R.id.home_team_bench_back_no15);
    }

    //儲存隊員名單到資料庫
    private void settingbench(){

        if(!"".equals(h_t_b_1.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_1),"B");
        if(!"".equals(h_t_b_2.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_2),"B");
        if(!"".equals(h_t_b_3.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_3),"B");
        if(!"".equals(h_t_b_4.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_4),"B");
        if(!"".equals(h_t_b_5.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_5),"B");
        if(!"".equals(h_t_b_6.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_6),"B");
        if(!"".equals(h_t_b_7.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_7),"B");
        if(!"".equals(h_t_b_8.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_8),"B");
        if(!"".equals(h_t_b_9.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_9),"B");
        if(!"".equals(h_t_b_10.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_10),"B");
        if(!"".equals(h_t_b_11.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_11),"B");
        if(!"".equals(h_t_b_12.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_12),"B");
        if(!"".equals(h_t_b_13.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_13),"B");
        if(!"".equals(h_t_b_14.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_14),"B");
        if(!"".equals(h_t_b_15.getText().toString().trim()))
            db.insertTeammate(gameid,hometeamid,turnback(h_t_b_15),"B");
    }
}