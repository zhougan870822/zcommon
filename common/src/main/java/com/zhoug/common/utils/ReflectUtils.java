package com.zhoug.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类
 *  1 getDeclaredMethod：获取当前类的所有声明的方法，包括public、protected和private修饰的方法。
 *  需要注意的是，这些方法一定是在当前类中声明的，从父类中继承的不算，实现接口的方法由于有声明所以包括在内
 *  2 getMethod：获取当前类和所有父类的所有非private声明的方法的方法。
 * @Author HK-LJJ
 * @Date 2019/12/23
 * @Description
 */
public class ReflectUtils {

    /**
     * 获取泛型的类型
     * @param cls
     * @param index
     * @return
     */
    public static Class getGenericType(Class cls,int index) {
        //这里获得到的是类的泛型的类型
        Type type = cls.getGenericSuperclass();//获得带有泛型的父类
        if(type!=null){
            //ParameterizedType参数化类型，即泛型
            if (type instanceof ParameterizedType) {
                //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
               return (Class ) actualTypeArguments[index];
            }
        }

        return null;
    }


    /**
     * 调用当前类和所有父类的非private声明的方法
     *
     * @param className   类全名
     * @param methodName  方法名
     * @param obj         类实例
     * @param paramTypes  方法参数类型
     * @param paramValues 方法参数
     * @return 方法返回值
     */
    public static Object invokeMethod(String className, String methodName, Object obj, Class[] paramTypes, Object[] paramValues) {
        try {
            Class<?> objClass = Class.forName(className);
            Method method = objClass.getMethod(methodName, paramTypes);
            method.setAccessible(true);
            Object invoke = method.invoke(obj, paramValues);
            method.setAccessible(false);
            return invoke;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用当前类和所有父类的非private声明的方法
     * @param cls
     * @param methodName
     * @param obj
     * @param paramTypes
     * @param paramValues
     * @return
     */
    public static Object invokeMethod(Class<?> cls, String methodName, Object obj, Class[] paramTypes, Object[] paramValues) {
        try {
            Method method = cls.getMethod(methodName, paramTypes);
            method.setAccessible(true);
            Object invoke = method.invoke(obj, paramValues);
            method.setAccessible(false);
            return invoke;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }



        /**
         * 调用当前类声明的方法
         *
         * @param className   类全名
         * @param methodName  方法名
         * @param obj         类实例
         * @param paramTypes  方法参数类型
         * @param paramValues 方法参数
         * @return 方法返回值
         */
    public static Object invokeDeclaredMethod(String className, String methodName, Object obj, Class[] paramTypes, Object[] paramValues) {
        try {
            Class<?> objClass = Class.forName(className);
            Method method = objClass.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            Object invoke = method.invoke(obj, paramValues);
            method.setAccessible(false);
            return invoke;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用当前类声明的方法
     * @param cls
     * @param methodName
     * @param obj
     * @param paramTypes
     * @param paramValues
     * @return
     */
    public static Object invokeDeclaredMethod(Class<?> cls, String methodName, Object obj, Class[] paramTypes, Object[] paramValues) {
        try {
            Method method = cls.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            Object invoke = method.invoke(obj, paramValues);
            method.setAccessible(false);
            return invoke;
        } catch ( NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 调用当前类和所有父类的非private声明的static方法
     *
     * @param className   类全名
     * @param methodName  静态方法名
     * @param paramTypes  方法参数类型
     * @param paramValues 方法参数
     * @return 方法返回值
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeMethod(className, methodName, null, paramTypes, paramValues);
    }

    /**
     * 调用当前类和所有父类的非private声明的static方法
     * @param cls
     * @param methodName
     * @param paramTypes
     * @param paramValues
     * @return
     */
    public static Object invokeStaticMethod(Class<?> cls, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeMethod(cls, methodName, null, paramTypes, paramValues);
    }

    /**
     * 调用当前类声明的static方法
     *
     * @param className   类全名
     * @param methodName  静态方法名
     * @param paramTypes  方法参数类型
     * @param paramValues 方法参数
     * @return 方法返回值
     */
    public static Object invokeStaticDeclaredMethod(String className, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeDeclaredMethod(className, methodName, null, paramTypes, paramValues);
    }

    /**
     * 调用当前类声明的static方法
     * @param cls
     * @param methodName
     * @param paramTypes
     * @param paramValues
     * @return
     */
    public static Object invokeStaticDeclaredMethod(Class<?> cls, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeDeclaredMethod(cls, methodName, null, paramTypes, paramValues);
    }

    /**
     * 获取本类或者父类中的Field的值
     *
     * @param className 类全名
     * @param obj       类实例 ,static字段时为null
     * @param filedName field名字
     * @return
     */
    public static Object getFieldValue(String className, Object obj, String filedName) {
        try {
            Class objClass = Class.forName(className);
            Field field = objClass.getField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本类或者父类中的Field的值
     * @param cls
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getFieldValue(Class<?> cls, Object obj, String filedName) {
        try {
            Field field = cls.getField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch ( IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取本类中的Field的值
     *
     * @param className 类全名
     * @param obj       类实例 ,static字段时为null
     * @param filedName field名字
     * @return Field的值
     */
    public static Object getDeclaredFieldValue(String className, Object obj, String filedName) {
        try {
            Class objClass = Class.forName(className);
            Field field = objClass.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本类中的Field的值
     * @param cls
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getDeclaredFieldValue(Class<?> cls, Object obj, String filedName) {
        try {
            Field field = cls.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch ( IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置本类或父类中的Field的值
     *
     * @param className  类全名
     * @param filedName  field名字
     * @param obj        类实例 ,static字段时为null
     * @param filedValue field值
     */
    public static void setField(String className, String filedName, Object obj, Object filedValue) {
        try {
            Class objClass = Class.forName(className);
            Field field = objClass.getField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
            field.setAccessible(false);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置本类或父类中的Field的值
     * @param cls
     * @param filedName
     * @param obj
     * @param filedValue
     */
    public static void setField(Class<?> cls, String filedName, Object obj, Object filedValue) {
        try {
            Field field = cls.getField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置本类中的Field的值
     *
     * @param className  类全名
     * @param filedName  field名字
     * @param obj        类实例 ,static字段时为null
     * @param filedValue field值
     */
    public static void setDeclaredField(String className, String filedName, Object obj, Object filedValue) {
        try {
            Class objClass = Class.forName(className);
            Field field = objClass.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
            field.setAccessible(false);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置本类中的Field的值
     * @param cls
     * @param filedName
     * @param obj
     * @param filedValue
     */
    public static void setDeclaredField(Class<?> cls, String filedName, Object obj, Object filedValue) {
        try {
            Field field = cls.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
