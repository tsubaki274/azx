<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Todo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
  String ctx = request.getContextPath();
  List<Todo> todos = (List<Todo>) request.getAttribute("todos");
  if (todos == null) {
    todos = java.util.Collections.emptyList();
  }
  String filter = (String) request.getAttribute("filter");
  if (filter == null) filter = "all";
  String allClass = (String) request.getAttribute("allClass");
  if (allClass == null) allClass = "filter-btn";
  String activeClass = (String) request.getAttribute("activeClass");
  if (activeClass == null) activeClass = "filter-btn";
  String doneClass = (String) request.getAttribute("doneClass");
  if (doneClass == null) doneClass = "filter-btn";
  boolean isAllActive = allClass.contains("active");
  boolean isActiveActive = activeClass.contains("active");
  boolean isDoneActive = doneClass.contains("active");
  SimpleDateFormat dateFormat = (SimpleDateFormat) request.getAttribute("dateFormat");
  if (dateFormat == null) {
    dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
  }
  String flashErr = (String) session.getAttribute("flashErr");
  if (flashErr != null) {
    session.removeAttribute("flashErr");
  }
  String flashInfo = (String) session.getAttribute("flashInfo");
  if (flashInfo != null) {
    session.removeAttribute("flashInfo");
  }
%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>TODOリスト</title>
  <style>
    :root {
      color-scheme: light;
      font-family: "Segoe UI", "Hiragino Kaku Gothic ProN", "Yu Gothic", sans-serif;
    }
    body {
      margin: 0;
      background: #f2f4fb;
      color: #1a1d2a;
    }
    .wrap {
      max-width: 920px;
      margin: 48px auto 120px;
      padding: 0 32px;
    }
    header {
      display: flex;
      align-items: flex-start;
      gap: 16px;
    }
    header h1 {
      font-size: 40px;
      font-weight: 700;
      margin: 0;
    }
    .hero-actions {
      margin-left: auto;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-end;
      gap: 16px;
    }
    .btn-add {
      min-width: 204px;
      padding: 0 32px;
      height: 52px;
      font-size: 17px;
    }
    .logout-btn {
      padding: 0 24px;
      border: 1px solid transparent;
      border-radius: 10px;
      background: none;
      color: #2560ff;
      font-size: 16px;
      font-weight: 600;
      box-shadow: none;
      height: 52px;
      display: inline-flex;
      align-items: center;
      text-decoration: none;
    }
    .btn {
      border-radius: 12px;
      border: 1px solid #dde3f2;
      background: #fff;
      padding: 0 30px;
      height: 52px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      color: #2a2f3d;
      box-shadow: 0 6px 16px rgba(30, 60, 120, 0.08);
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      transition: background .15s ease, color .15s ease, border-color .15s ease;
    }
    .btn-primary {
      background: #1c6bff;
      color: #fff;
      border-color: #1c6bff;
      box-shadow: 0 10px 24px rgba(28, 107, 255, 0.25);
    }
    .btn-option {
      background: #fff;
      color: #1f2534;
    }
    .btn-filter-active {
      background: #1c6bff;
      border-color: #1c6bff;
      color: #fff;
    }
    .btn-danger {
      color: #ef4565;
      border-color: #ef4565;
      box-shadow: none;
    }
    .btn-danger:hover {
      background: rgba(239, 69, 101, 0.08);
    }
    .button-row {
      margin-top: 28px;
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
      align-items: center;
    }
    .filters-cluster {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
      align-items: center;
    }
    .filters-form {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
      margin: 0;
    }
    .clear-form {
      margin: 0;
    }
    .empty {
      margin-top: 40px;
      text-align: center;
      padding: 80px 20px;
      color: #8c93a9;
      background: rgba(255,255,255,0.8);
      border-radius: 24px;
      border: 1px dashed #c7d0e8;
    }
    .errors {
      border-radius: 12px;
      padding: 16px 18px;
      margin: 28px 0 16px;
      font-size: 16px;
    }
    .info {
      background: #eaf2ff;
      color: #1c4fa7;
    }
    .errors {
      background: #ffe5e7;
      color: #b20e27;
    }
    .todo-list {
      margin-top: 24px;
      display: flex;
      flex-direction: column;
      gap: 16px;
      align-items: stretch;
    }
    .todo-card {
      display: flex;
      align-items: center;
      gap: 20px;
      padding: 20px 40px;
      width: 100%;
      background: #fff;
      border-radius: 22px;
      box-shadow: 0 18px 45px rgba(18, 32, 74, 0.08);
    }
    .toggle-form {
      margin: 0;
      display: flex;
      align-items: center;
    }
    .toggle {
      width: 22px;
      height: 22px;
      border-radius: 8px;
      border: 2px solid #cfd5e6;
      background: #fff;
      cursor: pointer;
      appearance: none;
      -webkit-appearance: none;
      transition: background .15s ease, border-color .15s ease;
      display: grid;
      place-items: center;
    }
    .toggle:checked {
      background: #1c6bff;
      border-color: #1c6bff;
    }
    .toggle:checked::after {
      content: "";
      width: 6px;
      height: 12px;
      border: solid #fff;
      border-width: 0 2px 2px 0;
      transform: rotate(45deg);
    }
    .todo-content {
      flex: 1;
    }
    .todo-title {
      font-size: 20px;
      font-weight: 600;
      margin-bottom: 6px;
      color: #1b1f2a;
    }
    .todo-title.done {
      color: #a5acbe;
      text-decoration: line-through;
    }
    .todo-meta {
      color: #7f869b;
      font-size: 14px;
    }
    .delete-form {
      margin: 0;
    }
    .icon-btn {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      border: 1px solid #d6dbea;
      background: #fff;
      font-size: 18px;
      font-weight: 600;
      cursor: pointer;
      text-align: center;
      color: #8c93a9;
      transition: background .15s ease, color .15s ease, border-color .15s ease;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }
    .icon-btn:hover {
      border-color: #ef4565;
      color: #ef4565;
      background: #fff5f7;
    }
  </style>
</head>
<body>
<div class="wrap">
  <header>
    <h1>TODOリスト</h1>
    <div class="hero-actions">
      <a class="btn btn-primary btn-add" href="<%= ctx %>/todo/add">タスクを追加</a>
      <a class="btn logout-btn" href="<%= ctx %>/login?action=logout">ログアウト</a>
    </div>
  </header>

  <div class="button-row">
    <div class="filters-cluster">
      <form class="filters-form" action="<%= ctx %>/todo" method="get">
        <button type="submit" name="filter" value="all" class="btn btn-option <%= isAllActive ? "btn-filter-active" : "" %>">すべて</button>
        <button type="submit" name="filter" value="active" class="btn btn-option <%= isActiveActive ? "btn-filter-active" : "" %>">未完了</button>
        <button type="submit" name="filter" value="done" class="btn btn-option <%= isDoneActive ? "btn-filter-active" : "" %>">完了</button>
      </form>
      <form class="clear-form" action="<%= ctx %>/todo/clear" method="post">
        <input type="hidden" name="filter" value="<%= filter %>">
        <button type="submit" class="btn btn-option btn-danger">完了を削除</button>
      </form>
    </div>
  </div>

  <% if (flashInfo != null) { %>
    <div class="info">
      <%= flashInfo %>
    </div>
  <% } %>

  <% if (flashErr != null) { %>
    <div class="errors">
      <%= flashErr %>
    </div>
  <% } %>

  <% if (todos == null || todos.isEmpty()) { %>
    <div class="empty">まだタスクがありません</div>
  <% } else { %>
    <div class="todo-list">
      <% for (Todo t : todos) { %>
        <div class="todo-card">
          <form class="toggle-form" action="<%= ctx %>/todo/done" method="post">
            <input type="hidden" name="id" value="<%= t.getId() %>">
            <input type="hidden" name="filter" value="<%= filter %>">
            <input type="hidden" name="done" class="done-value" value="<%= t.isDone() %>">
            <input type="checkbox" class="toggle" <%= t.isDone() ? "checked" : "" %>
                   aria-label="完了" onchange="submitToggle(this)">
          </form>
          <div class="todo-content">
            <div class="todo-title <%= t.isDone() ? "done" : "" %>"><%= t.getTodoForm() %></div>
            <div class="todo-meta">期限: <%= t.getLimitDay() != null ? dateFormat.format(t.getLimitDay()) : "-" %> / 優先度: <%= t.getPriority() %></div>
          </div>
          <form class="delete-form" action="<%= ctx %>/todo/delete" method="post">
            <input type="hidden" name="id" value="<%= t.getId() %>">
            <input type="hidden" name="filter" value="<%= filter %>">
            <button type="submit" class="icon-btn" aria-label="削除">×</button>
          </form>
        </div>
      <% } %>
    </div>
  <% } %>
</div>

<script>
  function submitToggle(el) {
    const form = el.closest('form');
    const hidden = form.querySelector('.done-value');
    hidden.value = el.checked;
    form.submit();
  }
</script>
</body>
</html>
