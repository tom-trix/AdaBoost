var child = require("child_process");
var querystring = require("querystring");
var fs = require("fs");

exports.landingPage = function(response, data) {
    fs.readFile('./html/query.html', function (error, filedata) {
        if (!error) {
            response.writeHead(200, {"Content-Type": "text/html"});
            response.write(filedata);
            response.end();
        }
    });
}

exports.resultPage = function(response, data) {
    var msg = '"' + querystring.parse(data).text + '"';
    child.exec("java -jar ./adaboost/adaBoostClient.jar " + msg, function(in1, out1, err1) {
        fs.readFile('./html/result.html', function (err2, out2) {
            var percent = (100*Math.min(1, Math.max(0, out1 / 2 + 0.5))).toString().substr(0, 5);
            response.writeHead(200, {"Content-Type": "text/html"});
            response.write(out2.toString().replace("###", "Сумма = " + out1 +"<br>Оценка = " + percent + "%"));
            response.end();
        });
    });
}

exports.serverStatistics = function(response, data) {
    fs.readFile('./html/statistics.html', function (err1, filedata) {
        fs.readFile('./adaboost/1.txt', function (err2, output) {
            if (!err1 && !err2) {
                output = output.toString().replace(/\n/g, "<br>");
                output = output.toString().replace(/ /g, "&nbsp");
                response.writeHead(200, {"Content-Type": "text/html"});
                response.write(filedata.toString().replace("###", output));
                response.end();
            }
        });
    });
}