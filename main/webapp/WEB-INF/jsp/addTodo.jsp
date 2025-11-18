<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%! private int parseOrDefault(String value, int fallback) {
    try {
      return Integer.parseInt(value);
    } catch (Exception e) {
      return fallback;
    }
  }
%>
<%
  String ctx = request.getContextPath();
  @SuppressWarnings("unchecked")
  List<String> errors = (List<String>) request.getAttribute("errors");
  Integer todayYear = (Integer) request.getAttribute("todayYear");
  Integer todayMonth = (Integer) request.getAttribute("todayMonth");
  Integer todayDay = (Integer) request.getAttribute("todayDay");
  LocalDate now = LocalDate.now();
  int defaultYear = todayYear != null ? todayYear : now.getYear();
  int defaultMonth = todayMonth != null ? todayMonth : now.getMonthValue();
  int defaultDay = todayDay != null ? todayDay : now.getDayOfMonth();

  String formValue = request.getParameter("todo_form");
  if (formValue == null) {
    formValue = (String) request.getAttribute("todo_form");
  }
  if (formValue == null) {
    formValue = "";
  }
  String yearParam = request.getParameter("year");
  if (yearParam == null) {
    yearParam = (String) request.getAttribute("year");
  }
  String monthParam = request.getParameter("month");
  if (monthParam == null) {
    monthParam = (String) request.getAttribute("month");
  }
  String dayParam = request.getParameter("day");
  if (dayParam == null) {
    dayParam = (String) request.getAttribute("day");
  }
  String priorityParam = request.getParameter("priority");
  if (priorityParam == null) {
    priorityParam = (String) request.getAttribute("priority");
  }
  if (priorityParam == null || priorityParam.isBlank()) {
    priorityParam = "3";
  }

  int selectedYear = parseOrDefault(yearParam, defaultYear);
  int selectedMonth = parseOrDefault(monthParam, defaultMonth);
  int selectedDay = parseOrDefault(dayParam, defaultDay);
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>TODOリスト追加</title>
  <style>
    body {
      margin: 0;
      background: #f6f7fb;
      font-family: "Segoe UI", "Hiragino Kaku Gothic ProN", "Yu Gothic", sans-serif;
    }
    .wrap {
      max-width: 720px;
      margin: 0 auto;
      padding: 40px 20px 80px;
    }
    header {
      font-size: 32px;
      font-weight: 600;
      margin-bottom: 24px;
    }
    form {
      background: #fff;
      padding: 32px;
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(15, 32, 54, 0.1);
      display: flex;
      flex-direction: column;
      gap: 24px;
    }
    label span,
    .field-title {
      display: block;
      font-weight: 600;
      margin-bottom: 6px;
    }
    input[type="text"],
    select {
      width: 100%;
      padding: 12px 14px;
      border: 1px solid #d3d9e4;
      border-radius: 8px;
      font-size: 16px;
    }
    .date-fields {
      display: flex;
      gap: 12px;
    }
    .date-field {
      flex: 1;
    }
    .actions {
      display: flex;
      gap: 16px;
      flex-wrap: wrap;
    }
    .primary-btn,
    .ghost-btn {
      border-radius: 8px;
      padding: 12px 24px;
      font-size: 16px;
      cursor: pointer;
      border: none;
    }
    .primary-btn {
      background: #1d7dfa;
      color: #fff;
    }
    .ghost-btn {
      background: transparent;
      border: 1px solid #c7c9d3;
    }
    .errors {
      background: #ffe8e8;
      color: #b71021;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 20px;
    }
    .errors li {
      margin-left: 20px;
    }
    a.ghost-btn {
      text-decoration: none;
      display: inline-flex;
      align-items: center;
    }
  </style>
</head>
<body>
<div class="wrap">
  <header>TODOリスト追加</header>

  <% if (errors != null && !errors.isEmpty()) { %>
    <div class="errors">
      <ul>
        <% for (String err : errors) { %>
          <li><%= err %></li>
        <% } %>
      </ul>
    </div>
  <% } %>

  <form action="<%= ctx %>/todo/add" method="post">
    <label>
      <span>タスク名</span>
      <input type="text" name="todo_form" value="<%= formValue %>" required>
    </label>

    <div>
      <span class="field-title">期限</span>
      <div class="date-fields">
        <div class="date-field">
          <select name="year">
            <% for (int y = defaultYear; y <= defaultYear + 5; y++) { %>
              <option value="<%= y %>" <%= y == selectedYear ? "selected" : "" %>><%= y %>年</option>
            <% } %>
          </select>
        </div>
        <div class="date-field">
          <select name="month">
            <% for (int m = 1; m <= 12; m++) { %>
              <option value="<%= m %>" <%= m == selectedMonth ? "selected" : "" %>><%= m %>月</option>
            <% } %>
          </select>
        </div>
        <div class="date-field">
          <select name="day">
            <% for (int d = 1; d <= 31; d++) { %>
              <option value="<%= d %>" <%= d == selectedDay ? "selected" : "" %>><%= d %>日</option>
            <% } %>
          </select>
        </div>
      </div>
    </div>

    <label>
      <span>重要度 (1〜5)</span>
      <input type="text" name="priority" value="<%= priorityParam %>" required>
    </label>

    <div class="actions">
      <button type="submit" class="primary-btn">タスクを追加</button>
      <a class="ghost-btn" href="<%= ctx %>/todo">一覧に戻る</a>
    </div>
  </form>
</div>
</body>
</html>
