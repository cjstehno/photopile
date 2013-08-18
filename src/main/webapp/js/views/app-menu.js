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
    'views/import-dialog',
    'views/user-messages-dialog',
    'text!templates/app-menu.html'
], function( ImportDialog, UserMessagesDialog, template ){

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click a.photos-add': 'onAddPhotoItem',
            'click a.photos-import': 'onImportPhotoItem',

            'click a.view-map':'onViewMap',
            'click a.view-grid-all':'onViewAllGrid',
            'click a.view-grid-tags':'onViewTagsGrid',

            'click a.message-menu-item': 'onMessageItem'
        },

        render:function(){
            this.$el.append( this.template() );
            return this;
        },

        onViewMap:function(){
            this.trigger('menu-item-selected', { id:'view-map' });
        },

        onViewAllGrid:function(){
            this.trigger('menu-item-selected', { id:'view-grid-all' });
        },

        onViewTagsGrid:function(){
            this.trigger('menu-item-selected', { id:'view-grid-tags' });
        },

        /*
            FIXME: need to poll for message count
         */

        onMessageItem:function( evt ){
            // TODO: move this out as an event handler
            new UserMessagesDialog().openDialog();

            return false;
        },

        onAddPhotoItem:function(evt){
            evt.preventDefault();
            console.log('adding photo');
        },

        onImportPhotoItem:function(evt){
            // TODO: move this out as handler
            evt.preventDefault();

            new ImportDialog().openDialog();
        }
    });
});