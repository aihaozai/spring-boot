layui.define(['form'],function (exports) {
    var $ = layui.jquery,
    form = layui.form;
    var cols = {};
    cols.checkCols = function(dom,param){
        var colsData = {};
        layui.each(dom.parent('tr')[0].children,function(index,item){
            colsData[item.dataset.field] = item.innerText.trim();
        });
        var array = param.split(':');
        if(array[0]==='check'){
            switch (colsData[array[1]]) {
                case '按钮':
                    layer.msg('按钮类型不能编辑菜单图标');
                    return false;
                case '菜单':
                    layer.msg('菜单类型不能編辑菜单图标');
                    return false;
            }
        }
        return true
    };
    cols.showSelect = function(dom,url){
        cols.type = dom.data('content').toString();
        if(dom.data('content')=='1'){
            layer.msg('目录类型不能更改');
            return;
        }
        var colsData = '';
        layui.each(dom.parent('tr')[0].children,function(index,item){
            if(item.dataset.field=="menuName")colsData= item.innerText.trim();
        });
        $.post(url, {"menuName":colsData},function (res) {
            if(res.data.length>0){
                var sel =  '<select id="selectType" lay-filter="menuType" >' ;
                layui.each(res.data,function(index,item){
                    sel += '<option value="'+item.value+'">'+item.name+'</option>';
                });
                sel+= '</select>';
                dom.text('').append(sel);
                $("#selectType").val(dom.data('content'));
                form.render('select');
            }else {
                if(res.message)layer.msg(res.message,{time:false,btn: ['我知道了']});
                else  layer.msg('该菜单拥有子菜单不能更改其类型');
            }
            if(res.data.code=='fail')layer.msg('系统异常');
        });
    };
    //监听选择事件
    form.on('select(menuType)', function (data) {
        layui.each(data.othis.parents('tr')[0].children,function(index,item){
            if(item.dataset.field=="menuName") {
                $.post("/menu/updateMenuByFiled", {"tField": "menuType", "tValue": data.value,"field": "menuName", "value": item.innerText.trim()}, function (res) {
                    layer.msg(res.message);
                    if (res.code === 'fail') {
                        setSelect(data,cols.type);  //回复旧数据
                    }
                    setSelect(data,data.value);

                })
            }
        });
    });
    function setSelect(data,value){
        switch (value) {
            case '1':
                data.othis.parent().empty().append('<button type="button" value="1" class="layui-btn layui-btn-xs layui-btn-normal">目录</button>');
                break;
            case '2':
                data.othis.parent().empty().append('<button type="button" value="2" class="layui-btn layui-btn-primary layui-btn-xs">菜单</button>');
                break;
            case '3':
                data.othis.parent().empty().append('<button type="button" value="3" class="layui-btn layui-btn-primary layui-btn-xs">按钮</button>');
                break;
        }
    }
    exports('cols',cols);
});