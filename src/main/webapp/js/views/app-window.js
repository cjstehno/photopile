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
    'views/gallery/gallery-panel'
], function( appTemplate, AppMenu, GalleryPanel ){

    return Backbone.View.extend({
        tpt: _.template(appTemplate),

        render:function(){
            this.$el.append( this.tpt() );

            new AppMenu({ el:'.app-menu' }).render();

            new GalleryPanel({ el:'.panel-container' }).render();

            return this;
        }
    });
});