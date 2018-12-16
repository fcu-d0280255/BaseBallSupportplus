package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class ShowODStrategy extends AppCompatActivity {

    TextView teamname,ODlist;
    Spinner gamenumber;
    Button taken;
    String Stringteam,ODShow;
    BaseballDB db;
    int Intnumber,Intgames;
    String [] Arraytotalgames,Arrayteamid,Arraygameid;
    int[] Arrayback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_odstrategy);

        db = new BaseballDB(this);
        gamenumber = (Spinner) findViewById(R.id.Games);
        teamname = (TextView)findViewById(R.id.TeamName);
        ODlist = (TextView)findViewById(R.id.DataShow);
        taken = (Button)findViewById(R.id.Analysis);

        setteam();
        Cursor c = db.selectteamid(Stringteam);
        layoutset(c);
        taken.setOnClickListener(setlist);

    }

    private View.OnClickListener setlist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intgames = Integer.parseInt(gamenumber.getSelectedItem().toString());
            Arrayteamid = new String[Intgames];
            Arraygameid = new String[Intgames];

            //取得那幾場的teamid
            Cursor teamid_c = db.selectteamid(Stringteam);
            String [] names = teamid_c.getColumnNames();
            teamid_c.moveToFirst();

            for(int i = 0; i < Intgames; i++){

                Arrayteamid[i] = teamid_c.getString(teamid_c.getColumnIndex(names[0]));
                teamid_c.moveToNext();
            }

            /*
            //取得那幾場的gameid
            for(int i = 0; i < Intgames; i++) {

                Cursor temp_c = db.selectgameid(Arrayteamid[i]);
                String[] tempnames = temp_c.getColumnNames();
                temp_c.moveToFirst();
                Arraygameid[i] = temp_c.getString(temp_c.getColumnIndex(tempnames[0]));
            }
            */

            //取得那幾場的背號
            int[] tempback = new int[Intgames*10];
            int index = 0;
            for (int i = 0; i < Intgames; i++){

                Cursor tempback_c = db.selestorder(Arrayteamid[i]);
                String[] tempnames = tempback_c.getColumnNames();
                tempback_c.moveToFirst();
                for(int j = index; j < index+10; j++){

                    tempback[j] = tempback_c.getInt(tempback_c.getColumnIndex(tempnames[3]));
                    tempback_c.moveToNext();
                }
                index = index+10;
            }

            //刪除重複背號
            Set<Integer> intSet = new HashSet<Integer>();
            for (int element : tempback) {
                intSet.add(element);
            }
            Arrayback = new int[intSet.size()];
            Object[] tempArray = intSet.toArray();
            for (int i = 0; i < tempArray.length; i++) {
                Arrayback[i] = (int) tempArray[i];
            }
            for(int i = 0; i < Arrayback.length; i++ )
                Log.v(i+"=>",Arrayback[i]+"");


            ODShow = "";
            for(int i = 0; i < Arrayback.length; i++){

                int error = 0;
                ODShow = ODShow + Arrayback[i] + "號  ";

                for(int j = 0; j < Intgames; j++){

                    Cursor c = db.selectfinalrecord(Arrayteamid[j],Arrayback[i]);
                    String[] cnames = c.getColumnNames();
                    c.moveToFirst();
                    if(c.getCount() > 0)
                        error = error + c.getInt(c.getColumnIndex(cnames[11]));

                }

                ODShow = ODShow + "失誤 : " + error +"次  飛向: ";
                for(int j = 0; j < Intgames; j++){

                    Cursor c = db.selectrecording(Arrayteamid[j],Arrayback[i]);
                    String[] cnames = c.getColumnNames();
                    c.moveToFirst();

                    for(int k = 0; k < c.getCount(); k++){

                        if(c.getInt(c.getColumnIndex(cnames[9]))!=0)
                            ODShow = ODShow + c.getInt(c.getColumnIndex(cnames[9])) +" ";
                        c.moveToNext();
                    }
                }
                ODShow = ODShow + "\n\n";
            }
            ODlist.setText(ODShow);
        }
    };

    private void setteam(){

        Intent intent = getIntent();
        Stringteam = intent.getStringExtra(EnemyList.Choice);
        teamname.setText(Stringteam);
    }

    private void layoutset(Cursor c){

        Intnumber = c.getCount();
        Arraytotalgames = new String[Intnumber];
        for(int i = 0; i < Intnumber; i++ )
            Arraytotalgames[i] = i+1+"";
        ArrayAdapter<String> adapterlist = new ArrayAdapter<String>(ShowODStrategy.this,android.R.layout.simple_spinner_dropdown_item,Arraytotalgames);
        gamenumber.setAdapter(adapterlist);
    }
}
