package com.example.mylibrary.sync;


import java.text.NumberFormat;
import java.util.Locale;

// 锁实例方法
public class AccountingSync implements Runnable {

    // 共享资源
    static int i = 0;

    // synchroized
    public synchronized void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            increase();
        }
    }

    public static void main(String[] args) {
//        AccountingSync sync = new AccountingSync();
//        Thread t1 = new Thread(sync);
//        Thread t2 = new Thread(sync);
//        t1.start();
//        t2.start();
//        try {
//            t1.join();
//            t2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(i);

        // 创建一个数字格式化器，基于指定的区域
//        NumberFormat numberFormat = NumberFormat.getInstance(Locale.CHINA);
//
//        // 示例数字
//        long number = 1234567890;
//
//        // 格式化数字
//        String formattedNumber = numberFormat.format(number);
//
//        // 输出格式化后的数字
//        System.out.println("Formatted Number in China Locale: " + formattedNumber);



    }



}