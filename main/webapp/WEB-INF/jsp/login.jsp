<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String ctx = request.getContextPath();
  String err = (String) request.getAttribute("err");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログイン</title>
  <style>
    body {
      margin: 0;
      background: #f0f2f5;
      font-family: "Segoe UI", "Hiragino Kaku Gothic ProN", "Yu Gothic", sans-serif;
    }
    .wrap {
      max-width: 420px;
      margin: 80px auto;
      background: #fff;
      padding: 40px;
      border-radius: 14px;
      box-shadow: 0 20px 45px rgba(16, 38, 73, 0.12);
    }
    h1 {
      margin-top: 0;
      margin-bottom: 24px;
      font-size: 28px;
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
      padding: 12px;
      border: 1px solid #d1d9e6;
      border-radius: 8px;
      font-size: 16px;
    }
    button {
      width: 100%;
      border: none;
      border-radius: 8px;
      padding: 12px;
      background: #1d7dfa;
      color: #fff;
      font-size: 16px;
      cursor: pointer;
    }
    .error {
      color: #b8061b;
      margin-bottom: 20px;
      text-align: center;
    }
  </style>
</head>
<body>
<div class="wrap">
  <h1>ログイン</h1>
  <% if (err != null && !err.isEmpty()) { %>
    <div class="error"><%= err %></div>
  <% } %>
  <form action="<%= ctx %>/login" method="post">
    <label>
      <span>ユーザーID</span>
      <input type="text" name="user_id" required>
    </label>
    <label>
      <span>パスワード</span>
      <input type="password" name="password" required>
    </label>
    <button type="submit">ログイン</button>
  </form>
  <div style="margin-top:16px; text-align:center;">
    <a href="<%= ctx %>/register" style="color:#1d7dfa; text-decoration:none; font-weight:600;">新規登録はこちら</a>
  </div>
</div>
</body>
</html>
