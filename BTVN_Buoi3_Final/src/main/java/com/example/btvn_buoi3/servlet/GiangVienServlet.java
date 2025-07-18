package com.example.btvn_buoi3.servlet;

import com.example.btvn_buoi3.entity.GiangVien;
import com.example.btvn_buoi3.service.GiangVienService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet(name = "GiangVienServlet", value = {
        "/giang-vien/hien-thi-tat-ca",
        "/giang-vien/phan-trang",
        "/giang-vien/detail",
        "/giang-vien/remove",
        "/giang-vien/view-update",
        "/giang-vien/update",
        "/giang-vien/tim-kiem",
        "/giang-vien/add",
})
public class GiangVienServlet extends HttpServlet {
    private GiangVienService service = new GiangVienService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("hien-thi-tat-ca")) {
            this.hienThiGiangVien(request, response);
        } else if (uri.contains("phan-trang")) {
            this.phanTrangGiangVien(request, response);
        } else if (uri.contains("detail")) {
            this.detailGiangVien(request, response);
        } else if (uri.contains("remove")) {
            this.removeGiangVien(request, response);
        } else if (uri.contains("view-update")) {
            this.viewUpdateGiangVien(request, response);
        } else if (uri.contains("tim-kiem")) {
            this.searchGiangVien(request, response);
        }
    }

    private void hienThiGiangVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int pageSize = 10; // Mặc định 10 bản ghi mỗi trang

        // Lấy tham số page từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Lấy tham số pageSize từ request
        String pageSizeParam = request.getParameter("pageSize");
        if (pageSizeParam != null) {
            try {
                int requestedPageSize = Integer.parseInt(pageSizeParam);
                if (requestedPageSize == 10 || requestedPageSize == 25 || requestedPageSize == 50 || requestedPageSize == 100) {
                    pageSize = requestedPageSize;
                }
            } catch (NumberFormatException e) {
                pageSize = 10;
            }
        }

        try {
            // Tính tổng số trang
            long totalRecords = service.countGiangVien();
            int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);

            // Đảm bảo page nằm trong khoảng hợp lệ
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;

            // Lấy danh sách giảng viên cho trang hiện tại
            List<GiangVien> listGiangVien = service.getGiangVienByPage(page, pageSize);

            // Đưa dữ liệu vào request
            request.setAttribute("a", listGiangVien);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải dữ liệu: " + e.getMessage());
        }

        request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
    }

    private void phanTrangGiangVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int pageSize = 3; // Mỗi trang 3 bản ghi

        // Lấy tham số page từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        try {
            // Tính tổng số trang
            long totalRecords = service.countGiangVien();
            int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);

            // Đảm bảo page nằm trong khoảng hợp lệ
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;

            // Lấy danh sách giảng viên cho trang hiện tại
            List<GiangVien> listGiangVien = service.getGiangVienByPage(page, pageSize);

            // Đưa dữ liệu vào request
            request.setAttribute("a", listGiangVien);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải dữ liệu: " + e.getMessage());
        }

        // Forward đến trang JSP với phân trang
        request.getRequestDispatcher("/view/giangviens-paging.jsp").forward(request, response);
    }

    private void detailGiangVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String maGV = request.getParameter("b");
        try {
            GiangVien gv = service.getGiangVienByMa(maGV);
            
            // Lấy trang hiện tại từ referer hoặc parameter
            int page = 1;
            int pageSize = 10;
            
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            
            String pageSizeParam = request.getParameter("pageSize");
            if (pageSizeParam != null) {
                try {
                    int requestedPageSize = Integer.parseInt(pageSizeParam);
                    if (requestedPageSize == 10 || requestedPageSize == 25 || requestedPageSize == 50 || requestedPageSize == 100) {
                        pageSize = requestedPageSize;
                    }
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }
            
            // Tính tổng số trang
            long totalRecords = service.countGiangVien();
            int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);
            
            // Đảm bảo page nằm trong khoảng hợp lệ
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            // Lấy danh sách giảng viên cho trang hiện tại
            List<GiangVien> listGiangVien = service.getGiangVienByPage(page, pageSize);
            
            // Đưa dữ liệu vào request
            request.setAttribute("gv1", gv);
            request.setAttribute("a", listGiangVien);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải chi tiết: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
    }

    private void removeGiangVien(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maGV = request.getParameter("id");
        try {
            service.deleteGiangVien(maGV);
        } catch (Exception e) {
            // Log error hoặc xử lý lỗi
            e.printStackTrace();
        }
        
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        
        StringBuilder redirectUrl = new StringBuilder("/giang-vien/hien-thi-tat-ca");
        if (pageParam != null || pageSizeParam != null) {
            redirectUrl.append("?");
            if (pageParam != null) {
                redirectUrl.append("page=").append(pageParam);
                if (pageSizeParam != null) {
                    redirectUrl.append("&pageSize=").append(pageSizeParam);
                }
            } else if (pageSizeParam != null) {
                redirectUrl.append("pageSize=").append(pageSizeParam);
            }
        }
        
        response.sendRedirect(redirectUrl.toString());
    }

    private void viewUpdateGiangVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String maGv = request.getParameter("a");
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        
        try {
            GiangVien gv = service.getGiangVienByMa(maGv);
            request.setAttribute("gv1", gv);
            request.setAttribute("currentPage", pageParam != null ? pageParam : "1");
            request.setAttribute("currentPageSize", pageSizeParam != null ? pageSizeParam : "10");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải thông tin cập nhật: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/view/update-giang-vien.jsp").forward(request, response);
    }

    private void searchGiangVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ten = request.getParameter("ten");
        String tuoiMinStr = request.getParameter("tuoiMin");
        String tuoiMaxStr = request.getParameter("tuoiMax");

        Long tuoiMin = null, tuoiMax = null;
        try {
            if (tuoiMinStr != null && !tuoiMinStr.isEmpty()) tuoiMin = Long.parseLong(tuoiMinStr);
            if (tuoiMaxStr != null && !tuoiMaxStr.isEmpty()) tuoiMax = Long.parseLong(tuoiMaxStr);
        } catch (NumberFormatException e) {
            // Có thể xử lý báo lỗi nhập liệu ở đây nếu cần
        }

        List<GiangVien> listGiangVien;
        try {
            listGiangVien = service.searchGiangVien(ten, tuoiMin, tuoiMax);
        } catch (Exception e) {
            // Xử lý lỗi tìm kiếm
            listGiangVien = service.getAllGiangVien();
            request.setAttribute("error", "Lỗi tìm kiếm: " + e.getMessage());
        }
        request.setAttribute("a", listGiangVien);
        request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);

    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("add")) {
            this.addGiangVien(request, response);
        } else if (uri.contains("update")) {
            this.updateGiangVien(request, response);
        }
    }

    private void updateGiangVien(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        GiangVien gv = new GiangVien();
        BeanUtils.populate(gv, request.getParameterMap());
        try {
            service.updateGiangVien(gv);
        } catch (Exception e) {
            // Log error hoặc xử lý lỗi
            e.printStackTrace();
        }
        
        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");
        
        StringBuilder redirectUrl = new StringBuilder("/giang-vien/hien-thi-tat-ca");
        if (pageParam != null || pageSizeParam != null) {
            redirectUrl.append("?");
            if (pageParam != null) {
                redirectUrl.append("page=").append(pageParam);
                if (pageSizeParam != null) {
                    redirectUrl.append("&pageSize=").append(pageSizeParam);
                }
            } else if (pageSizeParam != null) {
                redirectUrl.append("pageSize=").append(pageSizeParam);
            }
        }
        
        response.sendRedirect(redirectUrl.toString());
    }
    private void addGiangVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ma = request.getParameter("ma");
        String ten = request.getParameter("ten");
        String tuoiStr = request.getParameter("tuoi");
        String queQuan = request.getParameter("queQuan");

        // Check trống
        if (ma == null || ma.trim().isEmpty() ||
                ten == null || ten.trim().isEmpty() ||
                tuoiStr == null || tuoiStr.trim().isEmpty() ||
                queQuan == null || queQuan.trim().isEmpty()) {

            try {
                List<GiangVien> listGiangVien = service.getAllGiangVien();
                request.setAttribute("a", listGiangVien);
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi khi tải dữ liệu");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            }
            return;
        }

        // Kiểm tra mã đã tồn tại chưa
        if (service.isMaExists(ma.trim())) {
            try {
                List<GiangVien> listGiangVien = service.getAllGiangVien();
                request.setAttribute("a", listGiangVien);
                request.setAttribute("error", "Mã giảng viên '" + ma + "' đã tồn tại! Vui lòng nhập mã khác.");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi khi kiểm tra dữ liệu");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            }
            return;
        }

        // Nếu không trống và mã chưa tồn tại, thực hiện thêm mới
        try {
            Long tuoi = Long.parseLong(tuoiStr);
            GiangVien gv = new GiangVien();
            gv.setMa(ma);
            gv.setTen(ten);
            gv.setTuoi(tuoi);
            gv.setQueQuan(queQuan);
            service.addGiangVien(gv);

            List<GiangVien> listGiangVien = service.getAllGiangVien();
            request.setAttribute("a", listGiangVien);
            request.setAttribute("message", "Thêm giảng viên thành công!");
            request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            try {
                List<GiangVien> listGiangVien = service.getAllGiangVien();
                request.setAttribute("a", listGiangVien);
                request.setAttribute("error", "Tuổi phải là số!");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute("error", "Lỗi khi tải dữ liệu");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            }
        } catch (Exception e) {
            try {
                List<GiangVien> listGiangVien = service.getAllGiangVien();
                request.setAttribute("a", listGiangVien);
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute("error", "Lỗi khi xử lý dữ liệu");
                request.getRequestDispatcher("/view/giangviens.jsp").forward(request, response);
            }
        }
    }

}