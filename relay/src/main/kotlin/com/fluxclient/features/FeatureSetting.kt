package com.fluxclient.features

/** Typed, bounded settings used by Flux features; invalid values fail closed. */
sealed class FeatureSetting<T>(val key: String, val default: T) {
    var value: T = default
        private set
    fun set(candidate: T) { value = candidate }
}

class BooleanSetting(key: String, default: Boolean = false) : FeatureSetting<Boolean>(key, default)
class IntSetting(key: String, default: Int, private val range: IntRange) : FeatureSetting<Int>(key, default) {
    override fun toString() = "$key=$value"
    fun set(candidate: Int) { require(candidate in range); super.set(candidate) }
}
class DoubleSetting(key: String, default: Double, private val range: ClosedFloatingPointRange<Double>) : FeatureSetting<Double>(key, default) {
    fun set(candidate: Double) { require(candidate in range); super.set(candidate) }
}
