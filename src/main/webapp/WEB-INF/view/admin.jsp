<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.lang.Integer" %>

<!DOCTYPE html>
<html>
<head>
  <title>Admin Page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/admin">Admin</a>
  </nav>

  <div id="container">
    <h1>Admin Page</h1>

    <% if(request.getAttribute("error") != null){ %>

      <h2 style="color:red"><%= request.getAttribute("error") %></h2>

    <% } else { %>

      <%
      int userCount = (Integer) request.getAttribute("userCount");
      int messageCount = (Integer) request.getAttribute("messageCount");
      int conversationCount = (Integer) request.getAttribute("conversationCount");
      %>

      <h2>Hello, administrator!</h2>
      <p>Here is some data:</p>
      <p><b>User count:</b> <%= userCount %></p>
      <p><b>Message count:</b> <%= messageCount %></p>
      <p><b>Conversation count:</b> <%= conversationCount %></p>

    <% } %>

  </div>
</body>
</html>
