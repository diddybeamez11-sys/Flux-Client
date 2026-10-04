package com.fluxclient.game.module.combat

import com.fluxclient.game.InterceptablePacket
import com.fluxclient.game.Module
import com.fluxclient.game.ModuleCategory
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket

class AntiKnockbackModule : Module("anti_knockback", ModuleCategory.Combat) {

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (!isEnabled) return

        val packet = interceptablePacket.packet
        if (packet is SetEntityMotionPacket) {


            if (packet.runtimeEntityId == session.localPlayer.runtimeEntityId) {
                interceptablePacket.intercept()
            }
        }
    }
}
