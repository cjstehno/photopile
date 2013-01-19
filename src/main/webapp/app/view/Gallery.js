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

Ext.define( 'PP.view.Gallery', {
    extend:'Ext.grid.Panel',
    alias:'widget.gallery',

    title:'Gallery',
    store:'Photos',
    frame:true,

    dockedItems: [{
        xtype: 'pagingtoolbar',
        store:'Photos',
        dock: 'bottom',
        displayInfo: true
    }],

    initComponent: function() {
        var descriptionTemplate = new Ext.XTemplate(
            '<div class="photo-name">{name}</div>',
            '<div class="photo-dimensions">({width} x {height})</div>',
            '<div class="photo-description">{description}</div>'
        );

        var tagsTemplate = new Ext.XTemplate(
            '<tpl for="tags"><span>{.}</span>,</tpl>'
        );

        this.columns = [
            { header:'id', hidden:true, dataIndex:'id'},
            { header:'Description', xtype:'templatecolumn', tpl:descriptionTemplate, flex:2},
            { header:'Tags', xtype:'templatecolumn', tpl:tagsTemplate, flex: 1},
            { header:'Date Taken', dataIndex:'dateTaken', type:'date', width:100 },
            { header:'Size (kB)', dataIndex:'size', type:'number', width:80 },
            { header:'Preview', xtype:'templatecolumn', tpl:'<img src="http://localhost:8080/photopile/testimage.jpg"/>', width:128 }
        ];

        this.callParent(arguments);
    }
});