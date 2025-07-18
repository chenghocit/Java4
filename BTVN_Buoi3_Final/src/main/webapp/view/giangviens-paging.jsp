<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Danh sách Giảng Viên - Phân Trang</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .info {
            text-align: center;
            margin-bottom: 20px;
            color: #666;
        }
        .no-data {
            text-align: center;
            color: #999;
            font-style: italic;
            padding: 20px;
        }
        
        /* Pagination Styles */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 20px;
            gap: 5px;
        }
        .pagination a {
            padding: 8px 16px;
            text-decoration: none;
            color: #007bff;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            transition: all 0.3s ease;
            font-weight: 500;
        }
        .pagination a:hover:not(.disabled) {
            background-color: #e9ecef;
            border-color: #adb5bd;
        }
        .pagination a.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .pagination a.disabled {
            pointer-events: none;
            color: #6c757d;
            background-color: #e9ecef;
            border-color: #dee2e6;
        }
        .back-link {
            margin-bottom: 20px;
        }
        .back-link a {
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="back-link">
            <a href="/giang-vien/hien-thi-tat-ca">← Quay lại danh sách đầy đủ</a>
        </div>
        
        <h1>Danh sách Giảng Viên - Phân Trang</h1>
        
        <div class="info">
            <c:if test="${totalPages > 0}">
                Trang ${currentPage} / ${totalPages} - Tổng cộng ${totalRecords} bản ghi (3 bản ghi/trang)
            </c:if>
        </div>

        <c:choose>
            <c:when test="${not empty a}">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Mã GV</th>
                            <th>Họ tên</th>
                            <th>Tuổi</th>
                            <th>Giới tính</th>
                            <th>Quê quán</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${a}" var="gv">
                            <tr>
                                <td>${gv.id}</td>
                                <td>${gv.ma}</td>
                                <td>${gv.ten}</td>
                                <td>${gv.tuoi}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${gv.gioiTinh == true}">Nam</c:when>
                                        <c:when test="${gv.gioiTinh == false}">Nữ</c:when>
                                        <c:otherwise>Chưa xác định</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${gv.queQuan}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- PHÂN TRANG -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <!-- Nút Previous -->
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}">Previous</a>
                            </c:when>
                            <c:otherwise>
                                <a href="#" class="disabled">Previous</a>
                            </c:otherwise>
                        </c:choose>
                        
                        <!-- Các số trang -->
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <a href="#" class="active">${i}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="?page=${i}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        
                        <!-- Nút Next -->
                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}">Next</a>
                            </c:when>
                            <c:otherwise>
                                <a href="#" class="disabled">Next</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    Không có dữ liệu giảng viên nào để hiển thị.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html> 