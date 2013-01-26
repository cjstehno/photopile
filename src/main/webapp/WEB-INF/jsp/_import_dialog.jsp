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
        <div>
            <p>Enter the directory (accessible to the server) where your photos are stored:</p>

            <form>
                <fieldset>
                    <label>Directory:</label>
                    <input type="text" placeholder="/some/path">
                    <span class="help-block">(relative to the server root)</span>
                </fieldset>
            </form>
        </div>
        <div style="display: none;">
            <p>You selected directory is being scanned...</p>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-link pull-left">Help</button>
        <button class="btn btn-primary">Import</button>
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    </div>
</div>