# GridZero Troubleshooting Guide

## Common Issues and Solutions

### Error: "Analysis Failed: LLM component not initialized"

This error occurs when the AI model isn't properly loaded. Here's how to fix it:

#### **Solution 1: Load the Model (Most Common)**

1. **Tap the shield icon** (⚙️) in the top-right corner
2. **Check model status:**
    - 🟢 Green checkmark = Model is loaded (ready to use)
    - 🟡 Orange warning = No model loaded

3. **If you see "Qwen 2.5 0.5B" model:**
    - If it says "Download" → Tap **Download** (wait 2-3 minutes for 374 MB)
    - If it says "✓ Downloaded" → Tap **Load** (wait 10-15 seconds)
    - Wait until status shows **"OPERATIONAL - GridZero AI Online"**

4. **If you don't see any models:**
    - Tap **Refresh** button in model selector
    - Close and reopen the app
    - Check logs for SDK initialization errors

#### **Solution 2: Wait for SDK Initialization**

The SDK initializes when the app launches. If you try to use it too quickly:

1. **Wait 5-10 seconds** after app launch
2. Look at the status bar - it should say "GRIDZERO READY - Load tactical AI model"
3. If it says "Initializing...", wait a bit longer

#### **Solution 3: Reload the Model**

If the model was loaded but stopped working:

1. Tap shield icon (⚙️)
2. Find the loaded model (green background)
3. Close model selector
4. Reopen model selector
5. Tap **Load** again on the same model

#### **Solution 4: Restart the App**

Sometimes the SDK needs a fresh start:

1. **Force close** the app (swipe away from recent apps)
2. **Relaunch** GridZero
3. **Wait 5-10 seconds** for initialization
4. **Load the model** again

---

### Error: "Tactical AI not loaded. Please download and load a model first."

**Cause:** You tried to analyze a report without loading an AI model.

**Solution:**

1. Tap shield icon (⚙️)
2. Download a model (if not already downloaded)
3. Tap **Load** on the model
4. Wait for "OPERATIONAL" status
5. Try submitting the report again

---

### Error: "Failed to parse AI response"

**Cause:** The AI generated output that couldn't be converted to JSON.

**Solution:**

- The app automatically retries with a simpler prompt
- Wait for the retry to complete
- If it still fails, try:
    - Simplifying your input (shorter, clearer description)
    - Reloading the model
    - Using a different model

---

### Download Fails or Stalls

**Symptoms:**

- Download progress bar stuck
- "Download failed" error

**Solutions:**

1. **Check Internet Connection:**
    - Make sure you have WiFi or mobile data
    - Try opening a website to verify internet works

2. **Check Storage Space:**
    - Model is 374 MB
    - You need at least 500 MB free
    - Check: Settings → Storage

3. **Retry Download:**
    - Tap shield icon (⚙️)
    - Tap **Refresh**
    - Try downloading again

4. **Use Different Network:**
    - Some networks block HuggingFace downloads
    - Try switching to mobile data or different WiFi

---

### Model Loads Very Slowly

**Expected:** 10-20 seconds on most devices

**If it takes longer:**

- **Device has low RAM:** Close other apps
- **Device is older:** Normal for older/budget phones
- **First load:** First time is slower, subsequent loads are faster

---

### App Crashes When Loading Model

**Cause:** Insufficient device memory

**Solutions:**

1. **Close Background Apps:**
    - Clear recent apps (swipe away all)
    - Restart device if needed

2. **Try Smaller Model:**
    - Current model: Qwen 2.5 0.5B (374 MB)
    - Alternative: SmolLM2 360M (119 MB) - smaller, faster

3. **Check Device Requirements:**
    - Minimum: Android 7.0, 2GB RAM
    - Recommended: Android 10+, 4GB RAM

---

### "No models available. Check initialization..."

**Cause:** SDK didn't initialize properly

**Solutions:**

1. **Check AndroidManifest.xml:**
    - Verify `android:name=".MyApplication"` is present
    - Verify `android:largeHeap="true"` is set

2. **Check Logs:**
    - Open Android Studio → Logcat
    - Filter by "MyApp" or "GridZero"
    - Look for "SDK initialized successfully"

3. **Reinstall App:**
    - Uninstall GridZero
    - Rebuild and reinstall
    - Launch and wait for initialization

---

### Analysis Takes Too Long (>30 seconds)

**Expected:** 3-8 seconds on most devices

**If slower:**

1. **Device is under load:** Close other apps
2. **Input is too long:** Try shorter reports (< 500 characters)
3. **Device is low-end:** Normal on budget devices
4. **Try smaller model:** SmolLM2 is faster

---

### Offline Mode Not Working

**Symptoms:**

- "No internet" error when trying to analyze
- Airplane mode causes issues

**Solution:**

1. **Download model FIRST** (requires internet)
2. **Load model while online**
3. **Then** enable airplane mode
4. Analysis should work offline

**Note:** Model download and loading require internet once. After that, 100% offline.

---

### Shield Icon Shows Orange Warning

**Meaning:** No model is currently loaded

**To fix:**

1. Tap the orange shield icon
2. Download a model
3. Load the model
4. Shield turns green ✓

---

### Reports Don't Appear in Feed

**Check:**

1. **Error banner at top?** Read the error message
2. **Status says "Analyzing..."?** Wait for it to finish
3. **Input field empty?** Type a report first
4. **Model loaded?** Check shield icon is green

---

## Quick Diagnostic Steps

**If something isn't working:**

1. ✅ Check shield icon:
    - 🟢 Green = Good
    - 🟡 Orange = Load model

2. ✅ Read status bar message:
    - "OPERATIONAL" = Ready
    - "Initializing" = Wait
    - "ERROR" = Check error message

3. ✅ Check for red error banner at top

4. ✅ Try these in order:
    - Wait 10 seconds
    - Reload model
    - Restart app
    - Clear cache and reinstall

---

## Logs and Debugging

### View Logs in Android Studio:

1. **Open Logcat** (bottom panel)
2. **Filter by tag:**
    - "CrisisViewModel" - Analysis logs
    - "MyApp" - SDK initialization
    - "GridZero" - General app logs

3. **Look for:**
    - "SDK initialized successfully" ✓
    - "Model loaded successfully" ✓
    - Error messages ❌

### Enable Verbose Logging:

Add to code if needed:

```kotlin
Log.v("GridZero", "Detailed message here")
```

---

## Still Having Issues?

### Collect Information:

1. **Device Info:**
    - Android version
    - Device model
    - Available RAM

2. **Error Details:**
    - Full error message
    - When does it occur?
    - Can you reproduce it?

3. **Steps Taken:**
    - What have you tried?
    - Does it work sometimes?

### Check GitHub Issues:

- Open an issue with the information above
- Include relevant log output
- Describe expected vs actual behavior

---

## Prevention Tips

### For Best Experience:

1. ✅ **Wait for initialization** (5-10 seconds after launch)
2. ✅ **Download model on WiFi** (374 MB)
3. ✅ **Close other apps** before loading model
4. ✅ **Keep device charged** (>50% battery)
5. ✅ **Use clear, concise field reports** (100-500 characters)
6. ✅ **Check shield icon is green** before submitting

### Optimal Device Setup:

- **Storage:** 1GB+ free
- **RAM:** 4GB+ recommended
- **Network:** WiFi for download
- **Battery:** 50%+ charged
- **Apps:** Only GridZero running

---

## Technical Details

### Model Requirements:

- **Size:** 374 MB on disk
- **RAM:** ~800 MB in memory
- **CPU:** ARM64 required
- **Android:** API 24+ (Android 7.0+)

### Initialization Process:

1. App launches
2. MyApplication.onCreate() runs
3. SDK initializes (async, 3-5 seconds)
4. LlamaCppServiceProvider registers
5. Models are registered
6. Ready for use

### Analysis Process:

1. Check model loaded
2. Build prompt with system instructions
3. Call RunAnywhere.generate()
4. Parse JSON response
5. Create CrisisReport
6. Display in feed

---

## Contact Support

If you've tried everything and it still doesn't work:

1. **GitHub Issues:** [Repository URL]/issues
2. **Include:**
    - Device info
    - Android version
    - Error messages
    - Log output from Logcat
    - Steps to reproduce

---

**Remember:** The most common issue is simply not loading the model first! Always check that shield
icon is green. 🟢

---

## Quick Reference

| Symptom | Quick Fix |
|---------|-----------|
| "LLM not initialized" | Load model via shield icon |
| Orange shield icon | Download + load model |
| Analysis fails | Check model is loaded |
| Download fails | Check internet + storage |
| Slow analysis | Normal (3-8 sec), close other apps |
| No models showing | Tap Refresh, restart app |
| Crashes on load | Close other apps, try smaller model |

---

**Last Updated:** 2024
**Version:** 1.0
