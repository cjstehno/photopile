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
    'views/tag-selector-dialog',
    'text!templates/view-filter.html'
], function( TagSelectorDialog, template ){

    // FIXME: move to common area
    function _applyIcon( name ){
        if( name.indexOf('camera:') != -1 ){
            return '<i class="icon icon-camera"></i> ' + name.substring(7);
        } else if( name.indexOf('month:') != -1 ){
            return '<i class="icon icon-calendar"></i> ' + name.substring(6);
        } else if( name.indexOf('year:') != -1 ){
            return '<i class="icon icon-calendar"></i> ' + name.substring(5);
        } else {
            return '<i class="icon icon-tag"></i> ' + name;
        }
    };

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click div.album-filter a.filter-item': 'onAlbumSelect',
            'click div.tags-filter a.filter-item': 'onTagsSelect',
            'click div.sort-field-filter a.filter-item': 'onSortFieldSelect',
            'click div.sort-direction-filter a.filter-item': 'onSortDirSelect'
        },

        render:function(){
            this.$el.html( this.template() );

            this.$elements = {
                album: this.$('.album-filter a:first'),
                tags: this.$('.tags-filter a:first'),
                sortField: this.$('.sort-field-filter a:first'),
                sortDirection: this.$('.sort-direction-filter a:first')
            };

            return this;
        },

        getCurrent:function(){
            var current = {
                album: this.$elements.album.attr('data-selected'),
                sortField: this.$elements.sortField.attr('data-selected'),
                sortDirection: this.$elements.sortDirection.attr('data-selected')
            };

            var selectedTags = this.$elements.tags.attr('data-selected');
            if( selectedTags !== 'any' ){
                var parts = selectedTags.split('|');
                current.tags = parts[1];
                current.grouping = parts[0];
            }

            return current;
        },

        onAlbumSelect:function( evt ){
            var selection = this.extractSelection(evt);

            if( selection.id === 'more' ){
                console.log('open album selection dialog');
            }

            this.fireEvent(evt);
        },

        onTagsSelect:function( evt ){
            var selection = this.extractSelection(evt);
            if( selection.id === 'more' ){
                new TagSelectorDialog().render().on('tags-selected', function( selectedTags ){
                    selection.id = selectedTags.grouping.toUpperCase() + '|' + selectedTags.tags.join(',');

                    selection.label = _.map(selectedTags.tags, function(tag){
                        return _applyIcon(tag);
                    }).join( selectedTags.grouping === 'ANY' ? ' or ' : ' and ' );

                    // FIXME: label for tags should apply icons for groups
                    this.updateSelected('tags', selection);
                    this.fireEvent(evt);
                }, this);

            } else {
                this.updateSelected('tags', selection);
                this.fireEvent(evt);
            }
        },

        onSortFieldSelect:function(evt){
            this.updateSelected('sortField', this.extractSelection(evt));
            this.fireEvent( evt);
        },

        onSortDirSelect:function(evt){
            this.updateSelected('sortDirection', this.extractSelection(evt));
            this.fireEvent( evt );
        },

        fireEvent:function( evt ){
            evt.preventDefault();

            this.trigger('filter-change');
        },

        extractSelection:function( evt ){
            return {
                id: $(evt.currentTarget).attr('data-id'),
                label: $(evt.currentTarget).text()
            };
        },

        updateSelected:function( filterField, selection ){
            this.$elements[filterField].attr('data-selected', selection.id);
            this.$elements[filterField].html(selection.label);
        }

    });
});
