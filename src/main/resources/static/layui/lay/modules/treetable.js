layui.define(['layer', 'table'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var table = layui.table;
    var treetable = {
        // 渲染树形表格
        render: function (param) {
            // 检查参数
            if (!treetable.checkParam(param)) {
                return;
            }
            // 获取数据
            if (param.data) {
                treetable.init(param, param.data);
            } else {
                var page = {'data':param.where};
                $.post(param.url,page,function (res) {
                    treetable.init(param, res);
                })
            }
        },
        // 渲染表格
        init: function (param, data) {
            var mData = [];
            var doneCallback = param.done;
            var tNodes = data.data;
            // 补上id和pid字段
            for (var i = 0; i < tNodes.length; i++) {
                var tt = tNodes[i];
               // tt.id = tt[param.treeIdName];
                if (!tt.id) {
                    if (!param.treeIdName) {
                        layer.msg('参数treeIdName不能为空', {icon: 5});
                        return;
                    }
                    tt.id = tt[param.treeIdName]
                }
                if (!tt.pid) {
                    if (!param.treePidName) {
                        layer.msg('参数treePidName不能为空', {icon: 5});
                        return;
                    }
                    tt.pid = tt[param.treePidName];
                }
            }
            // 对数据进行排序
            var sort = function (s_pid, data) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].pid == s_pid) {
                        var len = mData.length;
                        if (len > 0 && mData[len - 1].menuId == s_pid) {
                            mData[len - 1].isParent = true;
                        }
                        mData.push(data[i]);
                        sort(data[i].menuId, data);
                    }
                }
            };
            sort(param.treeSpid, tNodes);

            // 重写参数
            param.url = undefined;
            param.data = mData;
            param.cols[0][param.treeColIndex].templet = function (d) {
                var mId = d.menuId;
                var mPid = d.pid;
                var isDir = d.isParent;
                var emptyNum = treetable.getEmptyNum(mPid, mData);
                var iconHtml = '';
                for (var i = 0; i < emptyNum; i++) {
                    iconHtml += '<span class="treeTable-empty"></span>';
                }
                if (isDir) {
                    //父层
                    iconHtml += '<i class="layui-icon layui-icon-triangle-d"></i> <i class="layui-icon"></i>';
                } else {
                    //子层
                    iconHtml += '<i class="layui-icon "></i>';
                }
                iconHtml += '&nbsp;&nbsp;';
                var celType = isDir ? 'dir' : 'file';
                var vg = '<span class="treeTable-icon open" lay-tid="' + mId + '" lay-cel-pid="' + mPid + '" lay-cel-type="' + celType + '">';
                return vg + iconHtml + d[param.cols[0][param.treeColIndex].field] + '</span>'
            };

            param.done = function (res, curr, count) {
                $(param.elem).next().addClass('treeTable');
                //$('.treeTable .layui-table-page').css('display', 'none');
                $(param.elem).next().attr('treeLinkage', param.treeLinkage);
                if (param.treeDefaultClose) {
                    treetable.foldAll(param.elem);
                }
                res.allData = mData;
                res.limit = param.limit;
                res.page = param.page;
                if (doneCallback) {
                    doneCallback(res, curr, count);
                }
            };
            // 渲染表格
            table.render(param);
        },

        // 计算缩进的数量
        getEmptyNum: function (pid, data) {
            var num = 0;
            if (!pid) {
                return num;
            }
            var tPid;
            for (var i = 0; i < data.length; i++) {
                if (pid == data[i].menuId) {
                    num += 1;
                    tPid = data[i].pid;
                    break;
                }
            }
            var number = treetable.getEmptyNum(tPid, data);
            return num + number;
        },

        // 检查参数
        checkParam: function (param) {
            if (!param.treeSpid && param.treeSpid != 0) {
                layer.msg('参数treeSpid不能为空', {icon: 5});
                return false;
            }

            if (!param.treeColIndex && param.treeColIndex != 0) {
                layer.msg('参数treeColIndex不能为空', {icon: 5});
                return false;
            }
            return true;
        },
        // 展开所有
        expandAll: function (dom) {
            $(dom).next('.treeTable').find('.layui-table-body tbody tr').each(function () {
                var $ti = $(this).find('.treeTable-icon');
                var celType = $ti.attr('lay-cel-type');
                var tOpen = $ti.hasClass('open');
                if ('dir' === celType && !tOpen) {
                    $ti.trigger('click');
                }
            });
        },
        // 折叠所有
        foldAll: function (dom) {
            $(dom).next('.treeTable').find('.layui-table-body tbody tr').each(function () {
                var $ti = $(this).find('.treeTable-icon');
                var celType = $ti.attr('lay-cel-type');
                var tOpen = $ti.hasClass('open');
                if ('dir' === celType && tOpen) {
                    $ti.trigger('click');
                }
            });
        },
        // 点击单元格展开/折叠行
        cellToggleRows: function ($dom, linkage) {
            var type = $dom.attr('lay-cel-type');
            if ('file' === type) return;
            var mId = $dom.attr('lay-tid');
            var isOpen = $dom.hasClass('open');
            if (isOpen)  $dom.removeClass('open');
            else $dom.addClass('open');
            $dom.closest('tbody').find('tr').each(function () {
                var $ti = $(this).find('.treeTable-icon');
                var pid = $ti.attr('lay-cel-pid');
                var celType = $ti.attr('lay-cel-type');
                var tOpen = $ti.hasClass('open');
                if (mId === pid) {
                    if (isOpen) {
                        $(this).hide();
                        if ('dir' === celType && tOpen === isOpen)
                            treetable.cellToggleRows($ti, true);
                    }else {
                        $(this).show();
                        if (linkage && 'dir' === celType && tOpen === isOpen)
                            treetable.cellToggleRows($ti,true);
                    }
                }
            });
        },
        // 点击图标展开/折叠行
        // toggleRows: function ($dom, linkage) {
        //     var type = $dom.attr('lay-cel-type');
        //     if ('file' === type) {
        //         return;
        //     }
        //     var mId = $dom.attr('lay-tid');
        //     var isOpen = $dom.hasClass('open');
        //     if (isOpen) {
        //         $dom.removeClass('open');
        //     } else {
        //         $dom.addClass('open');
        //     }
        //     $dom.closest('tbody').find('tr').each(function () {
        //         var $ti = $(this).find('.treeTable-icon');
        //         var pid = $ti.attr('lay-cel-pid');
        //         var celType = $ti.attr('lay-cel-type');
        //         var tOpen = $ti.hasClass('open');
        //         if (mId === pid) {
        //             if (isOpen) {
        //                 $(this).hide();
        //                 if ('dir' === celType && tOpen === isOpen) {
        //                     $ti.trigger('click');
        //                 }
        //             } else {
        //                 $(this).show();
        //                 if (linkage && 'dir' === celType && tOpen === isOpen) {
        //                     $ti.trigger('click');
        //                 }
        //             }
        //         }
        //     });
        // }
    };

    //给单元格列绑定事件
    $('body').on('click', '.treeTable .layui-table-cell', function () {
        var treeLinkage = $(this).parents('.treeTable').attr('treeLinkage');
        if ('true' === treeLinkage) {
            treetable.cellToggleRows($(this).find('.treeTable-icon'), true);
        } else {
            treetable.cellToggleRows($(this).find('.treeTable-icon'), false);
        }
    });

    // 给图标列绑定事件 (给单元格列绑定事件不可共存);
    // $('body').on('click', '.treeTable .treeTable-icon', function () {
    //     var treeLinkage = $(this).parents('.treeTable').attr('treeLinkage');
    //     if ('true' == treeLinkage) {
    //         treetable.toggleRows($(this), true);
    //     } else {
    //         treetable.toggleRows($(this), false);
    //     }
    // });
    exports('treetable', treetable);
});
