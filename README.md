<p align="center">
  <a href="https://kotlinlang.org/" target="_blank">
    <img alt="Kotlin" src="https://img.shields.io/badge/kotlin-1.3.20-blue.svg">
  </a>
  <a href="https://travis-ci.org/glwithu06/Semver.kt" target="_blank">
    <img alt="Build Status" src="https://travis-ci.org/glwithu06/Semver.kt.svg?branch=master">
  </a>
  <a href="https://codecov.io/gh/glwithu06/Semver.kt/" target="_blank">
      <img alt="Codecov" src="https://img.shields.io/codecov/c/github/glwithu06/Semver.kt.svg">
    </a>
</p>

# Semantic Versioning

Semantic Versioning implementation in Kotlin.
`Semver` represents the versioning system specified in [Semantic Versioning Specification](http://semver.org/spec/v2.0.0.html).

## Installation

`Semver` requires no external dependencies. You can install the library via:

### Gradle
```gradle
// build.gradle
repositories {
    jcenter()
}
dependencies {
    implementation 'com.github.glwithu06.semver:semver:x.y.z'
}
```

### JitPack
```gradle
// build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.glwithu06:semver.kt:x.y.z'
}
```

## Usage

### Create

You can create a `Semver` instance like the following:

```Kotlin
val version = Semver(major = 1, minor = 23, patch = 45, prereleaseIdentifiers = listOf("rc", "1"), buildMetadataIdentifiers = listOf("B001"))

```
`minor`, `patch` are optional parameters and set to "0" by default.

`prereleaseIdentifiers`, `buildMetadataIdentifiers` are optional parameters and set to empty lists by default.

### Parse

You can create `Semver` from String:

```Kotlin
val version = Semver("1.23.45-rc.1+B001")

```
or from Numeric:

```Kotlin
val version = Semver(1.23)
```

```Kotlin
val version = Semver(10)
```

If the given argument has an invalid format, it throws a `IllegalArgumentException`.

### Compare

`Semver` class implements `Comparable` interface and overrides `equals()`, so their instances can be compared using the default operators including `<` , `<=` , `>` ,`>=` ,`==` , `!=`.

The comparison rules are specified in [Semantic Versioning Specification](http://semver.org/spec/v2.0.0.html).

## Contribution
Pull requests and bug reports are welcomed!

Feel free to make a pull request.
