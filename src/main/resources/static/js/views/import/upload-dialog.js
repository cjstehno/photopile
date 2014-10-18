define([
    'text!templates/import/upload-dialog.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        attributes:{
            class:'modal fade'
        },

        render:function(){
            this.$el.html( this.template() );
            this.$el.modal();

            return this;
        }

    });
});