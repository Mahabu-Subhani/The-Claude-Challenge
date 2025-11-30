# GridZero Testing Guide

## Pre-Build Checklist

Before running GridZero, ensure:

- ✅ Android Studio installed
- ✅ JDK 17+ configured
- ✅ Android SDK API 24+ available
- ✅ 2GB+ RAM available on test device
- ✅ 500MB+ free storage

## Building the Project

### Option 1: Android Studio (Recommended)

1. Open Android Studio
2. File → Open → Select project directory
3. Wait for Gradle sync (first sync may take 2-3 minutes)
4. Build → Make Project (Ctrl+F9)
5. Run → Run 'app' (Shift+F10)

### Option 2: Command Line

```bash
# Windows (PowerShell)
cd "path/to/The-Claude-Challenge"
./gradlew.bat assembleDebug

# Mac/Linux
cd path/to/The-Claude-Challenge
./gradlew assembleDebug
```

## Testing Scenarios

### Test 1: Basic Initialization

**Expected:** App launches with dark tactical theme

1. Launch app
2. **Verify:** "GRIDZERO" title in terminal green
3. **Verify:** "Offline Tactical Intelligence" subtitle
4. **Verify:** Status shows "GRIDZERO READY - Load tactical AI model"
5. **Verify:** Model selector icon (shield) is orange/warning color

**✅ Pass Criteria:** App loads without crashes, UI is fully visible

---

### Test 2: Model Download & Load

**Expected:** Successfully download and load AI model

1. Tap shield icon (⚙️) in top right
2. **Verify:** Model selector opens with "Qwen 2.5 0.5B Instruct Q6_K"
3. Tap "Download" button
4. **Verify:** Progress bar appears showing 0-100%
5. **Wait:** 2-3 minutes for download (374 MB)
6. **Verify:** Button changes to "✓ Downloaded"
7. Tap "Load" button
8. **Verify:** "Loading tactical AI..." message appears
9. **Wait:** 10-15 seconds
10. **Verify:** Status changes to "OPERATIONAL - GridZero AI Online"
11. **Verify:** Shield icon turns green (checkmark)

**✅ Pass Criteria:** Model loads successfully, status shows operational

---

### Test 3: Field Report Analysis - Structural Collapse

**Input:** Critical incident with casualties

**Test Report:**

```
This is unit 4, we have a structural collapse at the library, 
severe flooding, three civilians trapped, send heavy lift gear immediately!
```

**Steps:**

1. Type/paste report in input field
2. Tap "TRANSMIT REPORT"
3. **Verify:** Button shows "ANALYZING..." with spinner
4. **Wait:** 3-5 seconds
5. **Verify:** Incident card appears in feed

**Expected Output:**

- 🔴 **RED CARD** (Critical severity)
- **Location:** "Library"
- **Type:** "COLLAPSE" or "STRUCTURAL COLLAPSE"
- **Casualties:** 3
- **Resources:** Contains "Crane" or "Heavy Lift", "Medics" or "Rescue"
- **Sentiment:** "Urgent"
- **Visual:** Card pulses (animated)

**✅ Pass Criteria:**

- Correct severity (Critical/Red)
- Location extracted accurately
- Casualties count = 3
- At least 2 relevant resources

---

### Test 4: Field Report Analysis - Medical Emergency

**Input:** Multiple casualties, panic situation

**Test Report:**

```
This is medic team 2, multiple casualties at North Hospital, 
approximately 15 injured, need ambulances and medical supplies ASAP, 
situation is chaotic and people are panicking!
```

**Expected Output:**

- 🔴 **RED CARD** (Critical severity - multiple casualties)
- **Location:** "North Hospital"
- **Type:** "MEDICAL" or "MEDICAL EMERGENCY"
- **Casualties:** 15 (or close estimate)
- **Resources:** Contains "Ambulance", "Medical Supplies", or "Medics"
- **Sentiment:** "Panic" or "Urgent"

**✅ Pass Criteria:**

- Severity reflects urgency (Critical/Red or Moderate/Yellow)
- Casualties ~15
- Medical resources identified

---

### Test 5: Field Report Analysis - Fire Incident

**Input:** Active fire, spreading

**Test Report:**

```
Fire at downtown sector, building fully engulfed, flames spreading to 
adjacent structures, need all available fire teams immediately!
```

**Expected Output:**

- 🔴 **RED CARD** (Critical - spreading fire)
- **Location:** "Downtown Sector" or "Downtown"
- **Type:** "FIRE"
- **Casualties:** 0 (none mentioned)
- **Resources:** Contains "Firefighters", "Fire Teams", or "Fire"
- **Sentiment:** "Urgent"

**✅ Pass Criteria:**

- Incident type = Fire
- Resources appropriate for fire
- Critical severity (spreading situation)

---

### Test 6: Field Report Analysis - Civil Unrest

**Input:** Low-moderate severity, crowd control

**Test Report:**

```
Civil unrest at city hall, large crowd estimated 200 people, 
situation under control but we need police backup for crowd management.
```

**Expected Output:**

- 🟡 **YELLOW CARD** (Moderate) or 🟢 **TEAL** (Low)
- **Location:** "City Hall"
- **Type:** "CIVIL UNREST" or "CIVILUNREST"
- **Casualties:** 0
- **Resources:** Contains "Police", "Crowd Control", or "Backup"
- **Sentiment:** "Calm" or "Urgent"

**✅ Pass Criteria:**

- NOT critical severity
- Type indicates civil/unrest
- Police resources identified

---

### Test 7: Multiple Incidents Management

**Expected:** Dashboard handles multiple active incidents

**Steps:**

1. Submit Test Report 3 (Structural Collapse)
2. Wait for extraction
3. Submit Test Report 4 (Medical Emergency)
4. Wait for extraction
5. Submit Test Report 5 (Fire)
6. Wait for extraction

**Verify:**

- ✅ All 3 incidents appear in feed (most recent at top)
- ✅ Each has correct color coding
- ✅ Critical incidents (red) are pulsing
- ✅ Scrolling works smoothly
- ✅ All details are readable

**✅ Pass Criteria:** All incidents visible and distinguishable

---

### Test 8: Bandwidth Savings Tracking

**Expected:** System tracks compression savings

**Steps:**

1. Clear all reports (trash icon)
2. Submit a long field report (>500 characters)
3. **Verify:** Top status bar shows "📡 [X]KB saved"
4. Submit another report
5. **Verify:** Savings counter increases

**✅ Pass Criteria:** Bandwidth savings display and increment correctly

---

### Test 9: Error Handling - Invalid Input

**Expected:** Graceful handling of unclear input

**Test Report:**

```
Hello, just checking in, everything is fine here.
```

**Expected Behavior:**

- Either extracts minimal incident (type: "General", severity: "Low")
- OR shows error message with retry option
- Does NOT crash the app

**✅ Pass Criteria:** No crashes, graceful degradation

---

### Test 10: Demo Mode

**Expected:** Demo instructions help new users

**Steps:**

1. Tap info icon (ℹ️) in top bar
2. **Verify:** Demo panel opens with example reports
3. **Verify:** Examples are readable and helpful
4. Tap info icon again
5. **Verify:** Demo panel closes

**✅ Pass Criteria:** Demo toggle works, examples visible

---

### Test 11: Clear Reports Function

**Expected:** Can clear tactical feed

**Steps:**

1. Ensure 2+ incidents in feed
2. Tap trash icon (🗑️) in top bar
3. **Verify:** All incidents disappear
4. **Verify:** Empty state appears: "NO ACTIVE INCIDENTS"
5. **Verify:** Bandwidth counter resets to 0

**✅ Pass Criteria:** Feed clears completely, UI shows empty state

---

### Test 12: Model Selector Toggle

**Expected:** Can open/close model selector

**Steps:**

1. Tap shield icon
2. **Verify:** Model selector opens
3. Tap "X" close button
4. **Verify:** Model selector closes
5. Tap shield icon again
6. Tap "Refresh" button
7. **Verify:** Models refresh without errors

**✅ Pass Criteria:** Selector opens/closes smoothly

---

### Test 13: Offline Functionality

**Expected:** Works without internet (after model download)

**Steps:**

1. Ensure model is downloaded and loaded
2. Enable airplane mode on device
3. Submit field report
4. **Verify:** Analysis completes successfully
5. **Verify:** Incident card appears normally

**✅ Pass Criteria:** Full functionality in airplane mode

---

### Test 14: App Lifecycle Handling

**Expected:** Proper state management across lifecycle

**Steps:**

1. Submit 2-3 field reports
2. Press home button (backgrounding app)
3. Wait 30 seconds
4. Reopen app
5. **Verify:** Incidents are still visible
6. **Verify:** Can submit new reports
7. (Optional) Reload model if needed

**✅ Pass Criteria:** State persists across backgrounding

---

### Test 15: Stress Test - Rapid Submission

**Expected:** Handles multiple rapid submissions

**Steps:**

1. Prepare 3 short reports in clipboard/notes
2. Paste and submit first report
3. Immediately paste and submit second report
4. Immediately paste and submit third report

**Verify:**

- ✅ All 3 reports are queued/processed
- ✅ No crashes or freezes
- ✅ All incidents appear (may be sequential)

**✅ Pass Criteria:** System handles queue gracefully

---

## Performance Benchmarks

### Expected Performance

| Metric | Target | Notes |
|--------|--------|-------|
| App Launch | < 3s | Cold start to UI visible |
| Model Load | 10-20s | Qwen 0.5B on mid-range device |
| Report Analysis | 3-8s | Depends on device CPU |
| UI Responsiveness | 60fps | Smooth scrolling and animations |
| Memory Usage | < 1.5GB | With model loaded |

### Device Requirements

- **Minimum:** Android 7.0 (API 24), 2GB RAM, ARM64
- **Recommended:** Android 10+, 4GB RAM, modern ARM64 CPU
- **Optimal:** Android 12+, 6GB+ RAM, flagship ARM64 CPU

---

## Known Issues & Limitations

### Expected Behaviors (Not Bugs)

1. **First model load is slow:** 10-20 seconds is normal
2. **Download takes time:** 374 MB = 2-3 minutes on normal WiFi
3. **JSON parsing may fail:** AI outputs vary, retry logic handles this
4. **Memory intensive:** LLMs require significant RAM
5. **ARM64 only:** Won't work on x86 emulators without proper config

### Workarounds

- **Slow analysis:** Use smaller model (SmolLM2 360M)
- **Out of memory:** Close other apps, use smaller model
- **Parsing errors:** System auto-retries with simpler prompt
- **Download fails:** Check internet, retry download

---

## Success Criteria Summary

For a successful demo/test, verify:

1. ✅ App launches without crashes
2. ✅ Model downloads and loads successfully
3. ✅ Field reports are analyzed and extracted
4. ✅ Incident cards display with correct data
5. ✅ Color coding matches severity (Red/Yellow/Teal)
6. ✅ Critical incidents pulse (animation)
7. ✅ Multiple incidents can coexist
8. ✅ Bandwidth savings are tracked
9. ✅ Works offline (after model download)
10. ✅ UI is responsive and polished

---

## Demo Preparation Checklist

**Before the presentation:**

- [ ] Model pre-downloaded and loaded
- [ ] Test all 4 demo reports
- [ ] Verify color coding is visible
- [ ] Clear old reports for clean demo
- [ ] Device brightness at 100%
- [ ] Disable notifications/interruptions
- [ ] Airplane mode ready (for offline demo)
- [ ] Bandwidth counter shows impressive savings
- [ ] Screenshots/screen recording ready

**During demo:**

1. Show empty tactical feed (clean slate)
2. Submit structural collapse report
3. Highlight red pulsing card, extracted data
4. Submit 2-3 more diverse reports rapidly
5. Show full feed with color coding
6. Highlight bandwidth savings metric
7. Toggle airplane mode, submit final report
8. Emphasize: "100% offline, 0.01 second transmission"

---

## Troubleshooting

### App won't launch

- Check Android version (API 24+)
- Verify device has ARM64 architecture
- Check available RAM (2GB minimum)

### Model won't download

- Verify internet connection
- Check available storage (500MB+)
- Try refreshing model list
- Check HuggingFace is accessible

### Analysis fails or crashes

- Check model is loaded (shield icon green)
- Try shorter input text
- Check available memory
- Try reloading model

### UI issues

- Rotate device and back
- Clear app cache
- Reinstall app

---

## Feedback & Iteration

After testing, document:

- Response accuracy
- UI clarity
- Performance metrics
- User confusion points
- Feature requests

Use feedback to improve prompt engineering and UI polish.

---

**Good luck with GridZero! 🚀**
