package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//初始頁面
//**************************************************

public class StartPage extends AppCompatActivity {

    Button newgame,teamlist,recordgame,o_d_strategy;
    BaseballDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        newgame = (Button)findViewById(R.id.new_game_btn); //開始新局按鈕
        teamlist = (Button)findViewById(R.id.RecommendList_Btn); //推薦名單按鈕
        o_d_strategy = (Button)findViewById(R.id.O_D_Strategy); //攻守策略按鈕
        recordgame = (Button)findViewById(R.id.record_game_btn); //球賽紀錄按鈕

        db = new BaseballDB(this);

        newgame.setOnClickListener(goto_newgame);
        teamlist.setOnClickListener(goto_teamlist);
        o_d_strategy.setOnClickListener(goto_odstrategy);
        recordgame.setOnClickListener(goto_gamelist);
    }

    //前往開新球局頁面
    private View.OnClickListener goto_newgame= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(StartPage.this,NewGameName.class);
            startActivity(intent);
        }
    };

    //前往推薦名單頁面
    private View.OnClickListener goto_teamlist= new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(StartPage.this,ShowTeamList.class);
            startActivity(intent);
        }
    };

    //前往球賽紀錄頁面
    private View.OnClickListener goto_gamelist= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(StartPage.this,ShowGameList.class);
            startActivity(intent);
        }
    };

    //前往攻守策略按鈕
    private View.OnClickListener goto_odstrategy= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(StartPage.this,EnemyList.class);
            startActivity(intent);
        }
    };
}
