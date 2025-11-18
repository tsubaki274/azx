package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.UserService;

@WebServlet(urlPatterns = { "/", "/login" })
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("user_id");
        String pass = req.getParameter("password");

        boolean ok = userService.authenticate(id, pass);

        if (ok) {
            HttpSession session = req.getSession();
            session.setAttribute("user", id);
            res.sendRedirect(req.getContextPath() + "/todo");
        } else {
            req.setAttribute("err", "ログイン失敗");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
        }
    }
}
