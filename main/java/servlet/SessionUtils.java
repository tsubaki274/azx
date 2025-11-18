package servlet;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public final class SessionUtils {

    private SessionUtils() {
    }

    public static String requireLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        String user = (String) session.getAttribute("user");
        if (user == null || user.isBlank()) {
            res.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return user;
    }
}
