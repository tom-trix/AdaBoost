/**
 * Created by JetBrains WebStorm.
 * User: tom-trix
 * Date: 12/11/12
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
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
    child.exec("java -jar ./adaboost/adaBoostClient.jar " + msg, function(input, output, error) {
        var p = Math.min(1, Math.max(0, output / 2 + 0.5));
        response.writeHead(200, {"Content-Type": "text/plain"});
        response.write("Оценка = " + p + " %");
        response.end();
    });
}

exports.serverStatistics = function(response, data) {
    fs.readFile('./html/statistics.html', function (err1, filedata) {
        fs.readFile('./adaboost/1.txt', function (err2, output) {
            if (!err1 && !err2) {
                var i = 0;
                while (i++ < 300) {
                    output = output.toString().replace("\n", "<br>");
                    output = output.toString().replace(" ", "&nbsp");
                }
                response.writeHead(200, {"Content-Type": "text/html"});
                response.write(filedata.toString().replace("###", output));
                response.end();
            }
        });
    });
}