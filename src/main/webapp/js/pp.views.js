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

/**
 * Defines the photo gallery view.
 */
var GalleryView = Backbone.View.extend({
    initialize: function () {
        this.listenTo(this.collection, "reset", this.render);
    },
    render: function () {
        var template = '';

        var cellTemplateContent = $('#gallery-cell-template').html();
        var rowTemplateContent = $("#gallery-row-template").html();

        var collectionIndex = 0;
        for (var row = 0; row < 4; row++) {
            var cells = {};
            for (var cell = 0; cell < 6; cell++) {
                if (collectionIndex < this.collection.length) {
                    var photo = this.collection.at(collectionIndex++);
                    cells['gallery_cell_' + (cell + 1)] = _.template(cellTemplateContent, { photo_id: photo.id });

                } else {
                    cells['gallery_cell_' + (cell + 1)] = '';
                }
            }

            template += _.template(rowTemplateContent, cells);
        }

        this.$el.html(template);

        return this;
    }
});

/**
 * Defines the server import dialog view.
 */
var ImportDialogView = Backbone.View.extend({
    events: {
        'click button.btn-primary': 'onImportClicked',
        'click button[data-dismiss=modal]': 'onDialogHidden',
        'change input[type=checkbox]': 'onUnderstandChanged'
        // FIXME: fix checkbox resolution
    },

    openDialog: function () {
        $(this.el).modal();
    },

    onDialogHidden: function (evt) {
        location.hash = '';
    },
    onUnderstandChanged: function (evt) {
        $('button.btn-primary', this.el).toggleClass('disabled');
    },
    onImportClicked: function (evt) {
        if (!$('button.btn-primary', this.el).hasClass('disabled')) {
            location.hash = '';

            // FIXME: need to call server and ensure import job accepted
            console.log("importing...");

            $('div.card', this.el).toggle();
        }
    }
});

