package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//開新球局頁面
//******************************************

public class NewGameName extends AppCompatActivity {

    public static final String GameID = "GameID";
    BaseballDB db;
    EditText gamename;
    Button enter,cancel;
    String gameid,awayteamid,hometeamid;
    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_name);

        gamename = (EditText)findViewById(R.id.game_name);
        enter = (Button)findViewById(R.id.enter_btn);
        cancel = (Button)findViewById(R.id.cancel_btn);
        db = new BaseballDB(this);
        enter.setOnClickListener(goto_key_the_away_list);
        cancel.setOnClickListener(cancelgame);

    }

    private View.OnClickListener goto_key_the_away_list = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();


            //得到gameid
            //測試用
            if(!gamename.getText().toString().equals("shaunlin168") && !gamename.getText().toString().equals("secret168") && !gamename.getText().toString().equals("download")) {

                gameName = gamename.getText().toString();
                Log.v("test",gameName);
                gameid = db.insertGamename(gameName);
                Log.v("test",gameid);
            }
            gameName = gamename.getText().toString();

            //傳gameid給別的activity
            intent.putExtra(GameID, gameid);

            //輸入空白時會提醒
            if("".equals(gamename.getText().toString().trim())){

                Toast toast = Toast.makeText(NewGameName.this,"請輸入比賽名稱",Toast.LENGTH_LONG);
                toast.show();
            }

            //當輸入shaunlin168時可以進入刪除隊伍頁面 待開發
            else if(gameName.equals("shaunlin168")) {

                intent.setClass(NewGameName.this,Secret.class);
                startActivity(intent);
                NewGameName.this.finish();

            }

            //當輸入secret168時可以進入刪除比賽頁面 待開發
            else if(gameName.equals("secret168")){

                intent.setClass(NewGameName.this,GameSecret.class);
                startActivity(intent);
                NewGameName.this.finish();
            }

            else if(gameName.equals("download")){

                Inputteamordata();
                NewGameName.this.finish();
            }

            else{
                //換頁面的參數
                intent.setClass(NewGameName.this,Key_The_Away_Team_List.class);
                intent.putExtra(GameID, gameid);
                startActivity(intent);
                NewGameName.this.finish();
            }
        }
    };

    private  View.OnClickListener cancelgame =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            NewGameName.this.finish();
        }
    };



//**************************************************************************************************************
    //預載入資料

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

    private void Inputteamordata(){

        Cursor awayteamorder,hometeamorder;

        gameid = db.insertGamename("G1");
        hometeamid = db.insertHometeamname(gameid,"東海資管");
        awayteamid = db.insertAwayteamname(gameid,"逢甲資工");
        db.insertBattingorder(gameid, awayteamid,7, 1,8);
        db.insertBattingorder(gameid, awayteamid,15, 2,9);
        db.insertBattingorder(gameid, awayteamid,29, 3,7);
        db.insertBattingorder(gameid, awayteamid,6, 4,3);
        db.insertBattingorder(gameid, awayteamid,4, 5,5);
        db.insertBattingorder(gameid, awayteamid,47, 6,4);
        db.insertBattingorder(gameid, awayteamid,51, 7,6);
        db.insertBattingorder(gameid, awayteamid,60, 8,10);
        db.insertBattingorder(gameid, awayteamid,3, 9,2);
        db.insertBattingorder(gameid, awayteamid,31, 10,1);

        db.insertBattingorder(gameid, hometeamid,4, 1,8);
        db.insertBattingorder(gameid, hometeamid,10, 2,6);
        db.insertBattingorder(gameid, hometeamid,31, 3,7);
        db.insertBattingorder(gameid, hometeamid,16, 4,3);
        db.insertBattingorder(gameid, hometeamid,5, 5,5);
        db.insertBattingorder(gameid, hometeamid,12, 6,10);
        db.insertBattingorder(gameid, hometeamid,11, 7,4);
        db.insertBattingorder(gameid, hometeamid,23, 8,9);
        db.insertBattingorder(gameid, hometeamid,49, 9,2);
        db.insertBattingorder(gameid, hometeamid,14, 10,1);

        awayteamorder = db.selestorder(gameid,awayteamid);
        hometeamorder = db.selestorder(gameid,hometeamid);

        InputG1Data(gameid,awayteamid,hometeamid);

        caculatefinaldata(awayteamorder,gameid,awayteamid,hometeamid);
        caculatefinaldata(hometeamorder,gameid,hometeamid,awayteamid);


        gameid = db.insertGamename("G2");
        hometeamid = db.insertHometeamname(gameid,"逢甲資工");
        awayteamid = db.insertAwayteamname(gameid,"東海資管");
        db.insertBattingorder(gameid, awayteamid,25, 1,8);
        db.insertBattingorder(gameid, awayteamid,10, 2,6);
        db.insertBattingorder(gameid, awayteamid,3, 3,7);
        db.insertBattingorder(gameid, awayteamid,16, 4,3);
        db.insertBattingorder(gameid, awayteamid,5, 5,5);
        db.insertBattingorder(gameid, awayteamid,36, 6,10);
        db.insertBattingorder(gameid, awayteamid,11, 7,4);
        db.insertBattingorder(gameid, awayteamid,1, 8,9);
        db.insertBattingorder(gameid, awayteamid,8, 9,2);
        db.insertBattingorder(gameid, awayteamid,14, 10,1);

        db.insertBattingorder(gameid, hometeamid,7, 1,8);
        db.insertBattingorder(gameid, hometeamid,15, 2,9);
        db.insertBattingorder(gameid, hometeamid,29, 3,7);
        db.insertBattingorder(gameid, hometeamid,6, 4,3);
        db.insertBattingorder(gameid, hometeamid,4, 5,5);
        db.insertBattingorder(gameid, hometeamid,23, 6,4);
        db.insertBattingorder(gameid, hometeamid,51, 7,6);
        db.insertBattingorder(gameid, hometeamid,49, 8,10);
        db.insertBattingorder(gameid, hometeamid,8, 9,2);
        db.insertBattingorder(gameid, hometeamid,31, 10,1);

        awayteamorder = db.selestorder(gameid,awayteamid);
        hometeamorder = db.selestorder(gameid,hometeamid);

        InputG2Data(gameid,awayteamid,hometeamid);

        caculatefinaldata(awayteamorder,gameid,awayteamid,hometeamid);
        caculatefinaldata(hometeamorder,gameid,hometeamid,awayteamid);

        gameid = db.insertGamename("G3");
        hometeamid = db.insertHometeamname(gameid,"東海資管");
        awayteamid = db.insertAwayteamname(gameid,"逢甲資工");
        db.insertBattingorder(gameid, awayteamid,7, 1,8);
        db.insertBattingorder(gameid, awayteamid,15, 2,9);
        db.insertBattingorder(gameid, awayteamid,52, 3,4);
        db.insertBattingorder(gameid, awayteamid,6, 4,3);
        db.insertBattingorder(gameid, awayteamid,2, 5,5);
        db.insertBattingorder(gameid, awayteamid,47, 6,7);
        db.insertBattingorder(gameid, awayteamid,51, 7,6);
        db.insertBattingorder(gameid, awayteamid,60, 8,10);
        db.insertBattingorder(gameid, awayteamid,3, 9,2);
        db.insertBattingorder(gameid, awayteamid,23, 10,1);

        db.insertBattingorder(gameid, hometeamid,25, 1,8);
        db.insertBattingorder(gameid, hometeamid,3, 2,7);
        db.insertBattingorder(gameid, hometeamid,16, 3,3);
        db.insertBattingorder(gameid, hometeamid,14, 4,1);
        db.insertBattingorder(gameid, hometeamid,5, 5,5);
        db.insertBattingorder(gameid, hometeamid,1, 6,9);
        db.insertBattingorder(gameid, hometeamid,11, 7,4);
        db.insertBattingorder(gameid, hometeamid,49, 8,10);
        db.insertBattingorder(gameid, hometeamid,10, 9,2);
        db.insertBattingorder(gameid, hometeamid,12, 10,6);

        awayteamorder = db.selestorder(gameid,awayteamid);
        hometeamorder = db.selestorder(gameid,hometeamid);

        InputG2Data(gameid,awayteamid,hometeamid);

        caculatefinaldata(awayteamorder,gameid,awayteamid,hometeamid);
        caculatefinaldata(hometeamorder,gameid,hometeamid,awayteamid);


    }

    private void InputG2Data(String gameid,String awayteamid,String hometeamid){

        db.insertRecord(gameid,hometeamid,7,1,0,1,"1","E",6,0,0,"");
        db.insertRecord(gameid,hometeamid,7,3,0,1,"無","D",5,1,0,"");
        db.insertRecord(gameid,hometeamid,7,5,0,1,"3","B",10,0,1,"");
        db.insertRecord(gameid,hometeamid,15,1,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,15,3,0,2,"無","D",5,1,0,"");
        db.insertRecord(gameid,hometeamid,15,5,0,2,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,29,1,0,3,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,29,3,0,3,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,6,1,0,4,"1","B",8,0,0,"");
        db.insertRecord(gameid,hometeamid,6,4,0,4,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,hometeamid,4,1,0,5,"無","D",1,2,0,"");
        db.insertRecord(gameid,hometeamid,4,4,0,5,"無","D",4,1,0,"");
        db.insertRecord(gameid,hometeamid,23,2,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,23,4,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,51,2,0,7,"1","B",7,0,0,"");
        db.insertRecord(gameid,hometeamid,51,4,0,7,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,49,2,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,49,5,0,8,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,8,2,0,9,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,8,5,0,9,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,31,2,0,10,"無","D",3,1,0,"");
        db.insertRecord(gameid,hometeamid,31,5,0,10,"無","D",10,1,0,"");

        db.insertRecord(gameid,awayteamid,25,1,0,1,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,25,4,0,1,"1","E",4,0,0,"");
        db.insertRecord(gameid,awayteamid,25,6,0,1,"無","D",5,1,0,"");
        db.insertRecord(gameid,awayteamid,10,1,0,2,"無","D",3,1,0,"");
        db.insertRecord(gameid,awayteamid,10,4,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,10,6,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,3,1,0,3,"無","D",1,1,0,"");
        db.insertRecord(gameid,awayteamid,3,4,0,3,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,3,6,0,3,"無","D",7,1,0,"");
        db.insertRecord(gameid,awayteamid,16,2,0,4,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,16,4,0,4,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,5,2,0,5,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,5,4,0,5,"無","D",2,1,0,"");
        db.insertRecord(gameid,awayteamid,36,2,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,36,5,0,6,"2","E",8,0,0,"");
        db.insertRecord(gameid,awayteamid,11,3,0,7,"無","D",7,1,0,"");
        db.insertRecord(gameid,awayteamid,11,5,0,7,"1","B",4,0,1,"");
        db.insertRecord(gameid,awayteamid,1,3,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,1,5,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,8,3,0,9,"無","D",10,1,0,"");
        db.insertRecord(gameid,awayteamid,8,5,0,9,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,14,4,0,10,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,14,5,0,10,"無","D",6,1,0,"");

        db.updatescore(gameid,1,2);
    }

    public void InputG1Data(String gameid,String awayteamid,String hometeamid) {

        db.insertRecord(gameid,awayteamid,7,1,0,1,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,7,2,0,1,"2","B",6,0,2,"");
        db.insertRecord(gameid,awayteamid,7,3,0,1,"1","B",6,0,2,"");
        db.insertRecord(gameid,awayteamid,7,6,0,1,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,awayteamid,15,1,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,15,2,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,15,3,0,2,"1","B",6,0,0,"");
        db.insertRecord(gameid,awayteamid,15,6,0,2,"無","D",6,1,0,"");
        db.insertRecord(gameid,awayteamid,29,1,0,3,"1","B",6,0,0,"");
        db.insertRecord(gameid,awayteamid,29,2,0,3,"1","E",6,0,0,"");
        db.insertRecord(gameid,awayteamid,29,3,0,3,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,29,6,0,3,"1","B",6,0,0,"");
        db.insertRecord(gameid,awayteamid,6,1,0,4,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,6,2,0,4,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,6,4,0,4,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,6,6,0,4,"全壘打","B",0,0,2,"");
        db.insertRecord(gameid,awayteamid,4,1,0,5,"1","B",4,0,0,"");
        db.insertRecord(gameid,awayteamid,4,3,0,5,"2","B",4,0,0,"");
        db.insertRecord(gameid,awayteamid,4,4,0,5,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,4,6,0,5,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,47,1,0,6,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,47,3,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,47,4,0,6,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,47,6,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,51,2,0,7,"1","E",7,0,0,"");
        db.insertRecord(gameid,awayteamid,51,3,0,7,"1","B",7,0,0,"");
        db.insertRecord(gameid,awayteamid,51,5,0,7,"無","D",7,1,0,"");
        db.insertRecord(gameid,awayteamid,60,2,0,8,"1","B",3,0,0,"");
        db.insertRecord(gameid,awayteamid,60,3,0,8,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,60,5,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,3,2,0,9,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,3,3,0,9,"無","保送",0,0,1,"");
        db.insertRecord(gameid,awayteamid,3,5,0,9,"1","B",3,0,0,"");
        db.insertRecord(gameid,awayteamid,31,2,0,10,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,31,3,0,10,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,31,5,0,10,"無","K",0,1,0,"");

        db.insertRecord(gameid,hometeamid,4,1,0,1,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,4,2,0,1,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,4,4,0,1,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,4,6,0,1,"2","B",9,0,0,"");
        db.insertRecord(gameid,hometeamid,10,1,0,2,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,10,2,0,2,"1","B",3,0,1,"");
        db.insertRecord(gameid,hometeamid,10,4,0,2,"1","B",3,0,0,"");
        db.insertRecord(gameid,hometeamid,10,6,0,2,"1","B",10,0,0,"");
        db.insertRecord(gameid,hometeamid,31,1,0,3,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,31,2,0,3,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,31,4,0,3,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,31,6,0,3,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,16,1,0,4,"1","E",4,0,0,"");
        db.insertRecord(gameid,hometeamid,16,2,0,4,"2","B",8,0,3,"");
        db.insertRecord(gameid,hometeamid,16,4,0,4,"1","B",8,0,2,"");
        db.insertRecord(gameid,hometeamid,16,6,0,4,"1","B",6,0,1,"");
        db.insertRecord(gameid,hometeamid,5,1,0,5,"1","B",8,0,2,"");
        db.insertRecord(gameid,hometeamid,5,2,0,5,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,5,4,0,5,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,12,1,0,6,"無","D",5,1,0,"");
        db.insertRecord(gameid,hometeamid,12,3,0,6,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,12,5,0,6,"無","D",10,1,0,"");
        db.insertRecord(gameid,hometeamid,11,1,0,7,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,11,3,0,7,"無","D",7,1,0,"");
        db.insertRecord(gameid,hometeamid,11,5,0,7,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,23,2,0,8,"2","E",8,0,0,"");
        db.insertRecord(gameid,hometeamid,23,3,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,23,5,0,8,"無","D",7,1,0,"");
        db.insertRecord(gameid,hometeamid,49,2,0,9,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,49,4,0,9,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,49,5,0,9,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,14,2,0,10,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,14,4,0,10,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,14,6,0,10,"無","D",7,1,0,"");

        db.updatescore(gameid,8,9);
    }

    public void InputG3Data(String gameid,String awayteamid,String hometeamid) {

        db.insertRecord(gameid,awayteamid,7,1,0,1,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,awayteamid,7,3,0,1,"無","D",6,1,0,"");
        db.insertRecord(gameid,awayteamid,7,5,0,1,"1","B",6,0,0,"");
        db.insertRecord(gameid,awayteamid,15,1,0,2,"無","D",5,1,0,"");
        db.insertRecord(gameid,awayteamid,15,3,0,2,"1","B",5,0,0,"");
        db.insertRecord(gameid,awayteamid,15,5,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,52,1,0,3,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,52,3,0,3,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,52,5,0,3,"無","D",6,1,0,"");
        db.insertRecord(gameid,awayteamid,6,1,0,4,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,awayteamid,6,3,0,4,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,6,5,0,4,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,awayteamid,2,1,0,5,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,2,3,0,5,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,2,5,0,5,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,47,2,0,6,"1","E",4,0,0,"");
        db.insertRecord(gameid,awayteamid,47,4,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,47,6,0,6,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,51,2,0,7,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,51,4,0,7,"無","D",6,2,0,"");
        db.insertRecord(gameid,awayteamid,51,6,0,7,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,60,2,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,60,5,0,8,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,60,6,0,8,"1","E",3,0,0,"");
        db.insertRecord(gameid,awayteamid,3,2,0,9,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,3,5,0,9,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,3,6,0,9,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,23,2,0,10,"無","D",6,2,0,"");
        db.insertRecord(gameid,awayteamid,23,5,0,10,"1","B",7,0,0,"");
        db.insertRecord(gameid,awayteamid,23,6,0,10,"無","K",0,1,0,"");

        db.insertRecord(gameid,hometeamid,25,1,0,1,"無","D",4,1,0,"");
        db.insertRecord(gameid,hometeamid,25,3,0,1,"全壘打","D",0,0,4,"");
        db.insertRecord(gameid,hometeamid,25,6,0,1,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,3,1,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,3,3,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,3,6,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,16,1,0,3,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,16,3,0,3,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,16,6,0,3,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,14,2,0,4,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,14,3,0,4,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,14,6,0,4,"1","E",6,0,0,"");
        db.insertRecord(gameid,hometeamid,5,2,0,5,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,5,4,0,5,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,5,6,0,5,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,1,2,0,6,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,1,4,0,6,"無","D",4,1,0,"");
        db.insertRecord(gameid,hometeamid,11,2,0,7,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,11,4,0,7,"無","D",5,1,0,"");
        db.insertRecord(gameid,hometeamid,49,3,0,8,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,49,5,0,8,"無","D",3,1,0,"");
        db.insertRecord(gameid,hometeamid,10,3,0,9,"1","E",3,0,0,"");
        db.insertRecord(gameid,hometeamid,10,5,0,9,"無","D",7,0,0,"");
        db.insertRecord(gameid,hometeamid,12,3,0,10,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,12,5,0,10,"無","D",10,1,0,"");

        db.updatescore(gameid,3,4);
    }

    public void InputG4Data(String gameid,String awayteamid,String hometeamid) {

        db.insertRecord(gameid,hometeamid,7,1,0,1,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,7,3,0,1,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,7,4,0,1,"2","B",9,0,3,"");
        db.insertRecord(gameid,hometeamid,8,1,0,2,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,8,4,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,8,5,0,2,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,52,1,0,3,"無","D",6,2,0,"");
        db.insertRecord(gameid,hometeamid,52,4,0,3,"1","E",6,0,0,"");
        db.insertRecord(gameid,hometeamid,52,5,0,3,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,6,2,0,4,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,6,4,0,4,"全壘打","B",0,0,2,"");
        db.insertRecord(gameid,hometeamid,6,5,0,4,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,47,2,0,5,"無","D",4,1,0,"");
        db.insertRecord(gameid,hometeamid,47,4,0,5,"2","B",9,0,0,"");
        db.insertRecord(gameid,hometeamid,47,6,0,5,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,29,2,0,6,"1","E",6,0,0,"");
        db.insertRecord(gameid,hometeamid,29,4,0,6,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,29,6,0,6,"無","D",6,1,0,"");
        db.insertRecord(gameid,hometeamid,4,2,0,7,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,4,4,0,7,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,4,6,0,7,"1","E",4,0,0,"");
        db.insertRecord(gameid,hometeamid,31,3,0,8,"1","E",3,0,0,"");
        db.insertRecord(gameid,hometeamid,31,4,0,8,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,31,6,0,8,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,3,3,0,9,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,3,4,0,9,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,3,6,0,9,"全壘打","B",0,0,3,"");
        db.insertRecord(gameid,hometeamid,49,3,0,10,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,49,4,0,10,"無","保送",0,0,1,"");

        db.insertRecord(gameid,awayteamid,25,1,0,1 ,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,25,3,0,1 ,"1","E",4,0,0,"");
        db.insertRecord(gameid,awayteamid,25,4,0,1 ,"2","B",8,0,1,"");
        db.insertRecord(gameid,awayteamid,36,1,0,2 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,36,3,0,2 ,"1","E",4,0,0,"");
        db.insertRecord(gameid,awayteamid,36,4,0,2 ,"無","D",10,1,0,"");
        db.insertRecord(gameid,awayteamid,16,1,0,3 ,"1","E",8,0,0,"");
        db.insertRecord(gameid,awayteamid,16,3,0,3 ,"2","B",7,0,2,"");
        db.insertRecord(gameid,awayteamid,16,4,0,3 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,8,1,0,4 ,"無","D",10,1,0,"");
        db.insertRecord(gameid,awayteamid,8,3,0,4 ,"1","B",10,0,1,"");
        db.insertRecord(gameid,awayteamid,8,5,0,4 ,"無","D",10,1,0,"");
        db.insertRecord(gameid,awayteamid,14,2,0,5 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,14,3,0,5 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,14,5,0,5 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,4,2,0,6 ,"全壘打","無",0,0,1,"");
        db.insertRecord(gameid,awayteamid,4,3,0,6 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,4,5,0,6 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,5,2,0,7 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,5,3,0,7 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,5,5,0,7 ,"2","E",8,0,0,"");
        db.insertRecord(gameid,awayteamid,31,2,0,8 ,"1","B",6,0,0,"");
        db.insertRecord(gameid,awayteamid,31,4,0,8 ,"無","D",6,1,0,"");
        db.insertRecord(gameid,awayteamid,31,5,0,8 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,23,2,0,9 ,"1","B",6,0,0,"");
        db.insertRecord(gameid,awayteamid,23,4,0,9 ,"1","E",6,0,0,"");
        db.insertRecord(gameid,awayteamid,12,2,0,10 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,12,4,0,10 ,"無","保送",0,0,0,"");

        db.updatescore(gameid,5,8);
    }

    public void InputG5Data(String gameid,String awayteamid,String hometeamid) {

        db.insertRecord(gameid,awayteamid,7,1,0,1 ,"無","D",6,1,0,"");
        db.insertRecord(gameid,awayteamid,7,3,0,1 ,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,awayteamid,7,5,0,1 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,15,1,0,2 ,"1","E",6,0,0,"");
        db.insertRecord(gameid,awayteamid,15,3,0,2 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,15,5,0,2 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,29,1,0,3 ,"1","B",10,0,0,"");
        db.insertRecord(gameid,awayteamid,29,3,0,3 ,"無","D",10,1,0,"");
        db.insertRecord(gameid,awayteamid,29,5,0,3 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,6,1,0,4 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,awayteamid,6,3,0,4 ,"1","E",8,0,0,"");
        db.insertRecord(gameid,awayteamid,6,6,0,4 ,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,awayteamid,4,1,0,5 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,4,3,0,5 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,4,6,0,5 ,"無","D",3,1,0,"");
        db.insertRecord(gameid,awayteamid,47,2,0,6 ,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,47,4,0,6 ,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,47,6,0,6 ,"無","D",4,1,0,"");
        db.insertRecord(gameid,awayteamid,51,2,0,7 ,"1","B",7,0,0,"");
        db.insertRecord(gameid,awayteamid,51,4,0,7 ,"無","D",7,1,0,"");
        db.insertRecord(gameid,awayteamid,51,6,0,7 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,60,2,0,8 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,awayteamid,60,4,0,8 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,3,2,0,9 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,3,5,0,9 ,"1","B",3,0,0,"");
        db.insertRecord(gameid,awayteamid,31,2,0,10 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,awayteamid,31,5,0,10 ,"無","K",0,1,0,"");

        db.insertRecord(gameid,hometeamid,4,1,0,1 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,4,3,0,1 ,"1","B",8,0,0,"");
        db.insertRecord(gameid,hometeamid,4,5,0,1 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,10,1,0,2 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,10,3,0,2 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,10,5,0,2 ,"無","D",3,1,0,"");
        db.insertRecord(gameid,hometeamid,31,1,0,3 ,"1","B",6,0,0,"");
        db.insertRecord(gameid,hometeamid,31,3,0,3 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,31,6,0,3 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,16,1,0,4 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,16,3,0,4 ,"1","B",8,0,0,"");
        db.insertRecord(gameid,hometeamid,16,6,0,4 ,"全壘打","B",0,0,1,"");
        db.insertRecord(gameid,hometeamid,5,1,0,5 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,5,3,0,5 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,5,6,0,5 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,12,2,0,6 ,"1","B",5,0,0,"");
        db.insertRecord(gameid,hometeamid,12,3,0,6 ,"無","D",5,1,0,"");
        db.insertRecord(gameid,hometeamid,12,6,0,6 ,"無","D",5,1,0,"");
        db.insertRecord(gameid,hometeamid,11,2,0,7 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,11,4,0,7 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,11,6,0,7 ,"1","B",5,0,0,"");
        db.insertRecord(gameid,hometeamid,23,2,0,8 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,23,4,0,8 ,"無","D",8,1,0,"");
        db.insertRecord(gameid,hometeamid,23,6,0,8 ,"無","保送",0,0,0,"");
        db.insertRecord(gameid,hometeamid,49,2,0,9 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,49,4,0,9 ,"無","D",3,1,0,"");
        db.insertRecord(gameid,hometeamid,49,6,0,9 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,14,2,0,10 ,"無","K",0,1,0,"");
        db.insertRecord(gameid,hometeamid,14,4,0,10 ,"無","K",0,1,0,"");

        db.updatescore(gameid,2,1);
    }
}
