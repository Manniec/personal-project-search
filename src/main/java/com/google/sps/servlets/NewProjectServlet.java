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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//For saving the user who creates a project
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


/** Servlet responsible for creating new projects. */
@WebServlet("/post-project")
public class NewProjectServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    long timestamp = System.currentTimeMillis();                    //store post time
    //UserService userService = UserServiceFactory.getUserService();  //store creator
    //String email = userService.getCurrentUser().getEmail();
    //general project info
    String title = request.getParameter("title");                   //store name of project
    String description = request.getParameter("description");       //store description text
    String giturl = request.getParameter("giturl");                 //store git url 
    //tags
    String language = request.getParameter("tag-languages");        //store language
    String timezone = request.getParameter("tag-timez");             //store time-zone
    String ratediff = request.getParameter("difficulties");         //store difficulties
    String timecommit = request.getParameter("time-commitment");    //store time commitment
    String collabtyp = request.getParameter("collab-type");         //store collaboration method
    
    Entity projectEntity = new Entity("Project");
    //projectEntity.setProperty("owner_email", email);
    projectEntity.setProperty("timestamp", timestamp);
    projectEntity.setProperty("title", title);
    projectEntity.setProperty("description", description);
    projectEntity.setProperty("giturl", giturl);
    projectEntity.setProperty("language", language);
    projectEntity.setProperty("timezone", timezone);
    projectEntity.setProperty("ratediff", ratediff);
    projectEntity.setProperty("timecommit", timecommit);
    projectEntity.setProperty("collabtyp", collabtyp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(projectEntity);

    response.sendRedirect("/index.html");
  }
}