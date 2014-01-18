define([
    'collections/photos',
    'text!templates/map/container.html',
    'text!templates/map/map-popup.html'
], function( Photos, template, popupTemplate ){

    return Backbone.View.extend({
        template: _.template(template),
        popupTemplate: _.template(popupTemplate),

        initialize:function(){
            this.collection = new Photos();
            this.collection.on('reset', _.bind(this.renderPhotos, this));
        },

        render:function(){
            this.$el.append( this.template() );

            this.map = L.map('map');

            var that = this;
            if( navigator.geolocation ){
                navigator.geolocation.getCurrentPosition(function(pos){
                    that.map.setView([pos.coords.latitude, pos.coords.longitude], 9);

                    // add an OpenStreetMap tile layer
                    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo(that.map);

                    // FIXME: need to call when map is moved

                    that.collection.fetchWithin( that.map.getBounds().toBBoxString() );
                });
            } else {
                this.map.fitWorld();
            }

            return this;
        },

        renderPhotos:function(){
            var icon = L.icon({ iconUrl: 'img/camera.png' });

            this.collection.each(function(photo){
                var loc = photo.get('location');
                L.marker([ loc.latitude, loc.longitude ], { icon:icon })
                    .addTo(this.map)
                    .on('click', _.bind(this.onPhotoClick, this, photo))
                    .on('mouseover', _.bind(this.onMarkerMouseover, this, photo))
                    .on('mouseout', _.bind(this.onMarkerMouseout, this));

            }, this);
        },

        onMarkerMouseover:function(model, e){
            L.popup()
                .setLatLng(e.latlng)
                .setContent(this.popupTemplate(model.toJSON()))
                .openOn(this.map);
        },

        onMarkerMouseout:function(){
            this.map.closePopup();
        },

        onPhotoClick:function(model){
//            this.trigger('photo-selected', model.toJSON());
            console.log('photo clicked - open color box?')
            return false;
        }

    });
});