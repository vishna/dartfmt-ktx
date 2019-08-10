# dartfmt-ktx

dartfmt-ktx is a wrapper around dart formatter `dartfmt` command

## Getting started

This repository is hosted via [jitpack](https://jitpack.io/) since it's by far the easiest delivery method while also being pretty transparent to the developer.

Make sure you have added jitpack to the list of your repositories:

```kotlin
maven("https://jitpack.io")
```

Then simply add the `mvel-ktx` dependency

```kotlin
dependencies {
    compile("com.github.vishna:dartfmt-ktx:master-SNAPSHOT")
}
```

## Example usage

```kotlin
val formattedDartCode  = "dart code".dartfmt()
```