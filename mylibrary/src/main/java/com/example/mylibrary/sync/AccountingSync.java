package com.example.mylibrary.sync;

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
        AccountingSync sync = new AccountingSync();
        Thread t1 = new Thread(sync);
        Thread t2 = new Thread(sync);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(i);

    }



}