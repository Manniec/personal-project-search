
package com.google.sps.servlets;

import com.google.sps.data.SearchProject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.*;
import com.google.gson.Gson;

// Author: David V.P.
@WebServlet("/profile-projects")
public class ProfileProjectsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String email = request.getParameter("user_id");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Project").addSort("timestamp", SortDirection.DESCENDING);
        Iterable<Entity> results = datastore.prepare(query).asIterable();

        List <SearchProject> projects = new ArrayList<>();
        for (Entity entity : results) {
            String author = (String) entity.getProperty("owner_email");

            // only add project if author matches the email
            if(email.equals(author)){

                //Get the properties of the entity:
                String title = (String) entity.getProperty("title");
                String collab = (String) entity.getProperty("collabtype");
                String commitment = (String) entity.getProperty("timecommit");
                String difficulty = (String) entity.getProperty("ratediff");
                String language = (String) entity.getProperty("language");
                String zone = (String) entity.getProperty("timezone");
                //String image = (String) entity.getProperty("image");
                String image = null;
                long timestamp = (long) entity.getProperty("timestamp");
                String description = (String) entity.getProperty("description");
                String git = (String) entity.getProperty("giturl");

                //Create an array of tags to be displayed:
                String[] projectTagsList = {language, zone, difficulty, commitment, collab};

                //Create a SearchProject object with the entity properties:
                SearchProject currentProject = new SearchProject(title, projectTagsList, email, image, timestamp, description, git); //Image is set to null for the moment as it displays the default project image.

                //Add the current project to the ArrayList of projects:
                projects.add(currentProject);
            }
        }

        String json = toJsonString(projects);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(json);

    }

    //Converts the ArrayList to JSON using GSON:
    private String toJsonString(List<SearchProject> projectList){

        Gson gson = new Gson();
        String json = gson.toJson(projectList);
        return json;

    }

}