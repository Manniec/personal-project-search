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

// Author: David V.P.
/** Servlet responsible for listing tasks. */
@WebServlet("/profile")
public class ProfileInfoServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    //   TODO: include datastore for profile's and retrieve profile data from datastore
    
    // parse email for name
    String email = request.getParameter("user_id");
    int endIndex;
    String name;
    if(email != null && email.contains("@")){
        endIndex = email.indexOf("@");
        name = email.substring(0, endIndex);
        System.out.println("inside");
    }
    else{
        name = "John Doe";
    }

    // profile info
    Map<String, Integer> thumbs = new HashMap<>();
    thumbs.put("up", 10);
    thumbs.put("down", 1);
    Map<String, String> contactInfo = new HashMap<>();
    contactInfo.put("Email", email);
    String country = "US";

    Profile profile1 = new Profile(name, thumbs, contactInfo, country);

    // return Profile as json string 
    Gson gson = new Gson();
    String json = gson.toJson(profile1);
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().println(json);

  }

}
