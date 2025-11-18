package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.TodoService;

@WebServlet("/todo/delete")
public class DeleteTodoServlet extends HttpServlet {

    private final TodoService todoService = new TodoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String user = SessionUtils.requireLogin(req, res);
        if (user == null) {
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            todoService.deleteTodo(user, id);
        } catch (NumberFormatException e) {
            // ignore invalid id
        }

        res.sendRedirect(req.getContextPath() + "/todo" + todoService.buildFilterQuery(req.getParameter("filter")));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }
}
