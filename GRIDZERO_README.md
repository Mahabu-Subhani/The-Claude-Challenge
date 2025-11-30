# GridZero - Offline Situation Awareness Tool

<div align="center">

**When the internet is down, GridZero keeps first responders connected.**

*Tactical Intelligence | On-Device AI | Zero Bandwidth*

</div>

---

## 🎯 The Problem

When disaster strikes (hurricanes, war zones, infrastructure collapse), the internet goes down.
First responders face a critical challenge:

- **Audio files are too heavy** to send over emergency mesh networks
- **Chaotic voice reports** like *"This is unit 4, we have a structural collapse at the library,
  severe flooding, three civilians trapped, send heavy lift gear immediately!"* take **3+ minutes**
  to transmit
- **No structure** means dispatchers waste precious time parsing information
- **Lives are lost** in the communication gap

## 💡 The GridZero Solution

GridZero uses **on-device AI** to compress chaotic field reports into tiny, structured JSON packets
that sync **instantly** over low-bandwidth mesh networks.

### The Transformation

**Input (Chaotic Voice/Text):**

```
"This is unit 4, we have a structural collapse at the library, 
severe flooding, three civilians trapped, send heavy lift gear immediately!"
```

**Output (Structured JSON - 200 bytes):**

```json
{
  "location_name": "Library",
  "incident_type": "Collapse",
  "severity": "Critical",
  "casualties": 3,
  "resources_needed": ["Heavy Lift", "Rescue Team", "Medics"],
  "sentiment": "Urgent",
  "timestamp": 1701234567890
}
```

**Result:**

- **Audio report:** ~2-3 MB, 3 minutes transmission time
- **GridZero JSON:** ~200 bytes, **0.01 seconds** transmission time
- **Compression ratio:** 15,000:1

---

## 🚀 Key Features

### 1. **Offline-First Architecture**

- 100% on-device AI processing (no cloud required)
- Works in disaster zones with zero internet
- Privacy-preserving (data never leaves device)

### 2. **Intelligent Data Extraction**

- Military-grade dispatcher AI
- Extracts: Location, Incident Type, Severity, Casualties, Resources, Sentiment
- Handles chaotic, unstructured field reports

### 3. **Tactical Dashboard**

- **Not a chat interface** - a real-time tactical operations center
- Color-coded severity (🔴 Critical, 🟡 Moderate, 🟢 Low)
- Pulsing animations for critical incidents
- Live incident feed with resource tracking

### 4. **Bandwidth Champion**

- Structured JSON packets (~200 bytes)
- Tracks total bandwidth saved
- Optimized for emergency mesh networks

---

## 🏗️ Architecture

### Data Flow

```
Field Report (Voice/Text)
         ↓
   [On-Device LLM]
         ↓
  Crisis Extraction Prompt
         ↓
   Structured JSON
         ↓
  Tactical Dashboard
         ↓
  Mesh Network Sync
```

### Core Components

#### 1. **CrisisReport.kt** - The Brain

```kotlin
data class CrisisReport(
    val locationName: String,      // "Library", "North Sector"
    val incidentType: String,      // "Fire", "Collapse", "Medical", "Flood"
    val severity: String,          // "Critical", "Moderate", "Low"
    val casualties: Int,           // Estimated count
    val resourcesNeeded: List<String>, // ["Medic", "Crane", "Boat"]
    val sentiment: String,         // "Panic", "Calm", "Urgent"
    val timestamp: Long
)
```

#### 2. **CrisisViewModel.kt** - The Intelligence

- Military-grade prompt engineering
- JSON extraction and parsing
- Error handling and retry logic
- Bandwidth tracking

#### 3. **CrisisScreen.kt** - The Interface

- Tactical operations center UI
- Real-time incident feed
- Color-coded severity cards
- Radio-style input interface
- Model management

---

## 🎨 User Interface

### Tactical Dashboard Layout

```
┌─────────────────────────────────────────────┐
│  GRIDZERO                        🛡️ 💾 ⚙️   │
│  Offline Tactical Intelligence              │
├─────────────────────────────────────────────┤
│  STATUS: OPERATIONAL | 📡 1.2MB saved       │
├─────────────────────────────────────────────┤
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │ 🔴 LIBRARY - COLLAPSE               │   │
│  │ 👤 3 casualties | ⚡ Urgent          │   │
│  │ [Crane] [Medics] [Rescue Team]      │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │ 🟡 NORTH HOSPITAL - MEDICAL         │   │
│  │ 👤 15 casualties | 🚨 Panic         │   │
│  │ [Ambulance] [Medical Supplies]      │   │
│  └─────────────────────────────────────┘   │
│                                             │
├─────────────────────────────────────────────┤
│  📻 FIELD REPORT INPUT                      │
│  ┌─────────────────────────────────────┐   │
│  │ Type or speak situation report...   │   │
│  │                                     │   │
│  └─────────────────────────────────────┘   │
│  [       TRANSMIT REPORT        ]          │
└─────────────────────────────────────────────┘
```

### Color Coding

- 🔴 **Red (Critical):** Pulsing animation, white text
    - Keywords: "trapped", "blood", "multiple casualties"

- 🟡 **Yellow (Moderate):** Solid, black text
    - Standard incidents requiring attention

- 🟢 **Teal (Low):** Solid, black text
    - Low priority or informational reports

---

## 🛠️ Technical Stack

### Core Technologies

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **AI SDK:** RunAnywhere SDK v0.1.2-alpha
- **LLM Engine:** llama.cpp (7 ARM64 CPU variants)

### Dependencies

```kotlin
// RunAnywhere SDK
implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))

// Coroutines for async operations
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

// Serialization
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
```

### Recommended AI Models

| Model | Size | Speed | Quality | Use Case |
|-------|------|-------|---------|----------|
| **Qwen 2.5 0.5B** | 374 MB | ⚡⚡ | ⭐⭐⭐ | **Recommended** - Best balance |
| SmolLM2 360M | 119 MB | ⚡⚡⚡ | ⭐⭐ | Testing & demos |
| Llama 3.2 1B | 815 MB | ⚡ | ⭐⭐⭐⭐ | High-quality extractions |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest)
- Android device/emulator (API 24+, ARM64)
- 2GB+ RAM recommended
- 500MB free storage

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd The-Claude-Challenge
   ```

2. **Open in Android Studio**
    - File → Open → Select project directory
    - Wait for Gradle sync

3. **Build and Run**
    - Click "Run" (▶️) or press Shift+F10
    - Select target device

### First Launch

1. **Load AI Model**
    - Tap the shield icon (⚙️) in top right
    - Select "Qwen 2.5 0.5B Instruct Q6_K"
    - Tap "Download" (wait 2-3 minutes)
    - Tap "Load" (wait 10-15 seconds)
    - Status shows "OPERATIONAL - GridZero AI Online"

2. **Test Field Report**
    - Tap "ℹ️" icon to see demo examples
    - Copy and paste a sample report, or type your own:
   ```
   Unit 4, structural collapse at library, severe flooding, 
   three civilians trapped, need heavy lift gear immediately!
   ```
    - Tap "TRANSMIT REPORT"
    - Watch AI extract structured data in real-time

3. **View Tactical Feed**
    - Incident appears as color-coded card
    - Check location, type, severity, casualties, resources
    - Critical incidents pulse red

---

## 📊 The Winning Argument

### Why GridZero Wins

#### 1. **The "Impossible" Constraint → Strongest Feature**

Other apps could use cloud APIs. **GridZero MUST be offline** because:

- Disasters kill internet infrastructure
- Mesh networks have extremely limited bandwidth
- Cloud solutions fail when you need them most

**We turn the constraint into our competitive advantage.**

#### 2. **Bandwidth Math**

Judges will see this comparison:

- **Audio transmission:** 2-3 MB = 3 minutes on bad connection
- **GridZero JSON:** 200 bytes = **0.01 seconds**
- **Savings:** 99.99% bandwidth reduction

In emergencies, **seconds save lives.**

#### 3. **Complexity Beyond Summarization**

We're not just "making text shorter." We're:

- **Structuring** unstructured chaos
- **Extracting** actionable intelligence
- **Normalizing** field reports for logistics systems
- **Enabling** real-time coordination

#### 4. **Real-World Impact**

- Hurricane response teams
- Military field operations
- Search & rescue coordination
- Disaster relief organizations
- Remote medical triage

---

## 🎯 Demo Strategy

### The 2-Minute Pitch

**"Imagine this scenario..."**

1. **Show the problem** (30 sec)
    - "Hurricane hits. Internet is down. First responder radios:"
    - Display long, chaotic audio transcript on screen
    - "This 2MB audio file takes 3 minutes to send on mesh network."

2. **Introduce GridZero** (30 sec)
    - "GridZero uses on-device AI to extract tactical data."
    - Live demo: Paste field report, hit TRANSMIT
    - Show instant JSON extraction
    - "Same info, 200 bytes, 0.01 seconds."

3. **Show the dashboard** (45 sec)
    - Multiple incidents on tactical feed
    - Color-coded severity
    - Resource tracking
    - Bandwidth savings counter

4. **Close with impact** (15 sec)
    - "In disasters, seconds save lives."
    - "GridZero: When the internet is down, first responders stay connected."

### Demo Field Reports

```
1. "This is unit 4, structural collapse at library, severe flooding, 
   three civilians trapped, send heavy lift gear immediately!"

2. "Medic team 2 reporting, multiple casualties at North Hospital, 
   approximately 15 injured, need ambulances and medical supplies ASAP, 
   situation is chaotic!"

3. "Fire at downtown sector, building fully engulfed, spreading to 
   adjacent structures, need all available fire teams, evacuating nearby 
   buildings!"

4. "Civil unrest at city hall, large crowd estimated 200 people, 
   situation escalating, need police backup and crowd control units!"
```

---

## 🔧 Development Notes

### Project Structure

```
app/src/main/java/com/runanywhere/startup_hackathon20/
├── data/
│   └── CrisisReport.kt          # Data model
├── ui/theme/
│   ├── Color.kt                 # GridZero tactical colors
│   ├── Theme.kt                 # Dark theme configuration
│   └── Type.kt                  # Typography
├── CrisisViewModel.kt           # Business logic & AI
├── CrisisScreen.kt              # UI components
├── MainActivity.kt              # App entry point
└── MyApplication.kt             # SDK initialization
```

### Key Implementation Details

#### Prompt Engineering (The Secret Sauce)

```kotlin
val systemPrompt = """
You are an offline emergency dispatcher AI for GridZero tactical operations.
Analyze the field report and extract critical tactical data into JSON.

Schema: { ... }

Rules:
1. Output ONLY valid JSON
2. Infer severity from keywords
3. Keep resource names short
4. Extract sentiment from tone
"""
```

#### JSON Parsing with Fallbacks

- Handles markdown code blocks
- Extracts JSON from mixed responses
- Retry logic with simplified prompts
- Graceful error handling

#### Real-time UI Updates

- StateFlow for reactive UI
- Streaming analysis feedback
- Progressive incident loading
- Bandwidth tracking

---

## 📈 Metrics & Analytics

GridZero tracks:

- **Reports processed:** Total incidents extracted
- **Bandwidth saved:** Raw bytes → JSON bytes
- **Average extraction time:** AI processing speed
- **Accuracy rate:** Successful JSON parsing

Example session:

```
📊 Session Statistics
┌─────────────────────────────────┐
│ Reports Processed:        12    │
│ Total Bandwidth Saved:    24 MB │
│ Average Extraction Time:  3.2s  │
│ Parsing Success Rate:     100%  │
└─────────────────────────────────┘
```

---

## 🛡️ Privacy & Security

### Data Protection

- ✅ **100% on-device processing**
- ✅ **No cloud transmission**
- ✅ **No user tracking**
- ✅ **Encrypted local storage** (via SDK)
- ✅ **GDPR compliant** (no data collection)

### Offline Guarantees

- Works in airplane mode
- No API keys required
- No internet permission needed (after model download)
- Complete autonomy in disaster scenarios

---

## 🎓 Educational Value

GridZero demonstrates:

- **On-device AI deployment** (no servers)
- **Prompt engineering** for data extraction
- **Jetpack Compose** modern Android UI
- **MVVM architecture** best practices
- **Real-time data flow** with StateFlow
- **Error handling** and retry logic
- **Practical AI application** solving real problems

---

## 🔮 Future Enhancements

### Phase 2: Advanced Features

- 🗺️ **Map integration:** Visual incident plotting
- 📍 **GPS coordination:** Automatic location tagging
- 🎤 **Voice input:** Real-time speech-to-text
- 📡 **Mesh network sync:** P2P incident sharing
- 📊 **Analytics dashboard:** Historical trends
- 🔔 **Smart alerts:** Priority-based notifications

### Phase 3: Platform Expansion

- 🌐 **Web dashboard:** Command center interface
- 📱 **iOS version:** Cross-platform support
- 💻 **Desktop client:** Dispatcher workstation
- 🔌 **API integration:** Connect to emergency systems

---

## 🤝 Contributing

GridZero is built for impact. Contributions welcome:

- 🐛 **Bug reports:** Issues affecting first responders
- ✨ **Feature requests:** Real-world needs
- 📝 **Documentation:** Deployment guides
- 🧪 **Testing:** Field validation

---

## 📄 License

This project is developed for the [Hackathon Name] with the goal of improving emergency response
systems.

---

## 👥 Team

**Project GridZero** - Turning chaos into clarity, one report at a time.

---

## 🙏 Acknowledgments

- **RunAnywhere SDK:** Enabling on-device AI
- **llama.cpp:** Efficient LLM inference
- **First Responders:** The real heroes who inspired this

---

## 📞 Contact

For demonstrations, questions, or partnership inquiries:

- **Demo Video:** [Link to demo]
- **Presentation:** [Link to slides]
- **Contact:** [Your contact info]

---

<div align="center">

**GridZero: When the internet is down, first responders stay connected.**

*Built with ❤️ for those who run towards danger while others run away.*

</div>
