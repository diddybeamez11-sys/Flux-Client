package com.fluxclient.features

/** Default original Flux feature set. The registry is deliberately explicit. */
object FluxFeaturePack {
    fun create(): FeatureRegistry = FeatureRegistry().also {
        it.register(NetworkStatsFeature())
        it.register(CombatFeature())
        it.register(MovementFeature())
        it.register(WorldFeature())
    }
}
