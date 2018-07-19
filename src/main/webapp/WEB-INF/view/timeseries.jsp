<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="\timeseries\style.css" />
    <script src="/underscore/underscore.js"></script>
    <script src="/moment/moment.js"></script>
    <script src="https://d3js.org/d3.v5.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3-time/1.0.8/d3-time.js"></script>
    <script src="/timeseries/timeseries.js"></script>
    <script>
        window.onload = function() {
            var domEl = 'timeseries';
            var data = [{'value': 1380854103662},{'value': 1363641921283}];
            var brushEnabled = true;
            timeseries(domEl, data, brushEnabled);
        }
    </script>
</head>
<body>
    <div class="timeseries"></div>
</body>
</html>
