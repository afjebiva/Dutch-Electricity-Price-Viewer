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
import com.example.dutchelectricity.data.PriceData
import com.example.dutchelectricity.ui.viewmodel.PriceViewModel

@Composable
fun PriceScreen(viewModel: PriceViewModel) {
    val prices by viewModel.prices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val cheapestCount by viewModel.cheapestCount.collectAsState()
    val expensiveCount by viewModel.expensiveCount.collectAsState()

    var cheapestInput by remember { mutableStateOf(cheapestCount.toString()) }
    var expensiveInput by remember { mutableStateOf(expensiveCount.toString()) }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
                label = { Text(stringResource(id = R.string.hint_cheapest)) },
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
                label = { Text(stringResource(id = R.string.hint_expensive)) },
                modifier = Modifier
                    .weight(1f)
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
                modifier = Modifier.padding(bottom = 8.dp)
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.time_label),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(id = R.string.price_label),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(id = R.string.status_label),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
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
fun PriceRow(priceData: PriceData) {
    val backgroundColor = when (priceData.status) {
        "cheap" -> colorResource(id = R.color.green_cheap)
        "expensive" -> colorResource(id = R.color.orange_expensive)
        else -> colorResource(id = R.color.gray_neutral)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = priceData.timestamp,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "€ %.2f".format(priceData.price),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = priceData.status.uppercase(),
            modifier = Modifier.weight(1f),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
