# GridZero Deployment Checklist

## Pre-Demo Setup (Day Before)

### Device Preparation

- [ ] Charge device to 100%
- [ ] Clear all unnecessary apps
- [ ] Enable "Do Not Disturb" mode
- [ ] Disable auto-updates
- [ ] Disable screen timeout (keep always-on)
- [ ] Set brightness to 100%
- [ ] Test device performance (no lag)

### App Installation

- [ ] Build latest version in Release mode
- [ ] Install APK on demo device
- [ ] Grant all required permissions
- [ ] Test launch - no crashes
- [ ] Verify UI looks correct on device screen

### Model Setup

- [ ] Download Qwen 2.5 0.5B model
- [ ] Verify model file integrity (374 MB)
- [ ] Load model successfully
- [ ] Confirm status: "OPERATIONAL"
- [ ] Test one sample report - verify extraction works

### Network Preparation

- [ ] Connect to reliable WiFi (for backup)
- [ ] Test airplane mode toggle
- [ ] Verify offline functionality
- [ ] Download any backup models (SmolLM2 360M as fallback)

---

## Demo Environment Setup

### Presentation Space

- [ ] Position device where all judges can see
- [ ] Test device visibility from judge positions
- [ ] Ensure adequate lighting on device screen
- [ ] Have device charger nearby (just in case)
- [ ] Keep backup device ready (if available)

### Materials Ready

- [ ] Device with GridZero installed
- [ ] Backup laptop with presentation slides
- [ ] Demo script printed/accessible
- [ ] Business cards or contact info
- [ ] Code repository link ready to share
- [ ] Screenshots/video backup (if demo fails)

### Demo Reports Prepared

- [ ] Copy all 7 demo reports to a notes app
- [ ] Test each report once (verify they work)
- [ ] Clear tactical feed before demo
- [ ] Reset bandwidth counter

**Demo Reports:**

```
1. Unit 4, structural collapse at library, severe flooding, three civilians trapped, need heavy lift gear immediately!

2. Medic team 2, multiple casualties at North Hospital, approximately 15 injured, need ambulances and medical supplies ASAP!

3. Fire at downtown sector, building fully engulfed, need all available fire teams immediately!

4. Civil unrest at city hall, large crowd estimated 200 people, need police backup for crowd control.

5. Hazmat situation at industrial plant, chemical spill, approximately 50 workers evacuated, need hazmat team immediately!

6. Mass casualty event at stadium, crowd crush, estimated 30-40 injured, need all available ambulances!

7. Unit 7 checking in, area secured, no additional casualties, situation stable.
```

---

## 30 Minutes Before Demo

### Final Checks

- [ ] Launch GridZero
- [ ] Confirm model loaded (green shield icon)
- [ ] Clear all old reports (fresh tactical feed)
- [ ] Close all background apps
- [ ] Airplane mode OFF (for now)
- [ ] Volume at appropriate level
- [ ] Test one quick report (then clear it)
- [ ] Device screen unlocked and ready

### Personal Preparation

- [ ] Review 2-minute demo script
- [ ] Practice key talking points
- [ ] Prepare for common Q&A
- [ ] Hydrate, breathe, focus
- [ ] Visualize successful demo

---

## Demo Execution Checklist

### Introduction (0:00 - 0:20)

- [ ] Establish the problem (disaster scenario)
- [ ] Hook judges with bandwidth numbers
- [ ] Set up the demo context

### Live Demo (0:20 - 1:20)

- [ ] Show GridZero tactical dashboard
- [ ] Submit first report (structural collapse)
- [ ] Highlight red critical card, pulsing animation
- [ ] Point out extracted fields (location, severity, etc.)
- [ ] Emphasize: "2 MB → 200 bytes"
- [ ] Submit 2-3 more reports quickly
- [ ] Show multiple incidents in feed
- [ ] Highlight color coding (red/yellow/teal)
- [ ] Point to bandwidth savings counter

### Offline Demo (1:20 - 1:40)

- [ ] Enable airplane mode
- [ ] Submit final report
- [ ] Prove 100% offline functionality
- [ ] Emphasize competitive advantage

### Close (1:40 - 2:00)

- [ ] State the math (15,000:1 compression)
- [ ] State the impact (save lives)
- [ ] Deliver closing hook
- [ ] Open for questions

---

## Post-Demo Actions

### Immediate Follow-up

- [ ] Answer judge questions confidently
- [ ] Offer to show code if requested
- [ ] Share GitHub repository link
- [ ] Exchange contact information
- [ ] Thank judges for their time

### Documentation

- [ ] Note any technical issues encountered
- [ ] Record judge feedback
- [ ] Document questions asked
- [ ] List areas for improvement

### Next Steps

- [ ] Follow up with organizers
- [ ] Share demo video (if recorded)
- [ ] Connect on LinkedIn with judges
- [ ] Prepare for next round (if applicable)

---

## Troubleshooting Guide

### Issue: Device freezes during demo

**Solution:**

1. Force close app
2. Relaunch GridZero
3. Model should auto-reload
4. Continue demo

### Issue: Model extraction is slow

**Solution:**

1. Acknowledge: "On-device processing takes 3-5 seconds"
2. Use wait time to explain architecture
3. Emphasize still faster than 3 minutes for audio

### Issue: JSON parsing fails

**Solution:**

1. Show error handling: "Watch the retry logic"
2. System auto-retries with simplified prompt
3. Explain: "AI outputs vary, we handle edge cases"

### Issue: Demo reports don't work

**Solution:**

1. Have backup reports in clipboard
2. Try different phrasing
3. Worst case: Show pre-recorded video

### Issue: Device won't go offline

**Solution:**

1. Skip airplane mode demo
2. Emphasize: "Works offline after model download"
3. Explain architecture instead

### Issue: App crashes

**Solution:**

1. Stay calm: "Perfect example of why robust tools matter"
2. Switch to slides/code walkthrough
3. Explain how it works theoretically
4. Offer to show working version later

---

## Backup Plans

### Plan A: Full Live Demo (Ideal)

- Everything works perfectly
- Model extracts all reports correctly
- Judges see full functionality

### Plan B: Partial Live Demo

- Model extraction works but is slow
- Use wait time to explain technology
- Show code and architecture

### Plan C: Pre-recorded Video

- Live demo fails completely
- Show high-quality screen recording
- Walk through each feature
- Offer code review

### Plan D: Code + Slides Only

- Device completely fails
- Show presentation slides
- Live code review in Android Studio
- Explain architecture deeply

**Always have Plans C and D ready on backup laptop!**

---

## Success Metrics

### During Demo

- ✅ No crashes
- ✅ At least 3 reports extracted successfully
- ✅ Judges visibly engaged
- ✅ Under 2-minute time limit
- ✅ Offline functionality proven

### Post-Demo

- ✅ Judges ask technical follow-up questions
- ✅ Judges can explain GridZero to others
- ✅ Judges remember "GridZero" by name
- ✅ Positive feedback on UI/UX
- ✅ Recognition of real-world impact

---

## Final Reminders

### Before Going On Stage

1. **Breathe:** You've built something incredible
2. **Believe:** This can save lives
3. **Be Clear:** Explain like judges are smart but unfamiliar with AI
4. **Be Confident:** You understand this better than anyone
5. **Be Passionate:** Your enthusiasm is contagious

### During Demo

1. **Pace yourself:** Don't rush through key moments
2. **Make eye contact:** Connect with judges
3. **Use pauses:** Let impressive moments sink in
4. **Handle errors gracefully:** Show you're a problem solver
5. **Enjoy it:** This is your moment to shine

### Key Message

> "GridZero turns the impossible constraint (offline-only) into our strongest competitive
advantage."

---

## Post-Mortem (After Demo)

### What Worked Well?

- [ ] Document successful elements
- [ ] Note judge reactions
- [ ] Record positive feedback

### What Could Improve?

- [ ] Technical issues encountered
- [ ] Timing adjustments needed
- [ ] Explanation clarity
- [ ] Demo flow optimization

### Action Items

- [ ] Fix any bugs discovered
- [ ] Improve prompt engineering if extraction failed
- [ ] Polish UI elements judges questioned
- [ ] Prepare better answers for common questions

---

## Contact Information

**For Technical Issues:**

- RunAnywhere SDK Docs: [Link]
- GitHub Issues: [Link]
- Discord/Slack: [Link]

**For Hackathon Organizers:**

- Demo submission: [Link/Email]
- Questions: [Contact]

---

## Motivation

Remember why you built this:

> "When disaster strikes and the internet goes down, first responders need to communicate. GridZero
ensures they can coordinate, even over the weakest mesh network, because we turn megabytes into
bytes."

> "You're not just building an app. You're building a lifeline for the people who run towards danger
while everyone else runs away."

**Now go show them what you've built! 🚀🏆**

---

## Final Checklist

### 5 Minutes Before Demo

- [x] Device at 100% battery
- [x] GridZero launched and ready
- [x] Model loaded (green shield)
- [x] Tactical feed cleared
- [x] Demo reports accessible
- [x] Airplane mode ready
- [x] Demo script memorized
- [x] Breathing calmly
- [x] Confident posture
- [x] Ready to win

**You've got this! 💪**
