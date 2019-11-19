package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.TreeMap;
import java.util.Map;

@Component
public class StressCheckResource {

  public Map<String, String> checkitems = new TreeMap<String, String>() {
    {
        put("質問1", "内容1");
        put("質問2", "内容2");
    }
};
}