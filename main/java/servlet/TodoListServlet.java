package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.TodoService;
import model.TodoService.TodoPage;

@WebServlet("/todo")
public class TodoListServlet extends HttpServlet {

    private final TodoService todoService = new TodoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String user = SessionUtils.requireLogin(req, res);
        if (user == null) {
            return;
        }

        TodoPage page = todoService.loadTodos(user, req.getParameter("filter"));
        req.setAttribute("todos", page.todos());
        req.setAttribute("filter", page.filter());
        req.getRequestDispatcher("/WEB-INF/jsp/todoList.jsp").forward(req, res);
    }
}
