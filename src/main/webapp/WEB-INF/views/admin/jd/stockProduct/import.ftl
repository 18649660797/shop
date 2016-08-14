<!DOCTYPE html>
<html>
<head></head>
<body>
<div class="container">
    <div class="row">
        <form id="J_Form" action="/admin/jd/stockProduct/import" method="post" enctype="multipart/form-data" class="form-horizontal">
            <div class="control-group">
                <div class="controls">
                    <input type="file" name="file" id="file" data-rules="{required:true}" /><br>
                </div>
                <div class="control-group">
                    <#--<a target="_blank" href="/leave/demo">模板文件</a>-->
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    (function($) {
        $(function() {
            var Form = BUI.Form;
            new Form.Form({
                srcNode : '#J_Form',
            }).render();
        });
    } (jQuery));
</script>
</body>
</html>