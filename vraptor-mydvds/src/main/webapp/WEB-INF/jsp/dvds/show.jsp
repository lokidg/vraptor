<%@ include file="/header.jsp" %> 

<div class="blue-box">
<table>
	<tr><td align="right"><a href="<c:url value="/logout" />"><fmt:message key="logout"/></a></td></tr>
</table>
</div>

<div class="blue-box" id="message">
<h1>${dvd.title} <fmt:message key="dvd_added"/></h1>
<hr/>
<a href="<c:url value="/home" />"><fmt:message key="home"/></a>
</div>

<%@ include file="/footer.jsp" %> 