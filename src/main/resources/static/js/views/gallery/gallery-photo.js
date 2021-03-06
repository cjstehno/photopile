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
    'views/photo-details-dialog',
    'text!templates/gallery/gallery-photo.html'
], function( PhotoDetailsDialog, template ){

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click .back-button':'onCloseClick',
            'click .details-button':'onDetailsClick'
        },

        initialize:function(options){
            this.model = options.model;
        },

        render:function(){
            this.$el.html( this.template({ photo:this.model }) );

            this.$('img').css('height', (window.innerHeight - 75) + 'px');

            return this;
        },

        onCloseClick:function(evt){
            this.trigger('photo-close');

            return false;
        },

        onDetailsClick:function(){
            new PhotoDetailsDialog({ model:this.model }).render();
            return false;
        }
    });
});
