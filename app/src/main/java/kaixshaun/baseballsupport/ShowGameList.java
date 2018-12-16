package kaixshaun.baseballsupport;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

public class ShowGameList extends AppCompatActivity {

    public static final String Choice = "Choice";
    ListView listView;
    String[] templist,names;
    ArrayAdapter<String> listAdapter;
    BaseballDB db;
    String choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_game_list);

        db = new BaseballDB(this);

        Cursor c = db.selectgamename();
        templist = new String[c.getCount()];
        names = c.getColumnNames();

        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){

            templist[i] = c.getString(c.getColumnIndex(names[0]));
            c.moveToNext();
        }

        listView = (ListView)findViewById(R.id.GameList);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templist);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                choice = templist[position];
                Intent intent = new Intent();
                intent.putExtra(Choice,choice);
                intent.setClass(ShowGameList.this,ShowGameData.class);
                startActivity(intent);
                ShowGameList.this.finish();
            }
        });
    }
}
