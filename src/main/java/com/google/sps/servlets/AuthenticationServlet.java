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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.Profile;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.lang.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for authenticating user's log in status. */
@WebServlet("/authentication")
public class AuthenticationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      UserService userService = UserServiceFactory.getUserService();
      // initialize data
      Boolean isLoggedIn = userService.isUserLoggedIn();
      String redirectLink = "/profile.html";
      String logoutUrl = userService.createLogoutURL(redirectLink);
      String loginUrl = userService.createLoginURL(redirectLink);
      HashMap<String, String> authentication = new HashMap<String, String>();
      authentication.put("isLoggedIn", Boolean.toString(isLoggedIn));
      authentication.put("logout", logoutUrl);
      authentication.put("login", loginUrl); 

      //return data as JSON
      Gson gson = new Gson();
      String json = gson.toJson(authentication);
      response.setContentType("application/json; charset=utf-8");
      response.getWriter().println(json);
  }

}
