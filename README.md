<p align="center">
  <a href="https://discord.gg/YOUR_INVITE_CODE" target="_blank">
    <img src="https://img.shields.io/discord/YOUR_SERVER_ID?label=Join%20Our%20Discord&logo=discord&logoColor=white&style=for-the-badge" alt="Discord">
  </a>
</p>

<h1 align="center">SculptLauncher</h1>
<p align="center">
  <strong>Native Modding Platform for Minecraft Bedrock Edition on Android</strong>
</p>

![SculptLauncher Banner](https://via.placeholder.com/800x200/5865F2/FFFFFF?text=SculptLauncher+-+C%2FC%2B%2B+Modding+for+Minecraft+BE) <!-- Replace with actual banner image -->

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

> More versions will be added as the project matures

## 🛠️ Development Roadmap
```mermaid
graph LR
    A[Core Launcher] --> B[Mod Loader]
    B --> C[JNI Bindings]
    C --> D[Modding API]
    D --> E[Debug Tools]
    E --> F[Mod Repository]
