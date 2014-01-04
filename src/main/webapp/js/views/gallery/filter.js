define([
    'text!templates/gallery/filter.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        events:{
            'click .album-filter a':'onAlbumChange',
            'click .tag-filter a':'onTagChange',
            'click .sort-filter a':'onSortChange',
            'click .sort-filter-direction a':'onSortDirectionChange'
        },

        render:function(){
            this.$el.append( this.template() );

            this.$elements = {
                album: this.$('.album-filter button:first'),
                tags: this.$('.tag-filter button:first'),
                sortField: this.$('.sort-filter button:first'),
                sortDirection: this.$('.sort-filter-direction button:first')
            };

            return this;
        },

        getCriteria:function(){
            var criteria = {
                album: this.$elements.album.attr('value'),
                sortField: this.$elements.sortField.attr('value'),
                sortDirection: this.$elements.sortDirection.attr('value')
            };

            var selectedTags = this.$elements.tags.attr('value');
            if( selectedTags !== 'any' ){
                var parts = selectedTags.split('|');
                criteria.tags = parts[1];
                criteria.grouping = parts[0];
            }

            return criteria;
        },

        onAlbumChange:function(evt){
            evt.preventDefault();
            var target = $(evt.currentTarget);

            var filter = target.attr('href').substring(1);
            if( filter == 'more' ){
                // FIXME: open the album selector...

//                this.trigger('filter-changed');

            } else {
                this.$elements.album.text( target.text() );
                this.$elements.album.attr( 'value', filter );

                this.trigger('filter-changed');
            }
        },

        onTagChange:function(evt){
            evt.preventDefault();
            // FIXME: tag selector dialog
        },

        onSortChange:function(evt){
            evt.preventDefault();
            var target = $(evt.currentTarget);

            this._updateFilter(
                this.$elements.sortField,
                target.text(),
                target.attr('href').substring(1)
            );
        },

        onSortDirectionChange:function(evt){
            evt.preventDefault();
            var target = $(evt.currentTarget);

            this._updateFilter(
                this.$elements.sortDirection,
                target.text(),
                target.attr('href').substring(1)
            );
        },

        _updateFilter:function( elt, label, value ){
            elt.text( label );
            elt.attr( 'value', value );

            this.trigger('filter-change');
        }
    });
});