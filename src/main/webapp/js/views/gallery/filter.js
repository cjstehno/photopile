define([
    'text!templates/gallery/filter.html'
], function( template ){

    return Backbone.View.extend({
        template: _.template(template),

        render:function(){
            this.$el.append( this.template() );

            this.$elements = {
                album: this.$('.album-filter'),
                tags: this.$('.tag-filter'),
                sortField: this.$('.sort-filter'),
                sortDirection: this.$('.sort-filter-direction')
            };

            return this;
        },

        getCriteria:function(){
            var criteria = {
                album: $('button:first',this.$elements.album).attr('value'),
                sortField: $('button:first',this.$elements.sortField).attr('value'),
                sortDirection: $('button:first',this.$elements.sortDirection).attr('value')
            };

            var selectedTags = $('button:first', this.$elements.tags).attr('value');
            if( selectedTags !== 'any' ){
                var parts = selectedTags.split('|');
                criteria.tags = parts[1];
                criteria.grouping = parts[0];
            }

            return criteria;
        }
    });
});