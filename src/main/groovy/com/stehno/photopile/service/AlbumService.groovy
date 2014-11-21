/*
 * Copyright (c) 2014 Christopher J. Stehno
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

package com.stehno.photopile.service

/**
 * Created by cjstehno on 11/16/2014.
 */
interface AlbumService {

    /*
	POST /album - add a new album
		- content is data

	PUT /album/{albumid} - update existign album

	GET /album/{albumid} - get data for a specific album

	GET /album/{albumid}/photos[?offset={}&limit={}] - retrieve the photos for a specified album

	GET /album[?offset={}&limit={}] - list albums with optional page offset/limit

	DELETE /album/{albumid} - delete an album (not contents)
     */
}