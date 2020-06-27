layui.use(['jquery']).define(function (exports) {
    var $ = layui.jquery;
    var self ={};
    self.array = ['show','hide','disabled'];
    self.loadView = function(id,target) {
        var dom = $('#' +id);
        var type = dom.find('.layui-check-view').val();
        layui.each(target,function(index,item) {
            findDom(dom.find(item),type);
        });
    };

    self.setData = function(target) {
        for(key in target){
            $('#'+key).val(target[key]);
        }
    };

    self.setDataByType = function(id,type,target) {
        var dom = $('#' +id);
        if(dom.find('.layui-check-view').val()===type){
            for(key in target){
                $('#'+key).val(target[key]);
            }
        }
    };

    function findDom(dom,type){
        layui.each(dom,function(index,item) {
            var value = $(this).attr("lay-check-view");
            if(value!==""&&value!==undefined){
                if(value.split('|')[0]===type){
                    render($(this),value.split('|'));
                }
            }
        });
    }

    function render(dom,type) {
        switch (type[0]) {
            case 'detail': //查看
                if(type[1]===self.array[1]){
                    dom.css("display","none");
                }else if (type[1]===self.array[2]) {
                    dom.attr("disabled", "true");
                }
                break;
            case 'edit':

                break;
        }
    }

    exports('controlView', self);
});