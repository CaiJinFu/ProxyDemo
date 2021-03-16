package com.example.proxydemo;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 猿小蔡
 * @name ProxyDemo
 * @class name：com.example.proxydemo
 * @class describe
 * @createTime 2021/3/16 12:26
 * @change
 * @changTime
 */
public class ListenerInvocationHandler implements InvocationHandler {

  private Object mObject;
  private Method mMethod;

  public ListenerInvocationHandler(Object object, Method method) {
    mObject = object;
    mMethod = method;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Log.i("TAG", "invoke: ");
    return mMethod.invoke(mObject, args);
  }
}
