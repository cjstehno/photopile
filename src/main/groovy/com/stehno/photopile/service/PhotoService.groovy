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
import com.stehno.photopile.domain.Photo
/**
 * Service used to manage and access photos and their images.
 */
interface PhotoService {

    /**
     * Persists a new photo to the database. The incoming Photo object will be updated with the missing data if the creation succeeds.
     * Any metadata specified in the incoming photo object will override the metadata contained in the image file content (found via extraction).
     *
     * @param photo the photo metadata
     * @param image the photo image object to be associated with the photo
     * @return the newly created photo object with all populated data
     */
    Photo create(final File contentFile, final PhotoInfo info)

    void createAsync(final File contentFile, final PhotoInfo info)

    Photo create(final File contentFile)

    void createAsync(final File contentFile)
    /*
        come up with an import controller
        might not need services per-se

        need a means of creating a photo async and sync (createAsync and create)

        provide a PhotoInfo dto type object that allows overriding of some photo/image meta data
        start from controller layer as transactional, prob wont need much in services
     */

    /**
     * Updates an existing photo with the contents of the incoming Photo object. The image content itself is not modified.
     * The incoming and returned Photo objects will be the same if the update succeeds.
     *
     * @param photo the photo containing updated information
     * @return the updated Photo object
     */
//    Photo update(final Photo photo)

    /**
     * Updates an existing photo with the contents of the incoming Photo object. The image content itself is replaced with that from the specified
     * image content. The incoming and returned Photo objects will be the same if the update succeeds.
     *
     * @param photo the photo containing updated information
     * @param image the PhotoImage object to be associated with the photo
     * @return the updated Photo object
     */
//    Photo update(final Photo photo, final PhotoImage image)

    /**
     * Retrieves the photo information associated with the specified photo id.
     *
     * @param photoId the id of the photo to be retrieved.
     * @return the Photo represented by the given id
     */
//    Photo retrieve(final long photoId)

    /**
     * Fetches the image content for the specified photo at the given scale factor.
     *
     * @param photoId the photo whose image is to be retrieved
     * @param scale the scale of the image to be retrieved
     * @return the PhotoImage object containing the image information for the photo
     */
//    PhotoImage fetchImage(final long photoId, final ImageScale scale)

    /**
     * Deletes the photo with the specified photo id. The system does not actually destroy deleted photos, they are put into an archived state so that
     * they may be manually removed or restored at a later time.
     *
     * @param photoId the photo id
     * @return a value of true if a photo was actually deleted
     */
//    boolean delete(final long photoId)

    /**
     * Lists all photos in the system in a paged and sorted manner.
     *
     * @param pageBy the paging information
     * @param sortBy the sorting information
     * @return a list of photos corresponding to the paging and sorting criteria
     */
//    List<Photo> listPhotos(final PageBy pageBy, final SortBy sortBy)
}