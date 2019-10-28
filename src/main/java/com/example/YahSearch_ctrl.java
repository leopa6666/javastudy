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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    String input_keyword = request.getParameter("keyword");
    System.out.println("input_keyword"+input_keyword);
    String encodedResult = URLEncoder.encode(input_keyword, "UTF-8");
    String yahId = System.getenv("YAHOO_CLIENT_ID");
    String yahQuery = create_query(request);
    URL url = new URL("https://shopping.yahooapis.jp/ShoppingWebService/V1/json/itemSearch?appid="+ yahId + yahQuery);
    //URL url = new URL("https://shopping.yahooapis.jp/ShoppingWebService/V1/json/itemSearch?appid="+ yahId +"&query="+encodedResult);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    urlConn.connect();
    //リクエスト end

    StringBuilder builder = new StringBuilder();
    int rspCode = urlConn.getResponseCode();
    if (rspCode == 200) {
      System.out.println("OK");
      //レスポンスの読み出し(JASON文字列の取得)
      String line;
      BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
      while ((line = br.readLine()) != null) {
        builder.append(line);
      }
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(builder.toString());
      System.out.println("★★Size★★"+root.get("ResultSet").get("totalResultsReturned"));
      System.out.println("★★Size★★"+root.get("ResultSet").get("0").get("Result").get("0") );
      for(Integer i=0;i<=root.get("ResultSet").get("totalResultsReturned");i++){
        System.out.println("★★S★★");
        System.out.println(root.get("ResultSet").get("0").get("Result").get(i.toString()).get("Name").textValue());
        System.out.println("★★E★★");
      }
      //System.out.println(root.get("ResultSet").get("0").get("Result").get("0").get("Name").textValue());
    }
    return "article_yah";
  }

  private String create_query(HttpServletRequest request) throws Exception  {
    String resultQuery = "";
    if(request.getParameter("keyword") != null){
      String input_keyword = request.getParameter("keyword");
      String encodedResult = URLEncoder.encode(input_keyword, "UTF-8");
      resultQuery = "&query=" + encodedResult;
    }
    return resultQuery;
  }
}