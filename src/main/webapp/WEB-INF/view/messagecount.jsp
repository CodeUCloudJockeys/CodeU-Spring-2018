<!DOCTYPE html>
<html>
<head>
    <title>User Message Count</title>
</head>
<body>
    <%
        String requestURL = request.getRequestURI();
        String conversationTitle = requestURL.substring("/chat".length());

        ConversationStore conversationStore = ConversationStore.getInstance();
        Conversation currentConversation = conversationStore.getConversationWithTitle(conversationTitle);

        ConversationDataUtil utilInstance = new ConversationDataUtil(currentConversation);
        Map<String, Integer> messageCount = utilInstance.getUsernameFrequency();
        String userMessages = JavaToJavascriptUtil.UsernameFreqUtil(messageCount);
    %>
<h1>Messages sent by user</h1>
<canvas id="canvasId"></canvas>
    <script src="/canvas/excanvas.js"></script>
    <script src="/canvas/html5-canvas-bar-graph.js"></script>
<script>
    var ctx = document.getElementById("canvasId").getContext("2d");

    var graph = new BarGraph(ctx);
    graph.margin = 2;
    graph.width = 450;
    graph.height = 150;
    graph.xAxisLabelArr = <%=userMessages%>;
    graph.update([3, 50, 30, 10]);
</script>
</body>
</html>
