<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${ctx}/css/register.css">
  <title>ユーザー登録</title>
</head>
<body>
<div class="wrap">
  <h1>ユーザー登録</h1>

  <c:if test="${not empty errors}">
    <div class="errors">
      <ul>
        <c:forEach var="err" items="${errors}">
          <li><c:out value="${err}" /></li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form action="${ctx}/register" method="post">
    <label>
      <span>ユーザーID</span>
      <input type="text" name="user_id" value="${userIdValue}" required>
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
      <a class="btn" href="${ctx}/login">ログインへ戻る</a>
    </div>
  </form>
</div>
</body>
</html>
