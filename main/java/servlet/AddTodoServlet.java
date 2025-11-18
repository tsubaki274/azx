package servlet;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.TodoService;
import model.TodoService.AddTodoCommand;
import model.TodoService.AddTodoResult;

@WebServlet("/todo/add")
public class AddTodoServlet extends HttpServlet {

    private final TodoService todoService = new TodoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String user = SessionUtils.requireLogin(req, res);
        if (user == null) {
            return;
        }
        attachToday(req);
        req.getRequestDispatcher("/WEB-INF/jsp/addTodo.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String user = SessionUtils.requireLogin(req, res);
        if (user == null) {
            return;
        }

        AddTodoCommand command = new AddTodoCommand(
                req.getParameter("todo_form"),
                req.getParameter("year"),
                req.getParameter("month"),
                req.getParameter("day"),
                req.getParameter("priority"));

        AddTodoResult result = todoService.addTodo(user, command);

        if (!result.isSuccess()) {
            attachToday(req);
            req.setAttribute("errors", result.getErrors());
            req.setAttribute("todo_form", command.todoForm());
            req.setAttribute("priority", command.priority());
            req.setAttribute("year", command.year());
            req.setAttribute("month", command.month());
            req.setAttribute("day", command.day());
            req.getRequestDispatcher("/WEB-INF/jsp/addTodo.jsp").forward(req, res);
            return;
        }

        res.sendRedirect(req.getContextPath() + "/todo");
    }

    private void attachToday(HttpServletRequest req) {
        LocalDate today = LocalDate.now();
        req.setAttribute("todayYear", today.getYear());
        req.setAttribute("todayMonth", today.getMonthValue());
        req.setAttribute("todayDay", today.getDayOfMonth());
    }
}
