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
public class StressCheckForm {
  private String scheck1;
  private String scheck2;
  private String scheck3;
  private String scheck4;
  private String scheck5;
  private String scheck6;
  private String scheck7;
  private String scheck8;
  private String scheck9;
  private String scheck10;

}