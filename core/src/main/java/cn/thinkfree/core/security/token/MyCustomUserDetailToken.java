package cn.thinkfree.core.security.token;

import cn.thinkfree.core.security.model.SecurityUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyCustomUserDetailToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;
    private String type;
    private String from;


    public MyCustomUserDetailToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public MyCustomUserDetailToken(Object user,Object passWord,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = user;
        this.credentials = passWord;
    }

    public MyCustomUserDetailToken(Object userName,Object passWord,String type) {
        super(null);
        this.principal = userName;
        this.credentials = passWord;
        this.type = type;
        setDetails(new TokenDetail(String.valueOf(principal),String.valueOf(credentials),type));
    }


    public Object getUserName() {
        return principal;
    }

    public void setUserName(Object userName) {
        this.principal = userName;
    }

    public Object getPassWord() {
        return credentials;
    }

    public void setPassWord(Object passWord) {
        this.credentials = passWord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }


    public static class TokenDetail{
        String type;
        String userName;
        String passWord;
        public TokenDetail(String userName,String passWord,String type){
            this.type = type;
            this.userName =userName;
            this.passWord =passWord;
        }

        public String getType() {
            return type;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassWord() {
            return passWord;
        }
    }
}
