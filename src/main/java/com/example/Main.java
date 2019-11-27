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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Scope;

import javax.servlet.http.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView; 
import org.springframework.web.bind.annotation.ModelAttribute;

//package com.example.MainConstants;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.*;

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

  StressCheckResource scResource = new StressCheckResource();  
  StressCheck_ctrl ctrl = new StressCheck_ctrl();

  @RequestMapping("/")
  String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return "index";
  }

  @ModelAttribute
  StressCheckResource setUpForm() {
      return new StressCheckResource();
  }

  @RequestMapping(value = "/contents/stresscheck", method = RequestMethod.GET)
  public ModelAndView getcheck(ModelAndView mav) throws Exception {
    /*mav.setViewName("article_stresscheck");
    mav.addObject("resource", new StressCheckForm());
    mav.addObject("answers", scResource.checklist1);*/
    mav = ctrl.get_stresscheck(mav);
    return mav;
  }
  @RequestMapping(value = "/contents/stresscheck", method = RequestMethod.POST)
  public ModelAndView setcheck(@ModelAttribute("resource") StressCheckForm form,ModelAndView mav,HttpServletRequest request) throws Exception {
    mav = ctrl.post_stresscheck(form,mav,request);   
    return mav;
  }

  @RequestMapping(path = "/content/{content_no}", method = RequestMethod.GET)
  String getarticle(HttpServletRequest request, HttpServletResponse response, @PathVariable String content_no ,Model model) throws Exception {
    
    if(content_no.equals("lib")){
      LibSerchResource input_SerchResource = new LibSerchResource();
      request.setAttribute("option", MainConstants.address);//都道府県
      request.setAttribute("input_info", input_SerchResource);
    }else if(content_no.equals("yah")){
      request.setAttribute("keyword", "初期値");//都道府県
    }else if(content_no.equals("jpmap")){
      //request.setAttribute("keyword", "初期値");//都道府県
    }else if(content_no.equals("stresscheck")){
      StressCheckResource scResource = new StressCheckResource();
      //request.setAttribute("resource",new StressCheckResource());
      //request.setAttribute("checkitems", scResource.questionMap);//質問マップ
      //request.setAttribute("answers", scResource.checklist1);//回答マップ
    }
    return "article_" + content_no;
  }

  @RequestMapping(path = "/content/{content_no}", method = RequestMethod.POST)
  String setarticle(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception {

    String content_key = request.getParameter("form");
    System.out.println("start1");
    if(content_key.equals("lib")){
      LibSearch_ctrl libs_ctrl = new LibSearch_ctrl();
      return libs_ctrl.post_libsearch(request);
    }else if(content_key.equals("yah")){
      System.out.println("start2");
      YahSearch_ctrl yah_ctrl = new YahSearch_ctrl();
      return yah_ctrl.post_yahsearch(request);
    }else if(content_key.equals("stresscheck")){
      //StressCheck_ctrl scheck_ctrl = new StressCheck_ctrl();
      //return scheck_ctrl.post_stresscheck(request);
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
