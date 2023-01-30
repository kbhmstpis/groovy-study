package atm

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Касса, кошелёк, всё что хранит купюры
 */
class Cash {

    //Ячейки кассы: купюры и их количество
    private Map<Banknote, Integer> cells = new LinkedHashMap<>()

    private Lock lock = new ReentrantLock()

    /**
     * Загрузить купюры
     * @param money Тип купюры
     * @param quantity Количество купюр
     */
    void load(Banknote money, int quantity) {
        lock.lock()
        try {
            cells.compute(money, (key,val) -> {val ? val + quantity : quantity})
        }
        finally {
            lock.unlock()
        }
    }

    /**
     * Извлечь купюры
     * @param money Тип купюры
     * @param quantity Количество купюр
     */
    void extract(Banknote money, int quantity) {
        lock.lock()
        try {
            int present = cells.get(money)
            if (present < quantity) {
                throw new IllegalStateException("No money $money")
            }
            cells.put(money, present-quantity)
        }
        finally {
            lock.unlock()
        }
    }

    /**
     * Проверить наличие нужного количества купюр
     * @param money Тип купюры
     * @param quantity Количество купюр
     * @return true - купюр хватает, false - нет нужного количества
     */
    boolean check(Banknote money, int quantity) {
        return cells.get(money) >= quantity
    }

    /**
     * Прибавляет один кошелёк к другому
     * @param cash Второй кошелёк
     * @return Новый кошелёк с результатом
     */
    Cash plus(Cash cash) {
        Cash result = new Cash()
        result.cells.putAll(cells)
        cash.cells.forEach((key, value) -> {
            result.load(key, value)
        })
        return result
    }


    /**
     * Вычитает один кошелёк из другого
     * @param cash Второй кошелёк
     * @return Новый кошелёк с результатом
     */
    Cash minus(Cash cash) {
        Cash result = new Cash()
        result.cells.putAll(cells)
        cash.cells.forEach((key, value) -> {
            result.extract(key, value)
        })
        return result
    }

    /**
     * Отдаёт содержимое ячеек.
     * @return Копия ячеек на момент выполнения запроса
     */
    Map<Banknote, Integer> report() {
        Map<Banknote, Integer> result = new LinkedHashMap<>()
        result.putAll(cells)
        return result
    }

}
