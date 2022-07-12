//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package efs.task.todoapp.autograding;

import efs.task.todoapp.util.ToDoServerExtension;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

@ExtendWith({ToDoServerExtension.class})
class NonExistingEndpointTest {
    private HttpClient httpClient;

    NonExistingEndpointTest() {
    }

    @BeforeEach
    void setUp() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Test
    @Timeout(1L)
    void shouldReturnNotFoundStatusForUnhandledPaths() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/todonon/exisiting/endpoint")).GET().build();
        HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as("Wrong HTTP status code for non existing endpoint", new Object[0])).isEqualTo(HttpResonseStatus.NOT_FOUND.getCode());
    }
}
