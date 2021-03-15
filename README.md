# ProxyDemo

## 动态代理含义

1. 定义：给目标对象提供一个代理对象，并由代理对象控制对目标对象的引用
2. 目的: (1) 通过引入代理对象的方式来间接访问目标对象，防止直接访问目标对象给系统带来的不必要复杂性

## 为什么会有动态代理

传统面向对象思想中，如果想要实现功能复用，要么继承、要么引用，无论哪种方式，对代码都有一定的侵入性，耦合无可避免。

侵入性含义：如果你想要用它增强你程序的功能，你必须改动你的程序代码，那它就具有侵入性。如果只有一点两点需要增强还好说，如果大量的功能点需要被增强，工作量就会很大，代码也不太优雅。想象一下，如果你对外公开了一系列的接口，现在领导说了，接口要加权限控制。在哪加？最笨的当然就是写个程序验证的逻辑，然后每个接口都拿来调用一遍。这也正是面向对象思想的短板，在要为程序新增一些通用功能时，只能通过耦合的方式才能进行。AOP正是为此而生，AOP旨在通过一种无耦合的方式来为程序带来增强。而动态代理就是AOP实现方式中的一种。

## 动态代理应用场景

1. 权限集中申请
2. 日志集中打印
3. 底层屏蔽具体网络请求，Retorfit网络请求
4. RPC即远程过程调用
5. 需要对较难修改的类方法进行功能增加

## Android中动态代理有哪几种实现方式

1. Java  Proxy（接口实现）

2. AspectJ

3. Cglib(只能java用，Android用不了)

## Proxy核心原理

编译时 ，代理对象的class并不存在，当需要调用Proxy.newProxyInstance时，会构建一个Proxy0的class字节码，并且加载到内存。

### Proxy具体代码实现

java中Proxy的动态代理必须要有接口

**HelloWorldInterface.java**

```java
public interface HelloWorldInterface {
  void hello();
}
```

**HelloImp.java**

```java 
public class HelloImp implements HelloWorldInterface {

  @Override
  public void hello() {
    System.out.println("这是HelloImp");
  }
}
```

**调用**

```java
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
      //com.sun.proxy.$Proxy0
      System.out.println("这是proxy" + proxy.getClass().getName());
      System.out.println("这是invoke");
      method.invoke(mObject, args);
      return null;
    }
  }
}
```



### 实现原理

**ProxyGenerator**：能能够在运行时生成一个对象，而这个对象是实现了该接口，这个对象所属的类是一个全新的class。 Class需要生成才能加载。而ProxyGenerator在运行时，是做生成class字节码。

在Proxy的内部，会通过ProxyGenerator.generateProxyClass来生成字节码。

```java
//对象的名字
//public static final String PROXY_PACKAGE = "com.sun.proxy";
//private static final String proxyClassNamePrefix = "$Proxy";
//num是一个AtomicLong，从0开始增加
//生成的名字格式就是com.sun.proxy.$Proxy0
String proxyName = proxyPkg + proxyClassNamePrefix + num;

/*
 * Generate the specified proxy class.
 */
byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
    proxyName, interfaces, accessFlags);
try {
    //调用defineClass0生成class对象，这是一个native方法
    return defineClass0(loader, proxyName,
                        proxyClassFile, 0, proxyClassFile.length);
} catch (ClassFormatError e) {
    /*
     * A ClassFormatError here means that (barring bugs in the
     * proxy class generation code) there was some other
     * invalid aspect of the arguments supplied to the proxy
     * class creation (such as virtual machine limitations
     * exceeded).
     */
    throw new IllegalArgumentException(e.toString());
}
```

Proxy#defineClass0

```java
private static native Class<?> defineClass0(ClassLoader loader, String name,
                                            byte[] b, int off, int len);
```

手动调用ProxyGenerator.generateProxyClass，看看里面生成的字节码

```java
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
```

JackfruitHelloImpl.class

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.example.lib.HelloWorldInterface;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public final class JackfruitHelloImpl extends Proxy implements HelloWorldInterface {
  private static Method m1;
  private static Method m3;
  private static Method m2;
  private static Method m0;

  public JackfruitHelloImpl(InvocationHandler var1) throws  {
    super(var1);
  }

  public final boolean equals(Object var1) throws  {
    try {
      return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
    } catch (RuntimeException | Error var3) {
      throw var3;
    } catch (Throwable var4) {
      throw new UndeclaredThrowableException(var4);
    }
  }

  public final void hello() throws  {
    try {
      super.h.invoke(this, m3, (Object[])null);
    } catch (RuntimeException | Error var2) {
      throw var2;
    } catch (Throwable var3) {
      throw new UndeclaredThrowableException(var3);
    }
  }

  public final String toString() throws  {
    try {
      return (String)super.h.invoke(this, m2, (Object[])null);
    } catch (RuntimeException | Error var2) {
      throw var2;
    } catch (Throwable var3) {
      throw new UndeclaredThrowableException(var3);
    }
  }

  public final int hashCode() throws  {
    try {
      return (Integer)super.h.invoke(this, m0, (Object[])null);
    } catch (RuntimeException | Error var2) {
      throw var2;
    } catch (Throwable var3) {
      throw new UndeclaredThrowableException(var3);
    }
  }

  static {
    try {
      m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
      m3 = Class.forName("com.example.lib.HelloWorldInterface").getMethod("hello");
      m2 = Class.forName("java.lang.Object").getMethod("toString");
      m0 = Class.forName("java.lang.Object").getMethod("hashCode");
    } catch (NoSuchMethodException var2) {
      throw new NoSuchMethodError(var2.getMessage());
    } catch (ClassNotFoundException var3) {
      throw new NoClassDefFoundError(var3.getMessage());
    }
  }
}
```

可以看到JackfruitHelloImpl是继承Proxy，并且实现了我们自己的接口。

```java
public final class JackfruitHelloImpl extends Proxy implements HelloWorldInterface {
  
}
```

在构造函数中传入了InvocationHandler的接口，并且调用了父类的构造函数

```java
 public JackfruitHelloImpl(InvocationHandler var1) throws  {
    super(var1);
  }
```

当我们调用自己的接口方法时，就会调用InvocationHandler的invoke

```java
public final void hello() throws  {
    try {
      //h就是InvocationHandler
      super.h.invoke(this, m3, (Object[])null);
    } catch (RuntimeException | Error var2) {
      throw var2;
    } catch (Throwable var3) {
      throw new UndeclaredThrowableException(var3);
    }
  }
```

让我们再回到Proxy.newProxyInstance方法里

```java
@CallerSensitive
public static Object newProxyInstance(ClassLoader loader,
                                      Class<?>[] interfaces,
                                      InvocationHandler h) throws IllegalArgumentException{
    //代码省略
    /*
     * Look up or generate the designated proxy class.
     */
    //生成class，就是上面我们说的生成class的过程
    Class<?> cl = getProxyClass0(loader, intfs);
    //代码省略
    //通过构造函数，实例化对象。构造函数的参数就是InvocationHandler
    //constructorParams就是InvocationHandler.class
    //private static final Class<?>[] constructorParams ={ InvocationHandler.class };
    final Constructor<?> cons = cl.getConstructor(constructorParams);
    final InvocationHandler ih = h;
    if (!Modifier.isPublic(cl.getModifiers())) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                cons.setAccessible(true);
                return null;
            }
        });
    }
    //实例化对象
    return cons.newInstance(new Object[]{h});
}
```



```java
Class<?> cl = getProxyClass0(loader, intfs);

/*
 * Invoke its constructor with the designated invocation handler.
 */
try {
    if (sm != null) {
        checkNewProxyPermission(Reflection.getCallerClass(), cl);
    }

    final Constructor<?> cons = cl.getConstructor(constructorParams);
    final InvocationHandler ih = h;
    if (!Modifier.isPublic(cl.getModifiers())) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                cons.setAccessible(true);
                return null;
            }
        });
    }
    return cons.newInstance(new Object[]{h});
```

## 什么是IOC

IOC是Inversion of Control的缩写，翻译成“控制反转”。

