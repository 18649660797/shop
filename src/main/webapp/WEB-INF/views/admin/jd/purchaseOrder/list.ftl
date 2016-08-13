<html>
<head></head>
<body>
<div class="container">
    <div class="row">
        <div class="span24">
            <form id="searchForm" class="form-horizontal" tabindex="0" style="outline: none;">
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">订单号：</label>
                        <div class="controls">
                            <input type="text" name="like_orderNumber" class="control-text">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">分配机构：</label>
                        <div class="controls">
                            <select name="eq_wareHouse.id">
                                <option value="">全部</option>
                            <#if wareHouses??>
                                <#list wareHouses as wareHouse>
                                    <option value="${(wareHouse.id)}">${(wareHouse.city)!}</option>
                                </#list>
                            </#if>
                            </select>
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
        <script type="text/javascript">
            BUI.use(['common/grid/format', 'common/search', 'common/page'], function(Format, Search, Page) {
                var editing = new BUI.Grid.Plugins.CellEditing({
                    triggerSelected : false //触发编辑的时候不选中行
                });
                var columns = [
                        {title:'订单ID',dataIndex:'id',width:80,renderer:function(v){
//                            return Search.createLink({
//                                id : 'product_edit_' + v,
//                                title : '编辑订单信息',
//                                text : '编辑',
//                                href : '/admin/jd/purchaseOrder/edit/' + v
//                            });
                            return v;
                        }},
                        {title:'订单编号',dataIndex:'orderNumber',width:100},
                        {title:'采购总价',dataIndex:'total',width:100,renderer:Format.renderMoney},
                        {title:'采购总数',dataIndex:'count',width:80},
                        {title:'分配机构',dataIndex:'city',width:80},
                        {title:'联系人',dataIndex:'contact',width:100},
                        {title:'联系方式',dataIndex:'telephone',width:200},
                        {title:'采购人',dataIndex:'buyer',width:60},
                        {title:'供应商',dataIndex:'provider',width:200},
                        {title:'仓库',dataIndex:'warehouse',width:150},
                        {title:'详细地址',dataIndex:'detailAddress',width:500}
                    ],
                    store = Search.createStore('/admin/jd/purchaseOrder/list', {
                        proxy : {
                            method : 'post'
                        },
                        pageSize: 10
                    }),
                    gridCfg = Search.createGridCfg(columns,{
                        tbar : {
                            items : [
                                {text : '<i class="icon-edit"></i>导入',btnCls : 'button button-small',handler:function(){
                                    var dialog = new top.BUI.Overlay.Dialog({
                                        title: '导入采购单',
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
                                                url : '/admin/jd/purchaseOrder/import',
                                                secureuri: false,
                                                fileElementId: "file",
                                                dataType : 'json',
                                                method : 'get',
                                                success: function (data) {
                                                    var w = top.window.open("/admin/jd/purchaseOrder/preview")
                                                    w.beforeunload =  function() {
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
                                }},
                                {text : '<i class="icon-plus"></i>导出',btnCls : 'button button-small',handler : excel},
                                {text : '<i class="icon-remove"></i>删除',btnCls : 'button button-small',handler : deleteRecord},
                                {text : '<i class="icon-remove"></i>删除全部',btnCls : 'button button-small',handler : deleteAllRecord}
                            ]
                        },
                        plugins : [editing, BUI.Grid.Plugins.CheckSelection] // 插件形式引入多选表格
                    });
                var search = new Search({
                    store : store,
                    gridCfg : gridCfg,
                    formId: 'searchForm',
                    btnId: 'btnSearch'
                }),
                grid = search.get('grid');
                function deleteRecord() {
                    var selections = grid.getSelection();
                    var ids = [];
                    for (var key in selections) {
                        ids.push(selections[key].id);
                    }
                    if (ids.length == 0) {
                        return edy.alert("请选择要删除的记录!");
                    }
                    edy.loading();
                    $.post("/admin/jd/purchaseOrder/delete", {in_id: ids.join(",")}, function(data) {
                        if (edy.ajaxHelp.handleAjax(data)) {
                            edy.alert("删除成功!");
                            store.load();
                            edy.loaded();
                        }
                    });
                }
                function deleteAllRecord() {
                    edy.loading();
                    $.post("/admin/jd/purchaseOrder/delete", {}, function(data) {
                        if (edy.ajaxHelp.handleAjax(data)) {
                            edy.alert("删除成功!");
                            store.load();
                            edy.loaded();
                        }
                    });
                }
                function excel() {
                    window.open("/admin/jd/purchaseOrder/excel?" + $("#searchForm").serialize());
                }
            });
        </script>
    </div>
</div>
</body>
</html>