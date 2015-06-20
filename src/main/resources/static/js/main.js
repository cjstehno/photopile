require.config({
    paths: {
        text: 'libs/text',
        templates: '../templates'
    },
    shim: {}
});

require([ 'app' ], function( Application ) {
    new Application().initialize();
});