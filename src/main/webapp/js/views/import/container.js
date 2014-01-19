define([
    'views/import/import-dialog',
    'views/import/upload-dialog',
    'text!templates/import/container.html',
    'text!templates/import/import-table-row.html'
], function( ImportDialog, UploadDialog, template, rowTemplate ){

    var emptyRowTemplate = '<tr><td colspan="7">No import data available.</td></tr>';

    return Backbone.View.extend({
        template: _.template(template),
        rowTemplate: _.template(rowTemplate),

        events:{
            'click button.upload-button':'onUploadClick',
            'click button.import-button':'onImportClick'
        },

        render:function(){
            this.$el.append( this.template() );

            this.$('.import-table tbody').append( emptyRowTemplate );

            return this;
        },

        onImportClick:function(evt){
            evt.preventDefault();

            var importDialog = new ImportDialog();
        },

        onUploadClick:function(evt){
            evt.preventDefault();

            new UploadDialog().render();
        }

    });
});