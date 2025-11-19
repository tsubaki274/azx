<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
  String ctx = request.getContextPath();
  List<String> errors = (List<String>) request.getAttribute("errors");
  String userIdValue = (String) request.getAttribute("userIdValue");
  if (userIdValue == null) {
    userIdValue = "";
  }
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ユーザー登録</title>
  <style>
    body {
      margin: 0;
      background: #f0f2f5;
      font-family: "Segoe UI", "Hiragino Kaku Gothic ProN", "Yu Gothic", sans-serif;
    }
    .wrap {
      max-width: 560px;
      margin: 90px auto;
      background: #fff;
      padding: 64px 58px;
      border-radius: 18px;
      box-shadow: 0 25px 55px rgba(16, 38, 73, 0.14);
    }
    h1 {
      margin-top: 0;
      margin-bottom: 24px;
      font-size: 34px;
      text-align: center;
    }
    label {
      display: block;
      margin-bottom: 16px;
    }
    label span {
      display: block;
      margin-bottom: 6px;
      font-weight: 600;
    }
    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 16px;
      border: 1px solid #d1d9e6;
      border-radius: 10px;
      font-size: 18px;
    }
    .actions {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
    }
    button,
    a.btn {
      flex: 1;
      border: none;
      border-radius: 10px;
      padding: 14px;
      font-size: 18px;
      cursor: pointer;
      text-align: center;
      text-decoration: none;
    }
    button {
      background: #1d7dfa;
      color: #fff;
      box-shadow: 0 10px 20px rgba(29, 125, 250, 0.25);
    }
    a.btn {
      border: 1px solid #c7c9d3;
      color: #333;
      background: #fff;
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
  </style>
</head>
<body>
<div class="wrap">
  <h1>ユーザー登録</h1>

  <% if (errors != null && !errors.isEmpty()) { %>
    <div class="errors">
      <ul>
        <% for (String err : errors) { %>
          <li><%= err %></li>
        <% } %>
      </ul>
    </div>
  <% } %>

  <form action="<%= ctx %>/register" method="post">
    <label>
      <span>ユーザーID</span>
      <input type="text" name="user_id" value="<%= userIdValue %>" required>
    </label>
    <label>
      <span>パスワード</span>
      <input type="password" name="password" required>
    </label>
    <label>
      <span>パスワード（確認）</span>
      <input type="password" name="password_confirm" required>
    </label>
    <div class="actions">
      <button type="submit">登録する</button>
      <a class="btn" href="<%= ctx %>/login">ログインへ戻る</a>
    </div>
  </form>
</div>
</body>
</html>
