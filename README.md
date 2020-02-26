[![Release](https://jitpack.io/v/albahrani/dimmingplan.svg)](https://jitpack.io/#albahrani/dimmingplan)
[![](https://jitci.com/gh/albahrani/dimmingplan/svg)](https://jitci.com/gh/albahrani/dimmingplan)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.github.albahrani%3Adimmingplan&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.albahrani%3Adimmingplan)
[![codebeat badge](https://codebeat.co/badges/ddfd1632-d5a2-4ae6-8121-081958334921)](https://codebeat.co/projects/github-com-albahrani-dimmingplan-master)
[![Known Vulnerabilities](https://snyk.io/test/github/albahrani/dimmingplan/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/albahrani/dimmingplan?targetFile=pom.xml)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Falbahrani%2Fdimmingplan.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Falbahrani%2Fdimmingplan?ref=badge_shield)
[![Hits-of-Code](https://hitsofcode.com/github/albahrani/dimmingplan)](https://hitsofcode.com/view/github/albahrani/dimmingplan)

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
    <version>0.0.3</version>
  </dependency>
```

Snapshot builds are available via
```xml
  <dependency>
    <groupId>com.github.albahrani</groupId>
    <artifactId>dimmingplan</artifactId>
    <version>0.0.4-SNAPSHOT</version>
  </dependency>
```
Don't forget to enable snapshots in your repsitory config 
```xml  
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


[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Falbahrani%2Fdimmingplan.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Falbahrani%2Fdimmingplan?ref=badge_large)

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
  DimmingPlan.create()
      .channel("ch1")
          .define(LocalTime.of(6, 0), 0.0d)
          .define(LocalTime.of(12, 0), 100.0d)
          .define(LocalTime.of(21, 0), 0.0d)
      .channel("ch2")
          .define(LocalTime.of(0, 0), 33.0d)
      .interpolate();
```
### Retrieve the percentage for a certain time
```Java
  OptionalDouble percentage = plan.channel("ch1").getPercentage(LocalTime.of(6, 0));
```
