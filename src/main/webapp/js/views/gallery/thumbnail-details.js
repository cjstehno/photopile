define([
    'text!templates/gallery/thumbnail-details.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        attributes:{
            class:'modal fade'
        },

        initialize:function( options ){
            this.photoId = options.photoId;
        },

        render:function(){
            this.$el.html( this.template() );
            this.$el.modal();

            console.log( this.photoId );

            return this;
        }

    });
});

