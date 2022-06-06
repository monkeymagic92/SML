<%--
  Created by IntelliJ IDEA.
  User: LJY
  Date: 2022-03-15
  Time: 오후 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>Title</title>
	<link href="/res/css/carculater/carculater.css" rel="stylesheet">
	<style>

	</style>
</head>
<body>
	<div class="carculater">
		<form id="frm" name="frm" action="#">
			<span id="oriTitle">기존평단</span>
			<br>
			<input type="text" id="oriQuote" name="oriQuote" placeholder="기존매수시세" onkeyup="inputNumberFormat(this)"/>
			<br>
			<input type="text" id="oriCash" name="oriCash" placeholder="기존매수금액" onkeyup="inputNumberFormat(this)"/>
			<br><br>
			<span id="newTitle">물타기</span>
			<br>
			<input type="text" id="newQuote" name="newQuote" placeholder="신규매수시세" onkeyup="inputNumberFormat(this)"/>
			<br>
			<input type="text" id="newCash" name="newCash" placeholder="신규매수금액" onkeyup="inputNumberFormat(this)" onkeydown="enter()"/>
			<br>
			<!-- button 디자인 codepen에서 가져오기 -->
			<div class="btnBox">
				<button type="button" id="btnChk" name="btnChk">계산</button>
				<br>
				<button type="button" id="btnClear" name="btnClear" onclick="fnClear();">지우기(Shift + a)</button>
			</div>

			<div class="resultBox">
				<div>평단 : <span id="result"></span></div>
			</div>
		</form>
	</div>

<script>

	// shift + A키를 누를경우 전체지우기
	$(document).bind('keypress', function(event) {
		if( event.which === 65 && event.shiftKey ) {
			fnClear();
		}
	});

	$(document).ready(function() {

		$("#btnChk").click(function() {

			/*
				- 수량구하는 공식 : (매수금액) / (매수시세)
				- 평단구하는 공식 : ((기존매수시세 * 수량) + (신규매수시세 * 수량)) / (전체수량)
			*/

			// 입력받은값 가져오기
			let oriQuote =$("#oriQuote").val().replace(',', '');    // 기존매수 시세
			let oriCash = $("#oriCash").val().replace(',', '');      // 기존매수 금액
			let newQuote = $("#newQuote").val().replace(',', '');    // 신규매수 시세
			let newCash = $("#newCash").val().replace(',', '');      // 신규매수 금액

			// 입력받은값을 정수형태로 변환
			let oriQuote2 = parseInt(oriQuote);
			let oriCash2 = parseInt(oriCash);
			let newQuote2 = parseInt(newQuote);
			let newCash2 = parseInt(newCash);

			// 기존.신규 수량
			let oriNum = oriCash2 / oriQuote2;    // 기존 매수수량
			let newNum = newCash2 / newQuote2;    // 신규 매수수량

			//           ((기존매수시세 + 수량) + (신규매수시세 + 수량)) / (기존수량 + 신규수량)
			let result = ((oriQuote2 * oriNum) + (newQuote2 * newNum)) / (oriNum + newNum);

			let resultLocale = Math.round(result);

			// 결과
			$("#result").text(resultLocale.toLocaleString('ko-KR'));
		});
	});

	function enter() {
		if(event.keyCode == 13){
			$("#btnChk").trigger('click');
		}
	}

	/********* input태그 천단위 콤마 Start *********/
	function inputNumberFormat(obj) {
		obj.value = comma(uncomma(obj.value));
	}

	function comma(str) {
		str = String(str);
		return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
	}

	function uncomma(str) {
		str = String(str);
		return str.replace(/[^\d]+/g, '');
	}
	/********* input태그 천단위 콤마 end **********/

	// 전체지우기
	function fnClear() {
		$("#oriQuote").val('');
		$("#oriCash").val('');
		$("#newQuote").val('');
		$("#newCash").val('');
		$("#result").text('');
		$("#oriQuote").focus();
	}
</script>
</body>
</html>
