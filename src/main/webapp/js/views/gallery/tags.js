define([
    'collections/tags',
    'text!templates/gallery/tags.html',
    'text!templates/gallery/tags-item.html'
], function( Tags, template, itemTemplate ){

    return Backbone.View.extend({
        template: _.template(template),
        itemTemplate: _.template(itemTemplate),

        attributes:{
            class:'modal fade'
        },

        events:{
            'click .tag-item':'onTagItemClick',
            'keypress form input[name="filter"]':'onFilterChange',
            'click .btn.btn-primary':'onApplyClick'
        },

        initialize:function(){
            this.collection = new Tags();
        },

        render:function(){
            this.$el.html( this.template() );
            this.$el.modal();

            this.$elements = {
                items: this.$('.tag-list')
            };

            this.collection.fetch({
                contentType:'application/json',
                success: _.bind(this.renderListItems, this, '')
            });

            return this;
        },

        onTagItemClick:function(evt){
            evt.preventDefault();

            $('span.selection', evt.currentTarget).toggleClass('glyphicon-ok');

            var selectedTag = $(evt.currentTarget).attr('data-tag');

            var tag = this.collection.find(function(it){
                return it.get('name') == selectedTag;
            }, this);

            tag.selected = true;
        },

        onApplyClick:function(evt){
            evt.preventDefault();

            var tags = [];

            $('.tag-item', this.$elements.items).each(function(idx,elt){
                if( $('span.selection', elt).hasClass('glyphicon-ok') ){
                    tags.push( $(elt).attr('data-tag') );
                }
            });

            this.trigger('tags-selected',{
                tags:tags,
                grouping: this.$('form input[name="grouping"]:checked').val()
            });
        },

        onFilterChange:function(evt){
            evt.stopPropagation();

            if( evt.keyCode === 13 ){
                this.renderListItems( this.$('input[name="filter"]').val() );
                evt.preventDefault();
            }
        },

        renderListItems:function( filter ){
            this.$elements.items.empty();

            var filterText = (filter || '').toLowerCase();

            this.collection.each(function(it){
                var name = it.get('name');

                if( filterText.trim().length == 0 || name.toLowerCase().indexOf(filterText) > 0 ){
                    this.$elements.items.append(this.itemTemplate({
                        name: name,
                        selected: it.selected || false
                    }));
                }

            }, this);
        }
    });
});

