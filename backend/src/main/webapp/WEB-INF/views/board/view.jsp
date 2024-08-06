<!-- 선택한 글 보여주기 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<c:if test="${article eq null}">
	<script>
		alert("글이 삭제되었거나 부적절한 URL 접근입니다.");
		location.href = "${root}/article?action=list";
		</script>
</c:if>
<%@ include file="/WEB-INF/views/common/confirm.jsp"%>
<div class="row justify-content-center">
	<div class="col-lg-8 col-md-10 col-sm-12">
		<h2 class="my-3 py-3 shadow-sm bg-light text-center">
			<mark class="sky">글보기</mark>
		</h2>
	</div>
	<div class="col-lg-8 col-md-10 col-sm-12">
		<div class="row my-2">
			<h2 class="text-secondary px-5">${article.articleNo}.
				${article.subject}</h2>
		</div>
		<div class="row">
			<div class="col-md-8">
				<div class="clearfix align-content-center">
					<img class="avatar me-2 float-md-start bg-light p-2"
						src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg" />
					<p>
						<span class="fw-bold">${article.userId}</span> <br /> <span
							class="text-secondary fw-light"> ${article.registerTime}
							조회 : ${article.hit} </span>
					</p>
				</div>
			</div>
			<!-- <div class="col-md-4 align-self-center text-end">댓글 : 17</div> -->
			<div class="divider mb-3"></div>
			<div class="text-secondary">${article.content}</div>
			
			<!-- 글에 파일이 있다면 파일마다 보여주고, 다운로드를 표시 -->
			
			<c:if test="${!empty article.fileInfos}">
				<div class="mt-3">
					<ul>
						<c:forEach var="file" items="${article.fileInfos}">
							<li>${file.originalFile}<a href="#" class="filedown"
								sfolder="${file.saveFolder}" sfile="${file.saveFile}"
								ofile="${file.originalFile}">다운로드</a></li>
						</c:forEach>
					</ul>
				</div>
			</c:if>
			
			<div class="divider mt-3 mb-3"></div>
			<div class="d-flex justify-content-end">
				<button type="button" id="btn-list"
					class="btn btn-outline-primary mb-3">글목록</button>
				<c:if test="${userinfo.userId eq article.userId}">
					<button type="button" id="btn-mv-modify"
						class="btn btn-outline-success mb-3 ms-1">글수정</button>
					<form action="delete" method="post">
						<input type="hidden" name="articleno" value="${article.articleNo}" />
						<button type="submit" id="btn-delete"
							class="btn btn-outline-danger mb-3 ms-1">글삭제</button>
					</form>
				</c:if>
			</div>
		</div>
	</div>
</div>

<!-- 다운로드 폼 -->

<form id="downform" action="${root}/article/download">
	<input type="hidden" name="sfolder"> <input type="hidden"
		name="ofile"> <input type="hidden" name="sfile">
</form>

<script>
      document.querySelector("#btn-list").addEventListener("click", function () {
        location.href = "${root}/article/list?pgno=1&key=&word=";
      });
      document.querySelector("#btn-mv-modify").addEventListener("click", function () {
        alert("글수정하자!!!");
        location.href = "${root}/article/modify?articleno=${article.articleNo}";
      });
      document.querySelector("#btn-delete").addEventListener("click", function () {
        alert("글삭제하자!!!");
      });
      
      // 파일경로, 원래 파일명, 암호화된 파일명
      
      document.querySelectorAll(".filedown").forEach(function(file) {
    	  file.addEventListener("click", function(){
    		 document.querySelector("[name='sfolder']").value = file.getAttribute("sfolder");
    		 document.querySelector("[name='ofile']").value = file.getAttribute("ofile");
    		 document.querySelector("[name='sfile']").value = file.getAttribute("sfile");
    		 document.querySelector("#downform").submit();
    	  });
    	  
      });
      
    </script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
