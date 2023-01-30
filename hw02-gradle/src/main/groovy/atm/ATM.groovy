package atm

/**
 * Упрощённый банкомат
 */
class ATM {

    private atmCash = new Cash() // касса

    /**
     * Загрузка банкнот в кассу
     * @param cash Источник
     */
    void input(Cash cash) {
        atmCash = atmCash + cash
    }

    /**
     * Выдача наличных
     * @param quantity Требуемая сумма в валюте currency
     * @param currency Валюта, в которой требуется выдать наличные
     * @return Наличные
     */
    Cash give(long quantity, Banknote.Currency currency) {
        Cash result = new Cash()

        List<Banknote> moneys = Banknote.get(currency)
        moneys = moneys.sort {it.nominal}

        def v = quantity

        for (int i = moneys.size()-1; i>=0; i--) {
            Banknote money = moneys[i]
            if (money.nominal.value > v) {
                continue
            }

            int cnt = v.intdiv(money.nominal.value)
            if (!atmCash.check(money, cnt)) {
                continue
            }
            result.load(money, cnt)
            atmCash.extract(money, cnt)
            v -= cnt * money.nominal.value

            if (v == 0) {
                break
            }
        }
        if (v > 0) {
            throw new IllegalStateException("Can not give $quantity $currency")
        }

        return result
    }


    /**
     * Отчёт по оставшимся в банкомате банкнот
     * @return Текст отчёта
     */
    String report() {
        StringBuilder b = new StringBuilder()

        Map<Banknote, Integer> report = atmCash.report()

        Banknote.Currency.values().each {
            long acc = 0
            b.append(it).append("\n")
            Banknote.Currency currency = it
            report.findAll {it.key.currency == currency}.each {
                acc += it.key.nominal.value * it.value
                b.append(it.key.nominal.value).append(": ").append(it.value).append(" items\n")
            }
            b.append("Total: ").append(acc).append(" ").append()
            b.append("\n\n")
        }

        return b.toString()
    }


}
