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
<%@ page import="codeu.model.data.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <title>Activity Feed Page</title>
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<%@ include file="/WEB-INF/reusables/navbar.jsp" %>

<div id="container">
    <h1>Activity</h1>
    <%
    List<Activity> activities = (List<Activity>) request.getAttribute("activities");
    if(activities == null || activities.isEmpty()){
    %>
    <p>There is no activity to be displayed</p>
    <%
    }else{
    %>
      <ul class="mdl-list">
    <%
        for(Activity activity : activities){
    %>
          <li><%=activity.getActivityMessage()%></li>
    <%
        }
    }
    %>
      </ul>
</div>
</body>
</html>
