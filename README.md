# DoorDashSample
Take Home exercise for DoorDash

# Architecture 
# Model-View-ViewModel + Repository Pattern + Reactive Observer Pattern

- View-ViewModel interaction is done through bidirectional reactive streams.
- User events are sent from View to ViewModel through Observable streams using RxRelay.
- States(Error/Loading/Success) are sent from ViewModel to View through Livedata.

- View-ViewModel contract is enforced using sealed classes. 
- ViewModel interacts with Repository implementation though repository interfaces. 
- This makes it easy to switch Repository implementations(api or loal db) without affecting rest of the app.

# Libraries 
- Android Architecture Components (ViewModel and LiveData)
- RxJava
- RxRelay
- Retrofit/Okio
- Dagger - Dependency Injection
- Mockito - Unit Testing
- Gson - JSON Serializer
- Timber- Logging




