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

define([ 'text!templates/gallery/pager.html' ], function( template ){

    return Backbone.View.extend({
        tpt: _.template(template),

        itemsPerPage: 12,
        current:{ page:1, offset:0 },

        events:{
            'click a':'onPageClicked'
        },

        initialize:function(options){
            if( options && options.itemsPerPage ){
                this.itemsPerPage = options.itemsPerPage;
            }
        },

        getCurrent:function(){
            return _.extend({ pageSize:this.itemsPerPage }, this.current);
        },

        render:function( total ){
            this.renderPages(total || 0);
            return this;
        },

        renderPages:function(total){
            this.$el.empty();
            this.$el.append( this.tpt({
                pages:Math.ceil(total / this.itemsPerPage),
                current:this.current
            }));
        },

        onPageClicked:function(evt){
            evt.preventDefault();

            var elt = $(evt.target);
            if( !elt.parent().hasClass('disabled') && !elt.parent().hasClass('active') ){
                var page = parseInt( elt.attr('href').substring(1) );

                var newState = {
                    page: page,
                    offset: this.offsetFromPage(page),
                    pageSize: this.itemsPerPage
                };

                this.current = {
                    page: newState.page,
                    offset: newState.offset
                };

                this.trigger('page-change', newState);
            }
        },

        offsetFromPage:function( pg ){
            return (pg - 1) * this.itemsPerPage;
        }
    });
});
