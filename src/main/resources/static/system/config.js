layui.define(function(exports) {
  exports('conf', {
    container: 'main',
    containerBody: 'main-body',
    pageBody: 'page-body',
    entry: '/index',
    eventName: 'layadmin-event',
    mobileWidth: 992,
    webSocket: 'ws://localhost:80/websocket/socketServer',
    mozWebSocket: 'ws://localhost:80/websocket/socketServer',
    sockJS: 'http://localhost:80/sockjs/socketServer'
  })
});
