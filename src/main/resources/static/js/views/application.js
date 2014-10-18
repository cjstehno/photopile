define([
    'views/application-menu',
    'views/dash/container',
    'views/gallery/container',
    'views/map/container',
    'views/import/container',
    'text!templates/application.html'
], function( ApplicationMenu, DashContainer, GalleryContainer, MapContainer, ImportContainer, template ){

    var containers = {
        home:DashContainer,
        gallery:GalleryContainer,
        map:MapContainer,
        'import':ImportContainer
    };

    return Backbone.View.extend({
        template: _.template(template),

        render:function(){
            this.$el.prepend( this.template() );

            var menu = new ApplicationMenu({ el:this.$('.navbar') }).render();

            this.showContainer('home');

            return this;
        },

        showContainer:function( cid ){
            this.$('.container').empty();
            new containers[cid]({ el:this.$('.container') }).render();
        }
    });
});