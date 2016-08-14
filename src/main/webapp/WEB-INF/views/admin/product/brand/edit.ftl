<html>
<head></head>
<body>
<div class="container">
    <form id="J_Form" action="/admin/product/brand/edit/${(brand.id)?default(-1)}" method="post" class="form-horizontal">
        <div class="panel">
            <div class="panel-body">
                <div class="control-group">
                    <label class="control-label">品牌名称：</label>
                    <div class="controls">
                        <input name="name" data-rules="{required : true}" value="${(brand.name)!}"/>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
