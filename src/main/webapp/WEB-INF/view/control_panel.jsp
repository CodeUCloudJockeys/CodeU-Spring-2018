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
<%@ page import="codeu.model.data.User" %>
<%
List<User> userList = (List<User>) request.getAttribute("userList");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Admin Page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@ include file="/WEB-INF/reusables/navbar.jsp" %>

  <div id="container">
    <h1>Control Panel</h1>

    <% if(request.getAttribute("error") != null){ %>

      <h2 style="color:red"><%= request.getAttribute("error") %></h2>

    <% } else { %>

      <form method="post" action="/control_panel">

        <h2>Hello, administrator!</h2>
        <p>Here are the users:</p>
        <table border=1 frame=void>
          <tr>
            <th>Username</th>
            <th>Admin</th>
          </tr>
          <%
            for (User user : userList) {
          %>

            <tr>
              <td><%= user.getName() %></td>

              <%-- Checkbox is checked and disabled if user is admin --%>
              <td><input type="checkbox" name="adminifier" value="<%= user.getId().toString() %>" <%= user.getIsAdmin() ? "checked disabled" : "" %> /></td>
            </tr>

          <% } %>

          <td><input type="submit" name="Submit" value="submit" /></td>

        </table>

      </form>

    <% } %>

  </div>
</body>
</html>
