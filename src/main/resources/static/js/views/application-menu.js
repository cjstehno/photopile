define([
    'text!templates/application-menu.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click a.app-menu':'onMenuItemClick'
        },

        render:function(){
            this.$el.append( this.template() );

            return this;
        },

        onMenuItemClick:function(evt){
            this.$('li > a.app-menu').parent('li').removeClass('active');

            $(evt.currentTarget).parent('li').addClass('active');
        }
    });
});