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
    'text!templates/gallery/gallery-panel.html',
    'text!templates/gallery/gallery-photo.html'
], function( Photos, Pager, panel, photoTemplate ){

    return Backbone.View.extend({
        tpt: _.template(panel),
        photoTpt: _.template(photoTemplate),

        events:{
            'click img.gallery-photo':'onPhotoClick'
        },

        initialize:function(){
            this.collection = new Photos();
        },

        render:function(){
            this.$el.append( this.tpt() );

            this.pager = new Pager({ el:this.$('.gallery-pages'), collection: this.collection });
            this.pager.on('gallery:page-change', _.bind(this.requestPhotos, this));

            this.requestPhotos( this.pager.getCurrent() );

            return this;
        },

        requestPhotos:function( page ){
            this.collection.fetch({
                contentType:'application/json',
                data:{
                    start:page.offset,
                    limit:page.pageSize
                },
                success:_.bind(this.renderPhotos, this)
            });
        },

        renderPhotos:function(){
            this.pager.render();

            var photoRoot = this.$('.thumbnails');
            photoRoot.empty();

            this.collection.each(function(it){
                photoRoot.append( this.photoTpt(it) );
            }, this);
        },

        onPhotoClick:function(evt){
            console.log('Someday, I will open a photo...');
        }
    });
});