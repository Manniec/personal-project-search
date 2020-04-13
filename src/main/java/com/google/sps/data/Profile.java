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
  private final ArrayList<String> projectHistory;
  private final ArrayList<String> reviews;
  private final HashMap<String, String> contactInfo;

  public Profile(String name, ArrayList<String> projectHistory, ArrayList<String> reviews, HashMap<String, String> contactInfo) {
    this.name = name;
    this.projectHistory = projectHistory;
    this.reviews = reviews;
    this.contactInfo = contactInfo;
  }
}