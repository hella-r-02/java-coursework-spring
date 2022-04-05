package main.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private Long id;
    private String userName;
    private String password;
    private List<String> roles = new ArrayList<>();
    private String token;
    private Long createDateToken;
    private Integer timeout;

    public User() {
    }

    public User(String userName, String password, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCreateDateToken() {
        return createDateToken;
    }

    public void setCreateDateToken(Long createDateToken) {
        this.createDateToken = createDateToken;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public boolean checkValidToken() {
        return ((token != null) && (new Date().getTime() - createDateToken > timeout));
    }
}
