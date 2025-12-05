# 📚 COMPLETE DOCUMENTATION SUITE (Continued)

---

## 7️⃣ DEPLOYMENT DOCUMENTATION

Path: `docs/07_DEPLOYMENT_DOCUMENTATION.md`

```markdown
# LearnHub Kenya - Deployment Documentation

## Table of Contents
1. [Deployment Overview](#deployment-overview)
2. [Environment Setup](#environment-setup)
3. [Build Configuration](#build-configuration)
4. [Release Preparation](#release-preparation)
5. [Google Play Store Deployment](#google-play-store-deployment)
6. [Backend Deployment](#backend-deployment)
7. [CI/CD Pipeline](#cicd-pipeline)
8. [Monitoring & Maintenance](#monitoring--maintenance)
9. [Rollback Procedures](#rollback-procedures)
10. [Post-Deployment Checklist](#post-deployment-checklist)

---

## Deployment Overview

### Deployment Strategy

LearnHub Kenya follows a **phased deployment approach**:

```
Development → Staging → Beta Testing → Production
↓           ↓            ↓            ↓
Internal   QA Team    Selected     All Users
Testing              Users
```

### Environments

| Environment | Purpose | Audience | Backend |
|------------|---------|----------|---------|
| **Development** | Active development | Developers | Mock/Local |
| **Staging** | Pre-production testing | QA Team | Staging API |
| **Beta** | User acceptance testing | Beta testers | Production API |
| **Production** | Live application | All users | Production API |

### Version Naming Convention

**Semantic Versioning**: `MAJOR.MINOR.PATCH`

- **MAJOR**: Breaking changes, major features
- **MINOR**: New features, backwards compatible
- **PATCH**: Bug fixes, minor improvements

**Examples:**
- `1.0.0` - Initial production release
- `1.1.0` - New features added
- `1.1.1` - Bug fixes
- `2.0.0` - Major redesign

**Build Numbers:**
- Incremental integer
- Never reused
- Format: `versionCode = 1, 2, 3...`

### Release Cycle

**Regular Releases:**
- **Major**: Every 6-12 months
- **Minor**: Every 1-2 months
- **Patch**: As needed (hot fixes)

**Emergency Releases:**
- Critical bugs
- Security vulnerabilities
- Data corruption issues
- < 24 hours turnaround

---

## Environment Setup

### Development Environment

#### Prerequisites

```bash
# Required Software
- Android Studio Hedgehog (2023.1.1) or later
- JDK 21
- Git 2.40+
- Gradle 8.7+

# Recommended
- 16 GB RAM minimum
- 50 GB free disk space
- Multi-core processor
```

#### Setup Steps

1. **Clone Repository**

```bash
git clone https://github.com/learnhub-kenya/android-app.git
cd android-app
```

2. **Create Local Properties**

Create `local.properties`:

```properties
sdk.dir=/path/to/Android/sdk
```

3. **Configure Signing (Debug)**

Create `keystore.properties`:

```properties
debugKeyStore=/path/to/debug.keystore
debugKeyAlias=androiddebugkey
debugKeyPassword=android
debugStorePassword=android
```

4. **Build Project**

```bash
./gradlew clean assembleDebug
```

5. **Run on Emulator**

```bash
./gradlew installDebug
```

#### Development Build Variants

```kotlin
android {
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
}
```

---

### Staging Environment

#### Purpose
- Integration testing
- QA validation
- Performance testing
- API integration verification

#### Configuration

**File**: `app/build.gradle.kts`

```kotlin
android {
    buildTypes {
        create("staging") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-STAGING"
            isMinifyEnabled = true
            isDebuggable = false
            
            buildConfigField("String", "API_BASE_URL", 
                "\"https://staging-api.learnhub.ke/v1/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }
    }
}
```

**Backend Configuration:**
- API: `https://staging-api.learnhub.ke`
- Database: Staging PostgreSQL instance
- CDN: Staging content delivery
- Analytics: Staging Firebase project

#### Building Staging APK

```bash
./gradlew assembleStaging
```

Output: `app/build/outputs/apk/staging/app-staging.apk`

---

### Production Environment

#### Configuration

**File**: `app/build.gradle.kts`

```kotlin
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            buildConfigField("String", "API_BASE_URL", 
                "\"https://api.learnhub.ke/v1/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

**Backend Configuration:**
- API: `https://api.learnhub.ke`
- Database: Production PostgreSQL (with replicas)
- CDN: CloudFront distribution
- Analytics: Production Firebase project
- Monitoring: Sentry, Crashlytics

---

## Build Configuration

### Gradle Configuration

**File**: `app/build.gradle.kts`

```kotlin
android {
    namespace = "com.learnhub.kenya"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.learnhub.kenya"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    
    buildTypes {
        debug {
            // Debug config
        }
        
        create("staging") {
            // Staging config
        }
        
        release {
            // Production config
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    
    kotlinOptions {
        jvmTarget = "21"
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
```

### ProGuard Configuration

**File**: `app/proguard-rules.pro`

```proguard
# LearnHub Kenya - ProGuard Rules

# Keep application class
-keep public class com.learnhub.kenya.LearnHubApplication

# Keep domain models (used with Gson/Retrofit)
-keep class com.learnhub.kenya.domain.model.** { *; }
-keep class com.learnhub.kenya.data.remote.dto.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keep,includedescriptorclasses class com.learnhub.kenya.**$$serializer { *; }
-keepclassmembers class com.learnhub.kenya.** {
    *** Companion;
}
-keepclasseswithmembers class com.learnhub.kenya.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep line numbers for stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Optimization
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
```

### Signing Configuration

**Release Signing Setup:**

1. **Generate Release Keystore**

```bash
keytool -genkey -v \
  -keystore learnhub-release.keystore \
  -alias learnhub-release \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**⚠️ CRITICAL:** Store keystore securely! Losing it means you can't update your app.

2. **Create Signing Configuration File**

Create `keystore.properties` (NOT in version control):

```properties
releaseKeyStore=/path/to/learnhub-release.keystore
releaseKeyAlias=learnhub-release
releaseKeyPassword=YOUR_KEY_PASSWORD
releaseStorePassword=YOUR_STORE_PASSWORD
```

3. **Add to `.gitignore`**

```gitignore
# Signing files
keystore.properties
*.keystore
*.jks
```

4. **Configure in Gradle**

**File**: `app/build.gradle.kts`

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["releaseKeyStore"] as String)
            storePassword = keystoreProperties["releaseStorePassword"] as String
            keyAlias = keystoreProperties["releaseKeyAlias"] as String
            keyPassword = keystoreProperties["releaseKeyPassword"] as String
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ... other config
        }
    }
}
```

### Build Variants Summary

```bash
# Debug build (development)
./gradlew assembleDebug

# Staging build (QA)
./gradlew assembleStaging

# Release build (production)
./gradlew assembleRelease

# Generate AAB (Play Store)
./gradlew bundleRelease
```

---

## Release Preparation

### Pre-Release Checklist

#### 1. Code Quality

```bash
# Run lint checks
./gradlew lint

# Check for errors
# Review: app/build/reports/lint-results.html

# Run all tests
./gradlew test
./gradlew connectedAndroidTest

# Check code coverage
./gradlew jacocoTestReport
```

#### 2. Version Bump

Update version in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2  // Increment by 1
    versionName = "1.0.1"  // Follow semver
}
```

**Version History:**
```
v1.0.0 (Build 1) - Initial release
v1.0.1 (Build 2) - Bug fixes
v1.1.0 (Build 3) - New features
```

#### 3. Update Changelog

**File**: `CHANGELOG.md`

```markdown
# Changelog

## [1.0.1] - 2024-12-15

### Fixed
- Fixed login crash on Android 8
- Resolved video playback issue
- Corrected progress sync bug

### Changed
- Improved loading performance
- Updated UI animations

### Security
- Updated dependencies with security patches
```

#### 4. Update Release Notes

**File**: `fastlane/metadata/android/en-US/changelogs/2.txt`

```
Bug Fixes:
• Fixed login issue on older devices
• Resolved video playback problems
• Fixed progress syncing

Improvements:
• Faster content loading
• Smoother animations
• Better error messages
```

#### 5. Asset Preparation

**Screenshots:**
- 2-8 screenshots per device type
- Phone: 1080x1920 or higher
- 7-inch tablet: 1536x2048 or higher
- 10-inch tablet: 2048x1536 or higher

**Feature Graphic:**
- 1024x500 pixels
- JPG or 24-bit PNG
- No transparency

**App Icon:**
- 512x512 pixels
- 32-bit PNG with alpha
- Full bleed (no padding)

#### 6. Localization Check

```bash
# Verify all strings are translated
# Check: app/src/main/res/values-*/strings.xml

# English (default)
app/src/main/res/values/strings.xml

# Swahili (if available)
app/src/main/res/values-sw/strings.xml
```

#### 7. Legal & Compliance

- [ ] Privacy Policy updated
- [ ] Terms of Service reviewed
- [ ] COPPA compliance checked (users under 13)
- [ ] GDPR compliance verified
- [ ] Data retention policy documented

#### 8. Final Build

```bash
# Clean build
./gradlew clean

# Generate release AAB
./gradlew bundleRelease

# Verify output
ls -lh app/build/outputs/bundle/release/
```

**Output:** `app-release.aab` (~15-25 MB)

#### 9. Test Release Build

```bash
# Install on test device
adb install app/build/outputs/apk/release/app-release.apk

# Manual testing checklist:
- [ ] Login/Register flow
- [ ] Content browsing
- [ ] Video playback
- [ ] Quiz functionality
- [ ] Progress tracking
- [ ] Offline mode
- [ ] Dark mode
- [ ] Search
- [ ] Settings
```

---

## Google Play Store Deployment

### Initial Setup (First Release)

#### 1. Create Developer Account

1. Go to [Google Play Console](https://play.google.com/console)
2. Pay one-time $25 registration fee
3. Complete developer profile
4. Accept developer agreement

#### 2. Create App

1. In Play Console, click "Create app"
2. Fill in details:
    - **App name**: LearnHub Kenya
    - **Default language**: English (UK)
    - **App or game**: App
    - **Free or paid**: Free (for now)

3. Complete declarations:
    - [ ] Developer Program Policies
    - [ ] US export laws

#### 3. Store Listing

**App Details:**

```
App name: LearnHub Kenya
Short description (80 chars max):
"Master Form 3 & 4 Math, Chemistry, Biology, Physics. Track progress, take quizzes!"

Full description (4000 chars max):
[See template below]

Category: Education
Tags: learning, education, KCSE, Kenya, students

Contact details:
- Email: support@learnhub.ke
- Website: https://www.learnhub.ke
- Phone: +254 XXX XXX XXX

Privacy Policy URL: https://www.learnhub.ke/privacy
```

**Full Description Template:**

```
🎓 LearnHub Kenya - Your KCSE Success Partner

Master Form 3 & Form 4 content with LearnHub Kenya, the comprehensive 
learning platform designed specifically for Kenyan students preparing 
for KCSE examinations.

📚 COMPLETE CURRICULUM COVERAGE
• Mathematics - Algebra, Geometry, Trigonometry, Calculus
• Chemistry - Organic, Inorganic, Physical Chemistry
• Biology - Cell Biology, Genetics, Ecology, Human Biology
• Physics - Mechanics, Electricity, Waves, Modern Physics

✨ KEY FEATURES
✓ Interactive Learning Content - Text, images, videos, PDFs
✓ Progress Tracking - Monitor your learning journey
✓ Quiz System - Test your knowledge, prepare for exams
✓ Offline Learning - Study anywhere, even without internet
✓ Teacher Tools - Lesson planning, question banks, exam generation
✓ Dark Mode - Study comfortably day or night
✓ Achievements - Earn badges, compete on leaderboards

🎯 WHY CHOOSE LEARNHUB KENYA?
→ Aligned with KICD curriculum (8-4-4 & CBC)
→ Created by educators, for educators
→ Comprehensive content for Form 3 & Form 4
→ Study at your own pace
→ Track your progress
→ Practice with instant feedback

📊 FOR STUDENTS
• Browse structured content by class, subject, and topic
• Learn with multimedia content (text, images, videos)
• Test your knowledge with interactive quizzes
• Track your completion and progress
• Compete with peers on leaderboards
• Earn achievements and badges

👨‍🏫 FOR TEACHERS
• Create detailed lesson plans
• Build comprehensive question banks
• Generate exams automatically
• Manage multiple classes
• Track student progress

🌟 PERFECT FOR:
• Form 3 & Form 4 students
• KCSE exam preparation
• Self-paced learners
• Teachers and tutors
• Educational institutions

📱 WORKS OFFLINE
Downloaded content available without internet - perfect for 
areas with limited connectivity!

🔒 SAFE & SECURE
Your data is encrypted and secure. We never share your information 
with third parties.

💰 FREE TO USE
Core features completely free. Premium features coming soon.

📞 NEED HELP?
Email: support@learnhub.ke
Website: https://www.learnhub.ke

Download LearnHub Kenya today and take control of your KCSE success! 🚀

---

Keywords: education, learning, KCSE, Kenya, Form 3, Form 4, mathematics, 
chemistry, biology, physics, exam preparation, students, teachers, quizzes, 
offline learning, progress tracking
```

#### 4. Upload Assets

**Screenshots (Required: 2-8 per device type):**

```
Phone Screenshots:
1. Home screen showing features
2. Content browsing (subjects/topics)
3. Content viewer with video
4. Quiz interface
5. Progress analytics dashboard
6. Dark mode example
7. Leaderboard screen
8. Teacher dashboard

Tablet Screenshots (Optional but recommended):
1. Home screen (landscape)
2. Content browsing
3. Split-screen view
```

**Feature Graphic (Required):**
- 1024 x 500 pixels
- Showcase app name, key features, visual appeal

**App Icon (Required):**
- 512 x 512 pixels
- Matches in-app icon

**Promotional Video (Optional):**
- YouTube link
- 30 seconds - 2 minutes
- Showcases key features

#### 5. Content Rating

Complete questionnaire:
- Target age groups
- Content types
- Interactive elements
- Data collection practices

**Expected Rating:** PEGI 3 / Everyone

#### 6. App Content

**Privacy Policy** (Required):
- Upload PDF or provide URL
- Must cover: data collection, usage, sharing, retention

**Ads Declaration:**
- Contains ads: No (currently)
- Third-party ads: No

**App Access:**
- Special access needed: Notifications (optional)
- Permissions explanation in listing

**Target Audience:**
- Primary: 16-18 years (Form 3-4 students)
- Secondary: Teachers, 18+

#### 7. Pricing & Distribution

**Pricing:**
- Free app (currently)
- In-app purchases (future)

**Countries:**
- Primarily: Kenya
- Consider: Uganda, Tanzania, Rwanda (East Africa)
- Global (optional)

**Device Categories:**
- Phone (required)
- Tablet (recommended)
- Chromebook (optional)
- Wear OS (not applicable)
- Android TV (not applicable)

---

### Production Release

#### 1. Upload AAB

1. In Play Console → App → Release → Production
2. Click "Create new release"
3. Upload `app-release.aab`
4. Release name: e.g., "1.0.1 (Build 2)"

#### 2. Release Notes

```
What's new in version 1.0.1:

Bug Fixes:
• Fixed login crash on Android 8 devices
• Resolved video playback issues
• Corrected progress synchronization

Improvements:
• Faster content loading
• Smoother animations
• Better error messages
• Improved offline support

We're constantly improving LearnHub Kenya. 
Thank you for your feedback!
```

#### 3. Review Release

Checklist before submitting:
- [ ] Correct version code and name
- [ ] Release notes accurate
- [ ] Screenshots up to date
- [ ] All required fields completed
- [ ] Privacy policy accessible
- [ ] Content rating current

#### 4. Submit for Review

1. Click "Review release"
2. Verify all information
3. Click "Start rollout to Production"
4. Confirm submission

**Review Timeline:**
- Initial review: 3-7 days
- Updates: 1-3 days (typically faster)

#### 5. Monitor Review Status

**Play Console → App → Release → Overview**

**Possible Statuses:**
- 📤 **Pending publication**: Submitted, awaiting review
- 🔍 **In review**: Google reviewing
- ✅ **Publishing**: Approved, rolling out
- 🚀 **Available**: Live on Play Store
- ❌ **Rejected**: Failed review (see feedback)

---

### Staged Rollout (Recommended)

#### What is Staged Rollout?

Gradually release to increasing percentages of users:

```
Day 1:  5% of users
Day 3:  20% of users
Day 5:  50% of users
Day 7:  100% of users (full rollout)
```

#### Configure Staged Rollout

1. During release creation
2. Select "Staged rollout"
3. Choose initial percentage: 5-20%
4. Monitor crash rates and reviews
5. Increase rollout if stable
6. Halt if issues detected

#### Benefits

- ✅ Catch critical bugs early
- ✅ Minimize user impact
- ✅ Gather initial feedback
- ✅ Easy rollback if needed
- ✅ Time to fix issues before full release

---

### Internal Testing Track

**Purpose:** Pre-release testing with team

**Setup:**
1. Play Console → Testing → Internal testing
2. Create email list of testers
3. Upload AAB
4. Share testing link with team

**Testers receive:**
- Early access to new versions
- Ability to provide feedback
- Crash reports to developers

---

### Closed Testing (Beta)

**Purpose:** User acceptance testing with real users

**Setup:**
1. Play Console → Testing → Closed testing
2. Create track (e.g., "Beta")
3. Add testers via email list or Google Group
4. Upload AAB
5. Set release notes

**Distribution:**
- Email invitation to testers
- Testers opt-in via Play Store
- Provide feedback via Play Console

**Beta Testing Checklist:**
- [ ] Recruit 50-100 beta testers
- [ ] Test for 1-2 weeks
- [ ] Gather feedback
- [ ] Fix critical issues
- [ ] Re-test if major changes
- [ ] Promote to production when stable

---

### Post-Release Monitoring

#### 1. Install Metrics

**Play Console → Dashboard**

Monitor:
- Install count (daily, weekly)
- Install conversion rate
- Uninstall rate
- Retention rate (1-day, 7-day, 30-day)

**Healthy Metrics:**
- Retention D1: > 40%
- Retention D7: > 20%
- Retention D30: > 10%
- Uninstall rate: < 5%

#### 2. Crash Reports

**Play Console → Quality → Android vitals → Crashes**

Monitor:
- Crash rate: Target < 1%
- ANR (App Not Responding) rate: Target < 0.5%
- Crash-free users: Target > 99%

**Action Required:**
- Crash rate > 2%: Urgent fix
- Crash rate > 5%: Critical - consider rollback

#### 3. User Reviews

**Play Console → Ratings and reviews**

**Respond to Reviews:**
- Positive reviews: Thank users
- Negative reviews: Acknowledge, offer help
- Bug reports: Confirm fixing in next update
- Response time: < 48 hours

**Review Monitoring:**
- Average rating goal: ≥ 4.0 stars
- Monitor common complaints
- Track sentiment trends
- Identify feature requests

#### 4. Performance Metrics

**Play Console → Quality → Android vitals**

Monitor:
- App startup time
- Battery consumption
- Wake locks
- Background ANR
- Memory usage

**Benchmarks:**
- Cold startup: < 3 seconds
- Warm startup: < 1 second
- Battery drain: < 2% per hour active use

---

## Backend Deployment

### Backend Architecture Overview

```
┌─────────────────────────────────────────────────┐
│              Load Balancer                       │
│         (AWS ALB / GCP Load Balancer)           │
└────────────────┬────────────────────────────────┘
                 │
        ┌────────┴────────┐
        │                 │
   ┌────▼────┐       ┌───▼─────┐
   │  API    │       │  API    │
   │ Server 1│       │ Server 2│
   │(Primary)│       │(Replica)│
   └────┬────┘       └───┬─────┘
        │                │
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │   PostgreSQL    │
        │  (RDS/Cloud SQL)│
        │   Primary DB    │
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │    Read Replica │
        └─────────────────┘
```

### Infrastructure Requirements

**Minimum Specifications (MVP - 1000 users):**

| Component | Specification |
|-----------|--------------|
| API Server | 2 vCPUs, 4 GB RAM |
| Database | PostgreSQL 14+, 20 GB storage |
| CDN | CloudFront / Cloud CDN |
| Object Storage | S3 / Cloud Storage (100 GB) |
| Bandwidth | 500 GB/month |

**Recommended (Production - 10,000 users):**

| Component | Specification |
|-----------|--------------|
| API Servers | 2x (4 vCPUs, 8 GB RAM each) |
| Database | PostgreSQL 14+, 100 GB SSD, 1 read replica |
| CDN | Global distribution |
| Object Storage | 1 TB |
| Bandwidth | 5 TB/month |
| Cache | Redis 6+ (4 GB) |

### Backend Technology Stack

**Recommended Stack:**

```
Application:
- Language: Kotlin / Node.js / Python (Django/FastAPI)
- Framework: Spring Boot / Express / Django REST
- API: RESTful (GraphQL optional for future)

Database:
- Primary: PostgreSQL 14+
- Cache: Redis 6+
- Search: Elasticsearch (optional)

Infrastructure:
- Cloud: AWS / GCP / Azure
- Container: Docker
- Orchestration: Kubernetes / ECS (optional for scale)
- CI/CD: GitHub Actions / GitLab CI

Monitoring:
- Logging: CloudWatch / Stackdriver
- APM: New Relic / Datadog
- Errors: Sentry
- Uptime: Pingdom / UptimeRobot
```

### Database Setup

#### PostgreSQL Schema

**Core Tables:**

```sql
-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('STUDENT', 'TEACHER', 'ADMIN', 'CREATOR')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Classes table
CREATE TABLE classes (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    curriculum VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Subjects table
CREATE TABLE subjects (
    id VARCHAR(50) PRIMARY KEY,
    class_id VARCHAR(50) REFERENCES classes(id),
    name VARCHAR(100) NOT NULL,
    icon_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Topics table
CREATE TABLE topics (
    id VARCHAR(50) PRIMARY KEY,
    subject_id VARCHAR(50) REFERENCES subjects(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Subtopics table
CREATE TABLE subtopics (
    id VARCHAR(50) PRIMARY KEY,
    topic_id VARCHAR(50) REFERENCES topics(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Content table
CREATE TABLE content (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subtopic_id VARCHAR(50) REFERENCES subtopics(id),
    creator_id UUID REFERENCES users(id),
    content_type VARCHAR(50) NOT NULL,
    body_text TEXT,
    body_image_url TEXT,
    body_video_url TEXT,
    body_pdf_url TEXT,
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Progress tracking
CREATE TABLE content_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    content_id UUID REFERENCES content(id),
    completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, content_id)
);

CREATE TABLE subtopic_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    subtopic_id VARCHAR(50) REFERENCES subtopics(id),
    completion_percentage FLOAT DEFAULT 0,
    last_accessed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, subtopic_id)
);

-- Questions table
CREATE TABLE questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subtopic_id VARCHAR(50) REFERENCES subtopics(id),
    creator_id UUID REFERENCES users(id),
    type VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    question TEXT NOT NULL,
    options JSONB,
    correct_answer TEXT,
    sub_questions JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookmarks
CREATE TABLE bookmarks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    item_type VARCHAR(50) NOT NULL,
    item_id VARCHAR(50) NOT NULL,
    item_title VARCHAR(255),
    item_description TEXT,
    bookmarked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, item_type, item_id)
);

-- Indexes for performance
CREATE INDEX idx_subjects_class ON subjects(class_id);
CREATE INDEX idx_topics_subject ON topics(subject_id);
CREATE INDEX idx_subtopics_topic ON subtopics(topic_id);
CREATE INDEX idx_content_subtopic ON content(subtopic_id);
CREATE INDEX idx_content_status ON content(status);
CREATE INDEX idx_progress_user ON content_progress(user_id);
CREATE INDEX idx_questions_subtopic ON questions(subtopic_id);
CREATE INDEX idx_bookmarks_user ON bookmarks(user_id);
```

#### Database Migration Strategy

**Use Migration Tool:** Flyway / Liquibase / Alembic

**Migration Naming:**
```
V1__initial_schema.sql
V2__add_user_roles.sql
V3__add_bookmarks_table.sql
V4__add_achievements.sql
```

**Best Practices:**
- Never edit applied migrations
- Always test migrations on staging
- Backup database before production migration
- Migrations should be reversible (where possible)
- Version control all migrations

### API Deployment

#### Docker Configuration

**Dockerfile:**

```dockerfile
# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Runtime stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Non-root user
RUN useradd -m appuser
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**docker-compose.yml (Development):**

```yaml
version: '3.8'

services:
  api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=postgresql://db:5432/learnhub
      - DATABASE_USER=learnhub
      - DATABASE_PASSWORD=dev_password
      - JWT_SECRET=dev_secret_change_in_production
      - REDIS_URL=redis://redis:6379
    depends_on:
      - db
      - redis
    restart: unless-stopped

  db:
    image: postgres:14
    environment:
      - POSTGRES_DB=learnhub
      - POSTGRES_USER=learnhub
      - POSTGRES_PASSWORD=dev_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    restart: unless-stopped

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    restart: unless-stopped

volumes:
  postgres_data:
```

#### AWS Deployment (Example)

**Using Elastic Beanstalk:**

1. **Install EB CLI:**
```bash
pip install awsebcli
```

2. **Initialize:**
```bash
eb init -p docker learnhub-api --region us-east-1
```

3. **Create Environment:**
```bash
eb create learnhub-production \
  --instance-type t3.medium \
  --envvars DATABASE_URL=<RDS_URL>,JWT_SECRET=<SECRET>
```

4. **Deploy:**
```bash
eb deploy
```

5. **Configure:**
```bash
# Set environment variables
eb setenv \
  DATABASE_URL=$DB_URL \
  JWT_SECRET=$JWT_SECRET \
  REDIS_URL=$REDIS_URL \
  CDN_URL=$CDN_URL

# Configure auto-scaling
eb scale 2 --min 1 --max 4
```

**Using ECS (Fargate):**

1. Build and push Docker image to ECR
2. Create ECS cluster
3. Define task definition
4. Create service with load balancer
5. Configure auto-scaling

### CDN Setup

#### CloudFront Configuration

**For Static Assets (Images, Videos, PDFs):**

1. **Create S3 Bucket:**
```bash
aws s3 mb s3://learnhub-content --region us-east-1
```

2. **Configure Bucket Policy:**
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::learnhub-content/*"
    }
  ]
}
```

3. **Create CloudFront Distribution:**
- Origin: S3 bucket
- Viewer Protocol: Redirect HTTP to HTTPS
- Allowed HTTP Methods: GET, HEAD, OPTIONS
- Cache Behavior: Cache based on headers
- TTL: 86400 seconds (1 day)

4. **Configure Custom Domain:**
- Create SSL certificate (AWS Certificate Manager)
- Add CNAME record: `cdn.learnhub.ke`

**Content URLs:**
```
Images: https://cdn.learnhub.ke/images/content1.png
Videos: https://cdn.learnhub.ke/videos/algebra-intro.mp4
PDFs: https://cdn.learnhub.ke/pdfs/linear-equations.pdf
```

### Environment Variables

**Production Configuration:**

```bash
# Database
DATABASE_URL=postgresql://user:pass@db.learnhub.ke:5432/learnhub_prod
DATABASE_POOL_SIZE=20
DATABASE_SSL=true

# Redis
REDIS_URL=redis://redis.learnhub.ke:6379
REDIS_PASSWORD=<secure-password>

# JWT
JWT_SECRET=<256-bit-secret>
JWT_EXPIRY=604800  # 7 days in seconds

# API
API_PORT=8080
API_BASE_URL=https://api.learnhub.ke
ALLOWED_ORIGINS=https://learnhub.ke,https://www.learnhub.ke

# CDN
CDN_URL=https://cdn.learnhub.ke
S3_BUCKET=learnhub-content
S3_REGION=us-east-1

# Email (for notifications)
SMTP_HOST=smtp.sendgrid.net
SMTP_PORT=587
SMTP_USER=apikey
SMTP_PASSWORD=<sendgrid-api-key>
FROM_EMAIL=noreply@learnhub.ke

# Monitoring
SENTRY_DSN=<sentry-dsn>
NEW_RELIC_LICENSE_KEY=<license-key>

# Feature Flags
ENABLE_REGISTRATION=true
ENABLE_SOCIAL_FEATURES=true
ENABLE_TEACHER_TOOLS=true

# Rate Limiting
RATE_LIMIT_REQUESTS=1000
RATE_LIMIT_WINDOW=3600  # 1 hour
```

**Secure Secret Management:**

**AWS Secrets Manager:**
```bash
# Store secret
aws secretsmanager create-secret \
  --name learnhub/prod/jwt-secret \
  --secret-string "your-jwt-secret"

# Retrieve in application
aws secretsmanager get-secret-value \
  --secret-id learnhub/prod/jwt-secret
```

---

## CI/CD Pipeline

### GitHub Actions Workflow

**File**: `.github/workflows/android-ci.yml`

```yaml
name: Android CI/CD

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  release:
    types: [ published ]

jobs:
  lint:
    name: Lint Check
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: gradle
      
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      
      - name: Run lint
        run: ./gradlew lint
      
      - name: Upload lint results
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: lint-results
          path: app/build/reports/lint-results.html

  unit-test:
    name: Unit Tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: gradle
      
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      
      - name: Run unit tests
        run: ./gradlew test
      
      - name: Upload test results
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-results
          path: app/build/reports/tests/

  build:
    name: Build APK
    needs: [lint, unit-test]
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: gradle
      
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      
      - name: Build debug APK
        run: ./gradlew assembleDebug
      
      - name: Upload APK
        uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk

  release:
    name: Build and Release
    if: github.event_name == 'release'
    needs: [lint, unit-test]
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: gradle
      
      - name: Decode Keystore
        env:
          ENCODED_KEYSTORE: ${{ secrets.KEYSTORE_BASE64 }}
        run: |
          echo $ENCODED_KEYSTORE | base64 -di > keystore.jks
      
      - name: Create keystore.properties
        env:
          KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
          KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
          KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
        run: |
          echo "releaseKeyStore=keystore.jks" > keystore.properties
          echo "releaseKeyAlias=$KEY_ALIAS" >> keystore.properties
          echo "releaseKeyPassword=$KEY_PASSWORD" >> keystore.properties
          echo "releaseStorePassword=$KEYSTORE_PASSWORD" >> keystore.properties
      
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      
      - name: Build release AAB
        run: ./gradlew bundleRelease
      
      - name: Upload Release AAB
        uses: actions/upload-artifact@v3
        with:
          name: app-release
          path: app/build/outputs/bundle/release/app-release.aab
      
      - name: Upload to Play Store
        uses: r0adkll/upload-google-play@v1
        with:
          serviceAccountJsonPlainText: ${{ secrets.PLAY_STORE_SERVICE_ACCOUNT }}
          packageName: com.learnhub.kenya
          releaseFiles: app/build/outputs/bundle/release/app-release.aab
          track: internal  # or 'beta', 'production'
          status: completed
          whatsNewDirectory: fastlane/metadata/android/en-US/changelogs/
```

### Setting Up Secrets

**GitHub Repository → Settings → Secrets and variables → Actions**

**Required Secrets:**

```
KEYSTORE_BASE64
- Base64 encoded keystore file
- Generate: cat keystore.jks | base64 | tr -d '\n'

KEYSTORE_PASSWORD
- Keystore password

KEY_ALIAS
- Key alias (e.g., learnhub-release)

KEY_PASSWORD
- Key password

PLAY_STORE_SERVICE_ACCOUNT
- Google Play service account JSON
```

### Automated Play Store Upload

**Setup Service Account:**

1. Google Play Console → Settings → API access
2. Create service account (Google Cloud Console)
3. Grant permissions: Release manager
4. Download JSON key
5. Add to GitHub secrets

---

## Monitoring & Maintenance

### Application Monitoring

#### Firebase Crashlytics

**Setup:**

1. Add Firebase to Android app
2. Add dependency:

```kotlin
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
}
```

3. Initialize in Application class:

```kotlin
class LearnHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Enable Crashlytics only in release
        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        
        // Set user ID for tracking
        FirebaseAnalytics.getInstance(this)
    }
}
```

4. Log custom events:

```kotlin
// Log non-fatal errors
try {
    // risky operation
} catch (e: Exception) {
    FirebaseCrashlytics.getInstance().recordException(e)
}

// Set custom keys
FirebaseCrashlytics.getInstance().apply {
    setCustomKey("user_id", userId)
    setCustomKey("screen", "ContentScreen")
}
```

#### Backend Monitoring

**Sentry (Error Tracking):**

```kotlin
// build.gradle.kts
plugins {
    id("io.sentry.android.gradle") version "3.14.0"
}

dependencies {
    implementation("io.sentry:sentry-android:6.34.0")
}

// Application.kt
Sentry.init { options ->
    options.dsn = BuildConfig.SENTRY_DSN
    options.environment = if (BuildConfig.DEBUG) "development" else "production"
    options.release = BuildConfig.VERSION_NAME
    options.tracesSampleRate = 1.0
}
```

**New Relic (APM):**

Monitor:
- Response times
- Throughput
- Error rates
- Database queries
- External services

**CloudWatch / Stackdriver:**

Monitor:
- CPU usage
- Memory usage
- Disk I/O
- Network traffic
- Log aggregation

### Health Checks

**API Health Endpoint:**

```kotlin
@RestController
@RequestMapping("/health")
class HealthController(
    private val dataSource: DataSource,
    private val redisTemplate: RedisTemplate<String, String>
) {
    
    @GetMapping
    fun health(): ResponseEntity<HealthStatus> {
        val status = HealthStatus(
            status = "UP",
            timestamp = System.currentTimeMillis(),
            checks = mapOf(
                "database" to checkDatabase(),
                "redis" to checkRedis(),
                "disk" to checkDisk()
            )
        )
        
        return if (status.checks.all { it.value == "UP" }) {
            ResponseEntity.ok(status)
        } else {
            ResponseEntity.status(503).body(status)
        }
    }
    
    private fun checkDatabase(): String {
        return try {
            dataSource.connection.use { it.isValid(5) }
            "UP"
        } catch (e: Exception) {
            "DOWN"
        }
    }
    
    private fun checkRedis(): String {
        return try {
            redisTemplate.opsForValue().get("health_check")
            "UP"
        } catch (e: Exception) {
            "DOWN"
        }
    }
    
    private fun checkDisk(): String {
        val free = File("/").freeSpace
        val total = File("/").totalSpace
        val usagePercent = ((total - free).toDouble() / total) * 100
        
        return if (usagePercent < 90) "UP" else "WARNING"
    }
}

data class HealthStatus(
    val status: String,
    val timestamp: Long,
    val checks: Map<String, String>
)
```

**Uptime Monitoring:**

Use external service:
- Pingdom
- UptimeRobot
- StatusCake

Configure:
- Check interval: 1-5 minutes
- Alert on: 2 consecutive failures
- Notifications: Email, SMS, Slack

### Log Management

**Structured Logging:**

```kotlin
// Use SLF4J with Logback
import org.slf4j.LoggerFactory

class ContentService {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    fun getContent(id: String): Content? {
        logger.info("Fetching content", 
            kv("contentId", id),
            kv("userId", getCurrentUserId()))
        
        return try {
            val content = repository.findById(id)
            logger.info("Content fetched successfully",
                kv("contentId", id),
                kv("contentType", content.type))
            content
        } catch (e: Exception) {
            logger.error("Failed to fetch content",
                kv("contentId", id),
                kv("error", e.message),
                e)
            null
        }
    }
}
```

**Log Levels:**

- **ERROR**: System errors, exceptions
- **WARN**: Unexpected situations, deprecated usage
- **INFO**: Important business events
- **DEBUG**: Detailed information (dev only)
- **TRACE**: Very detailed (dev only)

**Log Aggregation:**

Use:
- CloudWatch Logs (AWS)
- Stackdriver Logging (GCP)
- ELK Stack (self-hosted)
- Splunk (enterprise)

**Log Retention:**

- ERROR logs: 90 days
- WARN logs: 60 days
- INFO logs: 30 days
- DEBUG logs: 7 days (development only)

### Performance Monitoring

**Key Metrics:**

```
API Metrics:
- Average response time: < 200ms
- 95th percentile: < 500ms
- 99th percentile: < 1000ms
- Error rate: < 0.1%
- Throughput: requests per second

Database Metrics:
- Query time: < 50ms average
- Connection pool: 80% max usage
- Slow queries: < 5 per minute

App Metrics:
- App startup time: < 3s
- Screen load time: < 1s
- Memory usage: < 200 MB
- Battery drain: < 2% per hour
```

**Alerting Thresholds:**

```yaml
# Example alert configuration
alerts:
  - name: high_error_rate
    condition: error_rate > 1%
    duration: 5m
    severity: critical
    notify: [email, slack, pagerduty]
  
  - name: high_response_time
    condition: p95_response_time > 1000ms
    duration: 10m
    severity: warning
    notify: [email, slack]
  
  - name: low_disk_space
    condition: disk_usage > 85%
    duration: 5m
    severity: warning
    notify: [email]
  
  - name: database_down
    condition: database_health == "DOWN"
    duration: 1m
    severity: critical
    notify: [email, slack, pagerduty, sms]
```

---

## Rollback Procedures

### Play Store Rollback

**Scenario: Critical bug in production**

#### Option 1: Halt Rollout (Staged Rollout)

1. Play Console → Production track
2. Click "Halt rollout"
3. No new users receive update
4. Existing users keep current version

**When to use:**
- Bug affects small percentage
- Fix available within 24 hours
- Bug not critical

#### Option 2: Emergency Rollback

1. Play Console → Production track
2. Create new release with previous version
3. Increase version code (must be higher)
4. Upload previous AAB with new version code
5. Submit for expedited review

**When to use:**
- Critical bug (crashes, data loss)
- Affects majority of users
- Fix will take > 24 hours

**Example:**

```
Current: v1.1.0 (Build 3) - Has critical bug
Rollback: v1.0.1 (Build 4) - Previous stable version
          (Same code as Build 2, but new version code)
```

#### Option 3: Hot Fix Release

1. Create fix branch from last stable tag
2. Apply minimal fix
3. Test thoroughly
4. Build and release as patch version
5. Submit for expedited review

**Example:**

```
Current: v1.1.0 (Build 3) - Has bug
Hot Fix: v1.1.1 (Build 4) - Minimal fix only
```

### Backend Rollback

#### Docker Rollback

**Using Tags:**

```bash
# Current deployment
docker pull learnhub/api:1.1.0
docker-compose up -d

# Rollback to previous version
docker pull learnhub/api:1.0.1
docker-compose down
docker-compose up -d
```

#### Kubernetes Rollback

```bash
# View deployment history
kubectl rollout history deployment/learnhub-api

# Rollback to previous version
kubectl rollout undo deployment/learnhub-api

# Rollback to specific revision
kubectl rollout undo deployment/learnhub-api --to-revision=2

# Check rollback status
kubectl rollout status deployment/learnhub-api
```

#### Database Rollback

**⚠️ CAUTION: Database rollbacks are risky**

**Best Practice:**
- Never rollback database
- Write forward-compatible migrations
- Keep data migrations separate from schema changes

**If Necessary:**

1. **Backup first:**
```bash
pg_dump -h db.learnhub.ke -U admin learnhub_prod > backup_pre_rollback.sql
```

2. **Apply rollback migration:**
```bash
flyway undo  # If using Flyway
# or
alembic downgrade -1  # If using Alembic
```

3. **Verify data integrity:**
```sql
-- Run validation queries
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM content WHERE status = 'PUBLISHED';
-- etc.
```

### Rollback Decision Matrix

| Issue | Severity | Action | Timeline |
|-------|----------|--------|----------|
| UI glitch | Low | Hot fix in next release | 1-2 days |
| Feature bug (non-critical) | Medium | Hot fix or rollout halt | < 24 hours |
| Login failure | High | Emergency rollback | < 2 hours |
| Data corruption | Critical | Immediate rollback + DB restore | < 30 minutes |
| Security vulnerability | Critical | Immediate rollback or hot fix | < 1 hour |
| Frequent crashes | Critical | Emergency rollback | < 2 hours |

---

## Post-Deployment Checklist

### Immediately After Deployment (0-1 hour)

- [ ] Verify app appears in Play Store
- [ ] Test download and installation
- [ ] Verify app opens successfully
- [ ] Check Crashlytics for new crashes
- [ ] Monitor error rates (< 0.5%)
- [ ] Check backend health endpoints
- [ ] Verify API response times
- [ ] Test critical user flows:
    - [ ] Login
    - [ ] Browse content
    - [ ] Take quiz
    - [ ] Track progress
- [ ] Monitor first reviews

### First 24 Hours

- [ ] Monitor crash rate (target < 1%)
- [ ] Review user feedback/reviews
- [ ] Check analytics:
    - [ ] DAU (Daily Active Users)
    - [ ] Session duration
    - [ ] Retention rate
- [ ] Monitor backend metrics:
    - [ ] API response time
    - [ ] Error rates
    - [ ] Database performance
- [ ] Check CDN traffic
- [ ] Review logs for anomalies
- [ ] Respond to critical reviews

### First Week

- [ ] Analyze retention metrics:
    - [ ] D1 retention (target > 40%)
    - [ ] D3 retention
    - [ ] D7 retention (target > 20%)
- [ ] Review feature usage:
    - [ ] Most viewed content
    - [ ] Quiz completion rate
    - [ ] Search usage
    - [ ] Bookmark usage
- [ ] Check performance:
    - [ ] App startup time
    - [ ] Screen load times
    - [ ] Battery usage
- [ ] Analyze crash clusters
- [ ] Review and respond to all reviews
- [ ] Gather user feedback
- [ ] Plan hot fixes if needed

### Ongoing

- [ ] Weekly metric reviews
- [ ] Monthly performance audits
- [ ] Quarterly security reviews
- [ ] Regular dependency updates
- [ ] A/B testing for new features
- [ ] Content updates and additions
- [ ] Community engagement

---

## Deployment Calendar

### Recommended Schedule

**Regular Releases:**

```
Week 1-2: Development sprint
Week 3: Internal testing
Week 4: Staged rollout (beta)
Week 5: Full production release
Week 6-8: Monitor and iterate
```

**Annual Cycle:**

```
Q1 (Jan-Mar): Major feature release (v2.0)
Q2 (Apr-Jun): Enhancement release (v2.1)
Q3 (Jul-Sep): Polish and optimization (v2.2)
Q4 (Oct-Dec): Prepare for next major (v2.3)
```

**Best Times to Release:**

**Avoid:**
- Fridays (limited support availability)
- Holidays and weekends
- Peak exam periods (for education apps)
- End of month/quarter

**Prefer:**
- Tuesday or Wednesday
- Mid-morning (10 AM - 12 PM)
- After team standup
- When full team available

---

## Emergency Contacts

### Escalation Path

```
Level 1: On-call Engineer
         ↓ (15 minutes)
Level 2: Lead Engineer
         ↓ (30 minutes)
Level 3: Engineering Manager
         ↓ (1 hour)
Level 4: CTO / Founder
```

### Contact Information

**Team Contacts:**

```
On-Call Engineer: +254 XXX XXX XXX (Rotating)
Lead Engineer: lead@learnhub.ke
Engineering Manager: engineering@learnhub.ke
CTO: cto@learnhub.ke
DevOps: devops@learnhub.ke
Support Team: support@learnhub.ke
```

**External Services:**

```
AWS Support: Via console (Premium support)
Google Play Support: Via Play Console
Firebase Support: Via console
Sentry: Via web dashboard
```

---

## Summary

### Deployment Workflow

```
1. Develop feature
2. Write tests
3. Code review
4. Merge to develop
5. Automated tests pass
6. Build staging version
7. QA testing
8. Merge to main
9. Create release tag
10. Build production AAB
11. Upload to Play Console
12. Staged rollout (5% → 100%)
13. Monitor metrics
14. Full release
15. Post-release monitoring
```

### Key Success Metrics

| Metric | Target | Critical Threshold |
|--------|--------|-------------------|
| Crash Rate | < 0.5% | > 2% |
| ANR Rate | < 0.3% | > 1% |
| App Rating | > 4.0 | < 3.5 |
| D1 Retention | > 40% | < 30% |
| D7 Retention | > 20% | < 15% |
| API Response Time | < 200ms | > 1000ms |
| API Error Rate | < 0.1% | > 1% |
| Uptime | > 99.5% | < 99% |

---

**Document Version**: 1.0  
**Last Updated**: December 2024  
**Status**: Complete  
**Next Review**: Q1 2025
```

---