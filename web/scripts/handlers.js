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
    child.exec("java -jar x.jar " + msg, function(input, output, error) {
        if (!error) {
            response.writeHead(200, {"Content-Type": "text/plain"});
            response.write(output);
            response.end();
        }
    });
}