<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
  <c:if test="${not empty userinfo}">
	<div class="row justify-content-center">
	  <div class="col-lg-8 col-md-10 col-sm-12 m-3 text-end">
		<a href="${root}/user/mypage"><strong>${userinfo.userName}</strong> (${userinfo.userId})님 안녕하세요.</a>
		<a href="${root}/user/logout">로그아웃</a><br />
	  </div>
	</div>
  </c:if>
 <%--
 	interceptor로 확인!
 	
  <c:if test="${empty userinfo}">
	<script>
	alert("로그인이 필요한 페이지입니다.");
	location.href = "${root}/user/login";
	</script>
  </c:if> --%>