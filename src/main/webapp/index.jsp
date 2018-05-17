<%@ page language="java"  contentType="text/html; charset=UTF-8" %>

<html>
<body>

<form action="/mmall/manage/product/upload.json" method = "post" enctype="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="upload">
</form>

富文本

<form action="/mmall/manage/product/richtextImgUpload.json" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="upload">
</form>

</html>
