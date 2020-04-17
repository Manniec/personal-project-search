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
    

    UserService userService = UserServiceFactory.getUserService();  //get creator email
    String email = userService.getCurrentUser().getEmail();
    
    Entity projectEntity = new Entity("Project");
    //store creator info
    projectEntity.setProperty("owner_email", email);                //Store creator email           
    //store post info     
    projectEntity.setProperty("timestamp", System.currentTimeMillis());             //store post time
    //store project info
    projectEntity.setProperty("title", request.getParameter("title"));              //store name of project
    projectEntity.setProperty("description", request.getParameter("description"));  //store description text
    projectEntity.setProperty("giturl", request.getParameter("giturl"));            //store git url
    //store tags
    projectEntity.setProperty("language", request.getParameter("tag-languages"));       //store language
    projectEntity.setProperty("timezone", request.getParameter("tag-timez"));           //store time-zone
    projectEntity.setProperty("ratediff", request.getParameter("difficulties"));        //store difficulties
    projectEntity.setProperty("timecommit", request.getParameter("time-commitment"));   //store time commitment
    projectEntity.setProperty("collabtype", request.getParameter("collab-type"));       //store collaboration method

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(projectEntity);

    response.sendRedirect("/search.html");
  }
}