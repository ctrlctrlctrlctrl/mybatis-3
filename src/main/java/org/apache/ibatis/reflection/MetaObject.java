/*
 *    Copyright 2009-2024 the original author or authors.
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
package org.apache.ibatis.reflection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.CollectionWrapper;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * @author Clinton Begin
 * 本对象可以认为是一个保存了对象结构体，通过包装类对真实对象进行操作
 */
public class MetaObject {

  private final Object originalObject;
  private final ObjectWrapper objectWrapper;//对象包装，因为对象的格式有多种多样，比如list、map、object。所以在调用构造方法的时候确认然后放入对应的包装类，本类的操作基本都是基于该对象进行操作
  private final ObjectFactory objectFactory;//对象生产工厂
  private final ObjectWrapperFactory objectWrapperFactory;//包装对象生产工厂
  private final ReflectorFactory reflectorFactory;//反射工厂

  private MetaObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory,
      ReflectorFactory reflectorFactory) {
    this.originalObject = object;
    this.objectFactory = objectFactory;
    this.objectWrapperFactory = objectWrapperFactory;
    this.reflectorFactory = reflectorFactory;

    //这里的核心在于生成的过程当可以通过object进行生成包装类的时候一定要进行生产
    if (object instanceof ObjectWrapper) {//当object为ObjectWrapper，则类变量ObjectWrapper设置为该object的强制类型
      this.objectWrapper = (ObjectWrapper) object;
    } else if (objectWrapperFactory.hasWrapperFor(object)) {//当本方法的入参objectWrapperFactory是否可以生产object对象，如果可以使用改工厂进行生产一个包装对象
      this.objectWrapper = objectWrapperFactory.getWrapperFor(this, object);
    } else if (object instanceof Map) {//略
      this.objectWrapper = new MapWrapper(this, (Map) object);
    } else if (object instanceof Collection) {//略
      this.objectWrapper = new CollectionWrapper(this, (Collection) object);
    } else {//略
      this.objectWrapper = new BeanWrapper(this, object);
    }
  }

  public static MetaObject forObject(Object object, ObjectFactory objectFactory,
      ObjectWrapperFactory objectWrapperFactory, ReflectorFactory reflectorFactory) {//将一个对象生成本类的对象【相当于构造】
    if (object == null) {//如果该参数为空则默认返回一个该语境下的空对象
      return SystemMetaObject.NULL_META_OBJECT;
    }
    return new MetaObject(object, objectFactory, objectWrapperFactory, reflectorFactory);//不为空则通过本类的构造方法返回一个元对象
  }

  public ObjectFactory getObjectFactory() {//获得对象工厂
    return objectFactory;
  }

  public ObjectWrapperFactory getObjectWrapperFactory() {//获得包装对象生产工厂
    return objectWrapperFactory;
  }

  public ReflectorFactory getReflectorFactory() {//获得反射工厂
    return reflectorFactory;
  }

  public Object getOriginalObject() {//获得原始对象
    return originalObject;
  }

  public String findProperty(String propName, boolean useCamelCaseMapping) {
    return objectWrapper.findProperty(propName, useCamelCaseMapping);
  }

  public String[] getGetterNames() {
    return objectWrapper.getGetterNames();
  }

  public String[] getSetterNames() {
    return objectWrapper.getSetterNames();
  }

  public Class<?> getSetterType(String name) {
    return objectWrapper.getSetterType(name);
  }

  public Class<?> getGetterType(String name) {
    return objectWrapper.getGetterType(name);
  }

  public boolean hasSetter(String name) {
    return objectWrapper.hasSetter(name);
  }

  public boolean hasGetter(String name) {
    return objectWrapper.hasGetter(name);
  }

  public Object getValue(String name) {
    PropertyTokenizer prop = new PropertyTokenizer(name);
    return objectWrapper.get(prop);
  }

  public void setValue(String name, Object value) {
    objectWrapper.set(new PropertyTokenizer(name), value);
  }

  public MetaObject metaObjectForProperty(String name) {
    Object value = getValue(name);
    return MetaObject.forObject(value, objectFactory, objectWrapperFactory, reflectorFactory);
  }

  public ObjectWrapper getObjectWrapper() {
    return objectWrapper;
  }

  public boolean isCollection() {
    return objectWrapper.isCollection();
  }

  public void add(Object element) {
    objectWrapper.add(element);
  }

  public <E> void addAll(List<E> list) {
    objectWrapper.addAll(list);
  }

}
