package com.rt.engine.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @author wuwanli
 * @date 2021/9/7
 */
public class DemoTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        String join = StringUtils.join(list, ",");
        System.out.println(join);

        List<String> list1 = Arrays.asList(join.split(","));
        System.out.println(list1);

    }

    @Test
    public void demo1() {

        List<String> list = Arrays.asList("1", "2");
        Arrays.asList("1", "2").forEach(System.out::println);
        System.out.println("-----------");
        list.sort((t1, t2) -> {
            return t2.compareTo(t1);
        });
        System.out.println(list);

        System.out.println("-----------");
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        System.out.println(list);

    }

    interface test {

        int add(int a, int b);

        default int sum(int a, int b, int c) {
            return a + b + c;
        };
    }

    static class TestImpl implements test {

        @Override
        public int add(int a, int b) {
            return a + b;
        }

        public static int add1(TestImpl test) {
            return test.add(1, 3);
        }
    }

    @Test
    public void demo2() {

        // Consumer<Integer> consumer = a -> System.out.println(a + 1);
        // Consumer<Integer> consumer1 = a -> System.out.println(a + 2);

        // consumer.andThen(consumer).andThen(consumer1).accept(1);

        Function<Integer, Integer> func1 = a -> a + 1;
        Function<Integer, Integer> func2 = a -> a * a;
        System.out.println(func1.compose(func2).apply(5).intValue());
        System.out.println(func1.apply(2).intValue());

        Predicate<String> p1 = o -> "hello".equals(o);
        System.out.println(p1.test("hello"));
        System.out.println(p1.and(((String o) -> "test".equals(o))).test("hello"));

        List<Integer> list = Arrays.asList(1, 3, 4, 6, 2);
        System.out.println(list.stream().sorted().collect(Collectors.toList()));
        list.sort((Integer a, Integer b) -> -Integer.compare(a, b));
        System.out.println(list);

    }

    @Test
    public void demo3() {
        List<Integer> list = Arrays.asList(8, 1, 3, 4, 6, 2);
        System.out.println(list.stream().findFirst().get().intValue());
        System.out.println(list.stream().filter(o -> o == 0).collect(Collectors.toList()));
        list.stream().map(a -> a + 1).forEach(System.out::println);
    }

    @Test
    public void demo4() {
        List<String> list = Arrays.asList("hello", "hello ;world", "hello");
        list.stream().forEach(System.out::println);
        System.out.println("======================");
        list.stream().distinct().forEach(System.out::println);
        System.out.println("======================");
        System.out.println(list.stream().allMatch(o -> o.contains("hello")));
        System.out.println(list.stream().allMatch(o -> o.contains("world")));
        System.out.println(list.stream().anyMatch(o -> o.contains("world")));
        System.out.println("======================");
        list.stream().dropWhile(o -> o.contains("hello ;")).forEach(System.out::println);
    }
}
