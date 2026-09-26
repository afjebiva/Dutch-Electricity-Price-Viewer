package com.example.dutchelectricity.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dutchelectricity.R
import com.example.dutchelectricity.data.QuarterHourPrice
import com.example.dutchelectricity.ui.viewmodel.PriceViewModel

@Composable
fun PriceScreen(viewModel: PriceViewModel) {
    val prices by viewModel.prices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val cheapestCount by viewModel.cheapestCount.collectAsState()
    val expensiveCount by viewModel.expensiveCount.collectAsState()
    val hoursAhead by viewModel.hoursAhead.collectAsState()

    var cheapestInput by remember { mutableStateOf(cheapestCount.toString()) }
    var expensiveInput by remember { mutableStateOf(expensiveCount.toString()) }
    var hoursInput by remember { mutableStateOf(hoursAhead.toString()) }

    LaunchedEffect(Unit) {
        viewModel.fetchPrices()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = stringResource(id = R.string.title_prices),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.purple_500),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cheapestInput,
                    onValueChange = {
                        cheapestInput = it
                        it.toIntOrNull()?.let { count ->
                            if (count >= 0) viewModel.updateCheapestCount(count)
                        }
                    },
                    label = { Text("Cheapest") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = expensiveInput,
                    onValueChange = {
                        expensiveInput = it
                        it.toIntOrNull()?.let { count ->
                            if (count >= 0) viewModel.updateExpensiveCount(count)
                        }
                    },
                    label = { Text("Expensive") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    singleLine = true
                )
            }
            OutlinedTextField(
                value = hoursInput,
                onValueChange = {
                    hoursInput = it
                    it.toIntOrNull()?.let { hours ->
                        if (hours in 1..36) viewModel.updateHoursAhead(hours)
                    }
                },
                label = { Text("Hours Ahead (1-36)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true
            )
        }

        // Refresh Button
        Button(
            onClick = { viewModel.fetchPrices() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(id = R.string.button_update))
        }

        // Error Message
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 12.sp
            )
        }

        // Loading Indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (prices.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.no_data))
            }
        } else {
            // Price List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Time",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1.5f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Price",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Market",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Status",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            fontSize = 10.sp
                        )
                    }
                }
                items(prices) { priceData ->
                    PriceRow(priceData)
                }
            }
        }
    }
}

@Composable
fun PriceRow(priceData: QuarterHourPrice) {
    val backgroundColor = when (priceData.status) {
        "cheap" -> colorResource(id = R.color.green_cheap)
        "expensive" -> colorResource(id = R.color.orange_expensive)
        else -> colorResource(id = R.color.gray_neutral)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = priceData.getTimeRange(),
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
        Text(
            text = "€%.3f".format(priceData.price),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
        Text(
            text = "€%.3f".format(priceData.marketPrice),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp
        )
        Text(
            text = priceData.status.uppercase(),
            modifier = Modifier.weight(1f),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
