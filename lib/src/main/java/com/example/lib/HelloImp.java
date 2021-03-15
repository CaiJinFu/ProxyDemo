package com.example.lib;

/**
 * @name ProxyDemo
 * @class name：com.example.lib
 * @class describe
 * @anthor jin
 * @time 2021/3/15 21:11
 * @change
 * @chang time
 */

public class HelloImp implements HelloWorldInterface {

  @Override
  public void hello() {
    System.out.println("这是HelloImp");
  }
}
