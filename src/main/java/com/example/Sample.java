package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope("prototype")
public class Sample {

  private String input_address;

  public String getInputAddress() {
    return this.input_address;
  }

  public void setInputAddress(String address) {
    this.input_address = address;
  }
}