package com.mmall.thread.CallableTest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest implements Callable<Integer>{

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableTest callableTest = new CallableTest();
        FutureTask<Integer> task = new FutureTask<Integer>(callableTest);
        Thread t = new Thread(task);
        t.start();
        Integer integer = task.get();
        System.out.println("jieguo : "+integer);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("jinxingjisuanzhogn ...");
        Thread.sleep(300);
        return 15555;
    }
}
