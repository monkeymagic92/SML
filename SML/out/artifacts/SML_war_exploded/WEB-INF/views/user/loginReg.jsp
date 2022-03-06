<%--
    날짜 : 2022-02-28 / 월요일 / 오전 12:23
    JSP : loginReg
    유저 : LJY
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<title>loginReg</title>
	<link href="/res/css/loginReg/loginReg.css" rel="stylesheet">
	<!-- footer 아이콘 (나중에 구글아이콘으로 변경하기) -->
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css"
		  integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf"
		  crossorigin="anonymous">

	<script type="text/javascript" src="/res/js/common.js"></script>

	<!-- jquery 3.6.0 -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<!-- jquery ajax form -->
	<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js" integrity="sha384-qlmct0AOBiA2VPZkMY3+2WqkHtIQ9lSdAsAn5RUJD/3vA5MKDgSGcdmIv4ycVxyn" crossorigin="anonymous"></script>
	<!-- jquery 모달 UI -->
	<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
</head>
<body style="overflow:hidden">
<script type="text/javascript">

	$(document).ready(function() {
		$("#signUp").click(function() {
			$("#container").addClass('right-panel-active');
		});

		$("#signIn").click(function() {
			$("#container").removeClass('right-panel-active');
		});

		$("#LOGIN_ID").focus();

		// 로그인 실패시 alert메세지
		if(${message != null}) {
			alert('${message}');
			$("#LOGIN_ID").focus();
		}
	});

	// 회원가입
	function fnSignUp() {

		let flag = fnSignUpChk();

		if(flag) {
			$("#signUpFrm").ajaxSubmit({
				dataType: 'json'
				, url: "/user/signUp"
				, success: function(data) {

					if(data.message == 'failId') {
						alert('ID가 중복되었습니다.');
						return;

					} else if(data.message == 'failEmail') {
						alert('Email이 중복되었습니다.');
						return;

					} else {
						alert('회원가입되었습니다');
						$("#USER_ID").val('');
						$("#USER_PW").val('');
						$("#USER_PW2").val('');
						$("#USER_EMAIL").val('');
						$("#errorConf").text('');
						$("#signIn").trigger('click');
					}
				}
			});
		}
	}

	function fnSignUpChk() {
		var user_id = $("#USER_ID").val();
		var user_pw = $("#USER_PW").val();
		var user_pw2 = $("#USER_PW2").val();
		var user_email = $("#USER_EMAIL").val();

		let flag = true;

		if(user_id.length == 0 || user_pw.length == 0 || user_pw2.length == 0 || user_email.length == 0) {
			$("#errorConf").text("입력되지않은 항목이 있습니다");
			flag = false;
		}

		if(user_id.length < 4) {
			$("#USER_ID").focus();
			$("#errorConf").text("아이디는 최소 4자 이상 입니다.");
			flag = false;
		}

		if(user_id.length > 0) {
			const regExpId = /^[0-9a-z]+$/;
			if(!regExpId.test(user_id)) {
				$("#USER_ID").focus();
				$("#errorConf").text("아이디는 영문자, 숫자만 가능합니다");
				flag = false;
			}
		}

		if(user_pw != user_pw2) {
			$("#USER_PW").focus();
			$("#errorConf").text("두 비밀번호를 다시 확인해 주세요.");
			flag = false;
		}

		const pass = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/
		if( !pass.test(user_pw) ) {
			$("#USER_PW").focus();
			$("#errorConf").text("비밀번호는 특수문자 숫자를 포함한 8자 이상입니다.");
			flag = false;
		}

		if (user_email.length > 0) {
			const emailReg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i
			if(!emailReg.test(user_email)) {
				$("#USER_EMAIL").focus();
				$("#errorConf").text("올바른 이메일을 입력해 주세요");
				flag = false;
			}
		}
		return flag;
	}

	// 로그인 submit 체크 (onsubmit = '')
	function login() {
		let loginId = $("#LOGIN_ID").val();
		let loginPw = $("#LOGIN_PW").val();

		if (loginId.length == 0 || loginPw.length == 0) {
			alert('ID 또는 Password를 입력해 주세요.');
			return false;
		}
	}

</script>
<div class="container" id="container">

	<!-- 회원가입 form -->
	<div class="form-container sign-up-container">
		<form id="signUpFrm" name="signUpFrm" method="post">
			<h1>SIGN UP</h1>
			<div class="social-container">
				<a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
				<a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
				<a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
			</div>
			<span>or use your email for registration</span>
			<input id="USER_ID" name="USER_ID" type="text" placeholder="ID" />
			<input id="USER_PW" name="USER_PW" type="password" placeholder="Password" />
			<input id="USER_PW2" name="USER_PW2" type="password" placeholder="Password confirm" />
			<input id="USER_EMAIL" name="USER_EMAIL" type="email" placeholder="Email" />
			<div id="errorConf" style="color: red; font-size:0.9em; margin-bottom: 5px;"></div>
			<button type="button" id="btnSignUp" name="btnSignUp" onclick="fnSignUp();" style="cursor: pointer;">Sign Up</button>
		</form>
	</div>

	<!-- 로그인 form -->
	<div class="form-container sign-in-container">
		<form id="signInFrm" name="signInFrm" action="/user/signIn" method="post" onsubmit="return login()">
			<h1>SIGN IN</h1>
			<div class="social-container">
				<a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
				<a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
				<a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
			</div>
			<span>or use your account</span>
			<input id="LOGIN_ID" name="LOGIN_ID" type="text" placeholder="ID" />
			<input id="LOGIN_PW" name="LOGIN_PW" type="password" placeholder="Password" />
			<a href="#">Forgot your password?</a>
			<button type="submit" id="btnSignIn" name="btnSignIn" style="cursor: pointer;">Sign In</button>
		</form>
	</div>

	<!-- 사이드 박스 -->
	<div class="overlay-container">
		<div class="overlay">
			<!-- 회원가입 form에서 보이는 사이드 박스 -->
			<div class="overlay-panel overlay-left">
				<h1>Welcome Back!</h1>
				<p>To keep connected with us please login with your personal info</p>
				<button class="ghost" id="signIn" style="cursor: pointer;">Sign In</button>
			</div>

			<!-- 로그인 form에서 보이는 사이드 박스 -->
			<div class="overlay-panel overlay-right">
				<h1>Hello, Friend!</h1>
				<p>Enter your personal details and start journey with us</p>
				<button class="ghost" id="signUp" style="cursor: pointer;">Sign Up</button>
			</div>
		</div>
	</div>

</div>

<div class="footer">
	<b style="color: grey;">	Follow me on </b>
	<div class="icons">
		<a href="https://github.com/kvaibhav01" target="_blank" class="social" style="color: grey;"><i class="fab fa-github"></i></a>
		<a href="https://www.instagram.com/vaibhavkhulbe143/" target="_blank" class="social"><i class="fab fa-instagram"></i></a>
		<a href="https://medium.com/@vaibhavkhulbe" target="_blank" class="social"><i class="fab fa-medium"></i></a>
		<a href="https://twitter.com/vaibhav_khulbe" target="_blank" class="social"><i class="fab fa-twitter-square"></i></a>
		<a href="https://linkedin.com/in/vaibhav-khulbe/" target="_blank" class="social"><i class="fab fa-linkedin"></i></a>
	</div>
</div>

</body>
</html>