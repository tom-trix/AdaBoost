var http = require("http");
var url = require("url");

exports.startServer = function(handler) {
    var callHandler = function(path, response, data) {
        if (typeof (handler[path]) === "function")
            handler[path](response, data);
        else {
            response.writeHead(404, {"Content-Type": "text/plain"});
            response.write("error 404");
            response.end();
        }
    }

    var onRequest = function(request, response) {
        var data = "";
        var path = url.parse(request.url).pathname;
        request.setEncoding("utf8");
        request.addListener("data", function(chunk) {
            data += chunk;
        });
        request.addListener("end", function() {
            callHandler(path, response, data);
        });
    }

    http.createServer(onRequest).listen(3000);
    console.log("Server started at localhost:3000");
}
