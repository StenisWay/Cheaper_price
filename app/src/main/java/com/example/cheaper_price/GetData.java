package com.example.cheaper_price;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class GetData {

    private static final String TAG = "GetData";

    /**
     * 抓取什麼值得賣首頁的精選文章
     * @param html
     * @return ArrayList<Article> articles;
     */

    public static ArrayList<Sell> spiderArticle(String html){
        ArrayList<Sell> sells = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements elements = document
                .select("div[class=product-row ]")
                .select("div[class=d-flex w100]");

        Log.i(TAG, "spiderArticle: elements " +elements.html());

        for (Element element : elements){
            String title = element
                    .select("div")
                    .select("div[class=d-flex flex-column justify-content-between list-desc]")
                    .select("div")
                    .select("div[class=d-flex justify-content-center]")
                    .select("div[class=list-product-name line-clamp-2]")
                    .select("a")
                    .text();
            String author = element
                    .select("div")
                    .select("div[class=d-flex flex-column justify-content-between list-desc]")
                    .select("div[class=d-flex justify-content-between]")
                    .select("div")
                    .select("div")
                    .select("a[class=store]")
                    .select("span")
                    .text();
            if (author == "") {
                author = element
                        .select("div")
                        .select("div[class=d-flex flex-column justify-content-between list-desc]")
                        .select("div[class=d-flex justify-content-between]")
                        .select("div")
                        .select("div")
                        .select("div[class=store]")
                        .select("span")
                        .text();
            }
            String price = element
                    .select("div")
                    .select("div[class=d-flex flex-column justify-content-between list-desc]")
                    .select("div[class=d-flex flex-wrap align-items-center]")
                    .select("a")
                    .select("span")
                    .text();

            String imgurl = element
                    .select("div")
                    .select("div[class=list-img-wrap d-flex justify-content-center align-items-center position-relative]")
                    .select("div")
                    .select("img")
                    .attr("src");

            String articleUrl = element
                    .select("div")
                    .select("div[class=d-flex justify-content-center]")
                    .select("div[class=list-product-name line-clamp-2]")
                    .select("a")
                    .attr("href");
            Sell sell = new Sell(title, author, imgurl,price, articleUrl);
            sells.add(sell);

        }
        return sells;

    }
}
