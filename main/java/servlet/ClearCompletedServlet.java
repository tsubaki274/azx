package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.TodoService;

@WebServlet("/todo/clear")
public class ClearCompletedServlet extends HttpServlet {

    private final TodoService todoService = new TodoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String user = SessionUtils.requireLogin(req, res);
        if (user == null) {
            return;
        }

        todoService.clearCompleted(user);

        res.sendRedirect(req.getContextPath() + "/todo" + todoService.buildFilterQuery(req.getParameter("filter")));
    }
}
