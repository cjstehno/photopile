package com.stehno.photopile.repository

import com.stehno.photopile.service.PhotoOrderBy
import groovy.transform.TypeChecked

/**
 * Created by cjstehno on 1/16/16.
 */
@TypeChecked
class PhotoSelectBuilder {

    private static final String SQL = '''
            select
                p.id,p.version,p.name,p.description,p.hash,p.date_uploaded,p.date_updated,p.date_taken,p.geo_latitude,p.geo_longitude,p.geo_altitude,
                t.id as tag_id,t.category as tag_category,t.label as tag_label,
                i.id as image_id,i.scale as image_scale,i.width as image_width,i.height as image_height,i.content_length as image_content_length,i.content_type as image_content_type
            from photos p
            left outer join photo_tags pt on pt.photo_id=p.id
            left outer join tags t on t.id=pt.tag_id
            left outer join photo_images pi on pi.photo_id=p.id
            left outer join images i on i.id=pi.image_id
        '''

    private final List<String> wheres = []
    private final List<Object> args = []
    private String orderBy

    static PhotoSelectBuilder select() {
        new PhotoSelectBuilder()
    }

    PhotoSelectBuilder filterById(long id) {
        wheres << 'p.id = ?'
        args << id
        this
    }

    PhotoSelectBuilder filterByPhotoIds(List<Long> photoIds) {
        wheres << ("p.id in (${photoIds.collect { '?' }.join(',')})" as String)
        args.addAll(photoIds)
        this
    }

    PhotoSelectBuilder orderBy(PhotoOrderBy orderBy) {
        this.orderBy = "order by ${orderBy.field.field} ${orderBy.direction}"
        this
    }

    Object[] arguments() {
        args as Object[]
    }

    String sql() {
        StringBuilder str = new StringBuilder(SQL)

        if (wheres) {
            str.append(' where ').append(wheres.join(' and '))
        }

        if (orderBy) {
            str.append(' ').append(orderBy)
        }

        str.toString()
    }
}
