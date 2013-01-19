package com.stehno.photopile.dto;

import org.apache.shiro.authc.UsernamePasswordToken;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 */
public class AuthCredentials {

    @NotNull @Size( min=4, max=25 )
    private String username;

    @NotNull @Size( min=4, max=25 )
    private String password;

    private boolean rememberMe;

    public void setRememberMe( boolean rememberMe ){
        this.rememberMe = rememberMe;
    }

    public boolean getRememberMe(){
        return rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernamePasswordToken asToken(){
        final UsernamePasswordToken token = new UsernamePasswordToken( username, password );
        token.setRememberMe(rememberMe);
        return token;
    }
}
