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
    'text!templates/photo-details-dialog.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        attributes:{
            class:'modal hide fade'
        },

        initialize:function( options ){
            this.model = options.model;
        },

        render:function(){
            this.$el.html( this.template({ photo:this.model.toJSON() }) );
            this.$el.modal();

            return this;
        }
    });
});