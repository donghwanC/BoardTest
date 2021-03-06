<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>

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
		
		// 회원가입 조건부
		$("#submit").on("click", function(){
			if($("#userId").val()==""){
				alert("아이디를 입력해주세요");
				$("#userId").focus();
				return false;
			}
			
			if($("#userPass").val()==""){
				alert("비밀번호 입력해주세요");
				$("#userPass").focus();
				return false;
			}
			
			if($("#userName").val()==""){
				alert("이름을 입력해주세요");
				$("#userName").focus();
				return false;
			}
		});		
	});
	
	function fn_idChk(){
		$.ajax({
			url: "/member/idChk",
			data: {"userId" : $("#userId").val()},
			type: "post",
			dataType: "json",
			success: function(data){
				if(data == 1){
					alert("중복된 아이디입니다");
				} else {
					$("#idChk").attr("value", "Y");
					alert("사용 가능한 아이디 입니다");
				}
			}
		});
	}
</script>
</head>
<body>
	<section id="container">
		<form action="/member/register" method="post">
			<div class="form-group has-feedback">
				<label class="control-label" for="userId">아이디</label>
				<input class="form-control" type="text" id="userId" name="userId"/>
				<button class="idChk" id="idChk" type="button" onclick="fn_idChk();" value="N">중복확인</button>
			</div>
			<div class="form-group has-feedback">
				<label class="control-label" for="userPass">패스워드</label>
				<input class="form-control" type="password" id="userPass" name="userPass"/>
			</div>
			<div class="form-group has-feedback">
				<label class="control-label" for="userName">성명</label>
				<input class="form-control" type="text" id="userName" name="userName"/>
			</div>
			<div class="form-group has-feedback">
				<button class="btn btn-success" type="submit" id="submit">회원가입</button>
				<button class="cancle btn btn-danger" type="button">취소</button>
			</div>
		</form>
	</section>
</body>
</html>