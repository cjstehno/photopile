define([], function () {
    return Backbone.Model.extend({
        idAttribute: 'id',

        url: function () {
            return '/photo' + (_.isUndefined(this.id) ? '' : ('/' + this.id));
        }
    });
});