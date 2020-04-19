// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;
import java.util.*;

/** Profile information. */
public final class Profile {

  private final String name;
  private final Map<String, Integer> thumbs;
  private final Map<String, String> contactInfo;
  private final String country;


  public Profile(String name, Map<String, Integer> thumbs, Map<String, String> contactInfo, String country) {
    this.name = name;
    this.thumbs = thumbs;
    this.contactInfo = contactInfo;
    this.country = country;
  }
}