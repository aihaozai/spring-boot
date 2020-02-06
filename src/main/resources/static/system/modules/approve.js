layui.use(['jquery','page','layer','system']).define(function (exports) {
    var $ = layui.jquery,
    page = layui.page,
    system = layui.system,
    layer = layui.layer;
    var self ={};
    self.chooseLeader = function(taskId,level,comment,dom,callback){
        layer.open({
            type: 2,
            title: '选择领导',
            skin: 'layui-layer-admin layui-layer-white', //样式类名
            area: ['300px', '200px'],
            closeBtn: 0, //不显示关闭按钮
            blackBtn: 1, //我的自定义关闭按钮 1开启
            anim: 2,
            shadeClose: true, //开启遮罩关闭
            content: '/approve/getApproveTaskLeader?level='+level,
            success: function (layero, index) {    //成功获得加载html时，预先加载，将值从父窗口传到 子窗口
                var body = layer.getChildFrame('body', index);  //弹出窗口body
                //赋值数据
                body.find('#taskId').val(taskId);
                body.find('#comment').val(comment);
            },
            end:function () {
                //判断是否已经审批
                $.post("/task/checkTask",{"taskId":taskId},function(res){
                    if(res.code==="success"){
                        layer.msg("发起审批成功");
                        page.backParent(dom,true);
                    }else {
                        if ($.isFunction(callback)) callback();
                        system.dot();
                    }
                });
            }
        });
    };
    self.completeApprove = function(key,tableId){
        switch (key) {
            case 'leave': //请假
                $.post("/applyLeave/completeProcess",{"tableId":tableId,"type":"approveP"},function(res){
                    if(res.message!==undefined){
                        layer.msg(res.message);
                    }
                });
                break;
        }
    };
    exports('approve', self);
});