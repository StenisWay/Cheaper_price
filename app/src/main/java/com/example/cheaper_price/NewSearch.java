package com.example.cheaper_price;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.FocusFinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;


public class NewSearch extends Fragment {

    private View view, footView;
    ArrayList<Sell> sells = null;
    String searchitem = null;
    int page = 1;
    String highprice = "99999999";
    String lowprice = "1";
    String url = null;
    private MyBaseAdapter myBaseAdapter;
    final static String TAG = "NewSearch";
    private EditText edt_search2, edt_lowest, edt_highiest;
    private ImageButton search2_button;
    private ListView listView_newSearch_Item;
    private MyButton btn_NextPage;
    private ImageView list_picture;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i(TAG, "handleMessage: "+ "爬結束");
            switch (msg.what){
                case 1:
                    Log.i(TAG, "handleMessage " + "開始展示數據");
                    sells = (ArrayList<Sell>) msg.obj;
                    Log.i(TAG, "handleMessage:articles.size() " + sells.size());
                    myBaseAdapter = new MyBaseAdapter(sells, getContext());
                    listView_newSearch_Item.addFooterView(footView);
                    listView_newSearch_Item.setAdapter(myBaseAdapter);


                    break;
                case 2:
                    Toast.makeText(getContext(), "這是最一頁了", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_search, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        footView = inflater.inflate(R.layout.view_footer, null, false);
        searchitem = getArguments().getString("search");
        url =  "https://biggo.com.tw/s/"+searchitem+"/?price="+lowprice+"-"+highprice+"&sort=lp&p="+page;
        Search();
        init();

        return view;
    }

    private void init(){

        edt_search2 = view.findViewById(R.id.edt_search2);
        edt_lowest = view.findViewById(R.id.edt_lowest);
        edt_highiest = view.findViewById(R.id.edt_highiest);
        search2_button = view.findViewById(R.id.search2_button);
        search2_button.setOnClickListener(searchlistner);
        edt_search2.setText(searchitem);

        btn_NextPage = footView.findViewById(R.id.btn_NextPage);
        btn_NextPage.setOnClickListener(pagelistener);
        listView_newSearch_Item = view.findViewById(R.id.listView_newSearch_Item);

    }



    private View.OnClickListener searchlistner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
              lowprice = edt_lowest.getText().toString();
              highprice = edt_highiest.getText().toString();
              searchitem = edt_search2.getText().toString();
              url = "https://biggo.com.tw/s/"+searchitem+"/?price="+lowprice+"-"+highprice+"&sort=lp&p="+page;
              Search();
        }
    };



    private void Search(){
        new Thread() {
            public void run() {
                String html = OkHttpUtils.OkGetArt(url);
                Log.i("test", html);
                ArrayList<Sell> sells = GetData.spiderArticle(html);
                if (getwrongurl(html).equals("https://biggo.com.tw/s/"+ searchitem +"/")){
                    Message message2 = handler.obtainMessage();
                    message2.what=2;
                    handler.sendMessage(message2);
                }else {
                    //發送信息給handler用於更新UI介面

                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = sells;
                    Log.i("test", sells.toString());
                    handler.sendMessage(message);
                }
          
            }
        }.start();
    }

    private View.OnClickListener pagelistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            page += 1;
            url = "https://biggo.com.tw/s/"+searchitem+"/?price="+lowprice+"-"+highprice+"&sort=lp&p="+page;
            Search();
        }
    };

    private String getwrongurl(String html){
        Document document = Jsoup.parse(html);
        String wrongurl = document
                .select("link[rel=canonical]")
                .attr("href");
        return wrongurl;
    }

}