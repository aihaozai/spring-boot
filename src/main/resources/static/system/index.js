layui.extend({
    system: 'modules/system'
}).define(['system', 'conf'], function (exports) {
    layui.system.initPage();
    exports('index', {});
});