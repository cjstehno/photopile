<%--
  ~ Copyright (c) 2013 Christopher J. Stehno
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~         http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>PhotoPile: Your photos, your way.</title>
    <link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" media="screen">
    <link href="css/photopile.css" type="text/css" rel="stylesheet" media="screen">
</head>
<body>

<jsp:directive.include file="_import_dialog.jsp"/>
<jsp:directive.include file="_messages_dialog.jsp"/>

<div class="container-fluid">
    <div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <a class="brand" href="">PhotoPile</a>

            <div class="btn-group">
                <button class="btn">All Photos</button>
                <button class="btn dropdown-toggle" data-toggle="dropdown">
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li><a href="#photos">All Photos</a></li>
                    <li><a href="#albums">Albums</a></li>
                    <li><a href="#tags">Tags</a></li>
                    <li><a href="#dateTaken">Date Taken</a></li>
                </ul>
            </div>

            <div class="btn-group">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    New
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="#">Album</a></li>
                    <li><a href="#">Photo</a></li>
                    <li class="divider"></li>
                    <li><a href="#photoImport">Photo Import</a></li>
                </ul>
            </div>

            <ul class="nav pull-right">
                <li><a href="#messages">Messages <span class="badge badge-success">2</span></a></li>
                <li><a href="#">Selected <span class="badge badge-info">8</span></a></li>
                <li class="divider-vertical"></li>
                <li>
                    <div class="btn-group">
                        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                            Help
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="#"><i class="icon-question-sign"></i> Help Topics</a></li>
                            <li><a href="#"><i class="icon-info-sign"></i> About</a></li>
                        </ul>
                    </div>
                </li>
                <li><a href="#"><i class="icon-wrench"></i> Settings</a></li>
                <li><a href="auth/logout"><i class="icon-off"></i> Logout</a></li>
            </ul>

        </div>
    </div>

    <div class="row-fluid">
        <div class="span6">
            <ul class="breadcrumb">
                <li><a href="#">Albums</a> <span class="divider">/</span></li>
                <li><a href="#">Christmas 2010</a> <span class="divider">/</span></li>
                <li class="active">
                    <div class="btn-group">
                        <a class="btn dropdown-toggle btn-mini" data-toggle="dropdown" href="#">
                            Tags...
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="#">Album</a></li>
                            <li><a href="#">Photo Upload</a></li>
                            <li><a href="#">Photo Import</a></li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
        <div class="span6">
            <div class="pagination pagination-right pagination-mini no-top-margin">
                <ul>
                    <li class="disabled"><a href="#">Prev</a></li>
                    <li class="active"><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">Next</a></li>
                </ul>
            </div>
        </div>
    </div>

    <!-- FIXME: can this be moved into head? -->
    <jsp:directive.include file="_gallery_templates.jsp"/>

    <div id="gallery-container"></div>

</div>

<!-- FIXME: not sure why they put these at the bottom, its not really valid is it? -->
<script src="http://code.jquery.com/jquery-latest.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/underscore-min.js" type="text/javascript"></script>
<script src="js/backbone-min.js" type="text/javascript"></script>
<script src="js/pp.models.js" type="text/javascript"></script>
<script src="js/pp.views.js" type="text/javascript"></script>
<script src="js/pp.routes.js" type="text/javascript"></script>
<script src="js/photopile.js" type="text/javascript"></script>
</body>
</html>

