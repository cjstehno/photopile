<%= utils.include('header', pageCss:'css/normal-page.css')  %>

<div class="container-fluid">
    <div class="container-fluid">
        <%= utils.include('menubar', activeMenu:'gallery') %>

        <div class="content-list">
            <div class="row" style="margin-bottom: 4px;">
                <div class="col-md-9">
                    <div class="btn-group">
                        <button type="button" class="btn btn-xs btn-default"><span class="glyphicon glyphicon-book"></span> Any Album</button>
                        <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li class="active"><a href="#">All Albums</a></li>
                            <li><a href="#more" data-toggle="modal" data-target="#album-selector">More...</a></li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-xs btn-default"><span class="glyphicon glyphicon-tags"></span> Any Tags</button>
                        <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li class="active"><a href="#all">Any Tags</a></li>
                            <li><a href="#more" data-toggle="modal" data-target="#tag-selector">More...</a></li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-xs btn-default"><span class="glyphicon glyphicon-calendar"></span> Date Taken</button>
                        <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li class="active"><a href="#all">Date Taken</a></li>
                            <li><a href="#all">Date Uploaded</a></li>
                            <li><a href="#all">Date Modified</a></li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-xs btn-default"><span class="glyphicon glyphicon-calendar"></span> Most Recent</button>
                        <button type="button" class="btn btn-xs btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li class="active"><a href="#all">Most Recent</a></li>
                            <li><a href="#all">Least Recent</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="pull-right">
                        <a href="#" class="btn btn-xs btn-default" role="button"><span class="glyphicon glyphicon-fast-backward"></span></a>
                        <a href="#" class="btn btn-xs btn-default" role="button"><span class="glyphicon glyphicon-backward"></span></a>
                        <span>1 of 3</span>
                        <a href="#" class="btn btn-xs btn-default" role="button"><span class="glyphicon glyphicon-forward"></span></a>
                        <a href="#" class="btn btn-xs btn-default" role="button"><span class="glyphicon glyphicon-fast-forward"></span></a>
                    </div>
                </div>
            </div>

        <% page.eachRow { row-> %>
            <div class="row">
                <% row.each { photo-> %>
                    <div class="col-md-3">
                        <a href="onephoto.html" class="thumbnail"><img src="/image/FULL/${photo.id}"/></a>
                    </div>
                <% } %>
            </div>
        <% } %>

        </div>

    </div>
</div>

<%= utils.include('footer') %>