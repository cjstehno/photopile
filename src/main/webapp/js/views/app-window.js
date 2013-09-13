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
    'text!templates/app-window.html',
    'views/app-menu',
    'views/view-filter',
    'views/view-pager',
    'views/gallery/gallery-panel',
    'views/map/map-panel'
], function( appTemplate, AppMenu, ViewFilter, ViewPager, GalleryPanel, MapPanel ){

    return Backbone.View.extend({
        template: _.template(appTemplate),

        render:function(){
            this.$el.append( this.template() );

            var appMenu = new AppMenu({ el:'.app-menu' });
            appMenu.on('menu-item-selected', _.bind(this.menuItemSelected, this));
            appMenu.render();

            this.viewFilter = new ViewFilter({ el:'.breadcrumbs' }).render();
            this.viewPager = new ViewPager({ el:'.view-pager' }).render();

            // default to the gallery view
            this.showGallery();

            return this;
        },

        menuItemSelected:function( evt ){
            // TODO: clean this up

            this.$('.panel-container').empty();

            if( evt.id === 'view-grid-all' ){
                this.showGallery();

            } else if( evt.id === 'view-map' ){
                new MapPanel({ el:'.panel-container' }).render();

            } else if( evt.id === 'view-grid-tags' ){


            } else {
                console.log('unsupported option: ' + evt);
            }
        },

        showGallery:function(){
            new GalleryPanel({
                el:'.panel-container',
                viewFilter: this.viewFilter,
                viewPager: this.viewPager
            }).render();
        }
    });
});

