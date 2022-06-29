package com.example.cheaper_price;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class Sell {

    private String title;
    private String author;
    private String imgUrl;
    private String articleUrl;
    private String price;
    private Drawable drawable;

    public Sell(String title, String author, String imgUrl,String price, String articleUrl){
        this.title = title;
        this.author = author;
        this.imgUrl = imgUrl;
        this.articleUrl = articleUrl;
        this.price = price;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }


    public String getArticleUrl(){
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl){
        this.articleUrl = articleUrl;
    }

    public void setPrice(){
        this.price = price;
    }

    public Drawable getImage(){
        return drawable;
    }

    public void setImage(Drawable drawable){
        this.drawable = drawable;
    }

    public String getPrice(){
        return price;
    }

    @NonNull
    @Override
    public String toString() {
        return  "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", price='" + price + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

}
