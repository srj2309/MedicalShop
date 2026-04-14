package com.example.medicalshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

data class Product(val name: String, val price: Int, val image: Int)

@Composable
fun App() {
    var screen by remember { mutableStateOf("login") }
    val cart = remember { mutableStateListOf<Product>() }

    when (screen) {
        "login" -> LoginScreen {
            screen = "shop"
        }
        "shop" -> ShopScreen(cart) { screen = "cart" }
        "cart" -> CartScreen(cart) { screen = "payment" }
        "payment" -> PaymentScreen { screen = "success" }
        "success" -> SuccessScreen()
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("💊 Medical Shop Login", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (error.isNotEmpty()) {
            Text(error, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (username == "sanika" && password == "18018") {
                    onLoginSuccess()
                } else {
                    error = "Invalid Username or Password"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}
@Composable
fun ShopScreen(cart: MutableList<Product>, goToCart: () -> Unit) {

    val products = listOf(
        Product("Paracetamol", 20, R.drawable.tablet),
        Product("Cough Syrup", 60, R.drawable.syrup),
        Product("Injection", 120, R.drawable.injection),
        Product("Bandage", 15, R.drawable.bandage),
        Product("Thermometer", 150, R.drawable.thermometer),
        Product("Mask", 10, R.drawable.mask)
    )

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("💊 Medical Shop", color = Color.Red)

            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { goToCart() }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(products) { product ->

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp)
                    ) {

                        Image(
                            painter = painterResource(product.image),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )

                        Text(product.name)
                        Text("₹${product.price}", color = Color.Green)

                        Button(onClick = { cart.add(product) }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun CartScreen(cart: List<Product>, goToPayment: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val total = cart.sumOf { it.price }

    Column(modifier = Modifier.padding(10.dp)) {

        Text("🛒 Your Cart", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cart) { product ->
                Text("${product.name} - ₹${product.price}")
            }
        }

        Text("Total: ₹$total", color = Color.Green)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") }
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty()) {
                    goToPayment()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Proceed to Payment")
        }
    }
}


@Composable
fun PaymentScreen(onSuccess: () -> Unit) {

    var method by remember { mutableStateOf("COD") }
    var upi by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {

        Text("💳 Payment Options")

        Row {
            RadioButton(selected = method == "COD", onClick = { method = "COD" })
            Text("COD")
        }

        Row {
            RadioButton(selected = method == "UPI", onClick = { method = "UPI" })
            Text("UPI")
        }

        Row {
            RadioButton(selected = method == "QR", onClick = { method = "QR" })
            Text("QR Scanner")
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (method) {

            "UPI" -> {
                OutlinedTextField(
                    value = upi,
                    onValueChange = { upi = it },
                    label = { Text("Enter UPI ID") }
                )
            }

            "QR" -> {
                Text("📷 Scan QR Code")
                Image(
                    painter = painterResource(android.R.drawable.ic_menu_camera),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (method == "UPI" && upi.isEmpty()) return@Button
                onSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }
    }
}



@Composable
fun SuccessScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✅ Order Successful!", color = Color.Green)
    }
}