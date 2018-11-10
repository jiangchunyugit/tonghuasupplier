<%--
  Created by IntelliJ IDEA.
  User: 53049
  Date: 2018/8/22
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script
            src="https://code.jquery.com/jquery-3.3.1.js"
            integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60="
            crossorigin="anonymous"></script>
</head>
<body>
<script>
    $(function () {
        console.info("12")
        $.ajax({
            url: "http://localhost:8080/login",
            type:"post",
            dataType:"json",
            data:{
                'username':'pcTest',
                'password':'456789'
            },

        }).done(function (da) {
            console.info(da);
            var form = new FormData();
            form.append("resource",1);
            form.append("resource",2);
            $.ajax({
                url: "http://localhost:8080/account/permission/5/resource",
                headers:{
                    'token':da.token
                },
                type:"post",
                dataType:"json",
                data: form,
                processData:false,
                contentType:false,

            }).done(function (da) {
                console.info(da);
            }).fail(function (e) {
                console.info(e);
            })
        }).fail(function (e) {
            console.info(e);
        })

    })
</script>
</body>
</html>
