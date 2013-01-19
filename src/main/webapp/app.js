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


Ext.application({
    requires: ['Ext.container.Viewport'],
    name: 'PP',
    appFolder:'app',

    controllers:[
        'HeaderController', 'CategoryController', 'GalleryController', 'DetailsController'
    ],

    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'border',
            items: [
                { region:'north', xtype:'app-header' },
                {
                    region:'west',
                    title:'Categories',

                    width:'25%',
                    collapsible: true,
                    resizable: true,
                    frame:true,

                    layout: {
                        type: 'accordion',
                        titleCollapse: true,
                        animate: true,
                        activeOnTop: true
                    },

                    items: [
                        { xtype:'album-list' },
                        { xtype:'tag-list' },
                        {
                            title: 'Date Taken',
                            frame:true,
                            html: 'Panel content!'
                        }
                    ]
                },
                { region:'center', xtype:'gallery' },
                { region:'east', xtype:'photo-details' },
                {
                    region:'south',
                    frame:true,
                    html:'Logged in as admin...'
                }
            ]
        });
    }
});