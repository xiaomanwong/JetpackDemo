package com.example.lib;

// 锁实例方法
public class AccountingSync implements Runnable {

    private final Object mLock = new Object();
    // 共享资源
    static int i = 0;
    static int j = 0;

    // synchroized
    public void increase() {
        synchronized (mLock) {
            i++;
            System.out.println(i);
            increase2();
        }
    }

    // synchroized
    public synchronized void increase2() {
        j++;
        System.out.println(j);
    }


    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            increase();
            Thread.interrupted();

//            increase2();
        }
    }

    public static void main(String[] args) {
        AccountingSync sync1 = new AccountingSync();
        AccountingSync sync2 = new AccountingSync();
        Thread t1 = new Thread(sync1);
        Thread t2 = new Thread(sync1);
        StringBuffer sb = new StringBuffer();
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}