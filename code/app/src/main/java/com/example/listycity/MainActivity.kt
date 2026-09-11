package com.example.listycity
import androidx.compose.foundation.clickable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button


import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                       cities=cityRepository.cities,
                        onAddCity = {cityRepository.addCity(it)},
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}


class  CityRepository{
        private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver","Moscow",
        "Sydney","Berlin","Vienna",
        "Tokyo","Beijing","Osaka",
        "New Delhi"
    )
    val cities: List<String> get() = _cities
    fun addCity(city: String){
        _cities.add(city)
    }
    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var addingCity by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Button(
                onClick = { addingCity = true
                    selectedCity = null },
                modifier = Modifier.weight(1f)
            ) { Text("Add City") }
            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (selectedCity != null) {
                        onDeleteCity(selectedCity!!)
                        selectedCity = null
                    }
                },
                enabled = selectedCity != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Delete City")
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cities) { city -> CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = {
                        selectedCity =
                            if (selectedCity == city) null else city
                        addingCity = false
                    }
                )
            }
        }
        if (addingCity) { Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City name") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName.trim())
                            newCityName = ""
                            addingCity = false
                        }
                    }
                ) {
                    Text("CONFIRM")
                }
            }
        }
    }
}

@Composable
fun CityRow(
    city: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = if (isSelected) "✓ $city" else city,
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}
