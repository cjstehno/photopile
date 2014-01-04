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
            evt.preventDefault();

            var $target = $(evt.currentTarget);

            this.trigger('app-menu-click', {
                'item-id': $target.attr('href').substring(1)
            });

            this.$('li > a.app-menu').parent('li').removeClass('active');

            $target.parent('li').addClass('active');
        }
    });
});