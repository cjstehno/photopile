define([
    'text!templates/dialogs/create-project-dialog.html'
], function (template) {

    return Backbone.View.extend({
        template: _.template(template),

        attributes: {
            class: 'modal fade'
        },

        events: {
            'click button.btn-primary': 'createProject'
        },

        render: function () {
            this.$el.html(this.template());
            this.$el.modal();

            return this;
        },

        createProject: function (evt) {
            this.clearErrors();

            var self = this;

            this.model.save(
                {
                    name: this.$('#name').val(),
                    directory: this.$('#directory').val(),
                    includes: this.$('#includes').val(),
                    excludes: this.$('#excludes').val()
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

        // FIXME: these should probably be extracted into a utility or something

        clearErrors: function () {
            this.$('.form-group').removeClass('has-error');
            this.$('.field-error').remove();
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
            var formField = this.$('#' + err.field);

            var formGroup = formField.closest('.form-group');
            formGroup.addClass('has-error');

            formField.after('<div class="field-error text-danger text-capitalize">' + err.defaultMessage + '.</div>');
        }
    });
});