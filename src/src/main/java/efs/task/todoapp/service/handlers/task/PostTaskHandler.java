package efs.task.todoapp.service.handlers.task;

import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.IncorrectData;
import efs.task.todoapp.NotFoundException;
import efs.task.todoapp.repository.UserEntity;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.service.UnathorizedAccessException;
import efs.task.todoapp.web.HTTPcode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class PostTaskHandler {
    public void handle(HttpExchange exchange, ToDoService service) throws IOException, UnathorizedAccessException {
        String taskString = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        try {
            UserEntity userEntity = service.getUser(exchange.getRequestHeaders());
            String uuid = service.createTask(userEntity, taskString);
            exchange.sendResponseHeaders(HTTPcode.CREATED.getValue(), uuid.length());
            OutputStream os = exchange.getResponseBody();
            os.write(uuid.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (IncorrectData incorrectData) {
            exchange.sendResponseHeaders(HTTPcode.BAD_REQUEST.getValue(), 0);
        } catch (UnathorizedAccessException e) {
            exchange.sendResponseHeaders(HTTPcode.UNAUTHORIZED.getValue(), 0);
        } catch (NotFoundException notFoundException) {
            exchange.sendResponseHeaders(HTTPcode.NOT_FOUND.getValue(), 0);
        }
        exchange.close();
    }
}
