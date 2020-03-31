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

package com.google.sps.servlets;

import com.google.sps.data.Profile;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for listing tasks. */
@WebServlet("/profile")
public class ProfileInfoServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // initialize Profile with dummy data
    String name = "John Doe";
    ArrayList<String> projectHistory = new ArrayList<String>();
    projectHistory.add("Project 1");
    projectHistory.add("Project 2");
    ArrayList<String> reviews = new ArrayList<String>();
    reviews.add("Very reliable!");
    reviews.add("Great communicator and 100% reliable!");
    HashMap<String, String> contactInfo = new HashMap<String, String>();
    contactInfo.put("Mobile", "(123) 456-7809");
    contactInfo.put("Email", "johndoe@gmail.com");
    Profile profile1 = new Profile(name, projectHistory, reviews, contactInfo);

    // return Profile as json string 
    Gson gson = new Gson();
    String json = gson.toJson(profile1);
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().println(json);

  }

}
