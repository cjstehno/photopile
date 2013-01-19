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

Ext.define( 'PP.view.PhotoDetails', {
    extend:'Ext.panel.Panel',
    alias:'widget.photo-details',

    title:'Photo Details',
    width:256,
    collapsible: true,
    collapsed: true,
    resizable: true,
    frame:true,

    data:{},

    /*
        lets use a simple property grid here with the image on top.
        for location, provide a means of opening dialog with map
     */

    tpl:new Ext.XTemplate(
        '<div><img src="http://localhost:8080/photopile/testimage.jpg" width="256"/></div>',
        '<div>Name:</div>',
        '<div>Photo Name Here</div>',
        '<div>Description:</div>',
        '<div>A description of the photo...</div>',
        '<div>Tags:</div>',
        '<div>The tags...</div>',
        '<div>Dimensions:</div>',
        '<div>800 x 600</div>',
        '<div>Size:</div>',
        '<div>1234 kb</div>',
        '<div>Date Taken:</div>',
        '<div>1/1/2013</div>',
        '<div>Date Updated:</div>',
        '<div>1/2/2013</div>',
        '<div>Camera:</div>',
        '<div>HTC Sensation</div>',
        '<div>Location: 123,123</div>'
    )
});