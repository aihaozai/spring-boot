layui.use(['conf','jquery','layer','system']).define(function (exports) {
    var conf = layui.conf,
    $ = layui.jquery,
    system = layui.system,
    layer = layui.layer;
    var self ={};
    self.timeoutObj = null;
    self.connect = false;
    var ws = null,index = null;
    self.start = function () {
        try{
            if ('WebSocket' in window) {
                //Websocket的连接
                ws = new WebSocket(conf.webSocket);//WebSocket对应的地址
            }
            else if ('MozWebSocket' in window) {
                //Websocket的连接
                ws = new MozWebSocket(conf.mozWebSocket);//SockJS对应的地址
            }
            else {
                //SockJS的连接
                ws = new SockJS(conf.sockJS);    //SockJS对应的地址
            }
            ws.onopen = onOpen;
            ws.onmessage = onMessage;
            ws.onerror = onError;
            ws.onclose = onClose;
            window.close = function () {
                ws.onclose();
            };
        }catch(e){

        }
    };

    self.sendMsg = function (id,msg) {
        if (ws.readyState === ws.OPEN) {
            ws.send(id+"@"+msg);//调用后台handleTextMessage方法
            //发送成功
        } else {
            layer.msg('连接失败:'+ws.readyState);
        }
    };

    function onOpen(openEvt) {
        self.connect = true;
        if(self.timeoutObj!==null)clearInterval(self.timeoutObj);
        if(index!==null){
            layer.close(index);
            index = null;
            layer.msg('重新连接服务器成功');
        }
    }
    // 接收后台发送的数据
    function onMessage(evt) {
        var array  = evt.data.split('@');
        if(array.length>=2) {
             layer.open({
                id: 'loginWarn',
                type: 1,
                title: '登录警告',
                skin: 'layui-layer-admin layui-layer-white', //样式类名
                area: ['300px', '200px'],
                closeBtn: 0, //不显示关闭按钮
                blackBtn: 1, //我的自定义关闭按钮 1开启
                anim: 2,
                shadeClose: false, //开启遮罩关闭
                content: '<div class="layui-col-md12">' +
                    '      <div class="layui-card">' +
                    '        <div class="layui-card-header">' + array[0] + '</div>' +
                    '        <div class="layui-card-body">' +
                    '        </div>' +
                    '      </div>' +
                    '    </div>'
                ,
                success: function (layero, index) {    //成功获得加载html时，预先加载，将值从父窗口传到 子窗口

                },
                end: function () {
                    window.location.href = "/login";
                }
            });
        }else {
            layer.msg(evt.data, {
                offset: 't',
                anim: 3
            });
            system.dot();
        }
    }
    function onError() {

    }
    function onClose() {
        if(index!=null)return;
        self.connect = false;
        index = layer.open({
            id:'serverClose',
            type: 1,
            title: '服务器开小差了',
            skin: 'layui-layer-admin layui-layer-white', //样式类名
            area: ['300px', '200px'],
            closeBtn: 0, //不显示关闭按钮
            blackBtn: 0, //我的自定义关闭按钮 1开启
            anim: 2,
            shadeClose: false, //开启遮罩关闭
            content: '<div class="layui-col-md12">' +
                '      <div class="layui-card">' +
                '        <div class="layui-card-header">正在重新连接...</div>' +
                '        <div class="layui-card-body">' +
                '           <div class="layui-progress" lay-filter="pro">' +
                '               <div class="layui-progress-bar" lay-percent="10%"></div>' +
                '           </div>' +
                '        </div>' +
                '      </div>' +
                '    </div>' +
                '<script type="text/javascript">' +
                'layui.use([\'element\',\'websocket\'], function(){' +
                '  var element = layui.element,' +
                '   websocket = layui.websocket;' +
                '      var n = 0, timer = setInterval(function(){' +
                '        n = n + Math.random()*20|0;  ' +
                '        if(n>100){' +
                '          n = 100;' +
                '           if(!websocket.connect){' +
                '           n = 0;' +
                '           websocket.start();'+
                '           }else{' +
                '            clearInterval(timer);' +
                '           }' +
                '       }' +
                '        element.progress(\'pro\', n+\'%\');' +
                '      }, 1000);' +
                '      '+
                '});' +
                '</script>',
            success: function (layero, index) {    //成功获得加载html时，预先加载，将值从父窗口传到 子窗口

            },
            end:function () {

            }
        });
        layui.sessionData('online', {
            key: 'status'
            ,value: 'false'
        });
    }
    self.reconnect = function(){
        self.timeoutObj = setInterval(function(){
            self.start();
        },5000);
    };
    exports('websocket', self);
});