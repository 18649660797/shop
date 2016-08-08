<!doctype html>
<html>
<head>
    <title><sitemesh:write property="title"/></title>
    <link rel="stylesheet" href="/static/admin/bui/1.1.21/css/bs3/dpl-min.css"/>
    <link rel="stylesheet" href="/static/admin/bui/1.1.21/css/bs3/bui-min.css"/>
    <link rel="stylesheet" href="/static/admin/bui/1.1.21/css/main-min.css"/>
    <link rel="stylesheet" href="/static/admin/bui/1.1.21/css/page-min.css"/>
    <link rel="stylesheet" href="/static/admin/css/admin.css"/>
    <style>
        body {overflow: auto;}
    </style>
    <sitemesh:write property="head"/>
</head>
<body>

<script src="/static/admin/bui/jquery-1.8.1.min.js"></script>
<script src="/static/admin/bui/1.1.21/bui-min.js"></script>
<script src="/static/admin/bui/1.1.21/config-min.js"></script>
<script src="/static/admin/bui/1.1.21/prettify.js"></script>
<sitemesh:write property="body"/>
<sitemesh:write property="script"/>
</body>
</html>