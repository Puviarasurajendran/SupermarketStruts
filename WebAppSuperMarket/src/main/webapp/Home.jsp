<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fresh Supermarket Dashboard</title>
    <link rel="stylesheet" href="css/styles.css">
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
        <div class="welcome-section">
            <h1>Welcome to Fresh Supermarket Dashboard</h1>
             <div class="marquee">
            <div class="marquee-text">Welcome to Fresh Supermarket Dashboard! Manage your customers, products, sales, and more with ease.</div>
        </div>
            <p>Manage your customers, products, sales, and more with ease.</p>
            <div class="image-gallery">
                <img src="images/supermarket-1.jpg" alt="Supermarket Image 1">
                <img src="images/supermarket-2.jpg" alt="Supermarket Image 2">
                <img src="images/supermarket-3.jpg" alt="Supermarket Image 3">
            </div>
        </div>
    </div>
    </div>
    <script src="js/scripts.js"></script>
</body>
</html>
