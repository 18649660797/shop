<!DOCTYPE html>
<html lang="en">
<head>
    <style>
        .bui-simple-list.bui-select-list ul{
            max-height: 500px;
            overflow-y: auto;
        }
    </style>
</head>
<body>
<div class="row">

</div>
<div id="grid"></div>
<script>
    (function($) {
        BUI.use(['bui/grid'], function (Grid) {
            var editing = new Grid.Plugins.CellEditing({
                triggerSelected : false //触发编辑的时候不选中行
            });
            var columns = [
                {title: '序号', editor : {xtype : 'text', rules: {require:true}}, dataIndex: 'num', width: 250},
                {title: '品牌', editor : {xtype : 'text'}, dataIndex: 'brandName', width: 250},
                {title: '商品编码', editor : {xtype : 'text'}, dataIndex: 'commodityCode', width: 250},
                {title: '产品名称', editor : {xtype : 'text'}, dataIndex: 'skuName', width: 250},
                {title: '箱规', editor : {xtype : 'text'}, dataIndex: 'boxSku', width: 250},
            ];
            var store = new BUI.Data.Store({
                    url : '/admin/jd/stockProduct/previewCheck',
                    autoLoad:true, //自动加载数据
                    pageSize:1000	// 配置分页数目
                }),
                grid = new Grid.Grid({
//                    height: edy.getSuggestGridHeight(),
                    render:'#grid',
                    columns : columns,
                    loadMask: true, //加载数据时显示屏蔽层
                    store: store,
                    // 底部工具栏
                    bbar:{
                        pagingBar:true
                    },
                    tbar:{
                        items : [{
                            btnCls : 'button button-small',
                            text : '<i class="icon-plus"></i>确认提交',
                            listeners : {
                                'click' : function() {
                                    var data = store.getResult();
                                    $.post("/admin/jd/stockProduct/check", {jsonData: JSON.stringify({data: data})}, function(data) {
                                        if (edy.ajaxHelp.handleAjax(data)) {
                                            window.close();
                                        }
                                    });
                                }
                            }
                        }, {text: '<div class="tips tips-small tips-notice bui-inline-block"><span class="x-icon x-icon-small x-icon-warning"><i class="icon icon-volume-up"></i></span><div class="tips-content">请核对清楚是否有数据缺失,或异常.再确认提交,可以通过字段排序排查</div></div>'
                            ,  xclass:'bar-item-text'
                        }]
                    },
                    plugins : [editing,Grid.Plugins.CheckSelection,Grid.Plugins.ColumnResize]
                });
            grid.render();

        });
    } (jQuery));
</script>
</body>
</html>