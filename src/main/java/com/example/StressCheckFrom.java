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
import lombok.Data;

@Data
public class StressCheckResource {

  private String scheck1; // 案件区分
  private String anken_name; // 案件名 
}