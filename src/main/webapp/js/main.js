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

// FIXME: integrate a real dependency loading library

var PP = PP || {};

PP.namespace = function (ns_string) {
    var parts = ns_string.split('.'),
        parent = PP,
        i;

    // strip redundant leading global
    if (parts[0] === "PP") {
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
};

PP.logger = {
    enabled:true,

    debug:function(msg){
        if(PP.logger.enabled) console.debug(msg);
    },

    warn:function(msg){
        if(PP.logger.enabled) console.warn(msg);
    },

    error:function(msg){
        if(PP.logger.enabled) console.error(msg);
    }
};

PP.loadExternals = function( defn ){
    PP.loaders.template( _.first(defn.templates), _.rest(defn.templates), function(){
        PP.loaders.script( defn.scripts, defn.loaded )
    });
};

PP.loaders = {
    template:function( f, r, d ){
        $('<div />').load(f, function(){
            PP.logger.debug('loaded template: ' + f);

            if(r.length > 0 ){
                PP.loaders.template(_.first(r), _.rest(r), d);
            } else {
                d();
            }
        }).appendTo('body');
    },

    script:function( scripts, done ){
        _.each( scripts, function(s){
            PP.logger.debug('loaded script: ' + s);

            $('<script type="text/javascript" src="' + s + '"></script>').appendTo('head')
        });

        done();
    }
};


$(function() {
    PP.loadExternals({
        templates:[
            'templates/messages_dialog.html',
            'templates/import_dialog.html'
        ],
        scripts:[
            'js/models/Photo.js',
            'js/models/PhotoImport.js',
            'js/models/UserMessage.js',
            'js/collections/Photos.js',
            'js/collections/UserMessages.js',
            'js/views/ImporterDialog.js',
            'js/views/UserMessageDialog.js',
            'js/views/PhotoGallery.js',
            'js/app.js',
            'js/router.js'
        ],
        loaded:function(){
            PP.logger.debug('Done loading.');

            new PP.App().initialize();
        }
    });
});

