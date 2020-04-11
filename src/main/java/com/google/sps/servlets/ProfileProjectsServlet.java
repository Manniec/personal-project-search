
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

@WebServlet("/profile-projects")
public class ProfileProjectsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Project").addSort("timestamp", SortDirection.DESCENDING);
        Iterable<Entity> results = datastore.prepare(query).asIterable();

        String email = request.getParameter("email");

        List <SearchProject> projects = new ArrayList<>();
        for (Entity entity : results) {
            //Get the properties of the entity:
            String title = (String) entity.getProperty("title");
            String author = (String) entity.getProperty("author");
            String collab = (String) entity.getProperty("collab");
            String commitment = (String) entity.getProperty("commitment");
            String difficulty = (String) entity.getProperty("difficulty");
            String language = (String) entity.getProperty("language");
            String zone = (String) entity.getProperty("zone");
            String image = (String) entity.getProperty("image");

            //Create an array of tags to be displayed:
            String[] projectTagsList = {language, zone, difficulty, commitment, collab};

            //Create a SearchProject object with the entity properties:
            SearchProject currentProject = new SearchProject(title, projectTagsList, author, null); //Image is set to null for the moment as it displays the default project image.

            //Add the current project to the ArrayList of projects:
            projects.add(currentProject);

            // Boolean isTheCorrectEmail = author.equals(email);
            // isTheCorrectEmail = true; 
            // if(isTheCorrectEmail){
            // }
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