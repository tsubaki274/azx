package servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginLogic;

@WebServlet({ "/register", "/Register" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final LoginLogic loginLogic = new LoginLogic();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		forwardForm(request, response, null, Collections.emptyList());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("user_id");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("password_confirm");

		List<String> errors = loginLogic.validateRegistration(userId, password, passwordConfirm);
		if (!errors.isEmpty()) {
			forwardForm(request, response, userId, errors);
			return;
		}

		try {
			if (!loginLogic.register(userId, password)) {
				errors.add("登録に失敗しました。もう一度お試しください。");
				forwardForm(request, response, userId, errors);
				return;
			}
		} catch (RuntimeException e) {
			errors.add("サーバー内でエラーが発生しました。");
			forwardForm(request, response, userId, errors);
			return;
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("loginUser", userId.trim());
		session.setAttribute("flashInfo", "ユーザー登録が完了しました。");
		String target = response.encodeRedirectURL(request.getContextPath() + "/todo");
		response.sendRedirect(target);
	}

	private void forwardForm(HttpServletRequest request,
			HttpServletResponse response,
			String userIdValue,
			List<String> errors) throws ServletException, IOException {
		request.setAttribute("userIdValue", userIdValue == null ? "" : userIdValue);
		request.setAttribute("errors", errors);
		request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
	}
}
