layui.define(['layer', 'form'],function (exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form;
    var component = {};
    component.showSelect = function(dom,domId,url) {
        $.getJSON(url, function (res) {
            if (res.code === "success") {
                var sel = '<select id="'+domId+'" >';
                layui.each(res.data, function (index, item) {
                    sel += '<option value="' + item.value + '">' + item.name + '</option>';
                });
                sel += '</select>';
                dom.append(sel);
                form.render('select');
            } else {
                layui.msg(res.code)
            }
        });
    };
    exports('component',component);
});