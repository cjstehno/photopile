define(['views/application'], function (ApplicationView) {
    return Backbone.Router.extend({

        routes: {
            'gallery': 'gallery',
            'map': 'map',
            '*path': 'defaultRoute'
        },

        // FIXME: might want to pull application view creation out into the app.js file
        initialize: function () {
            this.view = new ApplicationView({el: 'body'}).render();
        },

        defaultRoute: function () {
            this.navigate('gallery', {trigger: true});
        },

        gallery: function () {
            this._firePageChange('gallery');
        },

        map: function () {
            console.log('Showing map...');
        },

        _firePageChange:function(pageId){
            this.view.trigger('page-change', {id:pageId});
        }
    });
});