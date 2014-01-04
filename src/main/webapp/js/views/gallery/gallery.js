define([
    'text!templates/gallery/gallery.html'
], function( template ){

    var itemsPerPage = 24; // TODO: externalize at some point

    return Backbone.View.extend({
        template: _.template(template),

        initialize:function(){
            this.collection.on('reset', _.bind(this.render, this));
        },

        render:function(){
            this.$el.empty();

            var limit = itemsPerPage / 4;

            for( var i=0; i<6; i++ ){
                var start = i * limit;

                this.$el.append( this.template({
                    photos: this.collection,
                    start: start,
                    end: start + limit
                }));
            }

            return this;
        }
    });
});