<%--
  Created by IntelliJ IDEA.
  User: 53049
  Date: 2018/8/22
  Time: 16:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
    </script>
</head>
<body>

userName:${userModel.username}
<form id="upload" action="/project/test">
    <input type="file" name="thumbnail">

    <input type="button" name="xxx" onclick="uf()">
</form>

<script>
    function uf(){
        console.info("UPLOAD");
        let form=document.getElementById("upload");
        let formData = new FormData(form);
        formData.append("tt","ap");
        formData.append("staffs[0].projectNo","1");
        formData.append("staffs[1].projectNo","1");

        console.info(form);
        $.ajax({
            url:'http://localhost:8080/project/upload',
            type:'POST',
            data:formData,
            async: false,
            cache: false,
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success:function(data){
                console.log(data);
            },
            error:function(){
                alert("上传失败！");
            }
        })

    }
    $(function(){
        console.info("BUG");

    })

</script>
</body>
</html>
