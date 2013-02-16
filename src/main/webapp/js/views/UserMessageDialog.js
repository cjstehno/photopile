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

PP.namespace('PP.views.UserMessageDialog');

PP.views.UserMessageDialog = (function () {
    var userMessageDialogView = Backbone.View.extend({
        collection:new PP.collections.UserMessages(),

        initialize:function(){
//            this.listenTo(this.collection, "reset", this.render);

            this.collection.fetch(
                { contentType:'application/json' }
            );
        },

        events:{
            'click button[data-dismiss=modal]': 'onDialogHidden'
        },

        onDialogHidden: function (evt) {
            console.log('doin it')
            location.hash = '';
        },

        openDialog: function( messageId ){
            this.messageId = messageId;

            this.render();
            $(this.el).modal();
        },

        render:function(){
            if( this.messageId ){
                // render the single message template
                $('div.modal-body',this.el).html( _.template($('#message-view-template').html(), { message:this.collection.get(this.messageId)}) );

            } else {
                // render the message list template
                $('div.modal-body',this.el).html( _.template( $('#message-list-template').html(), { messages:this.collection }) );
            }

            return this;
        }
    });

    return userMessageDialogView;
}());
