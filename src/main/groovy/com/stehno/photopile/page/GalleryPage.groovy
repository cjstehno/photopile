package com.stehno.photopile.page

import com.stehno.photopile.entity.Photo
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

/**
 * Created by cstehno on 1/24/2016.
 */
@TypeChecked @EqualsAndHashCode(includeFields = true) @ToString(includeFields = true, includeNames = true)
class GalleryPage {

    final PaginationControls pagination
    final int columnCount

    private final List<List<Photo>> rows = []

    GalleryPage(final Collection<Photo> photos, final PaginationControls pageControls, final int columnCount=4) {
        this.pagination = pageControls
        this.columnCount = columnCount
        addPhotos photos
    }

    void eachRow(Closure closure) {
        rows.each { row ->
            closure(row)
        }
    }

    private void addPhotos(final Collection<Photo> photos) {
        def row = []
        photos.eachWithIndex { Photo entry, i ->
            row << entry
            if ((i + 1) % columnCount == 0) {
                rows << new LinkedList<Photo>(row)
                row.clear()
            }
        }
        rows << row
    }
}

@TypeChecked @EqualsAndHashCode(includeFields = true) @ToString(includeNames = true, includeFields = true)
class PaginationControls {

    final int currentPage
    final int pageCount

    PaginationControls(final int currentPage, final int pageSize, final int totalCount) {
        this.currentPage = currentPage
        this.pageCount = ((totalCount / pageSize) as int) + (totalCount % pageSize == 0 ? 0 : 1)
    }

    boolean isFirstPage(){
        currentPage == 1
    }

    boolean isLastPage(){
        currentPage == pageCount
    }

    int getNextPage() {
        currentPage + 1 > pageCount ? pageCount : currentPage + 1
    }

    int getPreviousPage() {
        currentPage - 1 < 1 ? 1 : currentPage - 1
    }
}