layui.extend({
    conf: 'config',
    view: 'modules/view'
}).define(['conf', 'view', 'jquery'], function (exports) {
    var conf = layui.conf;
    var view = layui.view;
    var element = layui.element;
    var $ = layui.jquery;
    var self = {};
    conf.viewTabs = true;
    self.appBody = null;
    self.route = layui.router();
    self.routeLeaveFunc = null;
    self.shrinkCls = 'sidebar-shrink';
    self.openFlag = false;
    self.Click = false;
    self.resizeFlag = false;
    self.tabUrl = null;
    self.routeLeave = function (callback) {
        this.routeLeaveFunc = callback
    };
    //初始化整个页面
    self.initPage = function (initCallback) {
        //加载样式文件
        layui.each(layui.conf.style, function (index, url) {
            layui.link(url + '?v=' + conf.v)
        });
        self.initView(self.route)
    };

    //初始化视图区域
    self.initView = function (route) {
        console.log(route)
        console.log(JSON.stringify(route));
        if (!self.route.href || self.route.href === '/') {
            self.route = layui.router('#' + conf.entry);
            route = self.route
        }
        route.fileurl = route.path;

        if ($.inArray(route.fileurl, conf.indPage) === -1) {
            var loadRenderPage = function (params) {
                if (conf.viewTabs === true) {
                    view.renderTabs(route);
                } else {
                    //view.render(route.fileurl);
                }
            };

            if (view.containerBody == null) {
                //加载layout文件
                view.renderLayout(function () {
                    //加载视图文件
                    loadRenderPage();
                    //监听main-body宽度变化
                    // $("#main-body").resize(function(){
                    //
                    // });
                })
            } else {
                //layout文件已加载，加载视图文件
               loadRenderPage()
            }
        }
    };
    //页面转调导航
    self.navigate = function (url) {
        if (url === conf.entry) url = '/';
        window.location.hash = url
    };
    self.flexible = function (open) {
        //增强版table子表关闭
        $("[lay-url='"+self.tabUrl+"']").find('.childTable.layui-icon.layui-icon-down').trigger("click");
        self.openFlag = open;
        if (open === true) {
             view.container.removeClass(self.shrinkCls);
             view.container.addClass('sidebar-menu');
        } else {
             view.container.addClass(self.shrinkCls);
             view.container.removeClass('sidebar-menu');
        }
        var timer = function bodyresize(){
            self.Click = true;
            $(window).resize();
            self.Click = false;
        };
        setTimeout(timer,400);
        clearTimeout(timer);
    };
    $(window).on('hashchange',function () {
        self.route = layui.router();
        layer.closeAll();
        self.initView(self.route);
    });
    if ($(window).width() <= conf.mobileWidth) {
        self.flexible(false);
    }
    $(window).on('resize', function (e) {
        if(self.Click)return;
        if ($(window).width() < conf.mobileWidth) {
            self.flexible(false);
        } else {
            self.flexible(true);
        }
    });
    $(document).on('click','[lay-href]',function (e) {
        var href = $(this).attr('lay-href');
        var name = $(this).text().trim();
        if (href==='') return;
        self.renderTabTitle(name,href);
        return false;
    });
    $(document).on('click','#LAY-system-side-menu li a i',function (e) {
        if(!self.openFlag)self.flexible(true);
        return false;
    });
    $(document).on('click','#lay-tab .layui-tab-title li',function (e) {
        var url = $(this).attr('lay-url');
        if (url==='') return;
        self.showTab(url);
        return false;
    });
    $(document).on('click','.layui-tab-close',function (e) {
        var thisUrl = $(this).parent().attr("lay-url");
        var url = $("#lay-tab ul li:last-child").attr("lay-url");
        self.closeTab(thisUrl);
        if (url===undefined) return;
        self.showTab(url);
        return false;
    });

    self.on = function (name, callback) {
        return layui.onevent(conf.eventName, 'system(' + name + ')', callback)
    };
    self.event = function (name, params) {
        layui.event(conf.eventName, 'system(' + name + ')', params)
    };
    $(document).on('click','*['+conf.eventName+']',function (e) {
        self.event($(this).attr(conf.eventName), $(this));
    });
    //折叠菜单栏
    self.on('flexible', function (init) {
        var status = view.container.hasClass(self.shrinkCls);
        self.flexible(status);
    });

    //刷新事件
    self.on('refresh', function (init) {
        if(self.tabUrl===null)return;
        if($('#'+conf.containerBody).hasClass('container-parent-hide')){
            $('#'+conf.containerBody).removeClass('container-parent-hide');
        }
        $('#'+conf.pageBody).empty();
        self.closeTab(self.tabUrl);
        self.route.path=self.tabUrl;
        self.route.href=self.tabUrl;
        self.route.fileurl=self.tabUrl;
        self.initView(self.route);
    });
    //全屏事件
    self.on('fullscreen', function (init) {
        if (!document.fullscreenElement && // alternative standard method
            !document.mozFullScreenElement && !document.webkitFullscreenElement) {// current working methods
            if (document.documentElement.requestFullscreen) {
                document.documentElement.requestFullscreen();
            } else if (document.documentElement.mozRequestFullScreen) {
                document.documentElement.mozRequestFullScreen();
            } else if (document.documentElement.webkitRequestFullscreen) {
                document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
            }
        } else {
            if (document.cancelFullScreen) {
                document.cancelFullScreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }
        }
    });

    //消息事件
    self.on('message', function (init) {
        layui.each($('#LAY-system-side-menu').find('a'),function(index,item) {
            if($(this).attr('lay-href')===init.attr('lay-head-href')){
                $(this).closest('dd').addClass('layui-this');
                $(this).closest('li').addClass('layui-nav-itemed');
                self.tabUrl=$(this).attr('lay-href');
                self.renderTabTitle($(this).text().trim(),$(this).attr('lay-href'));
            }
            else {
                $(this).closest('dd').removeClass('layui-this');
                $(this).closest('li').removeClass('layui-nav-itemed').removeClass('layui-this');
            }
        });
    });
    //渲染tab
    self.renderTabTitle = function(name,url){
        var tabDom = $("#lay-tab"),
            arrayTab = [];
        tabDom.find('li').each(function () {
            arrayTab.push($(this).text().substring(0,$(this).text().length-1).trim())
        });
        self.removeDomClass(tabDom.find('li'));
        if(arrayTab.indexOf(name)===-1){
            tabDom.find('ul').append('<li lay-url="'+url+'" class="layui-this">'+name+'</li>');
            self.route = layui.router();
            self.route.path = url;
            self.route.href = url;
            layer.closeAll();
            //加载视图
            self.initView(self.route);
        }else tabDom.find('li').eq(arrayTab.indexOf(name)).addClass('layui-this');
        element.render('tab','system-tab');
        self.showTab(url);
    };
    //去除当前选择tab样式
    self.removeDomClass = function(dom){
        dom.each(function () {
            if($(this).toggleClass('layui-this'))
                $(this).removeClass('layui-this');
        });
    };
    //显示tab
    self.showTab = function(url) {
        $('#' + conf.containerBody).removeClass('container-parent-hide');
        layui.each( $('#' + conf.pageBody).children(),function(index,item){
            if($(this).attr('lay-parent-url')===self.tabUrl){
                $(this).remove();
            }
        });
        self.tabUrl=url;
        $("#" + conf.containerBody).children().each(function () {
            if($(this).attr("lay-url")===url)$(this).css("display","block");
            else $(this).css("display","none");
        });
    };
    //关闭tab
    self.closeTab = function(url) {
        $("#" + conf.containerBody).children().each(function () {
            if($(this).attr("lay-url")===url)$(this).remove();
        });
    };

    self.dot = function(){
        if($('#dot span').length===0){
            $('#dot').append('<span class="layui-badge-dot"></span>');
        }
    };
    self.clearDot = function(){
        $('#dot span').remove();
    };
    exports('system', self)
});
