import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        Config cfg = new Gson().fromJson(new FileReader("config.json"), Config.class);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(cfg.warehouseCapacity());
        List<Baker> bakers = new ArrayList<>();
        List<Courier> couriers = new ArrayList<>();

        // Списки для хранения ссылок на потоки
        List<Thread> bakerThreads = new ArrayList<>();
        List<Thread> courierThreads = new ArrayList<>();
        AtomicInteger orderCounter = new AtomicInteger(0);

        // Запуск пекарей
        for (int i = 0; i < cfg.bakers(); i++) {
            Baker b = new Baker(i, cfg.bakersSpeed()[i], queue, warehouse);
            bakers.add(b);
            Thread t = new Thread(b);
            bakerThreads.add(t);
            t.start();
        }

        // Запуск курьеров
        for (int i = 0; i < cfg.couriers(); i++) {
            Courier c = new Courier(i, cfg.couriersCapacity()[i], warehouse);
            couriers.add(c);
            Thread t = new Thread(c);
            courierThreads.add(t);
            t.start();
        }

        // Генератор заказов
        Thread generator = new Thread(() -> {
            try {
                while (queue.isAccepting()) {
                    queue.addOrder(orderCounter.incrementAndGet());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                // Нормальное прерывание
            }
        });
        generator.start();

        // === Работа пиццерии ===
        Thread.sleep(cfg.workDurationSec() * 1000);

        System.out.println("=== SHUTDOWN STARTED ===");

        // 1. Останавливаем прием заказов
        queue.stopAccepting();
        generator.interrupt();
        generator.join(); // Ждем завершения генератора

        // 2. Ждем, пока все заказы будут взяты в работу (очередь пуста)
        while (!queue.isEmpty()) {
            Thread.sleep(50);
        }

        // 3. Ждем, пока все пиццы будут доставлены (склад пуст)
        while (!warehouse.isEmpty()) {
            Thread.sleep(50);
        }

        // 4. Сигналим рабочим остановиться
        bakers.forEach(Baker::stop);
        couriers.forEach(Courier::stop);

        // 5. Ждем завершения всех потоков (join)
        for (Thread t : bakerThreads) {
            t.join();
        }

        for (Thread t : courierThreads) {
            t.join();
        }

        // 6. Финальное сообщение (теперь строго после завершения всех)
        System.out.println("=== PIZZERIA CLOSED ===");
    }
}