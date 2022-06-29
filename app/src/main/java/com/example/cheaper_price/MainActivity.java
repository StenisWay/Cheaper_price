package com.example.cheaper_price;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ListView listView_item;
    private ImageButton search1;
    private EditText edt_search1;
    private FragmentManager fragmentManager= null;
    private long exitTime = 0;
    private ArrayAdapter<String> adapter;
    private LinearLayout layout;
    private TextView txt_empty;
    private SQLiteDatabase db;
    private ArrayList<String> url;
    private FragmentTransaction fragmentTransaction = null;
    private ArrayList<String> id;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        search1 = findViewById(R.id.search_button1);
        edt_search1 = findViewById(R.id.edt_search1);
        search1.setOnClickListener(listener);
        initdb();
        id = new ArrayList<>();
        url = new ArrayList<>();
        cursorthing();
        listView_item.setAdapter(adapter);
        listView_item.setEmptyView(txt_empty);
        listView_item.setOnItemClickListener(listViewlistener);
        listView_item.setOnItemLongClickListener(deletelistviewlistener);
    }



    private void cursorthing(){
        adapter.clear();
        Cursor cursor = db.rawQuery("SELECT _id, title, url, price FROM list", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                adapter.add(cursor.getString(1) + "--------" + cursor.getString(3)+"元");
                id.add(cursor.getString(0));
                url.add(cursor.getString(2));
            }
        }
        cursor.close();
    }

    private AdapterView.OnItemClickListener listViewlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           if (url != null) {
               Uri intenturl = Uri.parse("https://biggo.com.tw"+url.get(i));
               Intent intent = new Intent(Intent.ACTION_VIEW, intenturl);
               startActivity(intent);
           }
        }
    };

    private AdapterView.OnItemLongClickListener deletelistviewlistener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            alert = null;
            builder = new AlertDialog.Builder(mContext);
            alert = builder.setTitle("確認")
                    .setMessage("確認刪除這筆資料")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.execSQL(" DELETE FROM list WHERE _id = "+id.get(position).toString());
                            id.remove(position);
                            cursorthing();
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alert.dismiss();
                        }
                    }).create();
            alert.show();
            return true;
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String search = edt_search1.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("search", search);
            NewSearch newSearch = new NewSearch();
            newSearch.setArguments(bundle);

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
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
            cursorthing();
            adapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}