package com.example.lib_java;

/**
 * @author Administrator
 */


class Instance {

    public static void main(String[] args) {
        ClassHolder.INSTANCE_A.fun1();
    }
    enum ClassHolder {
      INSTANCE_A;

      public void fun1(){
          System.out.println("fun1");
      }

      public void fun2(){
          System.out.println("fun2");
      }
    }
}




