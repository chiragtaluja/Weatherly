package com.example.weatherly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.api.Constant
import com.example.weatherly.api.NetworkResponse
import com.example.weatherly.api.RetrofitObject
import com.example.weatherly.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val weatherAPI= RetrofitObject.weatherAPI
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult:LiveData<NetworkResponse<WeatherModel>> =_weatherResult

    fun getData(city:String){
        _weatherResult.value= NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherAPI.getWeather(Constant.apiKey,city)
                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value= NetworkResponse.Success(it)
                    }
                }
                else{
                    _weatherResult.value= NetworkResponse.Error("Failed to load data")
                }
            }
            catch (
                e: Exception
            ){
                _weatherResult.value= NetworkResponse.Error("Failed to load data")
            }

        }

    }
}