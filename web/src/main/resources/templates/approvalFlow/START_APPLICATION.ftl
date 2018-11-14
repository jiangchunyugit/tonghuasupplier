<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <title></title>
    <style type="text/css">
        .center {
            width: 80%;
            margin-left: auto;
            margin-right: auto;
        }
        td {
            padding:10px;
        }
        .title {
            text-align: center;
            font-size:30px;
            margin-top: 60px;
        }
        .label {
            text-align: center;
        }
        .td_two {
            width: 80px;
        }
        .page-break {
            page-break-after:always;
        }
        table,table tr th, table tr td {
            border:1px solid #000000;
        }
        table tr td:first-of-type {
            width: 100px;
        }
        .content {
            text-align: left;
        }
        table {
            text-align: center;
            border-collapse: collapse;
        }
        .remark {
            height: 400px;
        }
    </style>
</head>
<body>
<div class="center">
<h1 class="title">${configName}</h1>
<table >
    <tr >
        <td class="label">项目编码</td>
        <td colspan="3" class="content">${projectNo}</td>
    </tr>
    <tr >
        <td class="label">业主</td>
        <td class="content td_two">${customerName}</td>
        <td class="label td_two">手机号码</td>
        <td class="content">${phoneNo}</td>
    </tr>
    <tr >
        <td class="label">项目地址</td>
        <td colspan="3" class="content">${address}</td>
    </tr>
    <tr >
        <td class="label">发起时间</td>
        <td colspan="3" class="content">${createTime}</td>
    </tr>
    <tr style="position: relative;">
        <td class="label">结果说明</td>
        <td colspan="3"  class="content remark"><div >${remark}</div></td>
    </tr>
    <tr >
        <td class="label">发起人</td>
        <td class="content">${createUsername}(${createRoleName})</td>
        <td class="label">审批人</td>
        <td class="content">
            <#list approvalUsers as approvalUser>
                ${approvalUser.username}(${approvalUser.roleName})
                <#sep>,
            </#list>
        </td>
    </tr>
</table>
</div>
<div class="page-break"/>
<div class="page">
    <img src="http://47.94.212.199:81/1541742836169.JPEG" alt="picture" height="100"/>
</div>
</body>
</html>