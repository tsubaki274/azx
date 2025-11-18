package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.UserService;
import model.UserService.RegisterResult;

@WebServlet(urlPatterns = { "/register", "/register/" })
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        RegisterResult result = userService.register(
                req.getParameter("user_id"),
                req.getParameter("password"),
                req.getParameter("password_confirm"));

        if (!result.isSuccess()) {
            req.setAttribute("errors", result.getErrors());
            req.setAttribute("user_id", result.getUserId());
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", result.getUserId());
        res.sendRedirect(req.getContextPath() + "/todo");
    }
}
