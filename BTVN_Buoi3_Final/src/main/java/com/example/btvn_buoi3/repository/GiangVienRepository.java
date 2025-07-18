package com.example.btvn_buoi3.repository;

import com.example.btvn_buoi3.entity.GiangVien;
import com.example.btvn_buoi3.util.HibernateUtl;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class GiangVienRepository {
    private Session session;
    private List<GiangVien> listGiangVien = new ArrayList<>();

    public GiangVienRepository() {
        session = HibernateUtl.getFACTORY().openSession();
    }

    public List<GiangVien> getAll() {
        return session.createQuery("FROM GiangVien ").list();
    }

    public GiangVien getOne(Integer id) {
        return session.find(GiangVien.class, id);
    }

    public GiangVien detailGiangVien(String mssv) {
        return session.createQuery("FROM GiangVien WHERE ma = :mssv", GiangVien.class)
                .setParameter("mssv", mssv)
                .uniqueResult();
    }

    public void delete(String mssv) {
        try {
            session.getTransaction().begin();
            // Tìm giảng viên theo mã
            GiangVien sv = (GiangVien) session.createQuery("FROM GiangVien WHERE ma = :mssv")
                    .setParameter("mssv", mssv)
                    .uniqueResult();
            if (sv != null) {
                session.delete(sv);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void update(GiangVien gv) {
        try {
            session.getTransaction().begin();
            //update: có thể dùng  saveOrUpdate, hoặc merge
            session.merge(gv);
            //commit (gửi dữ liệu đi )
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback(); //quay về trạng thái bắt đầu
        }
    }

    public List<GiangVien> search(String ten, Long tuoiMin, Long tuoiMax) {
        // Khởi tạo câu truy vấn HQL cơ bản
        StringBuilder hql = new StringBuilder("FROM GiangVien WHERE 1=1");

        //  thêm điều kiện tìm kiếm theo tên (LIKE)
        if (ten != null && !ten.trim().isEmpty()) {
            hql.append(" AND ten LIKE :ten");
        }
        // thêm điều kiện tuổi >= tuoiMin
        if (tuoiMin != null) {
            hql.append(" AND tuoi >= :tuoiMin");
        }
        //  thêm điều kiện tuổi <= tuoiMax
        if (tuoiMax != null) {
            hql.append(" AND tuoi <= :tuoiMax");
        }

        // Tạo đối tượng Query với câu HQL vừa xây dựng, chỉ định kiểu trả về là GiangVien
        Query<GiangVien> query = session.createQuery(hql.toString(), GiangVien.class);

        //  nhập tên, gán giá trị tham số :ten cho query (tìm gần đúng)
        if (ten != null && !ten.trim().isEmpty()) {
            query.setParameter("ten", "%" + ten.trim() + "%");
        }
        // nhập tuổi tối thiểu, gán giá trị tham số :tuoiMin cho query
        if (tuoiMin != null) {
            query.setParameter("tuoiMin", tuoiMin);
        }
        //  nhập tuổi tối đa, gán giá trị tham số :tuoiMax cho query
        if (tuoiMax != null) {
            query.setParameter("tuoiMax", tuoiMax);
        }

        // Thực thi truy vấn và trả về danh sách kết quả
        return query.list();
    }
    //phan trang
//    query.setMaxxResults()
//    query.setFirstResults()
    //page -1 * pagesize
    // tìm kiếm
//    public List<GiangVien> findByName (String name){
//        Session session = HibernateUtl.getFACTORY().openSession();
//        Query<GiangVien> query = session.createQuery(
//                "FROM GiangVien gv WHERE gv.ten Like :ten",GiangVien.class);
//        query.setParameter("ten", "%" + name + "%");
//        return query.getResultList();
//        )
//    }

    public boolean isMaExists(String ma) {
        try {
            GiangVien gv = session.createQuery("FROM GiangVien WHERE ma = :ma", GiangVien.class)
                    .setParameter("ma", ma)
                    .uniqueResult();
            return gv != null;
        } catch (Exception e) {
            // Nếu có nhiều bản ghi trùng mã, cũng coi như đã tồn tại
            return true;
        }
    }

    public void add(GiangVien gv) {
        try {
            session.getTransaction().begin();
            //add: có thể dùng save , saveOrUpdate, hoặc persist
            session.persist(gv);
            //commit (gửi dữ liệu đi )
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback(); //quay về trạng thái bắt đầu
        }
    }

    // Phương thức lấy danh sách theo trang
    public List<GiangVien> findByPage(int page, int pageSize) {
        try {
            Query<GiangVien> query = session.createQuery("FROM GiangVien ORDER BY id", GiangVien.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Phương thức đếm tổng số bản ghi
    public long count() {
        try {
            return (Long) session.createQuery("SELECT COUNT(*) FROM GiangVien").uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
