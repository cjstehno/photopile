package com.stehno.photopile.photo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 2/27/13
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan({
    "com.stehno.photopile.photo.dao",
    "com.stehno.photopile.photo.service",
    "com.stehno.photopile.photo.controller"
})
public class PhotoConfig {
}
