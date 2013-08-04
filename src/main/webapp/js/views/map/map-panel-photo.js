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

define([ 'text!templates/map/map-panel-photo.html' ], function( template ){
    // TODO: should create common photo panel

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click .back-button':'onCloseClick',
            'click .details-button':'onDetailsClick'
        },

        render:function(){
            this.renderPhoto({ id:null });
            return this;
        },

        renderPhoto:function( photo ){
            this.$el.empty();
            this.$el.html(this.template( photo ));

            this.delegateEvents(this.events);
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
