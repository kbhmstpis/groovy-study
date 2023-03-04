package my.groovy

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class DownloaderTest {

    @Disabled
    @Test
    void download() {
        def expected = """
{
  "name": "Пупкин Морква Свеклович",
  "age": 22,
  "secretIdentity": "322-223",
  "powers": [100, 50, 70]
}
""".trim()

        String url = "https://raw.githubusercontent.com/kbhmstpis/groovy-study/main/hw04-json-gradle/src/test/resources/test.json"
        def downloader = new Downloader()
        String jsonString = downloader.download(url)
        assertEquals(expected, jsonString)

    }


}
