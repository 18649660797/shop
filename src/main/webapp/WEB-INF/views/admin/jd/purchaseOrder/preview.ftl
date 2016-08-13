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
                {title: '订单号', /*editor : {xtype : 'text', rules: {require:true}}, */dataIndex: 'orderNumber', width: 100},
//                {title: '供应商简码', dataIndex: 'providerCN', width: 100},
//                {title: '供应商名称', dataIndex: 'providerName', width: 100},
                {title: '商品编码', dataIndex: 'commodityCode', width: 100},
//                {title: '商品名称', dataIndex: 'skuName', width: 100, renderer: function(val) {
//                    return '<label title="{val}">{label}</label>'.replace('{val}', val).replace('{label}', val.substring(0 ,5) + "...")
//                }},
//                {title: '订单属性', dataIndex: 'attribute', width: 100},
                {title: '分配机构', dataIndex: 'city', width: 100},
//                {title: '仓库', dataIndex: 'warehouseName', width: 100},
//                {title: '详细地址', dataIndex: 'detailAddress', width: 100},
//                {title: '联系人', dataIndex: 'link', width: 100},
//                {title: '联系方式', dataIndex: 'tel', width: 100},
//                {title: '采购价格', dataIndex: 'takePrice', width: 100},
                {title: '采购数量', dataIndex: 'itemCount', width: 100},
//                {title: '实收数量', dataIndex: 'realItemCount', width: 100},
//                {title: '采购金额', dataIndex: 'total', width: 100},
//                {title: '实际金额', dataIndex: 'realTotal', width: 100},
//                {title: '采购员', dataIndex: 'buyer', width: 100},
                {title: '订购时间', dataIndex: 'orderTime', width: 150, renderer: Grid.Format.datetimeRenderer},
//                {title: '入库到期时间', dataIndex: 'overdueDate', width: 100, renderer: Grid.Format.dateRenderer}
            ];
            var store = new BUI.Data.Store({
                    url : '/admin/jd/purchaseOrder/previewCheck',
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
                                    edy.loading();
                                    $.get("/admin/jd/purchaseOrder/check", function (data) {
                                        if (edy.ajaxHelp.handleAjax(data)) {
                                            window.close();
                                            edy.loaded();
                                        }
                                    });
                                }
                            }
                        }, {text: '<div class="tips tips-small tips-notice bui-inline-block"><span class="x-icon x-icon-small x-icon-warning"><i class="icon icon-volume-up"></i></span><div class="tips-content">核对数据</div></div>'
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