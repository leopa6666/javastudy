package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Component
public class StressCheckResource {

  public List<String> checklist1 = new ArrayList<String>(){
    {
      add("当てはまる");
      add("少し当てはまる");
      add("どちらでもない");
      add("あまり当てはまらない");
      add("当てはまらない");
    }
  };

  public Map<String, String> questionMap = new TreeMap<String,String>(){
    {
      put("q1","頻繁に頭痛がする");
      put("q2","いくら寝ても眠い");
      put("q3","手足がしびれることがある");
      put("q4","寝つきが悪い");
      put("q5","怒りっぽくなった");
    }
  };

  public Map<String, List<String>> checkitems = new TreeMap<String, List<String>>() {
    {
        put("q1", checklist1);
        put("q2", checklist1);
        put("q3", checklist1);
    }
  };  
}