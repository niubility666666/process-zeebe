package com.rt.engine.common.util;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CommonUtils {

    /**
     * 对象深克隆
     *
     * @param model 对象
     * @param <T>   泛型
     * @return 克隆的新对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneObject(T model) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(model);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行批量集合操作
     *
     * @param list     数据集合
     * @param function 执行的操作
     * @param <T>      数据集合泛型
     * @return int 影响的行数
     */
    public static <T> int executeBatchList(List<T> list, Function<List<T>, Integer> function) {
        if (!CollectionUtils.isEmpty(list)) {
            return function.apply(list);
        }
        return 0;
    }

    /**
     * 执行批量集合操作
     *
     * @param list     数据集合
     * @param function 执行的操作
     * @param <T>      数据集合泛型
     */
    public static <T> void addAllList(List<T> list, Function<List<T>, Boolean> function) {
        if (!CollectionUtils.isEmpty(list)) {
            function.apply(list);
        }
    }

    /**
     * 复制集合
     *
     * @param sourceList 资源集合
     * @param clazz      目标类的描述类
     * @param <T>        目标类的描述类的类型
     * @param <K>        资源类的类型
     * @return 复制之后的集合
     */
    public static <T, K> List<T> copyList(List<K> sourceList, Class<T> clazz) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Lists.newArrayList();
        }
        ArrayList<T> target = Lists.newArrayList();
        sourceList.forEach(k -> target.add(convert(k, clazz)));
        return target;
    }

    /**
     * 复制对象
     *
     * @param source 资源来源对象
     * @param clazz  目标类的描述类
     * @param <T>    目标类的描述类的类型
     * @param <K>    资源类的类型
     * @return 复制之后的类对象
     */
    private static <T, K> T convert(K source, Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(source, t);
        return t;
    }
}
