package cn.thinkfree.core.security.token;

import cn.thinkfree.core.security.model.SecurityUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyCustomUserDetailToken extends UsernamePasswordAuthenticationToken {

    private String userName;
    private String passWord;
    private String type;
    private String from;


    public MyCustomUserDetailToken(Collection<? extends GrantedAuthority> authorities) {
       super(null,null,authorities);

    }

    public MyCustomUserDetailToken(SecurityUser user,String type){
        super(user,null);
        System.out.println(type);
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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
}
