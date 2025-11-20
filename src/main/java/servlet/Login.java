package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.LoginLogic;

@WebServlet({ "/login" })
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final LoginLogic loginLogic = new LoginLogic();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String action = request.getParameter("action");
		if ("logout".equals(action) && session != null) {
			session.invalidate();
			session = null;
		}
		if (session != null && session.getAttribute("loginUser") != null) {
			String target = response.encodeRedirectURL(request.getContextPath() + "/todo");
			response.sendRedirect(target);
			return;
		}
		request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("user_id");
		String password = request.getParameter("password");
		if (loginLogic.authenticate(userId, password)) {
			HttpSession session = request.getSession(true);
			session.setAttribute("loginUser", userId.trim());
			String target = response.encodeRedirectURL(request.getContextPath() + "/todo");
			response.sendRedirect(target);
			return;
		}
		request.setAttribute("err", "ユーザーIDまたはパスワードが違います。");
		request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	}
}
