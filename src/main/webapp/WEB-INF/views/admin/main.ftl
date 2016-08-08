<!DOCTYPE html>
<html>
<body>
<div class="header">
    <div class="dl-title">
        <a href="http://www.builive.com/" title="文档库地址" target="_blank">
            <span class="lp-title-port">商城后台</span><span class="dl-title-text"></span>
        </a>
    </div>
    <div class="dl-log">
        欢迎您<span class="dl-log-user"></span>
        <a href="/admin/logout" class="dl-log-quit">[退出]</a>
        <a href="http://http//www.builive.com/" title="文档库" class="dl-log-quit">文档库</a>
    </div>
</div>
<div class="content">
    <div class="dl-main-nav">
        <ul id="J_Nav" class="nav-list ks-clear">
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-order">订单</div>
                <div class="nav-item-mask"></div>
            </li>
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-product">商品</div>
                <div class="nav-item-mask"></div>
            </li>
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-supplier">会员</div>
                <div class="nav-item-mask"></div>
            </li>
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-supplier">京东</div>
                <div class="nav-item-mask"></div>
            </li>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten"></ul>
</div>
<script>
    BUI.use('common/main', function () {
        var config = [{
            id: 'order',
            homePage: 'order',
            menu: [{
                text: '订单',
                items: [
                    {id: 'order', text: '所有订单', href: '/'},
                ]
            }]
        }, {
            id: 'product',
            homePage: 'product',
            menu: [{
                text: '所有商品',
                items: [
                    {id: 'product', text: '所有商品', href: '/admin/product/basic/list'},
                ]
            }, {
                text: '商品属性',
                items: [
                    {id: 'productOption', text: '商品规格', href: '/'},
                    {id: 'productBrand', text: '商品品牌', href: '/admin/product/brand/list'},
                ]
            }]
        }, {
            id: 'customer',
            homePage: 'customer',
            menu: [{
                text: '会员',
                items: [
                    {id: 'customer', text: '所有会员', href: '/'},
                ]
            }]
        }, {
            id: 'jd',
            homePage: 'stockProduct',
            menu: [{
                text: '京东采购对接',
                items: [
                    {id: 'stockProduct', text: '仓储商品', href: '/admin/jd/stockProduct/list'}
                ]
            }, {
                text: '京东采购单',
                items: [
                    {id: 'purchaseOrder', text: '采购单', href: '/admin/jd/purchaseOrder/list'}
                ]
            }]
        }];
        new PageUtil.MainPage({
            modulesConfig: config
        });
    });
</script>
</body>
</html>