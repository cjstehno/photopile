define(['router'], function (Router) {

    var ctor = function () {
    };

    ctor.prototype = {
        initialize: function () {
            new Router();

            Backbone.history.start();
        }
    };

    return ctor;
});
