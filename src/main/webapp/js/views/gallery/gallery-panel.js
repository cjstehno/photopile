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
    'text!templates/gallery/gallery-panel.html',
    'text!templates/gallery/gallery-photo.html'
], function( Photos, panel, photoTemplate ){

    var FETCH_LIMIT = 12;

    return Backbone.View.extend({
        tpt: _.template(panel),
        photoTpt: _.template(photoTemplate),

        currentOffset:0,

        events:{
            'click .more-bar':'onMore'
        },

        initialize:function(){
            this.collection = new Photos();
        },

        onMore:function(){
            this.requestPhotos( this.currentOffset + FETCH_LIMIT );
            /*
                request more
                are there still more?
                    y: keep button available
                    n: disable button
             */
        },

        onContentChange:function(){
            this.collection.each(function(it){
                this.$('.thumbnails').append( this.photoTpt(it) );
            }, this);
        },

        render:function(){
            this.$el.append( this.tpt() );

            this.requestPhotos(0);

            return this;
        },

        requestPhotos:function( offset ){
            var that = this;

            var changer = _.bind(this.onContentChange, this)

            this.collection.fetch({
                contentType:'application/json',
                data:{ start:offset, limit:FETCH_LIMIT },
                success:function(){
                    that.currentOffset = offset;
                    changer();
                }
            });
        }
    });
});