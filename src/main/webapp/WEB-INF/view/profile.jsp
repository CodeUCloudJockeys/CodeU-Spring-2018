<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Profile" %>

<%
Profile profile = (Profile) request.getAttribute("about");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>UserProfile</title>
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/profile">Profile</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a></a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>
  
  <h1>This is Exuser's Page</h1>
 <div id="container">

    <% String username = (String) request.getAttribute("username");
    	if(request.getSession().getAttribute("user") != null && username.equals("exuser")){ %>
      <h1>About Me!</h1>
            
      <form>
      	Edit Text:<br>
          <input type = "text" about = "about">
          <br>        
        <button type="submit">Update</button>
      </form>

      <hr/>
    <% } %>

  </div>
  
  <p>Where I am from</p>
</body>
</html>
