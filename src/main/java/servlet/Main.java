package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ResultLogic;
import model.Todo;

@WebServlet({ "/todo", "/todo/done", "/todo/delete", "/todo/clear", "/Main" })
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ResultLogic resultLogic = new ResultLogic();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = requireLogin(request, response);
		if (userId == null) {
			return;
		}
		String filter = normalizeFilter(request.getParameter("filter"));
		List<Todo> todos = resultLogic.fetchTodos(userId, filter);
		request.setAttribute("todos", todos);
		request.setAttribute("filter", filter);
		request.setAttribute("allClass", cssClass(filter, "all"));
		request.setAttribute("activeClass", cssClass(filter, "active"));
		request.setAttribute("doneClass", cssClass(filter, "done"));
		request.setAttribute("dateFormat", new SimpleDateFormat("yyyy年MM月dd日"));
		request.getRequestDispatcher("/WEB-INF/jsp/todoList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = requireLogin(request, response);
		if (userId == null) {
			return;
		}
		request.setCharacterEncoding("UTF-8");
		String path = request.getServletPath();
		String filter = normalizeFilter(request.getParameter("filter"));
		try {
			switch (path) {
				case "/todo/done":
					updateDone(request, userId);
					break;
				case "/todo/delete":
					deleteTodo(request, userId);
					break;
				case "/todo/clear":
					resultLogic.clearDone(userId);
					break;
				default:
					break;
			}
		} catch (NumberFormatException e) {
			request.getSession().setAttribute("flashErr", "不正なパラメーターが送信されました。");
		}
		String target = response.encodeRedirectURL(request.getContextPath() + "/todo?filter=" + filter);
		response.sendRedirect(target);
	}

	private void updateDone(HttpServletRequest request, String userId) {
		int id = Integer.parseInt(request.getParameter("id"));
		boolean done = Boolean.parseBoolean(request.getParameter("done"));
		resultLogic.updateDone(userId, id, done);
	}

	private void deleteTodo(HttpServletRequest request, String userId) {
		int id = Integer.parseInt(request.getParameter("id"));
		resultLogic.delete(userId, id);
	}

	private String requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null) {
			String target = response.encodeRedirectURL(request.getContextPath() + "/login");
			response.sendRedirect(target);
			return null;
		}
		return (String) session.getAttribute("loginUser");
	}

	private String normalizeFilter(String filter) {
		if (filter == null) {
			return "all";
		}
		switch (filter) {
			case "active":
			case "done":
				return filter;
			default:
				return "all";
		}
	}

	private String cssClass(String current, String target) {
		return current.equals(target) ? "filter-btn active" : "filter-btn";
	}
}
