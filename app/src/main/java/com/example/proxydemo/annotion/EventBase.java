package com.example.proxydemo.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventBase {
  /**
   * setOnLongClickListener
   *
   * @return 方法名
   */
  String listenerSetter();

  /**
   * OnLongClickListener.class
   *
   * @return 事件监听的类型
   */
  Class< ? > listenerType();

  /**
   * onLongClick
   *
   * @return 事件被触发之后，执行的回调方法的名称
   */
  String callbackMethod();

}
