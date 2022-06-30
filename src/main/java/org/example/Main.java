package org.example;

import Processing.StoreClientTCP;
import Processing.StoreServerTCP;

public class Main {
    public static void main(String[] args) {
        //clients
        StoreClientTCP client1 = new StoreClientTCP();
        client1.run();

      /*  StoreClientTCP client2 = new StoreClientTCP();
        client2.run();

        StoreClientTCP client3 = new StoreClientTCP();
        client3.run();*/

        //server
        StoreServerTCP srv = new StoreServerTCP();
        srv.run();


        //previous main
       /* ExecutorService executorService = Executors.newFixedThreadPool(4);
        //for 20
        for(int i = 0; i < 20; i++)
            executorService.submit(()->{
                UsableNetwork network = new UsableNetwork();
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
        Processor.shutdown();*/
    }
}