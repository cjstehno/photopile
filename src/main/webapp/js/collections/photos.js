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

define([ 'models/photo' ], function( Photo ){

    return Backbone.Collection.extend({
        model: Photo,
        url:'/photopile/photos',

        total: 0,

        parse:function( response ){
            if( response.meta ){
                this.total = response.meta.total;
                return response.data;
            }
            return response;
        },

        fetchWithin:function( bounds ){
            this.fetch({
                url:this.url + '/within/' + bounds,
                reset:true,
                contentType:'application/json'
            });
        },

        fetchPage:function( page, filter ){
            var urlSuffix = '/' + filter.filter + (filter.subfilter ? '/' + filter.subfilter : '') + '/' + filter.sort;

            this.fetch({
                url:this.url + urlSuffix,
                reset:true,
                contentType:'application/json',
                data:{
                    start:page.offset,
                    limit:page.pageSize,
                    order:filter.order
                }
            });
        }

    });
});
