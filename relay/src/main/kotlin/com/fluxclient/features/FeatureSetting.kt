package com.fluxclient.features

/** Typed, bounded settings used by Flux features; invalid values fail closed. */
sealed class FeatureSetting<T>(val key: String, val default: T) {
    var value: T = default
        private set
    protected fun assign(candidate: T) { value = candidate }
}

class BooleanSetting(key: String, default: Boolean = false) : FeatureSetting<Boolean>(key, default) {
    fun set(candidate: Boolean) = assign(candidate)
}

class IntSetting(key: String, default: Int, private val range: IntRange) : FeatureSetting<Int>(key, default) {
    fun set(candidate: Int) { require(candidate in range); assign(candidate) }
}

class DoubleSetting(key: String, default: Double, private val range: ClosedFloatingPointRange<Double>) : FeatureSetting<Double>(key, default) {
    fun set(candidate: Double) { require(candidate in range); assign(candidate) }
}
