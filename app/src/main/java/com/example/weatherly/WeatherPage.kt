﻿package com.example.weatherly

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import com.example.weatherly.api.NetworkResponse
import com.example.weatherly.api.WeatherModel
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage

@Composable
fun WeatherPage(viewModel: WeatherViewModel){
    var city by remember{
        mutableStateOf("")
    }

    val weatherResult=viewModel.weatherResult.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city ,
                onValueChange={
                    city=it
                              },
                label= {
                    Text(text="Search for any location")
                }
                )
            IconButton(onClick = {
                viewModel.getData(city)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search for any location")
            }
        }
    }
    when (val result = weatherResult.value){
        is NetworkResponse.Error -> {
            Text(text = result.message)
        }
        NetworkResponse.Loading -> {
            CircularProgressIndicator()
        }
        is NetworkResponse.Success<*> -> {
            WeatherDetails(data = result.data as WeatherModel)
        }
        null -> { }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel){

Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
){
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ){  Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription="Location Icon",
        modifier = Modifier.size(40.dp)
    )
        Text(
            text = data.location.name,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.location.country,
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "${data.current.temp_c} °C",
        fontSize = 56.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    AsyncImage(
            modifier = Modifier.size(1600.dp),
            model = "https:${data.current.condition.icon}",
            contentDescription = "Condition image"

        )
}
}