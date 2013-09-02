package com.stehno.photopile.photo.dao

import com.stehno.photopile.test.Integration
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate

@org.junit.experimental.categories.Category(Integration)
@RunWith(SpringJUnit4ClassRunner)
@TestExecutionListeners([DatabaseTestExecutionListener, DependencyInjectionTestExecutionListener, TransactionalTestExecutionListener])
abstract class AbstractDaoTest {

    @Autowired private PlatformTransactionManager transactionManager

    def withinTransaction( Closure ops ){
        new TransactionTemplate(transactionManager).execute(new GroovyTransactionCallback(closure:ops))
    }
}

class GroovyTransactionCallback implements TransactionCallback {

    Closure closure

    @Override
    Object doInTransaction( final TransactionStatus transactionStatus ){
        try {
            return closure()
        } catch( ex ){
            ex.printStackTrace()
            return null
        }
    }
}