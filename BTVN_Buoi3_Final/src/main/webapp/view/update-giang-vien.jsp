<%--
  Created by IntelliJ IDEA.
  User: Nguyen Thu Trang
  Date: 7/14/2025
  Time: 3:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/giang-vien/update" method="post">
    <input type="hidden" name="id" value="${gv1.id}">
    <input type="hidden" name="page" value="${currentPage}">

    Mã GV: <input type="text" name="ma" value="${gv1.ma} " readonly>
    <br>
    Tên: <input type="text" name="ten" value="${gv1.ten}">
    <br>
    Tuổi: <input type="text" name="tuoi" value="${gv1.tuoi}">
    <br>
    Quê Quán: <input type="text" name="queQuan" value="${gv1.queQuan}">
    <br>
    Giới tính:
    <input type="radio" name="gioiTinh" ${gv1.gioiTinh == true ? "checked":""} value="true">Nam
    <input type="radio" name="gioiTinh" ${gv1.gioiTinh == false ? "checked":""} value="false">Nữ
    <br>
    <button type="submit">Edit</button>

</form>
</body>
</html>
