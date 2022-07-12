//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package efs.task.todoapp.autograding;

import efs.task.todoapp.util.ToDoServerExtension;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith({ToDoServerExtension.class})
class TaskEndpointTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String BASE_64_USERNAME = "dXNlcm5hbWU=";
    private static final String BASE_64_USERNAME_2 = "dXNlcm5hbWUy";
    private static final String BASE_64_PASSWORD = "cGFzc3dvcmQ=";
    private static final String BASE_64_WRONG_PASSWORD = "d3JvbmdQYXNzd29yZA==";
    private HttpClient httpClient;

    TaskEndpointTest() {
    }

    @BeforeEach
    void setUp() {
        this.httpClient = HttpClient.newHttpClient();
    }

    private static Stream<Arguments> authHeaderBadRequestsPost() {
        return Stream.of(TestUtils.taskPostArguments(null, TestUtils.taskJson("task")), TestUtils.taskPostArguments("dXNlcm5hbWU=", TestUtils.taskJson("task")), TestUtils.taskPostArguments("dummy:cGFzc3dvcmQ=", TestUtils.taskJson("task")), TestUtils.taskPostArguments("dXNlcm5hbWU=:dummy", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing auth header", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> authHeaderBadRequestsGet() {
        return Stream.of(TestUtils.taskGetArguments(null), TestUtils.taskGetArguments("dXNlcm5hbWU="), TestUtils.taskGetArguments("dummy:cGFzc3dvcmQ="), TestUtils.taskGetArguments("dXNlcm5hbWU=:dummy")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing auth header", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> authHeaderBadRequestsPut() {
        return Stream.of(TestUtils.taskPutArguments(null, TestUtils.taskJson("task")), TestUtils.taskPutArguments("dXNlcm5hbWU=", TestUtils.taskJson("task")), TestUtils.taskPutArguments("dummy:cGFzc3dvcmQ=", TestUtils.taskJson("task")), TestUtils.taskPutArguments("dXNlcm5hbWU=:dummy", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing auth header", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> authHeaderBadRequestsDelete() {
        return Stream.of(TestUtils.taskDeleteArguments(null), TestUtils.taskDeleteArguments("dXNlcm5hbWU="), TestUtils.taskDeleteArguments("dummy:cGFzc3dvcmQ="), TestUtils.taskDeleteArguments("dXNlcm5hbWU=:dummy")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing auth header", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> taskBodyBadRequestsPost() {
        return Stream.of(TestUtils.taskPostArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", ""), TestUtils.taskPostArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson(null)), TestUtils.taskPostArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson("buy milk", "not-a-date"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing task body", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> taskBodyBadRequestsPut() {
        return Stream.of(TestUtils.taskPutArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", ""), TestUtils.taskPutArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson(null)), TestUtils.taskPutArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson("buy milk", "not-a-date"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing task body", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> taskPathBadRequestsGet() {
        return Stream.of(TestUtils.taskGetArguments("123", "dXNlcm5hbWU=:cGFzc3dvcmQ=")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing task path", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> taskPathBadRequestsPut() {
        return Stream.of(TestUtils.taskPutArguments("dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson("task")), TestUtils.taskPutArguments("123", "dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing task path", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> taskPathBadRequestsDelete() {
        return Stream.of(TestUtils.taskDeleteArguments("dXNlcm5hbWU=:cGFzc3dvcmQ="), TestUtils.taskDeleteArguments("123", "dXNlcm5hbWU=:cGFzc3dvcmQ=")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong or missing task path", objects[0], objects[1]);
        });
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"taskPathBadRequestsGet"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusIncorrectTaskPathGet(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"taskPathBadRequestsPut"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusIncorrectTaskPathPut(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"taskPathBadRequestsDelete"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusIncorrectTaskPathDelete(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"taskBodyBadRequestsPost"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusIncorrectTaskBodyPost(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"taskBodyBadRequestsPut"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusIncorrectTaskBodyPut(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"authHeaderBadRequestsPost"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusForIncorrectAuthenticationHeadersPost(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"authHeaderBadRequestsGet"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusForIncorrectAuthenticationHeadersGet(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"authHeaderBadRequestsPut"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusForIncorrectAuthenticationHeadersPut(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"authHeaderBadRequestsDelete"})
    @Timeout(1L)
    void shouldReturnBadRequestStatusForIncorrectAuthenticationHeadersDelete(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.BAD_REQUEST.getCode());
    }

    private static Stream<Arguments> wrongUsernameUnauthorizedRequestsGet() {
        return Stream.of(TestUtils.taskGetArguments("dXNlcm5hbWUy:cGFzc3dvcmQ="), TestUtils.taskGetArguments(ID, "dXNlcm5hbWUy:cGFzc3dvcmQ=")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong username", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongUsernameUnauthorizedRequestsPost() {
        return Stream.of(TestUtils.taskPostArguments("dXNlcm5hbWUy:cGFzc3dvcmQ=", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong username", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongUsernameUnauthorizedRequestsPut() {
        return Stream.of(TestUtils.taskPutArguments(ID, "dXNlcm5hbWUy:cGFzc3dvcmQ=", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong username", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongUsernameUnauthorizedRequestsDelete() {
        return Stream.of(TestUtils.taskDeleteArguments(ID, "dXNlcm5hbWUy:cGFzc3dvcmQ=")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong username", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongPasswordUnauthorizedRequestsGet() {
        return Stream.of(TestUtils.taskGetArguments("dXNlcm5hbWUy:d3JvbmdQYXNzd29yZA=="), TestUtils.taskGetArguments(ID, "dXNlcm5hbWUy:d3JvbmdQYXNzd29yZA==")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong password", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongPasswordUnauthorizedRequestsPost() {
        return Stream.of(TestUtils.taskPostArguments("dXNlcm5hbWU=:d3JvbmdQYXNzd29yZA==", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong password", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongPasswordUnauthorizedRequestsPut() {
        return Stream.of(TestUtils.taskPutArguments(ID, "dXNlcm5hbWUy:d3JvbmdQYXNzd29yZA==", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong password", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> wrongPasswordUnauthorizedRequestsDelete() {
        return Stream.of(TestUtils.taskDeleteArguments(ID, "dXNlcm5hbWUy:d3JvbmdQYXNzd29yZA==")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Wrong password", objects[0], objects[1]);
        });
    }

    private static Stream<HttpRequest.Builder> unauthorizedRequests() {
        return Stream.of(TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWUy:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("task"))), TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWUy:cGFzc3dvcmQ=").GET(), TestUtils.taskRequestBuilder(ID).header("auth", "dXNlcm5hbWUy:cGFzc3dvcmQ=").GET(), TestUtils.taskRequestBuilder(ID).header("auth", "dXNlcm5hbWUy:cGFzc3dvcmQ=").PUT(BodyPublishers.ofString(TestUtils.taskJson("task"))), TestUtils.taskRequestBuilder(ID).header("auth", "dXNlcm5hbWUy:cGFzc3dvcmQ=").DELETE(), TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:d3JvbmdQYXNzd29yZA==").POST(BodyPublishers.ofString(TestUtils.taskJson("task"))), TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:d3JvbmdQYXNzd29yZA==").GET(), TestUtils.taskRequestBuilder(ID).header("auth", "dXNlcm5hbWU=:d3JvbmdQYXNzd29yZA==").GET(), TestUtils.taskRequestBuilder(ID).header("auth", "dXNlcm5hbWU=:d3JvbmdQYXNzd29yZA==").PUT(BodyPublishers.ofString(TestUtils.taskJson("task"))), TestUtils.taskRequestBuilder(ID).header("auth", "dXNlcm5hbWU=:d3JvbmdQYXNzd29yZA==").DELETE());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongUsernameUnauthorizedRequestsGet"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongUsernameGet(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongUsernameUnauthorizedRequestsPost"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongUsernamePost(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongUsernameUnauthorizedRequestsPut"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongUsernamePut(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongUsernameUnauthorizedRequestsDelete"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongUsername(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongPasswordUnauthorizedRequestsGet"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongPasswordGet(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongPasswordUnauthorizedRequestsPost"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongPasswordPost(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongPasswordUnauthorizedRequestsPut"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongPasswordPut(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"wrongPasswordUnauthorizedRequestsDelete"})
    @Timeout(1L)
    void shouldReturnUnauthorizedStatusWrongPasswordDelete(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.UNAUTHORIZED.getCode());
    }

    @Test
    @Timeout(1L)
    void shouldCreateTask() throws IOException, InterruptedException, JSONException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpRequest postTaskRequest = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("buy milk", "2021-06-30"))).build();
        HttpResponse<String> postTaskResponse = this.httpClient.send(postTaskRequest, BodyHandlers.ofString());
        org.junit.jupiter.api.Assertions.assertAll(() -> {
            ((AbstractIntegerAssert)Assertions.assertThat(postTaskResponse.statusCode()).as(TestUtils.wrongCodeMessage(postTaskRequest), new Object[0])).isEqualTo(HttpResonseStatus.CREATED.getCode());
        }, () -> {
            JSONObject jsonResponse = this.getJsonFromResponse(postTaskResponse);
            ((AbstractStringAssert)Assertions.assertThat(jsonResponse.getString("id")).as("Wrong task identifier in response", new Object[0])).is(TestUtils.validUUID());
        });
    }

    @Test
    @Timeout(1L)
    void shouldReturnAllTasksForUser() throws IOException, InterruptedException, JSONException {
        HttpRequest user1Request = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(user1Request, BodyHandlers.ofString());
        HttpRequest user2Request = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username2", "password"))).build();
        this.httpClient.send(user2Request, BodyHandlers.ofString());
        HttpRequest postTaskForUser1Request = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("buy milk", "2021-06-30"))).build();
        HttpResponse<String> postTaskForUser1RequestResponse = this.httpClient.send(postTaskForUser1Request, BodyHandlers.ofString());
        String task1Id = this.getIdOfCreatedTask(postTaskForUser1RequestResponse);
        postTaskForUser1Request = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("eat an apple"))).build();
        postTaskForUser1RequestResponse = this.httpClient.send(postTaskForUser1Request, BodyHandlers.ofString());
        String task2Id = this.getIdOfCreatedTask(postTaskForUser1RequestResponse);
        HttpRequest postTaskForUser2Request = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWUy:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("buy milk", "2021-06-30"))).build();
        this.httpClient.send(postTaskForUser2Request, BodyHandlers.ofString());
        HttpRequest getForUser1Request = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").GET().build();
        HttpResponse<String> getForUser1Response = this.httpClient.send(getForUser1Request, BodyHandlers.ofString());
        JSONArray jsonTasks = new JSONArray(getForUser1Response.body());
        JSONObject[] sortedTasks = this.sortJsonTasks(task1Id, jsonTasks);
        org.junit.jupiter.api.Assertions.assertAll(() -> {
            ((AbstractIntegerAssert)Assertions.assertThat(getForUser1Response.statusCode()).as(TestUtils.wrongCodeMessage(getForUser1Request), new Object[0])).isEqualTo(HttpResonseStatus.OK.getCode());
        }, () -> {
            ((AbstractIntegerAssert)Assertions.assertThat(jsonTasks.length()).as("Number of tasks", new Object[0])).isEqualTo(2);
        }, () -> {
            JSONAssert.assertEquals("Wrong task in response : ", "{\"id\" : \"" + task1Id + "\",\"description\" : \"buy milk\",\"due\" : \"2021-06-30\"}", sortedTasks[0], JSONCompareMode.STRICT);
        }, () -> {
            JSONAssert.assertEquals("Wrong task in response : ", "{\"id\" : \"" + task2Id + "\",\"description\" : \"eat an apple\"}", sortedTasks[1], JSONCompareMode.STRICT);
        });
    }

    private JSONObject[] sortJsonTasks(String task1Id, JSONArray jsonTasks) throws JSONException {
        JSONObject jsonTask1 = (JSONObject)jsonTasks.get(1);
        JSONObject jsonTask2 = (JSONObject)jsonTasks.get(0);
        JSONObject[] sortedTasks = new JSONObject[2];
        if (task1Id.equals(jsonTask1.getString("id"))) {
            sortedTasks[0] = jsonTask1;
            sortedTasks[1] = jsonTask2;
        } else {
            sortedTasks[0] = jsonTask2;
            sortedTasks[1] = jsonTask1;
        }

        return sortedTasks;
    }

    @Test
    @Timeout(1L)
    void shouldReturnTask() throws IOException, InterruptedException, JSONException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpRequest postTaskRequest = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("buy milk", "2021-06-30"))).build();
        HttpResponse<String> postTaskResponse = this.httpClient.send(postTaskRequest, BodyHandlers.ofString());
        String taskId = this.getIdOfCreatedTask(postTaskResponse);
        HttpRequest getRequest = TestUtils.taskRequestBuilder(taskId).header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").GET().build();
        HttpResponse<String> getResponse = this.httpClient.send(getRequest, BodyHandlers.ofString());
        JSONObject jsonTask = this.getJsonFromResponse(getResponse);
        org.junit.jupiter.api.Assertions.assertAll(() -> {
            ((AbstractIntegerAssert)Assertions.assertThat(getResponse.statusCode()).as(TestUtils.wrongCodeMessage(getRequest), new Object[0])).isEqualTo(HttpResonseStatus.OK.getCode());
        }, () -> {
            JSONAssert.assertEquals("Wrong task in response : ", "{\"id\" : \"" + taskId + "\",\"description\" : \"buy milk\",\"due\" : \"2021-06-30\"}", jsonTask, JSONCompareMode.STRICT);
        });
    }

    private static Stream<Arguments> notFoundRequestsGet() {
        return Stream.of(TestUtils.taskGetArguments(ID, "dXNlcm5hbWU=:cGFzc3dvcmQ=")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Task not found", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> notFoundRequestsPut() {
        return Stream.of(TestUtils.taskPutArguments(ID, "dXNlcm5hbWU=:cGFzc3dvcmQ=", TestUtils.taskJson("task"))).map(Arguments::get).map((objects) -> {
            return Arguments.of("Task not found", objects[0], objects[1]);
        });
    }

    private static Stream<Arguments> notFoundRequestsDelete() {
        return Stream.of(TestUtils.taskDeleteArguments(ID, "dXNlcm5hbWU=:cGFzc3dvcmQ=")).map(Arguments::get).map((objects) -> {
            return Arguments.of("Task not found", objects[0], objects[1]);
        });
    }

    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"notFoundRequestsGet"})
    @Timeout(1L)
    void shouldReturnNotFoundGet(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.NOT_FOUND.getCode());
    }


    @ParameterizedTest(
            name = "{1}"
    )
    @MethodSource({"notFoundRequestsDelete"})
    @Timeout(1L)
    void shouldReturnNotFoundDelete(String testPrefix, String testName, HttpRequest taskRequest) throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpResponse<String> httpResponse = this.httpClient.send(taskRequest, BodyHandlers.ofString());
        ((AbstractIntegerAssert)Assertions.assertThat(httpResponse.statusCode()).as(() -> {
            return "[" + testPrefix + " : " + testName + "] " + TestUtils.wrongCodeMessage(taskRequest);
        })).isEqualTo(HttpResonseStatus.NOT_FOUND.getCode());
    }

    @Test
    @Timeout(1L)
    void shouldUpdateTask() throws IOException, InterruptedException, JSONException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpRequest postTaskRequest = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("buy milk", "2021-06-30"))).build();
        HttpResponse<String> postTaskResponse = this.httpClient.send(postTaskRequest, BodyHandlers.ofString());
        String taskId = this.getIdOfCreatedTask(postTaskResponse);
        HttpRequest putRequest = TestUtils.taskRequestBuilder(taskId).header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").PUT(BodyPublishers.ofString(TestUtils.taskJson("eat an apple"))).build();
        HttpRequest getRequest = TestUtils.taskRequestBuilder(taskId).header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").GET().build();
        HttpResponse<String> putResponse = this.httpClient.send(putRequest, BodyHandlers.ofString());
        HttpResponse<String> getResponse = this.httpClient.send(getRequest, BodyHandlers.ofString());
        org.junit.jupiter.api.Assertions.assertAll(() -> {
            ((AbstractIntegerAssert)Assertions.assertThat(putResponse.statusCode()).as(TestUtils.wrongCodeMessage(putRequest), new Object[0])).isEqualTo(HttpResonseStatus.OK.getCode());
        }, () -> {
            ((AbstractIntegerAssert)Assertions.assertThat(getResponse.statusCode()).as(TestUtils.wrongCodeMessage(getRequest), new Object[0])).isEqualTo(HttpResonseStatus.OK.getCode());
        }, () -> {
            JSONAssert.assertEquals("Wrong task in response : ", "{\"id\" : \"" + taskId + "\",\"description\" : \"eat an apple\"}", putResponse.body(), JSONCompareMode.STRICT);
        }, () -> {
            JSONAssert.assertEquals("Wrong task in response : ", "{\"id\" : \"" + taskId + "\",\"description\" : \"eat an apple\"}", putResponse.body(), JSONCompareMode.STRICT);
        });
    }

    @Test
    @Timeout(1L)
    void shouldDeleteTask() throws IOException, InterruptedException {
        HttpRequest userRequest = TestUtils.userRequestBuilder().POST(BodyPublishers.ofString(TestUtils.userJson("username", "password"))).build();
        this.httpClient.send(userRequest, BodyHandlers.ofString());
        HttpRequest postTaskRequest = TestUtils.taskRequestBuilder().header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").POST(BodyPublishers.ofString(TestUtils.taskJson("buy milk", "2021-06-30"))).build();
        HttpResponse<String> postTaskResponse = this.httpClient.send(postTaskRequest, BodyHandlers.ofString());
        String taskId = this.getIdOfCreatedTask(postTaskResponse);
        HttpRequest deleteRequest = TestUtils.taskRequestBuilder(taskId).header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").DELETE().build();
        HttpRequest getRequest = TestUtils.taskRequestBuilder(taskId).header("auth", "dXNlcm5hbWU=:cGFzc3dvcmQ=").GET().build();
        HttpResponse<String> deleteResponse = this.httpClient.send(deleteRequest, BodyHandlers.ofString());
        HttpResponse<String> getResponse = this.httpClient.send(getRequest, BodyHandlers.ofString());
        org.junit.jupiter.api.Assertions.assertAll(() -> {
            ((AbstractIntegerAssert)Assertions.assertThat(deleteResponse.statusCode()).as(TestUtils.wrongCodeMessage(deleteRequest), new Object[0])).isEqualTo(HttpResonseStatus.OK.getCode());
        }, () -> {
            ((AbstractIntegerAssert)Assertions.assertThat(getResponse.statusCode()).as(TestUtils.wrongCodeMessage(getRequest), new Object[0])).isEqualTo(HttpResonseStatus.NOT_FOUND.getCode());
        });
    }

    private String getIdOfCreatedTask(HttpResponse<String> taskResponse) {
        try {
            return taskResponse.body().split("\"")[3];
        } catch (Exception var3) {
            org.junit.jupiter.api.Assertions.fail("Can't get id of created task", var3);
            return null;
        }
    }

    private JSONObject getJsonFromResponse(HttpResponse<String> response) throws JSONException {
        try {
            return new JSONObject(response.body());
        } catch (JSONException var3) {
            org.junit.jupiter.api.Assertions.fail("Can't serialize a response to JSON format", var3);
            return null;
        }
    }
}
