define([
    'text!templates/map/container.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        render:function(){
            this.$el.append( this.template() );

            return this;
        }
    });
});