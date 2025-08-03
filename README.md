<div align="center">
  <!-- <img src="path/logo.png" alt="SculptLauncher" width="150" height="150" /> -->
  
  <h1>SculptLauncher
    <br />
    <a href="https://discord.gg/TUKfADsC">
      <img alt="Discord" src="https://img.shields.io/discord/1401241266682859520?color=5865f2&label=Discord&style=flat" />
    </a>
    <img alt="GitHub Downloads" src="https://img.shields.io/github/downloads/your-username/SculptLauncher/total?style=flat">
  </h1>
  <p><strong>Native Modding Platform for Minecraft Bedrock Edition on Android</strong></p>
</div>

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
git clone https://github.com/SculptAPI-Team/SculptLauncher.git
cd SculptLauncher
./gradlew assembleDebug
