<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>검색</title>
</head>
<body>
<script
  src="https://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>
parse
<form>
	<input type="text" name="keyword" value="${keyword}">
	<button type="submit">search</button>
</form>

<ul>
	<c:forEach var="item" items="${list}">
		<li>${item }</li>
	</c:forEach>
</ul>

</body>
</html>