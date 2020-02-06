layui.use(['system','view','conf','jquery']).define(function (exports) {
    var system = layui.system;
    var view = layui.view;
    var conf = layui.conf;
    var $ = layui.jquery;
    var self ={};
    self.loadPage = function(dom,url,data) {
        view.renderPage(url,data,system.tabUrl,dom,function(dom){
            var type = dom.attr('lay-page-jump');
            switch (type) {
                case 'parentJump': //主体转翻页
                    $('#' + conf.containerBody).addClass('container-parent-hide');
                    self.childrenShow(url);
                    break;
                case 'childrenJump':   //翻页转翻页
                    dom.closest('.layui-body').addClass('container-page-hide');
                    self.childrenShow(url);
                    break;
            }
        });
        //$(window).resize();
    };

    self.intoHtml = function(dom,url,data) {
        view.loadPostHtml(url,data,function (res) {
            $('#' + dom).append(res.html).find('button').remove();
        })
    };
    //翻页返回主体（方法）
    self.backParent = function(dom,flag){
        $('#' + conf.containerBody).removeClass('container-parent-hide');
        if(flag){
            layui.each( $('#' + conf.containerBody).children(),function(index,item) {
                if ($(this).attr('lay-url') === system.tabUrl) {
                    $(this).remove();
                    system.initView(system.route);
                    return false;
                }
            });
        }
        dom.closest('.layui-body').remove();
        layui.each( $('#' + conf.pageBody).children(),function(index,item) {
            if ($(this).attr('lay-parent-url') === system.tabUrl) {
                $(this).remove();
                return false;
            }
        });
    };
    //翻页返回主体（点击按钮）
    $(document).on('click','[lay-page-backParent]',function (e) {
        $('#' + conf.containerBody).removeClass('container-parent-hide');
        if($(this).attr('lay-page-backParent')==='true'){
            layui.each( $('#' + conf.containerBody).children(),function(index,item) {
                if ($(this).attr('lay-url') === system.tabUrl) {
                    $(this).remove();
                    system.initView(system.route);
                    return false;
                }
            });
        }

        $(this).closest('.layui-body').remove();
        layui.each( $('#' + conf.pageBody).children(),function(index,item) {
            if ($(this).attr('lay-parent-url') === system.tabUrl) {
                $(this).remove();
                return false;
            }
        });
        return false;
    });

    //翻页返回孩子返回孩子（点击按钮）
    $(document).on('click','[lay-page-backChildren]',function (e) {
        var url = $(this).closest('.layui-body').attr('lay-cParent-url');
        layui.each( $('#' + conf.pageBody).children(),function(index,item) {
            if ($(this).attr('lay-url') === url) {
                $(this).removeClass('container-page-hide');
                return false;
            }
        });
        $(this).closest('.layui-body').remove();
        return false;
    });

    self.childrenShow = function(url) {
        layui.each( $('#' + conf.pageBody).children(),function(index,item){
            if($(this).attr('lay-url')===url){
                $(this).removeClass('container-page-hide');
                return false;
            }
        });
    };
    exports('page', self);
});