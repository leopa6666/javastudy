/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;//
import java.net.HttpsURLConnection;
import java.net.HttpURLConnection;//
import java.net.URL;
import java.io.BufferedReader;
import javax.xml.parsers.DocumentBuilder;//
import javax.xml.parsers.DocumentBuilderFactory;//

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    //test
    return "index";
  }

  @RequestMapping("/calilrec")
  String calil() throws Exception {

    System.setProperty("javax.net.ssl.trustStore", "jssecacerts.cert");

    //HttpsURLConnection.setDefaultHostnameVerifier(hv);
    URL url = new
      URL("http://api.calil.jp/library"+
          "?appkey=eff2329beb9938a9b6443b5795ff2db1&pref=埼玉県");
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();

    System.out.println("sending request...");
    urlConn.setRequestMethod("GET");
    urlConn.setAllowUserInteraction(false); // no user interaction
    urlConn.setDoOutput(true); // want to send
    urlConn.setRequestProperty( "Content-type", "text/xml" );
    urlConn.setRequestProperty( "accept", "text/xml" );
    urlConn.setRequestProperty( "authorization", "Basic ");
    Map headerFields = urlConn.getHeaderFields();
    System.out.println("header fields are: " + headerFields);

    int rspCode = urlConn.getResponseCode();
    if (rspCode == 200) {
        InputStream ist = urlConn.getInputStream();
        InputStreamReader isr = new InputStreamReader(ist);
        BufferedReader br = new BufferedReader(isr);
    
        String nextLine = br.readLine();
        while (nextLine != null) {
            System.out.println(nextLine);
            nextLine = br.readLine();
        }
    }
    /*
    URL url = new URL("http://api.calil.jp/library");
    System.out.println(url);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    conn.setDoOutput(true);
    conn.connect();
    //1. パラメーターを送って
    PrintWriter out = new PrintWriter(conn.getOutputStream());
    out.write("?appkey=eff2329beb9938a9b6443b5795ff2db1&pref=埼玉県");
    out.flush();
    out.close();
    //2. XMLを取得して
    InputStream in = conn.getInputStream();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(in);
    in.close();

    //3. 解析して中身をとりだします。
    NodeList nodes = doc.getElementsByTagName("systemname");
    for(int i=0; i<nodes.getLength();i++)
    {
        System.out.println(nodes.item(i).getTextContent());
    }*/
    return "index";
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
