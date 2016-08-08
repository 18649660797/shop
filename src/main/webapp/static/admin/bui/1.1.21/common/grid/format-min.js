define('common/grid/format',[],function (require) {
    var BUI = require('bui/common');

    function Format(config){
        Dialog.superclass.constructor.call(this, config);
        this._init();
    }

    Format.ATTRS = {
        /**
         * 是否自动查询，打开页面时未点击查询按钮时是否自动查询
         * @type {Boolean}
         */
        closeAction :{
            value : "destroy"
        },
        success :{
            value : function() {

            }
        }
    };

    BUI.extend(Format, BUI.Base);

    BUI.augment(Format, {
        _init: function () {
            var _self = this;
            _self._initEvent();
        },
        //初始化事件
        _initEvent: function () {

        }
    });

    Format.renderMoney = function (value, rowObj) {
        if (!value) {
            return "--";
        }
        if (!isNaN(value)) {
            return "￥" + (value || 0);
        }
        return "￥" + (value.amount || 0);
    };
    return Format;
});