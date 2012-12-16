var child = require("child_process");
var querystring = require("querystring");
var fs = require("fs");

//вывод главной страницы
exports.landingPage = function(response, data) {
    fs.readFile('./html/index.html', function (error, filedata) {
        if (!error) {
            response.writeHead(200, {"Content-Type": "text/html"});
            response.write(filedata);
            response.end();
        }
    });
}

//вывод страницы результата
exports.resultPage = function(response, data) {
    var msg = '"' + querystring.parse(data).text + '"';
    child.exec("java -jar ./adaboost/adaBoostClient.jar " + msg, function(in1, out1, err1) {
        fs.readFile('./html/result.html', function (err2, out2) {
            var percent = (Math.min(100*Math.abs(out1))).toString().substr(0, 5);
            response.writeHead(200, {"Content-Type": "text/html"});
            response.write(out2.toString().replace("###", "Сумма = " + out1 +"<br>Оценка: это "+ (out1 > 0 ? "" : " не ") + "спам (" + percent + "%)"));
            response.end();
        });
    });
}

//вывод статистики работы сервера (файл 1.txt)
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

//вывод качества работы сервера (отправка запроса getQuality)
exports.quality = function(response, data) {
    child.exec("java -jar ./adaboost/adaBoostClient.jar getQuality", function(in1, out1, err1) {
        fs.readFile('./html/result.html', function (err2, out2) {
            out1 = out1.toString().replace(/\n/g, "<br>");
            response.writeHead(200, {"Content-Type": "text/html"});
            response.write(out2.toString().replace("###", "<b>Характеристики сильного классификатора на тестовой выборке:</b><br><br>" + out1));
            response.end();
        });
    });
}