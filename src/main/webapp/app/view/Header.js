Ext.define( 'PP.view.Header', {
    extend:'Ext.panel.Panel',
    alias:'widget.app-header',
    html:'<div  class="x-panel-header"><span>PhotoPile:</span> <span class="x-panel-subheader">Your photos, your way.</span></div>',
    autoHeight:true,
    border:false,
    margins:'0 0 5 0',
    bbar:[
        {
            text:'New Photo'
        },
        { text:'New Album' },
        { text:'Search' },
        { xtype:'tbfill' },
        {
            text:'Settings',
            iconCls:'icon-settings',
            menu:{
                xtype:'menu',
                items:[
                    {
                        text:'Users'
                    },
                    {
                        text:'Properties'
                    }
                ]
            }
        },
        {
            text:'About',
            iconCls:'icon-about'
        },
        {
            text:'Help',
            iconCls:'icon-help'
        }
    ]
});
