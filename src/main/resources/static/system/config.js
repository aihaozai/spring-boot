layui.define(function(exports) {
  exports('conf', {
    container: 'main',
    containerBody: 'main-body',
    pageBody: 'page-body',
    entry: '/index',
    eventName: 'layadmin-event',
    webSocket: 'ws://localhost:8082/websocket/socketServer',
    mozWebSocket: 'ws://localhost:8082/websocket/socketServer',
    sockJS: 'http://localhost:8082/sockjs/socketServer'
  })
});
