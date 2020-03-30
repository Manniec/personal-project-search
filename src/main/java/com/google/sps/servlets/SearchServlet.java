
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
import com.google.gson.Gson;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Dummy Data:
        ArrayList<SearchProject> projectData = new ArrayList<SearchProject>();

        String[] project1List = {"Tag1", "Tag2", "Tag3", "Tag4"};
        SearchProject project1 = new SearchProject("Test Project 1", project1List, "Test Author", null);

        String[] project2List = {"English", "Pacific/Midway", "Easy", "Online"};
        SearchProject project2 = new SearchProject("Test Project 2", project2List, "John Doe", null);

        String[] project3List = {"Spanish", "America/Mexico_City", "Hard", "Online"};
        SearchProject project3 = new SearchProject("Test Project 3", project3List, "Diego Velázquez", null);

        String[] project4List = {"German", "America/Los_Angeles", "Impossible", "Presential"};
        SearchProject project4 = new SearchProject("Test Project 4", project4List, "Toto Wolf", null);

        String[] project5List = {"English", "US/Eastern", "Medium", "Offline"};
        SearchProject project5 = new SearchProject("Test Project 5", project5List, "Danika Vanderlienden", null);

        String[] project6List = {"Japanese", "Asia/Tokyo", "Hard", "Online"};
        SearchProject project6 = new SearchProject("Test Project 6", project6List, "Takumi Fujiwara", null);

        projectData.add(project1);
        projectData.add(project2);
        projectData.add(project3);
        projectData.add(project4);
        projectData.add(project5);
        projectData.add(project6);

        String json = toJsonString(projectData);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(json);

    }

    //Converts the ArrayList to JSON using GSON:
    private String toJsonString(ArrayList<SearchProject> projectList){

        Gson gson = new Gson();
        String json = gson.toJson(projectList);
        return json;

    }

}