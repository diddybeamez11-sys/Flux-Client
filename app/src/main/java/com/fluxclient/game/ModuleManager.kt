package com.fluxclient.game

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.fluxclient.application.AppContext
import com.fluxclient.game.module.combat.ACAModule
import com.fluxclient.game.module.combat.AntiCrystalModule
import com.fluxclient.game.module.combat.AntiKnockbackModule
import com.fluxclient.game.module.combat.CrystalSmashModule
import com.fluxclient.game.module.combat.EnemyHunterModule
import com.fluxclient.game.module.combat.HitAndRunModule
import com.fluxclient.game.module.combat.HitboxModule
import com.fluxclient.game.module.combat.KillauraModule
import com.fluxclient.game.module.combat.TriggerBotModule
import com.fluxclient.game.module.combat.WAuraModule
import com.fluxclient.game.module.combat.AutoFightModule
import com.fluxclient.game.module.combat.AutoHvHModule
import com.fluxclient.game.module.combat.AutoTotemModule
import com.fluxclient.game.module.combat.HotbarSwitcherModule
import com.fluxclient.game.module.combat.InfiniteAuraModule
import com.fluxclient.game.module.misc.ArrayListModule
import com.fluxclient.game.module.motion.NoClipModule
import com.fluxclient.game.module.misc.AutoDisconnectModule
import com.fluxclient.game.module.misc.CommandHandlerModule
import com.fluxclient.game.module.visual.CoordinatesModule
import com.fluxclient.game.module.misc.DesyncModule
import com.fluxclient.game.module.misc.FakeDeathModule
import com.fluxclient.game.module.misc.FakeXPModule
import com.fluxclient.game.module.misc.MinerModule
import com.fluxclient.game.module.misc.NoChatModule
import com.fluxclient.game.module.misc.PieChartModule
import com.fluxclient.game.module.misc.PositionLoggerModule
import com.fluxclient.game.module.misc.ReplayModule
import com.fluxclient.game.module.misc.ChestStealerModule
import com.fluxclient.game.module.misc.SpammerModule
import com.fluxclient.game.module.misc.ToggleSoundModule
import com.fluxclient.game.module.misc.WaterMarkModule
import com.fluxclient.game.module.world.AntiDebuffModule
import com.fluxclient.game.module.world.EffectsModule
import com.fluxclient.game.module.world.ParticlesModule
import com.fluxclient.game.module.world.TimeShiftModule
import com.fluxclient.game.module.world.WeatherControllerModule
import com.fluxclient.game.module.motion.AirJumpModule
import com.fluxclient.game.module.motion.AntiAFKModule
import com.fluxclient.game.module.motion.AutoWalkModule
import com.fluxclient.game.module.motion.BhopModule
import com.fluxclient.game.module.motion.FlyModule
import com.fluxclient.game.module.motion.HighJumpModule
import com.fluxclient.game.module.motion.JetPackModule
import com.fluxclient.game.module.motion.MotionFlyModule
import com.fluxclient.game.module.motion.PlayerTPModule
import com.fluxclient.game.module.motion.SpeedModule
import com.fluxclient.game.module.motion.SpiderModule
import com.fluxclient.game.module.motion.SprintModule
import com.fluxclient.game.module.visual.CrosshairModule
import com.fluxclient.game.module.visual.DamageTextModule
import com.fluxclient.game.module.visual.ESPModule
import com.fluxclient.game.module.visual.FullbrightModule
import com.fluxclient.game.module.visual.MinimapModule
import com.fluxclient.game.module.visual.NetworkInfoModule
import com.fluxclient.game.module.visual.NoHurtCameraModule
import com.fluxclient.game.module.visual.PlayerJoinModule
import com.fluxclient.game.module.visual.SpeedDisplayModule
import com.fluxclient.game.module.visual.WorldStateModule
import com.fluxclient.game.module.visual.ZoomModule
import com.fluxclient.game.module.visual.TargetHudModule
import com.fluxclient.game.module.world.FreeCameraModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import java.io.File

object ModuleManager {

    private val _modules: MutableList<Module> = ArrayList()

    val modules: List<Module> = _modules

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    init {
        with(_modules) {
            // Combat
            add(WAuraModule())
            add(HotbarSwitcherModule())
            add(KillauraModule())
            add(AutoFightModule())
            add(InfiniteAuraModule())
            add(ACAModule())
            add(AutoTotemModule())
            add(AutoHvHModule())
            add(EnemyHunterModule())
            add(AntiKnockbackModule())

            add(AntiCrystalModule())
            add(HitAndRunModule())
            add(HitboxModule())
            add(CrystalSmashModule())
            add(TriggerBotModule())

            // Motion
            add(MotionFlyModule())
            add(PlayerTPModule())
            add(FlyModule())
            add(SpeedModule())
            add(AirJumpModule())
            add(NoClipModule())
            add(JetPackModule())
            add(HighJumpModule())
            add(BhopModule())
            add(SprintModule())
            add(AutoWalkModule())
            add(AntiAFKModule())
            add(SpiderModule())

            // Visual
            add(DamageTextModule())
            add(ESPModule())
            add(PlayerJoinModule())
            add(ZoomModule())
            add(CoordinatesModule())
            add(NoHurtCameraModule())
            add(SpeedDisplayModule())
            add(NetworkInfoModule())
            add(WorldStateModule())
            add(MinimapModule())
            add(CrosshairModule())
            add(TargetHudModule())
            add(FullbrightModule())

            // World
            add(FreeCameraModule())
            add(TimeShiftModule())
            add(WeatherControllerModule())
            add(EffectsModule())
            add(ParticlesModule())
            add(AntiDebuffModule())

            // Misc

            add(AutoDisconnectModule())
            add(ArrayListModule())
            add(ToggleSoundModule())
            add(ChestStealerModule())
            add(DesyncModule())
            add(SpammerModule())
            add(WaterMarkModule())
            add(PositionLoggerModule())
            add(NoChatModule())
            add(CommandHandlerModule())
            add(ReplayModule())
            add(PieChartModule())
            add(FakeDeathModule())
            add(FakeXPModule())
            add(MinerModule())
        }
    }

    fun saveConfig() {

        if (!AppContext.isInitialized) {
            return
        }

        val configsDir = AppContext.instance.filesDir.resolve("configs")
        configsDir.mkdirs()

        val config = configsDir.resolve("UserConfig.json")
        val jsonObject = buildJsonObject {
            put("modules", buildJsonObject {
                _modules.forEach {
                    if (it.private) {
                        return@forEach
                    }
                    put(it.name, it.toJson())
                }
            })
        }

        config.writeText(json.encodeToString(JsonObject.serializer(), jsonObject))
    }

    fun loadConfig() {

        if (!AppContext.isInitialized) {
            return
        }

        val configsDir = AppContext.instance.filesDir.resolve("configs")
        configsDir.mkdirs()

        val config = configsDir.resolve("UserConfig.json")
        if (!config.exists()) {
            return
        }

        val jsonString = config.readText()
        if (jsonString.isEmpty()) {
            return
        }

        try {
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            val modules = jsonObject["modules"]?.jsonObject ?: return

            _modules.forEach { module ->
                (modules[module.name] as? JsonObject)?.let {
                    module.fromJson(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exportConfig(): String {
        val jsonObject = buildJsonObject {
            put("modules", buildJsonObject {
                _modules.forEach {
                    if (it.private) {
                        return@forEach
                    }
                    put(it.name, it.toJson())
                }
            })
        }
        return json.encodeToString(JsonObject.serializer(), jsonObject)
    }

    fun importConfig(configStr: String) {
        try {
            val jsonObject = json.parseToJsonElement(configStr).jsonObject
            val modules = jsonObject["modules"]?.jsonObject ?: return

            _modules.forEach { module ->
                modules[module.name]?.let {
                    if (it is JsonObject) {
                        module.fromJson(it)
                    }
                }
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid config format: ${e.message}")
        }
    }

    fun exportConfigToFile(context: Context, filePath: String): Boolean {
        return try {
            val file = if (filePath.contains("/")) {
                File(filePath)
            } else {
                val configsDir = context.getExternalFilesDir("configs")
                configsDir?.mkdirs()
                File(configsDir, if (filePath.endsWith(".json")) filePath else "$filePath.json")
            }

            file.parentFile?.mkdirs()
            file.writeText(exportConfig())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getFluxClientConfigsDirectory(): File? {
        return try {
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val baseDir = if (documentsDir.exists() || documentsDir.mkdirs()) {
                documentsDir
            } else {
                downloadsDir
            }

            val wclientDir = File(baseDir, "FluxClient")
            val configsDir = File(wclientDir, "configs")

            if (configsDir.exists() || configsDir.mkdirs()) {
                configsDir
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun importConfigFromFile(context: Context, uri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val configStr = input.bufferedReader().readText()
                importConfig(configStr)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}