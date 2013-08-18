define([
    'collections/tags',
    'text!templates/tag-selector-dialog.html',
    'text!templates/tag-selector-dialog-item.html'
], function( Tags, template, rowTemplate ){

    return Backbone.View.extend({
        template: _.template(template),
        rowTemplate: _.template(rowTemplate),

        events:{
            'click .tag-item':'onTagItemClick',
            'keypress form input[name="filter"]':'onFilterChange',
            'click .btn.btn-primary':'onApplyClick'
        },

        /* FIXME: next...
            -   save tag selection state and forward to the gallery grid request (callback)
            -   support for changing the selected tags in the gallery grid view itself (crumb)
         */

        attributes:{
            class:'modal hide fade'
        },

        initialize:function(){
            this.collection = new Tags();
        },

        render:function(){
            this.$el.html( this.template() );
            this.$el.modal();

            this.$elements = {
                items: this.$('table tbody')
            };

            this.collection.fetch({
                contentType:'application/json',
                success: _.bind(this.renderListItems, this, '')
            });

            return this;
        },

        renderListItems:function( filter ){
            this.$elements.items.empty();

            var filterText = (filter || '').toLowerCase();

            this.collection.each(function(it){
                var name = it.get('name');

                if( filterText.trim().length == 0 || name.toLowerCase().indexOf(filterText) > 0 ){
                    this.$elements.items.append(this.rowTemplate({
                        name: name,
                        selected: it.selected || false
                    }));
                }

            }, this);
        },

        onTagItemClick:function(evt){
            $('td.selected-column > i.icon', evt.currentTarget).toggleClass('icon-ok');

            var selectedTag = $(evt.currentTarget).attr('data-tag');

            var tag = this.collection.find(function(it){
                return it.get('name') == selectedTag;
            }, this);

            tag.selected = true;

            return false;
        },

        onApplyClick:function(){
            var tags = [];

            $('.tag-item', this.$elements.items).each(function(idx,elt){
                if( $('td.selected-column > i', elt).hasClass('icon-ok') ){
                    tags.push( $(elt).attr('data-tag') );
                }
            });

            this.trigger('tags-selected',{
                tags:tags,
                grouping: this.$('form input[name="grouping"]:checked').val()
            });

            return false;
        },

        onFilterChange:function(evt){
            evt.stopPropagation();

            if( evt.keyCode === 13 ){
                var filter = this.$('form input[name="filter"]').val();
                this.renderListItems(filter);

                evt.preventDefault();
            }
        }

    });
});