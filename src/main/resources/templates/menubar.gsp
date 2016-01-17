<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#main-navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/" title="Your photos, your way."><strong>Photopile</strong></a>
        </div>

        <div class="collapse navbar-collapse" id="main-navbar">
            <ul class="nav navbar-nav">
                <li <% if(activeMenu == 'gallery'){ %>class="active"<% } %>><a href="#gallery"><span class="glyphicon glyphicon-picture"></span> Gallery</a></li>
                <li <% if(activeMenu == 'map'){ %>class="active"<% } %>><a href="#map"><span class="glyphicon glyphicon-globe"></span> Map</a></li>
                <li <% if(activeMenu == 'albums'){ %>class="active"<% } %>><a href="#albums"><span class="glyphicon glyphicon-book"></span> Albums</a></li>
                <li <% if(activeMenu == 'tags'){ %>class="active"<% } %>><a href="#tags"><span class="glyphicon glyphicon-tags"></span> Tags</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="help.html" target="_blank"><span class="glyphicon glyphicon-question-sign"></span> Help</a></li>
                <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Exit</a></li>
            </ul>
        </div>
    </div>
</nav>