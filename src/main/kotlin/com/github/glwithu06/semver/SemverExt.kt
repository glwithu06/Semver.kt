package main.kotlin.com.github.glwithu06.semver

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.max

@ExperimentalContracts
private fun require(condition: Boolean, message: String) {
    contract { returns() implies condition }
    if (!condition) throw IllegalArgumentException(message)
}

private class SemverExt {
    companion object {
        @UseExperimental(ExperimentalContracts::class)
        internal fun parse(input: String): Semver {
            val major: String
            val minor: String
            val patch: String
            val prereleaseIdentifiers: List<String>
            val buildMetadataIdentifiers: List<String>

            val dotDelimiterInRegex = "\\" + Semver.DOT_DELIMITER
            val prereleaseDelimiterInRegex = "\\" + Semver.PRERELEASE_DELIMITER
            val buildMetadataDelimiterInRegex = "\\" + Semver.BUILD_METADATA_DELIMITER

            val validateRegex = "^([0-9A-Za-z|$prereleaseDelimiterInRegex|$dotDelimiterInRegex|$buildMetadataDelimiterInRegex]+)$".toRegex()
            if (validateRegex.find(input)?.value?.count() != input.count()) {
                throw IllegalArgumentException("Invalid version ($input)")
            }

            var searchIndex = 0
            val majorMatchResult = "[0-9]+".toRegex().find(input, searchIndex)
            require(majorMatchResult != null, "No digits in $input")
            require(input.substring(0..max(0, majorMatchResult.range.first - 1)) != "-", "Invalid version ($input)")

            major = majorMatchResult.value
            searchIndex = majorMatchResult.range.last + 1

            val versionNumericRegex = "^($dotDelimiterInRegex)[0-9]+".toRegex()
            minor = versionNumericRegex.find(input.substring(searchIndex))?.also { searchIndex += it.range.last + 1 }?.value?.substringAfter(Semver.DOT_DELIMITER) ?: "0"
            patch = versionNumericRegex.find(input.substring(searchIndex))?.also { searchIndex += it.range.last + 1 }?.value?.substringAfter(Semver.DOT_DELIMITER) ?: "0"

            val prereleaseRegex = "(?<=$prereleaseDelimiterInRegex)([0-9A-Za-z|$prereleaseDelimiterInRegex|$dotDelimiterInRegex]+)".toRegex()
            prereleaseIdentifiers = prereleaseRegex.find(input, searchIndex)?.value?.let { it.split(Semver.DOT_DELIMITER) } ?: emptyList()

            val buildMetadataRegex = "(?<=$buildMetadataDelimiterInRegex)([0-9A-Za-z|$prereleaseDelimiterInRegex|$dotDelimiterInRegex]+)".toRegex()
            buildMetadataIdentifiers = buildMetadataRegex.find(input, searchIndex)?.value?.let { it.split(Semver.DOT_DELIMITER) } ?: emptyList()

            val prerelease = prereleaseIdentifiers.let { if (it.count() > 0) "${Semver.PRERELEASE_DELIMITER}" + it.joinToString(Semver.DOT_DELIMITER) else "" }
            val metadata = buildMetadataIdentifiers.let { if (it.count() > 0) "${Semver.BUILD_METADATA_DELIMITER}" + it.joinToString(Semver.DOT_DELIMITER) else "" }
            val remainder= input.substring(searchIndex)
                .replace(prerelease, "")
                .replace(metadata, "")
            if (remainder.count() > 0) {
                throw IllegalArgumentException("Invalid version ($input)")
            }

            return Semver(major, minor, patch, prereleaseIdentifiers, buildMetadataIdentifiers)
        }
    }
}

fun Semver(version: String): Semver = SemverExt.parse(version)
fun Semver(version: Number): Semver = SemverExt.parse("$version")

fun String.toVersion(): Semver {
    return Semver(this)
}

fun String.toVersionOrNull(): Semver? {
    return try {
        Semver(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun Number.toVersion(): Semver {
    return Semver(this)
}

fun Number.toVersionOrNull(): Semver? {
    return try {
        Semver(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}
