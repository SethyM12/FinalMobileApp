# Photo Journal App

A beautiful, feature-rich photo journal application built with Kotlin and Jetpack Compose for Android.

## Features

### Core Features
- **Three Main Screens**: Home (entry list), Detail (full entry view), Edit (create/edit)
- **Clear Navigation**: Seamless navigation between screens with state management
- **Meaningful User Input**: Create entries with title, description, mood selector, and photos
- **Display User Data**: Show journal entries in grid layout with thumbnails and details
- **Basic App Logic**: Full CRUD operations for journal entries
- **Validation & Error Handling**: Input validation, permission handling, user-friendly error messages

### Advanced Features (45+ Points)

#### 1. Local Persistent Storage (10 pts)
- Room database with SQLite backend
- Automatic persistence of all journal entries
- Data survives app restarts

#### 2. Camera/Photo Use (10 pts)
- Photo picker from device gallery
- Image loading and display using Coil
- Photos stored in app-specific storage

#### 3. Full CRUD (10 pts)
- **Create**: Add new entries with all details
- **Read**: View all entries and individual details
- **Update**: Edit existing entries
- **Delete**: Remove entries with confirmation

#### 4. Search & Filter (5 pts)
- Real-time search by title and description
- Filter entries by mood (Happy, Sad, Excited, Thoughtful)
- Combined search + filter in single operation

#### 5. Strong Visual Design (5 pts)
- Material 3 Design System
- Consistent theming throughout app
- Responsive layouts
- Mood emoji indicators
- Professional typography and colors

#### 6. Input Validation & Error Handling (5 pts)
- Title field validation (required)
- Permission handling for storage/camera
- Graceful error messages
- Error state display in UI

## Architecture

### Tech Stack
- **UI Framework**: Jetpack Compose with Material 3
- **Database**: Room (SQLite)
- **State Management**: ViewModel + StateFlow
- **Image Loading**: Coil
- **Kotlin Coroutines**: For async operations

### Project Structure
```
com.example.finalproject/
├── data/
│   ├── model/
│   │   └── JournalEntry.kt          # Data class for entries
│   ├── database/
│   │   ├── JournalDatabase.kt       # Room database setup
│   │   └── JournalEntryDao.kt       # Database access object
│   └── repository/
│       └── JournalRepository.kt     # Data abstraction layer
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt            # Entry list + search/filter
│   │   ├── DetailScreen.kt          # Entry details view
│   │   └── EditScreen.kt            # Create/edit form
│   ├── components/
│   │   └── JournalEntryCard.kt      # Entry list item
│   ├── viewmodel/
│   │   └── JournalViewModel.kt      # State + business logic
│   └── theme/                       # Material 3 theming
├── navigation/
│   └── Screen.kt                    # Navigation routes
└── MainActivity.kt                  # App entry point
```

## Data Model

### JournalEntry
```kotlin
@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val mood: String = "Neutral",  // Happy, Sad, Excited, Thoughtful, Neutral
    val date: Long = System.currentTimeMillis(),
    val photoUri: String = ""
)
```

## Database Queries

The app supports advanced queries:
- `getAllEntries()` - Get all entries sorted by date
- `searchEntries(query)` - Search by title or description
- `getEntriesByMood(mood)` - Filter by mood
- `getEntriesByDateRange(start, end)` - Filter by date range
- Full CRUD operations

## Building & Running

### Prerequisites
- Android Studio Koala or later
- Android SDK 24+ (API 24+)
- Gradle 8.x

### Build
```bash
./gradlew build
```

### Run on Emulator/Device
```bash
./gradlew installDebug
```

## Permissions

The app requires the following permissions:
- `android.permission.CAMERA` - For photo capture
- `android.permission.READ_EXTERNAL_STORAGE` - For gallery access
- `android.permission.READ_MEDIA_IMAGES` - For Android 13+ photo access
- `android.permission.WRITE_EXTERNAL_STORAGE` - For storing photos

All permissions are optional and the app gracefully handles permission denials.

## Future Enhancements

- Cloud synchronization with Firebase
- User authentication and accounts
- Local notifications/reminders
- More sophisticated filtering (date ranges, multiple moods)
- Export entries as PDF
- Voice-to-text transcription
- Collaborative journals
- Privacy/encryption for sensitive entries

## Project Requirements Met

✅ At least 3 screens (Home, Detail, Edit)
✅ Clear navigation between screens
✅ Meaningful user input (title, description, mood, photo)
✅ Data display (entry list, entry details)
✅ Basic app logic (CRUD operations)
✅ Validation and error handling
✅ 45+ advanced feature points:
  - Local storage (10)
  - Photo picker/camera (10)
  - Full CRUD (10)
  - Search/filter (5)
  - Visual design (5)
  - Validation/error handling (5)

## License

This is a final project for mobile app development course.

