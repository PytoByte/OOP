package game.model;

/**
 * Контейнеры пары объектов.
 *
 * @param key первый элемент пары
 * @param value второй элемент пары
 * @param <T1> тип первого элемента пары
 * @param <T2> тип второго элемента пары
 */
public record Pair<T1, T2>(T1 key, T2 value) {

}
