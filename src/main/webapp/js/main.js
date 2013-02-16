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

/**
 * Pre-loads the templates by adding them to the end of the document body.
 *
 * @param templates an array of template paths to be loaded
 */
PP.loadTemplates = function( templates ){
    var count = templates.length;
    var loaded = 0;

    _.each( templates, function(tpt){
        console.log('loading ' + tpt + '...');

        $('#templates').add('div').load(current, function(){
            loaded++;
        });
    });
};
