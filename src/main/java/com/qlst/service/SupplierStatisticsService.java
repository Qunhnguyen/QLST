package com.qlst.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.qlst.dao.SupplierStatisticsDAO;
import com.qlst.model.SupplierStatistics;

public class SupplierStatisticsService {

    private final SupplierStatisticsDAO supplierStatisticsDAO = new SupplierStatisticsDAO();

    public List<SupplierStatistics> getStatistics(LocalDate from, LocalDate to) throws SQLException {
        if (from == null || to == null) {
            return Collections.emptyList();
        }
        return supplierStatisticsDAO.findByRange(from, to);
    }

    public SupplierStatistics getStatisticsForSupplier(int supplierId, LocalDate from, LocalDate to) throws SQLException {
        if (from == null || to == null) {
            return null;
        }
        return supplierStatisticsDAO.findOne(supplierId, from, to);
    }
}
