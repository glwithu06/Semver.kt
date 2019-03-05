package main.kotlin.com.github.glwithu06.semver

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
private fun require(condition: Boolean, message: String) {
    contract { returns() implies condition }
    if (!condition) throw IllegalArgumentException(message)
}

private class SemverExt {
    companion object {
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
            require(input.substring(0..majorMatchResult.range.first) != "-", "Invalid version($input)")

            major = majorMatchResult.value
            searchIndex = majorMatchResult.range.last

            val versionNumericRegex = "$dotDelimiterInRegex[0-9]+".toRegex()
            minor = versionNumericRegex.find(input, searchIndex)?.also { searchIndex = it.range.last }?.value?.substringAfter(Semver.DOT_DELIMITER) ?: "0"
            patch = versionNumericRegex.find(input, searchIndex)?.also { searchIndex = it.range.last }?.value?.substringAfter(Semver.DOT_DELIMITER) ?: "0"

            val prereleaseRegex = "(?<=$prereleaseDelimiterInRegex)([0-9A-Za-z-$dotDelimiterInRegex]+)".toRegex()
            prereleaseIdentifiers = prereleaseRegex.find(input, searchIndex)?.value?.let { it.split(Semver.DOT_DELIMITER) } ?: emptyList()

            val buildMetadataRegex = "(?<=$buildMetadataDelimiterInRegex)([0-9A-Za-z-$dotDelimiterInRegex]+)".toRegex()
            buildMetadataIdentifiers = buildMetadataRegex.find(input, searchIndex)?.value?.let { it.split(Semver.DOT_DELIMITER) } ?: emptyList()

            val remainder= input.substring(searchIndex)
                .replace("${Semver.PRERELEASE_DELIMITER}" + prereleaseIdentifiers.joinToString(Semver.DOT_DELIMITER), "")
                .replace("${Semver.BUILD_METADATA_DELIMITER}" + buildMetadataIdentifiers.joinToString(Semver.DOT_DELIMITER), "")
            if (remainder.count() > 0) {
                throw IllegalArgumentException("Invalid version ($input)")
            }

            return Semver(major, minor, patch, prereleaseIdentifiers, buildMetadataIdentifiers)
        }
    }
}

fun Semver(version: String): Semver = SemverExt.parse(version)
fun Semver(version: Number): Semver = SemverExt.parse("$version")
