<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>GiangVienHome</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #000; padding: 4px 8px; }
        th { background: #f5f5f5; }
        form { margin-bottom: 10px; }
        .action-btn { margin-right: 2px; }
        
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
        .info {
            text-align: center;
            margin: 10px 0;
            color: #666;
            font-size: 14px;
        }
    </style>
</head>
<body>

<form action="/giang-vien/tim-kiem" method="get">
    <div style="border: 1px solid #ccc; padding: 10px; width: 350px; margin-bottom: 10px;">
        Tên: <input type="text" name="ten" value="${param.ten}">
        <br>
        Tuổi:
        <br>
        Min: <input type="text" name="tuoiMin" value="${param.tuoiMin}" style="margin-bottom: 10px;">
        <br>
        Max: <input type="text" name="tuoiMax" value="${param.tuoiMax}">
        <br>
        <button type="submit">Search</button>
    </div>
</form>

<form action="/giang-vien/add" method="post" style="margin-bottom: 10px;">
    Mã GV: <input type="text" name="ma" value="${gv1.ma}" style="margin-bottom: 10px;">
    Họ tên: <input type="text" name="ten" value="${gv1.ten}" style="margin-bottom: 10px;">
    <br>
    Tuổi: <input type="text" name="tuoi" value="${gv1.tuoi}" style="margin-bottom: 20px;">
    Quê quán: <input type="text" name="queQuan" value="${gv1.queQuan}">
    <br>
    <button type="submit">Add</button>
</form>



<table>
    <tr>
        <th>Mã GV</th>
        <th>Họ tên</th>
        <th>Tuổi</th>
        <th>Quê quán</th>
        <th colspan="3">Action</th>
    </tr>
    <c:forEach items="${a}" var="gv">
        <tr>
            <td>${gv.ma}</td>
            <td>${gv.ten}</td>
            <td>${gv.tuoi}</td>
            <td>${gv.queQuan}</td>
            <td>
                <form action="/giang-vien/detail" method="get" style="display:inline;">
                    <input type="hidden" name="b" value="${gv.ma}">
                    <input type="hidden" name="page" value="${currentPage}">
                    <input type="hidden" name="pageSize" value="${pageSize}">
                    <button type="submit" class="action-btn">Detail</button>
                </form>
            </td>
            <td>
                <form action="/giang-vien/view-update" method="get" style="display:inline;">
                    <input type="hidden" name="a" value="${gv.ma}">
                    <input type="hidden" name="page" value="${currentPage}">
                    <input type="hidden" name="pageSize" value="${pageSize}">
                    <button type="submit" class="action-btn">Edit</button>
                </form>
            </td>
            <td>
                <form action="/giang-vien/remove" method="get" style="display:inline;">
                    <input type="hidden" name="id" value="${gv.ma}">
                    <input type="hidden" name="page" value="${currentPage}">
                    <input type="hidden" name="pageSize" value="${pageSize}">
                    <button type="submit" class="action-btn">Remove</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${not empty message}">
        <div style="color: green;">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div style="color: red;">${error}</div>
    </c:if>

</table>

<!-- Thông tin phân trang và chọn số bản ghi -->
<div style="display: flex; justify-content: space-between; align-items: center; margin: 15px 0;">
    <div class="info">
        <c:if test="${totalPages > 0}">
            Trang ${currentPage} / ${totalPages} - Tổng cộng ${totalRecords} bản ghi
        </c:if>
    </div>
    
    <div style="display: flex; align-items: center; gap: 10px;">
        <span style="color: #666; font-size: 14px;">Xem:</span>
        <select id="pageSizeSelect" style="padding: 5px 10px; border: 1px solid #dee2e6; border-radius: 4px; font-size: 14px;">
            <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
            <option value="25" ${pageSize == 25 ? 'selected' : ''}>25</option>
            <option value="50" ${pageSize == 50 ? 'selected' : ''}>50</option>
            <option value="100" ${pageSize == 100 ? 'selected' : ''}>100</option>
        </select>
        <span style="color: #666; font-size: 14px;">mục</span>
    </div>
</div>

<!-- PHÂN TRANG -->
<c:if test="${totalPages > 1}">
    <div class="pagination">
        <!-- Nút Previous -->
        <c:choose>
            <c:when test="${currentPage > 1}">
                <a href="/giang-vien/hien-thi-tat-ca?page=${currentPage - 1}&pageSize=${pageSize}">Previous</a>
            </c:when>
            <c:otherwise>
                <a href="#" class="disabled">Previous</a>
            </c:otherwise>
        </c:choose>
        
        <!-- Các số trang với logic rút gọn -->
        <c:choose>
            <c:when test="${totalPages <= 7}">
                <!-- Nếu tổng số trang <= 7, hiển thị tất cả -->
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <a href="#" class="active">${i}</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/giang-vien/hien-thi-tat-ca?page=${i}&pageSize=${pageSize}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <!-- Logic phân trang rút gọn -->
                <c:choose>
                    <c:when test="${currentPage <= 4}">
                        <!-- Trang hiện tại ở đầu: 1 2 3 4 5 ... last -->
                        <c:forEach begin="1" end="5" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <a href="#" class="active">${i}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/giang-vien/hien-thi-tat-ca?page=${i}&pageSize=${pageSize}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span style="padding: 8px 16px; color: #6c757d;">...</span>
                        <a href="/giang-vien/hien-thi-tat-ca?page=${totalPages}&pageSize=${pageSize}">${totalPages}</a>
                    </c:when>
                    <c:when test="${currentPage >= totalPages - 3}">
                        <!-- Trang hiện tại ở cuối: 1 ... last-4 last-3 last-2 last-1 last -->
                        <a href="/giang-vien/hien-thi-tat-ca?page=1&pageSize=${pageSize}">1</a>
                        <span style="padding: 8px 16px; color: #6c757d;">...</span>
                        <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <a href="#" class="active">${i}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/giang-vien/hien-thi-tat-ca?page=${i}&pageSize=${pageSize}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Trang hiện tại ở giữa: 1 ... current-1 current current+1 ... last -->
                        <a href="/giang-vien/hien-thi-tat-ca?page=1&pageSize=${pageSize}">1</a>
                        <span style="padding: 8px 16px; color: #6c757d;">...</span>
                        <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <a href="#" class="active">${i}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/giang-vien/hien-thi-tat-ca?page=${i}&pageSize=${pageSize}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span style="padding: 8px 16px; color: #6c757d;">...</span>
                        <a href="/giang-vien/hien-thi-tat-ca?page=${totalPages}&pageSize=${pageSize}">${totalPages}</a>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        
        <!-- Nút Next -->
        <c:choose>
            <c:when test="${currentPage < totalPages}">
                <a href="/giang-vien/hien-thi-tat-ca?page=${currentPage + 1}&pageSize=${pageSize}">Next</a>
            </c:when>
            <c:otherwise>
                <a href="#" class="disabled">Next</a>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<script>
document.getElementById('pageSizeSelect').addEventListener('change', function() {
    var selectedPageSize = this.value;
    var currentUrl = window.location.href;
    
    // Tạo URL mới với pageSize và reset về trang 1
    var newUrl = '/giang-vien/hien-thi-tat-ca?page=1&pageSize=' + selectedPageSize;
    
    // Chuyển hướng đến URL mới
    window.location.href = newUrl;
});
</script>

</body>
</html>
