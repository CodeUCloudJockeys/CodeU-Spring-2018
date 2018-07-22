<%--TODO: fix wordcloud displaying --%>
<%@ page import="codeu.controller.util.JavaToJavascriptUtil" %>
<%@ page import="codeu.controller.util.ConversationDataUtil" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>

<head>
    <script src="https://cdn.zingchart.com/zingchart.min.js"></script>
    <script>
        zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
        ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "ee6b7db5b51705a13dc2339db3edaf6d"];
    </script>
    <link href="https://fonts.googleapis.com/css?family=Crete+Round" rel="stylesheet">
    <style></style>
</head>

<body>
<%
    String requestURL = request.getRequestURI();
    String conversationTitle = requestURL.substring("/chat".length());

    ConversationStore conversationStore = ConversationStore.getInstance();
    Conversation currentConversation = conversationStore.getConversationWithTitle(conversationTitle);

    ConversationDataUtil utilInstance = new ConversationDataUtil(currentConversation);
    Map<String, Integer> wordFrequency = utilInstance.getWordFrequency();
    String wordcloudText = JavaToJavascriptUtil.WordFreqUtil(wordFrequency);

%>
<div id="myChart"></div>
<script>
    var myConfig = {
        type: 'wordcloud',
        options: {
            text: <%=wordcloudText%>,
            aspect: 'flow-center',
            rotate: true,
            colorType: 'palette',
            palette: ['#D32F2F', '#5D4037', '#1976D2', '#E53935', '#6D4C41', '#1E88E5', '#F44336', '#795548', '#2196F3', '#EF5350', '#8D6E63', '#42A5F5'],

            style: {
                fontFamily: 'Crete Round',

                hoverState: {
                    backgroundColor: '#D32F2F',
                    borderRadius: 2,
                    fontColor: 'white'
                },
                tooltip: {
                    text: '%text: %hits',
                    visible: true,

                    alpha: 0.9,
                    backgroundColor: '#1976D2',
                    borderRadius: 2,
                    borderColor: 'none',
                    fontColor: 'white',
                    fontFamily: 'Georgia',
                    textAlpha: 1
                }
            }
        },
    };

    zingchart.render({
        id: 'myChart',
        data: myConfig,
        height: 200,
        width: '100%'
    });
</script>
</body>

</html>