package com.example.medicalshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.material3.RadioButton

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
    var loggedIn by remember { mutableStateOf(false) }

    if (!loggedIn) {
        LoginScreen { loggedIn = true }
    } else {
        ShopScreen()
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {

        Text("💊 Chintamani Medical Shop", color = Color.Red)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (username == "admin" && password == "1234") {
                onLogin()
            }
        }) {
            Text("Login")
        }
    }
}

@Composable
fun ShopScreen() {

    val products = listOf(
        Product("Paracetamol Tablet", 20, R.drawable.tablet),
        Product("Cough Syrup", 60, R.drawable.syrup),
        Product("Injection", 120, R.drawable.injection),
        Product("Bandage", 15, R.drawable.bandage),
        Product("Thermometer", 150, R.drawable.thermometer),
        Product("Face Mask", 10, R.drawable.mask),
        Product("Hand Sanitizer", 50, R.drawable.sanitizer),
        Product("Glucose Powder", 80, R.drawable.glucose),
        Product("Ointment", 45, R.drawable.ointment),
        Product("First Aid Kit", 200, R.drawable.firstaid)
    )

    val cart = remember { mutableStateListOf<Product>() }
    var goToCart by remember { mutableStateOf(false) }

    if (goToCart) {
        CartScreen(cart)
        return
    }

    Column(modifier = Modifier.padding(10.dp)) {

        Text("💊 Chintamani Medical Shop", color = Color.Red)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { product ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {

                    Image(
                        painter = painterResource(product.image),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.name)
                        Text("₹${product.price}", color = Color.Green)
                    }

                    Button(onClick = { cart.add(product) }) {
                        Text("Add")
                    }
                }
            }
        }

        Button(
            onClick = { goToCart = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go To Cart 🛒")
        }
    }
}
@Composable
fun CartScreen(cart: List<Product>) {

    var address by remember { mutableStateOf("") }
    var payment by remember { mutableStateOf("COD") }
    var success by remember { mutableStateOf(false) }

    if (success) {
        SuccessScreen()
        return
    }

    val total = cart.sumOf { it.price }

    Column(modifier = Modifier.padding(10.dp)) {

        Text("🛒 Your Cart", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cart) {
                Text("${it.name} - ₹${it.price}")
            }
        }

        Text("Total: ₹$total", color = Color.Green)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Enter Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Select Payment Method")

        Row {
            RadioButton(
                selected = payment == "COD",
                onClick = { payment = "COD" }
            )
            Text("Cash on Delivery", modifier = Modifier.clickable { payment = "COD" })
        }

        Row {
            RadioButton(
                selected = payment == "Online",
                onClick = { payment = "Online" }
            )
            Text("Online Payment", modifier = Modifier.clickable { payment = "Online" })
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (address.isNotEmpty()) {
                    success = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }
    }
}

@Composable
fun SuccessScreen() {
    Column(modifier = Modifier.padding(20.dp)) {
        Text("✅ Order Successful!", color = Color.Green)
        Text("Get well soon 🙏")
    }
}