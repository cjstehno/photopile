define([ 'models/photo' ], function( Photo ){

    return Backbone.Collection.extend({
        model: Photo,

        total: 0,

        parse:function( response ){
            if( response.meta ){
                this.total = response.meta.total;
                return response.data;
            }
            return response;
        },

        fetchWithin:function( bounds ){
            this.fetch({
                url:this.url + '/within/' + bounds,
                reset:true,
                contentType:'application/json'
            });
        },

        fetchPage:function( page, filter ){
            var params = {
                start: page.offset,
                limit: page.pageSize,
                field: filter.sortField,
                direction: filter.sortDirection
            };

            if( filter.tags ){
                params.tags = filter.tags;
                params.grouping = filter.grouping;
            }

            this.fetch({
                url:this.url + '/album/' + filter.album,
                reset:true,
                contentType:'application/json',
                data:params
            });
        }

    });
});