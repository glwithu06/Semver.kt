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
`Semver` represent a semantic version according to the [Semantic Versioning Specification](http://semver.org/spec/v2.0.0.html).

## Installation

`Semver` doesn't contain any external dependencies.
These are currently support options:

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

`Semver` can be instantiated directly:

```Kotlin
val version = Semver(major = 1, minor = 23, patch = 45, prereleaseIdentifiers = listOf("rc", "1"), buildMetadataIdentifiers = listOf("B001"))

```
`minor`, `patch` are optional parameters default to "0".

`prereleaseIdentifiers`, `buildMetadataIdentifiers` are optional parameters default to empty list.

### Parse

You can create `Semver` from String.

```Kotlin
val version = Semver("1.23.45-rc.1+B001")

```
or from Numeric.

```Kotlin
val version = Semver(1.23)
```

```Kotlin
val version = Semver(10)
```

If the version is invalid, it throws a `IllegalArgumentException`.

### Compare

`Semver` class implements `Comparable`, it also overrides `equals` method.

So `Semver`s can be compared using the default operators for comparsion(`<` , `<=` , `>` ,`>=` ,`==` , `!=`).

This will comapre major, minor, patch and the prerelease identifiers according to the [Semantic Versioning Specification](http://semver.org/spec/v2.0.0.html).

## Contribution
Any pull requests and bug reports are welcome!

Feel free to make a pull request.
