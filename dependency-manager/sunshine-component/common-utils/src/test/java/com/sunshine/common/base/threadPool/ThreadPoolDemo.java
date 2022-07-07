package com.sunshine.common.base.threadPool;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
//        demo1();
        SynchronousQueue();
    }

    public static void SynchronousQueue(){
        SynchronousQueue queue = new SynchronousQueue();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    queue.put(i);
                    System.out.println("put data => "+ i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            for (;;) {
                try {

                    Object res = queue.take();
                    System.out.println(" get data = > "+ res);
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private static void demo1() {
        BlockingQueue<Runnable> workQueue=   new SynchronousQueue();  // 读缓冲队列， 是一个不存储元素的阻塞队列
//                BlockingQueue<Runnable> workQueue=   new ArrayBlockingQueue(10);// 有界阻塞队列
//        BlockingQueue<Runnable> workQueue=   new LinkedBlockingQueue();// 无界阻塞队列
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,workQueue, new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 20; i++) {
            try{
//                executor.submit(new Run(true));
                executor.execute(new Run(true,String.valueOf(i+1)));
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("丢弃任务："+(i+1));
            }
        }
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        if ("stop".equals(next)){
            executor.shutdown();
        }
    }

    static class  Run implements Runnable {

        private final String name;


        private volatile boolean flag;

       public Run(boolean flag,String name) {
           this.flag = flag;
           this.name = name;
       }

       @Override
        public void run() {
            System.out.println("开始执行"+name+":"+ Thread.currentThread().getName());
           while (flag) {
                for (;;);
            }
            System.out.println("执行结束"+ Thread.currentThread().getName());
        }
    }
}
