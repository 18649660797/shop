<html>
<head></head>
<body>
<div class="container">
    <div class="row">
        <div class="span24">
            <form id="searchForm" class="form-horizontal" tabindex="0" style="outline: none;">
                <input type="hidden" name="eq_productType" value="STOCK"/>
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">商品名称：</label>
                        <div class="controls">
                            <input type="text" name="like_defaultSku.name" class="control-text">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">商品编号：</label>
                        <div class="controls">
                            <input type="text" name="like_defaultSku.commodityCode" class="control-text">
                        </div>
                    </div>
                    <div class="form-actions span5">
                        <button id="btnSearch" type="submit" class="button button-primary">搜索</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="search-grid-container">
        <div id="grid">
        </div>
        <script src="http://g.tbcdn.cn/fi/bui/jquery-1.8.1.min.js"></script>
        <script src="http://g.tbcdn.cn/fi/bui/bui-min.js?t=201309041336"></script>
        <script type="text/javascript">
            BUI.use(['common/grid/format', 'common/search', 'common/page'], function(Format, Search, Page) {
                var editing = new BUI.Grid.Plugins.CellEditing({
                    triggerSelected : false //触发编辑的时候不选中行
                }), Grid = BUI.Grid;
                var columns = [
                        {title:'商品编号',dataIndex:'commodityCode',width:100},
                        {title:'商品名称',dataIndex:'name',width:200},
                        {title:'品牌名称',dataIndex:'brandName',width:200},
                        {title:'箱规',dataIndex:'boxSku',width:100},
                        {title:'操作',dataIndex:'id',width:200,renderer : function(value,obj){
//                            var editStr =  Search.createLink({ //链接使用 此方式
//                                id : 'product_edit_' + value,
//                                title : '编辑商品信息',
//                                text : '编辑',
//                                href : '/admin/product/basic/edit/' + value
//                            });
                            var editStr = '<a href="javascript:void(0);" class="btn-edit">编辑</a>';
                            return editStr;
                        }}
                    ],
                    store = Search.createStore('/admin/jd/stockProduct/list', {
                        proxy : {
                            method : 'post'
                        },
                        pageSize: 10
                    }),
                    gridCfg = Search.createGridCfg(columns,{
                        tbar : {
                            items : [
                                {text : '<i class="icon-edit"></i>新增',btnCls : 'button button-small',handler:function() {
                                    edit(-1);
                                }},
                                {text : '<i class="icon-plus"></i>导入',btnCls : 'button button-small',handler:importProduct},
                                {text : '<i class="icon-remove"></i>删除',btnCls : 'button button-small',handler : delFunction},
                                {text : '<i class="icon-remove"></i>删除全部',btnCls : 'button button-small',handler : delAllFunction}
                            ]
                        },
                        plugins : [editing, BUI.Grid.Plugins.CheckSelection, Grid.Plugins.ColumnResize] // 插件形式引入多选表格
                    });
                var search = new Search({
                        store : store,
                        gridCfg : gridCfg,
                        formId: 'searchForm',
                        btnId: 'btnSearch'
                    }),
                    grid = search.get('grid');
                //删除操作
                function delFunction(){
                    var selections = grid.getSelection();
                    var ids = [];
                    for (var key in selections) {
                        ids.push(selections[key].id);
                    }
                    if (ids.length <= 0) {
                        return edy.alert("请选择要操作的记录!");
                    }
                    edy.loading();
                    $.post("/admin/jd/stockProduct/delete", {in_id: ids.join(",")}, function(data) {
                        if (edy.ajaxHelp.handleAjax(data)) {
                            edy.alert("删除成功!");
                            store.load();
                            edy.loaded();
                        }
                    });
                }

                function delAllFunction(){
                    edy.loading();
                    $.post("/admin/jd/stockProduct/delete", {}, function(data) {
                        if (edy.ajaxHelp.handleAjax(data)) {
                            edy.alert("删除成功!");
                            store.load();
                            edy.loaded();
                        }
                    });
                }

                function importProduct() {
                    var dialog = new top.BUI.Overlay.Dialog({
                        title: '导入仓储商品',
                        width:430,
                        height:150,
                        closeAction: "destroy",
                        loader : {
                            url : '/admin/jd/stockProduct/import',
                            autoLoad : false, //不自动加载
                            lazyLoad : false, //不延迟加载
                        },
                        mask:true,
                        success: function() {
                            top.$.ajaxFileUpload({
                                url : '/admin/jd/stockProduct/import',
                                secureuri: false,
                                fileElementId: "file",
                                dataType : 'json',
                                method : 'method',
                                success: function (data) {
                                    var w = top.window.open("/admin/jd/stockProduct/preview")
                                    w.onunload = function() {
                                        store.load();
                                    }
                                },
                                error: function (data, status, e) {

                                }
                            });
                            this.close();
                        }
                    });
                    dialog.show();
                    dialog.get('loader').load()
                }
                //监听事件，删除一条记录
                grid.on('cellclick',function(ev){
                    var sender = $(ev.domTarget); //点击的Dom
                    if(sender.hasClass('btn-edit')){
                        var record = ev.record;
                        edit(record.id);
                    }
                });
                function edit (id) {
                    var form;
                    new BUI.Overlay.Dialog({
                        title: (id && '编辑' || '新增') + '商品',
                        width:800,
                        height:400,
                        closeAction: "destroy",
                        loader : {
                            url : '/admin/product/basic/edit/' + id,
                            autoLoad : true, //不自动加载
                            lazyLoad : false,
                            callback: function() {
                                form = new BUI.Form.Form({
                                    srcNode : '#J_Form',
                                    submitType : 'ajax',
                                    callback : function(data){
                                        if (edy.ajaxHelp.handleAjax((data))) {
                                            edy.alert(data.message || "操作成功");
                                        }
                                    }
                                });
                                form.render();
                            }
                        },
                        mask:true,
                        success: function() {
                            form.submit();
                            this.close();
                        }
                    }).show();

                }
            });
        </script>
    </div>
</div>
</body>
</html>