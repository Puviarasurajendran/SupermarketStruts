package com.crm.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.crm.model.Customer;
import com.crm.model.Product;
import com.crm.model.SalesOrder;
import com.crm.model.Vendor;

public class SuperMarketSalesDAO implements SuperMarketSalesInterface{

    @Override
	public List<Customer> getAllCustomers_v1(){
        List<Customer> customers = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Customers");

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return customers;
    }

    @Override
	public void addNewCustomer(Customer customer) {
        Connection con = null;
        PreparedStatement stmt = null;
         if(validationCheck(customer)) {
        try {
            con = DBConnection.getConnection();
            stmt = con.prepareStatement("INSERT INTO Customers (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getAddress());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
      }
    }

    public boolean validationCheck(Customer c) {
    	if(c.getFirstName().length()>0&&c.getFirstName()!=null&&
    			c.getLastName().length()>0&&c.getLastName()!=null &&
    			c.getEmail().length()>0&&c.getEmail()!=null &&
    			c.getPhone().length()>0&&c.getPhone()!=null) {
    		return true;
    	}
    	return false;
    }

        @Override
		public List<Product> getAllAvailableProducts() {
            List<Product> products = new ArrayList<>();
            Connection con = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
            	  con = DBConnection.getConnection();
                  stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT * FROM Products where stock > 0");

                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setProductName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setVendorId(rs.getInt("vendor_id"));
                    products.add(product);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
						rs.close();
					}
                    if (stmt != null) {
						stmt.close();
					}
                    if (con != null) {
						con.close();
					}
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return products;
        }

        @Override
		public boolean saveSalesOrder_v1(SalesOrder order) {

            String getProductIdSql = "SELECT product_id FROM Products WHERE name = ?";
            String salesOrderSql = "INSERT INTO SalesOrders (customer_id, product_id, quantity, order_date, total_amount) VALUES (?, ?, ?, NOW(), ?)";
            String updateProductStockSql = "UPDATE Products SET stock = stock - ? WHERE product_id = ?";

            Connection conn = null;
            PreparedStatement getProductIdStmt = null;
            PreparedStatement salesOrderStmt = null;
            PreparedStatement updateProductStockStmt = null;

            try {
                conn = DBConnection.getConnection();
                conn.setAutoCommit(false);

                getProductIdStmt = conn.prepareStatement(getProductIdSql);
                getProductIdStmt.setString(1, order.getProduct());
                ResultSet rs = getProductIdStmt.executeQuery();

                if (rs.next()) {
                    int productId = rs.getInt("product_id");
                    salesOrderStmt = conn.prepareStatement(salesOrderSql);
                    salesOrderStmt.setInt(1, order.getCustomerId());
                    salesOrderStmt.setInt(2, productId);
                    salesOrderStmt.setInt(3, order.getQuantity());
                    salesOrderStmt.setDouble(4, order.getAmount());
                    int rowsInserted = salesOrderStmt.executeUpdate();

                    if (rowsInserted > 0) {

                        updateProductStockStmt = conn.prepareStatement(updateProductStockSql);
                        updateProductStockStmt.setInt(1, order.getQuantity());
                        updateProductStockStmt.setInt(2, productId);
                        int rowsUpdated = updateProductStockStmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            conn.commit();
                            return true;
                        }
                    }
                }

                conn.rollback();
                return false;

            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (conn != null) {
						conn.close();
					}
                    if (getProductIdStmt != null) {
						getProductIdStmt.close();
					}
                    if (updateProductStockStmt != null) {
						updateProductStockStmt.close();
					}
                    if (salesOrderStmt != null) {
						salesOrderStmt.close();
					}
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
		public List<SalesOrder> getAllSalesDeatils_v1() {
            List<SalesOrder> salesOrders = new ArrayList<>();
            Connection con = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {

                con = DBConnection.getConnection();
                stmt = con.createStatement();
                String query = "SELECT so.customer_id, so.product_id, p.name as product_name, so.quantity, so.order_date, so.total_amount, " +
                        "concat(c.first_name ,' ',c.last_name) as customer_name, c.phone as customer_phone " +
                        "FROM SalesOrders so " +
                        "JOIN Products p ON so.product_id = p.product_id " +
                        "JOIN Customers c ON so.customer_id = c.customer_id";

         rs = stmt.executeQuery(query);

         while (rs.next()) {
             SalesOrder salesOrder = new SalesOrder();
             salesOrder.setCustomerId(rs.getInt("customer_id"));
             salesOrder.setProductId(rs.getInt("product_id"));
             salesOrder.setProduct(rs.getString("product_name"));
             salesOrder.setQuantity(rs.getInt("quantity"));
             salesOrder.setOrderDate(rs.getDate("order_date"));
             salesOrder.setAmount(rs.getDouble("total_amount"));
             salesOrder.setCustomerName(rs.getString("customer_name"));
             salesOrder.setCustomerPhone(rs.getString("customer_phone"));
             salesOrders.add(salesOrder);
         }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
						rs.close();
					}
                    if (stmt != null) {
						stmt.close();
					}
                    if (con != null) {
						con.close();
					}
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return salesOrders;
        }



		@Override
		public void addProducts(Product product) {
	        Connection con = null;
	        PreparedStatement stmt = null;

	        try {
	            con = DBConnection.getConnection();
	            stmt = con.prepareStatement("INSERT INTO Products (name, category, price, stock, vendor_id) VALUES (?, ?, ?, ?, ?)");
	            stmt.setString(1, product.getProductName());
	            stmt.setString(2, product.getCategory());
	            stmt.setDouble(3, product.getPrice());
	            stmt.setInt(4, product.getStock());
	            stmt.setInt(5, product.getVendorId());
	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (stmt != null) {
	                try {
	                    stmt.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (con != null) {
	                try {
	                    con.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    @Override
	    public void UpdateProductDetails(Product product) {
	        Connection con = null;
	        PreparedStatement stmt = null;

	        try {
	            con = DBConnection.getConnection();
	            stmt = con.prepareStatement("UPDATE Products SET name = ?, category = ?, price = ?, stock = ?, vendor_id = ? WHERE product_id = ?");
	            stmt.setString(1, product.getProductName());
	            stmt.setString(2, product.getCategory());
	            stmt.setDouble(3, product.getPrice());
	            stmt.setInt(4, product.getStock());
	            stmt.setInt(5, product.getVendorId());
	            stmt.setInt(6, product.getProductId());
	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (stmt != null) {
	                try {
	                    stmt.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (con != null) {
	                try {
	                    con.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    @Override
	    public List<Vendor> getAllVendor() {
	        List<Vendor> vendors = new ArrayList<>();
	        Connection con = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        try {
	            con = DBConnection.getConnection();
	            stmt = con.createStatement();
	            rs = stmt.executeQuery("SELECT * FROM Vendors");

	            while (rs.next()) {
	                Vendor vendor = new Vendor();
	                vendor.setVendorId(rs.getInt("vendor_id"));
	                vendor.setName(rs.getString("name"));
	                vendor.setContactName(rs.getString("contact_name"));
	                vendor.setContactPhone(rs.getString("contact_phone"));
	                vendor.setAddress(rs.getString("address"));
	                vendor.setVendor_email(rs.getString("vendor_email"));
	                vendors.add(vendor);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (stmt != null) {
	                try {
	                    stmt.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (con != null) {
	                try {
	                    con.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return vendors;
	    }


	    @Override
		public void addNewVendor(Vendor vendor) {
	        Connection con = null;
	        PreparedStatement stmt = null;
	        if (validationCheck(vendor)) {
	            try {
	                con = DBConnection.getConnection();
	                stmt = con.prepareStatement("INSERT INTO Vendors (name, contact_name, contact_phone, address, vendor_email) VALUES (?, ?, ?, ?, ?)");
	                stmt.setString(1, vendor.getName());
	                stmt.setString(2, vendor.getContactName());
	                stmt.setString(3, vendor.getContactPhone());
	                stmt.setString(4, vendor.getAddress());
	                stmt.setString(5, vendor.getVendor_email());
	                stmt.executeUpdate();

	            } catch (SQLException e) {
	                e.printStackTrace();
	            } finally {
	                if (stmt != null) {
	                    try {
	                        stmt.close();
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                }
	                if (con != null) {
	                    try {
	                        con.close();
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    }
	    private boolean validationCheck(Vendor vendor) {

	        if (vendor.getName() == null || vendor.getName().isEmpty() || vendor.getContactName() == null || vendor.getContactName().isEmpty()) {
	            return false;
	        }
	        if (vendor.getContactPhone() == null || vendor.getContactPhone().isEmpty() || vendor.getAddress() == null || vendor.getAddress().isEmpty()) {
	            return false;
	        }
	        if (vendor.getVendor_email() == null || vendor.getVendor_email().isEmpty()) {
	            return false;
	        }
	        return true;
	    }

		@Override
		 public boolean saveInvoiceDetails_v1(int cId, Double totalAmount) {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        boolean success = false;

	        try {

	            conn =  DBConnection.getConnection();

	            String sql = "INSERT INTO Invoices (customer_id, invoice_date, total_amount) VALUES (?, CURRENT_DATE, ?)";

	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, cId);
	            pstmt.setDouble(2, totalAmount);

	            int rowsInserted = pstmt.executeUpdate();

	            if (rowsInserted > 0) {
	                success = true;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return success;
	    }

		@Override
		public boolean updateCreditPoints_v1(int customerId, double totalAmount) {
	        Connection con = null;
	        PreparedStatement stmt = null;

	        try {
	            con = DBConnection.getConnection();

	            double creditPoints = totalAmount / 1000;
	            String sql = "UPDATE Customers SET creadit_points = ? WHERE customer_id = ?";
	            stmt = con.prepareStatement(sql);
	            stmt.setDouble(1, creditPoints);
	            stmt.setInt(2, customerId);

	            int rowsUpdated = stmt.executeUpdate();
	            return rowsUpdated > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        } finally {
	            if (stmt != null) {
	                try {
	                    stmt.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (con != null) {
	                try {
	                    con.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

		public int getCreditPoints(int customerId) {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        int creditPoints = -1;
	        try {

	            conn = DBConnection.getConnection();

	            String sql = "SELECT creadit_points FROM Customers WHERE customer_id = ?";

	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, customerId);

	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                creditPoints = rs.getInt("creadit_points");
	            } else {
	            	creditPoints=0;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {

	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return creditPoints;
	    }

		@Override
		public List<Customer> getAllCustomers_v2() {
			List<Customer> customers = new ArrayList<>();
	        Connection con = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        try {
	            con = DBConnection.getConnection();
	            stmt = con.createStatement();
	            rs = stmt.executeQuery("SELECT * FROM Customers");

	            while (rs.next()) {
	                Customer customer = new Customer();
	                customer.setCustomerId(rs.getInt("customer_id"));
	                customer.setFirstName(rs.getString("first_name"));
	                customer.setLastName(rs.getString("last_name"));
	                customer.setEmail(rs.getString("email"));
	                customer.setPhone(rs.getString("phone"));
	                customer.setAddress(rs.getString("address"));
	                customer.setCreditPoints(rs.getInt("creadit_points"));
	                customers.add(customer);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (stmt != null) {
	                try {
	                    stmt.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (con != null) {
	                try {
	                    con.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return customers;
	    }

	public List getAllSalesDeatils_v2(){
		  List al = new ArrayList();
		  al.add(groupProducts(getAllSalesDeatils_v1()));
		  System.out.println("getAllSalesDeatils_v2 "+al);
		  return al;
		}

	public  Map<String, List<SalesOrder>> groupProducts(List<SalesOrder> salesList) {
        return salesList.stream()
                .collect(Collectors.groupingBy(SalesOrder::getCustomerName));
    }
    }