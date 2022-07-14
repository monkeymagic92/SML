<%--
    날짜 : 2022-02-27 / 일요일 / 오전 10:43
    JSP : indexTemp
    유저 : LJY
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<head>
	<title>SML</title>

	<%-- js / css공통함수cdn jsp파일 --%>
	<%@ include file="../../common/commonUtils.jsp" %>

</head>
<body style="background-color: #212529;">


<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark" style="height: 80px;">
	<!-- Navbar Brand-->
	<a class="navbar-brand ps-3" href="/temp/index" style="font-size: 2em;">SML</a>
	<!-- Sidebar Toggle-->
	<button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>
	<div style="display: inline-block;">
		<a href="https://www.tradingview.com/chart/?symbol=NASDAQ%3AAAPL" target="_blank">TradingView</a>
		<a href="https://upbit.com/exchange?code=CRIX.UPBIT.KRW-BTC" target="_blank" style="margin-left: 5%;">Upbit</a>
		<%-- PUMP페이지를 만듬으로 인해 주석처리 --%>
<%--		<div style="color: red;">--%>
<%--			<c:forEach var="list" items="${dayRankList}">--%>
<%--				<div>${list.KOR_NM}</div>--%>
<%--				<div>${list.RISE_PRICE}</div>--%>
<%--				<div>${list.TRADE_DATE_CHAR}</div>--%>
<%--			</c:forEach>--%>
<%--		</div>--%>
	</div>
	<!-- Navbar Search-->
	<form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
		<div class="input-group">
			<input class="form-control" type="text" placeholder="Search for..." aria-label="Search for..." aria-describedby="btnNavbarSearch" />
			<button class="btn btn-primary" id="btnNavbarSearch" type="button"><i class="fas fa-search"></i></button>
		</div>
	</form>
	<!-- Navbar-->
	<ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
			<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
				<li><a id="moveToMyPage" class="dropdown-item" href="#!">My Page</a></li>
				<li><a id="btn-open-popup" class="dropdown-item">AVG Carculater</a></li>
				<li><a class="dropdown-item" href="#!">Activity Log</a></li>
				<li><hr class="dropdown-divider" /></li>
				<li><a class="dropdown-item" href="/user/logout">Logout</a></li>
			</ul>
		</li>
	</ul>
</nav>
<div id="layoutSidenav">
	<div id="layoutSidenav_nav">
		<nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
			<div class="sb-sidenav-menu">
				<div class="nav">
					<div class="sb-sidenav-menu-heading">Core</div>
					<a class="nav-link" href="/temp/index">
						<div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
						Dashboard
					</a>
					<div class="sb-sidenav-menu-heading">Interface</div>
					<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
						<div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
						Quote
					<div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
					</a>
					<div class="collapse" id="collapseLayouts" aria-labelledby="headingOne" data-bs-parent="#sidenavAccordion">
						<nav class="sb-sidenav-menu-nested nav">
							<a class="nav-link" href="/quote/quote_race">Race (10:00am)</a>
							<a class="nav-link" href="/pump/pumpRaceIndex">Pump (Race)</a>
							<a class="nav-link" href="/quote/quote_day">Day (08:55am)</a>
							<a class="nav-link" href="/pump/pumpDayIndex">Pump (Day)</a>
						</nav>
					</div>
					<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapsePages" aria-expanded="false" aria-controls="collapsePages">
						<div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
						Pages
						<div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
					</a>
					<div class="collapse" id="collapsePages" aria-labelledby="headingTwo" data-bs-parent="#sidenavAccordion">
						<nav class="sb-sidenav-menu-nested nav accordion" id="sidenavAccordionPages">
							<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#pagesCollapseAuth" aria-expanded="false" aria-controls="pagesCollapseAuth">
								Authentication
								<div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
							</a>
							<div class="collapse" id="pagesCollapseAuth" aria-labelledby="headingOne" data-bs-parent="#sidenavAccordionPages">
								<nav class="sb-sidenav-menu-nested nav">
									<a class="nav-link" href="login.html">Login</a>
									<a class="nav-link" href="register.html">Register</a>
									<a class="nav-link" href="password.html">Forgot Password</a>
								</nav>
							</div>
							<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#pagesCollapseError" aria-expanded="false" aria-controls="pagesCollapseError">
								Error
								<div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
							</a>
							<div class="collapse" id="pagesCollapseError" aria-labelledby="headingOne" data-bs-parent="#sidenavAccordionPages">
								<nav class="sb-sidenav-menu-nested nav">
									<a class="nav-link" href="401.html">401 Page</a>
									<a class="nav-link" href="404.html">404 Page</a>
									<a class="nav-link" href="500.html">500 Page</a>
								</nav>
							</div>
						</nav>
					</div>
					<div class="sb-sidenav-menu-heading">Addons</div>
					<a class="nav-link" href="charts.html">
						<div class="sb-nav-link-icon"><i class="fas fa-chart-area"></i></div>
						Write
					</a>
					<a class="nav-link" href="tables.html">
						<div class="sb-nav-link-icon"><i class="fas fa-table"></i></div>
						Memo
					</a>
				</div>
			</div>
			<div class="sb-sidenav-footer">
				<div class="small">Logged in as:</div>
				Start Bootstrap
			</div>
		</nav>
	</div>
	<div id="layoutSidenav_content" style="background-color: rgb(22, 26, 30); border-radius: 15px;">
		<main>
			<!-- 섹터 -->
			<div class="container-fluid px-4">
				<h1 class="mt-4" style="color: white;">${title}</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active" style="color: #b9c0d3;">${subCntn}</li>
				</ol>
				<div class="card mb-4" style="background-color: rgb(22, 26, 30); color: white; border: 1px solid #333;">
				<div class="card-body">
					<!-- include area -->
					<p class="mb-0">
						<div class="card-body">
							<p class="mb-0">
								<jsp:include page="/WEB-INF/views/${view}.jsp"></jsp:include>
							</p>
						</div>
					</p>
				</div>
			</div>

			<!-- Top버튼 -->
			<span id="topBtn" class="TOP">
				<span class="material-icons" style="font-size: 70px; color: white;">file_upload</span>
			</span>

			<!-- 물타기계산기 모달 -->
			<div class="modal">
				<div class="modal_body">
					<span style="font-weight: bold; color: #333; font-size: 1.2em;">Carculater</span>
					<div>
						<jsp:include page="/WEB-INF/views/modal/Carculater.jsp"></jsp:include>
					</div>
				</div>
			</div>
			<div style="height: 100vh"></div>

<%--		<div class="card mb-4"><div class="card-body">When scrolling, the navigation stays at the top of the page. This is the end of the static navigation demo.</div></div>--%>
		</main>
		<footer class="py-4 bg-light mt-auto">
			<div class="container-fluid px-4">
				<div class="d-flex align-items-center justify-content-between small">
					<div class="text-muted">Copyright &copy; Your Website 2021</div>
					<div>
						<a href="#">Privacy Policy</a>
						&middot;
						<a href="#">Terms &amp; Conditions</a>
					</div>
				</div>
			</div>
		</footer>
	</div>
</div>

<script type="text/javascript">

	/**			Top 버튼 Start 		**/
	let Top = document.querySelector('#TopBtn');

	window.addEventListener('scroll', function(){
		if(this.scrollY > 100){
			Top.classList.add('on')
		}else{
			Top.classList.remove('on')
		}
	});
	Top.addEventListener('click', function(el){
		el.preventDefault()
		window.scrollTo({
			top: 0
		})
	});
	/**			Top 버튼 End 		**/


	/**         모달띄우기 Start      **/
	const body = document.querySelector('body');
	const modal = document.querySelector('.modal');
	const btnOpenPopup = document.querySelector('#btn-open-popup');

	btnOpenPopup.addEventListener('click', () => {
		modal.classList.toggle('show');

		if (modal.classList.contains('show')) {
			body.style.overflow = 'hidden';
		}
	});

	modal.addEventListener('click', (event) => {
		if (event.target === modal) {
			modal.classList.toggle('show');

			if (!modal.classList.contains('show')) {
				body.style.overflow = 'auto';
			}
		}
	});
	/**         모달띄우기 End      **/

</script>
</body>
</html>