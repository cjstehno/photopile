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
    'models/active-import',
    'text!templates/import-dialog.html',
    'text!templates/import-dialog-confirm.html'
], function( ActiveImport, template, confirmTemplate ){

    return Backbone.View.extend({
        template: _.template(template),
        confirmTemplate: _.template(confirmTemplate),
        model:new ActiveImport(),

        attributes:{
            class:'modal hide fade'
        },

        events: {
            'click .scan-button': 'onScanClicked',
            'click .import-button': 'onImportClicked'
        },

        openDialog:function(){
            this.model.fetch({
                headers:{
                    Accept:'application/json'
                },
                accepts:'application/json',
                contentType:'application/json',
                success: _.bind(this.render, this)
            });
        },

        render:function( data ){
            this.$el.html( this.template(data.toJSON() || {}) );
            this.$el.modal();

            this.$('.carousel').carousel('pause');

            return this;
        },

        onScanClicked:function(){
            this.model.save(
                {
                    directory: this.$('input[name="directory"]').val(),
                    tags: this.$('input[name="tags"]').val()
                },
                {
                    contentType:'application/json',
                    success: _.bind(this.displaySummary, this)
                }
            );
        },

        displaySummary:function(){
            this.$('.confirm-panel').html( this.confirmTemplate(this.model.toJSON()) );

            this.$('.import-button').toggleClass('hide');
            this.$('.scan-button').toggleClass('hide');

            this.$('.carousel').carousel('next');
        },

        onImportClicked:function(){
            this.model.save(
                {
                    scheduled: true
                },
                {
                    contentType:'application/json',
                    success: _.bind(this.finish, this)
                }
            );
        },

        finish:function(){
            this.$el.modal('toggle');
        }

    });
});
