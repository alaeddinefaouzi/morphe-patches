# 🎮 Morphe Game Patches

A curated suite of game patches for the [Morphe](https://morphe.software) Android patching ecosystem.

[![Morphe Compatible](https://img.shields.io/badge/Morphe-Compatible-6C5CE7?style=flat-square)](https://morphe.software)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](LICENSE)
[![Latest Release](https://img.shields.io/github/v/release/alaeddinefaouzi/morphe-patches?style=flat-square)](https://github.com/alaeddinefaouzi/morphe-patches/releases)

---

## 📲 Add to Morphe Manager

### ⚡ Option 1: One-Click Integration (Recommended)
On your Android device with **Morphe Manager** installed, click the badge below to add this repository directly as a patch source:

[![Add to Morphe](https://img.shields.io/badge/Add%20to-Morphe%20Manager-4CAF50?style=for-the-badge&logo=android)](https://morphe.software/add-source?github=alaeddinefaouzi/morphe-patches)

---

### 🛠 Option 2: Add Manually in Morphe Manager
1. Open **Morphe Manager** on your Android device.
2. Tap the **Sources** tab at the bottom.
3. Tap the **`+` (Add)** button.
4. Under the **Remote** tab, enter:
   ```text
   github.com/alaeddinefaouzi/morphe-patches
   ```
5. Tap **Add**. Morphe Manager will automatically fetch the latest patches and notify you when updates are released!

---

### 📦 Option 3: Manual `.mpp` Import
If you prefer offline or manual patching:
1. Go to the [Releases](https://github.com/alaeddinefaouzi/morphe-patches/releases) page.
2. Download `patches-1.0.0.mpp`.
3. In Morphe Manager or Morphe Desktop, select the downloaded `.mpp` bundle to apply patches.

---

## 🕹 Supported Games & Features

### 🥊 BroBuddy (`com.brobuddy.game`) — Version 1.0.3

| Patch | Description | Default |
|---|---|:---:|
| **Free Store & Unlimited Currency** | Unlocks all in-game store purchases (Gold, Cash bundles, VIP, double resources) instantly without Google Play payment sheets or account requirements. Fully functional offline. | ✅ Yes |
| **No Ads** | Completely eliminates bottom banner ads, interstitial screens between heist/stages, and forced ads. Results screens appear immediately without delay. | ✅ Yes |
| **Instant Ad Rewards** | Converts every "Watch Ad for 2x Rewards" button into an instant payout with 0 waiting time. Claimable repeatedly. | ✅ Yes |
| **Native IAP Verification Bypass** | Engine-level IL2CPP bypass for `ValidatePurchaseIfPossible`, `CanCrossPlatformValidate`, and `OnConfirmationFailed`. Bypasses server receipt verification so purchases confirm immediately. | ✅ Yes |

---

## 🚀 How to Build from Source

### Prerequisites
- Java Development Kit (JDK 21 or higher)

### Build Commands
1. Clone the repository:
   ```bash
   git clone https://github.com/alaeddinefaouzi/morphe-patches.git
   cd morphe-patches
   ```
2. Build the patch bundle:
   ```bash
   ./gradlew :patches:buildAndroid
   ```
3. Generate the patches list manifest:
   ```bash
   ./gradlew generatePatchesList
   ```
4. The output `.mpp` bundle is created under `patches/build/libs/`.

---

## 🧩 Adding Future Game Patches

This repository follows the official Morphe patches modular architecture:

1. **Bytecode / Smali Hooks**:
   Create a new package under `patches/src/main/kotlin/app/morphe/patches/<game_name>/` containing your `bytecodePatch` implementations.
2. **Native IL2CPP Hex Patches**:
   If the game uses Unity IL2CPP, register pattern replacements inside a `rawResourcePatch` using `hexPatch` targeting `lib/arm64-v8a/libil2cpp.so`.
3. **Register Target Compatibility**:
   Add the app's package name and version into `Constants.kt` under `app.morphe.patches.shared`.
4. **Rebuild**:
   Run `./gradlew :patches:buildAndroid` to build the new unified `.mpp` bundle.

---

## ⚖️ License
Distributed under the GNU General Public License v3.0 (GPLv3). See `LICENSE` for details.
