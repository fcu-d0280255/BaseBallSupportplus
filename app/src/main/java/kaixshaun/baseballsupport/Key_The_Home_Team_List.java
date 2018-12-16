package kaixshaun.baseballsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Key_The_Home_Team_List extends AppCompatActivity {

    BaseballDB db;
    public static final String HomeTeamID = "HomeTeamID";
    public static final String AwayTeamID = "AwayTeamID";
    public static final String GameID = "GameID";
    private String gameid = null, awayteamid=null, hometeamid=null,hometeamname=null;

    //後攻方守位變數
    EditText h_t_b_n_1, h_t_b_n_2, h_t_b_n_3, h_t_b_n_4, h_t_b_n_5, h_t_b_n_6;
    EditText h_t_b_n_7, h_t_b_n_8, h_t_b_n_9, h_t_b_n_10, h_t_name;
    Spinner h_t_d_l_1, h_t_d_l_2, h_t_d_l_3, h_t_d_l_4, h_t_d_l_5, h_t_d_l_6;
    Spinner h_t_d_l_7, h_t_d_l_8, h_t_d_l_9, h_t_d_l_10;

    Button startgame, store_t_h_t_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key__the__home__team__list);

        gameid = setgameid();
        awayteamid = setawayteamid();

        db = new BaseballDB(this);
        Log.v("GameID", gameid);
        h_t_name = (EditText) findViewById(R.id.home_team_name);

        declare();

        //跳去輸入後攻候補名單功能
        startgame = (Button) findViewById(R.id.startgame_btn);
        startgame.setOnClickListener(StartGame);

        //儲存儲存先發名單
        store_t_h_t_l =(Button)findViewById(R.id.store_the_home_team_list_btn);
        store_t_h_t_l.setOnClickListener(store_the_home_team_list);
    }

    private View.OnClickListener StartGame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Intent intent = new Intent();
            intent.putExtra(AwayTeamID, awayteamid);
            intent.putExtra(HomeTeamID, hometeamid);
            intent.putExtra(GameID, gameid);
            intent.setClass(Key_The_Home_Team_List.this, PlayRecording.class);
            if (judgment()) {

                Toast toast = Toast.makeText(Key_The_Home_Team_List.this, "請填好名單並儲存!!", Toast.LENGTH_LONG);
                toast.show();
            } else {

                startActivity(intent);
                Key_The_Home_Team_List.this.finish();
            }
        }
    };

    private View.OnClickListener store_the_home_team_list = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast toast;
            if( hometeamid != null){

                if(db.deleteStartingOrder(gameid,hometeamid))
                    Log.v("delete","successful");
                else
                    Log.v("delete","fail");

            }


            if(!judgment()) {

                hometeamname = h_t_name.getText().toString();
                hometeamid = db.insertHometeamname(gameid, hometeamname);
                settingorder();
                toast = Toast.makeText(Key_The_Home_Team_List.this,"以儲存名單",Toast.LENGTH_LONG);
            }
            else
                toast = Toast.makeText(Key_The_Home_Team_List.this,"儲存失敗",Toast.LENGTH_LONG);
            toast.show();

        }
    };

    //將守位轉換成數字
    public int turnrule(String rule) {

        switch (rule) {

            case "投手":return 1;
            case "捕手":return 2;
            case "一壘手":return 3;
            case "二壘手":return 4;
            case "三壘手":return 5;
            case "游擊手":return 6;
            case "左外野手":return 7;
            case "中外野手":return 8;
            case "右外野手":return 9;
            case "自由手":return 10;
            default:return 0;
        }
    }

    //得到比賽ID
    private String setgameid() {

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Away_Team_List.GameID);
        return temp;
    }

    private String setawayteamid() {

        Intent intent = getIntent();
        String temp = intent.getStringExtra(Key_The_Away_Team_List.AwayTeamID);
        return temp;
    }

    //轉換背號
    private int turnback(EditText back) {

        return Integer.parseInt(back.getText().toString().trim());
    }

    //得到守備位置
    private int getrule(Spinner rule) {

        return turnrule(rule.getSelectedItem().toString());
    }

    //將先發名單與球員名單儲入資料庫
    private void settingorder(){

        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_1),1,getrule(h_t_d_l_1));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_1),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_2),2,getrule(h_t_d_l_2));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_2),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_3),3,getrule(h_t_d_l_3));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_3),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_4),4,getrule(h_t_d_l_4));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_4),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_5),5,getrule(h_t_d_l_5));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_5),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_6),6,getrule(h_t_d_l_6));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_6),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_7),7,getrule(h_t_d_l_7));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_7),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_8),8,getrule(h_t_d_l_8));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_8),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_9),9,getrule(h_t_d_l_9));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_9),"S");
        db.insertBattingorder(gameid,hometeamid,turnback(h_t_b_n_10),10,getrule(h_t_d_l_10));
        db.insertTeammate(gameid,hometeamid,turnback(h_t_b_n_10),"S");

    }

    private boolean judgment(){

        return ("".equals(h_t_b_n_1.getText().toString().trim()) || "".equals(h_t_b_n_2.getText().toString().trim())|| "".equals(h_t_b_n_3.getText().toString().trim())
                ||"".equals(h_t_b_n_4.getText().toString().trim())||"".equals(h_t_b_n_5.getText().toString().trim())||"".equals(h_t_b_n_6.getText().toString().trim())
                ||"".equals(h_t_b_n_7.getText().toString().trim())||"".equals(h_t_b_n_8.getText().toString().trim())||"".equals(h_t_b_n_9.getText().toString().trim())
                ||"".equals(h_t_b_n_10.getText().toString().trim()));
    }

    //整理宣告
    private void declare() {

        h_t_b_n_1 = (EditText) findViewById(R.id.home_team_back_number_1);
        h_t_b_n_2 = (EditText) findViewById(R.id.home_team_back_number_2);
        h_t_b_n_3 = (EditText) findViewById(R.id.home_team_back_number_3);
        h_t_b_n_4 = (EditText) findViewById(R.id.home_team_back_number_4);
        h_t_b_n_5 = (EditText) findViewById(R.id.home_team_back_number_5);
        h_t_b_n_6 = (EditText) findViewById(R.id.home_team_back_number_6);
        h_t_b_n_7 = (EditText) findViewById(R.id.home_team_back_number_7);
        h_t_b_n_8 = (EditText) findViewById(R.id.home_team_back_number_8);
        h_t_b_n_9 = (EditText) findViewById(R.id.home_team_back_number_9);
        h_t_b_n_10 = (EditText) findViewById(R.id.home_team_back_number_10);

        final String[] defense = {"投手","捕手","一壘手","二壘手","三壘手","游擊手","左外野手","中外野手","右外野手","自由手"};
        h_t_d_l_1 = (Spinner) findViewById(R.id.home_team_defense_location_1);
        h_t_d_l_2 = (Spinner) findViewById(R.id.home_team_defense_location_2);
        h_t_d_l_3 = (Spinner) findViewById(R.id.home_team_defense_location_3);
        h_t_d_l_4 = (Spinner) findViewById(R.id.home_team_defense_location_4);
        h_t_d_l_5 = (Spinner) findViewById(R.id.home_team_defense_location_5);
        h_t_d_l_6 = (Spinner) findViewById(R.id.home_team_defense_location_6);
        h_t_d_l_7 = (Spinner) findViewById(R.id.home_team_defense_location_7);
        h_t_d_l_8 = (Spinner) findViewById(R.id.home_team_defense_location_8);
        h_t_d_l_9 = (Spinner) findViewById(R.id.home_team_defense_location_9);
        h_t_d_l_10 = (Spinner) findViewById(R.id.home_team_defense_location_10);
        ArrayAdapter<String> defenselist = new ArrayAdapter<String>(Key_The_Home_Team_List.this, android.R.layout.simple_spinner_dropdown_item, defense);
        h_t_d_l_1.setAdapter(defenselist);
        h_t_d_l_2.setAdapter(defenselist);
        h_t_d_l_3.setAdapter(defenselist);
        h_t_d_l_4.setAdapter(defenselist);
        h_t_d_l_5.setAdapter(defenselist);
        h_t_d_l_6.setAdapter(defenselist);
        h_t_d_l_7.setAdapter(defenselist);
        h_t_d_l_8.setAdapter(defenselist);
        h_t_d_l_9.setAdapter(defenselist);
        h_t_d_l_10.setAdapter(defenselist);
    }
}

