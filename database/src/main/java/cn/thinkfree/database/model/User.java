package cn.thinkfree.database.model;


import cn.thinkfree.core.model.BaseModel;

public class User  extends BaseModel {

    /**
     * id主键
     */
    private Integer  id;
    /**
     * 账号名
     */
    private String  username;
    /**
     * 密码
     */
    private String  password;
    /**
     * 用户名
     */
    private String  name;
    /**
     * 用户类型
     */
    private Byte  usertype;
    /**
     * 头像
     */
    private String  face;
    /**
     * 部门
     */
    private Integer  dept;
    /**
     * 是否删除
     */
    private Byte  dr;



    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Byte getUsertype(){
        return usertype;
    }

    public void setUsertype(Byte usertype){
        this.usertype = usertype;
    }

    public String getFace(){
        return face;
    }

    public void setFace(String face){
        this.face = face;
    }

    public Integer getDept(){
        return dept;
    }

    public void setDept(Integer dept){
        this.dept = dept;
    }

    public Byte getDr(){
        return dr;
    }

    public void setDr(Byte dr){
        this.dr = dr;
    }



}
