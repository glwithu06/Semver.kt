package main.java.com.github.glwithu06.semver

import java.math.BigDecimal

data class Semver(
    val major: String,
    val minor: String = "0",
    val patch: String = "0",
    val prereleaseIdentifiers: Array<String> = emptyArray(),
    val buildMetadataIdentifiers: Array<String> = emptyArray()
) : Comparable<Semver> {
    constructor(major: Number,
                minor: Number = 0,
                patch: Number = 0,
                prereleaseIdentifiers: Array<String> = emptyArray(),
                buildMetadataIdentifiers: Array<String> = emptyArray())
            : this("$major", "$minor", "$patch", prereleaseIdentifiers, buildMetadataIdentifiers)

    enum class Style {
        SHORT, COMPARABLE, FULL
    }

    fun toString(style: Style): String {
        val version = arrayOf(major, minor, patch).joinToString(DOT_DELIMETER)
        val prerelease = prereleaseIdentifiers.let {
            if (it.size > 0) PRERELEASE_DELIMETER + it.joinToString(DOT_DELIMETER) else ""
        }
        val buildMetadata = buildMetadataIdentifiers.let {
            if (it.size > 0) BUILD_METADATA_DELIMETER + it.joinToString(DOT_DELIMETER) else ""
        }
        return when (style) {
            Style.SHORT -> version
            Style.COMPARABLE -> version + prerelease
            Style.FULL -> version + prerelease + buildMetadata
        }
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun toString(): String {
        return toString(Style.FULL)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Semver) {
            return major.toBigDecimal() == other.major.toBigDecimal() &&
                    minor.toBigDecimal() == other.minor.toBigDecimal() &&
                    patch.toBigDecimal() == other.patch.toBigDecimal() &&
                    prereleaseIdentifiers.zip(other.prereleaseIdentifiers)
                        .fold(true) { result, it ->
                            val itInDecimal = Pair(it.first.toBigDecimalOrNull(), it.second.toBigDecimalOrNull())
                            return !result && when {
                                itInDecimal.first != null && itInDecimal.second != null -> itInDecimal.first == itInDecimal.second
                                itInDecimal.first == null && itInDecimal.second == null -> it.first == it.second
                                else -> false
                            }
                        }
        }
        return false
    }

    override fun compareTo(other: Semver): Int {
        fun Semver.versionsInDecimal(): List<BigDecimal> {
            return arrayOf(major, minor, patch).map { it.toBigDecimal() }
        }

        for (it in this.versionsInDecimal() zip other.versionsInDecimal()) {
            if (it.first != it.second) return it.first.compareTo(it.second)
        }
        if (prereleaseIdentifiers.count() == 0) return if (other.prereleaseIdentifiers.count() == 0) 0 else 1
        if (other.prereleaseIdentifiers.count() == 0) return -1
        for (it in prereleaseIdentifiers zip other.prereleaseIdentifiers) {
            val lhs = it.first.toBigDecimalOrNull()
            val rhs = it.second.toBigDecimalOrNull()
            when {
                lhs != null && rhs != null -> if (lhs != rhs) return lhs.compareTo(rhs)
                lhs != null && rhs == null -> return -1
                lhs == null && rhs != null -> return 1
                else -> if (it.first != it.second) return it.first.compareTo(it.second)
            }
        }
        return prereleaseIdentifiers.count().compareTo(other.prereleaseIdentifiers.count())
    }

    companion object {
        internal val DOT_DELIMETER = "."
        internal val PRERELEASE_DELIMETER = "-"
        internal val BUILD_METADATA_DELIMETER = "+"
    }
}