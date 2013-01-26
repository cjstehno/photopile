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

$(new function () {
    // use moustache style template variables
    _.templateSettings = {
        interpolate: /\{\{(.+?)\}\}/g
    };

    var galleryView = new GalleryView({ el: '#gallery-container', collection: Photos });
    var importDialogView = new ImportDialogView({ el: '#import-dialog', model: new PhotoImport });

    new PhotopileRouter({
        views: {
            'gallery': galleryView,
            'importDialog': importDialogView
        }
    });

    Backbone.history.start();
});



