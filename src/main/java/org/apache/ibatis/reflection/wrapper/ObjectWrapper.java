/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection.wrapper;

import java.util.List;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/**
 * @author Clinton Begin
 */
public interface ObjectWrapper {//对象包装的接口

  Object get(PropertyTokenizer prop);

  void set(PropertyTokenizer prop, Object value);

  //这个方法用于查找指定的属性名，第一个参数 name 是你想要查找的属性的名称，第二个参数 useCamelCaseMapping 是一个布尔值，如果设置为 true，则使用驼峰命名法进行映射
  String findProperty(String name, boolean useCamelCaseMapping);

  //获取该类所有get方法的名称
  String[] getGetterNames();

  //获取该类所有set方法的名称
  String[] getSetterNames();

  //获取该属性的set方法名获取该方法的入参类型
  Class<?> getSetterType(String name);

  //获取该属性的get方法名获取该方法的出参类型
  Class<?> getGetterType(String name);

  //这个方法用于检查对象是否有指定名称的 set 方法。它接受一个字符串参数，这个参数是你想要检查的属性的名称。
  boolean hasSetter(String name);

  //这个方法用于检查对象是否有指定名称的 get 方法。它接受一个字符串参数，这个参数是你想要检查的属性的名称。
  boolean hasGetter(String name);

  MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

  boolean isCollection();

  void add(Object element);

  <E> void addAll(List<E> element);

}
