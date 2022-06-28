package com.example.cheaper_price;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

public class MyBaseAdapter extends BaseAdapter {

    private List<Sell> mSell;
    private Context mContext;
    private SQLiteDatabase db;
    private static final String DB_FILE = "save_list.db", DB_TABLE = "list";

    public MyBaseAdapter(List<Sell> mSell,Context mContext){
        this.mSell = mSell;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mSell!= null ? mSell.size() : 0;
    }

    @Override
    public Sell getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        ViewHolder viewHolder;
        InitImageLoader();//初始化ImageLoader
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.list_name = convertView.findViewById(R.id.list_name);
            viewHolder.list_price = convertView.findViewById(R.id.list_price);
            viewHolder.list_picture = convertView.findViewById(R.id.list_picture);
            viewHolder.list_button = convertView.findViewById(R.id.list_button);
            viewHolder.marketname = convertView.findViewById(R.id.marketname);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.list_picture.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (mSell.get(index).getImgUrl() != null) {
            viewHolder.list_picture.postInvalidate();
            viewHolder.list_picture.setImageDrawable(null);
            ImageLoader.getInstance().displayImage(mSell.get(index).getImgUrl(), viewHolder.list_picture);
        }else {
            viewHolder.list_picture.setImageResource(R.mipmap.ic_launcher);
        }

        viewHolder.list_button.setOnClickListener(null);
        viewHolder.list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mSell.get(index).getTitle();
                String price = mSell.get(index).getPrice();
                String url = mSell.get(index).getArticleUrl();
                db = mContext.openOrCreateDatabase(DB_FILE, mContext.MODE_PRIVATE, null);
                ContentValues newRow = new ContentValues();
                newRow.put("tablenumber", title);
                newRow.put("mainMeal", price);
                newRow.put("secondMeal", url);
                db.insert(DB_TABLE, null, newRow);

            }
        });
        viewHolder.marketname.setText(mSell.get(index).getAuthor());
        viewHolder.list_name.setText(mSell.get(index).getTitle());
        viewHolder.list_price.setText(String.valueOf(mSell.get(index).getPrice()));
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                       break;
                    case MotionEvent.ACTION_UP:
                        Uri uri = Uri.parse("https://biggo.com.tw/"+ mSell.get(index).getArticleUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(intent);
                       break;
                    case MotionEvent.ACTION_CANCEL:
                       break;
                }
                return true;
            }
        });
        return convertView;
    }


    private class ViewHolder{
        TextView list_name, list_price, marketname;
        MyImageView list_picture;
        MyButton list_button;
    }

    /**
     * 配置ImageLoader
     */
    private void InitImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(
                mContext, "imageloader/Cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(mContext, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        // Initialize ImageLoader with configuration.

        ImageLoader.getInstance().init(config);// 全局初始化此配置
        //ImageLoader.getInstance().displayImage("http://192.168.1.100:8080/videonews/images/image1.jpg", imageView);
    }



}
