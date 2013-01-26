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

<script type="text/template" id="gallery-cell-template">
    <a href="#" class="thumbnail"><img src="http://localhost:8080/photopile/image/{{photo_id}}"/></a>
</script>

<!-- Gallery template (start) -->
<script type="text/template" id="gallery-row-template">
    <div class="row-fluid">
        <div class="span12">
            <ul class="thumbnails">
                <li class="span2">{{gallery_cell_1}}</li>
                <li class="span2">{{gallery_cell_2}}</li>
                <li class="span2">{{gallery_cell_3}}</li>
                <li class="span2">{{gallery_cell_4}}</li>
                <li class="span2">{{gallery_cell_5}}</li>
                <li class="span2">{{gallery_cell_6}}</li>
            </ul>
        </div>
    </div>
</script>
<!-- Gallery template (end) -->