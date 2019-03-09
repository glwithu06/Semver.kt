package com.github.glwithu06.semver

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class SemverTest {

    @Test
    fun testSemverConstructor() {
        val ver = Semver(1, 452, 368,
                listOf("rc", "alpha", "11", "log-test"), listOf("sha", "exp", "5114f85", "20190121"))

        assertEquals(ver.major, "1")
        assertEquals(ver.minor, "452")
        assertEquals(ver.patch, "368")
        assertEquals(ver.prereleaseIdentifiers.count(), 4)
        assertEquals(ver.prereleaseIdentifiers[0], "rc")
        assertEquals(ver.prereleaseIdentifiers[1], "alpha")
        assertEquals(ver.prereleaseIdentifiers[2], "11")
        assertEquals(ver.prereleaseIdentifiers[3], "log-test")
        assertEquals(ver.buildMetadataIdentifiers.count(), 4)
        assertEquals(ver.buildMetadataIdentifiers[0], "sha")
        assertEquals(ver.buildMetadataIdentifiers[1], "exp")
        assertEquals(ver.buildMetadataIdentifiers[2], "5114f85")
        assertEquals(ver.buildMetadataIdentifiers[3], "20190121")
    }

    @Test
    fun testEqualOtherObjects() {
        assertFalse(Semver("1.100.3").equals("1.101.3"))
    }

    @Test
    fun testEqualBasicVersion() {
        assertEquals(Semver("1.100.3"), Semver(major = "1", minor = "100", patch = "3"))
        assertNotEquals(Semver("1.100.3"), Semver("1.101.3"))
        // Big number
        assertEquals(Semver("69938113471411635120691317071569414.452.368"),
                Semver(major = "69938113471411635120691317071569414", minor = "452", patch = "368"))
        assertNotEquals(Semver("69938113471411635120691317071569414.452.36"),
                Semver("69938113471411635120691317071569414.452.368"))
        // prefixed
        assertEquals(Semver("semver.1.100.3"), Semver(major = "1", minor = "100", patch = "3"))
        assertNotEquals(Semver("semver.1.100.3"), Semver("semver.2.100.3"))
    }

    @Test
    fun testEqualPrefixedVersion() {
        assertEquals(Semver("semver.1.100.3"), Semver("v1.100.3"))
        assertEquals(Semver("semver.1.100.3-rc.1"), Semver("ver1.100.3-rc.1"))
    }

    @Test
    fun testEqualPrereleaseVersion() {
        assertEquals(Semver("1.100.3-rc.1"), Semver(major = "1", minor = "100", patch = "3", prereleaseIdentifiers = listOf("rc", "1")))
        assertNotEquals(Semver("1.100.3-rc.1"), Semver("1.101.3-rc.2"))
        assertNotEquals(Semver("1.100.3-alpha"), Semver("1.101.3-beta"))
        assertNotEquals(Semver("1.100.3-rc.a"), Semver("1.101.3-rc.1"))
    }

    @Test
    fun testEqualBuildmetadataVersion() {
        assertEquals(Semver("1.101.345+build.sha.111"), Semver(major = "1", minor = "101", patch = "345"))
        assertEquals(Semver("1.101.345+build.sha.111"), Semver("1.101.345+test.metadata"))
        assertEquals(Semver("1.101.345-rc.1+build.sha.111"), Semver("1.101.345-rc.1+test.metadata"))
        assertNotEquals(Semver("1.101.345-rc.1+build.sha.111"), Semver("1.101.345-rc.2+build.sha.111"))
    }

    @Test
    fun testCompareBasicVersion() {
        // Compare Major
        assert(Semver("1.100.0") < Semver("2.0.0"))
        // Compare Minor
        assert(Semver("1.99.231") < Semver("1.101.12344"))
        // Compare Patch
        assert(Semver("1.99.231") < Semver("1.99.12344"))
        // Ignore build metadata
        assertEquals(Semver("1.99.231+b"), Semver("1.99.231+a"))
    }

    @Test
    fun testCompareBasicAndPrereleaseVersion() {
        assert(Semver("1.99.231-alpha") < Semver("1.99.231"))
        // lhs <-> rhs
        assert(Semver("1.99.231") > Semver("1.99.231-alpha"))
    }

    @Test
    fun testComparePrereleaseVersion() {
        // alphabetical order
        assert(Semver("1.99.231-test.alpha") < Semver("1.99.231-test.beta"))
        assert(Semver("1.99.231-test.19b") < Semver("1.99.231-test.alpha"))

        // numeric < non-numeric
        assert(Semver("1.99.231-test.2") < Semver("1.99.231-test.19b"))

        // smaller-set < larger-set
        assert(Semver("1.99.231-alpha.beta") < Semver("1.99.231-alpha.beta.11"))

        // Ignore build metatdata
        assertEquals(Semver("1.99.231-alpha.beta+b"), Semver("1.99.231-alpha.beta+a"))
    }

    @Test
    fun testVersionToString() {
        val version = Semver("1.101.345-rc.alpha.11+build.sha.111.extended")

        assertEquals("1.101.345", version.toString(Semver.Style.COMPACT))
        assertEquals("1.101.345-rc.alpha.11", version.toString(Semver.Style.COMPARABLE))
        assertEquals("1.101.345-rc.alpha.11+build.sha.111.extended", version.toString(Semver.Style.FULL))
        assertEquals("1.101.345-rc.alpha.11+build.sha.111.extended", version.toString())
    }
}