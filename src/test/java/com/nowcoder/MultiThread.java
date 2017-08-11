package com.nowcoder;

public class MultiThread {

    public static void testThread() {
        for (int j=0;j<10;j++) {
            //new MyThread(i);
            final int id=j;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i=0;i<10;i++) {
                            System.out.println("Thread"+id+":  "+i);
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static Object obj=new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int i=0;i<10;i++) {
                    System.out.println("Thread 1: "+i);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (obj) {
            try {
                for (int i=0;i<10;i++) {
                    System.out.println("Thread 2: "+i);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //testThread();

        for (int i=0;i<5;i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

}

class MyThread extends Thread{
    private int threadId;
    public MyThread(int threadId){
        this.threadId=threadId;
    }

    @Override
    public void run() {
        for (int i=0;i<10;i++) {
            try {
                System.out.println("线程" + threadId + ":" + i);
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}