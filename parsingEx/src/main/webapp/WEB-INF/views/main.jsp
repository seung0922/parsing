<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>메인화면</h1>

<form id="f1" method="POST">
	<input type="text" name="keyword">
	<input type="text" name="lang">
	<button id="btn" type="submit">search</button>
</form>

<input type="checkbox" value="java">자바
<input type="checkbox" value="py">파이썬
<input type="checkbox" value="sql">SQL

<script
  src="https://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>

<script>
$(document).ready(function() {
	
	$('#btn').on("click", function(e) {
		
		e.preventDefault();
		
		var ch = [];

		$('input[type=checkbox]:checked').each(function() {
			
			ch.push($(this).val());
			
			$("input[name=lang]").val(ch);
			
		});
		
		console.log(ch);
		
		$("#f1").submit();
		
	});
	
})
</script>

</body>
</html>