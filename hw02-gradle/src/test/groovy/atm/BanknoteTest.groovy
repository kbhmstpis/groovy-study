package atm

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class BanknoteTest {

    @Test
    void getCurrency() {
        Banknote.init()
        List<Banknote> moneys = Banknote.get(Banknote.Currency.RUB)
        assertEquals(6, moneys.size())
    }
}
