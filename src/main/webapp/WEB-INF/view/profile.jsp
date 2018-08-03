<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.List" %>
<%@ page import= "java.util.UUID" %>

<%@ page import="codeu.model.data.Profile" %>
<%@ page import="codeu.model.data.User" %>

<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.store.basic.ProfileStore" %>

<% String user = (String) request.getSession().getAttribute("user");
User userA = (User) request.getAttribute("thisUser");
String aboutMe = (String) request.getAttribute("aboutMe");
List<String> convos = (List<String>) request.getAttribute("convos");
Profile prof = (Profile) request.getAttribute("prof");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= user %></title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@ include file="../reusables/navbar.jsp" %>
  
  <div id="container">
    <% if(user != null){ %>
      <a>Hi my name is <%= user %>!</a>
      <hr/>
    <%} %>
    
    <h1>About me:</h1>
    <p> <%= aboutMe %></p>
    <form action="/profile/" method="POST">
      <div class="form-group">
        <label class="form-control-label">Edit:</label>
        <input type="text" name="profileAbout">
      </div>       
      <button type="submit">Update</button>
    </form>
  <hr/>
    <h2>My Conversations:</h2>
    <% for(int i = 0; i <convos.size(); i++){ %>
      <p><%= convos.get(i) %></p>
    <%} %>
   
  <hr/>
  </div>  
</body>
</html>
