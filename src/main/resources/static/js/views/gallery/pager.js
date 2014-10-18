define([
    'text!templates/gallery/pager.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click button':'onButtonClick'
        },

        initialize:function(){
            this.itemsPerPage = 24;
            this.current = { page:1, offset:0, pageSize:this.itemsPerPage };

            this.collection.on('reset', _.bind(this.render, this));
        },

        render:function(){
            this.$el.empty();

            this.$el.append( this.template({
                pages:Math.ceil( this.collection.total / this.itemsPerPage),
                current:this.current
            }));

            return this;
        },

        getCurrent:function(){
            return this.current;
        },

        onButtonClick:function(evt){
            var page = parseInt( $(evt.currentTarget).attr('value') );

            this.current = {
                page: page,
                offset: (page - 1) * this.itemsPerPage,
                pageSize: this.itemsPerPage
            };

            this.trigger( 'page-change' );
        }

    });
});