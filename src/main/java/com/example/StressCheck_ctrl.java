package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Scope("prototype")
public class StressCheck_ctrl {



  public String post_stresscheck(HttpServletRequest request) throws Exception {

    
    System.out.println(request.getAttribute("resource"));
    return "article_stresscheck";
  }
}