/**
 * Created by JetBrains WebStorm.
 * User: tom-trix
 * Date: 12/11/12
 * Time: 12:06 AM
 * To change this template use File | Settings | File Templates.
 */
var server = require("./scripts/server")
var handlers = require("./scripts/handlers")

var h = {}
h["/"] = handlers.landingPage
h["/result"] = handlers.resultPage

server.startServer(h);