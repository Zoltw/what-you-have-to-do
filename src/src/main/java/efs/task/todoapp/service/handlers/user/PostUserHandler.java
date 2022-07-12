package efs.task.todoapp.service.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.IncorrectData;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.service.UnathorizedAccessException;
import efs.task.todoapp.web.HTTPcode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PostUserHandler {

    public void handle(HttpExchange exchange, ToDoService service) throws IOException{
        final String userString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        try {
            String response = service.saveUser(userString);
            exchange.sendResponseHeaders(HTTPcode.CREATED.getValue(), response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (IncorrectData incorrectData) {
            exchange.sendResponseHeaders(HTTPcode.CONFLICT.getValue(), 0);
        } catch (UnathorizedAccessException e) {
            exchange.sendResponseHeaders(HTTPcode.BAD_REQUEST.getValue(), 0);
        }
        exchange.close();
    }
}
