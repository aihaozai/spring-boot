layui.define(function(exports) {
  exports('conf', {
    container: 'main',
    containerBody: 'main-body',
    pageBody: 'page-body',
    entry: '/index',
    eventName: 'layadmin-event',
    mobileWidth: 992,
    webSocket: 'ws://47.115.80.180:80/websocket/socketServer',
    mozWebSocket: 'ws://47.115.80.180:80/websocket/socketServer',
    sockJS: 'http://47.115.80.180:80/sockjs/socketServer' 
  })
});
