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
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>

<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">

  <%-- For lock/unlock icons --%>
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.2.0/css/all.css" integrity="sha384-hWVjflwFxL6sNzntih27bfxkr27PmbbK/iSvJ+a4+0owXq79v+lsFkW54bOGbiDQ" crossorigin="anonymous">

</head>
<body>

  <%@ include file="/WEB-INF/reusables/navbar.jsp" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h1>New Conversation</h1>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
            <input type="text" name="conversationTitle">
            <label class="form-control-label">Add Users:</label>
            <input type="text" name="add_users">
            <label class="form-control-label">Private:</label>
            <input type="checkbox" name="privatize">
          </div>

        <button type="submit">Create</button>
      </form>
      <hr/>
    <% } %>

    <h1>Conversations</h1>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list" style="list-style: none;">
    <%
      for(Conversation conversation : conversations){
    %>
      <li>
        <% if (conversation.getIsPrivate()) { %>
          <i class="fas fa-lock fa-fw"></i>
        <% } else { %>
          <i class="fas fa-lock-open fa-fw"></i>
        <% } %>
        <a href="/chat/<%= conversation.getTitle()%>">
          <%= conversation.getTitle() %></a>
        </li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
  </div>
</body>
</html>
