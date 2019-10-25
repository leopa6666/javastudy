package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import java.io.InputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;//
import javax.xml.parsers.DocumentBuilderFactory;//
import org.json.*;

import org.w3c.dom.*;
import java.util.*;
import java.io.*;

@Controller
@Scope("prototype")
public class YahSearch_ctrl {
  
  public String post_yahsearch(HttpServletRequest request) throws Exception {
    System.out.println("start3");
    System.setProperty("javax.net.ssl.trustStore", "jssecacerts.cert");

    //リクエスト start
    String encodedResult = URLEncoder.encode("讃岐うどん", "UTF-8");
    //System.out.println("https://shopping.yahooapis.jp/ShoppingWebService/V1/json/itemSearch?appid="+ yahId +"&query="+encodedResult);
    URL url = new URL("https://shopping.yahooapis.jp/ShoppingWebService/V1/json/itemSearch?appid=dj00aiZpPXZMZUxXMXhBc3FXUyZzPWNvbnN1bWVyc2VjcmV0Jng9NDk-"+"&query="+encodedResult);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    urlConn.connect();
    //Map headerFields = urlConn.getHeaderFields();
    //リクエスト end

    int rspCode = urlConn.getResponseCode();
    StringBuilder builder = new StringBuilder();
    if (rspCode == 200) {
      System.out.println("OK");
      //レスポンスの読み出し(JASON文字列の取得)
      BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
      builder.append(br.readLine());
      String json = br.readLine();
      JSONArray jsonArray = new JSONArray(json);
      //JSON文字列を読み込み、JsonNodeオブジェクトに変換
      for(int i = 0; i <= 9; i++) {
        String hitNum = String.valueOf(i);
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String productName = jsonObject.getString("ResultSet");
        System.out.println(productName);
      }
    }
    return "article_yah";
  }
}