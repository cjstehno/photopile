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

var Photopile = Photopile || {
    ns:function(ns_string){
        var parts = ns_string.split('.'),
            parent = Photopile,
            i;

        // strip redundant leading global
        if (parts[0] === "Photopile") {
            parts = parts.slice(1);
        }

        for (i = 0; i < parts.length; i += 1) {
            // create a property if it doesn't exist
            if (typeof parent[parts[i]] === "undefined") {
                parent[parts[i]] = {};
            }
            parent = parent[parts[i]];
        }
        return parent;
    },

    loaders:{
        template:function( f, r, d ){
            $('<div />').load(f, function(){
                console.debug('loaded template: ' + f);

                if(r.length > 0 ){
                    Photopile.loaders.template(_.first(r), _.rest(r), d);
                } else {
                    d();
                }
            }).appendTo('body');
        },

        script:function( scripts, done ){
            _.each( scripts, function(s){
                console.debug('loaded script: ' + s);

                $('<script type="text/javascript" src="' + s + '"></script>').appendTo('head')
            });

            done();
        }
    },

    load:function( defn ){
        Photopile.loaders.template( _.first(defn.templates), _.rest(defn.templates), function(){
            Photopile.loaders.script( defn.scripts, defn.loaded )
        });
    }
};

$(function() {
    Photopile.load({
        templates:[
            'templates/messages_dialog.html',
            'templates/import_dialog.html'
        ],
        scripts:[
            'js/models/photo.js',
            'js/models/photo-import.js',
            'js/models/user-message.js',
            'js/collections/photos.js',
            'js/collections/user-messages.js',
            'js/views/import-dialog.js',
            'js/views/user-messages-dialog.js',
            'js/views/gallery.js',
            'js/app.js',
            'js/router.js'
        ],
        loaded:function(){
            console.debug('Done loading.');

            new Photopile.App().initialize();
        }
    });
});

