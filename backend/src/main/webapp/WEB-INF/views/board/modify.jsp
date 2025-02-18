<!-- 글 수정 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/confirm.jsp"%>
<div class="row justify-content-center">
	<div class="col-lg-8 col-md-10 col-sm-12">
		<h2 class="my-3 py-3 shadow-sm bg-light text-center">
			<mark class="sky">글수정</mark>
		</h2>
	</div>
	<div class="col-lg-8 col-md-10 col-sm-12">
		<form id="form-modify" method="POST" action="">
			<input type="hidden" name="articleNo" value="${article.articleNo}">
			<div class="mb-3">
				<label for="subject" class="form-label">제목 : </label> <input
					type="text" class="form-control" id="subject" name="subject"
					value="${article.subject}" />
			</div>
			<div class="mb-3">
				<label for="content" class="form-label">내용 : </label>
				<textarea class="form-control" id="content" name="content" rows="7">${article.content}</textarea>
			</div>
			<div class="col-auto text-center">
				<button type="button" id="btn-modify"
					class="btn btn-outline-primary mb-3">글수정</button>
				<button type="button" id="btn-list"
					class="btn btn-outline-danger mb-3">목록으로이동...</button>
			</div>
		</form>
	</div>
</div>
<script>

	  // 제목, 내용을 입력하지 못하면 alert	
      document.querySelector("#btn-modify").addEventListener("click", function () {
        if (!document.querySelector("#subject").value) {
          alert("제목 입력!!");
          return;
        } else if (!document.querySelector("#content").value) {
          alert("내용 입력!!");
          return;
        } else {
          let form = document.querySelector("#form-modify");
          form.setAttribute("action", "${root}/article/modify");
          form.submit();
        }
      });
      
      // 수정이 끝나면 1페이지를 보여준다
      document.querySelector("#btn-list").addEventListener("click", function () {
        location.href = "${root}/article/list?pgno=1&key=&word=";
      });
    </script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
