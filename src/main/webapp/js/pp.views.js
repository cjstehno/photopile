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


var GalleryView = Backbone.View.extend({
    initialize: function () {
        this.listenTo(this.collection, "reset", this.render);
    },
    render: function () {
        var template = '';

        console.log("photo count: " + this.collection.length);

        for (var row = 0; row < 4; row++) {
            template += _.template($("#gallery-row-template").html(), {});
        }

        this.$el.html(template);

        return this;
    }
});

var gallery = new GalleryView({ el: '#gallery-container', collection: Photos });