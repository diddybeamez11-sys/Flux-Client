package com.fluxclient.game.module.misc

import com.fluxclient.game.InterceptablePacket
import com.fluxclient.game.Module
import com.fluxclient.game.ModuleCategory

class ToggleSoundModule : Module(
    name = "toggle_sounds",
    category = ModuleCategory.Misc,
    defaultEnabled = false
) {
    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
    }
}
