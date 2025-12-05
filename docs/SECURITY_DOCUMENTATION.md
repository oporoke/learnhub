# 📚 COMPLETE DOCUMENTATION SUITE (Continued)

---

## 🔟 SECURITY DOCUMENTATION

Path: `docs/10_SECURITY_DOCUMENTATION.md`

```markdown
# LearnHub Kenya - Security Documentation

## Table of Contents
1. [Security Overview](#security-overview)
2. [Security Requirements](#security-requirements)
3. [Threat Model](#threat-model)
4. [Security Architecture](#security-architecture)
5. [Authentication & Authorization](#authentication--authorization)
6. [Data Protection](#data-protection)
7. [Network Security](#network-security)
8. [Application Security](#application-security)
9. [Security Testing](#security-testing)
10. [Incident Response](#incident-response)
11. [Compliance & Regulations](#compliance--regulations)
12. [Security Best Practices](#security-best-practices)

---

## Security Overview

### Security Mission Statement

> "Protect user data, ensure privacy, and maintain trust by implementing robust security measures across all layers of the LearnHub Kenya platform while enabling seamless learning experiences."

### Security Principles

**Core Principles:**

1. **Defense in Depth**
   - Multiple layers of security controls
   - No single point of failure
   - Redundant security measures

2. **Least Privilege**
   - Minimum necessary access
   - Role-based permissions
   - Time-limited credentials

3. **Secure by Default**
   - Secure configurations out-of-box
   - Encryption enabled by default
   - Security-first design decisions

4. **Privacy by Design**
   - Minimal data collection
   - User control over data
   - Transparency in data usage

5. **Fail Securely**
   - Graceful degradation
   - No sensitive data in error messages
   - Secure fallback mechanisms

6. **Zero Trust**
   - Verify everything
   - Never assume trust
   - Continuous validation

### Security Scope

**In Scope:**
- Mobile application security (Android)
- API security (backend)
- Data at rest and in transit
- User authentication and authorization
- Network communications
- Third-party integrations
- Infrastructure security

**Out of Scope:**
- Physical security of user devices
- User account compromise due to social engineering
- Security of third-party services beyond our control
- Client-side modifications (rooted devices)

### Security Posture

**Current Status:**

| Area | Status | Risk Level | Notes |
|------|--------|-----------|-------|
| Authentication | ✅ Implemented | Low | Session-based, needs JWT upgrade |
| Data Encryption | ✅ Implemented | Low | HTTPS, encrypted storage |
| API Security | ⚠️ Basic | Medium | Mock API, needs hardening |
| Input Validation | ✅ Implemented | Low | Client-side validation |
| Dependency Security | ✅ Monitored | Low | Regular updates |
| Code Obfuscation | ✅ Implemented | Low | ProGuard enabled |
| Penetration Testing | ❌ Not Done | Medium | Planned for Q1 2025 |
| Security Audits | ❌ Not Done | Medium | Planned for Q1 2025 |

---

## Security Requirements

### Functional Security Requirements

#### SR-001: User Authentication

**Requirement**: Users must authenticate before accessing protected resources

**Implementation:**
- Email and password authentication
- Password minimum 6 characters
- Session persistence
- Logout functionality
- Password reset capability (planned)

**Acceptance Criteria:**
- [ ] Passwords hashed with bcrypt (salt rounds ≥ 10)
- [ ] Session tokens expire after 7 days
- [ ] Failed login attempts throttled (max 5 attempts/15 min)
- [ ] Account lockout after 10 failed attempts
- [ ] Secure password reset flow

**Priority**: Critical  
**Status**: Partial (basic auth implemented, hardening needed)

---

#### SR-002: Authorization

**Requirement**: Role-based access control for different user types

**Roles:**
- **Student**: Access to learning content, quizzes, progress
- **Teacher**: All student access + lesson planner, question bank, exam builder
- **Admin**: All access + user management, content moderation
- **Creator**: Content creation and management

**Implementation:**
- Role stored in user profile
- Permission checks on all protected routes
- Server-side authorization (when backend ready)

**Acceptance Criteria:**
- [ ] Role assigned at registration
- [ ] Role-based UI rendering
- [ ] Server-side permission validation
- [ ] Audit log for privileged actions

**Priority**: High  
**Status**: Implemented (client-side, needs server validation)

---

#### SR-003: Data Encryption

**Requirement**: Sensitive data must be encrypted at rest and in transit

**Data Classification:**
- **Highly Sensitive**: Passwords, payment info (future)
- **Sensitive**: Email, progress data, quiz answers
- **Public**: Published content, public profiles

**Implementation:**
- HTTPS for all network communication
- Encrypted SharedPreferences for tokens
- Encrypted database for sensitive data (planned)
- Certificate pinning (planned)

**Acceptance Criteria:**
- [x] HTTPS enforced for all API calls
- [x] TLS 1.2+ only
- [ ] Certificate pinning implemented
- [ ] SQLCipher for database encryption
- [ ] Encrypted backup storage

**Priority**: Critical  
**Status**: Partial (HTTPS implemented, enhanced encryption planned)

---

#### SR-004: Input Validation

**Requirement**: All user input must be validated and sanitized

**Validation Points:**
- Client-side: Immediate user feedback
- Server-side: Security enforcement

**Implementation:**
- Email format validation
- Password strength validation
- Content length limits
- SQL injection prevention (parameterized queries)
- XSS prevention (escape output)

**Acceptance Criteria:**
- [x] Client-side validation for all forms
- [ ] Server-side validation (backend)
- [x] Parameterized database queries
- [x] Output encoding for user-generated content
- [ ] Rate limiting on API endpoints

**Priority**: High  
**Status**: Implemented (client-side)

---

#### SR-005: Session Management

**Requirement**: Secure session handling with timeout and revocation

**Implementation:**
- Session token stored securely
- Token refresh mechanism
- Session timeout after inactivity
- Logout clears session

**Acceptance Criteria:**
- [x] Secure token storage (EncryptedSharedPreferences)
- [ ] Token expiration (7 days)
- [ ] Token refresh before expiry
- [ ] Server-side session validation
- [x] Logout clears local session
- [ ] Concurrent session control

**Priority**: High  
**Status**: Basic implementation

---

### Non-Functional Security Requirements

#### NSR-001: Performance

**Requirement**: Security measures must not significantly impact app performance

**Targets:**
- Encryption/decryption: < 50ms overhead
- Authentication check: < 100ms
- SSL handshake: < 500ms

**Status**: ✅ Met

---

#### NSR-002: Availability

**Requirement**: Security measures must maintain 99.5% uptime

**Measures:**
- DDoS protection
- Rate limiting
- Graceful degradation

**Status**: 🔄 To be validated with backend

---

#### NSR-003: Auditability

**Requirement**: All security-relevant events must be logged

**Events to Log:**
- Login attempts (success/failure)
- Password changes
- Role changes
- Data access (sensitive)
- Permission violations
- Security configuration changes

**Status**: ❌ Pending (requires backend)

---

## Threat Model

### Threat Modeling Methodology

**Framework**: STRIDE (Microsoft)

- **S**poofing Identity
- **T**ampering with Data
- **R**epudiation
- **I**nformation Disclosure
- **D**enial of Service
- **E**levation of Privilege

### Asset Identification

**Critical Assets:**

| Asset | Value | Criticality | Protection |
|-------|-------|-------------|------------|
| User Credentials | High | Critical | Hashing, encryption |
| User PII (email, name) | High | Critical | Encryption, access control |
| Student Progress Data | Medium | High | Encryption, backup |
| Quiz Answers | Medium | High | Encryption |
| Content Library | Medium | Medium | Access control |
| Session Tokens | High | Critical | Encryption, expiration |
| API Keys | High | Critical | Secrets management |
| Database | High | Critical | Encryption, access control |

### Threat Actors

**1. External Attackers**

**Motivation**: Data theft, service disruption, financial gain

**Capabilities**:
- Network attacks
- Application exploits
- Social engineering
- Automated tools

**Threat Level**: Medium

**Mitigation**:
- Input validation
- Rate limiting
- DDoS protection
- Security monitoring

---

**2. Malicious Users**

**Motivation**: Cheat on quizzes, access premium content, vandalism

**Capabilities**:
- Account manipulation
- API abuse
- Content scraping
- False data submission

**Threat Level**: Low-Medium

**Mitigation**:
- Client-side integrity checks
- Server-side validation
- Rate limiting
- User behavior monitoring

---

**3. Compromised Accounts**

**Motivation**: Varies (attacker goals)

**Capabilities**:
- Access to user data
- Content modification
- Impersonation

**Threat Level**: Medium

**Mitigation**:
- Multi-factor authentication (planned)
- Anomaly detection
- Session management
- Account recovery process

---

**4. Insider Threats**

**Motivation**: Data theft, sabotage, curiosity

**Capabilities**:
- Direct database access
- Code modification
- Privileged operations

**Threat Level**: Low

**Mitigation**:
- Principle of least privilege
- Audit logging
- Code reviews
- Background checks

---

### Threat Scenarios

#### Threat T-001: Credential Theft

**Scenario**: Attacker obtains user credentials through phishing or brute force

**Impact**: High (account takeover, data exposure)  
**Likelihood**: Medium  
**Risk Score**: 6/9

**Attack Vector**:
1. Phishing email tricks user into revealing password
2. OR: Brute force attack on login endpoint
3. Attacker logs in with stolen credentials
4. Access to user's progress, personal data

**Existing Controls**:
- Password complexity requirements
- Client-side validation

**Gaps**:
- No MFA
- No rate limiting (backend)
- No account lockout
- No breach detection

**Mitigation Plan**:
- [ ] Implement rate limiting (backend)
- [ ] Add account lockout after failed attempts
- [ ] Implement CAPTCHA for suspicious logins
- [ ] Add MFA option (Phase 2)
- [ ] Monitor for credential stuffing attacks
- [ ] Educate users on phishing

**Priority**: High  
**Timeline**: Q1 2025

---

#### Threat T-002: Man-in-the-Middle (MITM)

**Scenario**: Attacker intercepts network communication

**Impact**: High (data exposure, session hijacking)  
**Likelihood**: Low  
**Risk Score**: 4/9

**Attack Vector**:
1. User connects to compromised Wi-Fi
2. Attacker intercepts HTTPS traffic
3. Attempts to downgrade to HTTP or bypass certificate validation
4. Captures session tokens or sensitive data

**Existing Controls**:
- HTTPS for all communication
- TLS 1.2+

**Gaps**:
- No certificate pinning
- User could ignore certificate warnings

**Mitigation Plan**:
- [ ] Implement certificate pinning
- [ ] Add network security config (Android)
- [ ] Detect and warn on network anomalies
- [ ] Use public key pinning

**Priority**: Medium  
**Timeline**: Q1 2025

---

#### Threat T-003: SQL Injection

**Scenario**: Attacker injects malicious SQL through input fields

**Impact**: Critical (database compromise)  
**Likelihood**: Low  
**Risk Score**: 5/9

**Attack Vector**:
1. Attacker finds input field (search, login)
2. Injects SQL code (e.g., `' OR '1'='1`)
3. Bypasses authentication or extracts data
4. Gains unauthorized access or data

**Existing Controls**:
- Parameterized queries (Room)
- Input validation

**Gaps**:
- No server-side validation yet
- Limited input sanitization

**Mitigation Plan**:
- [x] Use parameterized queries (Room) ✅
- [ ] Implement server-side input validation
- [ ] Add WAF when backend deployed
- [ ] Regular security scanning

**Priority**: High  
**Timeline**: With backend deployment

---

#### Threat T-004: Data Leakage

**Scenario**: Sensitive data exposed through logs, errors, or backups

**Impact**: Medium (privacy violation)  
**Likelihood**: Medium  
**Risk Score**: 5/9

**Attack Vector**:
1. Sensitive data written to logs
2. Error messages reveal internal details
3. Backups stored insecurely
4. Attacker accesses logs or backups

**Existing Controls**:
- Production logging disabled for sensitive data
- Error messages sanitized

**Gaps**:
- Logs not reviewed for sensitive data
- No secure backup encryption
- Debug builds may leak data

**Mitigation Plan**:
- [ ] Audit all logging statements
- [ ] Implement log scrubbing
- [ ] Encrypt backups
- [ ] Separate debug/release logging
- [ ] Regular log reviews

**Priority**: Medium  
**Timeline**: Q1 2025

---

#### Threat T-005: Reverse Engineering

**Scenario**: Attacker decompiles app to extract secrets or bypass security

**Impact**: Medium (API keys exposed, logic revealed)  
**Likelihood**: High  
**Risk Score**: 6/9

**Attack Vector**:
1. Attacker downloads APK
2. Decompiles using jadx or similar
3. Extracts API keys, endpoints, secrets
4. Analyzes business logic
5. Bypasses client-side checks

**Existing Controls**:
- ProGuard obfuscation
- No hardcoded secrets (mostly)

**Gaps**:
- API endpoints visible in code
- Business logic exposed
- Limited obfuscation

**Mitigation Plan**:
- [x] Enable ProGuard for release ✅
- [ ] Use R8 full mode
- [ ] Store secrets in native code (NDK)
- [ ] Implement root detection
- [ ] Server-side validation for critical logic
- [ ] API key rotation

**Priority**: Medium  
**Timeline**: Q1-Q2 2025

---

#### Threat T-006: Denial of Service (DoS)

**Scenario**: Attacker overwhelms service with requests

**Impact**: High (service unavailable)  
**Likelihood**: Medium  
**Risk Score**: 6/9

**Attack Vector**:
1. Attacker identifies API endpoints
2. Floods endpoints with requests
3. Consumes server resources
4. Legitimate users cannot access service

**Existing Controls**:
- None (mock API currently)

**Gaps**:
- No rate limiting
- No DDoS protection
- No traffic monitoring

**Mitigation Plan**:
- [ ] Implement rate limiting (backend)
- [ ] Add CDN with DDoS protection (Cloudflare)
- [ ] Implement request throttling
- [ ] Monitor for unusual traffic patterns
- [ ] Have incident response plan

**Priority**: High  
**Timeline**: With backend deployment

---

### Threat Priority Matrix

```
           Impact
           │
      High │  T-002  │  T-001  │  T-006  │
           │  MITM   │  Cred   │  DoS    │
           │         │  Theft  │         │
           ├─────────┼─────────┼─────────┤
    Medium │  T-004  │  T-003  │  T-005  │
           │  Leak   │  SQLi   │  Reverse│
           │         │         │         │
           ├─────────┼─────────┼─────────┤
      Low  │         │         │         │
           │         │         │         │
           └─────────┴─────────┴─────────┘
              Low    Medium    High
                  Likelihood

Priority:
1. T-001 (High Impact, Medium Likelihood)
2. T-006 (High Impact, Medium Likelihood)
3. T-005 (Medium Impact, High Likelihood)
4. T-003 (Critical Impact, Low Likelihood)
5. T-004 (Medium Impact, Medium Likelihood)
6. T-002 (High Impact, Low Likelihood)
```

---

## Security Architecture

### Security Layers

```
┌─────────────────────────────────────────────────────┐
│                  User Layer                          │
│  - User awareness training                          │
│  - Secure password practices                        │
└─────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────┐
│              Application Layer                       │
│  - Input validation                                 │
│  - Authentication & authorization                   │
│  - Session management                               │
│  - Code obfuscation                                 │
└─────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────┐
│               Network Layer                          │
│  - HTTPS/TLS                                        │
│  - Certificate pinning                              │
│  - API authentication                               │
│  - Rate limiting                                    │
└─────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────┐
│                Data Layer                            │
│  - Encryption at rest                               │
│  - Database access control                          │
│  - Encrypted backups                                │
│  - Data minimization                                │
└─────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────┐
│           Infrastructure Layer                       │
│  - Firewalls                                        │
│  - DDoS protection                                  │
│  - Security monitoring                              │
│  - Incident response                                │
└─────────────────────────────────────────────────────┘
```

### Trust Boundaries

**Trust Boundary 1: User Device → App**
- User input untrusted
- Validate all inputs
- Sanitize before processing

**Trust Boundary 2: App → Network**
- Network communication untrusted
- Use HTTPS only
- Validate all responses

**Trust Boundary 3: App → Local Storage**
- Storage potentially accessible
- Encrypt sensitive data
- Validate data on read

**Trust Boundary 4: Network → Backend**
- All requests untrusted
- Authenticate and authorize
- Rate limit and validate

### Security Components

#### 1. Authentication Manager

**Responsibility**: User authentication and session management

**Location**: `data/repository/AuthRepositoryImpl.kt`

**Security Features**:
- Password validation
- Session token generation
- Token storage (encrypted)
- Session expiration
- Logout functionality

**Code Example**:

```kotlin
class AuthRepositoryImpl(
    private val preferences: EncryptedSharedPreferences,
    private val api: AuthApiService
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Result<User> {
        // Validate input
        if (!isValidEmail(email)) {
            return Result.failure(InvalidEmailException())
        }
        
        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.failure(WeakPasswordException())
        }
        
        // Call API (when available)
        return try {
            val response = api.login(email, password)
            
            // Store session token securely
            preferences.edit {
                putString(KEY_SESSION_TOKEN, response.token)
                putLong(KEY_TOKEN_EXPIRY, response.expiryTime)
            }
            
            Result.success(response.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun logout() {
        // Clear session
        preferences.edit {
            remove(KEY_SESSION_TOKEN)
            remove(KEY_TOKEN_EXPIRY)
        }
    }
    
    override fun isSessionValid(): Boolean {
        val token = preferences.getString(KEY_SESSION_TOKEN, null)
        val expiry = preferences.getLong(KEY_TOKEN_EXPIRY, 0L)
        
        return token != null && System.currentTimeMillis() < expiry
    }
    
    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_TOKEN_EXPIRY = "token_expiry"
    }
}
```

---

#### 2. Encrypted Storage Manager

**Responsibility**: Secure data storage

**Location**: `data/preferences/SecurePreferences.kt`

**Security Features**:
- EncryptedSharedPreferences for sensitive data
- Key encryption using Android Keystore
- Automatic key generation
- Secure key storage

**Code Example**:

```kotlin
object SecurePreferences {
    
    private const val PREFS_FILE = "secure_prefs"
    
    fun getEncryptedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        return EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
```

---

#### 3. Network Security Manager

**Responsibility**: Secure network communication

**Location**: `data/remote/NetworkSecurityConfig.kt`

**Security Features**:
- HTTPS only
- Certificate pinning (planned)
- Network security config
- SSL/TLS validation

**Network Security Config** (`res/xml/network_security_config.xml`):

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <!-- Production configuration -->
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">api.learnhub.ke</domain>
        <domain includeSubdomains="true">cdn.learnhub.ke</domain>
        
        <!-- Certificate pinning (planned) -->
        <pin-set expiration="2026-01-01">
            <pin digest="SHA-256">AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=</pin>
            <pin digest="SHA-256">BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=</pin>
        </pin-set>
    </domain-config>
    
    <!-- Debug configuration -->
    <debug-overrides>
        <trust-anchors>
            <certificates src="user" />
        </trust-anchors>
    </debug-overrides>
</network-security-config>
```

**Application Manifest**:

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    android:usesCleartextTraffic="false">
    <!-- ... -->
</application>
```

---

#### 4. Input Validation Manager

**Responsibility**: Validate and sanitize all user input

**Location**: `presentation/util/InputValidator.kt`

**Code Example**:

```kotlin
object InputValidator {
    
    private const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private const val MIN_PASSWORD_LENGTH = 6
    private const val MAX_TEXT_LENGTH = 1000
    
    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_PATTERN.toRegex())
    }
    
    fun isValidPassword(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }
    
    fun sanitizeText(text: String): String {
        return text
            .take(MAX_TEXT_LENGTH)
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("/", "&#x2F;")
    }
    
    fun validateSearchQuery(query: String): Boolean {
        // Prevent SQL injection in search
        val blacklist = listOf("--", ";", "/*", "*/", "xp_", "sp_", "drop", "insert", "delete", "update")
        return blacklist.none { query.lowercase().contains(it) }
    }
}
```

---

## Authentication & Authorization

### Authentication Flow

```
┌──────────┐
│  User    │
└────┬─────┘
     │ 1. Enter credentials
     ▼
┌──────────────┐
│ LoginScreen  │
└──────┬───────┘
       │ 2. Validate input
       ▼
┌──────────────┐
│LoginViewModel│
└──────┬───────┘
       │ 3. Call UseCase
       ▼
┌──────────────┐
│ LoginUseCase │
└──────┬───────┘
       │ 4. Authenticate
       ▼
┌──────────────┐
│AuthRepository│
└──────┬───────┘
       │ 5. Verify credentials
       ▼
┌──────────────┐
│   Database   │ (Future: API call)
│ or API       │
└──────┬───────┘
       │ 6. Return user + token
       ▼
┌──────────────┐
│Store Session │ (EncryptedSharedPreferences)
└──────┬───────┘
       │ 7. Navigate to Home
       ▼
┌──────────────┐
│  HomeScreen  │
└──────────────┘
```

### Session Management

**Session Lifecycle:**

```
Login → Token Generated → Token Stored → Token Used → Token Expires → Re-authenticate
                                  ↓
                            Token Refresh (optional)
                                  ↓
                            New Token Generated
```

**Session Properties:**
- Token Type: JWT (planned), currently simple UUID
- Token Storage: EncryptedSharedPreferences
- Token Expiry: 7 days
- Refresh: Not implemented (planned)
- Revocation: On logout

**Session Security:**

```kotlin
class SessionManager(
    private val encryptedPrefs: SharedPreferences
) {
    
    fun saveSession(token: String, expiryTime: Long) {
        encryptedPrefs.edit {
            putString(KEY_TOKEN, token)
            putLong(KEY_EXPIRY, expiryTime)
        }
    }
    
    fun getToken(): String? {
        val token = encryptedPrefs.getString(KEY_TOKEN, null)
        val expiry = encryptedPrefs.getLong(KEY_EXPIRY, 0L)
        
        // Check if expired
        if (System.currentTimeMillis() > expiry) {
            clearSession()
            return null
        }
        
        return token
    }
    
    fun isSessionValid(): Boolean {
        return getToken() != null
    }
    
    fun clearSession() {
        encryptedPrefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_EXPIRY)
        }
    }
    
    companion object {
        private const val KEY_TOKEN = "session_token"
        private const val KEY_EXPIRY = "session_expiry"
    }
}
```

### Authorization Model

**Role-Based Access Control (RBAC):**

| Role | Permissions |
|------|------------|
| **Student** | - View content<br>- Take quizzes<br>- Track progress<br>- View analytics<br>- Bookmark content<br>- Search content |
| **Teacher** | - All Student permissions<br>- Create lesson plans<br>- Manage question bank<br>- Generate exams<br>- View class analytics |
| **Admin** | - All Teacher permissions<br>- User management<br>- Content moderation<br>- System configuration |
| **Creator** | - Content creation<br>- Content editing<br>- Content submission |

**Permission Check Example:**

```kotlin
fun hasPermission(user: User, permission: Permission): Boolean {
    return when (user.role) {
        UserRole.ADMIN -> true  // Admin has all permissions
        UserRole.TEACHER -> teacherPermissions.contains(permission)
        UserRole.STUDENT -> studentPermissions.contains(permission)
        UserRole.CREATOR -> creatorPermissions.contains(permission)
    }
}

// Usage
if (hasPermission(currentUser, Permission.CREATE_LESSON_PLAN)) {
    // Allow access to lesson planner
} else {
    // Show access denied message
}
```

---

## Data Protection

### Data Classification

| Classification | Examples | Protection Measures |
|---------------|----------|---------------------|
| **Public** | Published content, public profiles | Access control |
| **Internal** | App analytics, usage statistics | Access control, aggregation |
| **Confidential** | User emails, progress data | Encryption, access control, audit logs |
| **Restricted** | Passwords, payment info | Hashing, encryption, strict access control |

### Encryption Standards

**Data in Transit:**
- Protocol: TLS 1.2 / TLS 1.3
- Cipher Suites: Strong ciphers only
- Certificate Validation: Strict
- Certificate Pinning: Planned

**Data at Rest:**
- Algorithm: AES-256-GCM
- Key Management: Android Keystore
- Storage: EncryptedSharedPreferences, Room (with encryption planned)

**Passwords:**
- Algorithm: bcrypt (planned)
- Salt Rounds: 12 (planned)
- Current: Plain text in mock (MUST change for production)

### Sensitive Data Handling

**Do's:**
- ✅ Encrypt sensitive data
- ✅ Use Android Keystore for keys
- ✅ Hash passwords (never store plain)
- ✅ Minimize data collection
- ✅ Sanitize logs (no PII)
- ✅ Secure data in transit (HTTPS)

**Don'ts:**
- ❌ Log sensitive data
- ❌ Store passwords in plain text
- ❌ Hard-code secrets
- ❌ Use weak encryption
- ❌ Include PII in error messages
- ❌ Share data without consent

### Data Retention

**Policy:**

| Data Type | Retention Period | Reason |
|-----------|-----------------|--------|
| User Account Data | Until account deletion | Service provision |
| Progress Data | Until account deletion | Service provision |
| Quiz History | 2 years | Analytics, reporting |
| Login Logs | 90 days | Security monitoring |
| Error Logs | 30 days | Debugging |
| Analytics Data | 1 year (aggregated) | Product improvement |
| Deleted Account Data | 30 days (soft delete) | Recovery window |

**Data Deletion:**
- User-initiated deletion: Immediate from app, 30-day grace period on server
- Inactive accounts: No automatic deletion
- Legal requests: Compliance with GDPR (right to erasure)

---

## Network Security

### HTTPS Enforcement

**Implementation:**

```kotlin
// Retrofit configuration
val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(AuthInterceptor())
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    })
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.learnhub.ke/")  // HTTPS enforced
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

### Certificate Pinning (Planned)

**Implementation:**

```kotlin
val certificatePinner = CertificatePinner.Builder()
    .add("api.learnhub.ke", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
    .add("api.learnhub.ke", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")  // Backup pin
    .build()

val okHttpClient = OkHttpClient.Builder()
    .certificatePinner(certificatePinner)
    .build()
```

### API Security

**Authentication Header:**

```kotlin
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()
        
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("X-Client-Version", BuildConfig.VERSION_NAME)
                .addHeader("X-Platform", "Android")
                .build()
        } else {
            chain.request()
        }
        
        return chain.proceed(request)
    }
}
```

### Rate Limiting (Backend)

**Strategy:**
- Per IP: 100 requests/minute
- Per User: 1000 requests/hour
- Login attempts: 5/15 minutes
- Gradual backoff on repeated violations

---

## Application Security

### Code Obfuscation

**ProGuard Configuration** (Implemented):

```proguard
# Obfuscation
-dontskipnonpubliclibraryclasses
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Shrink code
-dontshrink

# Keep important classes
-keep public class com.learnhub.kenya.LearnHubApplication
-keep class com.learnhub.kenya.domain.model.** { *; }
-keep class com.learnhub.kenya.data.remote.dto.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}
```

**R8 Full Mode** (Planned):

```properties
# gradle.properties
android.enableR8.fullMode=true
```

### Root Detection (Planned)

**Implementation:**

```kotlin
object RootDetection {
    
    fun isDeviceRooted(): Boolean {
        return checkRootBinaries() || 
               checkRootPackages() || 
               checkSuperuserApk()
    }
    
    private fun checkRootBinaries(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        
        return paths.any { File(it).exists() }
    }
    
    private fun checkRootPackages(): Boolean {
        val packages = arrayOf(
            "com.noshufou.android.su",
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.zachspong.temprootremovejb",
            "com.ramdroid.appquarantine"
        )
        
        return try {
            packages.any {
                context.packageManager.getPackageInfo(it, 0)
                true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    
    private fun checkSuperuserApk(): Boolean {
        return try {
            Runtime.getRuntime().exec("su")
            true
        } catch (e: IOException) {
            false
        }
    }
}
```

### Secure Logging

**Current Implementation:**

```kotlin
// Only log in debug builds
if (BuildConfig.DEBUG) {
    Log.d("ContentViewModel", "Loading content for subtopic: $subtopicId")
}

// Never log sensitive data
// ❌ Bad
Log.d("AuthRepo", "User password: $password")

// ✅ Good
Log.d("AuthRepo", "Attempting login for user")
```

**Planned Enhancement:**

```kotlin
object SecureLogger {
    
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, sanitize(message))
        }
    }
    
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, sanitize(message), throwable)
        } else {
            // Send to crash reporting (without PII)
            Sentry.captureException(throwable)
        }
    }
    
    private fun sanitize(message: String): String {
        return message
            .replace(Regex("[0-9]{3}-[0-9]{2}-[0-9]{4}"), "[SSN_REDACTED]")  // SSN
            .replace(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"), "[EMAIL_REDACTED]")  // Email
            .replace(Regex("password|pwd|passwd|secret", RegexOption.IGNORE_CASE), "[REDACTED]")
    }
}
```

---

## Security Testing

### Testing Strategy

**Test Pyramid:**

```
                  ▲
                 / \
                /   \
               /  E2E \          (5 tests)
              /_______\
             /         \
            /  Security \       (20 tests)
           /   Testing   \
          /_______________\
         /                 \
        /   Unit Tests      \   (200 tests)
       /_____________________\
```

### Security Test Cases

#### ST-001: Authentication Security

```kotlin
@Test
fun `login with SQL injection attempt fails`() = runTest {
    // Given
    val maliciousEmail = "admin' OR '1'='1"
    val password = "password"
    
    // When
    val result = authRepository.login(maliciousEmail, password)
    
    // Then
    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is InvalidEmailException)
}

@Test
fun `weak password is rejected`() = runTest {
    // Given
    val email = "test@example.com"
    val weakPassword = "123"
    
    // When
    val result = authRepository.login(email, weakPassword)
    
    // Then
    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is WeakPasswordException)
}

@Test
fun `session token expires after timeout`() = runTest {
    // Given
    val token = "valid_token"
    val expiry = System.currentTimeMillis() - 1000  // Expired 1 second ago
    sessionManager.saveSession(token, expiry)
    
    // When
    val isValid = sessionManager.isSessionValid()
    
    // Then
    assertFalse(isValid)
    assertNull(sessionManager.getToken())
}
```

#### ST-002: Data Protection

```kotlin
@Test
fun `sensitive data is encrypted in storage`() {
    // Given
    val sensitiveData = "user_token_12345"
    val encryptedPrefs = SecurePreferences.getEncryptedPreferences(context)
    
    // When
    encryptedPrefs.edit {
        putString("token", sensitiveData)
    }
    
    // Then
    // Read raw SharedPreferences file (requires root access in test)
    val rawPrefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
    val rawValue = rawPrefs.getString("token", null)
    
    // Verify data is encrypted (not readable)
    assertNotEquals(sensitiveData, rawValue)
}

@Test
fun `passwords are never logged`() {
    // Given
    val logWatcher = TestLogWatcher()
    
    // When
    val password = "secretPassword123"
    viewModel.onPasswordChange(password)
    
    // Then
    assertFalse(logWatcher.logs.any { it.contains("secretPassword123") })
}
```

#### ST-003: Network Security

```kotlin
@Test
fun `API calls use HTTPS only`() {
    // Given
    val retrofit = provideRetrofit()
    
    // When
    val baseUrl = retrofit.baseUrl().toString()
    
    // Then
    assertTrue(baseUrl.startsWith("https://"))
}

@Test
fun `authentication header is added to requests`() {
    // Given
    val token = "test_token"
    sessionManager.saveSession(token, System.currentTimeMillis() + 10000)
    
    // When
    val interceptor = AuthInterceptor()
    val chain = mockk<Interceptor.Chain>()
    every { chain.request() } returns Request.Builder()
        .url("https://api.learnhub.ke/content")
        .build()
    
    // Capture the modified request
    val slot = slot<Request>()
    every { chain.proceed(capture(slot)) } returns mockk()
    
    interceptor.intercept(chain)
    
    // Then
    assertEquals("Bearer $token", slot.captured.header("Authorization"))
}
```

#### ST-004: Input Validation

```kotlin
@Test
fun `XSS attempt is sanitized`() {
    // Given
    val xssInput = "<script>alert('XSS')</script>"
    
    // When
    val sanitized = InputValidator.sanitizeText(xssInput)
    
    // Then
    assertFalse(sanitized.contains("<script>"))
    assertTrue(sanitized.contains("&lt;script&gt;"))
}

@Test
fun `SQL injection in search is blocked`() {
    // Given
    val sqlInjection = "'; DROP TABLE users; --"
    
    // When
    val isValid = InputValidator.validateSearchQuery(sqlInjection)
    
    // Then
    assertFalse(isValid)
}
```

### Penetration Testing (Planned)

**Scope:**
- Mobile application
- API endpoints
- Authentication mechanisms
- Data storage
- Network communications

**Schedule:**
- Initial: Q1 2025
- Ongoing: Quarterly
- After major releases

**Testing Areas:**
1. Authentication & Authorization
2. Session Management
3. Input Validation
4. Cryptography
5. Business Logic
6. Client-Side Controls
7. Data Validation
8. Error Handling

**Tools:**
- OWASP ZAP
- Burp Suite
- MobSF (Mobile Security Framework)
- Frida (dynamic instrumentation)

---

## Incident Response

### Incident Response Plan

**Phases:**

```
1. Preparation → 2. Detection → 3. Containment → 4. Eradication → 5. Recovery → 6. Lessons Learned
```

### Incident Classification

| Severity | Definition | Response Time | Examples |
|----------|-----------|---------------|----------|
| **Critical** | Massive data breach, complete service down | < 1 hour | Database compromised, credentials leaked |
| **High** | Significant data exposure, major service disruption | < 4 hours | API key leaked, extended downtime |
| **Medium** | Limited data exposure, partial service issues | < 24 hours | Minor vulnerability exploited |
| **Low** | No data exposure, minimal impact | < 72 hours | Failed login attempts, suspicious activity |

### Incident Response Team

| Role | Responsibility | Contact |
|------|---------------|---------|
| **Incident Commander** | Overall coordination | CTO |
| **Technical Lead** | Technical investigation | Lead Developer |
| **Communications Lead** | Stakeholder communication | CEO |
| **Legal Counsel** | Legal compliance | Legal Advisor |
| **Security Analyst** | Forensics and analysis | Security Consultant (contracted) |

### Response Procedures

#### Critical Incident Response

**Example: Database Breach**

**1. Immediate Actions (< 1 hour):**
- [ ] Isolate affected systems
- [ ] Revoke all active sessions
- [ ] Enable maintenance mode
- [ ] Notify incident response team
- [ ] Begin forensics (preserve logs)

**2. Containment (< 4 hours):**
- [ ] Identify breach scope
- [ ] Block attacker access
- [ ] Patch vulnerabilities
- [ ] Change all credentials
- [ ] Notify affected users

**3. Eradication (< 24 hours):**
- [ ] Remove attacker artifacts
- [ ] Apply security patches
- [ ] Restore from clean backup
- [ ] Verify system integrity

**4. Recovery (< 48 hours):**
- [ ] Gradual service restoration
- [ ] Monitor for anomalies
- [ ] Implement additional controls
- [ ] Update security measures

**5. Post-Incident (< 1 week):**
- [ ] Conduct post-mortem
- [ ] Document lessons learned
- [ ] Update security policies
- [ ] Train team on improvements
- [ ] File regulatory reports (if required)

### Communication Plan

**Internal Communication:**
- Incident Commander → CEO (immediate)
- CEO → Board (within 4 hours for critical)
- Team notification (via Slack)

**External Communication:**
- Affected users (email, in-app)
- Media (if public interest)
- Regulatory bodies (if required by law)
- Partners (if impacted)

**Communication Template:**

```
Subject: Security Incident Notification - LearnHub Kenya

Dear [User/Stakeholder],

We are writing to inform you of a security incident that may have 
affected your LearnHub Kenya account.

WHAT HAPPENED:
[Brief description of incident]

WHAT INFORMATION WAS INVOLVED:
[Specific data types affected]

WHAT WE ARE DOING:
[Actions taken to secure systems]

WHAT YOU SHOULD DO:
[Recommended user actions]

FOR MORE INFORMATION:
[Contact details, FAQ link]

We sincerely apologize for this incident and are committed to 
protecting your data.

Sincerely,
LearnHub Kenya Security Team
```

---

## Compliance & Regulations

### Regulatory Requirements

#### GDPR (General Data Protection Regulation)

**Applicability**: Applies if users in EU

**Requirements:**
- [ ] Lawful basis for processing
- [ ] User consent (opt-in)
- [ ] Data minimization
- [ ] Right to access
- [ ] Right to erasure
- [ ] Data portability
- [ ] Privacy by design
- [ ] Data breach notification (72 hours)

**Implementation:**
- Privacy Policy: https://www.learnhub.ke/privacy
- User consent flow on registration
- Data export feature (planned)
- Account deletion feature (planned)
- DPO (Data Protection Officer) appointed

---

#### COPPA (Children's Online Privacy Protection Act)

**Applicability**: Users may be under 18

**Requirements:**
- [ ] Parental consent for < 13 (not applicable in Kenya, but good practice)
- [ ] Limited data collection
- [ ] No behavioral advertising to children
- [ ] Secure data handling

**Implementation:**
- Age verification on registration (planned)
- Minimal data collection from students
- No advertising currently
- COPPA-compliant privacy policy

---

#### Kenyan Data Protection Act (2019)

**Applicability**: All operations in Kenya

**Requirements:**
- [ ] Register as data controller
- [ ] Lawful processing of personal data
- [ ] Data subject rights
- [ ] Data breach notification
- [ ] Security safeguards
- [ ] Cross-border data transfer controls

**Implementation:**
- Registration with Data Commissioner (in progress)
- Privacy Policy compliant with DPA
- Data retention policy
- Security measures documented
- User rights mechanisms (planned)

---

### Compliance Checklist

**Pre-Launch:**
- [x] Privacy Policy created
- [x] Terms of Service created
- [ ] Data Protection Impact Assessment (DPIA) completed
- [ ] Register with Data Commissioner
- [ ] Cookie policy (if applicable)
- [ ] Age verification mechanism

**Ongoing:**
- [ ] Annual privacy policy review
- [ ] Quarterly security audits
- [ ] User consent management
- [ ] Data retention compliance
- [ ] Vendor assessment (third-parties)
- [ ] Employee training on data protection

---

## Security Best Practices

### Development Best Practices

**Secure Coding:**

1. **Never Trust User Input**
   ```kotlin
   // ❌ Bad
   val query = "SELECT * FROM users WHERE email = '$email'"
   
   // ✅ Good
   @Query("SELECT * FROM users WHERE email = :email")
   suspend fun getUserByEmail(email: String): User?
   ```

2. **Use Parameterized Queries**
   ```kotlin
   // Always use Room's parameterized queries
   @Query("SELECT * FROM content WHERE subtopicId = :subtopicId")
   suspend fun getContent(subtopicId: String): List<ContentEntity>
   ```

3. **Validate All Input**
   ```kotlin
   fun validateInput(text: String): Boolean {
       return text.length <= MAX_LENGTH &&
              !text.contains(Regex("[<>\"'`]")) &&
              text.isNotBlank()
   }
   ```

4. **Handle Errors Securely**
   ```kotlin
   // ❌ Bad - reveals internal details
   catch (e: SQLException) {
       throw Exception("Database error: ${e.message}")
   }
   
   // ✅ Good - generic message, log details internally
   catch (e: SQLException) {
       Logger.error("Database error", e)
       throw Exception("An error occurred. Please try again.")
   }
   ```

5. **Use Strong Cryptography**
   ```kotlin
   // ✅ Use Android Keystore
   val masterKey = MasterKey.Builder(context)
       .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
       .build()
   ```

### Operational Best Practices

1. **Regular Updates**
    - Update dependencies monthly
    - Apply security patches immediately
    - Monitor CVE databases

2. **Access Control**
    - Principle of least privilege
    - Separate production/development access
    - Use SSH keys (no passwords)
    - MFA for all admin accounts

3. **Monitoring**
    - Log all security events
    - Monitor for anomalies
    - Set up alerts for suspicious activity
    - Regular log reviews

4. **Backups**
    - Automated daily backups
    - Encrypted backup storage
    - Test restore procedures monthly
    - Off-site backup storage

5. **Vendor Management**
    - Assess third-party security
    - Review vendor agreements
    - Monitor vendor incidents
    - Have contingency plans

### User Education

**Topics:**
- Strong password practices
- Phishing awareness
- Device security
- Public Wi-Fi risks
- Two-factor authentication
- Recognizing suspicious activity

**Delivery:**
- In-app security tips
- Email newsletters
- Blog posts
- Video tutorials

---

## Security Roadmap

### Q1 2025 (High Priority)

- [ ] Implement JWT authentication
- [ ] Add rate limiting (backend)
- [ ] Certificate pinning
- [ ] Enhanced input validation
- [ ] Security audit (external)
- [ ] Penetration testing
- [ ] Implement MFA (optional)
- [ ] Root detection
- [ ] Encrypted database (SQLCipher)

### Q2 2025 (Medium Priority)

- [ ] Bug bounty program
- [ ] Security training for team
- [ ] Automated security scanning (CI/CD)
- [ ] SIEM integration
- [ ] Enhanced logging and monitoring
- [ ] Incident response drills
- [ ] Security metrics dashboard

### Q3 2025 (Future Enhancements)

- [ ] Biometric authentication
- [ ] Advanced threat protection
- [ ] Security certifications (ISO 27001)
- [ ] Compliance audits
- [ ] Security awareness program for users

---

## Appendix

### Security Contacts

**Internal:**
- Security Lead: security@learnhub.ke
- CTO: cto@learnhub.ke
- Incident Response: incident@learnhub.ke

**External:**
- Security Researcher Contact: security@learnhub.ke
- Bug Bounty (planned): bugbounty@learnhub.ke

### Responsible Disclosure

**Policy:**

We welcome security researchers to report vulnerabilities responsibly.

**Process:**
1. Email: security@learnhub.ke
2. Include: Description, impact, reproduction steps
3. Allow: 90 days for fix before public disclosure
4. Recognition: Hall of Fame (with permission)
5. Reward: Bug bounty program (coming Q2 2025)

**Rules:**
- No social engineering
- No DDoS attacks
- No data theft
- No public disclosure before fix

### Security Resources

**Standards:**
- OWASP Mobile Security: https://owasp.org/www-project-mobile-security/
- OWASP Top 10: https://owasp.org/www-project-top-ten/
- Android Security: https://source.android.com/security

**Tools:**
- MobSF: https://github.com/MobSF/Mobile-Security-Framework-MobSF
- OWASP ZAP: https://www.zaproxy.org/
- Burp Suite: https://portswigger.net/burp

**Training:**
- OWASP Mobile Security Testing Guide
- Android Security Best Practices
- Secure Coding Guidelines

---

**Document Version**: 1.0  
**Last Updated**: December 2024  
**Status**: Living Document  
**Next Review**: March 2025  
**Classification**: Internal Use Only
```

---

**That's Document 10 of 12 complete!** 🔒

**Progress: 10/12 (83% complete)**

Completed:
1. ✅ Project Overview & Requirements
2. ✅ Architecture & Design
3. ✅ API Documentation
4. ✅ Code Documentation
5. ✅ Test Documentation
6. ✅ User Documentation
7. ✅ Deployment Documentation
8. ✅ Version Control & Release Notes
9. ✅ Project Plans & Management
10. ✅ Security Documentation ← **JUST COMPLETED**

**Remaining: 2 documents (11-12)**

Should I continue with **Document 11: Operations & Maintenance** and then **Document 12: Compliance Documentation** to complete the suite?

Let me know! 🚀📚🔐