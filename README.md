# Flux Client

Flux is an Android Minecraft Bedrock Edition (MCPE) utility client built around a
local RakNet MITM relay. It is designed for protocol research, private worlds,
and servers where packet tooling is explicitly permitted. It does not patch
Minecraft memory or inject native code into the game.

## What was learned from the two supplied sources

- **ProtoHax** has a small, understandable relay/UI split and a useful overlay
  lifecycle, but its native Netty channel and account/service code are tightly
  coupled to the app and its protocol support is dated.
- the second source has the stronger foundation: Cloudburst Bedrock codecs, RakNet
  transport, version-aware mappings, packet listeners, a module registry, and a
  Compose control surface. Its legacy snapshot also had an unsafe hard-coded
  signing key, broad storage permissions, and service startup ordering issues.

Flux provides an independent relay implementation informed by the supplied projects while treating the relay as a boundary:
packet listeners are isolated from UI state, mappings are selected after
`StartGamePacket`, and unknown packets are forwarded without losing payloads.
The app also uses the Android system debug/release signing flow, avoids legacy
shared-storage permissions, and creates the foreground notification channel
before promotion.

## Architecture

```
Minecraft (local Bedrock client)
        | UDP/RakNet :19132
        v
Flux WRelay -> codec/version negotiation -> packet listeners -> remote server
                                      |
                                      +-- modules (combat, motion, visual, world, misc)
                                      +-- Compose configuration + floating overlay
```

The relay supports online Microsoft authentication, offline mode when no
account is selected, protocol mappings from bundled `mcpedata`, packet replay
and logging, and a version tracker. Module configuration is stored only in the
app's private files directory as JSON. Server rules and Mojang/Microsoft terms
still apply; never use this to bypass access controls or disrupt servers.

## Build

Requires JDK 17 and the Android SDK. The project targets Android API 36 and
supports `arm64-v8a` and `armeabi-v7a` on Android 9+.

```bash
./gradlew :app:assembleDebug
# or
./gradlew :app:assembleRelease
```

Release builds intentionally use Gradle's normal signing configuration. Supply
a keystore through your local Gradle/Android Studio signing setup rather than
committing credentials or keystores.

## Capturing a connection

1. Install and open Flux, grant the overlay permission, and select an account
   only when online authentication is required.
2. Enter the remote Bedrock host and port in the Server page.
3. Start capture, then point Minecraft Bedrock at the device's LAN address on
   port `19132`.
4. Stop capture before changing server settings. Use only on networks and
   servers you own or have permission to test.

Flux is not affiliated with Mojang Studios or Microsoft.
