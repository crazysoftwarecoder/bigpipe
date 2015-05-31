## A BigPipe implementation for Java

A java implementation of [bigpipe](https://www.facebook.com/notes/facebook-engineering/bigpipe-pipelining-web-pages-for-high-performance/389414033919) technology developed originally at Facebook.

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

  @PageletTaskMethod // Annotate the method that will fetch the data for you.
  public ViewObject doStuff() {
    
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

	<bigpipe:pagelet name="leftModule" viewObject="item"> <!-- The name is the one in @PageletTask and the viewObject is the variable name that you want to use in the pagelet for the data object -->
		${item.message}
	</bigpipe:pagelet>

  <!-- A second pagelet -->
	<bigpipe:pagelet name="rightModule" viewObject="item">
		${item.moduleName}
	</bigpipe:pagelet>

  <!-- Add more pagelets here -->

</body>
</html>
```
