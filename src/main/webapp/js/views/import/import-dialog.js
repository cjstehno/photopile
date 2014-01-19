define([
    'models/active-import',
    'text!templates/import/import-dialog.html',
    'text!templates/import/import-dialog-confirm.html'
], function( ActiveImport, template, confirmTemplate ){

    return Backbone.View.extend({
        template: _.template(template),
        confirmTemplate: _.template(confirmTemplate),
        model:new ActiveImport(),

        attributes:{
            class:'modal fade'
        },

        events:{
            'click button.scan-button':'onScanClick',
            'click button.import-button':'onImportClick'
        },

        initialize:function(){
            this.model.fetch({
                headers:{
                    Accept:'application/json'
                },
                accepts:'application/json',
                contentType:'application/json',
                success: _.bind(this.render, this)
            });
        },

        render:function(){
            this.$el.html( this.template( this.model.toJSON() ) );
            this.$el.modal();

            this.$elements = {
                carousel: this.$('.import-carousel'),
                scanButton: this.$('.scan-button'),
                importButton: this.$('.import-button'),
                cancelButton: this.$('.cancel-button')
            }

            this.$elements.carousel.carousel('pause');

            return this;
        },

        onScanClick:function( evt ){
            evt.preventDefault();

            this.model.save(
                {
                    directory: this.$('input[name="directory"]').val(),
                    tags: this.$('input[name="tags"]').val()
                },
                {
                    contentType:'application/json',
                    success: _.bind(this.handleScanResults, this)
                }
            );
        },

        handleScanResults:function(){
            this.$('.confirm-page').append( this.confirmTemplate( this.model.toJSON() ) );

            this.$elements.carousel.carousel('next');

            this.$elements.scanButton.attr('disabled', 'disabled');
            this.$elements.importButton.removeAttr('disabled');
        },

        onImportClick:function( evt ){
            evt.preventDefault();

            // submit the data and when returns. go to next page

            this.$elements.carousel.carousel('next');

            this.$elements.importButton.attr('disabled', 'disabled');

            this.$elements.cancelButton.removeClass('btn-danger');
            this.$elements.cancelButton.addClass('btn-default');
            this.$elements.cancelButton.text('Close');

            // TODO: cause a refresh of the import table on close from results page
        }

    });
});