<%@ taglib prefix="bigpipe" uri="http://www.github.com/crazysoftwarecoder/bigpipe"%>
<%@ page isELIgnored="false" %>

<html>
<head></head>
<body>
	<b>Instructions</b>
	<br/>
	Call this JSP like this:= http://localhost:8080/bigpipe/home?left-module-param=left&right-module-param=right.

	<br/>
	You will see that the left param's value is shown in the leftModule's pagelet and similarly for the right module.
	<br/>
	
	<br/>
	<br/>

	<bigpipe:enablePagelets />

	<bigpipe:pagelet name="leftModule" viewObject="item">
		${item.message}
	</bigpipe:pagelet>

	<bigpipe:pagelet name="rightModule" viewObject="item">
		${item.moduleName}
	</bigpipe:pagelet>

</body>
</html>