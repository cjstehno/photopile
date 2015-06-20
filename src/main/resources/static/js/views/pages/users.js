define([
    'models/user',
    'collections/users',
    'views/pages/user-dialog',
    'text!templates/pages/users.html',
    'text!templates/pages/user-row.html'
], function (User, Users, UserDialog, template, rowTemplate) {

    return Backbone.View.extend({
        template: _.template(template),
        rowTemplate: _.template(rowTemplate),

        events: {
            'click a[href="#add"]': 'showAddDialog',
            'click a[href="#edit"]': 'showEditDialog',
            'click a[href="#remove"]': 'removeUser'
        },

        initialize: function () {
            this.collection = new Users();
            this.collection.on('reset', _.bind(this.renderUsers, this));
        },

        render: function () {
            this.$el.prepend(this.template());

            this.collection.fetch({reset: true});

            return this;
        },

        renderUsers: function () {
            var tableRows = this.$('tbody');
            tableRows.empty();

            this.collection.each(function (usr) {
                tableRows.append(this.rowTemplate({'_':_, user: usr.toJSON()}));
            }, this);
        },

        removeUser: function (evt) {
            evt.preventDefault();

            // FIXME: add confirmation dialog

            var user = this.collection.get(parseInt($(evt.currentTarget).attr('data-id')));

            user.destroy({
                wait: true,
                success: _.bind(this.renderUsers, this),
                error: function (mod, res, opt) {
                    console.log('error');
                }
            });
        },

        showAddDialog: function (evt) {
            evt.preventDefault();

            var user = new User();
            user.on('change', function(){
                this.collection.add(user);
                this.renderUsers();
            }, this);

            var userDialog = new UserDialog({model: user});
            userDialog.render();
        },

        showEditDialog: function (evt) {
            evt.preventDefault();

            var user = this.collection.get(parseInt($(evt.currentTarget).attr('data-id')));
            user.on('change', _.bind(this.renderUsers, this));

            var userDialog = new UserDialog({model: user});

            user.fetch({
                success: _.bind(userDialog.render, userDialog)
            });
        }
    });
});
