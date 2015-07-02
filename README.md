[![Build Status](https://travis-ci.org/crazysoftwarecoder/bigpipe.svg?branch=master)](https://travis-ci.org/crazysoftwarecoder/bigpipe)

## A BigPipe implementation for Java
 
A java implementation of [bigpipe](https://www.facebook.com/notes/facebook-engineering/bigpipe-pipelining-web-pages-for-high-performance/389414033919) technology developed originally at Facebook.

## Architecture

![Imgur](http://i.imgur.com/OkKZ0iO.jpg)

The flow of a request goes like this:

1. The client browser sends a HTTP request.
2. A BigPipeDispatcherServlet(Front Controller) receives the request, and starts multiple tasks (also called as Pagelet Tasks).
3. The Pagelet tasks start fetching data for different pagelets from the EIS/Internet data sources.
4. Immediately after 2, the JSP to churn out presentation markup (HTML) is also cranked up.
5. The JSP receives all the pagelet data asynchronously and flushes out HTML markup over the wire, along with javascript code to paint placeholders on the page. During this time, the server keeps the HTTP connection open.
6. The client receives the javascript code + content (HTML, CSS + more javascript) and paints placeholders on the page appropriately.
7. The server finally closes the HTTP connection.

**Note** - The execution of the JSP is still blocking (i.e. the first pagelet on the page will still block - waiting for its data to come back, and then it will go to the second pagelet, and so on). This will be changed to asynchronous execution in the very near future (Submit a patch if you'd like to contribute). However, it is good to note that such a restriction will give a pleasant user experience to visitors (as they see the header loading first, then the middle components, and then the footer in a top down fashion).

### Prerequisites

* You are running a JSP/Servlet container (Apache Tomcat)
* You are running atleast JDK 7

## Installation

```
git clone https://github.com/crazysoftwarecoder/bigpipe.git
cd bigpipe
mvn clean install
```

Import maven dependency in your pom:

```
<dependency>
  <groupId>org.myseriousorganization.bigpipe</groupId>
  <artifactId>bigpipe-java-web</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Usage

### Step 1

Define your pagelet task methods (APIs that provide data to independent pagelets)

If ClassA.doStuff will serve a pagelet's data, then do the following

```java
@PageletTask(name="leftNavigationBar") // Lets assume this serves the left navigation bar of a website.
public ClassA {

  /**
   * Annotate the method that will fetch the data for the pagelet.
   * This method will be called in a separate thread so that it
   * can be parallelized.
   *
   * The ONLY argument to this method must be a <b>HttpServletRequest</b>
   * Remember for the developer who is using this, this is the 
   * Servlet and the response is the <b>ViewObject</b>
   */
  @PageletTaskMethod // Annotate the method that will fetch the data for you.
  public ViewObject doStuff(HttpServletRequest servletRequest) {
    // Type code to transform the request into the 
    // response here, optionally accessing all resources to
    // help achieve that.
  }
}
```

###Step 2

Define the return ViewObject for each @PageletTaskMethod

```java
class LeftNavBarDataVO implements ViewObject { // ViewObject is just a marker interface. It does not have anything.

  // Add your properties and getter and setter methods here that return
  // data for the pagelet to the JSP

  private String categoryName;
  
  public String getCategoryName() {
    return categoryName;
  }
  
  public void setCategoryName(String categoryName) {
  
  }
}
```

###Step 3

In your JSP

```jsp
<%@ taglib prefix="bigpipe" uri="http://www.github.com/crazysoftwarecoder/bigpipe"%>
<%@ page isELIgnored="false" %>

<html>
<head></head>
<body>
	<bigpipe:enablePagelets /> <!-- Enable bigpipe tech for this JSP here -->

	<bigpipe:pagelet name="leftModule" viewObject="item"> <!-- The name is the one in @PageletTask and the viewObject is the variable name that you want to use in the pagelet to get at the data object -->
		${item.categoryName} <!-- Get at the property of the ViewObject -->
	</bigpipe:pagelet>

  <!-- A second pagelet -->
	<bigpipe:pagelet name="rightModule" viewObject="item">
		${item.moduleName}
	</bigpipe:pagelet>

  <!-- Add more pagelets here -->

</body>
</html>
```

###Step 4

Then finally in your web.xml
```xml
	<servlet>
		<servlet-name>HomePageServingServlet</servlet-name>
		<servlet-class>com.myseriousorganization.bigpipe.core.servlet.BigPipeDispatcherServlet</servlet-class>
		<init-param>
			<param-name>jsp-file</param-name> <!-- The JSP file to forward to after the @PageletTasks are run -->
			<param-value>/home.jsp</param-value>
		</init-param>
		<init-param>
			<param-name>pageletTaskClasses</param-name> <!-- The FQCN of the @PageletTaskClasses -->
			<param-value>com.myseriousorganization.bigpipe.example.LeftModuleDisplayTask,com.myseriousorganization.bigpipe.example.RightModuleDisplayTask</param-value>
		</init-param>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>HomePageServingServlet</servlet-name>
		<url-pattern><url-that-you-want-serve></url-pattern> <!-- URL you want to serve -->
	</servlet-mapping>
```

###Step 5
You are all set! Go to **http://localhost:8080/app-context/url-that-you-want-serve/** and see the results.

## Example
For an example, look [here](https://github.com/crazysoftwarecoder/bigpipe/tree/master/bigpipe-java-web-example)

###License
[MIT](https://github.com/strongloop/express/blob/master/LICENSE)
