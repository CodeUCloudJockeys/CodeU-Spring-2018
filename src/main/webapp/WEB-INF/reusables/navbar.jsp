
<nav>
  <a id="navTitle" href="/">CodeU Chat App</a>
  <a href="/conversations">Conversations</a>
    <a href="/about.jsp">About</a>
  <% if (request.getSession().getAttribute("admin") != null
      && request.getSession().getAttribute("admin") == "true") {
  %>
    <a href="/admin">Admin</a>
    <a href="/control_panel">Control Panel</a>
  <% } %>
    <a href="/activityfeed">Activity</a>
  <% String username = (String) request.getSession().getAttribute("user");
    if(username != null){ %>
    <a href = "/profile/<%= username %>" >Profile</a>
  <form action="/logout" method="POST">
    <input id="logout" type="submit" value="Logout">
  </form>
  <% } else{ %>
    <a href="/login">Login</a>
  <% } %>
</nav>
