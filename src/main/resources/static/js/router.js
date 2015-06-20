define(['views/application'], function (ApplicationView) {
    return Backbone.Router.extend({

        routes: {
            'gallery': 'gallery',
            'map': 'map',
            'admin/users': 'users',
            '*path': 'defaultRoute'
        },

        // FIXME: might want to pull applciation view creation out into the app.js file
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

        users:function(){
            this._firePageChange('admin/users');
        },

        _firePageChange:function(pageId){
            this.view.trigger('page-change', {id:pageId});
        }
    });
});