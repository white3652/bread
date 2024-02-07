<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<header>
	<img id="main_logo" src="${pageContext.request.contextPath}/resources/css/img/mainlogo.PNG" onclick="location.href='/myapp'">
            <img id="c1" src="${pageContext.request.contextPath}/resources/css/img/c1.png">
            <img id="c2" src="${pageContext.request.contextPath}/resources/css/img/c2.jpg">
            <img id="c3" src="${pageContext.request.contextPath}/resources/css/img/c3.jpg">
            <img id="c4" src="${pageContext.request.contextPath}/resources/css/img/c4.jpg">
            <img id="c5" src="${pageContext.request.contextPath}/resources/css/img/c5.png">
    <div id="header_list">
		<ul>
			<li><a href="${pageContext.request.contextPath}/search/searchBakery.do"><span>빵</span>집 찾기</a></li>
			<li><a href="${pageContext.request.contextPath}/map/map.do">&nbsp;&nbsp;<span>빵</span> 지도</a></li>
			<li><a href="${pageContext.request.contextPath}/board/notice.do">&nbsp;<span>공</span>지사항</a></li>
			<li><a href="${pageContext.request.contextPath}/info/info.do"><span>빵빵</span>한천안 소개</a></li>
		</ul>
	</div>
	<div id="line1"></div>
</header>