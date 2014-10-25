package com.stehno.gsm

import com.stehno.photopile.common.SortBy
import org.junit.Test

import static com.stehno.photopile.common.SortBy.Direction.ASCENDING

class SqlMappingsTest {

    @Test void 'verify mappings from script'(){
        SqlMappings sqlMappings = SqlMappings.compile('/testmappings.gql')

        assert sqlMappings.mappings.size() == 8

        assert sqlMappings.sql('COUNT') == 'select count(*) from somewhere'
        assert sqlMappings.mapping('COUNT') == 'select count(*) from somewhere'

        def sortOrder = new SortBy( field:'alpha', direction:ASCENDING )
        assert sqlMappings.sql('_ORDERING_', sortOrder) == 'order by alpha asc, id  asc'

        assert sqlMappings.sql('SELECT_SORTED', sortOrder) == 'select a,b,c from somewhere order by alpha asc, id  asc'

        assert sqlMappings.sql('QUERY_1', 1) == 'select a,b,c from somewhere where a=1'
        assert sqlMappings.sql('QUERY_2', 1, 2) == 'select a,b,c from somewhere where a=1 and b=2'
        assert sqlMappings.sql('QUERY_3', 1, 2, 3) == 'select a,b,c from somewhere where a=1 and b=2 and c=3'
    }

    @Test void 'verify mappings from inline'(){
        def sqlMappings = new SqlMappings().mappings {
            map('alpha'){
                'select a from b where c=?'
            }
            map('bravo'){
                return { x->
                    "select $x from y"
                }
            }
        }

        assert sqlMappings.mappings.size() == 2
        assert sqlMappings.sql('alpha') == 'select a from b where c=?'
        assert sqlMappings.sql('bravo', 'something') == 'select something from y'
    }
}
