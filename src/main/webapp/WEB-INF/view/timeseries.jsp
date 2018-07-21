<!DOCTYPE HTML>
<html>
<head>
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
                    dataPoints: [
                        { y: 10, label: "1" },
                        { y: 30,  label: "2" },
                        { y: 45,  label: "3" },
                        { y: 60,  label: "4" },
                        { y: 15,  label: "5" }
                    ]
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