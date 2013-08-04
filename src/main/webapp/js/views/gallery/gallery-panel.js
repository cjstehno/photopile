/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

define([
    'collections/photos',
    'views/gallery/pager',
    'views/gallery/gallery-breadcrumbs',
    'views/gallery/gallery-photo',
    'text!templates/gallery/gallery-panel.html',
    'text!templates/gallery/gallery-row.html'
], function( Photos, Pager, Breadcrumbs, GalleryPhoto, panel, photoTemplate ){

    return Backbone.View.extend({
        template: _.template(panel),

        events:{
            'click img.gallery-photo':'onPhotoClick'
        },

        initialize:function(){
            this.collection = new Photos();
            this.collection.on('reset', _.bind(this.renderPhotos, this));
        },

        render:function(){
            this.$el.append( this.template() );

            this.breadcrumbs = new Breadcrumbs({ el:this.$('.breadcrumbs') });
            this.breadcrumbs.on('filter-change', _.bind(this.onFilterChange, this));

            this.pager = new Pager({ el:this.$('.gallery-pages') });
            this.pager.on('page-change', _.bind(this.onPageChange, this));

            this.collection.fetchPage( this.pager.getCurrent(), this.breadcrumbs.getCurrent() );

            this.$('.carousel').carousel('pause');

            return this;
        },

        onFilterChange:function( filter ){
            this.collection.fetchPage( this.pager.getCurrent(), filter );
        },

        onPageChange:function( page ){
            this.collection.fetchPage( page, this.breadcrumbs.getCurrent() );
        },

        renderPhotos:function(){
            this.breadcrumbs.render();
            this.pager.render( this.collection.total );

            var photoRoot = this.$('.gallery-content');
            photoRoot.empty();

            var limit = this.pager.itemsPerPage / 3;

            for( var i=0; i<3; i++ ){
                var start = i * limit;

                photoRoot.append( _.template(photoTemplate, {
                    photos: this.collection,
                    start: start,
                    end: start + limit
                }) );
            }
        },

        onPhotoClick:function(evt){
            var photoId = $(evt.target).attr('data-id');

            var galleryPhoto = new GalleryPhoto({
                el:this.$('.item.photo-item'),
                model:this.collection.get(photoId)
            });
            galleryPhoto.on('photo-close',function(){
                // TODO: should dispose of the photo objects on close
                this.$('.carousel').carousel('prev');
            }, this);
            galleryPhoto.render();

            this.$('.carousel').carousel('next');

            return false;
        }
    });
});