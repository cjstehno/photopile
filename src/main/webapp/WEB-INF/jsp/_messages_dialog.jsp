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

<div id="messages-dialog" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h3 id="myModalLabel">Messages</h3>
    </div>
    <div class="modal-body">
        <div class="message-list hide">
            <table class="table table-striped" style="height:200px;overflow: auto;">
                <thead>
                <tr>
                    <th>Type</th>
                    <th>Title</th>
                    <th>Date</th>
                    <th>Read</th>
                    <th>&nbsp;</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Info</td>
                    <td><a href="#messages/1">Something interesting</a></td>
                    <td>01/29/2013 @ 12:51pm</td>
                    <td>No</td>
                    <td><i class="icon-ok" title="Mark as read"></i> <i class="icon-remove" title="Remove"></i></td>
                </tr>
                <tr class="error">
                    <td>Error</td>
                    <td><a href="#messages/2">Something bad</a></td>
                    <td>01/29/2013 @ 12:51pm</td>
                    <td>Yes</td>
                    <td><i class="icon-ok"></i> <i class="icon-remove"></i></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="message-view">
            <p>Your message...when you click a message title, this panel should slide in and have a close, mark read and delete button.</p>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-link pull-left">Help</button>
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    </div>
</div>