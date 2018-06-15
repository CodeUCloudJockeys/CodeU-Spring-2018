<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Profile" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= request.getSession().getAttribute("user") %></title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@ include file="../reusables/navbar.jsp" %>
  
  <div id="container">
    <% String username = (String) request.getAttribute("username");
 	if(request.getSession().getAttribute("user") != null){ %>
      <a>Hi my name is <%= request.getSession().getAttribute("user") %>!</a>
      <p>About me:</p>
      <hr/>
    <%} %>
    
  <% List<Profile> profiles = (List<Profile>) request.getAttribute("profiles");
    if(profiles == null){
  %>
    <p>Create a profile to get started.</p>
  <% } else{
  %>
    <%
      for(Profile profile : profiles){
      String user = (String) request.getSession().getAttribute("user");
    %>
  
      <% if(profile.getProfile().equals(user)){
      %>
        <a><%= profile.getAbout() %></a>
      <% } %>
    <%} %>
  <% } %>
  <hr/>
  
  <form action="/profile/" method="POST">
       <div class="form-group">
          <label class="form-control-label">Edit:</label>
        <input type="text" name="profileAbout">
      </div>
             
      <button type="submit">Update</button>
      
    </form>
  </div>  
</body>
</html>
