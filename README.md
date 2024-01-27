# Fetch Weather

## Getting set up
+ Run on Android Studio version :
  Android Studio Hedgehog | 2023.1.1
  Build #Al-231.9392.1.2311.11076708, built on November 9, 2023
+ SDK: 33
+ Java SDK 17/13 (jvmTarget = 17)


## Overview how to used FetchWeather
* When the opens app you need to enter your name before using the app. The step will be skipped when opening the app again.
* On Dashboard have 3 screen
    ```
    - Search screen: Enter a location that has support forecast weather. (Note: click icon search right Text Field begin search data) 
    - Favorite Location: All locations your favorite saved into the local database will be shown here. 
    - Profile screen: Only show name and clear session when clicking logout.
    - Weather Details: On the Screen will show all information Weather location (3 days), If you want click the Float Button favorite to save the location for the next last using.
    ```  
* When you lose connection the Internet state screen warning is show lock all actions on the app.
* UnitTest I only write simply for `ViewModel`
* UITest only example page login.

## Architectures

* Single Activity (1 *Explicitly* Activity per project)
* MVVM (Model-View-ViewModel)
* DI using Koin

We use a simple architecture of `Fragment` -> `LifecycleObserver` -> `ViewModel` -> `Usecase`-> `Repo` -> `Service`

## Code Styles

This project uses KtLint, this can be run from the command line via `gradlew ktlintFormat`

## Testing
FetchWeather have taken ownership of UI tests, using Appium/Espresso (can using firebase test lab apply) . Unit tests should be written for `ViewModel`, and above. Fragments and Activities will not be test, so the least amount of code the better.

* Folders:
  ** `test` is used for unit tests that are not Android specific (jvm based tests)

* Naming:
  ** Unit tests - use backticks

* Running tests:
```
- When running tests via the ui (using the play button), make sure your Build Variant is in Debug mode.
- Open "build.gradle(app):119" You can click button play run all test into project and generated coverage report. 
**Commands to run project tests:
- For local unit tests: "./gradlew testStagingDebugUnitTest", this can also be access in the gradle sidebar.
```

## Data Type Objects
When modelling (above of all for model-objects coming from the server), try to keep `data` classes for simple
data holding structures. Try to keep data models inside a model / models folder, this will help Proguard. If not possible, please annotate with `@Keep`
#### Example
`data class Account(val id: String, val name: String)`

## UI declarations in XML
When declaring a new `@+id/`:

1. Keep it _lowercase_snake_case_
2. Add some information on it by prefixing it with what you're doing. (`@+id/feature1_id`, `@+id/feature2_id`)

## Logging
Only use `Timber` to log.

## Build Variants

We use: Debug, and Release. Release will make the app not debuggable and enable Proguard

## Build Flavors

We use Dev, Staging, and Prod


