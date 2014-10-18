define([
    'models/photo',
    'text!templates/gallery/thumbnail-details.html'
], function( Photo, template ){

    return Backbone.View.extend({
        template: _.template(template),

        attributes:{
            class:'modal fade'
        },

        initialize:function( options ){
            this.photo = new Photo({ id:options.photoId });
            this.photo.fetch({
                // TODO: I thought the url and reset were automatic?
                url:'/photopile/photos/' + options.photoId,
                reset:true,
                contentType:'application/json',
                success: _.bind(this.render,this)
            });
        },

        render:function(){
            this.$el.html( this.template(this.photo.toJSON()) );
            this.$el.modal();

            return this;
        }

    });
});

