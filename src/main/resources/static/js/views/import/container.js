define([
    'collections/active-imports',
    'views/import/import-dialog',
    'views/import/upload-dialog',
    'text!templates/import/container.html',
    'text!templates/import/import-table-row.html'
], function( ActiveImports, ImportDialog, UploadDialog, template, rowTemplate ){

    var emptyRowTemplate = '<tr><td colspan="7">No import data available.</td></tr>';

    return Backbone.View.extend({
        template: _.template(template),
        rowTemplate: _.template(rowTemplate),

        events:{
            'click button.upload-button':'onUploadClick',
            'click button.import-button':'onImportClick',
            'click button.cancel-button':'onCancelClick'
        },

        initialize:function(){
            this.collection = new ActiveImports();
            this.collection.on('reset', _.bind(this.renderImports, this));
        },

        render:function(){
            this.$el.append( this.template() );

            this.collection.fetch({ reset:true, contentType:'application/json' });

            return this;
        },

        renderImports:function(){
            var tableBody = this.$('.import-table tbody');
            if( this.collection.size() == 0 ){
                tableBody.append( emptyRowTemplate );

            } else {
                this.collection.each(function(imp){
                    tableBody.append( this.rowTemplate(imp.toJSON()) );
                }, this);
            }
        },

        onImportClick:function(evt){
            evt.preventDefault();

            var importDialog = new ImportDialog();
        },

        onUploadClick:function(evt){
            evt.preventDefault();

            new UploadDialog().render();
        },

        onCancelClick:function(evt){
            var importId = $(evt.currentTarget).attr('data-id');
            var importModel = this.collection.get(importId);

            // FIXME: a bit of a hack
            importModel.url = importModel.url + '/status/' + importModel.id

            importModel.destroy(); // fires remove event
        }

    });
});