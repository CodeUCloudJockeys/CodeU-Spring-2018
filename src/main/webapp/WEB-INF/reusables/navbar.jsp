
<nav>
  <a id="navTitle" href="/">CodeU Chat App</a>
  <a href="/conversations">Conversations</a>
  <% String username = (String) request.getSession().getAttribute("user");
    if(username != null){ %>
    <a>Hello <%= username %>!</a>
    <a href = "/profile/<%= username %>" >Profile</a>
  <% } else{ %>
    <a href="/login">Login</a>
  <% } %>
  <a href="/about.jsp">About</a>
  <% if (request.getSession().getAttribute("admin") != null
      && request.getSession().getAttribute("admin") == "true") {
  %>
    <a href="/admin">Admin</a>
    <a href="/control_panel">Control Panel</a>
  <% } %>
    <a href="/activityfeed">Activity</a>
  <form action="/logout" method="POST">
    <input id="logout" type="submit" value="Logout">
  </form>
</nav>
