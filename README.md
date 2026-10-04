# MAX PROXY 🛡️
### Minimalistic Liquid Glass VPN for Android

**MAX PROXY** is a fully functional, ultra-fast Android VPN client engineered with native `android.net.VpnService` support, Apple-inspired typography, and a modern Liquid Glass / Glassmorphism aesthetic.

---

## 📲 Direct APK Download

The compiled, ready-to-install Android APK is located directly in the root directory of this repository:
- **[`MAX_PROXY.apk`](./MAX_PROXY.apk)** (Click to download or install)
- **[`app-debug.apk`](./app-debug.apk)** (Standard debug binary)

---

## ✨ Key Features

- **⚡ Native Android VPN Tunnel (`VpnService`)**:
  - Establishes a real TUN interface with encrypted routing (`0.0.0.0/0`).
  - Seamless Android system VPN permission confirmation dialog.
  - Native key icon in the Android status bar when active.
  - Ongoing foreground service notification with connection uptime, data transferred, and one-tap **Disconnect** action.

- **🌐 12 Global Server Locations (Over 20 Nodes)**:
  - 🇺🇸 **United States** — New York (10 Gbps), Los Angeles, Miami
  - 🇬🇧 **United Kingdom** — London (Docklands), Manchester
  - 🇨🇳 **China** — Hong Kong (Direct CN2 GIA), Shanghai, Beijing
  - 🇯🇵 **Japan** — Tokyo (Shinjuku IX), Osaka
  - 🇩🇪 **Germany** — Frankfurt (DE-CIX), Berlin
  - 🇸🇬 **Singapore** — Equinix SG1 APAC Hub
  - 🇨🇦 **Canada** — Toronto, Vancouver
  - 🇫🇷 **France** — Paris, Marseille
  - 🇦🇺 **Australia** — Sydney, Melbourne
  - 🇳🇱 **Netherlands** — Amsterdam (AMS-IX 20 Gbps)
  - 🇰🇷 **South Korea** — Seoul Gangnam
  - 🇨🇭 **Switzerland** — Zurich Privacy Bunker

- **💎 Liquid Glass & Apple Minimalist Aesthetic**:
  - Dynamic animated fluid background with refractive neon-cyan, emerald, and cyber-violet orbs.
  - Frosted translucent glass cards with specular highlight borders.
  - **Tactile Hero Connection Orb**: Multi-layered liquid glass power button with pulsating concentric aura, rotating orbital arcs, and smooth state animations.
  - Clean Apple SF Pro typography hierarchy and frosted pill badges.

- **📊 Live Network Telemetry & Diagnostics**:
  - Real-time download/upload bandwidth tracker.
  - Real-time animated Canvas traffic waveform.
  - Public IP detection and virtual masked IP verification.
  - Built-in circular Liquid Glass **Speedometer** with ping latency, jitter, download Mbps, upload Mbps, and grade rating.

- **🔒 Advanced Security Suite**:
  - **Multi-Protocol Engine**: MaxWireGuard (Instant UDP), OpenVPN (UDP/TCP), Shadowsocks Stealth, and IKEv2.
  - **Kill Switch**: Blocks unencrypted leaks if tunnel disconnects.
  - **CleanWeb**: DNS-level ad, tracker, and malware blocker.
  - **Zero-Logs Architecture**: Hardware RAM-only design.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3 + Custom Liquid Glass Design System)
- **Architecture:** MVVM + Clean Architecture + StateFlow
- **Core Engine:** Android `VpnService` with TUN packet routing
- **Networking:** OkHttp & Coroutines
- **Target SDK:** Android 36 (Min SDK 24 / Android 7.0+)

---

## 🚀 Building from Source

```bash
# Clone the repository
git clone <repo-url>
cd max-proxy

# Build the debug APK
gradle assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 License
MAX PROXY is distributed for secure personal privacy and networking.
