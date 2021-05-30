<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
	 	
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		// 취소
		$(".cancle").on("click", function(){
			location.href = "/";
		});
		
		// 수정
		$("#submit").on("click", function(){
			if($("#userPass").val() == ""){
				alert("비밀번호 입력해주세요.");
				$("#userPass").focus();
				return false;
			}
			if($("#userName").val() == ""){
				alert("성명을 입력해주세요.");
				$("#userName").focus();
				return false;
			}
			
			
			$.ajax({
				url: "/member/passChk",
				data: $("#updateForm").serializeArray(),
				type: "post",
				dataType: "json",
				success: function(data){

					if(data == true){
						if(confirm("수정하시겠습니까?")){
							$("#updateForm").submit();
						}
					}else{
						alert("비밀번호가 틀렸습니다");
						return;
					}
				}
			});
		});
		
		// 탈퇴
		$(".memberDelete").on("click", function(){
			location.href = "/member/memberDeleteView";
		});
	});
</script>
</head>
<body>
	<section id="container">
		<form id="updateForm" action="/member/memberUpdate" method="post">
			<div class="form-group has-feedback">
				<label class="control-label" for="userId">아이디</label>
				<input class="form-control" type="text" id="userId" name="userId" value="${member.userId }" readonly="readonly"/>
			</div>
			<div class="form-group has-feedback">
				<label class="control-label" for="userPass">패스워드</label>
				<input class="form-control" type="password" id="userPass" name="userPass"/>
				<label class="control-label" for="newPass">변경할 패스워드</label>
				<input class="form-control" type="password" id="newPass" name="newPass"/>
			</div>
			<div class="form-group has-feedback">
				<label class="control-label" for="userName">성명</label>
				<input class="form-control" type="text" id="userName" name="userName" value="${member.userName }"/>
			</div>			
		</form>
		<div class="form-group has-feedback">
			<button class="btn btn-success" type="button" id="submit">수정</button>
			<button class="cancle btn btn-danger" type="button">취소</button>
		</div>
		<div class="form-group has-feedback">
			<button class="memberDelete btn btn-danger" type="button">회원탈퇴</button>
		</div>
	</section>
</body>
</html>