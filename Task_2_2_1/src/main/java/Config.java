/**
 * Параметры системы.
 *
 * @param workDurationSec сколько работает в секундах
 * @param orderDelayMillis раз в сколько миллисекунд появляется новый заказ
 * @param bakers количество пекарей
 * @param couriers количество курьеров
 * @param warehouseCapacity вместимость склада
 * @param bakersSpeedMillis список сколько времени уходит на готовку пиццы у каждого пекаря
 * @param couriersCapacity список вместимостей пицц у каждого курьера
 * @param couriersSpeedMillis список сколько времени уходит на доставку пиццы у каждого курьера
 */
record Config(
        long workDurationSec,
        long orderDelayMillis,
        int bakers,
        int couriers,
        int warehouseCapacity,
        long[] bakersSpeedMillis,
        int[] couriersCapacity,
        long[] couriersSpeedMillis
) {}