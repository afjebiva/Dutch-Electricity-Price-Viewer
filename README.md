# Dutch Electricity Price Viewer

An Android app that retrieves Dutch dynamic quarter-hourly electricity prices and highlights the cheapest and most expensive time slots.

## Features

- **Real-time Price Data**: Fetches current Dutch electricity prices for every 15-minute interval
- **Color-Coded Highlighting**: 
  - 🟢 **Green**: Highlights a configurable number of the cheapest 15-minute slots
  - 🟠 **Orange**: Highlights a configurable number of the most expensive 15-minute slots
- **Adjustable Highlighting**: Users can set how many cheapest and most expensive slots to highlight
- **Manual Refresh**: Button to fetch the latest prices on demand
- **Error Handling**: Graceful error messages for network issues

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture:

- **Repository Layer**: Handles data fetching and price highlighting logic
- **ViewModel Layer**: Manages UI state and user interactions
- **Compose UI**: Modern, declarative UI using Jetpack Compose
- **Retrofit**: HTTP client for API communication

## API Integration

The app uses the Dutch Electricity Prices API endpoint:
```
https://api.electricityprices.nl/v1/electricity
```

## Requirements

- Android 8.0 (API 26) or higher
- Internet permission for API calls

## Building and Running

1. Clone the repository
2. Open the project in Android Studio
3. Build and run on an emulator or physical device

## Technologies Used

- **Kotlin**: Primary programming language
- **Jetpack Compose**: UI framework
- **Retrofit**: REST client
- **Gson**: JSON serialization
- **Coroutines**: Asynchronous programming
- **StateFlow**: Reactive state management

## Future Enhancements

- Historical price data and charts
- Price trend predictions
- Notifications for price drops
- Dark mode support
- Offline caching
