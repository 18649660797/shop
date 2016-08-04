<!doctype html>
<html>
<head>
    <title><sitemesh:write property="title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1"> -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/static/ab-ui/css/lib/bootstrap.css"/>
    <script src="/static/ab-ui/js/lib/angular.js"></script>
    <script src="/static/ab-ui/js/lib/ui-bootstrap-tpls-1.3.3.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
    <script src="/static/js/wx/wx.js"></script>
    <script>
        angular.module('nav-top-app', ['ui.bootstrap.collapse']).controller('CollapseDemoCtrl', function ($scope) {
            $scope.isCollapsed = true;
        });
    </script>
    <sitemesh:write property="head"/>
</head>
<body>
<nav ng-app="nav-top-app" class="navbar navbar-inverse navbar-static-top">
    <div class="container-fluid" ng-controller="CollapseDemoCtrl">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" ng-click="isCollapsed = !isCollapsed">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Gabin & Edy</a>
        </div>
        <div uib-collapse="isCollapsed">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">联系我们</a></li>
                <li><a href="/login">登录</a></li>
                <li><a href="#">注册</a></li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>
<sitemesh:write property="body"/>
<div class="container-fluid navbar navbar-inverse navbar-fixed-bottom">
    <div class="row">
        <div class="col-xs-12"><h5 class="text-center">CopyRight by gabin 2016</h5></div>
    </div>
</div>
<sitemesh:write property="script"/>
</body>
</html>