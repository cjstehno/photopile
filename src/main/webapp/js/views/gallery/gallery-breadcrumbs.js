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

define([ 'text!templates/gallery/gallery-breadcrumbs.html' ], function( template ){
    var ORDERS = [
        { id:'desc', label:'Most Recent'},
        { id:'asc', label:'Oldest'}
    ];

    var SORTS = [
        { id:'dateTaken', label:'Date Taken' },
        { id:'dateUploaded', label:'Date Uploaded' }
    ];

    var FILTERS = [
        { id:'all', label:'All' },
        { id:'album', label:'Album' },
        { id:'tag', label:'Tag' },
        { id:'camera', label:'Camera' }
    ];

    return Backbone.View.extend({
        tpt: _.template(template),

        events:{
            'click a.view-filter':'onFilterClick',
            'click a.view-subfilter':'onSubfilterClick',
            'click a.view-sort':'onSortClick',
            'click a.view-sort-order':'onSortOrderClick'
        },

        current:{
            filter:'all',
            subfilter:null,
            sort:'dateTaken',
            order:'desc'
        },

        render:function(){
            this.$el.html( this.tpt({ context:this.buildContext() }) );

            return this;
        },

        buildContext:function(){
            // FIXME: should build a view context based on current view and selected filters

            var enhancedCurrent = {
                filter: _.find(FILTERS, function(it){ return it.id == this.current.filter }, this),
                subfilter:null,
                sort: _.find(SORTS, function(it){ return it.id == this.current.sort }, this),
                order: _.find(ORDERS, function(it){ return it.id == this.current.order }, this)
            };

            return {
                current:enhancedCurrent,
                filters:FILTERS,
                sorts:SORTS,
                orders:ORDERS
            };
        },

        getCurrent:function(){
            return this.current;
        },

        onFilterClick:function(evt){
            this.updateViewWith('filter', evt);
        },

        onSubfilterClick:function(evt){
            this.updateViewWith('subfilter', evt);
        },

        onSortClick:function(evt){
            this.updateViewWith('sort', evt);
        },

        onSortOrderClick:function(evt){
            this.updateViewWith('order', evt);
        },

        updateViewWith:function( filterVar, evt ){
            evt.preventDefault();

            var changes = {};
            changes[filterVar] = this.extractId(evt.target);

            this.trigger('filter-change', _.extend( this.current, changes ));
        },

        extractId:function( target ){
            return $(target).attr('href').substring(1);
        }

    });
});
