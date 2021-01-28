package com.example.lib;

import java.util.ArrayList;

/**
 * @author wangxu
 * @date 2020/9/18
 * @Description
 */
class ArrayTest {

    String a;

    public ArrayTest() {
    }

    public ArrayTest(String a) {
        this.a = a;
    }

    public static void main(String[] args) {

        ArrayTest list = new ArrayTest();
        ArrayTest list2 = new ArrayTest();
        ArrayTest list3 = new ArrayTest("a");
        ArrayTest list4 = new ArrayTest();
        int n;
        System.out.println("llllllllllllllllll======" + ((n = list.hashCode())));
        System.out.println(15 & n);

//        System.out.println("1101".compareTo("1210"));

//
//        System.out.println("结果：" + (1 << 33));
//        String a = null;
//        System.out.println("a = " + a);
//
//        ArrayList<String> arrayList = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            arrayList.add("index " + i);
//        }
//
//
//        for (int i = 0; i < arrayList.size(); i++) {
//            if (i == 5) {
//                arrayList.remove(i);
//            }
//            System.out.println(i);
//        }
    }
}
