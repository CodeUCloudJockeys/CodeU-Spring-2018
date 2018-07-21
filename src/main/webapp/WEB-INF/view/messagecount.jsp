<!DOCTYPE html>
<html>
<head>
    <title>User Message Count</title>
</head>
<body>
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
    graph.xAxisLabelArr = ["jcolladokuri", "eselenic", "dgalbraith", "raguilera"];
    graph.update([3, 50, 30, 10]);
</script>
</body>
</html>
