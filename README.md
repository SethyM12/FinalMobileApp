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

### Advanced Features (40 points)

#### Local Persistent Storage (10 pts)
- Room database with SQLite backend
- Automatic persistence of all journal entries
- Data survives app restarts

#### Camera/Photo Use (10 pts)
- Photo picker from device gallery
- Image loading and display using Coil
- Photos stored in app-specific storage

#### Full CRUD (10 pts)
- **Create**: Add new entries with all details
- **Read**: View all entries and individual details
- **Update**: Edit existing entries
- **Delete**: Remove entries with confirmation

#### Search & Filter (5 pts)
- Real-time search by title and description
- Filter entries by mood (Happy, Sad, Excited, Thoughtful)
- Combined search + filter in single operation

#### Input Validation & Error Handling (5 pts)
- Title field validation (required)
- Permission handling for storage/camera
- Graceful error messages
- Error state display in UI
