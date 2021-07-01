<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 방법1 --%>
<%-- <%response.sendRedirect(application.getContextPath() + "/home");%> --%>

<%-- 방법2 --%>
<c:redirect url="/home"/>