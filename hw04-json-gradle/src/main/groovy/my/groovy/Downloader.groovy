package my.groovy


import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Downloader {
    private final HttpClient client

    Downloader() {
        client = HttpClient.newBuilder().build()
    }

    String download(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers("Content-Type", "application/json", "accept", "application/json")
                .GET()
                .build()

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

}
