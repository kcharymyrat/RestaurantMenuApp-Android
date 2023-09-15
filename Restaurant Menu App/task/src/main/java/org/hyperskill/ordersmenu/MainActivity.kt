package org.hyperskill.ordersmenu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.ordersmenu.theme.PlayOrdersMenuTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayOrdersMenuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    OrderMenu()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrderMenu() {
    val recipesNameToStockAmount = mutableMapOf(
        "Fettuccine" to 5,
        "Risotto" to 6,
        "Gnocchi" to 4,
        "Spaghetti" to 3,
        "Lasagna" to 5,
        "Steak Parmigiana" to 2
    )
    val context = LocalContext.current
    val orderMap = remember { mutableStateOf(mutableMapOf<String, Int>()) }
    val menuMap = remember {
        mutableStateOf(recipesNameToStockAmount)
    }
    val newOrders = remember {
        mutableStateMapOf(
            "Fettuccine" to 0,
            "Risotto" to 0,
            "Gnocchi" to 0,
            "Spaghetti" to 0,
            "Lasagna" to 0,
            "Steak Parmigiana" to 0
        )
    }
    val amountOrdered = remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Title()
        for ((name, stock) in menuMap.value) {
            MenuItem(name, stock, menuMap, newOrders, orderMap, amountOrdered)
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                onClick = {
                    if (orderMap.value.isNotEmpty()) {
                        var toastMessage = "Ordered:"
                        for ((name, amount) in orderMap.value) {
                            toastMessage += "\n==> $name: $amount"
                            menuMap.value[name] = menuMap.value[name]!!.toInt() - amount
                            newOrders[name] = 0
                        }
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                        orderMap.value.clear()
                        println("recipesNameToStockAmount = $recipesNameToStockAmount")
                    }
                }
            ) {
                Text(
                    text = "Make Order",
                    fontSize = 24.sp
                )
            }
        }

    }
}

@Composable
fun MenuItem(
    dishName: String,
    stock: Int,
    menuMap: MutableState<MutableMap<String, Int>>,
    newOrders: SnapshotStateMap<String, Int>,
    orderMap: MutableState<MutableMap<String, Int>>,
    amountOrdered: MutableState<Int>,
) {
//    println("in MenuItem: $recipesNameToStockAmount")
    val amountStock = stock

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = dishName,
            style = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 24.sp
            ),
            modifier = Modifier.weight(1f),
            color = if (getColor(newOrders, menuMap, dishName)) Color.Red else Color.Black
        )
        Button(onClick = {
            if (newOrders[dishName]!!.toInt() < menuMap.value[dishName]!!.toInt()) {

                newOrders[dishName] = newOrders[dishName]!!.toInt() + 1
                if (newOrders[dishName]!!.toInt() > 0) {
                    orderMap.value[dishName] = newOrders[dishName]!!.toInt()
                }
                println("+, newOrders.[$dishName]!!.toInt()  = ${newOrders[dishName]!!.toInt()}")
                amountOrdered.value++
            }
        }) {
            Text(
                text = "+",
                fontSize = 24.sp
            )
        }
        Text(
            text = getText(newOrders[dishName].toString(), dishName),
            fontSize = 20.sp,
            modifier = Modifier.padding(12.dp, 3.dp)
        )
        Button(onClick = {
            if (newOrders[dishName]!!.toInt() > 0) {
                newOrders[dishName] = newOrders[dishName]!!.toInt() - 1
                orderMap.value[dishName] = newOrders[dishName]!!.toInt()
                println("-, newOrders[$dishName]!!.toInt()  = ${newOrders[dishName]!!.toInt()}")
                if (newOrders[dishName]!!.toInt() == 0) orderMap.value.remove(dishName)
            }
            amountOrdered.value--
        }) {
            Text(
                text = "-",
                fontSize = 24.sp
            )
        }
    }
}

fun getText(text: String, dishName: String): String {
    println("newOrders.value[$dishName].toString() = $text")
    return text
}

fun getColor(
    newOrders: SnapshotStateMap<String, Int>,
    menuMap: MutableState<MutableMap<String, Int>>,
    dishName: String
): Boolean {
    println("newOrders[$dishName]!!.toInt() = ${newOrders[dishName]!!.toInt()}")
    println("menuMap.value[$dishName]!!.toInt() = ${menuMap.value[dishName]!!.toInt()}")
    println(newOrders[dishName]!!.toInt() == menuMap.value[dishName]!!.toInt())
    return newOrders[dishName]!!.toInt() == menuMap.value[dishName]!!.toInt()
}

@Composable
fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Orders Menu",
        style = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 48.sp
        )
    )
}




//package org.hyperskill.ordersmenu
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateMapOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.snapshots.SnapshotStateMap
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import org.hyperskill.ordersmenu.theme.PlayOrdersMenuTheme
//
//val recipesNameToStockAmount = mutableMapOf(
//    "Fettuccine" to 5,
//    "Risotto" to 6,
//    "Gnocchi" to 4,
//    "Spaghetti" to 3,
//    "Lasagna" to 5,
//    "Steak Parmigiana" to 2
//)
//
//
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            PlayOrdersMenuTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    OrderMenu()
//                }
//            }
//        }
//    }
//}
//
//@Preview(showSystemUi = true)
//@Composable
//fun OrderMenu() {
//    val context = LocalContext.current
//    val orderMap = remember { mutableStateOf(mutableMapOf<String, Int>()) }
//    val menuMap = remember {
//        mutableStateOf(recipesNameToStockAmount)
//    }
//    val newOrders = remember {
//        mutableStateMapOf(
//            "Fettuccine" to 0,
//            "Risotto" to 0,
//            "Gnocchi" to 0,
//            "Spaghetti" to 0,
//            "Lasagna" to 0,
//            "Steak Parmigiana" to 0
//        )
//    }
//    val amountOrdered = remember {
//        mutableStateOf(0)
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//    ) {
//        Title()
//        for ((name, stock) in menuMap.value) {
//            MenuItem(name, stock, menuMap, newOrders, orderMap, amountOrdered)
//        }
//        Row (
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Button(
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color.Black,
//                    contentColor = Color.White
//                ),
//                onClick = {
//                    if (orderMap.value.isNotEmpty()) {
//                        var toastMessage = "Ordered:"
//                        for ((name, amount) in orderMap.value) {
//                            toastMessage += "\n==> $name: $amount"
//                            menuMap.value[name] = menuMap.value[name]!!.toInt() - amount
//                            newOrders[name] = 0
//                        }
//                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
//                        orderMap.value.clear()
//                        println("recipesNameToStockAmount = $recipesNameToStockAmount")
//                    }
//                }
//            ) {
//                Text(
//                    text = "Make Order",
//                    fontSize = 24.sp
//                )
//            }
//        }
//
//    }
//}
//
//@Composable
//fun MenuItem(
//    dishName: String,
//    stock: Int,
//    menuMap: MutableState<MutableMap<String, Int>>,
//    newOrders: SnapshotStateMap<String, Int>,
//    orderMap: MutableState<MutableMap<String, Int>>,
//    amountOrdered: MutableState<Int>,
//) {
//    println("in MenuItem: $recipesNameToStockAmount")
//    val amountStock = stock
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//    ) {
//        Text(
//            text = dishName,
//            style = TextStyle(
//                textAlign = TextAlign.Start,
//                fontSize = 24.sp
//            ),
//            modifier = Modifier.weight(1f),
//            color = if (getColor(newOrders, menuMap, dishName)) Color.Red else Color.Black
//        )
//        Button(onClick = {
//            if (newOrders[dishName]!!.toInt() < menuMap.value[dishName]!!.toInt()) {
//
//                newOrders[dishName] = newOrders[dishName]!!.toInt() + 1
//                if (newOrders[dishName]!!.toInt() > 0) {
//                    orderMap.value[dishName] = newOrders[dishName]!!.toInt()
//                }
//                println("+, newOrders.[$dishName]!!.toInt()  = ${newOrders[dishName]!!.toInt()}")
//                amountOrdered.value++
//            }
//        }) {
//            Text(
//                text = "+",
//                fontSize = 24.sp
//            )
//        }
//        Text(
//            text = getText(newOrders[dishName].toString(), dishName),
//            fontSize = 20.sp,
//            modifier = Modifier.padding(12.dp, 3.dp)
//        )
//        Button(onClick = {
//            if (newOrders[dishName]!!.toInt() > 0) {
//                newOrders[dishName] = newOrders[dishName]!!.toInt() - 1
//                orderMap.value[dishName] = newOrders[dishName]!!.toInt()
//                println("-, newOrders[$dishName]!!.toInt()  = ${newOrders[dishName]!!.toInt()}")
//                if (newOrders[dishName]!!.toInt() == 0) orderMap.value.remove(dishName)
//            }
//            amountOrdered.value--
//        }) {
//            Text(
//                text = "-",
//                fontSize = 24.sp
//            )
//        }
//    }
//}
//
//fun getText(text: String, dishName: String): String {
//    println("newOrders.value[$dishName].toString() = $text")
//    return text
//}
//
//fun getColor(
//    newOrders: SnapshotStateMap<String, Int>,
//    menuMap: MutableState<MutableMap<String, Int>>,
//    dishName: String
//): Boolean {
//    println("newOrders[$dishName]!!.toInt() = ${newOrders[dishName]!!.toInt()}")
//    println("menuMap.value[$dishName]!!.toInt() = ${menuMap.value[dishName]!!.toInt()}")
//    println(newOrders[dishName]!!.toInt() == menuMap.value[dishName]!!.toInt())
//    return newOrders[dishName]!!.toInt() == menuMap.value[dishName]!!.toInt()
//}
//
//@Composable
//fun Title() {
//    Text(
//        modifier = Modifier.fillMaxWidth(),
//        text = "Orders Menu",
//        style = TextStyle(
//            textAlign = TextAlign.Center,
//            fontSize = 48.sp
//        )
//    )
//}
