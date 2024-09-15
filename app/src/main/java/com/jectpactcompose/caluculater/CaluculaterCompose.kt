package com.jectpactcompose.caluculater

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EditTextFieldCompose() {
    var enterValue by remember {
        mutableStateOf("")
    }

    var isError = false
    Column(
        modifier = Modifier
            .padding(10.dp)
            .padding(top = 40.dp)
            .background(color = Color.White)
    ) {
        if (enterValue.toDoubleOrNull() is Double) {
            Log.e("check", " entered value is " + enterValue)
        } else if (!enterValue.equals("")) {
            Log.e("check", " entered value is 2 " + enterValue)
            isError = true
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(5.dp),
            value = enterValue,
            onValueChange = { it ->
                enterValue = it
            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 30.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right
            ),
//            enabled = false,
            readOnly = true,
            isError = isError,
        )
//        if (isError) {
//            Text(
//                text = "please enter number",
//                color = Color.Red,
//                fontSize = 10.sp
//            )
//        }

        Spacer(modifier = Modifier.height(10.dp))

        // BigButtons, passing the lambda to update enterValue
        BigButtons(array = arrayOf("AC", "/", "=", "0")) {
            if (it.equals("AC")) {
                enterValue = ""
            } else if (it.equals("=")) {
//                enterValue = getSuprateValues(enterValue)
                enterValue = data(enterValue)
//                data()
            } else {
                enterValue += it // Update the text with the clicked button value
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ShowRowButtons, passing the lambda to update enterValue
        ShowRowButtons(array = arrayOf("7", "8", "9", "x")) {
            if (it.equals("x")) {
                enterValue += "*"
            } else
                enterValue += it // Update the text with the clicked button value
        }

        Spacer(modifier = Modifier.height(10.dp))

        ShowRowButtons(array = arrayOf("4", "5", "6", "+")) {
            enterValue += it
        }

        Spacer(modifier = Modifier.height(10.dp))

        ShowRowButtons(array = arrayOf("1", "2", "3", "-")) {
            enterValue += it
        }
    }
}

@Composable
fun ButtonCompose(
    buttonText: String,
    width: Dp = 80.dp,
    height: Dp = 100.dp,
    buttonColor: Color = Color.Unspecified,
    onClick: (String) -> Unit // Accept a lambda to handle button click
) {
    Button(
        modifier = Modifier
            .width(width)
            .height(height),
        onClick = {
            onClick(buttonText) // Pass the buttonText to the lambda
        },
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(
            text = buttonText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun ShowRowButtons(array: Array<String>, onButtonClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        array.forEach { buttonText ->
            ButtonCompose(buttonText, onClick = onButtonClick) // Pass the callback to ButtonCompose
        }
    }
}

@Composable
fun BigButtons(array: Array<String>, onButtonClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ButtonCompose(array[0], buttonColor = Color.Red, onClick = onButtonClick)
        ButtonCompose(array[1], onClick = onButtonClick)
        ButtonCompose(array[3], onClick = onButtonClick)
        ButtonCompose(array[2], onClick = onButtonClick)
    }
}

//@Preview(showBackground = true)
@Composable
fun EditTextFieldComposePreview() {
    EditTextFieldCompose()
}

fun doValidation(first: String, oper: Any, second: String): String {
    var first = first.toDoubleOrNull()
    var second = second.toDoubleOrNull()
    var result = 0.0
    if (oper.equals("+")) {
        result = first!! + second!!
    } else if(oper.equals("-")){
        result = first!! - second!!
    } else if(oper.equals("*")){
        result = first!! * second!!
    } else if(oper.equals("/")){
        result = first!! / second!!
    }
    return result.toString()
}

fun getSuprateValues(input: String): String {


    val regex = Regex("([0-9]+)([*+-/])([0-9]+)")
    val matchResult = regex.find(input)
    var result = ""

    if (matchResult != null) {
        val (first, operator, second) = matchResult.destructured
        println("First value: $first")
        println("Operator: $operator")
        println("Second value: $second")
        result =  doValidation(first, operator, second)
    }
    return result
}

fun data(data : String) :String{
//    val data = "7/8*3-4+8"
    var first = ""
    var opr = ""
    var second = ""
    for (i in data){
        println("first time first $first second $second opr $opr")
        if(i.equals('-') || i.equals('+') || i.equals('*') || i.equals('/')) {
            Log.e("check", "data: ${i}" )
            if(opr.isNotEmpty()) {
                first = doValidation(first,opr,second)
                second =""
                opr = i.toString()
//                println( "second time first $first second $second opr $opr")
            } else {
                opr = i.toString()
            }
        } else {
            if(opr.isEmpty()){
                first += i
            } else {
                second += i

            }
        }

    }
    if (opr.isNotEmpty() && second.isNotEmpty()) {
        first = doValidation(first, opr, second) // Evaluate the final operation
    }
    println("validation "+first)
    return  first;
}
