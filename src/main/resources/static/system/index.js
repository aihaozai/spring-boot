layui.extend({
    system: 'modules/system',
    page: 'modules/page',
    approve: 'modules/approve',
    controlView: 'modules/controlView',
    websocket: 'modules/websocket'
}).define(['system', 'conf' ,'page','approve','controlView','websocket'], function (exports) {
    layui.system.initPage();
    layui.websocket.start();
    exports('index', {});
});