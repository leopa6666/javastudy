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
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;//
import javax.xml.parsers.DocumentBuilderFactory;//

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import javax.servlet.http.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

//package com.example.MainConstants;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;


  public static String[] testlist = new String[47];

  public static void main(String[] args) throws Exception {
    testlist = MainConstants.address;
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
    request.setAttribute("option", MainConstants.address);
    return "index";
  }

  @RequestMapping("/calilrec")
  String calil(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception {

    System.setProperty("javax.net.ssl.trustStore", "jssecacerts.cert");
    //選択した都道府県
    String input_address = request.getParameter("example");
    request.setAttribute("option", MainConstants.address);

    String encodedResult = URLEncoder.encode(input_address, "UTF-8");
    URL url = new URL("https://api.calil.jp/library?appkey=eff2329beb9938a9b6443b5795ff2db1&pref="+ encodedResult);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");

    urlConn.connect();
    Map headerFields = urlConn.getHeaderFields();
    //System.out.println("header fields are: " + headerFields);

    int rspCode = urlConn.getResponseCode();
    if (rspCode == 200) {
        InputStream in = urlConn.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        in.close();
    
        //3. 解析して中身をとりだします。
        Element bookList = doc.getDocumentElement();
        NodeList nodes = bookList.getElementsByTagName("Library");
        ArrayList<String> liblist = new ArrayList<String>();
        Map<Integer, Object> libmap = new HashMap<>();
        for(int i=0; i<nodes.getLength();i++) {
          Node personNode = nodes.item(i);
          NodeList chnodes = nodes.item(i).getChildNodes();
          for(int j=0; j<chnodes.getLength();j++){
            if(chnodes.item(j).getNodeName() == "formal"){
              liblist.add(chnodes.item(j).getTextContent());
            }else if(chnodes.item(j).getNodeName() == "url_pc"){
              liblist.add(chnodes.item(j).getTextContent());
            }else if(chnodes.item(j).getNodeName() == "address"){
              liblist.add(chnodes.item(j).getTextContent());
            }else if(chnodes.item(j).getNodeName() == "post"){
              liblist.add(chnodes.item(j).getTextContent());
            }else if(chnodes.item(j).getNodeName() == "category"){
              liblist.add(chnodes.item(j).getTextContent());
            }
          }
          libmap.put(i, liblist);
        }
        model.put("libmap",libmap);
    }
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
