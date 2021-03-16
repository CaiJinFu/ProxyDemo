package com.example.proxydemo;

import android.view.View;

import com.example.proxydemo.annotion.ContentView;
import com.example.proxydemo.annotion.ViewInject;

import java.lang.reflect.Field;
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

  //注入方法  i
  public static void inject(Object context) {
    //setContentView  的逻辑
    injectLayout(context);
    //findViewById
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
    Class< ? > aClass = context.getClass();
    Field[] declaredFields = aClass.getDeclaredFields();
    for (Field field : declaredFields) {
      ViewInject viewInject = field.getAnnotation(ViewInject.class);
      if (viewInject == null) {
        continue;
      }
      int valueId = viewInject.value();
      try {
        Method findViewById = aClass.getMethod("findViewById", int.class);
        View view = (View) findViewById.invoke(context, valueId);
        field.setAccessible(true);
        field.set(context, view);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void injectClick(Object context) {

  }
}
