# GridZero Quick Fix - "LLM not initialized"

## 🎯 The Problem

You're seeing: **"LLM not initialized. Please wait a moment and try reloading the model."**

**This means:** The AI model isn't loaded yet. You MUST load a model before you can analyze reports.

---

## ✅ The Solution (5 Minutes)

### **Visual Guide:**

```
┌─────────────────────────────────────┐
│ ● GRIDZERO               [⚙️ Yellow]│  ← Yellow/Orange = No model
│   OFFLINE SITUATION COMMAND         │
├─────────────────────────────────────┤
│ 1. TAP THIS ──────────────→ [⚙️]   │  ← Tap settings icon
└─────────────────────────────────────┘

       ↓ OPENS MODEL SELECTOR ↓

┌─────────────────────────────────────┐
│ TACTICAL AI SYSTEMS    [🔄] [✕]    │
├─────────────────────────────────────┤
│ ▌Qwen 2.5 0.5B Instruct Q6_K       │
│ ▌                                   │
│ ▌ [  DOWNLOAD  ] [   LOAD   ]      │  ← 2. Tap DOWNLOAD first
└─────────────────────────────────────┘

       ↓ WAIT 2-3 MINUTES ↓

┌─────────────────────────────────────┐
│ DOWNLOADING: 45%                    │  ← Progress bar
│ ▓▓▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░           │
└─────────────────────────────────────┘

       ↓ DOWNLOAD COMPLETE ↓

┌─────────────────────────────────────┐
│ ▌Qwen 2.5 0.5B Instruct Q6_K       │
│ ▌                                   │
│ ▌ [   READY   ] [   LOAD   ]       │  ← 3. Now tap LOAD
└─────────────────────────────────────┘

       ↓ WAIT 10-15 SECONDS ↓

┌─────────────────────────────────────┐
│ ▌Qwen 2.5 0.5B Instruct Q6_K       │
│ ▌ OPERATIONAL                       │  ← ✅ Success!
└─────────────────────────────────────┘

       ↓ CLOSE SELECTOR ↓

┌─────────────────────────────────────┐
│ ● GRIDZERO               [⚙️ Green] │  ← Green/Teal = Model loaded!
│   OFFLINE SITUATION COMMAND         │
├─────────────────────────────────────┤
│ INCOMING TRANSMISSION               │
│ ┌─────────────────────────────────┐ │
│ │ Enter field report...           │ │  ← 4. Type report here
│ └─────────────────────────────────┘ │
│ [    PROCESS INTEL    ]             │  ← 5. Tap this!
└─────────────────────────────────────┘
```

---

## 📱 Step-by-Step Instructions

### **STEP 1: Open Model Selector**

- Look at top-right corner
- See the **⚙️ icon**?
- **Yellow/Orange** = No model loaded
- **Tap it**

### **STEP 2: Download Model**

You'll see:

```
┌─────────────────────────────────────┐
│ TACTICAL AI SYSTEMS                 │
│                                     │
│ Qwen 2.5 0.5B Instruct Q6_K        │
│ [  DOWNLOAD  ] [   LOAD   ]        │
└─────────────────────────────────────┘
```

**Action:**

- Tap red **"DOWNLOAD"** button
- **Wait 2-3 minutes** (downloading 374 MB)
- You'll see a progress bar
- **Do NOT close the app** while downloading

### **STEP 3: Load Model**

When download finishes:

```
┌─────────────────────────────────────┐
│ Qwen 2.5 0.5B Instruct Q6_K        │
│ [   READY   ] [   LOAD   ]         │
└─────────────────────────────────────┘
```

**Action:**

- Tap green **"LOAD"** button
- **Wait 10-15 seconds**
- Screen might freeze briefly (normal!)
- Model card shows **"OPERATIONAL"**

### **STEP 4: Verify Success**

Check these indicators:

- ✅ Settings icon (⚙️) is now **TEAL/GREEN**
- ✅ Model card shows **"OPERATIONAL"**
- ✅ "PROCESS INTEL" button is **not grayed out**

### **STEP 5: Test It!**

Now try analyzing a report:

```
Collapse at library, 3 trapped, need crane
```

Tap **"PROCESS INTEL"** → Should work! 🎉

---

## 🚨 Common Issues

### **Issue 1: "No models available"**

**Cause:** SDK didn't initialize

**Fix:**

1. Force close app (swipe away)
2. Reopen GridZero
3. Wait 10 seconds
4. Try again

### **Issue 2: Download fails**

**Fix:**

- Check internet connection
- Make sure you have WiFi (374 MB)
- Check storage space (need 500 MB free)
- Try again

### **Issue 3: Load button is grayed out**

**Cause:** Model not fully downloaded

**Fix:**

- Wait for download to complete
- Button changes from "DOWNLOAD" to "READY"
- Then you can tap "LOAD"

### **Issue 4: Still says "LLM not initialized"**

**Fix:**

1. Close model selector
2. Check settings icon (⚙️)
3. Is it GREEN? → Model is loaded
4. Is it YELLOW? → Model NOT loaded
5. If yellow, tap it and check model status

---

## 🔍 How to Check If Model is Loaded

### **Method 1: Settings Icon Color**

```
⚙️ YELLOW/ORANGE = Not loaded ❌
⚙️ TEAL/GREEN    = Loaded ✅
```

### **Method 2: Open Model Selector**

Loaded model shows:

```
▌ Qwen 2.5 0.5B Instruct Q6_K
▌ OPERATIONAL                    ← This line means loaded
```

### **Method 3: Try to Analyze**

- If button is **grayed out** → Not loaded ❌
- If button is **red** → Loaded ✅

---

## 📊 Expected Timings

| Step | Time | What's Happening |
|------|------|------------------|
| App Launch | 5-10 sec | SDK initializing |
| Model Download | 2-3 min | Downloading 374 MB |
| Model Load | 10-15 sec | Loading into memory |
| Analysis | 3-8 sec | Processing report |

**Total first-time setup: ~5 minutes**

---

## 🎯 Quick Checklist

Before analyzing a report, verify:

- [ ] App has been open for 10+ seconds
- [ ] Opened model selector (⚙️)
- [ ] Saw "Qwen 2.5 0.5B" model listed
- [ ] Tapped "DOWNLOAD" button
- [ ] Waited for download (2-3 min)
- [ ] Tapped "LOAD" button
- [ ] Waited for load (10-15 sec)
- [ ] Saw "OPERATIONAL" status
- [ ] Settings icon is GREEN/TEAL
- [ ] "PROCESS INTEL" button is RED (not gray)

**If all checked ✅ → You can analyze reports!**

---

## 💡 Pro Tips

1. **Download on WiFi** - 374 MB is large
2. **Keep app open** during download
3. **Wait patiently** during model load (can take 15 sec)
4. **Check icon color** before analyzing
5. **Model stays loaded** once you load it (until app restart)

---

## 🆘 Still Not Working?

### Check These:

1. **Device Requirements:**
    - Android 7.0+ (API 24+)
    - 2GB+ RAM (4GB recommended)
    - 500 MB free storage
    - ARM64 processor

2. **Logcat Output** (Android Studio):
   ```
   Filter: "MyApp" or "CrisisViewModel"
   Look for: "SDK initialized successfully"
   ```

3. **Clean Build:**
   ```
   Build → Clean Project
   Build → Rebuild Project
   Run again
   ```

---

## 📺 Video Tutorial Would Show:

```
1. [0:00] Launch app → Wait 10 seconds
2. [0:10] Tap ⚙️ icon (top right)
3. [0:12] See model selector open
4. [0:15] Tap red "DOWNLOAD" button
5. [2:30] Download completes → "READY"
6. [2:32] Tap green "LOAD" button
7. [2:45] See "OPERATIONAL" status
8. [2:47] Settings icon turns GREEN
9. [2:50] Close model selector
10. [2:52] Type: "Collapse at library, 3 trapped"
11. [2:55] Tap "PROCESS INTEL"
12. [3:03] See red card appear! ✅
```

**Total time: 3 minutes** (2 min download, rest is quick)

---

## ✅ Success Indicators

You'll know it's working when:

1. ✅ **Settings icon is GREEN** (not yellow)
2. ✅ **"PROCESS INTEL" button is RED** (not gray)
3. ✅ **No error message** when you tap button
4. ✅ **Button shows "ANALYZING"** with spinner
5. ✅ **Red card appears** after 3-8 seconds

---

## 🎓 What You're Doing

**Loading the model** = Installing the AI "brain"

- Like downloading a game before playing
- One-time process (stays loaded)
- Required for AI to work

**The model file:**

- Name: Qwen 2.5 0.5B Instruct
- Size: 374 MB
- Purpose: Extracts data from text
- Quality: Good balance of speed & accuracy

---

**Remember:** You MUST load the model first! The app can't magically work without an AI brain
loaded. 🧠

Once loaded, it stays loaded until you close the app. Then you just need to load it again (only 10
seconds).

---

**Need help?** Check the TROUBLESHOOTING.md file for more details.
