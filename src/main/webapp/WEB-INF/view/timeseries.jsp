<!DOCTYPE HTML>
<html>
<head>
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
</head>
<body>
<div id="chartContainer" style="height: 300px; width: 100%;"></div>
<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
</body>
</html>