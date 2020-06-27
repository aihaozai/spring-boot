<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <td>phone</td>
    </tr>
    <#if user ??&&(user?size>O)>
    <#list user as item>
    <td>${item.phone}</td>
    </#list>
    </#if>
</table>
</body>
</html>