<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% response.addHeader("renderer","webkit");
    response.addHeader("X-UA-Compatible","IE=Edge,chrome=1");
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta charset="utf-8">
    <title>xx</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">


</head>
<body class="login">
<section id="login_bg" class="visible">
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-box">
                    <h2 class="bigintro">xxx </h2>
                    <div class="divide-40"></div>
                    <form role="form" id="loginForm"  action="${pageContext.request.contextPath}/login" method="post">
                        <div class="form-group">
                            <label for="username">用户名或邮箱</label>
                            <i class="fa fa-envelope"></i>
                            <input type="text" class="form-control" id="username" name="username" >
                        </div>
                        <div class="form-group">
                            <label for="password">密码</label>
                            <i class="fa fa-lock"></i>
                            <input type="password" class="form-control" id="password" name="password" >
                        </div>
                        <div>
                            <label class="checkbox"> <input type="checkbox" class="uniform" name="remember" value="1">自动登录</label>
                            <button type="submit" id="login_ok" class="btn btn-danger">登录</button>
                        </div>
                    </form>


                </div>
            </div>
        </div>
    </div>
</section>



<script href="https://cdn.bootcss.com/jquery/3.3.1/core.js"></script>

<script>
    $(function(){
        console.info(1);
    });

</script>

</body>
</html>