package atm

/**
 * Описание банкнот, singleton
 */
class Banknote {

    /**
     * Номиналы банкнот
     */
    enum Denominations {
        N1(1),
        N2(2),
        N3(3),
        N5(5),
        N10(10),
        N50(50),
        N100(100),
        N500(500),
        N1000(1000),
        N2000(2000),
        N5000(5000)

        final int value

        private Denominations(int value) {
            this.value = value
        }
    }

    /**
     * Валюта банкнот
     */
    enum Currency {
        RUB, EUR, USD
    }


    final Denominations nominal
    final Currency currency

    private static final Map<String, Banknote> store = new LinkedHashMap<>() // статический кэш банкнот

    private Banknote(Denominations nominal, Currency currency) {
        this.nominal = nominal
        this.currency = currency
    }

    /**
     * Получить описание банкноты по номиналу и валюте
     * @param nominal Запрошенный номинал
     * @param currency Запрошенная валюта
     * @return Описание банкноты
     */
    static Banknote get(Denominations nominal, Currency currency) {
        String key = key(nominal, currency)
        Banknote money = store.get(key)
        if (money == null) {
            throw new IllegalArgumentException("The money $nominal : $currency is not exists")
        }
        return money
    }


    /**
     * Получить описания банкнот для конкретно валюте
     * @param currency Запрошенная валюта
     * @return Список с описанием банкнот
     */
    static List<Banknote> get(Currency currency) {
        List<Banknote> result = store.values()
                .findAll {it.currency == currency}
        return result
    }


    //todo: move to property
    static void init() {
        initMoney(Denominations.N50, Currency.RUB)
        initMoney(Denominations.N100, Currency.RUB)
        initMoney(Denominations.N500, Currency.RUB)
        initMoney(Denominations.N1000, Currency.RUB)
        initMoney(Denominations.N2000, Currency.RUB)
        initMoney(Denominations.N5000, Currency.RUB)
    }
    private static void initMoney(Denominations nominal, Currency currency) {
        String key = key(nominal, currency)
        store.put(key, new Banknote(nominal, currency))
    }


    private static String key(Denominations nominal, Currency currency) {
        return "$currency | $nominal"
    }


    String toString() {
        return "$currency | $nominal"
    }

}
