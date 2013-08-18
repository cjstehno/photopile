define([ 'text!templates/common/dialog.html' ], function( template ){


    // FIXME: not working yet

    return Backbone.View.extend({
        template: _.template(template),

        render:function(){
            this.$el.html( this.template({
                allowDismiss: this.options.allowDismiss || false,
                title: this.options.title
            }) );

            this.options.body.render();

            this.$el.modal();

            return this;
        }

    });
});