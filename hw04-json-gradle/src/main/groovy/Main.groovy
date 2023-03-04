import my.groovy.Downloader
import my.groovy.Worker

static void main(String[] args) {
  def url = "https://raw.githubusercontent.com/kbhmstpis/groovy-study/main/hw04-json-gradle/src/test/resources/test.json"

  Downloader downloader = new Downloader()
  Worker worker = new Worker(downloader)

  String html = worker.toHtml(url)
  println html

  String xml = worker.toXML(url)
  println xml
}