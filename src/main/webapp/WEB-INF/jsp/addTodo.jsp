<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${ctx}/css/addTodo.css">
  <title>TODOリスト追加</title>
</head>
<body>
<div class="wrap">
  <header>TODOリスト追加</header>

  <c:if test="${not empty errors}">
    <div class="errors">
      <ul>
        <c:forEach var="err" items="${errors}">
          <li><c:out value="${err}" /></li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form action="${ctx}/todo/add" method="post">
    <label>
      <span>タスク名</span>
      <input type="text" name="todo_form" value="${formValue}" required>
    </label>

    <div>
      <span class="field-title">期限</span>
      <div class="date-fields">
        <div class="date-field">
          <select name="year">
            <c:forEach var="y" begin="${defaultYear}" end="${defaultYear + 5}">
              <option value="${y}" <c:if test="${y == selectedYear}">selected</c:if>>${y}年</option>
            </c:forEach>
          </select>
        </div>
        <div class="date-field">
          <select name="month">
            <c:forEach var="m" begin="1" end="12">
              <option value="${m}" <c:if test="${m == selectedMonth}">selected</c:if>>${m}月</option>
            </c:forEach>
          </select>
        </div>
        <div class="date-field">
          <select name="day">
            <c:forEach var="d" begin="1" end="31">
              <option value="${d}" <c:if test="${d == selectedDay}">selected</c:if>>${d}日</option>
            </c:forEach>
          </select>
        </div>
      </div>
    </div>

    <label>
      <span>優先度</span>
      <select name="priority" required>
        <option value="弱" <c:if test="${priorityParam eq '弱'}">selected</c:if>>弱</option>
        <option value="中" <c:if test="${priorityParam eq '中'}">selected</c:if>>中</option>
        <option value="強" <c:if test="${priorityParam eq '強'}">selected</c:if>>強</option>
      </select>
    </label>

    <div class="actions">
      <button type="submit" class="primary-btn">タスクを追加</button>
      <a class="ghost-btn" href="${ctx}/todo">一覧に戻る</a>
    </div>
  </form>
</div>
</body>
</html>
