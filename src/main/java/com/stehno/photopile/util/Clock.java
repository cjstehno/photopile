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

package com.stehno.photopile.util;

import org.apache.commons.lang.time.StopWatch;
import org.apache.logging.log4j.Logger;

/**
 * A simple logging timer.
 *
 * The log messages will always be logged at the DEBUG level. If the DEBUG level is not enabled, no
 * operations will be performed.
 *
 * NOTE: Perf4j would be preferable; however, it does not currently work with log4j 2. It may be interesting
 * to contribute support for it, but for now I have other things to do.
 */
public class Clock {

    private final String tag;
    private final Logger log;
    private final StopWatch stopWatch;

    /**
     * Creates the timing clock and starts the timer.
     *
     * @param tag the log message, should accept one parameter for time in ms
     * @param log the logger to be used
     */
    public Clock( final String tag, final Logger log ){
        this.tag = tag;
        this.log = log;
        this.stopWatch = new StopWatch();

        if( log.isDebugEnabled() ){
            stopWatch.start();
        }
    }

    /**
     * Stops the timer and logs the recorded duration.
     */
    public void stop(){
        if( log.isDebugEnabled() ){
            stopWatch.stop();

            log.debug( tag, stopWatch.getTime() );
        }
    }
}
