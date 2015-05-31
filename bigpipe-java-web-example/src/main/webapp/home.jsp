<%@ taglib prefix="bigpipe" uri="http://www.github.com/crazysoftwarecoder/bigpipe"%>
<%@ page isELIgnored="false" %>

<html>
<head></head>
<body>
	<bigpipe:enablePagelets />

	<bigpipe:pagelet name="leftModule" viewObject="item">
		${item.message}
	</bigpipe:pagelet>

	<bigpipe:pagelet name="rightModule" viewObject="item">
		${item.moduleName}
	</bigpipe:pagelet>

</body>
</html>