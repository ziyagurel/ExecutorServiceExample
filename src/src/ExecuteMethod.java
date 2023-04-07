package src;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExecuteMethod {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<String> itemList = Arrays.asList("Zahit", "Ziya", "GUREL");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Boolean isExecute = processPrintList(itemList);
                System.out.println("metot icerisinde istenildigi gibi geri donus alinabilir. Metot calisti : " + isExecute);
            }
        });
        System.out.println("Fakat execute metodu disariya herhangi bir deger donmez");
    }

    private static Boolean processPrintList(List<String> itemList) {

        for (String item : itemList)
            System.out.print(item + " ");

        return true;
    }
}
