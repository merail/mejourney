# Meshcherov Rail's Travel App

Mejourney is your guide to a world of captivating travel! Step into my adventures through the lens of this travel blog. 
Immerse yourself in unforgettable moments and the beauty of diverse corners of the globe, brought to life with vibrant photographs and compelling stories.

The Mejourney app invites you to dive into new cultures, emotions, and discoveries. Explore remarkable places, get inspired, and dream about your next adventure with Mejourney!

<img src="https://github.com/user-attachments/assets/67059272-4f4e-46c4-a9e0-07cc9fce393e" width="144" /> <img src="https://github.com/user-attachments/assets/a72c8f9a-fba1-418d-a282-cfed9d9e2a26" width="144" /> <img src="https://github.com/user-attachments/assets/a7f6d61e-1a1e-4bb0-b0b5-341c85b03fc3" width="144" /> <img src="https://github.com/user-attachments/assets/7b776dc4-180f-4dd2-9613-b95619b07ecc" width="144" /> <img src="https://github.com/user-attachments/assets/ca29c15c-55e2-47c6-8446-c390db572aed" width="144" />

## Tech Stack

Mejourney is built with a modern Android development toolkit focused on performance, reliability, and modularity.  
Below is the complete technology stack, sorted by decreasing importance:

### 1. Kotlin  
Primary language used across the entire codebase for expressive, safe, and maintainable development.

### 2. Jetpack Compose  
Modern declarative UI toolkit powering all interface components and animations.

### 3. Custom Backend (Ktor)
Mejourney uses a **custom backend built with Ktor**.

The backend is responsible for:
- Providing travel posts and metadata
- Serving media content (covers, images)
- Centralized API logic and access control

The server is fully open-source and available here:  
👉 **https://github.com/merail/mejourney-server**

### 4. Firebase Services  
A robust backend foundation leveraging multiple Firebase products:
- Firebase Messaging – push notifications  
- Firebase Crashlytics – real-time crash reporting  
- Firebase Remote Config – remote feature configuration  
- Firebase Auth – secure authentication

### 5. Hilt  
Dependency injection framework simplifying component wiring and improving testability.

### 6. Jetpack Navigation (2.8+)  
Modern navigation framework enabling type-safe routes and structured app flows.

### 7. Room  
Local persistence layer providing a type-safe abstraction over SQLite for caching and offline-first functionality.

### 8. Baseline Profiles  
Optimizes app startup and runtime performance on Android devices.

### 9. Unit & Instrumentation Tests  
- MockK for unit-test mocking  
- Full instrumentation test suite to validate UI and runtime behavior

### 10. Coil  
Lightweight, modern image loading library optimized for Compose.

### 11. Gradle Convention Plugins  
Custom Gradle plugins centralizing build logic for cleaner, maintainable module configuration.

### 12. SplashScreen API  
Native Android API providing a smooth, consistent launch experience across devices.

---

## Architecture Overview

- **Android App**
  - Clean, modular architecture
  - Offline-first approach using Room
  - Declarative UI with Compose

- **Backend**
  - Ktor-based REST API
  - Self-hosted
  - Public GitHub repository

---

## Links

- 📱 **Android App:** https://www.rustore.ru/catalog/app/merail.life.mejourney
- 🖥️ **Backend (Ktor):** https://github.com/merail/mejourney-server
