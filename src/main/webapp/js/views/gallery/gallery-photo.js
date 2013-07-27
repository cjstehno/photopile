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

define([ 'text!templates/gallery/gallery-photo.html' ], function( template ){

    return Backbone.View.extend({
        tpt: _.template(template),

        events:{
            'click .back-button':'onCloseClick',
            'click .details-button':'onDetailsClick'
        },

        initialize:function(options){
            this.model = options.model;
        },

        render:function(){
            this.$el.html( this.tpt({ photo:this.model }) );

            return this;
        },

        onCloseClick:function(evt){
            this.trigger('photo-close');

            return false;
        },

        onDetailsClick:function(){
            console.log('display the photo details as popup');
            return false;
        }
    });
});
