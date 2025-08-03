<p align="center">
  <a href="https://discord.gg/TUKfADsC" target="_blank">
    <!-- 使用Discord官方API显示在线人数 -->
    <img src="https://discord.com/api/guilds/1401241266682859520/widget.png?style=banner2" alt="Join our Discord">
  </a>
</p>

<h1 align="center">SculptLauncher</h1>
<p align="center">
  <strong>Native Modding Platform for Minecraft Bedrock Edition on Android</strong>
</p>



## 📦 Overview
SculptLauncher is an Android modding platform for Minecraft Bedrock Edition that enables developers to create powerful game modifications using native C/C++ code. Designed as a lightweight and performant alternative to script-based modding solutions, SculptLauncher provides low-level access to game mechanics while maintaining stability.

> **Note**: This project is currently in early development. Core APIs and features are being actively developed.

## 🚀 Features

| Feature                | Status        | Description                                     |
|------------------------|---------------|-------------------------------------------------|
| Game Launching         | ✅ Completed  | Fully functional game bootstrapping             |
| Mod Integration        | 🚧 In Progress| Native C/C++ mod loading framework              |
| Core API Development   | 🚧 In Progress| Low-level game interaction interfaces           |
| Version Compatibility  | ✅ Implemented| Multi-version support (see below)               |

## ✔️ Supported Minecraft Versions
- 1.16.201
- 1.20.60.04
- 1.21.0.03

## 📚 Getting Started
### Prerequisites
- Android NDK r25+
- Android Studio Giraffe+
- Minecraft Bedrock Edition (supported version)

### Building from Source
```bash
git clone https://github.com/your-username/SculptLauncher.git
cd SculptLauncher
./gradlew assembleDebug
