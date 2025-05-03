# 🚗 Google Maps Route Viewer – Android (Jetpack Compose + Ktor + Directions API)

This project is an Android application that allows users to view routes between two destinations
using Google Maps. It utilizes Jetpack Compose for UI, Ktor for networking, and the Google
Directions API to fetch real-time routing information. The app showcases how to draw a polyline on a
map based on user-selected locations.

---

## ✨ Features

- 📍 Display start and end points on Google Maps.
- 🧭 Fetch and render driving routes using the Directions API.
- 🧰 Built with Jetpack Compose and Google Maps Compose.
- 🌐 Network calls via Ktor (with Kotlinx Serialization).
- 🔄 Handles API states (Loading, Success, Error).
- 🎥 Demo included (see below).

---

## 📹 Demo

https://github.com/user-attachments/assets/17f3629b-0144-4833-bab1-1562e911f84e
*This video demonstrates route drawing between two destinations on the emulator.*

---

## 🧱 Tech Stack

| Tech                      | Purpose              |
|---------------------------|----------------------|
| **Jetpack Compose**       | UI framework         |
| **Google Maps Compose**   | Map rendering        |
| **Google Directions API** | Route data           |
| **Ktor Client**           | Network calls        |
| **Kotlinx Serialization** | JSON parsing         |
| **Kotlin Flows**          | State management     |
| **Koin**                  | Dependency Injection |

---

## 🛠️ Setup Instructions

1. **Clone the repo**
   ```bash
   git clone https://github.com/Dev-Husnain/LiveTracker

2. Add your Google Maps API key: In local.properties or directly in code if not using secrets
   gradle.
3. Enable APIs on Google Cloud:
   ✅ Directions API
   ✅ Maps SDK for Android

