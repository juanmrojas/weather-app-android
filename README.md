It's a weather mobile app with solid foundation in clean architecture and modern android development. 

Contains the following tech stack:
- Kotlin
- Coroutines - Flows
- Hilt
- Jetpack Compose

It is a multi-modular app based on Clean Architecture which comprises modules such as abstract, app, features, core, network and data. 
I wanted to come up with a simple approach that could suit up ok for this use case / requirement without walking in to over complexity 
and designed a solution based out of UseCase(s) and DataStore (mainly focusing on them - Domain / Data layers) to address such kind of
scenarios like the one for when 1) the user queries for weather info for a particular address given in a human-readable format, also
addressed the use case to displaying the last weather info / city we queried for once the user opens the screen. 


<img width="377" alt="Screenshot 2024-01-26 at 9 33 08 AM" src="https://github.com/juanmrojas/weather-app-android/assets/10778929/a06b6100-b34d-48e8-9c89-557316f28632">
<img width="376" alt="Screenshot 2024-01-26 at 9 31 58 AM" src="https://github.com/juanmrojas/weather-app-android/assets/10778929/3ddbb2a3-5e67-454b-afaa-e0177a99193b">
