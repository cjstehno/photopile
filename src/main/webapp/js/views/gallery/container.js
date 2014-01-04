define([
    'collections/photos',
    'views/gallery/gallery',
    'views/gallery/filter',
    'views/gallery/pager',
    'text!templates/gallery/container.html'
], function( Photos, GalleryView, GalleryFilter, GalleryPager, template ){

    return Backbone.View.extend({
        template: _.template(template),

        initialize:function(){
            this.collection = new Photos();
//            this.collection.on('reset', _.bind(this.renderPhotos, this));
        },

        render:function(){
            this.$el.append( this.template() );

            /*
                1-up view
                details view
             */

            var filter = new GalleryFilter({ el:'.gallery-filter', collection:this.collection }).render();

            var pager = new GalleryPager({ el:'.gallery-pager', collection:this.collection }).render();

            var gallery = new GalleryView({ el:'.gallery-container', collection:this.collection }).render();

            this.collection.fetchPage( pager.getCurrent(), filter.getCriteria() );

            pager.on('page-change', function(){
                this.collection.fetchPage( pager.getCurrent(), filter.getCriteria() );
            }, this);

            return this;
        }
    });
});