package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.stereotype.Controller;


@Controller
@Scope("prototype")
public class StressCheck_ctrl {



  public String post_stresscheck(HttpServletRequest request) throws Exception {

    //checkitems
    return "article_stresscheck";
  }
}