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
	<input type="hidden" name="lang">
	<input type="hidden" name="comment">
	<button id="btn" type="submit">search</button>
</form>

<div class="langCon">
	<h3>언어</h3>
	<input type="checkbox" value="java">자바
	<input type="checkbox" value="py">파이썬
	<input type="checkbox" value="sql">SQL
</div>

<div class="commentCon">
	<h3>커멘트</h3>
	<input type="checkbox" value="all">전체
</div>

<script
  src="https://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>

<script>
$(document).ready(function() {
	
	$langCon = $(".langCon");
	$commentCon = $(".commentCon");
	
	$('#btn').on("click", function(e) {
		
		//e.preventDefault();
		
		var langArr = [];
		
		var langVal = null;

		$('.langCon input[type=checkbox]:checked').each(function() {
			
			langArr.push($(this).val());
			
			langVal = langArr.toString().replace(","," ");
			
			$("input[name=lang]").val(langVal);
			
		});
		
		console.log(langVal);
		
		
		if($(".commentCon input").is(":checked") == true) {
			
			$("input[name=comment]").val($(".commentCon input").val());
			
		} else {
			$("input[name=comment]").val("comment");
		}
		
	});
	
})
</script>

</body>
</html>