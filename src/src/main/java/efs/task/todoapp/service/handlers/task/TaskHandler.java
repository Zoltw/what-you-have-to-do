package efs.task.todoapp.service.handlers.task;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.service.UnathorizedAccessException;

import java.io.IOException;

public class TaskHandler implements HttpHandler {

    ToDoService service;

    public TaskHandler(ToDoService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Auth");
        switch (httpExchange.getRequestMethod()) {
            case "OPTIONS":
                new OptionsTaskHandler().handle(httpExchange);
                break;
            case "POST":
                try {
                    new PostTaskHandler().handle(httpExchange, service);
                } catch (UnathorizedAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "GET":
                if(httpExchange.getRequestURI().getPath().equals("/todo/task")) {
                    new GetTasksHandler().handle(httpExchange, service);
                } else {
                    new GetChosenTaskHandler().handle(httpExchange, service);
                }
                break;
            case "PUT":
                new PutTaskHandler().handle(httpExchange, service);
                break;
            case "DELETE":
                new DeleteTaskHandler().handle(httpExchange, service);
                break;
        }
    }
}
