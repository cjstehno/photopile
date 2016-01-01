define([
    'text!templates/menubar.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        render:function(){
            this.$el.prepend( this.template() );
            return this;
        },

        onPageChange:function(evt){
            // deactivate the current active
            this.$('li.active').removeClass('active');

            // activate the new active
            this.$('li > a[href="#' + evt.id + '"]').parent().addClass('active');
        }
    });
});
