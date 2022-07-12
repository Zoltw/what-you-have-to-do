package efs.task.todoapp.service.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.web.HTTPcode;

import java.io.IOException;

public class OptionsUserHandler {
    public void handle(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(HTTPcode.OK.getValue(), 0);
        exchange.close();
    }
}
