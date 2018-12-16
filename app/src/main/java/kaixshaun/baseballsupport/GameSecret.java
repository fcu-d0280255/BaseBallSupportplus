package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GameSecret extends AppCompatActivity {

    ListView listView;
    String[] templist,names;
    ArrayAdapter<String> listAdapter;
    BaseballDB db;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_secret);

        db = new BaseballDB(this);

        Cursor c = db.selectgamename();
        templist = new String[c.getCount()];
        names = c.getColumnNames();

        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){

            templist[i] = c.getString(c.getColumnIndex(names[0]));
            c.moveToNext();
        }

        listView = (ListView)findViewById(R.id.List);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templist);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                choice = templist[position];
                Cursor tempc = db.selectGameID(choice);
                String[] names = tempc.getColumnNames();
                tempc.moveToFirst();
                String gameid = tempc.getString(tempc.getColumnIndex(names[0]));
                db.deletegame(gameid);
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
    }

}
