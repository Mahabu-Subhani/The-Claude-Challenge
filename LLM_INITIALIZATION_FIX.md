# GridZero: LLM Initialization Fix Guide

## 🔧 Problem: "LLM component not initialized"

This error occurs when trying to analyze reports before the AI model is properly loaded into memory.

---

## ✅ **SOLUTION - Follow These Exact Steps:**

### **Step 1: Launch the App**

- Open GridZero
- Wait **5-10 seconds** for SDK initialization
- You'll see: "GRIDZERO READY - Load tactical AI model"

### **Step 2: Open Model Settings**

- Look at **top-right corner** of screen
- Tap the **⚙️ icon** (or CheckCircle icon)
- Model selector panel will slide down

### **Step 3: Download Model (First Time Only)**

You should see:

```
TACTICAL AI SYSTEMS

Qwen 2.5 0.5B Instruct Q6_K
[  DOWNLOAD  ] [   LOAD   ]
```

**If you see "DOWNLOAD" button:**

1. Tap the blue **DOWNLOAD** button
2. Wait 2-3 minutes (downloading 374 MB)
3. Watch progress bar fill up
4. **Do NOT close the app during download!**
5. Button will change to **"✓ READY"**

**If you see "✓ READY" button:**

- Model is already downloaded, skip to Step 4

### **Step 4: Load the Model**

**This is the CRITICAL step!**

1. Tap the red **LOAD** button
2. You'll see: "Loading tactical AI..."
3. Wait **10-15 seconds** (don't tap anything!)
4. Status will change to: **"OPERATIONAL - GridZero AI Online"**
5. Model card will show: **"✓ OPERATIONAL"** with green/teal border
6. Settings icon (⚙️) turns **green/teal**

### **Step 5: Verify It Worked**

- Close the model selector (tap anywhere outside)
- Status bar should say: **"OPERATIONAL - GridZero AI Online"**
- Settings icon is **GREEN** ✅
- Input field is **ENABLED** (not grayed out)
- Button says **"INITIATE ANALYSIS"** (not disabled)

### **Step 6: Test It!**

Type this in the input field:

```
Unit 4, structural collapse at library, three civilians trapped, need heavy lift gear immediately
```

Tap **"INITIATE ANALYSIS"**

**Expected Result:**

- Button shows loading spinner
- After 3-8 seconds, a glass card appears with:
    - Red gradient border (Critical severity)
    - "COLLAPSE" badge
    - "Library" as location
    - "👤 3 casualties"
    - "⚡ Urgent"
    - Resources: [Crane] [Medics] [Rescue Team]

---

## 🚨 **Common Issues & Fixes:**

### **Issue 1: "No models available"**

**Symptoms:**

- Model selector is empty
- No models shown

**Fix:**

1. Tap the **refresh icon** (🔄) in model selector
2. Wait 5 seconds
3. If still empty:
    - Force close app (swipe away from recent apps)
    - Reopen app
    - Wait 10 seconds
    - Try again

---

### **Issue 2: "Model downloaded but won't load"**

**Symptoms:**

- Tap LOAD button
- Status says "Loading tactical AI..."
- But nothing happens or it fails

**Fix:**

1. **Force close the app**
    - Swipe away from recent apps
    - Or: Settings → Apps → GridZero → Force Stop

2. **Reopen the app**
    - Wait 10 seconds for initialization

3. **Try loading again**
    - Tap ⚙️ → Tap LOAD
    - Wait full 15 seconds

4. **If still failing:**
    - Tap DOWNLOAD again (it will re-download)
    - Then tap LOAD

---

### **Issue 3: "Model loads but still says 'LLM not initialized'"**

**Symptoms:**

- Model shows "✓ OPERATIONAL"
- But analysis still fails with error

**Fix:**

1. **Reload the model:**
    - Tap ⚙️ icon
    - Tap LOAD again (even though it shows operational)
    - Wait 10 seconds

2. **Clear and retry:**
    - Close model selector
    - Wait 5 seconds
    - Try analyzing again

3. **Last resort:**
    - Force close app
    - Reopen
    - Wait 10 seconds
    - Load model
    - Try again

---

### **Issue 4: "Analysis starts but never finishes"**

**Symptoms:**

- Button shows loading spinner
- But it spins forever
- No result appears

**Fix:**

1. **Wait 30 seconds** (some models are slow on first run)

2. **If still spinning:**
    - App may have crashed internally
    - Force close and reopen

3. **Try simpler input:**
   ```
   Fire at library, need firefighters
   ```
    - Shorter inputs work better

---

### **Issue 5: "Download fails or gets stuck"**

**Symptoms:**

- Progress bar stops moving
- Download percentage doesn't change

**Fix:**

1. **Check internet connection**
    - Make sure WiFi/mobile data is working
    - Try opening a webpage

2. **Force close and retry:**
    - Close app
    - Reopen
    - Tap DOWNLOAD again
    - It will resume from where it left off

3. **Clear partial download:**
    - Go to: Settings → Apps → GridZero → Storage → Clear Cache
    - Reopen app
    - Download again (fresh start)

---

## 📊 **Visual Status Indicators:**

### **Before Model Loaded:**

```
Status Bar: "GRIDZERO READY - Load tactical AI model"
Settings Icon: ⚙️ (white/gray)
Button: Grayed out/disabled
Input: Shows warning message
```

### **During Download:**

```
Status Bar: "Downloading: 45%"
Progress Bar: Visible and moving
Button: Disabled
```

### **During Load:**

```
Status Bar: "Loading tactical AI..."
Settings Icon: ⚙️ (white)
Button: Disabled
```

### **After Successful Load (READY!):**

```
Status Bar: "OPERATIONAL - GridZero AI Online" ✅
Settings Icon: ⚙️ (GREEN/TEAL) ✅
Model Card: "✓ OPERATIONAL" with green border ✅
Button: Red, enabled, says "INITIATE ANALYSIS" ✅
Input: Enabled, ready for text ✅
```

---

## 🔍 **How to Check Logs (Advanced):**

If you're debugging in Android Studio:

1. Open **Logcat** panel (bottom of screen)
2. Filter by: **"CrisisViewModel"** or **"MyApp"**
3. Look for these messages:

**Good signs:**

```
MyApp: SDK initialized successfully
CrisisViewModel: Model loaded successfully: [model_id]
CrisisViewModel: Starting analysis with model: [model_id]
CrisisViewModel: Raw LLM Response: {json...}
CrisisViewModel: Parsed report: CrisisReport(...)
```

**Bad signs:**

```
CrisisViewModel: LLM not initialized error
CrisisViewModel: Model load failed
MyApp: SDK initialization failed
```

---

## ⚡ **Quick Checklist:**

Before trying to analyze, verify ALL of these:

- [ ] App has been open for at least 10 seconds
- [ ] Model selector shows models (not empty)
- [ ] Model shows "✓ READY" (downloaded)
- [ ] You tapped the "LOAD" button
- [ ] Status says "OPERATIONAL - GridZero AI Online"
- [ ] Model card shows "✓ OPERATIONAL"
- [ ] Settings icon is GREEN/TEAL
- [ ] Input field is enabled (not grayed)
- [ ] Button is red and enabled

**If ALL checkboxes are ✅ → Analysis WILL work!**

---

## 💡 **Why This Happens:**

The "LLM not initialized" error occurs because:

1. **The SDK initializes asynchronously** (takes 5-10 seconds on first launch)
2. **Downloading ≠ Loading** (you must LOAD after downloading)
3. **Loading takes time** (10-15 seconds to load 374MB into memory)
4. **The model must be in memory** before generating responses

Think of it like this:

- **Download** = Copy the engine to your garage
- **Load** = Install the engine in your car
- **Analyze** = Drive the car

You can't drive without installing the engine first!

---

## 🎯 **Testing Checklist:**

After following all steps, test with these inputs:

### **Test 1: Simple Critical Report**

```
Collapse at library, 3 trapped, need crane
```

Expected: Red card, COLLAPSE, Critical

### **Test 2: Medical Emergency**

```
Multiple casualties at hospital, need ambulances urgently
```

Expected: Orange/red card, MEDICAL, casualties > 1

### **Test 3: Fire**

```
Fire at downtown building, spreading fast, evacuate immediately
```

Expected: Red card, FIRE, Urgent sentiment

**If ALL THREE work → System is fully operational! 🎉**

---

## 📞 **Still Not Working?**

If you've tried everything above and it still doesn't work:

1. **Uninstall and reinstall** the app
2. **Make sure you have at least 500MB free space** on device
3. **Check Android version** (needs Android 7.0+)
4. **Try on a different device/emulator**
5. **Check logcat** for detailed error messages

---

## ✅ **Success Indicator:**

You'll know it's working when you see:

1. **Glass card appears** with extracted data
2. **Status bar shows**: "Report processed - [TYPE] at [LOCATION]"
3. **Bandwidth counter increments**: "📡 XXX KB SAVED"
4. **No error messages**
5. **Card has color-coded border** (red/yellow/teal)

**That's it! GridZero is operational and compressing field reports into structured data! 🚀**
