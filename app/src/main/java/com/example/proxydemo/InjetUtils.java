package com.example.proxydemo;

import com.example.proxydemo.annotion.ContentView;

import java.lang.reflect.Method;

/**
 * @author 猿小蔡
 * @name ProxyDemo
 * @class name：com.example.proxydemo
 * @class describe
 * @createTime 2021/3/16 9:44
 * @change
 * @changTime
 */
public class InjetUtils {

  //注入方法  ioc  苦逼
  public static void inject(Object context) {
    //setContentView  的逻辑
    injectLayout(context);
    injectView(context);
    injectClick(context);
  }

  private static void injectLayout(Object context) {
    Class< ? > aClass = context.getClass();
    ContentView contentView = aClass.getAnnotation(ContentView.class);
    if (contentView == null) {
      return;
    }
    int layoutId = contentView.value();
    try {
      //通过反射拿到setContentView方法
      Method setContentView = aClass.getMethod("setContentView", int.class);
      //调用setContentView方法
      setContentView.invoke(context, layoutId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void injectView(Object context) {

  }

  private static void injectClick(Object context) {

  }
}
