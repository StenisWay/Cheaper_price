package com.example.cheaper_price;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ListView listView_item;
    private ImageButton search1;
    private EditText edt_search1;
    private FragmentManager fragmentManager= null;
    private long exitTime = 0;
    private ArrayAdapter<String> adapter;
    private String[] saveItem;
    private LinearLayout layout;
    private TextView txt_empty;
    private SQLiteDatabase db;

    private static final String DB_FILE = "save_list.db", DB_TABLE = "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        init();
    }

    private void init() {
        listView_item = (ListView) findViewById(R.id.listView_item);
        layout = (LinearLayout) findViewById(R.id.searchTitle);
        txt_empty = (TextView) findViewById(R.id.txt_empty);
        txt_empty.setText("暫無數據");
        if (saveItem != null) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, saveItem);
        }
        search1 = findViewById(R.id.search_button1);
        edt_search1 = findViewById(R.id.edt_search1);
        search1.setOnClickListener(listener);
        listView_item.setAdapter(adapter);
        listView_item.setEmptyView(txt_empty);
        initdb();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragmentManager= getSupportFragmentManager();
            String search = edt_search1.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("search", search);
            NewSearch newSearch = new NewSearch();
            newSearch.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(android.R.id.content, newSearch);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            listView_item.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.INVISIBLE);
            fragmentTransaction.commit();
        }
    };


    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            fragmentManager.popBackStack();
            listView_item.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    private void initdb(){
        //資料庫建立、資料表建立
        db = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
        try {
            db.execSQL(" CREATE TABLE " + DB_TABLE +
                    "(_id INTEGER PRIMARY KEY," + "title TEXT NOT NULL," + "url TEXT," + "price TEXT);");
        }catch (Exception e){

        }

        db.close();
    }

}