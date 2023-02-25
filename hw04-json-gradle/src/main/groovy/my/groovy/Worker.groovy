package my.groovy

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

class Worker {

    private final Downloader downloader

    Worker(Downloader downloader) {
        this.downloader = downloader
    }


    String toHtml(String url) {
        String jsonStr = downloader.download(url)
        def slurper = new JsonSlurper()
        def json = slurper.parse(jsonStr.toCharArray())

        def writer = new StringWriter()

        def b = new MarkupBuilder(writer)
        b.doubleQuotes = true

        b.div {
            div("id":"employee") {
                p("name: " + json.name)
                b.br()
                p("age: " + json.age)
                b.br()
                p("secretIdentity: " + json.secretIdentity)
                b.br()
                b.ul("id":"powers") {
                    json.powers.each {
                        b.li(it)
                    }
                }
            }
        }

        b.println()
        return writer.toString()
    }

    String toXML(String url) {
        String jsonStr = downloader.download(url)
        def slurper = new JsonSlurper()
        def json = slurper.parse(jsonStr.toCharArray())

        def writer = new StringWriter()

        def b = new MarkupBuilder(writer)
        b.doubleQuotes = true

        b.employees {
            employee {
                name(json.name)
                age(json.age)
                secretIdentity(json.secretIdentity)
                b.powers {
                    json.powers.each {
                        b.power(it)
                    }
                }
            }

        }


        b.println()
        return writer.toString()
    }

}
