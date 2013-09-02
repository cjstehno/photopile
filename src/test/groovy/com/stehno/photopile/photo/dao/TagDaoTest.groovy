package com.stehno.photopile.photo.dao
import com.stehno.photopile.image.ImageConfig
import com.stehno.photopile.photo.PhotoConfig
import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Tag
import com.stehno.photopile.test.config.TestConfig
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes=[TestConfig, PhotoConfig, ImageConfig])
class TagDaoTest extends AbstractDaoTest {

    static TABLES = ['tags']

    @Autowired private TagDao tagDao

    @Test void 'create'(){
        def tagId = createTag('alpha')

        fetchAndAssert tagId, 'alpha'
    }

    @Test void 'update'(){
        def tagId = createTag('alpha')

        withinTransaction {
            tagDao.update( new Tag(id:tagId, name:'bravo') )
        }

        fetchAndAssert tagId, 'bravo'
    }

    @Test void 'delete'(){
        def tagId = createTag('alpha')

        withinTransaction {
            assert tagDao.exists('alpha')
        }

        withinTransaction {
            def deleted = tagDao.delete tagId
            assert deleted
        }

        withinTransaction {
            assert !tagDao.exists('alpha')
        }
    }

    private void fetchAndAssert( final long tagId, final String name ){
        withinTransaction {
            def fetchedTag = tagDao.fetch( tagId )
            assert fetchedTag.id == tagId
            assert fetchedTag.name == name
        }
    }

    private long createTag( final String name ){
        withinTransaction {
            def tag = new Tag( name:name )
            def id = tagDao.create tag

            assert id
            assert tag.id

            return id
        } as long
    }
}
