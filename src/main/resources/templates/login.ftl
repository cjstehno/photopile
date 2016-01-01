<!DOCTYPE html>
<html>
<head>
    <title>PhotoPile: Your photos, your way.</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="stylesheet" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" href="css/start-page.css"/>

    <style type="text/css">
        body {
            padding-top: 50px;
        }
    </style>
</head>
<body>

<div class="container-fluid">
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
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="help.html" target="_blank"><span class="glyphicon glyphicon-question-sign"></span> Help</a></li>
                    <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="jumbotron">
        <h1>Photopile</h1>
        <p>Your photos, your way.</p>
    </div>

    <div class="row">
        <div class="col-sm-2">&nbsp;</div>
        <div class="col-sm-8">

            <form name="form" action="/login" method="POST">
                <div class="form-group">
                    <label>Username:
                        <input type="text" class="form-control" name="username" placeholder="Enter username" />
                    </label>
                </div>
                <div class="form-group">
                    <label>Password:
                        <input type="password" class="form-control" name="password" placeholder="Enter password" />
                    </label>
                </div>
                <button type="submit" class="btn btn-success">Sign In</button>
            </form>

        </div>
        <div class="col-sm-2">&nbsp;</div>
    </div>
</div>

<script src="js/libs/jquery-2.1.4.min.js"></script>
<script src="js/libs/bootstrap.min.js"></script>

</body>
</html>
