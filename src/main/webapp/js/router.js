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

define([
    'views/import-dialog',
    'views/user-messages-dialog'
], function( ImportDialog, UserMessagesDialog ){

    var importDialog = new ImportDialog();
    var messagesDialog = new UserMessagesDialog();

    return Backbone.Router.extend({
        routes: {
            'photos': 'listPhotos',
            'photoImport': 'photoImport',

            'messages(/:messageId)': 'messages',

            '*actions': 'listPhotos'
        },

//            allPhotos: function () {
//                Photos.fetch({
//                    data: {start: 0, limit: 22},
//                    success: function (collection, response, options) {
//                        collection.total = response.total;
//                        console.log('Total = ' + collection.total);
//                    }
//                });
//            },

        messages:function( messageId ){
            messagesDialog.openDialog( messageId );
        },

        photoImport: function () {
            importDialog.openDialog();
        }
    });
});