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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.stream.Stream;

@ExtendWith({ToDoServerExtension.class})
class UserEndpointTest {
    private HttpClient httpClient;

    UserEndpointTest() {
    }

    @BeforeEach
    void setUp() {
        this.httpClient = HttpClient.newHttpClient();
    }

    private static Stream<String> badRequestBody() {
        return Stream.of("", TestUtils.userJson("username", null), TestUtils.userJson("username", ""), TestUtils.userJson(null, "password"), TestUtils.userJson("", "password"));
    }

    @ParameterizedTest(
            name = "Request body = {0}"
    )
    @MethodSource({"badRequestBody"})
    @Timeout(1L)
    void shouldReturnBadRequestStatus(String body) throws IOException, InterruptedException {
        HttpRequest httpRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(body)).build();
        HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return TestUtils.wrongCodeMessage(httpRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @Test
    @Timeout(1L)
    void shouldReturnCreatedStatus() throws IOException, InterruptedException {
        HttpRequest httpRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return TestUtils.wrongCodeMessage(httpRequest);
        })).isEqualTo(HttpResonseStatus.CREATED.getCode());
    }

    @Test
    @Timeout(1L)
    void shouldReturnConflictStatus() throws IOException, InterruptedException {
        HttpRequest httpRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(httpRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "Creation of a user with the existing username. " + TestUtils.wrongCodeMessage(httpRequest);
        })).isEqualTo(HttpResonseStatus.CONFLICT.getCode());
    }
}
