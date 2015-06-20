define([
    'text!templates/pages/user-dialog.html'
], function (template) {

    return Backbone.View.extend({
        template: _.template(template),

        attributes: {
            class: 'modal fade'
        },

        events: {
            'click button.btn-primary': 'saveUser'
        },

        render: function () {
            this.$el.html(this.template({user: this.model.toJSON()}));
            this.$el.modal();

            return this;
        },

        saveUser: function (evt) {
            this.clearErrors();

            var self = this;

            this.model.save(
                {
                    username: this.$('#username').val(),
                    displayName: this.$('#displayName').val(),
                    password: this.$('#password').val(),
                    passwordConfirm: this.$('#passwordConfirm').val(),
                    authorities: [this.$('input:checkbox[name=roles]').find(':checked').val()],
                    enabled: this.$('input:checkbox[name=enabled]').val(),
                    accountExpired: this.$('input:checkbox[name=accountExpired]').val(),
                    credentialsExpired: this.$('input:checkbox[name=credentialsExpired]').val(),
                    accountLocked: this.$('input:checkbox[name=accountLocked]').val()
                },
                {
                    wait: true,

                    success: function (mod, res, opt) {
                        self.$el.modal('hide');
                    },

                    error: _.bind(this.handleErrors, this)
                }
            );
        },

        clearErrors: function () {
            this.$('.form-group').removeClass('has-error');
            this.$('.alert.alert-danger').addClass('hidden');
        },

        handleErrors: function (mod, res, opt) {
            var errors = JSON.parse(res.responseText);

            _.each(errors.fieldErrors, _.bind(this.flagFieldError, this));
            _.each(errors.objectErrors, _.bind(this.flagObjectError, this));
        },

        flagObjectError: function (err) {
            var alertBox = this.$('.alert.alert-danger');

            var errorList = this.$('ul.global-errors');
            errorList.append('<li>' + err.defaultMessage + '</li>')

            alertBox.removeClass('hidden');
        },

        flagFieldError: function (err) {
            var formGroup = this.$('input[name=' + err.field + ']').parent('.form-group');
            formGroup.addClass('has-error');
            formGroup.attr('title', err.defaultMessage);
        }
    });
});