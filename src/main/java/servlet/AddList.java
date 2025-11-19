package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AddListLogic;
import model.Todo;

@WebServlet({ "/todo/add", "/AddList" })
public class AddList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final AddListLogic addListLogic = new AddListLogic();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = requireLogin(request, response);
		if (userId == null) {
			return;
		}
		LocalDate today = LocalDate.now();
		setFormAttributes(request, "", today.getYear(), today.getMonthValue(), today.getDayOfMonth(), "中",
				Collections.emptyList());
		request.getRequestDispatcher("/WEB-INF/jsp/addTodo.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = requireLogin(request, response);
		if (userId == null) {
			return;
		}
		request.setCharacterEncoding("UTF-8");
		String todoForm = request.getParameter("todo_form");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String day = request.getParameter("day");
		String priority = request.getParameter("priority");
		List<String> errors = addListLogic.createErrorList();
		Todo todo = addListLogic.validateAndBuild(userId, todoForm, year, month, day, priority, errors);
		if (todo == null) {
			int yearVal = safeParse(year, LocalDate.now().getYear());
			int monthVal = safeParse(month, LocalDate.now().getMonthValue());
			int dayVal = safeParse(day, LocalDate.now().getDayOfMonth());
			setFormAttributes(request, todoForm, yearVal, monthVal, dayVal, priority, errors);
			request.getRequestDispatcher("/WEB-INF/jsp/addTodo.jsp").forward(request, response);
			return;
		}
		addListLogic.register(todo);
		String target = response.encodeRedirectURL(request.getContextPath() + "/todo");
		response.sendRedirect(target);
	}

	private void setFormAttributes(HttpServletRequest request,
			String formValue,
			int year,
			int month,
			int day,
			String priority,
			List<String> errors) {
		LocalDate today = LocalDate.now();
		request.setAttribute("formValue", formValue == null ? "" : formValue);
		request.setAttribute("defaultYear", today.getYear());
		request.setAttribute("selectedYear", year);
		request.setAttribute("selectedMonth", month);
		request.setAttribute("selectedDay", day);
		request.setAttribute("priorityParam", normalizePriority(priority));
		request.setAttribute("errors", errors);
	}

	private String normalizePriority(String priority) {
		if (priority == null || priority.isBlank()) {
			return "中";
		}
		switch (priority.trim()) {
			case "1":
			case "2":
				return "弱";
			case "3":
				return "中";
			case "4":
			case "5":
				return "強";
			default:
				return priority.trim();
		}
	}

	private int safeParse(String value, int fallback) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return fallback;
		}
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
}
