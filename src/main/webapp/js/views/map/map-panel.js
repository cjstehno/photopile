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
    'text!templates/map/map-panel.html',
    'text!templates/map/map-popup.html'
], function( Photos, template, popupTemplate ){

    return Backbone.View.extend({

        template: _.template(template),
        popupTemplate: _.template(popupTemplate),

        initialize:function(){
            this.collection = new Photos();
            this.collection.on('reset', _.bind(this.renderPhotos, this));
        },

        render:function(){
            this.$el.html( this.template() );

            this.map = L.map('map');

            var that = this;
            if( navigator.geolocation ){
                navigator.geolocation.getCurrentPosition(function(pos){
                    that.map.setView([pos.coords.latitude, pos.coords.longitude], 9);

                    // add an OpenStreetMap tile layer
                    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo(that.map);

                    // FIXME: need to call when map is moved

                    that.collection.fetchWithin( that.map.getBounds().toBBoxString() );
                });
            } else {
                this.map.fitWorld();
            }

            return this;
        },

        renderPhotos:function(){
            var icon = L.icon({ iconUrl: 'img/camera.png' });

            this.collection.each(function(photo){
                var loc = photo.get('location');

                L.marker([ loc.latitude, loc.longitude ], { icon:icon })
                    .addTo(this.map)
                    .bindPopup(this.popupTemplate(photo.toJSON()));

            }, this);
        }

    });
});
