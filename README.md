# Reduktor
Implementation of Unidirectional Data Flow for Android with Kotlin Coroutines 

[![](https://jitpack.io/v/devDebajo/reduktor.svg)](https://jitpack.io/#devDebajo/reduktor)

# Principle of working
<img src="img/Diagram.png" alt="Diagram"/>

# Install

In project level `build.gradle` add jitpack.io repository:
```kotlin
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add it to module level `build.gradle`:
```kotlin
implementation 'com.github.devDebajo:reduktor:{latest_version}'
```

License
-------

    Copyright (C) 2022 debajo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
