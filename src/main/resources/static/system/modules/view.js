//视图路由
layui.extend({
    })
    .define(
        ['jquery','laytpl','element', 'form'],
        function (exports) {
            var $ = layui.jquery;
            var conf = layui.conf;
            var laytpl = layui.laytpl;
            conf.viewTabs === '1';
            var self = {
                ie8:
                    navigator.appName === 'Microsoft Internet Explorer' &&
                    navigator.appVersion.split(';')[1].replace(/[ ]/g, '') === 'MSIE8.0',
                container: $('#' + conf.container),
                containerBody: null,
                containerMain: null
            };
            self.tab = {
                change: function (route, callback) {
                    if(self.containerMain===null){
                        self.containerMain='main';
                        route.fileurl = '/baiduMap';
                        //route.fileurl = 'menu/menuList';
                    }
                    var fileUrl = route.fileurl;
                    self.loadHtml(fileUrl, function (res) {
                        var htmlElem = $(
                            "<div lay-url='" +fileUrl + "'>" +res.html +'</div>'
                        );
                        var params = self.fillHtml(fileUrl, htmlElem, 'prepend');
                       // if ($.isFunction(callback)) callback(params)
                    });
                    return false
                }
            };
            self.parse = function (container) {
                if (container === undefined) container = self.containerBody;
                var template = '';
                if (container.get(0) === undefined)
                template = container.find('[template]');
                else
                template = container.get(0).tagName === 'SCRIPT'
                    ? container
                    : container.find('[template]');
                var renderTemplate = function (template, data, callback) {
                    laytpl(template.html()).render(data, function (html) {
                       console.log(html);
                        //html.attr('is-template', true);
                        template.after(html);
                        if ($.isFunction(callback)) callback(html)
                    })
                };

                layui.each(template, function (index, t) {
                    var tem = $(t);
                    console.log(tem)
                    var url = tem.attr('lay-url') || '';
                    var api = tem.attr('lay-api') || '';
                    var type = tem.attr('lay-type') || 'get';
                    var data = new Function('return ' + tem.attr('lay-data'))();
                    var done = tem.attr('lay-done') || '';
                    console.log("url"+url)
                    console.log("api"+api)
                    console.log("type"+type)
                    console.log("data"+data)
                    console.log("done"+done)
                    if (url || api) {
                        //进行AJAX请求
                        $.ajax({
                            type: "POST",
                            url: api,
                            contentType:"application/json;charset=UTF-8",
                            dataType:"json",
                            success: function (res) {
                                templateData = data;
                                renderTemplate(tem, res);
                                if (done) new Function(done)()
                            }
                        });
                    }else{
                        renderTemplate(tem, {});
                        if (done) new Function(done)()
                    }
                })
            };
            self.loadHtml = function (url, callback) {
                console.log(callback)
                console.log(url)
                $.ajax({
                    url: url,
                    type: 'get',
                    dataType: 'html',
                    success: function (r) {
                        callback({html: r, url: url});
                    }
                });
            };
            self.loadPostHtml = function (url,data,callback) {
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType:"application/json;charset=UTF-8",
                    data: JSON.stringify(data),
                    dataType: 'html',
                    success: function (r) {
                        callback({html: r, url: url});
                    }
                });
            };
            self.fillHtml = function (url, htmlElem, modeName) {
                var title = '';
                var container = self.containerBody || self.container;
                container[modeName]("<div lay-url='" +url+ "'>" +htmlElem.html() +'</div>');
                if (modeName === 'prepend') {
                    self.parse(container.children('[lay-url="' + url + '"]'))
                } else {
                    self.parse(container)
                }
                return {title: title, url: url, htmlElem: htmlElem}
            };
            //解析html文件
            self.render = function (fileurl, callback) {
                self.loadHtml(fileurl, function (res) {
                    console.log(res.html+"file")
                    var htmlElem = $('<div>' + res.html + '</div>');
                    var params = self.fillHtml(res.url, htmlElem, 'html');
                    if ($.isFunction(callback)) callback()
                })
            };

            //加载 tab
            self.renderTabs = function (route, callback) {
                console.log(callback);
                var tab = self.tab;
                tab.change(route, callback)
            };
            //加载layout文件
            self.renderLayout = function (callback, url) {
                if (url === undefined) url = '/layout';
                self.containerBody = null;
                self.render(url, function (res) {
                    self.containerBody = $('#' + conf.containerBody);
                    if (conf.viewTabs === true) {
                        //self.containerBody.addClass('febs-tabs-body');
                    }
                    layui.system.appBody = self.containerBody;
                    if ($.isFunction(callback)) callback()
                })
            };
            self.renderPage = function (url,data,tabUrl,dom,callback) {
                if (url === undefined) return;
                self.pageBody = $('#' + conf.pageBody);
                self.loadPostHtml(url,data,function (res) {
                    var htmlElem = $('<div class="layui-body container-page-hide" lay-parent-url="'+tabUrl+'" lay-url="'+res.url+'">' + res.html + '</div>');
                    if(dom.attr('lay-page-jump')==="childrenJump") {
                       var url = dom.closest('.layui-body').attr("lay-url");
                        htmlElem.attr("lay-cParent-url",url);
                    }
                    $('#' + conf.pageBody).append(htmlElem);
                    if ($.isFunction(callback)) callback(dom);
                })
            };
            exports('view', self)
        }
    );
