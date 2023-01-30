package atm

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class ATMTest {

    @Test
    void input() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 10)
        ATM atm = new ATM()
        atm.input(cash1)

        assertTrue(cash1.check(money1, 1))
    }

    @Test
    void give_happy_path() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)
        Banknote money2 = Banknote.get(Banknote.Denominations.N500, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 10)
        cash1.load(money2, 10)
        ATM atm = new ATM()
        atm.input(cash1)


        Cash cash2 = atm.give(600, Banknote.Currency.RUB)
        assertTrue(cash2.check(money1, 1))
        assertTrue(cash2.check(money2, 1))
    }

    @Test
    void "give: No big denominations"() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)
        Banknote money2 = Banknote.get(Banknote.Denominations.N500, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 20)
        cash1.load(money2, 1)
        ATM atm = new ATM()
        atm.input(cash1)

        Cash cash2 = atm.give(1200, Banknote.Currency.RUB)
        assertTrue(cash2.check(money1, 12))
        assertFalse(cash2.check(money2, 1))
    }

    @Test
    void "give: Insufficient funds"() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)
        Banknote money2 = Banknote.get(Banknote.Denominations.N500, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 1)
        cash1.load(money2, 1)
        ATM atm = new ATM()
        atm.input(cash1)

        Cash cash2 = null
        try {
            cash2 = atm.give(1200, Banknote.Currency.RUB)
        }
        catch (IllegalStateException ex) {
            assertEquals(IllegalStateException.class, ex.getClass())
        }
        assertNull(cash2)
    }


    @Test
    void report() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)
        Banknote money2 = Banknote.get(Banknote.Denominations.N500, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 20)
        cash1.load(money2, 1)

        ATM atm = new ATM()
        atm.input(cash1)

        String report = atm.report()

        String expected = """
RUB
100: 20 items
500: 1 items
Total: 2500 

EUR
Total: 0 

USD
Total: 0
""".trim()
        assertEquals(expected, report.trim())
    }
}
