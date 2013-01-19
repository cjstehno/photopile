package com.stehno.photopile.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;

/**
 */
@Configuration
public class SecurityConfig {

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    public Realm securityRealm(){
        // FIXME: this is just for kick-staring dev...

        SimpleAccountRealm realm = new SimpleAccountRealm();

        realm.addRole("Admin");
        realm.addRole("User");

        realm.addAccount( "admin", "admin", "Admin", "User");
        realm.addAccount( "user", "user", "User");

        return realm;
    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(securityRealm());
        return securityManager;
    }

    @Bean
    public Filter securityFilter() throws Exception {
        final ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager());

        filterFactoryBean.setLoginUrl("/auth/login");
        filterFactoryBean.setSuccessUrl("/");
        filterFactoryBean.setUnauthorizedUrl("/auth/unauthorized");

        filterFactoryBean.setFilterChainDefinitionMap(new HashMap<String, String>(){
            {
                put("/favicon.ico", DefaultFilter.anon.name());
                put("/**", DefaultFilter.authc.name());
            }
        });

        return (Filter)filterFactoryBean.getObject();
    }
}
