package service;

import config.DbConfig;
import entity.Customer;
import entity.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {

    public void addInvoice(Invoice invoice) throws SQLException
    {
        Connection con = DbConfig.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT into invoices (customer_id, vehicle_id, service_id) values (?, ?, ?)");
        ps.setInt(1, invoice.getCustomerId());
        ps.setInt(2, invoice.getVehicleId());
        ps.setInt(3, invoice.getServiceId());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public List<Invoice> getAllInvoices() throws SQLException
    {
        List<Invoice> list = new ArrayList<>();
        Connection con = DbConfig.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select * from invoices");
        while(rs.next())
        {
            list.add(new Invoice(rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("vehicle_id"), rs.getInt("service_id")));
        }

        return list;
    }
}
