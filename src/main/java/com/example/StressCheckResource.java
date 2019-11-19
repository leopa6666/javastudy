package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Component
public class StressCheckResource {

  public List<String> checklist1 = new ArrayList<String>(){
    {
      add('当てはまる');
      add('少し当てはまる');
      add('どちらでもない');
      add('あまり当てはまらない');
      add('当てはまらない');
    }
  };

  public Map<String, String> checkitems = new TreeMap<String, String>() {
    {
        put("質問1", checklist1);
        put("質問2", checklist1);
    }
  };  
}