package atm


import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class CashTest {

    @Test
    void flow() {
        Cash box = new Cash()

        Banknote.init()
        Banknote money = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)

        box.load(money, 10)

        assertTrue(box.check(money, 1))
        assertTrue(box.check(money, 10))
        assertFalse(box.check(money, 11))

        box.extract(money, 1)
        assertTrue(box.check(money, 1))
        assertFalse(box.check(money, 10))

        box.extract(money, 9)
        assertFalse(box.check(money, 1))
    }

    @Test
    void add() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)
        Banknote money2 = Banknote.get(Banknote.Denominations.N500, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 10)

        Cash cash2 = new Cash()
        cash2.load(money1, 10)

        Cash cash3 = cash1 + cash2
        assertTrue(cash3.check(money1, 20))

        cash2.load(money2, 10)
        Cash cash4 = cash1 + cash2
        assertTrue(cash4.check(money1, 20))
        assertTrue(cash4.check(money2, 10))
    }

    @Test
    void remove() {
        Banknote.init()
        Banknote money1 = Banknote.get(Banknote.Denominations.N100, Banknote.Currency.RUB)
        Banknote money2 = Banknote.get(Banknote.Denominations.N500, Banknote.Currency.RUB)

        Cash cash1 = new Cash()
        cash1.load(money1, 20)
        cash1.load(money2, 20)

        Cash cash2 = new Cash()
        cash2.load(money2, 10)

        Cash cash3 = cash1 - cash2
        assertTrue(cash3.check(money1, 20))
        assertTrue(cash3.check(money2, 10))

        Cash cash4 = cash3 - cash2
        assertTrue(cash4.check(money1, 20))
        assertTrue(cash4.check(money2, 0))

        Cash cash5 = null
        try {
            cash5 = cash4 - cash2
        }
        catch (IllegalStateException ex) {
            assertEquals("No money RUB | N500", ex.getMessage())
        }
        assertNull(cash5)

    }



}
