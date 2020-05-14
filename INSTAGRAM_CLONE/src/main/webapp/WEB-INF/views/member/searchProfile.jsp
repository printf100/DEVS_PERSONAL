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
</head>
<body>
	
	<section class="container w-75">
		<div id="header">
			<div class="row">
				
				<!-- 멤버 프로필 이미지 -->
				<div class="col-sm-4">
					<form id="imageForm" action="/member/updateprofileimage" method="POST" enctype="multipart/form-data">
						<input type="hidden" name="member_code" value="${profile.member_code }">
						
						<div class="rounded-circle border w-150 h-150 overflow-hidden mx-auto">						
							<img 
								id="profile_image" 
								class="w-150 h-150 bg-white cursor-pointer"
								src="<c:choose>
										 <c:when test="${not empty profile.member_img_server_name}">
										 	/resources/images/profileupload/${profile.member_img_server_name }
										 </c:when>
										 <c:otherwise>
										 	/resources/images/profile/add.png
										 </c:otherwise>
									 </c:choose>"
							>
							
						</div>
						
					</form>
				</div>
				
				<!-- 계정관련 -->
				<div class="col-sm-8">
					<div class="d-flex mb-2">
						<div class="my-auto mx-1">
							<h3>${profile.member_id }</h3>
						</div>
						<div class="my-auto mx-1">
							<button type="button" class="" onclick="#">팔로우</button>
						</div>
					</div>
					<div class="mb-2">
						<ul class="navbar-nav list-group-horizontal">
							<li class="nav-item mr-5">게시물 : ${profile.board_code }</li>
							<li class="nav-item mr-5">팔로워 : ${profile.member_code }</li>
							<li class="nav-item mr-5">팔로우 : ${profile.channel_code }</li>
							<li class="nav-item mr-5">성별  : ${profile.member_gender }</li>
						</ul>
					</div>
					<div class="mb-2">
						<div>
							${profile.member_name }
						</div>
						<div>
							${profile.member_introduce }
						</div>
						<div>
							<h3><a href="${profile.member_website }">${profile.member_website } 웹 사이트</a></h3>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div id="contant" class="h-150">
		
		</div>
	</section>
</body>
</html>