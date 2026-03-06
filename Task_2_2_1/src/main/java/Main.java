import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Config cfg = new Gson().fromJson(new FileReader("config.json"), Config.class);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(cfg.warehouseCapacity());

        List<Thread> bakerThreads = new ArrayList<>();
        List<Thread> courierThreads = new ArrayList<>();

        for (int i = 0; i < cfg.bakers(); i++) {
            Baker b = new Baker(i, cfg.bakersSpeed()[i], queue, warehouse);
            Thread t = new Thread(b);
            bakerThreads.add(t);
            t.start();
        }

        for (int i = 0; i < cfg.couriers(); i++) {
            Courier c = new Courier(i, cfg.couriersCapacity()[i], warehouse);
            Thread t = new Thread(c);
            courierThreads.add(t);
            t.start();
        }

        System.out.println("=== RUNNING GENERATOR ===");

        Thread generator = new Thread(() -> {
            try {
                int i = 0;
                while (!queue.isClosing()) {
                    queue.addOrder(i);
                    i++;
                    Thread.sleep(cfg.orderDelayMillis());
                }
            } catch (InterruptedException _) {}
        });
        generator.start();

        Thread.sleep(cfg.workDurationSec() * 1000);

        System.out.println("=== SHUTDOWN STARTED ===");

        queue.close();
        generator.join();
        for (Thread t : bakerThreads) {
            t.join();
        }

        warehouse.close();
        for (Thread t : courierThreads) {
            t.join();
        }

        System.out.println("=== PIZZERIA CLOSED ===");
    }
}