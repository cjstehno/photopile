define([
    'views/menubar',
    'views/pages/gallery',
    'text!templates/application.html'
], function (MenuBar, GalleryPage, template) {

    return Backbone.View.extend({
        template: _.template(template),

        initialize: function () {
            this.on('page-change', _.bind(this.onPageChanged, this));
        },

        render: function () {
            this.$el.prepend(this.template());

            var menuBar = new MenuBar({el: this.$('.navbar')}).render();
            this.on('page-change', _.bind(menuBar.onPageChange, menuBar));

            return this;
        },

        onPageChanged: function (evt) {
            /// FIXME: this should probably be in a map or something
            if (evt.id == 'gallery') {
                new GalleryPage({el: this.$('div.page-content')}).render();
            }
        }
    });
});
