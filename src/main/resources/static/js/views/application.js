define([
    'views/common/menubar',
    'views/pages/users',
    'text!templates/application.html'
], function( MenuBar, UsersPage, template ){

    /*
        common:
            import dialog

        pages:
            gallery
            map
            albums
            tags
            profile
            help (new controller and view)
            admin: users
            (admin: others)
     */

    return Backbone.View.extend({
        template: _.template(template),

        initialize:function(){
            this.on('page-change', _.bind(this.onPageChanged, this));
        },

        render:function(){
            this.$el.prepend( this.template() );

            var menuBar = new MenuBar({ el:this.$('.navbar') }).render();
            this.on('page-change', _.bind(menuBar.onPageChange, menuBar));

            return this;
        },

        onPageChanged:function( evt){
            /// FIXME: this should probably be in a map or something
            if(evt.id == 'admin/users'){
                var usersPage = new UsersPage({el:this.$('div.page-content')}).render();
            }
        }
    });
});
