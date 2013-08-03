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

        initialize:function(){
            if(this.options.panels){
                console.log(this.options.panels);
            }
        },

        render:function(){


            return this;
        }
    });
});