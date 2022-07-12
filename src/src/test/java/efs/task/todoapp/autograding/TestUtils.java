//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package efs.task.todoapp.autograding;

import org.assertj.core.api.Condition;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class TestUtils {
    static final String PATH_ROOT = "http://localhost:8080/todo";
    static final String PATH_USER = "http://localhost:8080/todo/user";
    static final String PATH_TASK = "http://localhost:8080/todo/task";
    static final String HEADER_AUTH = "auth";

    private TestUtils() {
    }

    static String userJson(String username, String password) {
        Map<String, String> userProperties = new HashMap();
        userProperties.put("username", username);
        userProperties.put("password", password);
        return toJson(userProperties);
    }

    static String taskJson(String description) {
        return taskJson(description, null);
    }

    static String taskJson(String description, String due) {
        Map<String, String> taskProperties = new HashMap();
        taskProperties.put("description", description);
        taskProperties.put("due", due);
        return toJson(taskProperties);
    }

    private static String toJson(Map<String, ?> properties) {
        return properties.entrySet().stream().filter((entry) -> {
            return Objects.nonNull(entry.getValue());
        }).map((entry) -> {
            String var10000 = entry.getKey();
            return "\"" + var10000 + "\":\"" + entry.getValue() + "\"";
        }).collect(Collectors.joining(",", "{", "}"));
    }

    static HttpRequest.Builder userRequestBuilder() {
        return HttpRequest.newBuilder(URI.create("http://localhost:8080/todo/user"));
    }

    static HttpRequest.Builder taskRequestBuilder() {
        return HttpRequest.newBuilder(URI.create("http://localhost:8080/todo/task"));
    }

    static HttpRequest.Builder taskRequestBuilder(Object taskId) {
        return HttpRequest.newBuilder(URI.create("http://localhost:8080/todo/task/" + taskId.toString()));
    }

    static Arguments taskPostArguments(String authHeader, String body) {
        HttpRequest.Builder requestBuilder = taskRequestBuilder();
        if (Objects.nonNull(authHeader)) {
            requestBuilder.header("auth", authHeader);
        }

        return Arguments.of("auth = " + authHeader + " , body = " + body, requestBuilder.POST(BodyPublishers.ofString(body)).build());
    }

    public static Arguments taskGetArguments(String authHeader) {
        return taskGetArguments(authHeader, taskRequestBuilder());
    }

    public static Arguments taskGetArguments(Object taskId, String authHeader) {
        return taskGetArguments(authHeader, taskRequestBuilder(taskId));
    }

    static Arguments taskGetArguments(String authHeader, HttpRequest.Builder requestBuilder) {
        if (Objects.nonNull(authHeader)) {
            requestBuilder.header("auth", authHeader);
        }

        return Arguments.of("auth = " + authHeader, requestBuilder.GET().build());
    }

    static Arguments taskPutArguments(String authHeader, String body) {
        return taskPutArguments(authHeader, body, taskRequestBuilder());
    }

    static Arguments taskPutArguments(Object taskId, String authHeader, String body) {
        return taskPutArguments(authHeader, body, taskRequestBuilder(taskId));
    }

    static Arguments taskPutArguments(String authHeader, String body, HttpRequest.Builder requestBuilder) {
        if (Objects.nonNull(authHeader)) {
            requestBuilder.header("auth", authHeader);
        }

        return Arguments.of("auth = " + authHeader + " , body = " + body, requestBuilder.PUT(BodyPublishers.ofString(body)).build());
    }

    static Arguments taskDeleteArguments(String authHeader) {
        return taskDeleteArguments(authHeader, taskRequestBuilder());
    }

    static Arguments taskDeleteArguments(Object taskId, String authHeader) {
        return taskDeleteArguments(authHeader, taskRequestBuilder(taskId));
    }

    static Arguments taskDeleteArguments(String authHeader, HttpRequest.Builder requestBuilder) {
        if (Objects.nonNull(authHeader)) {
            requestBuilder.header("auth", authHeader);
        }

        return Arguments.of("auth = " + authHeader, requestBuilder.DELETE().build());
    }

    static Condition<String> validUUID() {
        return new Condition((id) -> {
            try {
                UUID.fromString((String) id);
                return true;
            } catch (IllegalArgumentException var2) {
                return false;
            }
        }, "Valid UUID");
    }

    static String wrongCodeMessage(HttpRequest request) {
        StringBuilder stringBuilder = (new StringBuilder()).append("Wrong HTTP status code for ").append(request.method()).append(" ").append(request.uri()).append(" endpoint");
        return stringBuilder.toString();
    }
}
