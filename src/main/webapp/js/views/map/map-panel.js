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
    'common/views/carousel',
    'views/map/map-panel-map',
    'views/map/map-panel-photo',
    'text!templates/map/map-panel.html'
], function( CarouselView, MapPanel, PhotoPanel, template ){

    return Backbone.View.extend({

        template: _.template(template),

        render:function(){
            this.$el.html(this.template());

            var mapPanel = new MapPanel();
            var photoPanel = new PhotoPanel();

            var carousel = new CarouselView({
                el: '.carousel-container',
                panels:[
                    mapPanel,
                    photoPanel
                ]
            }).render();

            mapPanel.on('photo-selected', function(photo){
                photoPanel.renderPhoto(photo);
                carousel.nextPanel();
            }, this);

            photoPanel.on('photo-close', function(){
                console.log('close');
                carousel.prevPanel();
            }, this);

            return this;
        }

    });
});
