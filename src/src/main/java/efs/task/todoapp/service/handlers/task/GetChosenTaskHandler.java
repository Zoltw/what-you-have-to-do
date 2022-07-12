package efs.task.todoapp.service.handlers.task;

import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.AnotherUserException;
import efs.task.todoapp.IncorrectData;
import efs.task.todoapp.NotFoundException;
import efs.task.todoapp.repository.UserEntity;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.service.UnathorizedAccessException;
import efs.task.todoapp.web.HTTPcode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetChosenTaskHandler {

    public void handle(HttpExchange exchange, ToDoService service) throws IOException {
        try {
            UserEntity userEntity = service.getUser(exchange.getRequestHeaders());
            String description = service.getChosenTask(exchange.getRequestURI().getPath(), userEntity);
            exchange.sendResponseHeaders(HTTPcode.OK.getValue(), description.length());
            OutputStream os = exchange.getResponseBody();
            os.write(description.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (NotFoundException NotFoundException) {
            exchange.sendResponseHeaders(HTTPcode.NOT_FOUND.getValue(), 0);
        } catch (UnathorizedAccessException unathorizedAccessException) {
            exchange.sendResponseHeaders(HTTPcode.UNAUTHORIZED.getValue(), 0);
        } catch (AnotherUserException anotherUserException) {
            exchange.sendResponseHeaders(HTTPcode.FORBIDDEN.getValue(), 0);
        } catch (IncorrectData incorrectData) {
            exchange.sendResponseHeaders(HTTPcode.BAD_REQUEST.getValue(), 0);
        }
        exchange.close();

    }
}
