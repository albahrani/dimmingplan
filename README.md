[![Build Status](https://travis-ci.org/albahrani/dimmingplan.svg?branch=master)](https://travis-ci.org/albahrani/dimmingplan)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.github.albahrani%3Adimmingplan&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.albahrani%3Adimmingplan)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.albahrani/dimmingplan/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.albahrani/dimmingplan)
# dimmingplan | dimming timetable for Java
Realize timetable based dimming with interpolation in Java.

## PROJECT INFORMATION

Project website: https://github.com/albahrani/dimmingplan <br />
Project issues list: https://github.com/albahrani/dimmingplan/issues <br />
<br />
Release builds are available from Maven Central
```xml
  <dependency>
    <groupId>com.github.albahrani</groupId>
    <artifactId>dimmingplan</artifactId>
    <version>0.0.1</version>
  </dependency>
```

Snapshot builds are available via
```xml
  <dependency>
    <groupId>com.github.albahrani</groupId>
    <artifactId>dimmingplan</artifactId>
    <version>0.0.2-SNAPSHOT</version>
  </dependency>
  ...
  <repository>
    <id>sonatype-snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
```

## LICENSE
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0
  
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

## USAGE
### Create a dimming plan
```Java
  DimmingPlan plan = DimmingPlan.create();
```
### Create a Channel
```Java
  DimmingPlan plan = DimmingPlan.create();
  DimmingPlanChannel channel = plan.channel("ch1");
```
### Define a Timetable entry
```Java
  channel.define(LocalTime.of(6, 0), 0.0d);		
```
### Interpolate the plan / channel
```Java
  plan.interpolate();
  channel.interpolate();
```
### Use fluent API
```Java
  DimmingPlan plan = DimmingPlan.create();
  plan.channel("ch1").define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(12, 0), 100.0d).define(LocalTime.of(21, 0), 0.0d);
  plan.channel("ch2").define(LocalTime.of(0, 0), 33.0d);        
  plan.interpolate();
```
### Retrieve the percentage for a certain time
```Java
  OptionalDouble percentage = plan.channel("ch1").getPercentage(LocalTime.of(6, 0));
```
