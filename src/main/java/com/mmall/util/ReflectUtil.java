package com.mmall.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtil {

    public static<T> T set(List<Object> list, Class clazz){
        Object obj = null;
        try{
            obj = clazz.newInstance();
        }catch(Exception e){
            System.out.println("实例失败"+e.getMessage());
        }
        if(obj!=null){
            Method[] declaredMethods = clazz.getDeclaredMethods();
            Field[] declaredFields = clazz.getDeclaredFields();
            Method[] methods = new Method[declaredMethods.length/2];
            int flag = 0;
            if(list.size() == declaredFields.length){
                for(int i=0;i<declaredMethods.length;i++){
                    if(declaredMethods[i].getName().startsWith("set")){
                        methods[flag] = declaredMethods[i];
                        flag++;
                    }
                }
                for(int i=0;i<list.size();i++){
                    try{
                        methods[i].invoke(obj,list.get(i));
                    }catch(Exception e) {
                        System.out.println("转换异常 "+e.getMessage());
                    }
                }
            }
        }
        return (T)obj;
    }

}
