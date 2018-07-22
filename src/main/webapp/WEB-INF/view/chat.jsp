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
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.controller.util.ConversationDataUtil" %>
<%@ page import="codeu.controller.util.JavaToJavascriptUtil" %>
<%@ page import="java.util.Map" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
Boolean isOwner = (Boolean) request.getAttribute("is_owner");

ConversationDataUtil utilInstance = new ConversationDataUtil(conversation);

%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }

    #flip-tabs{
      width:700px;
      margin:20px auto;
      position:relative;
    }
    #flip-navigation{
      margin:0 0 10px; padding:0;
      list-style:none;
    }
    #flip-navigation li{
      display:inline;
    }
    #flip-navigation li a{
      text-decoration:none; padding:10px;
      margin-right:0px;
      background:#f9f9f9;
      color:#333; outline:none;
      font-family:Arial; font-size:12px; text-transform:uppercase;
    }
    #flip-navigation li a:hover{
      background:#999;
      color:#f0f0f0;
    }
    #flip-navigation li.selected a{
      background:#999;
      color:#f0f0f0;
    }
    #flip-container{
      width:300px;
      font-family:Arial; font-size:13px;
    }
    #flip-container div{
    }
  </style>

  <script src="/jquery.quickflip/jquery-3.3.1.min.js"></script>
  <script src="/jquery.quickflip/jquery.quickflip.js"></script>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
    $('document').ready(function(){
        //initialize quickflip
        $('#flip-container').quickFlip();

        $('#flip-navigation li a').each(function(){
            $(this).click(function(){
                $('#flip-navigation li').each(function(){
                    $(this).removeClass('selected');
                });
                $(this).parent().addClass('selected');
                //extract index of tab from id of the navigation item
                var flipid=$(this).attr('id').substr(4);
                //Flip to that content tab
                $('#flip-container').quickFlipper({ }, flipid, 1);

                return false;
            });
        });
    });
  </script>
</head>
<body onload="scrollChat()">

  <%@ include file="/WEB-INF/reusables/navbar.jsp" %>

  <div id="container">

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>
    <hr/>

    <div id="chat">
      <ul>
    <%
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
    %>
      <li><strong><%= author %>:</strong> <%= message.getContent() %></li>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message">
        <br/>
        <button type="submit">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>
    <% if (isOwner && conversation.getIsPrivate()) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="add_users">
        <br/>
        <button type="submit">Add Users</button>
    </form>
    <% } %>
  </div>

  <div id="flip-tabs" >
    <ul id="flip-navigation" >
      <li class="selected"><a href="#" id="tab-0"  >Word Cloud</a></li>
      <li><a href="#" id="tab-1" >TimeSeries</a></li>
      <li><a href="#" id="tab-2" >Message count</a></li>
    </ul>
    <div id="flip-container">
      <div>
        <script src="https://cdn.zingchart.com/zingchart.min.js"></script>
        <script>
            zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
            ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "ee6b7db5b51705a13dc2339db3edaf6d"];
        </script>
        <link href="https://fonts.googleapis.com/css?family=Crete+Round" rel="stylesheet">
        <%
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
      </div>
      <div>
        <%
          Map<Integer, Integer> hourFrequency = utilInstance.getHourFrequency();
          String frequency = JavaToJavascriptUtil.HourFreqUtil(hourFrequency);
        %>

        <script>
            window.onload = function () {

                var chart = new CanvasJS.Chart("chartContainer", {
                    animationEnabled: true,
                    theme: "light2", // "light1", "light2", "dark1", "dark2"
                    title:{
                        text: "Messages sent per hour"
                    },
                    axisY: {
                        title: "Messages"
                    },
                    data: [{
                        type: "column",
                        showInLegend: true,
                        legendMarkerColor: "grey",
                        legendText: "Number of messages",
                        dataPoints: <%=frequency%>
                    }]
                });
                chart.render();

            }
        </script>
        <div id="chartContainer" style="height: 300px; width: 100%;"></div>
        <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
      </div>
      <div>
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
      </div>
    </div>
  </div>

</body>
</html>
