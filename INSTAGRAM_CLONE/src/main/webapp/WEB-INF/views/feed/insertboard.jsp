<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>피드 작성</title>
<!-- START :: HEADER FORM -->
	<%@ include file="../form/header.jsp"%>
<!-- END :: HEADER FORM -->
</head>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>
	<h1>글작성</h1>
   <form action="/feed/insertFeed" method="post" enctype="multipart/form-data">
	      	<input type="hidden" name="member_code" value="${sessionLoginMember.member_code } ">
	 		<input type="file" name="mpfile">
	 		<textarea name="board_content" id="editor1" rows="10" cols="80"></textarea>
			<input type="submit" value="등록">
   </form>
   <br>
   
   <div id="progressbar" style="border:0px; height:10px; width:500px;"></div>
   
	<script>
	$(function() {
		var progressbar = $("#progressbar");
		var progressLabel = $(".progress-label");
		progressbar.progressbar({
			value: true,
			change: function() {
							progressLabel.text("Current Progress: " + progressbar.progressbar("value") + "%");
						},
			complete: function() {
								progressLabel.text("Complete!");
								$(".ui-dialog button").last().trigger("focus");
						}
		});
		$('form').ajaxForm({
			// 결로는 upload.html이다.
			url: "/feed/insertFeed",
			type: "POST",
			beforeSubmit: function(arr, $form, options) {
								progressbar.progressbar( "value", 0 );
							},
			uploadProgress: function(event, position, total, percentComplete) {
								progressbar.progressbar( "value", percentComplete );
							},
			success: function(text, status, xhr, element) {
								progressbar.progressbar( "value", 100 );
								alert("등록 성공");
								location.href="/member/profile?member_code=${sessionLoginMember.member_code}";
							},
			error: function(){
				alert("등록 실패");
			}
		});
	});
	</script>
	

   
</body>

</html>