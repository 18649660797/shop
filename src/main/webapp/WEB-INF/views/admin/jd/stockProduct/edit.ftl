<html>
<head></head>
<body>
<div class="container">
    <form id="J_Form" action="/admin/jd/stockProduct/edit/${(product.id)?default(-1)}" method="post" class="form-horizontal">
        <div class="panel">
            <div class="panel-body">
                <div class="control-group">
                    <label class="control-label">商品名称：</label>
                    <div class="controls">
                        <input name="name" data-rules="{required : true}" value="${(product.defaultSku.name)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><s>*</s>销售价格：</label>
                    <div class="controls">
                        <input name="salePrice" value="${(product.defaultSku.salePrice.amount)!}" type="text" class="control-text" data-rules="{required : true}">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><s>*</s>商品编号：</label>
                    <div class="controls">
                        <input type="text" name="commodityCode" value="${(product.defaultSku.commodityCode)!}" class="control-text" data-rules="{required : true}">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><s>*</s>品牌：</label>
                    <div class="controls bui-form-group-select">
                        <select name="brandId">
                            <#if brandList??>
                                <#list brandList as brand>
                                    <option value="${(brand.id)!}">${(brand.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
