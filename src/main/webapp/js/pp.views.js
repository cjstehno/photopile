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
    initialize:function(){
        this.model.on('error',function(e){
            // FIXME: implement error handling
            console.log('error ' + e);
        });
        this.listenTo( this.model, 'change', this.render );
    },
    events: {
        'click button.btn-primary': 'onImportClicked',
        'click button[data-dismiss=modal]': 'onDialogHidden',
        'change input[type=checkbox]': 'onUnderstandChanged'
        // FIXME: fix checkbox resolution
    },

    openDialog: function () {
        this.model.clear();

        $(this.el).modal();
    },

    render:function(){
        $('div.modal-body div.alert', this.el).hide();

        var cards = $('div.modal-body .card', this.el);
        $(cards[0]).show();
        $(cards[1]).hide();
        $(cards[2]).show();
        $(cards[3]).hide();

        $('form input[name=path]',this.el).val(this.model.get('path'));
        $('form input[name=preview]',this.el).prop('checked', true);
        $('form input[name=understand]',this.el).prop('checked', false);

        return this;
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

            var theModel = this.model;
            theModel.save(
                {
                    path:$('form input[name=path]', this.el).val(),
                    preview:$('form input[name=preview]', this.el).is(':checked'),
                    understand:$('form input[name=understand]', this.el).is(':checked')
                },
                {
                    success:function(){
                        console.log('succeeded...');

                        $($('div.modal-body div.card p',this.el)[1]).html('Your scan of ' + theModel.get('path') + ' has started.')
                        $('div.card', this.el).toggle();
                    },

                    error:function(model, xhr, options){
                        console.log('failed: ' + xhr);
                        $('div.modal-body div.alert div', this.el).text(xhr.responseText);
                        $('div.modal-body div.alert', this.el).show();
                    }
                }
            );
        }
    }
});

