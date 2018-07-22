<!DOCTYPE html>
<html>
<head>
    <title>User Message Count</title>
</head>
<body>
   <%
        Map<String, Integer> messageCount = utilInstance.getUsernameFrequency();
        String userMessages = JavaToJavascriptUtil.UsernameFreqUtil(messageCount);
    %>
<h1>Messages sent per user</h1>
<canvas id="canvasId"></canvas>
    <script src="/canvas/excanvas.js"></script>
    <script src="/canvas/html5-canvas-bar-graph.js"></script>
<script>
    var ctx = document.getElementById("canvasId").getContext("2d");

    var graph = new BarGraph(ctx);
    graph.margin = 2;
    graph.width = 450;
    graph.height = 150;
     <%=userMessages%>;
</script>
</body>
</html>
