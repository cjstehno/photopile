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
    'text!templates/gallery-panel.html',
    'collections/photos',
    'text!templates/gallery-photo.html'
], function( panel, Photos, photoTemplate ){

    return Backbone.View.extend({
        tpt: _.template(panel),
        photoTpt: _.template(photoTemplate),

        initialize:function(){
            this.collection = new Photos();
            this.collection.on('reset', _.bind(this.onContentChange, this));
        },

        onContentChange:function(){
            this.collection.each(function(it){
                this.$('.thumbnails').append( this.photoTpt(it) );
            }, this);
        },

        render:function(){
            this.$el.append( this.tpt() );

            this.collection.fetch({
                contentType:'application/json',
                data:{ start:0, limit:12 },
                reset:true
            });

            return this;
        }
    });
});