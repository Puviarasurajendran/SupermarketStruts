
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="s" uri="/struts-tags" %>
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
       <div id="vendor" class="content-section">
       
         <h3>Add New Vendor</h3>
    <s:form action="Vendors" method="post">
        <s:textfield name="vendor.name" label="Organisation" required="true"/>
        <s:textfield name="vendor.contactName" label="Contact Name" required="true" />
        <s:textfield name="vendor.contactPhone" label="Contact Phone" required="true"/>
        <s:textfield name="vendor.address" label="Address" required="true"/>
        <s:textfield name="vendor.vendor_email" label="Email" required="true"/>
        <s:submit value="Add Vendor" cssClass="s2submit-btn"/>
    </s:form>
    <!--  <div class="show-customers-button">
            <s:a action="Vendors">Show Vendors</s:a>
            </div>
   Displaying the list of vendors -->
    <h3>Vendor List</h3>
    <table id="vendorTable" >
        <thead>
            <tr>
                <th>Vendor ID</th>
                <th>Oraganisation</th>
                <th>Contact Name</th>
                <th>Contact Phone</th>
                <th>Address</th>
                <th>Email</th>
            </tr>
        </thead>
        <tbody>
     
        </tbody>
    </table>
   </div>
    </div>
    </body>
    <script> 
    
    document.addEventListener('DOMContentLoaded', async () => {
        try {
            const response = await fetch('http://localhost:8080/WebAppSuperMarket/pages/Vendors');
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            const vendors = await response.json();
            const tableBody = document.getElementById('vendorTable').getElementsByTagName('tbody')[0];

            vendors.forEach(vendor => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = vendor.vendorId;
                row.insertCell().textContent = vendor.name;
                row.insertCell().textContent = vendor.contactName;
                row.insertCell().textContent = vendor.contactPhone;
                row.insertCell().textContent = vendor.address;
                row.insertCell().textContent = vendor.vendor_email;
            });
        } catch (error) {
            console.error('Error fetching and populating vendors table:', error);
        }
    });
    
    
    </script>
</body>
</html>
