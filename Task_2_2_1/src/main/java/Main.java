import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Основной класс.
 */
public class Main {

    /**
     * Запуск пиццерии.
     *
     * @param args аргументы
     * @throws InterruptedException если поток был прерван
     * @throws FileNotFoundException если файл config.json не найден
     */
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Config cfg = new Gson().fromJson(new FileReader("config.json"), Config.class);
        Channel queue = new Channel("ORDER_QUEUE", 0);
        Channel warehouse = new Channel("WAREHOUSE", cfg.warehouseCapacity());

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

        System.out.println("=== PIZZERIA OPENED ===");

        Thread generator = new Thread(() -> {
            try {
                int i = 0;
                while (!queue.isClosed()) {
                    queue.put(i);
                    i++;
                    Thread.sleep(cfg.orderDelayMillis());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        generator.start();

        Thread.sleep(cfg.workDurationSec() * 1000);

        System.out.println("=== PIZZERIA CLOSING ===");

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