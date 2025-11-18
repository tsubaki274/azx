<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Todo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
  @SuppressWarnings("unchecked")
  List<Todo> todos = (List<Todo>) request.getAttribute("todos");
  String filter = (String) request.getAttribute("filter");
  if (filter == null) {
    filter = "all";
  }
  String ctx = request.getContextPath();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  String allClass = "filter-btn" + ("all".equals(filter) ? " active" : "");
  String activeClass = "filter-btn" + ("active".equals(filter) ? " active" : "");
  String doneClass = "filter-btn" + ("done".equals(filter) ? " active" : "");
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
      background: #f6f7fb;
      color: #222;
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
    .toolbar {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      align-items: center;
      margin-bottom: 24px;
    }
    .primary-btn,
    .filter-btn,
    .danger-btn {
      border: 1px solid #c7c9d3;
      background: #fff;
      border-radius: 6px;
      padding: 10px 20px;
      font-size: 15px;
      cursor: pointer;
      transition: all .15s ease;
    }
    .primary-btn {
      background: #1d7dfa;
      border-color: #1d7dfa;
      color: #fff;
    }
    .filter-btn.active {
      background: #1d7dfa;
      color: #fff;
      border-color: #1d7dfa;
    }
    .danger-btn {
      border-color: #ff7272;
      color: #ff4d4d;
    }
    .danger-btn:hover {
      background: #ff4d4d;
      color: #fff;
    }
    .todo-card {
      background: #fff;
      border-radius: 12px;
      padding: 18px 20px;
      display: flex;
      align-items: center;
      gap: 16px;
      box-shadow: 0 8px 18px rgba(20, 37, 63, 0.08);
    }
    .todo-list {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .todo-title {
      font-size: 18px;
      font-weight: 500;
    }
    .todo-meta {
      color: #666;
      font-size: 13px;
    }
    .todo-card form {
      margin: 0;
    }
    .toggle-form {
      display: inline-flex;
      align-items: center;
    }
    .todo-actions {
      margin-left: auto;
      display: flex;
      gap: 8px;
    }
    .icon-btn {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      border: 1px solid #d1d5db;
      background: #fff;
      font-size: 18px;
      line-height: 1;
      cursor: pointer;
    }
    .icon-btn:hover {
      background: #ffe4e4;
      border-color: #ff7272;
      color: #ff4d4d;
    }
    .toggle {
      width: 20px;
      height: 20px;
      accent-color: #1d7dfa;
      cursor: pointer;
    }
    .empty {
      text-align: center;
      padding: 80px 20px;
      color: #777;
      background: #fff;
      border-radius: 12px;
      box-shadow: inset 0 0 0 1px #eceff5;
    }
    .filters-form,
    .clear-form {
      display: inline-flex;
      gap: 10px;
    }
    a.primary-btn {
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }
  </style>
</head>
<body>
<div class="wrap">
  <header>TODOリスト</header>

  <div class="toolbar">
    <a class="primary-btn" href="<%= ctx %>/todo/add">タスクを追加</a>

    <form class="filters-form" action="<%= ctx %>/todo" method="get">
      <button type="submit" name="filter" value="all" class="<%= allClass %>">すべて</button>
      <button type="submit" name="filter" value="active" class="<%= activeClass %>">未完了</button>
      <button type="submit" name="filter" value="done" class="<%= doneClass %>">完了</button>
    </form>

    <form class="clear-form" action="<%= ctx %>/todo/clear" method="post">
      <input type="hidden" name="filter" value="<%= filter %>">
      <button type="submit" class="danger-btn">完了を削除</button>
    </form>
  </div>

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
          <div>
            <div class="todo-title">
              <%= t.getTodoForm() %>
            </div>
            <div class="todo-meta">
              <span>期限: <%= t.getLimitDay() != null ? dateFormat.format(t.getLimitDay()) : "-" %></span>
              <span> / 重要度: <%= t.getPriority() %></span>
            </div>
          </div>
          <div class="todo-actions">
            <form action="<%= ctx %>/todo/delete" method="post">
              <input type="hidden" name="id" value="<%= t.getId() %>">
              <input type="hidden" name="filter" value="<%= filter %>">
              <button type="submit" class="icon-btn" aria-label="削除">×</button>
            </form>
          </div>
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
