package org.example;

import Processing.Processor;
import Processing.usableNetwork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        //for 20
        for(int i = 0; i < 20; i++)
            executorService.submit(()->{
                usableNetwork network = new usableNetwork();
                network.receiveMessage();
            });
        //shutdown
        try{
            executorService.shutdown();
            while(!executorService.awaitTermination(24L, TimeUnit.HOURS)){
                System.out.println("Please wait.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Processor.shutdown();

    }
}