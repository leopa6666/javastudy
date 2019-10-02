package com.example;

public class Sample {

  @Component
  @Scope(value = "prototype")
  private String input_address;

  public String getInputAddress() {
    return this.input_address;
  }

  public void setInputAddress(String address) {
    this.input_address = address;
  }
}