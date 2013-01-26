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

<div id="import-dialog" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h3 id="myModalLabel">Import photos from server...</h3>
    </div>
    <div class="modal-body">
        <div class="card">
            <div class="well well-small"><span class="label label-important">Important:</span> This ia a long-running
                server
                operation that will be loading files from the server; be sure you know what you are doing.
            </div>
            <p>Enter the directory (accessible to the server) where your photos are stored:</p>

            <form>
                <fieldset>
                    <label>Directory:</label>
                    <input type="text" placeholder="/some/path"/>
                    <span class="help-block">(relative to the server filesystem root)</span>
                    <label class="checkbox"><input type="checkbox"/>I understand what I am doing.</label>
                </fieldset>
            </form>
        </div>
        <div class="card" style="display: none;">
            <p>You selected directory is being scanned. You will receive a notification message when the summary
                is ready for review.</p>
        </div>
    </div>
    <div class="modal-footer">
        <div class="card">
            <button class="btn btn-link pull-left">Help</button>
            <button class="btn btn-primary disabled">Import</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        </div>
        <div class="card" style="display: none;">
            <button class="btn btn-link pull-left">Help</button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        </div>
    </div>
</div>