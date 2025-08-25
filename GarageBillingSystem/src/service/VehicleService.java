package service;

import config.DbConfig;
import entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleService {

    public void addCustomer(Customer customer) throws SQLException
    {
        Connection con = DbConfig.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT into customers (name, phone) values (?, ?)");
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getPhone());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public List<Customer> getAllCustomers() throws SQLException
    {
        List<Customer> list = new ArrayList<>();
        Connection con = DbConfig.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select * from customers");
        while(rs.next())
        {
            list.add(new Customer(rs.getInt("id"),
                            rs.getString("name"), rs.getString("phone")));
        }

        return list;
    }


    public Customer getCustomersBasedOnPhoneNum(String num) throws SQLException
    {
        Customer customer = new Customer();
        Connection con = DbConfig.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select * from customers where phone = "+num);
        while(rs.next())
        {
            customer = new Customer(rs.getInt("id"),
                    rs.getString("name"), rs.getString("phone"));
        }

        return customer;
    }
}
