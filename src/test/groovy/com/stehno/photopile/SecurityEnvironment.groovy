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

package com.stehno.photopile

import org.apache.shiro.SecurityUtils
import org.apache.shiro.UnavailableSecurityManagerException
import org.apache.shiro.subject.Subject
import org.apache.shiro.subject.support.SubjectThreadState
import org.apache.shiro.util.LifecycleUtils
import org.apache.shiro.util.ThreadState
import org.junit.rules.ExternalResource

/**
 * Provides a test-implementation of the Shiro security environment to be used in unit tests. Generally, this
 * should be used as a ClassRule.
 *
 * As a class rule, this environment will only clean up after all tests have run for the class; however, you may
 * want to call clearSubject() after each individual test to clean up the subject settings.
 *
 * This class is based on the suggested unit test base class provided by the Shiro team (http://shiro.apache.org/testing.html).
 */
class SecurityEnvironment extends ExternalResource {

    private static ThreadState THREAD_STATE

    /**
     *
     * @param subject the Subject instance
     */
    void setSubject(Subject subject) {
        clearSubject()
        THREAD_STATE = new SubjectThreadState(subject)
        THREAD_STATE.bind()
    }

    Subject getSubject() {
        return SecurityUtils.getSubject()
    }

    void clearSubject(){
        doClearSubject()
    }

    void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
    }

    SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    @Override
    protected void after() {
        doClearSubject()

        try {
            SecurityManager securityManager = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            /* we don't care about this when cleaning up the test environment */
        }

        setSecurityManager(null);
    }

    private static void doClearSubject() {
        if (THREAD_STATE != null) {
            THREAD_STATE.clear();
            THREAD_STATE = null;
        }
    }
}
