# To-Read Manager

I have built this app to let me manage my to-read links at one place on my phone. I can share tweets, webpages or anything else and then later open it from the app.

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.varunbarad.toreadmanager)

You can also find the signed APK for free [here](https://github.com/VarunBarad/to-read-manager-android/releases/latest).

## Running the application

To build the debug-variant of application, use the below command

```shell
# On Linux/Unix system
./gradlew assembleDebug

# On Windows system
gradlew.bat assembleDebug
```

You can then find the generated apk file at `<project-dir>/app/build/outputs/apk/debug/app-debug.apk`

## Running the tests

To run the unit-tests, use the below command

```shell
# On Linux/Unix system
./gradlew test

# On Windows system
gradlew.bat test
```

After it finishes, you can find the results of the test in this directory: `<project-dir>/app/build/reports/test/testReleaseUnitTest`

## Screenshots

![Current Entries](https://github.com/VarunBarad/to-read-manager-android/raw/master/screenshots/01%20Current%20Entries.png)

![Add Entry](https://github.com/VarunBarad/to-read-manager-android/raw/master/screenshots/02%20Add%20Entry.png)

![Archived Entries](https://github.com/VarunBarad/to-read-manager-android/raw/master/screenshots/03%20Archived%20Entries.png)
