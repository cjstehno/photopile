define([
    'text!templates/pages/gallery.html'
], function (template) {

    return Backbone.View.extend({
        template: _.template(template),

        render: function () {
            this.$el.prepend(this.template());

            return this;
        }
    });
});
