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

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class ShowRecommandList extends AppCompatActivity {

    TextView teamname,recommandlist;
    Spinner gamenumber;
    Button taken;
    String Stringteam,StringRecommandListShow="";
    BaseballDB db;
    int Intnumber,Intgames;
    String [] Arraytotalgames,Arrayteamid,Arraygameid;
    int[] Arrayback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recommand_list);

        db = new BaseballDB(this);

        gamenumber = (Spinner) findViewById(R.id.Games);
        teamname = (TextView)findViewById(R.id.TeamName);
        recommandlist = (TextView)findViewById(R.id.OrderList);
        taken = (Button)findViewById(R.id.button);
        taken.setOnClickListener(setlist);

        setteam();

        Cursor c = db.selectteamid(Stringteam);
        layoutset(c);
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
            }*/

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


            //計算球員數值
            float[] Arrayba = new float[Arrayback.length];
            float[] Arrayobp = new float[Arrayback.length];
            int[] Arrayrbi = new int[Arrayback.length];
            float tempba,tempobp;
            int temprbi,times;

            for(int i = 0; i< Arrayback.length; i++){

                tempba=0;   tempobp=0;  temprbi=0;  times=0;

                for(int j = 0; j < Intgames; j++){

                    Cursor tempfinaldata_c = db.selectfinalrecord(Arrayteamid[j],Arrayback[i]);
                    String[] tempfinaldatanames = tempfinaldata_c.getColumnNames();
                    tempfinaldata_c.moveToFirst();

                    if(tempfinaldata_c.getCount() > 0){

                        tempba = tempba + tempfinaldata_c.getFloat(tempfinaldata_c.getColumnIndex(tempfinaldatanames[7]));
                        tempobp = tempobp + tempfinaldata_c.getFloat(tempfinaldata_c.getColumnIndex(tempfinaldatanames[8]));
                        temprbi = temprbi + tempfinaldata_c.getInt(tempfinaldata_c.getColumnIndex(tempfinaldatanames[12]));
                        times++;
                    }

                }

                if(times == 0){

                    tempba = 0;
                    tempobp = 0;
                    temprbi = 0;
                }else{

                    tempba = (float)tempba/times;
                    tempobp = (float)tempobp/times;
                }
                Arrayba[i] = tempba;
                Arrayobp[i] = tempobp;
                Arrayrbi[i] = temprbi;
            }

            int[] ArrayRecommandback = new int[10];
            float[] ArrayRecommandobp = new float[10];
            float[] ArrayRecommandba = new float[10];
            int[]  ArrayRecommandrbi = new int[10];


            /*
                           1    obp1
                           2    ba2
                           3    ba1
                           4    rbi1
                           5    rbi2
                           6    rbi3
                           7    ba3
                           8    ba4
                           9    obp3
                           10  obp2
                        */

            int check = 0, defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
                ArrayRecommandback[3] = Arrayback[defaltcheck];
                ArrayRecommandba[3] = Arrayba[defaltcheck];
                ArrayRecommandobp[3] = Arrayobp[defaltcheck];
                ArrayRecommandrbi[3] = Arrayrbi[defaltcheck];

            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandrbi[3]<=Arrayrbi[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[3] = Arrayback[i];
                    ArrayRecommandba[3] = Arrayba[i];
                    ArrayRecommandobp[3] = Arrayobp[i];
                    ArrayRecommandrbi[3] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[2] = Arrayback[defaltcheck];
            ArrayRecommandba[2] = Arrayba[defaltcheck];
            ArrayRecommandobp[2] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[2] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandba[2]<=Arrayba[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[2] = Arrayback[i];
                    ArrayRecommandba[2] = Arrayba[i];
                    ArrayRecommandobp[2] = Arrayobp[i];
                    ArrayRecommandrbi[2] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[0] = Arrayback[defaltcheck];
            ArrayRecommandba[0] = Arrayba[defaltcheck];
            ArrayRecommandobp[0] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[0] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandobp[0]<=Arrayobp[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[0] = Arrayback[i];
                    ArrayRecommandba[0] = Arrayba[i];
                    ArrayRecommandobp[0] = Arrayobp[i];
                    ArrayRecommandrbi[0] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[1] = Arrayback[defaltcheck];
            ArrayRecommandba[1] = Arrayba[defaltcheck];
            ArrayRecommandobp[1] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[1] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandba[1]<=Arrayba[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[1] = Arrayback[i];
                    ArrayRecommandba[1] = Arrayba[i];
                    ArrayRecommandobp[1] = Arrayobp[i];
                    ArrayRecommandrbi[1] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[4] = Arrayback[defaltcheck];
            ArrayRecommandba[4] = Arrayba[defaltcheck];
            ArrayRecommandobp[4] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[4] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandrbi[4]<=Arrayrbi[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[4] = Arrayback[i];
                    ArrayRecommandba[4] = Arrayba[i];
                    ArrayRecommandobp[4] = Arrayobp[i];
                    ArrayRecommandrbi[4] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[9] = Arrayback[defaltcheck];
            ArrayRecommandba[9] = Arrayba[defaltcheck];
            ArrayRecommandobp[9] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[9] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandobp[9]<=Arrayobp[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[9] = Arrayback[i];
                    ArrayRecommandba[9] = Arrayba[i];
                    ArrayRecommandobp[9] = Arrayobp[i];
                    ArrayRecommandrbi[9] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[5] = Arrayback[defaltcheck];
            ArrayRecommandba[5] = Arrayba[defaltcheck];
            ArrayRecommandobp[5] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[5] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandrbi[5]<=Arrayrbi[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[5] = Arrayback[i];
                    ArrayRecommandba[5] = Arrayba[i];
                    ArrayRecommandobp[5] = Arrayobp[i];
                    ArrayRecommandrbi[5] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[6] = Arrayback[defaltcheck];
            ArrayRecommandba[6] = Arrayba[defaltcheck];
            ArrayRecommandobp[6] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[6] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandba[6]<=Arrayba[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[6] = Arrayback[i];
                    ArrayRecommandba[6] = Arrayba[i];
                    ArrayRecommandobp[6] = Arrayobp[i];
                    ArrayRecommandrbi[6] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            check = 0;
            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[7] = Arrayback[defaltcheck];
            ArrayRecommandba[7] = Arrayba[defaltcheck];
            ArrayRecommandobp[7] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[7] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandba[7]<=Arrayba[i]&&Arrayback[i]!=-1) {

                    check = i;
                    ArrayRecommandback[7] = Arrayback[i];
                    ArrayRecommandba[7] = Arrayba[i];
                    ArrayRecommandobp[7] = Arrayobp[i];
                    ArrayRecommandrbi[7] = Arrayrbi[i];
                }
            }
            Arrayback[check] = -1;

            defaltcheck=0;
            while(Arrayback[defaltcheck]==-1) {
                defaltcheck++;
            }
            ArrayRecommandback[8] = Arrayback[defaltcheck];
            ArrayRecommandba[8] = Arrayba[defaltcheck];
            ArrayRecommandobp[8] = Arrayobp[defaltcheck];
            ArrayRecommandrbi[8] = Arrayrbi[defaltcheck];
            for(int i = 0; i < Arrayback.length; i++){
                if(ArrayRecommandobp[8]<=Arrayobp[i]&&Arrayback[i]!=-1) {

                    ArrayRecommandback[8] = Arrayback[i];
                    ArrayRecommandba[8] = Arrayba[i];
                    ArrayRecommandobp[8] = Arrayobp[i];
                    ArrayRecommandrbi[8] = Arrayrbi[i];
                }
            }

            DecimalFormat df = new DecimalFormat("#.##");

            StringRecommandListShow = "";
            for(int i = 1; i <= 10; i++){
                StringRecommandListShow = StringRecommandListShow + "第 "+ i +" 棒 =>"
                        + ArrayRecommandback[i-1] + "  號   " + "打擊率  :  "
                        + df.format(ArrayRecommandba[i-1]) + "   上壘率 : "
                        + df.format(ArrayRecommandobp[i-1]) + "   打點 : "
                        + ArrayRecommandrbi[i-1] + "  分\n\n";

            }

            recommandlist.setText(StringRecommandListShow);


        }
    };

    private void setteam(){

        Intent intent = getIntent();
        Stringteam = intent.getStringExtra(ShowTeamList.Choice);
        teamname.setText(Stringteam);
    }

    private void layoutset(Cursor c){

        Intnumber = c.getCount();
        Arraytotalgames = new String[Intnumber];
        for(int i = 0; i < Intnumber; i++ )
            Arraytotalgames[i] = i+1+"";
        ArrayAdapter<String> adapterlist = new ArrayAdapter<String>(ShowRecommandList.this,android.R.layout.simple_spinner_dropdown_item,Arraytotalgames);
        gamenumber.setAdapter(adapterlist);
    }


}

