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
    cols.switchFun = function(dom,fun){
        switch (fun) {
            case 'dblMenuType':
                mFun(dom);
                break;
            case 'dblOrganLevel':
                oFun(dom);
                break;
            case '3':

                break;
        }
    };
    function bind(dom) {
        var innerHtml = dom.prop("innerHTML");
        var colsData = '';
        layui.each(dom.parent('tr')[0].children,function(index,item){
            //获取id
            if(item.dataset.field==="id"){
                colsData = item.innerText.trim();
            }
            //添加旁边单元格点击事件-关闭下拉选择
            if(item.dataset.field!==dom.closest('td').attr('data-field')){
                $(this).click(function () {
                    dom.empty().append(innerHtml);
                });
            }
        });
        return colsData;
    }
    /*
    * 双击显示菜单类型下拉选择
    * 菜单类型方法
    * */
    function mFun(dom){
        cols.type = dom.data('content').toString();
        if(dom.data('content')=='1'){
            layer.msg('目录类型不能更改');
            return;
        }
        var colsData = bind(dom);
        $.post('/menu/getCurrentMenuType', {"id":colsData},function (res) {
            if(res.data.length>0){
                var sel =  '<select id="downSelectType" lay-filter="downSelectType" >' ;
                layui.each(res.data,function(index,item){
                    sel += '<option value="'+item.value+'">'+item.name+'</option>';
                });
                sel+= '</select>';
                dom.text('').append(sel);
                $("#downSelectType").val(dom.data('content'));
                form.render('select');
            }else {
                if(res.message)layer.msg(res.message,{time:false,btn: ['我知道了']});
                else  layer.msg('该菜单拥有子菜单不能更改其类型');
            }
            if(res.data.code==='fail')layer.msg('系统异常');
        });
    }
    //监听选择事件
    form.on('select(downSelectType)', function (data) {
        layui.each(data.othis.parents('tr')[0].children,function(index,item){
            if(item.dataset.field==="id") {
                $.post("/menu/updateMenuById", {"id":item.innerText.trim(),"field": "menuType", "value": data.value}, function (res) {
                    layer.msg(res.message);
                    if (res.code === 'fail') {
                        setSelect(data,cols.type);  //回复旧数据
                    }else {
                        setSelect(data,data.value);
                    }
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

    /*
    * 双击显示机构等级下拉选择
    * 机构等级方法
    * */
    function oFun(dom){
        cols.type = dom.data('content').toString();
        cols.text = dom.text();
        var colsData = bind(dom);
        $.post('/organ/getCurrentOrganLevel', {"id":colsData},function (res) {
            if(res.data.level.length>0){
                var sel ='<select id="downSelectLevel" lay-filter="downSelectLevel" >';
                layui.each(res.data.level,function(index,item){
                    sel += '<option value="'+item.dictCode+'">'+item.dictName+'</option>';
                });
                sel+= '</select>';
                dom.text('').append(sel);
                $("#selectLevel").val(res.data.currentLevel);
                form.render('select');
            }
            if(res.data.code==='fail')layer.msg('系统异常');
        });
    }
    //监听选择事件
    form.on('select(downSelectLevel)', function (data) {
        var text = $(this).text();
        layui.each(data.othis.parents('tr')[0].children,function(index,item){
            if(item.dataset.field==="id") {
                $.post("/organ/updateOrganById", {"id": item.innerText.trim(),"field": "organLevel" ,"value": data.value}, function (res) {
                    layer.msg(res.message);
                    if (res.code === 'fail') {
                        //回复旧数据
                        data.othis.parent().empty().append('<button type="button" value="'+cols.type+'" class="layui-btn layui-btn-primary layui-btn-xs">'+cols.text+'</button>');
                    }else {
                        data.othis.parent().empty().append('<button type="button" value="'+data.value+'" class="layui-btn layui-btn-primary layui-btn-xs">'+text+'</button>');
                    }
                })
            }
        });
    });
    exports('cols',cols);
});