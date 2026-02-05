# 📱 AnimePedia – Android Application

AnimePedia is a modern Android application that displays top-rated anime using the **Jikan API (MyAnimeList unofficial API)**.  
The app is built with **Clean Architecture**, supports **offline-first behavior**, local caching, pagination, search, and rich UI using **Jetpack Compose**.

---

## 🧰 Tech Stack

### Core
- Kotlin
- Android SDK 24+
- Jetpack Compose (Material 3)

### Architecture & Patterns
- Clean Architecture (Data / Domain / Presentation)
- MVVM
- Repository Pattern
- UseCase pattern
- Offline-first strategy

### Data & Networking
- Retrofit + OkHttp
- Gson
- Room Database
- In-memory cache (HashMap)

### Async & State
- Kotlin Coroutines
- StateFlow / SharedFlow

### Dependency Injection
- Hilt

### Media & UI
- Coil (image loading)
- WebView (YouTube embed)
- Material 3 theming

### Logging & Analytics
- AppLogger (errors + info)
- Generic EventLogger (user events)

---

## 🏗 High Level Design (HLD)

```
┌────────────────────┐
│   Presentation     │
│  (Activity + UI)   │
└─────────▲──────────┘
          │
┌─────────┴──────────┐
│     ViewModel      │
│  State + Events    │
└─────────▲──────────┘
          │
┌─────────┴──────────┐
│     UseCases       │
│ Business Rules     │
└─────────▲──────────┘
          │
┌─────────┴──────────┐
│    Repository      │
│ Online / Offline   │
└─────────▲──────────┘
          │
┌─────────┴──────────┐
│ API   |   Room DB  │
└────────────────────┘
```

---

## 🧱 Low Level Design (LLD)

### Data Flow – Anime Listing

1. Activity checks internet availability
2. Activity triggers `ViewModel.loadNextPage()`
3. ViewModel calls `LoadAnimePageUseCase`
4. UseCase delegates to `AnimeRepository`
5. Repository:
   - If offline → fetch from Room
   - If online → fetch from API → save to Room → return
6. ViewModel updates StateFlow
7. Compose UI re-renders automatically

---

## 📂 Project Structure

```
com.example.animepedia
│
├── analytics
│   ├── logger
│   │   └── AppLogger
│   └── eventLogger
│       ├── EventLogger
│       └── EventConstants
│
├── data
│   ├── retrofit
│   ├── room
│   │   ├── dao
│   │   └── entity
│   ├── mapper
│   ├── cache
│   └── repository
│
├── domain
│   ├── model
│   ├── repository
│   ├── usecase
│   └── common
│
├── presentation
│   ├── animeListing
│   ├── animeDetail
│   └── util
│
└── AnimePediaApplication
```

---

## 🗄 Database Design (Room)

### `anime` table
| Column | Type |
|------|-----|
| id | Int (PK) |
| title | String |
| imageUrl | String |
| episodes | Int? |
| rating | Double? |

### `anime_detail` table
| Column | Type |
|------|-----|
| id | Int (PK) |
| title | String |
| titleEnglish | String? |
| synopsis | String? |
| imageUrl | String |
| episodes | Int? |
| rating | Double? |
| genres | String (CSV) |
| studios | String (CSV) |
| trailerUrl | String? |

---

## 📶 Offline-First Strategy

### Anime List
- If **offline** → fetch paginated data from Room
- If **online** → fetch from API → save to DB → return
- Pagination works in both modes

### Anime Detail
- In-memory cache tracks which IDs exist locally
- If cached → DB only
- If not cached & offline → error
- If online → API → save → update cache → return

---

## 🧠 In-Memory Cache

```
AnimeDetailCache : Map<Int, Boolean>
```

- Initialized on app startup
- Avoids unnecessary DB queries
- Lives in Application scope
- Used only inside Repository

---

## 🔍 Search Behavior

- Search happens **only on locally loaded list**
- No API calls during search
- Pagination is paused while searching
- “Load more” button appears during search
- Shows: `Showing X of Y animes`

---

## 🧩 Pagination Logic

- Page-based pagination (not Paging 3)
- Auto-load when near bottom
- Manual load during search
- Handles offline pagination gracefully

---

## 🎨 UI Architecture

- Activity owns ViewModel
- Compose receives state and callbacks
- No ViewModel inside composables
- Stateless UI components
- Material 3 styling
- Rounded images with placeholders

---

## 🎥 Video Playback

- Uses WebView with embedded YouTube player
- HTML injected dynamically
- Full width & height
- Falls back to image if trailer unavailable

---

## 📝 Logging

### AppLogger
Used for:
- Errors
- Exceptions
- Informational logs

### EventLogger
Generic event tracking with event name and parameters

---

## 🔑 Major Classes & Responsibilities

### AnimeRepositoryImpl
- Single source of truth
- Handles online/offline logic
- Manages cache
- Converts DTO → Entity → Domain

### ViewModels
- Hold UI state
- Trigger use cases
- Emit loading & toast events

### UseCases
- Encapsulate business logic
- Keep ViewModel thin

### DAOs
- Pure DB operations
- No business logic

---

## 🚀 App Startup Flow

1. Application initializes
2. AnimeDetailCache populated from DB
3. Logger & EventLogger initialized
4. User lands on Anime Listing screen

---

## ✅ Key Design Decisions

- Offline-first without redundant DB fetch
- Stateless Compose UI
- Explicit pagination control
- Clear separation of concerns

---

## 📌 Future Improvements
- Sorting & filtering
- Download trailers
---

## 👨‍💻 Author

**Gurmeet Singh**  
Android Developer

