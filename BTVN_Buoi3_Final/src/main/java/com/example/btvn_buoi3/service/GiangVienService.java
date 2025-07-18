package com.example.btvn_buoi3.service;

import com.example.btvn_buoi3.entity.GiangVien;
import com.example.btvn_buoi3.repository.GiangVienRepository;

import java.util.List;

public class GiangVienService {
    private GiangVienRepository repository;

    public GiangVienService() {
        this.repository = new GiangVienRepository();
    }

    /**
     * Lấy tất cả giảng viên
     */
    public List<GiangVien> getAllGiangVien() {
        return repository.getAll();
    }

    /**
     * Lấy giảng viên theo ID
     */
    public GiangVien getGiangVienById(Integer id) {
        return repository.getOne(id);
    }

    /**
     * Lấy chi tiết giảng viên theo mã
     */
    public GiangVien getGiangVienByMa(String ma) {
        if (ma == null || ma.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giảng viên không được để trống");
        }
        return repository.detailGiangVien(ma.trim());
    }

    /**
     * Thêm mới giảng viên với validation
     */
    public void addGiangVien(GiangVien giangVien) throws Exception {
        // Validation
        validateGiangVien(giangVien);
        
        // Kiểm tra mã đã tồn tại
        if (repository.isMaExists(giangVien.getMa().trim())) {
            throw new Exception("Mã giảng viên '" + giangVien.getMa() + "' đã tồn tại");
        }

        // Chuẩn hóa dữ liệu
        giangVien.setMa(giangVien.getMa().trim());
        giangVien.setTen(giangVien.getTen().trim());
        giangVien.setQueQuan(giangVien.getQueQuan().trim());

        repository.add(giangVien);
    }

    /**
     * Cập nhật thông tin giảng viên
     */
    public void updateGiangVien(GiangVien giangVien) throws Exception {
        if (giangVien.getId() == null) {
            throw new IllegalArgumentException("ID giảng viên không được để trống khi cập nhật");
        }

        // Validation
        validateGiangVien(giangVien);

        // Chuẩn hóa dữ liệu
        giangVien.setMa(giangVien.getMa().trim());
        giangVien.setTen(giangVien.getTen().trim());
        giangVien.setQueQuan(giangVien.getQueQuan().trim());

        repository.update(giangVien);
    }

    /**
     * Xóa giảng viên theo mã
     */
    public void deleteGiangVien(String ma) throws Exception {
        if (ma == null || ma.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giảng viên không được để trống");
        }

        // Kiểm tra giảng viên có tồn tại không
        GiangVien existingGV = repository.detailGiangVien(ma.trim());
        if (existingGV == null) {
            throw new Exception("Không tìm thấy giảng viên với mã: " + ma);
        }

        repository.delete(ma.trim());
    }

    /**
     * Tìm kiếm giảng viên theo các tiêu chí
     */
    public List<GiangVien> searchGiangVien(String ten, Long tuoiMin, Long tuoiMax) throws Exception {
        // Validation tuổi
        if (tuoiMin != null && tuoiMin < 0) {
            throw new IllegalArgumentException("Tuổi tối thiểu không được âm");
        }
        if (tuoiMax != null && tuoiMax < 0) {
            throw new IllegalArgumentException("Tuổi tối đa không được âm");
        }
        if (tuoiMin != null && tuoiMax != null && tuoiMin > tuoiMax) {
            throw new IllegalArgumentException("Tuổi tối thiểu không được lớn hơn tuổi tối đa");
        }

        return repository.search(ten, tuoiMin, tuoiMax);
    }

    /**
     * Lấy danh sách giảng viên theo trang
     */
    public List<GiangVien> getGiangVienByPage(int page, int pageSize) throws Exception {
        if (page < 1) {
            throw new IllegalArgumentException("Số trang phải lớn hơn 0");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException("Kích thước trang phải từ 1 đến 100");
        }

        return repository.findByPage(page, pageSize);
    }

    /**
     * Đếm tổng số giảng viên
     */
    public long countGiangVien() {
        return repository.count();
    }

    /**
     * Tính tổng số trang
     */
    public int getTotalPages(int pageSize) {
        if (pageSize < 1) {
            throw new IllegalArgumentException("Kích thước trang phải lớn hơn 0");
        }
        long totalRecords = repository.count();
        return (int) Math.ceil(totalRecords / (double) pageSize);
    }

    /**
     * Kiểm tra mã giảng viên đã tồn tại
     */
    public boolean isMaExists(String ma) {
        if (ma == null || ma.trim().isEmpty()) {
            return false;
        }
        return repository.isMaExists(ma.trim());
    }

    /**
     * Validation dữ liệu giảng viên
     */
    private void validateGiangVien(GiangVien giangVien) throws Exception {
        if (giangVien == null) {
            throw new IllegalArgumentException("Thông tin giảng viên không được để trống");
        }

        // Kiểm tra mã
        if (giangVien.getMa() == null || giangVien.getMa().trim().isEmpty()) {
            throw new Exception("Mã giảng viên không được để trống");
        }
        if (giangVien.getMa().trim().length() > 20) {
            throw new Exception("Mã giảng viên không được quá 20 ký tự");
        }

        // Kiểm tra tên
        if (giangVien.getTen() == null || giangVien.getTen().trim().isEmpty()) {
            throw new Exception("Tên giảng viên không được để trống");
        }
        if (giangVien.getTen().trim().length() > 100) {
            throw new Exception("Tên giảng viên không được quá 100 ký tự");
        }

        // Kiểm tra tuổi
        if (giangVien.getTuoi() == null) {
            throw new Exception("Tuổi không được để trống");
        }
        if (giangVien.getTuoi() < 18 || giangVien.getTuoi() > 100) {
            throw new Exception("Tuổi phải từ 18 đến 100");
        }

        // Kiểm tra quê quán
        if (giangVien.getQueQuan() == null || giangVien.getQueQuan().trim().isEmpty()) {
            throw new Exception("Quê quán không được để trống");
        }
        if (giangVien.getQueQuan().trim().length() > 100) {
            throw new Exception("Quê quán không được quá 100 ký tự");
        }
    }

    /**
     * Lấy thống kê cơ bản
     */
    public GiangVienStatistics getStatistics() {
        List<GiangVien> allGiangVien = repository.getAll();
        
        long totalCount = allGiangVien.size();
        long maleCount = allGiangVien.stream()
                .filter(gv -> gv.getGioiTinh() != null && gv.getGioiTinh())
                .count();
        long femaleCount = allGiangVien.stream()
                .filter(gv -> gv.getGioiTinh() != null && !gv.getGioiTinh())
                .count();
        
        double averageAge = allGiangVien.stream()
                .filter(gv -> gv.getTuoi() != null)
                .mapToLong(GiangVien::getTuoi)
                .average()
                .orElse(0.0);

        return new GiangVienStatistics(totalCount, maleCount, femaleCount, averageAge);
    }

    /**
     * Class để chứa thống kê
     */
    public static class GiangVienStatistics {
        private long totalCount;
        private long maleCount;
        private long femaleCount;
        private double averageAge;

        public GiangVienStatistics(long totalCount, long maleCount, long femaleCount, double averageAge) {
            this.totalCount = totalCount;
            this.maleCount = maleCount;
            this.femaleCount = femaleCount;
            this.averageAge = averageAge;
        }

        // Getters
        public long getTotalCount() { return totalCount; }
        public long getMaleCount() { return maleCount; }
        public long getFemaleCount() { return femaleCount; }
        public double getAverageAge() { return averageAge; }
    }
}