package kaixshaun.baseballsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Away_Team_Bench_List extends AppCompatActivity {

    public static final String AwayTeamID = "AwayTeamID";
    public static final String GameID = "GameID";

    EditText a_t_b_1,a_t_b_2,a_t_b_3,a_t_b_4,a_t_b_5,a_t_b_6,a_t_b_7,a_t_b_8;
    EditText a_t_b_9,a_t_b_10,a_t_b_11,a_t_b_12,a_t_b_13,a_t_b_14,a_t_b_15;
    Button store_a_t_b_btn,key_hometeam_list_btn;
    BaseballDB db;

    private String gameid,awayteamid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_away__team__bench__list);

        declare();
        gameid = setgameid();
        awayteamid = setawayteamid();
        Log.v("awayteamid==>>",awayteamid);

        db =new BaseballDB(this);

        store_a_t_b_btn = (Button)findViewById(R.id.store_away_team_bench_list_btn);
        store_a_t_b_btn.setOnClickListener(store_awayteam_bench_list);

        key_hometeam_list_btn = (Button)findViewById(R.id.Key_The_Home_Team_Starting_List_Btn);
        key_hometeam_list_btn.setOnClickListener(key_hometeam_list);

    }

    private View.OnClickListener store_awayteam_bench_list = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            db.deleteBenchOrder(gameid,awayteamid);
            settingbench();
            Toast toast = Toast.makeText(Away_Team_Bench_List.this, "成功儲存", Toast.LENGTH_LONG);
            toast.show();
        }
    };

    private View.OnClickListener key_hometeam_list = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.putExtra(AwayTeamID, awayteamid);
            intent.putExtra(GameID, gameid);
            intent.setClass(Away_Team_Bench_List.this,Key_The_Home_Team_List.class);
            startActivity(intent);
            Away_Team_Bench_List.this.finish();
        }
    };

    //得到gameid
    private String setgameid(){

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Away_Team_List.GameID);
        return temp;
    }

    //得到awayteamid
    private String setawayteamid(){

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Away_Team_List.AwayTeamID);
        return temp;
    }

    //得到背號
    private int turnback(EditText back){

        return Integer.parseInt(back.getText().toString());
    }

    //宣告edittext
    private void declare(){

        a_t_b_1 = (EditText)findViewById(R.id.away_team_bench_back_no1);
        a_t_b_2 = (EditText)findViewById(R.id.away_team_bench_back_no2);
        a_t_b_3 = (EditText)findViewById(R.id.away_team_bench_back_no3);
        a_t_b_4 = (EditText)findViewById(R.id.away_team_bench_back_no4);
        a_t_b_5 = (EditText)findViewById(R.id.away_team_bench_back_no5);
        a_t_b_6 = (EditText)findViewById(R.id.away_team_bench_back_no6);
        a_t_b_7 = (EditText)findViewById(R.id.away_team_bench_back_no7);
        a_t_b_8 = (EditText)findViewById(R.id.away_team_bench_back_no8);
        a_t_b_9 = (EditText)findViewById(R.id.away_team_bench_back_no9);
        a_t_b_10 = (EditText)findViewById(R.id.away_team_bench_back_no10);
        a_t_b_11 = (EditText)findViewById(R.id.away_team_bench_back_no11);
        a_t_b_12 = (EditText)findViewById(R.id.away_team_bench_back_no12);
        a_t_b_13 = (EditText)findViewById(R.id.away_team_bench_back_no13);
        a_t_b_14 = (EditText)findViewById(R.id.away_team_bench_back_no14);
        a_t_b_15 = (EditText)findViewById(R.id.away_team_bench_back_no15);
    }

    //儲存隊員名單到資料庫
    private void settingbench(){

        if(!"".equals(a_t_b_1.getText().toString().trim())) {

            db.insertTeammate(gameid, awayteamid, turnback(a_t_b_1), "B");
            Log.v("awayteambench==>","a_t_b_1.");
        }

        if(!"".equals(a_t_b_2.getText().toString().trim())){

            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_2),"B");
            Log.v("awayteambench==>","a_t_b_2.");
        }

        if(!"".equals(a_t_b_3.getText().toString().trim())){

            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_3),"B");
            Log.v("awayteambench==>","a_t_b_3.");
        }

        if(!"".equals(a_t_b_4.getText().toString().trim())){

            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_4),"B");
            Log.v("awayteambench==>","a_t_b_4.");
        }

        if(!"".equals(a_t_b_5.getText().toString().trim())){

            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_5),"B");
            Log.v("awayteambench==>","a_t_b_5.");
        }

        if(!"".equals(a_t_b_6.getText().toString().trim())){

            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_6),"B");
            Log.v("awayteambench==>","a_t_b_6.");
        }

        if(!"".equals(a_t_b_7.getText().toString().trim())){

            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_7),"B");
            Log.v("awayteambench==>","a_t_b_7.");
        }

        if(!"".equals(a_t_b_8.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_8),"B");

        if(!"".equals(a_t_b_9.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_9),"B");

        if(!"".equals(a_t_b_10.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_10),"B");

        if(!"".equals(a_t_b_11.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_11),"B");

        if(!"".equals(a_t_b_12.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_12),"B");

        if(!"".equals(a_t_b_13.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_13),"B");

        if(!"".equals(a_t_b_14.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_14),"B");

        if(!"".equals(a_t_b_15.getText().toString().trim()))
            db.insertTeammate(gameid,awayteamid,turnback(a_t_b_15),"B");
    }
}
