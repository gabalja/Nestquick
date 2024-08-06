<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/confirm.jsp" %>

<h1>마이 페이지</h1>

<h3>나의 정보</h3></br>

<ul>
	<!-- Member 컨트롤러에서 넘겨준 userinfo에 담긴 정보를 출력한다. -->
	<li>아이디 : ${userinfo.userId}</li>
	<li>이름 : ${userinfo.userName}</li>
	<li>이메일 : ${userinfo.emailId} @ ${userinfo.emailDomain}</li>
	<li>가입일 : ${userinfo.joinDate}</li>
</ul></br>

<h3>내가 쓴 게시글</h3></br>

<table class="table table-hover">
            <thead>
              <tr class="text-center">
                <th scope="col">이름</th>
                <th scope="col">제목</th>
                <th scope="col">작성자</th>
                <th scope="col">조회수</th>
                <th scope="col">작성일</th>
              </tr>
            </thead>
            <tbody>    
            	<!-- Member 컨트롤러에서 넘겨준 로그인한 사용자가 작성한 게시글 목록 articles를 출력한다. -->
            	<!-- article의 타입인 BoardDto에 선언된 get~함수를 property로 하여 값을 가져온다. -->
				<c:forEach var="article" items="${articles}">    
	              <tr class="text-center">
	                <th scope="row">${article.articleNo}</th>
	                <td class="text-start">
	                  <a
	                    href="#"
	                    class="article-title link-dark"
	                    data-no="${article.articleNo}"
	                    style="text-decoration: none"
	                  >
	                    ${article.subject}
	                  </a>
	                </td>
	                <td>${article.userId}</td>
	                <td>${article.hit}</td>
	                <td>${article.registerTime}</td>
	              </tr>            
				</c:forEach>   
            </tbody>
          </table>


<%@ include file="/WEB-INF/views/common/footer.jsp" %>