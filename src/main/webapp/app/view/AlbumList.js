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

Ext.define( 'PP.view.AlbumList', {
    extend:'Ext.grid.Panel',
    alias:'widget.album-list',

    title:'Albums',
    frame:true,

    store:'Albums',

    initComponent: function() {
        this.hideHeaders = true;

        this.columns = [
            { header:'id', hidden:true, dataIndex:'id', flex: 1},
            { header:'name', dataIndex:'name', flex: 1},
            { header:'count', dataIndex:'count', width:30, align:'right' }
        ];

        this.callParent(arguments);
    }
});