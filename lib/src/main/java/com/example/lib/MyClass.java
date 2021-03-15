package com.example.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

public class MyClass {
  public static void main(String[] args) {
    HelloImp helloImp = new HelloImp();
    HelloWorldInterface helloWorldInterface = (HelloWorldInterface) Proxy
        .newProxyInstance(helloImp.getClass().getClassLoader(), helloImp.getClass().getInterfaces(),
            new InvocationHandlerImp(helloImp));
    helloWorldInterface.hello();

    byte[] bytes =
        ProxyGenerator.generateProxyClass("JackfruitHelloImpl", new Class[]{HelloWorldInterface.class});
    File file = new File(
        "E:\\JackfruitProject\\ProxyDemo\\lib\\src\\main\\java\\com\\example\\lib\\JackfruitHelloImpl" +
            ".class");
    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      outputStream.write(bytes);
      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  static class InvocationHandlerImp implements InvocationHandler {
    /**
     * 被代理的类
     */
    private Object mObject;

    public InvocationHandlerImp(Object object) {
      mObject = object;
    }

    /**
     * @param proxy 代理类
     * @param method 被代理的方法
     * @param args 被代理方法的参数
     * @return 返回代理对象
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      System.out.println("这是invoke" + proxy.getClass().getName());
      System.out.println("这是invoke");
      method.invoke(mObject, args);
      return null;
    }
  }
}