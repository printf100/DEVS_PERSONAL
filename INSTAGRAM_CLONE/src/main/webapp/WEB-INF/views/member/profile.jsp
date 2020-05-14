<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Insert title here</title>
<!-- START :: HEADER FORM -->
	<%@ include file="../form/header.jsp"%>
<!-- END :: HEADER FORM -->

<!-- START :: board -->
	<script type="text/javascript">
		$(document).ready(function(){
			
		})
	
		$(function(){
			
			$("#profile_image").click(function(){
				$("#member_profile_image_name").click();
			})
			
			$("#member_profile_image_name").change(function(e){
				var form = $("#imageForm")[0];
				var formData = new FormData(form);
				
				$.ajax({
					type: "POST",
					enctype: "multipart/form-data",
					url: "/member/updateprofileimage",
					processData: false,
					contentType: false,
					data: formData,
					dataType: "JSON",
					success: function(msg){
						$("#profile_image").attr("src","/resources/images/profileupload/" + msg.img);
					},
					error : function() {
						alert("통신 실패");
					}
				})				
			})
			
		})
		
		
		
	</script>
<!-- END :: board -->
	
<style type="text/css">
.thumbnail{
	position: relative;
	width: 300px;
	height: 300px;
	overflow: hidden;
	margin: 10px;
}

.fit{
	position: absolute;
	width: 100%;
	height: 100%;
	object-fit : cover;
}
.play-button{
	position: absolute;
	top:100px;
	left:100px;
	width:100px;
	height:100px; 
}
.bg-modal{
	width:100%;
	height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
	position: absolute;
	top:0;
	left:0;
	display:flex;
    justify-content: center;
    align-items: center;
    display:none;
}
.modal-box{
	width: 60%;
	height: 80%;
	background-color: white;
	border-radius: 5px;
	position: relative;
}

.close{
	position: absolute;
	top : 0;
	right: 5px;
	font-size: 40px;
	transform: rotate(45deg);
	cursor: pointer;
}

</style>
	
</head>



<body>
	
	<section class="container w-75">
		<div id="header">
			<div class="row">
				
				<!-- 멤버 프로필 이미지 -->
				<div class="col-sm-4">
					<form id="imageForm" action="/member/updateprofileimage" method="POST" enctype="multipart/form-data">
						<input type="hidden" name="member_code" value="${sessionLoginMember.member_code }">
						
						<div class="rounded-circle border w-150 h-150 overflow-hidden mx-auto">						
							<img 
								id="profile_image" 
								class="w-150 h-150 bg-white cursor-pointer"
								src="<c:choose>
										 <c:when test="${not empty sessionLoginMemberProfile.member_img_server_name}">
										 	/resources/images/profileupload/${sessionLoginMemberProfile.member_img_server_name }
										 </c:when>
										 <c:otherwise>
										 	/resources/images/profile/add.png
										 </c:otherwise>
									 </c:choose>"
							>
							<input id="member_profile_image_name" type="file" name="member_img_original_name" value="${sessionLoginMemberProfile.member_img_original_name }">					
							
						</div>

					</form>
				</div>
				
				<!-- 계정관련 -->
				<div class="col-sm-8">
					<div class="d-flex mb-2">
						<div class="my-auto mx-1">
							<h3>${sessionLoginMember.member_id }</h3>
						</div>
						<div class="my-auto mx-1">
							<button type="button" class="" onclick="location.href='/member/profileEdit'">프로필 편집</button>
						</div>
						<div class="my-auto mx-1">수정
							<h3><a href="/member/profileEdit"><i class="fas fa-cog"></i></a></h3>
						</div>
						<div class="my-auto mx-1">
							<h3><a href=""><i class="fas fa-cog"></i></a></h3>
						</div>
					</div>
					<div class="mb-2">
						<ul class="navbar-nav list-group-horizontal">
							<li class="nav-item mr-5">게시물 ?</li>
							<li class="nav-item mr-5">팔로워 ?</li>
							<li class="nav-item mr-5">팔로우 ?</li>
							<li class="nav-item mr-5">성별  ?</li>
						</ul>
					</div>
					<div class="mb-2">
						<div>
							${sessionLoginMember.member_name }
						</div>
						<div>
							${sessionLoginMemberProfile.member_introduce }
						</div>
						<div>
							<h3>test<a href="${sessionLoginMemberProfile.member_website }">${sessionLoginMemberProfile.member_website }</a></h3>
						</div>
					</div>
					<div>
						<input type="button" value="글작성" onclick="location.href='/feed/insertpage?member_code=${sessionLoginMember.member_code }'">
					</div>
				</div>
			</div>
		</div>
	
		<div id="contant" class="h-150">
			<div class="row">
				
				
				<c:forEach var="list" items="${feedList }">
					<div class="contents">
						<c:choose>
							
							<c:when test="${list.board_file_ext eq 'mp4'}">
		                     <div class="thumbnail" id="${list.board_code}">
		                        <c:set var="board_file_server_name" value="${list.board_file_server_name }"></c:set>
		                        <img class="fit" src="/resources/images/feedupload/${fn:substringBefore(board_file_server_name, '.')}.jpg">
		                        <img class="play-button" src="/resources/images/feedupload/playbutton.png">
		                     </div>
		                    
							</c:when>
							<c:otherwise>
								<div class="thumbnail" id="${list.board_code}">
								<c:set var="file_name" value="${list.board_file_server_name }"></c:set>
								<img class="fit" src="/resources/images/feedupload/${file_name }">
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</c:forEach>
	
	
			
				<!-- 모달창 -->
				<div class="bg-modal">
					<div class="modal-box">
						<div class="close">+</div>
						<div class="content-left">
							<img class="modal-content" src="">
						</div>
						<div class="content-right"></div>
					</div>
				</div>
				
			</div>

 		</div>		
	</section>
	
	
	
	
	<script type="text/javascript">
		$(".thumbnail").click(function() {
			var board_num = $(this).attr('id');
			
			$(".bg-modal").css("display", "flex");
			
			$.ajax({
				type:"GET",
				url:"/feed/selectContent",
				data:{"board_num": board_num},
				success: function(data){
					console.log("data");
				}
			});
			
		})
		
		$(".close").click(function(){
			$(".bg-modal").css("display", "none");
		})
		
	</script>
	
</body>
</html>