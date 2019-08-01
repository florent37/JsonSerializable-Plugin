# JsonSerializable-Plugin

Intellij Plugin for `Dart` and `Flutter` projects.
Generates the code that JsonSerializable need to generate the .g.dart files
      
1. Open a .dart file
2. Alt + N (on mac) / Shift + Insert (Windows)
3. Select "Generate JsonSerializable"

it will insert
<ul>
<li>the <i>json_annotation</i> import</li>
<li>the @JsonSerializable annotation</li>
<li>the fromJson method</li>
<li>the toJson method</li>
</ul>

<a href="https://github.com/florent37/JsonSerializable-Plugin/blob/master/medias/sample.gif">
<img src="https://github.com/florent37/JsonSerializable-Plugin/blob/master/medias/sample.gif">
</a>

# Installation

## Using Zip 

Download `JsonSerializable-Plugin.zip` then open the intellij plugin panel `Preferences > Plugin`,  then click `InstallPluginFromDisk` and select the downloaded zip file.

## Using MarketPlace

Search `JsonSerializable-Plugin` in the intellij plugin marketplace then click on install.

# JsonSerializable

https://pub.dev/packages/json_serializable

Add json_serializable dependencies into your pubspec.yaml file:
```
dependencies:
  json_annotation: ^3.1.0

dev_dependencies:
  build_runner: ^1.0.0
  json_serializable: ^3.1.0
```

## Generate .g.dart files

https://dart.dev/tools/build_runner

```
# From a directory that contains a pubspec.yaml file:
pub run build_runner build  # Dart SDK
flutter pub run build_runner build  # Flutter SDK
```

# License

    Copyright 2019 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.