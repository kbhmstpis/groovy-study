package my.groovy

import org.junit.jupiter.api.Test
import org.mockito.Mockito

import static org.junit.jupiter.api.Assertions.*

class WorkerTest {

    def json = """
{
  "name": "Пупкин Морква Свеклович",
  "age": 22,
  "secretIdentity": "322-223",
  "powers": [100, 50, 70]
}
""".trim()


    @Test
    void toHtml() {

        def expected = """
<div>
  <div id="employee">
    <p>name: Пупкин Морква Свеклович</p>
    <br />
    <p>age: 22</p>
    <br />
    <p>secretIdentity: 322-223</p>
    <br />
    <ul id="powers">
      <li>100</li>
      <li>50</li>
      <li>70</li>
    </ul>
  </div>
</div>
""".trim()

        Downloader downloaderMock = Mockito.mock(Downloader.class)
        Mockito.when(downloaderMock.download("url")).thenReturn(json)

        Worker worker = new Worker(downloaderMock)

        String html = worker.toHtml("url")
        assertEquals(expected, html)
    }

    @Test
    void toXML() {

        def expected = """
<employees>
  <employee>
    <name>Пупкин Морква Свеклович</name>
    <age>22</age>
    <secretIdentity>322-223</secretIdentity>
    <powers>
      <power>100</power>
      <power>50</power>
      <power>70</power>
    </powers>
  </employee>
</employees>
""".trim()

        Downloader downloaderMock = Mockito.mock(Downloader.class)
        Mockito.when(downloaderMock.download("url")).thenReturn(json)

        Worker worker = new Worker(downloaderMock)

        String xml = worker.toXML("url")
        assertEquals(expected, xml)
    }

}
