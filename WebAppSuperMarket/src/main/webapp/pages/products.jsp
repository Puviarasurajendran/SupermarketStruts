<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
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
            <li><a href="<c:url value='/pages/invoice_billing.jsp' />"><i class="fas fa-file-invoice-dollar"></i> Invoice Billing</a></li>
            <li><a href="<c:url value='/pages/vendor.jsp' />"><i class="fas fa-truck"></i> Vendors</a></li>
            <li><a href="<c:url value='/pages/purchase_order.jsp' />"><i class="fas fa-receipt"></i> Purchase Orders</a></li>
        </ul>
    </div>
    <div class="main-content">
        <div id="products" class="content-section">
            <h2>Products</h2>
            <button type="button" id="toggleButton" class="add-product-button">Toggle Form</button>

            <!-- Form to add a product -->
            <div class="form-section" id="productForm">
                <s:form action="Products" >
              <!--       <s:textfield name="product.productId" id="productId" label="Product ID" required="true" /> -->
                    <s:textfield name="product.productName" id="productName" label="Product Name" required="true" />
                    <s:textfield name="product.category" id="category" label="Category" required="true" />
                    <s:textfield name="product.price" id="price" label="Price" required="true" />
                    <s:textfield name="product.stock" id="stock" label="Stock" required="true" />
                    <s:textfield name="product.vendorId" id="vendorId" label="Vendor ID" required="true" />
                    <s:submit value="Add Product" onclick="addProduct()" cssClass="add-product-button" />
                </s:form>
            </div>

            <!-- Products table -->
            <table id="productsTable" class="invoice-table" style="display: none;">
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Vendor ID</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Rows will be added dynamically by JavaScript -->
                </tbody>
            </table>
        </div>
    </div>
    <script>
        document.addEventListener('DOMContentLoaded', async () => {
            try {
                const response = await fetch('http://localhost:8080/WebAppSuperMarket/api/v1/Products');
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                const products = await response.json();
                const tableBody = document.getElementById('productsTable').getElementsByTagName('tbody')[0];

                products.forEach(product => {
                    const row = tableBody.insertRow();
                    row.insertCell().textContent = product.productId;
                    row.insertCell().textContent = product.productName;
                    row.insertCell().textContent = product.category;
                    row.insertCell().textContent = product.price;
                    row.insertCell().textContent = product.stock;
                    row.insertCell().textContent = product.vendorId;

                    // Add Edit and Save buttons
                    const actionCell = row.insertCell();
                    const editButton = document.createElement('button');
                    editButton.textContent = 'Edit';
                    editButton.className = 'edit-btn';
                    editButton.addEventListener('click', () => editRow(row));

                    const saveButton = document.createElement('button');
                    saveButton.textContent = 'Save';
                    saveButton.className = 'save-btn';
                    saveButton.style.display = 'none';
                    saveButton.addEventListener('click', () => saveRow(row));

                    actionCell.appendChild(editButton);
                    actionCell.appendChild(saveButton);
                });

                console.log('Fetch productList data successfully:', products);
            } catch (error) {
                console.error('There was a problem with the fetch operation:', error);
            }
        });
        
        function addProduct() {
            try {
             //   const productId = document.getElementById('productId').value;
                const productName = document.getElementById('productName').value;
                const category = document.getElementById('category').value;
                const price = document.getElementById('price').value;
                const stock = document.getElementById('stock').value;
                const vendorId = document.getElementById('vendorId').value;

                const requestBody = JSON.stringify({
                   // productId: productId,
                    productName: productName,
                    category: category,
                    price: price,
                    stock: stock,
                    vendorId: vendorId
                });
               console.log('addProduct requestBody',requestBody);
            const response = fetch('http://localhost:8080/WebAppSuperMarket/api/v1/Products', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: requestBody
                });  

                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }

                console.log('Product added successfully');
            } catch (error) {
                console.error('Error adding product:', error);
            }
        }

        function editRow(row) {
            let cells = row.querySelectorAll('td');
            cells.forEach((cell, index) => {
                if (index < 6) { // Assuming there are 6 editable fields per row
                    let value = cell.innerText;
                    cell.innerHTML = '<input type="text" value="' + value + '">';
                }
            });
            row.querySelector('.edit-btn').style.display = 'none';
            row.querySelector('.save-btn').style.display = 'inline';
        }

        function saveRow(row) {
            let inputs = row.querySelectorAll('input');
            let data = {
                productId: inputs[0].value,
                productName: inputs[1].value,
                category: inputs[2].value,
                price: parseFloat(inputs[3].value),
                stock: parseInt(inputs[4].value, 10),
                vendorId: inputs[5].value
            };
            // Making an AJAX request to the API
            fetch('http://localhost:8080/WebAppSuperMarket/api/v1/Products', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(json => {
                // Update UI after successful update
                const inputs = document.querySelectorAll('table input[type="text"]');
                for (let i = 0; i < inputs.length; i++) {
                    let cell = inputs[i].parentNode;
                    cell.innerHTML = inputs[i].value; // Set the updated text
                }  
              
                row.querySelector('.edit-btn').style.display = 'inline';
                row.querySelector('.save-btn').style.display = 'none';
            })
            .catch(error => console.error('Error updating product:', error));
        }

        // Function to toggle visibility of form and table
        function toggleFormAndTable() {
            var form = document.getElementById("productForm");
            var table = document.getElementById("productsTable");

            if (form.style.display === "none") {
                form.style.display = "block";
                table.style.display = "none";
            } else {
                form.style.display = "none";
                table.style.display = "block";
            }
        }

        // Event listener for button click
        document.getElementById("toggleButton").addEventListener("click", toggleFormAndTable);
        
        //window.onload = () => {
        //   fetchProducts();
       // };
    </script>
</body>
</html>
