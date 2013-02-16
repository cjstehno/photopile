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
        el: '#messages-dialog',
        collection:new PP.collections.UserMessages(),

        initialize:function(){
            this.listTemplate = $('#message-list-template').html();
            this.viewTemplate = $('#message-view-template').html();
        },

        events:{
            'click button[data-dismiss=modal]': 'onDialogHidden'
        },

        onDialogHidden: function (evt) {
            location.hash = '';
        },

        openDialog: function( messageId ){
            this.messageId = messageId;

            var dialog = this;
            if( this.messageId ){
                this.render();
                $(this.el).modal();

            } else {
                // render the message list template
                this.collection.fetch(
                    {
                        contentType:'application/json',
                        success:function(){
                            dialog.render();
                            $(this.el).modal();
                        }
                    }
                );
            }
        },

        render:function(){
            if( this.messageId ){
                // render the single message template
                $('div.modal-body',this.el).html( _.template(this.viewTemplate, {}) );

            } else {
                // render the message list template
                var tpt = _.template(this.listTemplate, { messages:this.collection });
                console.log(tpt);

                $('div.modal-body',this.el).html( tpt );
            }

            return this;
        }
    });

    return userMessageDialogView;
}());
