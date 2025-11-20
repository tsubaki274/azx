<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${ctx}/css/login.css">
  <title>ログイン</title>
</head>
<body>
<div class="wrap">
  <h1>ログイン</h1>
  <c:if test="${not empty err}">
    <div class="error"><c:out value="${err}" /></div>
  </c:if>
  <form action="${ctx}/login" method="post">
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
  <div style="text-align:center;">
    <a class="register-link" href="${ctx}/register">新規登録はこちら</a>
  </div>
</div>
</body>
</html>
