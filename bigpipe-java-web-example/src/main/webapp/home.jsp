<%@ taglib prefix="bigpipe" uri="WEB-INF/tld/bigpipe.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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