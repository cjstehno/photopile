define([
    'text!templates/common/carousel.html'
], function( template ){

    /**
     * Provides a Backbone view on top of a Bootstrap carousel.
     *
     * NOTE: controls and indicators are not supported yet
     */
    return Backbone.View.extend({
        carouselTemplate: _.template(template),
        panelTemplate: _.template('<div class="item<%- first ? " active" : "" %>"></div>'),

        initialize:function(){
            if( this.options && this.options.panels ){
                this.panels = this.options.panels;
            }
        },

        render:function(){
            this.$el.html( this.carouselTemplate() );

            this.$elements = {
                carousel:this.$('.carousel'),
                panels:this.$('.carousel-inner')
            };

            var i = 0
            _.each(this.panels, function(panel){
                var $panel = $(this.panelTemplate({ first:(i === 0) }));

                this.$elements.panels.append($panel);

                panel.$el = $panel;
                panel.render();

                i++;
            }, this);

            this.$elements.carousel.carousel('pause');

            return this;
        },

        nextPanel:function(){
            this.$elements.carousel.carousel('next');
        },

        prevPanel:function(){
            this.$elements.carousel.carousel('prev');
        }
    });
});