
package com.google.sps.servlets;

import com.google.sps.data.SearchProject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import com.google.gson.Gson;
import java.sql.Timestamp;
import me.xdrop.fuzzywuzzy.FuzzySearch;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Get the datastore instance:
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Iterable<Entity> results;

        //An ArrayList of searched projects:
        ArrayList<SearchProject> projects = new ArrayList<SearchProject>();

        //Check if there is any parameters on the request:
        if(request.getQueryString() != null){

            //Query the datastore based on the request:
            results = queryProjectDatastore(request, datastore);

        } else {

            //In case of an empty query: Return nothing or everything:
            Query query = new Query("Project").addSort("timestamp", SortDirection.DESCENDING);
    
            //Query the datastore and get the results:
            results = datastore.prepare(query).asIterable();

        }

        for(Entity entity : results){

            //Get the properties of the entity:
            String title = (String) entity.getProperty("title");
            String email = (String) entity.getProperty("owner_email");
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

        //Create a JSONString from the ArrayList:
        String json = toJsonString(projects);

        //Write the JSON to the response:
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(json);

    }

    //This post method is only to put dummy entities in the datastore for test purposes:
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Get a datastore instance:
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        //Create test Project Entity with dummy values:
        Entity projectEntity = new Entity("Project");
        projectEntity.setProperty("title", "Quantic computation in a potato");
        projectEntity.setProperty("description", "This is a test description but its about potatos.");    
        projectEntity.setProperty("timestamp", System.currentTimeMillis());
        projectEntity.setProperty("giturl", "https://github.com/Manniec/personal-project-search");
        //projectEntity.setProperty("author", "Manuel Doe"); //This can be the username or email.
        projectEntity.setProperty("owner_email", "james_may@gmail.com");

        //TAGS:
        projectEntity.setProperty("language", "English"); //In a future this can be an array of languages.
        projectEntity.setProperty("timezone", "Europe/Amsterdam"); //Time Zone
        projectEntity.setProperty("ratediff", "hard"); //Difficulty
        projectEntity.setProperty("timecommit", "1-3"); //Time commitment
        projectEntity.setProperty("collabtype", "online"); //Collaboration type
        //projectEntity.setProperty("image", "default"); //This is optional. Set to "default" to tell the JS to display the placeholder project image.

        //Put the Entity in the datastore:
        datastore.put(projectEntity);

        System.out.println("Dummy project posted!");

        // Redirect back to the HTML search page.
        response.sendRedirect("/search.html");

    }

    private Iterable<Entity> queryProjectDatastore(HttpServletRequest request, DatastoreService datastore){

            //Get parameters for the search:
            String searchBarText = request.getParameter("bar");
            String language = request.getParameter("tag-languages");
            String zone = request.getParameter("tag-zones");
            String difficulty = request.getParameter("difficulties");
            String timeCommitment = request.getParameter("time-commitment");
            String collabType = request.getParameter("collab-type");

            //ArrayList of query subfilters for the composite filter:
            Collection filters = new ArrayList<FilterPredicate>();

            //Store the results of the query:
            PreparedQuery results;

            //Create and add query filters for each tag based on the request parameters:
            if(!"".equals(language)){

                //Property name, comparison operator, search value:
                filters.add(new FilterPredicate("language", FilterOperator.EQUAL, language));

            }

            if(!"".equals(zone)){

                filters.add(new FilterPredicate("timezone", FilterOperator.EQUAL, zone));

            }

            if(difficulty != null){

                filters.add(new FilterPredicate("ratediff", FilterOperator.EQUAL, difficulty));

            }

            if(timeCommitment != null){

                filters.add(new FilterPredicate("timecommit", FilterOperator.EQUAL, timeCommitment));

            }

            if(collabType != null){

                filters.add(new FilterPredicate("collabtype", FilterOperator.EQUAL, collabType));

            }

            //Check for single filter queries (individual filter) and empty queries (no filters):
            if(filters.size() == 0) {

                //In case of an empty query: Return nothing or everything:
                Query query = new Query("Project").addSort("timestamp", SortDirection.DESCENDING);
    
                //Query the datastore and get the results:
                results = datastore.prepare(query);

            } else if(filters.size() == 1) {
                
                Iterator<FilterPredicate> iterator = filters.iterator();

                Filter queryFilter = iterator.next();

                //Create a query with the single filter:
                Query query = new Query("Project").setFilter(queryFilter).addSort("timestamp", SortDirection.DESCENDING);
    
                //Query the datastore and get the results:
                results = datastore.prepare(query);

            } else {

                //Create a composite filter with all the previous filters:
                Filter queryFilter = new CompositeFilter(CompositeFilterOperator.AND, filters);

                //Create a query with the composite filter and sort:
                Query query = new Query("Project").setFilter(queryFilter).addSort("timestamp", SortDirection.DESCENDING);
    
                //Query the datastore and get the results:
                results = datastore.prepare(query);

            }

            Iterable<Entity> finalResults;

            //Needs to return an iterable:
            finalResults = results.asIterable();

            if(!"".equals(searchBarText)){

                //If there is text in the searchBar, perform a fuzzySearch in the queried projects:
                finalResults = fuzzySearch(searchBarText, finalResults);

            }

            //Return the results:
            return finalResults;

    }

    //Filters the already filtered entities from the datastore query by it's title similarity to the searchBarText:
    private Iterable<Entity> fuzzySearch(String searchText, Iterable<Entity> entities){

        //0 is completely different. 100 is equal:
        final int threshold = 30;

        //ArrayList of projects that matched the free text search:
        ArrayList<Entity> matches = new ArrayList<Entity>();

        //Do a FuzzySearch between the searchBarText and every entity's title:
        for(Entity entity : entities){

            String currentTitle = (String) entity.getProperty("title");

            if(FuzzySearch.ratio(searchText, currentTitle) > threshold){

                matches.add(entity);

            }

        }

        return matches;

    }

    //Converts the ArrayList to JSON using GSON:
    private String toJsonString(ArrayList<SearchProject> projectList){

        Gson gson = new Gson();
        String json = gson.toJson(projectList);
        return json;

    }

}