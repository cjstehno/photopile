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

Photopile.ns('views.UserMessageDialog');

Photopile.views.UserMessageDialog = (function () {
    return Backbone.View.extend({
        collection:new Photopile.collections.UserMessages(),

        initialize:function(){
            this.listenTo(this.collection, "reset", this.render);
            this.listenTo(this.collection, "change", this.render);
            this.listenTo(this.collection, "remove", this.render);
        },

        events:{
            'click button[data-dismiss=modal]': 'onDialogHidden',
            'click button[data-cmd=mark]': 'onMarkRead',
            'click button[data-cmd=delete]': 'onDelete'
        },

        onMarkRead:function(evt){
            var mid = $(evt.target).parent().attr('data-id');
            this.collection.get(mid).save('read','true');
        },

        onDelete:function(evt){
            var mid = $(evt.target).parent().attr('data-id');
            this.collection.get(mid).destroy({ contentType:'application/json' });
        },

        onDialogHidden: function (evt) {
            location.hash = '';
        },

        openDialog: function( messageId ){
            this.messageId = messageId;

            // TODO: this is a bit hacky, need to just add polling to keep status current
            var that = this;
            this.collection.fetch(
                {
                    contentType:'application/json',
                    success:function(){
                        that.render();
                    }
                }
            );
        },

        render:function(){
            $(this.el).modal();

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
}());
