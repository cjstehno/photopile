define([], function(){
    return Backbone.Model.extend({
        idAttribute: "id",

        url:function(){
            return '/user' + (_.isUndefined(this.id) ? '' : ('/' + this.id));
        }
    });
});