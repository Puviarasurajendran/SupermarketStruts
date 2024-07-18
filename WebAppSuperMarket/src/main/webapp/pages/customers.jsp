
       
    <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="/struts-tags" prefix="s" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fresh Supermarket Dashboard</title>
     <link rel="stylesheet" href="<c:url value='/css/styles.css' />">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>Fresh</h2>
        </div>
        <ul class="sidebar-menu">
            <li><a href="<c:url value='/pages/customers.jsp' />"><i class="fas fa-users"></i> Customers</a></li>
            <li><a href="<c:url value='/pages/products.jsp' />"><i class="fas fa-boxes"></i> Products</a></li>
            <li><a href="<c:url value='/pages/sales_summary.jsp' />"><i class="fas fa-chart-line"></i> Sales Summary</a></li> 
            <li><a href="<c:url value='/pages/invoice_billing.jsp' />"><i class="fas fa-file-invoice-dollar"></i>Invoice Billing</a></li> 
            <li><a href="<c:url value='/pages/vendor.jsp' />"><i class="fas fa-truck"></i> Vendors</a></li> 
            <li><a href="<c:url value='/pages/purchase_order.jsp' />"> <i class="fas fa-receipt"></i> Purchase Orders</a></li>  
        </ul>
    </div>
    <div class="main-content">
         <div id="customers" class="content-section">
            <h2>Customers</h2>
            <!-- Form to add a customer -->
            <div class="form-section">
                <s:form action="Customers" method="post">
                    <s:textfield name="customer.firstName" label="First Name" required="true" />
                    <s:textfield name="customer.lastName" label="Last Name" required="true"/>
                    <s:textfield name="customer.email" label="Email" required="true"/>
                    <s:textfield name="customer.phone" label="Phone" required="true"/>
                    <s:textfield name="customer.address" label="Address" required="true"/>
                    <s:submit value="Add Customer" cssClass="s2submit-btn" required="true"/>

                </s:form>
            </div>
          <!--    <div class="show-customers-button">
            <s:a action="Customers" >Show Customers</s:a>
              </div> -->
            
            <div  id="customerTable" class="table-section">
                <table>
                    <thead>
                        <tr>
                            <th>Customer ID</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Address</th>
                        </tr>
                    </thead>
                    <tbody>
                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <script>
    
    document.addEventListener('DOMContentLoaded', async () => {
        try {
            const response = await fetch('http://localhost:8080/WebAppSuperMarket/pages/Customers');
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            const customers = await response.json();
            const tableBody = document.getElementById('customerTable').getElementsByTagName('tbody')[0];

            customers.forEach(customer => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = customer.customerId;
                row.insertCell().textContent = customer.firstName;
                row.insertCell().textContent = customer.lastName;
                row.insertCell().textContent = customer.email;
                row.insertCell().textContent = customer.phone;
                row.insertCell().textContent = customer.address;
            });
        } catch (error) {
            console.error('Error fetching and populating customers table:', error);
        }
    });

    </script>
</body>
</html>
    
