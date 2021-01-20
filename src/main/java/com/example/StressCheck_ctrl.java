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
    mav.addObject("page","stresscheck1");
    mav.addObject("resource", new StressCheckForm());
    mav.addObject("answers", scResource.checklist1);
    return mav;
  }

  public ModelAndView post_stresscheck(@ModelAttribute("resource") StressCheckForm form,ModelAndView mav,HttpServletRequest request) throws Exception {
    mav.setViewName("article_stresscheck");
    mav.addObject("resource", form);
    mav.addObject("page","stresscheck1");
    String req = request.getParameter("form");
    if(req.equals("stresscheck1")){
      //System.out.println("★2");
      if(request.getParameter("action").equals("next")){
        mav.addObject("page","stresscheck2");
        mav.addObject("answers", scResource.checklist1);
      }
    }else if(req=="stresscheck2"){
      if(request.getParameter("action")=="next"){
        mav.addObject("page","stresscheck2");
        mav.addObject("answers", scResource.checklist1);
      }
    }
    System.out.println("★aaa"+form);
    return mav;
  }
}