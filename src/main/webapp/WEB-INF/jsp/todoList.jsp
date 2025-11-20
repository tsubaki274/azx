<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${ctx}/css/todoList.css">
  <title>TODOリスト</title>
</head>
<body>
<div class="wrap">
  <header>
    <h1>TODOリスト</h1>
    <div class="hero-actions">
      <a class="btn btn-primary btn-add" href="${ctx}/todo/add">タスクを追加</a>
      <a class="btn logout-btn" href="${ctx}/login?action=logout">ログアウト</a>
    </div>
  </header>

  <div class="button-row">
    <div class="filters-cluster">
      <form class="filters-form" action="${ctx}/todo" method="get">
        <button type="submit" name="filter" value="all" class="btn btn-option ${isAllActive ? 'btn-filter-active' : ''}">すべて</button>
        <button type="submit" name="filter" value="active" class="btn btn-option ${isActiveActive ? 'btn-filter-active' : ''}">未完了</button>
        <button type="submit" name="filter" value="done" class="btn btn-option ${isDoneActive ? 'btn-filter-active' : ''}">完了</button>
      </form>
      <form class="clear-form" action="${ctx}/todo/clear" method="post">
        <input type="hidden" name="filter" value="${filter}">
        <button type="submit" class="btn btn-option btn-danger">完了を削除</button>
      </form>
    </div>
  </div>

  <c:if test="${not empty flashInfo}">
    <div class="info">${flashInfo}</div>
  </c:if>

  <c:if test="${not empty flashErr}">
    <div class="errors">${flashErr}</div>
  </c:if>

  <c:choose>
    <c:when test="${empty todos}">
      <div class="empty">まだタスクがありません</div>
    </c:when>
    <c:otherwise>
      <div class="todo-list">
        <c:forEach var="todo" items="${todos}">
          <div class="todo-card">
            <form class="toggle-form" action="${ctx}/todo/done" method="post">
              <input type="hidden" name="id" value="${todo.id}">
              <input type="hidden" name="filter" value="${filter}">
              <input type="hidden" name="done" class="done-value" value="${todo.done}">
              <input type="checkbox" class="toggle" ${todo.done ? 'checked="checked"' : ''}
                     aria-label="完了" onchange="submitToggle(this)">
            </form>
            <div class="todo-content">
              <div class="todo-title ${todo.done ? 'done' : ''}">${todo.todoForm}</div>
              <div class="todo-meta">
                期限:
                <c:choose>
                  <c:when test="${not empty todo.limitDay}">
                    <fmt:formatDate value="${todo.limitDay}" pattern="${datePattern}" />
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
                / 優先度: ${todo.priority}
              </div>
            </div>
            <form class="delete-form" action="${ctx}/todo/delete" method="post">
              <input type="hidden" name="id" value="${todo.id}">
              <input type="hidden" name="filter" value="${filter}">
              <button type="submit" class="icon-btn" aria-label="削除">×</button>
            </form>
          </div>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
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
