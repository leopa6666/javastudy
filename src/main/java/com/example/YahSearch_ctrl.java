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

import org.w3c.dom.*;
import java.util.*;

@Controller
@Scope("prototype")
public class YahSearch_ctrl {
  
  @Value("${spring.yclientid.url}")
  private String yahId;

  public String post_yahsearch(HttpServletRequest request) throws Exception {
    
    System.setProperty("javax.net.ssl.trustStore", "jssecacerts.cert");
    //選択した都道府県セット
    String input_address = request.getParameter("example");
    LibSerchResource input_SerchResource = new LibSerchResource();
    input_SerchResource.setInputAddress(input_address);
    //Modelへ
    request.setAttribute("input_info", input_SerchResource);
    request.setAttribute("option", MainConstants.address);

    //リクエスト start
    String encodedResult = URLEncoder.encode("讃岐うどん", "UTF-8");
    URL url = new URL("https://shopping.yahooapis.jp/ShoppingWebService/V1/json/itemSearch?appid="+ yahId +"&query="+encodedResult);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    urlConn.connect();
    //Map headerFields = urlConn.getHeaderFields();
    //リクエスト end

    int rspCode = urlConn.getResponseCode();
    if (rspCode == 200) {
        System.out.println("OK");
      }
    return "article_yah";
  }
}