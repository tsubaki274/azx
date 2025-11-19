<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%
  String ctx = request.getContextPath();
  List<String> errors = (List<String>) request.getAttribute("errors");
  String formValue = (String) request.getAttribute("formValue");
  Integer defaultYear = (Integer) request.getAttribute("defaultYear");
  Integer selectedYear = (Integer) request.getAttribute("selectedYear");
  Integer selectedMonth = (Integer) request.getAttribute("selectedMonth");
  Integer selectedDay = (Integer) request.getAttribute("selectedDay");
  String priorityParam = (String) request.getAttribute("priorityParam");
  if (formValue == null) formValue = "";
  java.time.LocalDate today = java.time.LocalDate.now();
  if (defaultYear == null) defaultYear = today.getYear();
  if (selectedYear == null) selectedYear = today.getYear();
  if (selectedMonth == null) selectedMonth = today.getMonthValue();
  if (selectedDay == null) selectedDay = today.getDayOfMonth();
  if (priorityParam == null || priorityParam.isBlank()) {
    priorityParam = "中";
  } else if ("1".equals(priorityParam) || "2".equals(priorityParam)) {
    priorityParam = "弱";
  } else if ("3".equals(priorityParam)) {
    priorityParam = "中";
  } else if ("4".equals(priorityParam) || "5".equals(priorityParam)) {
    priorityParam = "強";
  }
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
      min-height: 100vh;
    }
    .wrap {
      max-width: 560px;
      margin: 0 auto;
      padding: 64px 20px 120px;
    }
    header {
      font-size: 34px;
      font-weight: 600;
      margin-bottom: 32px;
      text-align: center;
    }
    form {
      background: #fff;
      padding: 36px 32px;
      border-radius: 24px;
      box-shadow: 0 18px 42px rgba(15, 32, 54, 0.12);
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
      padding: 14px 16px;
      border: 1px solid #d3d9e4;
      border-radius: 12px;
      font-size: 17px;
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
      justify-content: center;
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
      <span>優先度</span>
      <select name="priority" required>
        <option value="弱" <%= "弱".equals(priorityParam) ? "selected" : "" %>>弱</option>
        <option value="中" <%= "中".equals(priorityParam) ? "selected" : "" %>>中</option>
        <option value="強" <%= "強".equals(priorityParam) ? "selected" : "" %>>強</option>
      </select>
    </label>

    <div class="actions">
      <button type="submit" class="primary-btn">タスクを追加</button>
      <a class="ghost-btn" href="<%= ctx %>/todo">一覧に戻る</a>
    </div>
  </form>
</div>
</body>
</html>
