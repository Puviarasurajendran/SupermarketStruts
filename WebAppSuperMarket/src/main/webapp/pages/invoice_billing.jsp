<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
            <li><a href="<c:url value='/pages/invoice_billing.jsp' />"><i class="fas fa-file-invoice-dollar"></i> Invoice Billing</a></li>
            <li><a href="<c:url value='/pages/vendor.jsp' />"><i class="fas fa-truck"></i> Vendors</a></li>
            <li><a href="<c:url value='/pages/purchase_order.jsp' />"><i class="fas fa-receipt"></i> Purchase Orders</a></li>
        </ul>
    </div>
    <div class="main-content">
        <div id="invoice-billing" class="content-section">
            <h2>Invoice Billing</h2>
            <!-- Invoice Billing Form -->
            <div class="invoice-form" id=invoice_form>
            
         <label for="customerPhone">Customer Phone:</label>
        <input type="text" id="customerPhone" name="customerPhone" onblur="fetchCustomerDetails()"><br><br>
        <label for="customerId">Customer ID:</label>
        <input type="text" id="customerId" name="customerId" readonly><br><br>
        <label for="customerName">Customer Name:</label>
        <input type="text" id="customerName" name="customerName" readonly><br><br>

                <label for="productList">Product List:</label>
                <select id="productList" name="productList" onchange="fetchProductPrice()" required>
                    <!-- Populate with products 
                    <option value="Product1" data-price="10">Product 1 - $10</option>
                    <option value="Product2" data-price="20">Product 2 - $20</option>
                    <option value="Product3" data-price="30">Product 3 - $30</option>
                         Add more products as needed -->
                </select>

                <label for="productQuantity">Quantity:</label>
                <input type="number" id="productQuantity" name="productQuantity" min="1" required>

                <label for="productPrice">Price:</label>
                <input type="text" id="productPrice" name="productPrice" readonly>

                <label for="totalAmount">Total Amount:</label>
                <input type="text" id="totalAmount" name="totalAmount" readonly>
                
                <label for="credit_ptn">Credit point:</label>
                <input type="text" id="credit_ptn" name="credit_ptn" readonly>

                <button type="button" class="add-product-button" onclick="addProduct()">Add Product</button>
                <button type="button" style="margin: 10px 20px; background-color: #66b3ff;" class="add-product-button" onclick="printBill()">Print Bill</button>
                
            </div>

            <!-- Invoice Billing Table -->
            <table class="invoice-table" id="invoiceTable">
                <thead>
                    <tr>
                        <th>Customer ID</th>
                        <th>Customer Name</th>
                        <th>Customer Mobile Number</th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Amount</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Dynamic rows will be added here -->
                </tbody>
            </table>

            <!-- Display Total Amount -->
            <div class="total-amount">
                Total: $<span id="grandTotal">0.00</span>
            </div>
        </div>
    </div>
    <script>
    let customerList = {};
    let productList = {};

    async function fetchCustomers() {
        try {
            const response = await fetch('http://localhost:8080/WebAppSuperMarket/api/v1/Customers');
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            const responseText = await response.text();
           // console.log('Raw response text:', responseText);

            const data = JSON.parse(responseText);
            console.log('Fetch Customer Response data successfully:', data);
            
            customerList = data.reduce((acc, customer) => {
                acc[customer.phone] = { id: customer.customerId, name: customer.firstName + " " + customer.lastName , credit_ptn:customer.creditPoints};
                return acc;
            }, {});
            console.log('Fetch customerList data successfully:', customerList);
        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    }

    function fetchCustomerDetails() {
        const customerPhone = document.getElementById('customerPhone').value;
        console.log('enter into fetchCustomerDetails customerPhone:', customerPhone);
        const customer = customerList[customerPhone];
        console.log('enter into fetchCustomerDetails:', customer);
        if (customer) {
            document.getElementById('customerId').value = customer.id;
            document.getElementById('customerName').value = customer.name;
            document.getElementById('credit_ptn').value = customer.credit_ptn;
        } else {
            alert("New Customer, please create customer details");
            window.location.href = "http://localhost:8080/WebAppSuperMarket/pages/customers.jsp";
            document.getElementById('customerId').value = '';
            document.getElementById('customerName').value = '';
            document.getElementById('credit_ptn').value = 0;
        }
    }

    async function fetchProducts() {
        try {
            const response = await fetch('http://localhost:8080/WebAppSuperMarket/api/v1/Products');
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            const responseText = await response.text();
        //    console.log('Raw response text:', responseText);

            const data = JSON.parse(responseText);
            console.log('Fetch Customer Response data successfully:', data);
            
            const productListElement = document.getElementById('productList');
            data.forEach(product => {
                const option = document.createElement('option');
                option.value = product.productName;
                option.textContent = product.productName;
                option.setAttribute('data-price', product.price);
                productListElement.appendChild(option);
                productList[product.productName] = product.price;
            });
            console.log('Fetch productList data successfully:', productList);
        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    }

    function fetchProductPrice() {
        const productSelect = document.getElementById('productList');
        const selectedOption = productSelect.options[productSelect.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        document.getElementById('productPrice').value = price;
        calculateTotalAmount();
    }

    function calculateTotalAmount() {
        const price = parseFloat(document.getElementById('productPrice').value);
        const quantity = parseFloat(document.getElementById('productQuantity').value);
        const totalAmount = price * quantity;
        document.getElementById('totalAmount').value = totalAmount.toFixed(2);
    }

    function addProduct() {
        const customerId = document.getElementById('customerId').value;
        const customerName = document.getElementById('customerName').value;
        const customerPhone = document.getElementById('customerPhone').value;
        const productList = document.getElementById('productList');
        const product = productList.options[productList.selectedIndex].text;
        const productQuantity = document.getElementById('productQuantity').value;
        const productPrice = document.getElementById('productPrice').value;
        const totalAmount = document.getElementById('totalAmount').value;

        if (!customerId || !customerName || !customerPhone || !product || !productQuantity || !productPrice || !totalAmount) {
            alert("Please fill out all fields before adding a product.");
            return;
        }

        const table = document.getElementById('invoiceTable').getElementsByTagName('tbody')[0];
        const newRow = table.insertRow();

        newRow.insertCell().textContent = customerId;
        newRow.insertCell().textContent = customerName;
        newRow.insertCell().textContent = customerPhone;
        newRow.insertCell().textContent = product;
        newRow.insertCell().textContent = productQuantity;
        newRow.insertCell().textContent = totalAmount;

        const removeCell = newRow.insertCell();
        const removeButton = document.createElement('span');
        removeButton.className = 'remove-button';
        removeButton.textContent = 'Remove';
        removeButton.addEventListener('click', () => removeProduct(removeButton, parseFloat(totalAmount)));
        removeCell.appendChild(removeButton);

        updateGrandTotal(parseFloat(totalAmount));

        document.getElementById('productQuantity').value = '';
        document.getElementById('productPrice').value = '';
        document.getElementById('totalAmount').value = '';
    }

    function removeProduct(button, amount) {
        const row = button.parentElement.parentElement;
        row.parentElement.removeChild(row);
        updateGrandTotal(-amount);
    }

    function updateGrandTotal(amount) {
        const grandTotalElement = document.getElementById('grandTotal');
        let grandTotal = parseFloat(grandTotalElement.innerText);
        grandTotal += amount;
        grandTotalElement.innerText = grandTotal.toFixed(2);
    }

    function printBill() {
        const table = document.getElementById('invoiceTable').getElementsByTagName('tbody')[0];
        const rows = table.getElementsByTagName('tr');
        const invoiceData = [];

        for (let i = 0; i < rows.length; i++) {
            const cells = rows[i].getElementsByTagName('td');
            const rowData = {
                customerId: cells[0].textContent,
                customerName: cells[1].textContent,
                customerPhone: cells[2].textContent,
                product: cells[3].textContent,
                quantity: cells[4].textContent,
                amount: cells[5].textContent
            };
            invoiceData.push(rowData);
        }
        const discount = document.getElementById('credit_ptn').value;
        console.log('Fetch Discount  :',discount);
        
        const invoice = {
            items: invoiceData,
            totalAmount: document.getElementById('grandTotal').textContent
        };
        var dis = invoice.totalAmount * ((discount*5) / 100);
        const disPer=discount*5;
        const total = invoice.totalAmount-dis;
        console.log('Discount ',dis);
        
        console.log('Fetch Invoice data successfully :',invoice);
        fetch('http://localhost:8080/WebAppSuperMarket/api/v1/SalesOrders', {
             method: 'POST',
             headers: {
                 'Content-Type': 'application/json'
             },
             body: JSON.stringify(invoice)
         })
         .then(response => response.json())
         .then(data => { 
         	console.log(data);
             if (data=== 'success') {                  
                 alert('Invoice saved successfully!');
                 console.log(invoice);
                 printInvoice(invoice,disPer,total);
             } else {
             	console.log(data.status);
                 alert('Failed to save invoice.');
             }
         })
         .catch(error => console.error('Error:', error));   
     }

    function printInvoice(invoice,disPer,total) {
        const printWindow = window.open('', '_blank');
        printWindow.document.write('<html><head><title>Invoice</title>');
        printWindow.document.write('<link rel="stylesheet" href="<c:url value="/css/styles.css" />">');
        printWindow.document.write('</head><body>');
        printWindow.document.write('<div id="invoice-billing" class="content-section">');
        printWindow.document.write('<h1>Fresh Super Market Invoice</h1>');
        printWindow.document.write('<h2>Customer ID: ' + invoice.items[0].customerId + '</h2>');
        printWindow.document.write('<h2>Customer Name: ' + invoice.items[0].customerName + '</h2>');
        printWindow.document.write('<h2>Mobile Number: ' + invoice.items[0].customerPhone + '</h2>');

        printWindow.document.write('<table border="1"><thead><tr><th>Product</th><th>Quantity</th><th>Amount</th></tr></thead><tbody>');

        invoice.items.forEach(item => {
            printWindow.document.write('<tr>' +
                '<td>' + item.product + '</td>' +
                '<td>' + item.quantity + '</td>' +
                '<td>' + item.amount + '</td>' +
            '</tr>');
        });
        printWindow.document.write('</div>');
        printWindow.document.write('</tbody></table>');
        printWindow.document.write('<h3>Subtotal: $' + invoice.totalAmount + '</h3>');
        printWindow.document.write('<h3>Discount:' + disPer + '%</h3>');
        printWindow.document.write('<h3>Total: $' + total + '</h3>');
        printWindow.document.write('</body></html>');
        printWindow.document.close();
        printWindow.print();
    }
    
    window.onload = () => {
        fetchCustomers();
        fetchProducts();
    };
        
    </script>
</body>
</html>
