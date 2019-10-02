package com.example;

@Component
@Scope(value = "prototype")
public class Sample {
  private String input_address;

  public String getInputAddress() {
    return this.input_address;
  }

  public void setInputAddress(String address) {
    this.input_address = address;
  }
}