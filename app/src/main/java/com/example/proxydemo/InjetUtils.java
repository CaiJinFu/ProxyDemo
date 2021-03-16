package com.example.proxydemo;

import android.view.View;

import com.example.proxydemo.annotion.ContentView;
import com.example.proxydemo.annotion.EventBase;
import com.example.proxydemo.annotion.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
    Class< ? > clazz = context.getClass();
    //获取到例如activity所有的方法对象
    Method[] methods = clazz.getDeclaredMethods();
    for (Method method : methods) {
      //获取到所有的方法上面的注解
      Annotation[] annotations = method.getAnnotations();
      for (Annotation annotation : annotations) {
        //获取注解的类型
        Class< ? > annotationType = annotation.annotationType();
        EventBase eventBase = annotationType.getAnnotation(EventBase.class);
        if (eventBase == null) {
          continue;
        }
        //拿到事件三要素
        //setOnLongClickListener
        String listenerSetter = eventBase.listenerSetter();
        //OnLongClickListener.class
        Class< ? > listenerType = eventBase.listenerType();
        //事件被触发之后，执行的回调方法的名称 onLongClick
        String callBackMethod = eventBase.callbackMethod();
        try {
          //getDeclaredMethod：方法返回一个Method对象，它反映此Class对象所表示的类或接口的指定已声明方法。
          //获取viewId
          Method valueMethod = annotationType.getDeclaredMethod("value");
          int[] viewId = (int[]) valueMethod.invoke(annotation);
          for (int id : viewId) {
            //获取findViewById方法对象
            Method findViewById = clazz.getMethod("findViewById", int.class);
            //通过findViewById找到具体的view对象
            View view = (View) findViewById.invoke(context, id);
            ListenerInvocationHandler listenerInvocationHandler =
                new ListenerInvocationHandler(context, method);
            Class< ? extends View > viewClass = view.getClass();
            //获取view的setOnLongClickListener方法对象
            Method setListener = viewClass.getMethod(listenerSetter, listenerType);
            Object proxy =
                Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType},
                    listenerInvocationHandler);
            setListener.invoke(view, proxy);
            //                        view.setOnClickListener(动态代理);
            //                        view.setOnClickListener(new View.OnClickListener() {
            //                            @Override
            //                            public void onClick(View v) {
            //
            //                            }
            //                        });
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
