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

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
<head>
    <title>PhotoPile: Your photos, your way.</title>
    <link href="http://localhost:8080/photopile/css/bootstrap.min.css" type="text/css" rel="stylesheet" media="screen">
    <link href="http://localhost:8080/photopile/css/login.css" type="text/css" rel="stylesheet" media="screen">
</head>
<body>

<div class="container-fluid">
    <div class="page-header">
        <h1><img src="../img/icon.png"/>PhotoPile:
            <small>Your photos, your way.</small>
        </h1>
    </div>

    <spring:form commandName="credentials" method="POST" class="form-horizontal">
        <spring:errors/>
        <div class="control-group">
            <label class="control-label" for="username">Username:</label>

            <div class="controls">
                <spring:input path="username" id="username" placeholder="Username..."/>
                <spring:errors path="username"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="password">Password:</label>

            <div class="controls">
                <spring:password path="password" id="password" placeholder="Password..."/>
                <spring:errors path="password"/>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <spring:checkbox path="rememberMe" id="rememberMe"/> Remember me
                </label>
                <button type="submit" class="btn">Sign in</button>
            </div>
        </div>
    </spring:form>
</div>


<script src="http://localhost:8080/photopile/js/libs/jquery.js"></script>
<script src="http://localhost:8080/photopile/js/libs/bootstrap.min.js"></script>
<script src="http://localhost:8080/photopile/js/libs/underscore.min.js"></script>
<script src="http://localhost:8080/photopile/js/libs/backbone.min.js"></script>
</body>
</html>

