(function(w, $) {
    function edy() {

    }
    edy.alert = function(msg) {
        return w.BUI && w.BUI.Message && w.BUI.Message.Alert && w.BUI.Message.Alert(msg) || alert(msg);
    };

    edy.confirm = function(msg, callback) {
        return w.BUI && w.BUI.Message && w.BUI.Message.Confirm && w.BUI.Message.Confirm(msg, callback) || (function(m, cb) {
            var result = confirm(msg);
            if (result && typeof callback === "function") {
                callback();
            }
        } ());
    };
    edy.ajaxHelp = {
        handleAjax: function(data) {
            if (!data || !data.result) {
                edy.alert(data.message);
                return false;
            }
            return true;
        }
    };
    edy.grid = {
        format: {
            renderStatus: function(val, row) {
                return val && val.label || val || "";
            }
        }
    }
    edy.rendererHelp = {
        createLink: function(href, text) {
            return "<a href='{0}'>{1}</a>".replace("{0}", href || "").replace("{1}", text || "");
        },
        createJavaScriptLink: function(prefix, val, text) {
            return "<a href='javascript:void(0)' data-{0}='{2}'>{1}</a>".replace("{0}", prefix || "").replace(/\{1}/g, text || "").replace(/\{2}/g, val);;
        }
    };
    edy.getSuggestGridHeight = function() {
        return $("body").height() - $(".row:first-child").height();
    };
    edy.fullMask = new BUI.Mask.LoadMask({
        el : 'body',
        msg : 'loading'
    });
    edy.loading = function() {
        this.fullMask.show();
    };
    edy.loaded = function() {
        this.fullMask.hide();
    };
    w.edy = edy;
} (window, jQuery));