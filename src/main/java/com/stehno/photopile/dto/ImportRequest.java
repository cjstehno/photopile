/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.photopile.dto;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/29/13
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportRequest {

    private String path;
    private boolean preview;
    private boolean understand;

    public String getPath(){
        return path;
    }

    public void setPath( final String path ){
        this.path = path;
    }

    public boolean isPreview(){
        return preview;
    }

    public void setPreview( final boolean preview ){
        this.preview = preview;
    }

    public boolean isUnderstand(){
        return understand;
    }

    public void setUnderstand( final boolean understand ){
        this.understand = understand;
    }

    @Override
    public String toString(){
        return "ImportRequest{" +
            "path='" + path + '\'' +
            ", preview=" + preview +
            ", understand=" + understand +
            '}';
    }
}
