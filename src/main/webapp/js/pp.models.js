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

/**
 * The model for a single photo.
 */
var Photo = Backbone.Model.extend({});

/**
 * A collection of Photo objects. This collection should only contain a single
 * page of photos at a time.
 */
var PhotoCollection = Backbone.Collection.extend({
    model: Photo,
    url: '/photopile/photos',
    parse: function (response) {
        return response.photos;
    }
});
var Photos = new PhotoCollection;

/**
 * Defines the model used by the photo bulk import functionality.
 */
var PhotoImport = Backbone.Model.extend({
    url:'/photopile/import',
    defaults:{
        'path':'',
        'preview': true,
        'understand': false
    },
    validate:function(attrs, options){
        // FIXME: put this back
//        if( !attrs.understand ){
//            return 'You must understand what you are doing.'
//        } else if( !attrs.path ){
//            return 'A path must be specified.'
//        }

    }
});

