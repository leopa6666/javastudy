package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView; 
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Scope("prototype")
public class StressCheck_ctrl {

  StressCheckResource scResource;
  public StressCheck_ctrl(){
    scResource = new StressCheckResource();  
  }
  

  public ModelAndView get_stresscheck(ModelAndView mav) throws Exception {

    mav.setViewName("article_stresscheck");
    mav.addObject("resource", new StressCheckForm());
    mav.addObject("answers", scResource.checklist1);
    return mav;
  }

  public String post_stresscheck(@ModelAttribute("resource") StressCheckForm form,ModelAndView mav,HttpServletRequest request) throws Exception {

    
    System.out.println(request.getAttribute("resource"));
    return "article_stresscheck";
  }
}