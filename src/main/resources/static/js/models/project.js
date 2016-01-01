define([], function () {
    return Backbone.Model.extend({
        idAttribute: 'id',

        url: function () {
            return '/project' + (_.isUndefined(this.id) ? '' : ('/' + this.id));
        }
    });
});