var server = require("./scripts/server")
var handlers = require("./scripts/handlers")

var h = {}
h["/"] = handlers.landingPage
h["/result"] = handlers.resultPage
h["/statistics"] = handlers.serverStatistics

server.startServer(h);