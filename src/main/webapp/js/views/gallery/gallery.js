define([
    'views/gallery/thumbnail-details',
    'text!templates/gallery/gallery.html'
], function( ThumbnailDetailsDialog, template ){

    var itemsPerPage = 24; // TODO: externalize at some point

    var toolsTemplate = '<div class="tools" style="opacity:0.4">' +
        '<span class="glyphicon glyphicon-info-sign" title="Details"></span> ' +
        '</div>';

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'contextmenu .thumbnail':'openThumbnailMenu'
        },

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

            var thumbnails = this.$('.thumbnail');

            thumbnails.colorbox({
                photo: true,
                rel: 'thumbnails',
                maxWidth: '80%',
                maxHeight: '80%'/*,
                onComplete:function(){
                    $('#cboxContent').append('<button type="button" id="foo" style="">??</button>')
                }*/
            });

            thumbnails.append( toolsTemplate );

            this.$('.thumbnail .tools').on('click', _.bind(this.onDetailsClick, this));

            return this;
        },

        onDetailsClick:function(evt){
            evt.preventDefault();
            evt.stopPropagation();

            var photoId = $(evt.currentTarget).prev().attr('data-id');

            new ThumbnailDetailsDialog({ photoId:photoId });
        }

    });
});