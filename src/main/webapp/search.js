
//Cache the latest searched projects:
let currentProjects;

//Function that renders the project cards:
function renderProjects(projects){

    //Get the div where the project cards are gonna be displayed:
    const projectDisplay = document.getElementById("projects-display-section");
    let currentProjectRow = document.createElement('div'); //Create the first row container.
    let currentProjectRowContent = document.createElement('div'); //Create the first content container
    //We need to keep a counter to know when to close a row and start a new one:
    let counter = 0;

    //CSS style:
    currentProjectRow.className = "projects-row";
    currentProjectRowContent.className = "content-row";

    //Iterates the JSON by keys:
    for(let project of projects){

        counter++;

        //Create a card element with the current project values:
        const currentProjectCard = buildProjectCard(project.title, project.tags, project.author, project.image, counter);

        //Add it to the current row:
        currentProjectRowContent.appendChild(currentProjectCard);

        //Only 3 elements per row:
        if(counter % 3 == 0){

            //Add the row to the DOM:
            currentProjectRow.appendChild(currentProjectRowContent);
            projectDisplay.appendChild(currentProjectRow);

            //Create a new row and make it the current row:
            currentProjectRow = document.createElement('div');
            currentProjectRowContent = document.createElement('div');

            //CSS style to the new row:
            currentProjectRow.className = "projects-row";
            currentProjectRowContent.className = "content-row";

        }

    }

    //In case there is a number of projects that is not a multiple of 3, add the last row with the remaining cards:
    if(counter % 3 != 0){

        //Add the row to the DOM:
        currentProjectRow.appendChild(currentProjectRowContent);
        projectDisplay.appendChild(currentProjectRow);

    }

}

//Fetch the searched projects:
function getProjects(){

    const url = getURL();

    fetch(url).then(response => response.json()).then((projects) => {

        currentProjects = projects;
        renderProjects(projects);

    });

}

//Get the current url with the parameters:
function getURL(){

    const urlString = window.location.href;
    const url = urlString.replace('.html', '');
    
    return url;

}

//Fill the search form and return it:
function fillForm(bar){

    const searchBar = document.getElementById("search-bar");
    searchBar.value = bar;

    const form = new FormData(document.getElementById("search-form"));

    return form;

}

//Builds a project card element from the given parameter values:
function buildProjectCard(title, tags, author, image, projectId){

    const projectCard = document.createElement('div');
    projectCard.className = "project-card";
    //Set the function for when the project is clicked with the projectId being the index of that project on the cached results:
    projectCard.onclick = function() {getSingleProject(projectId);};

    const cardImageDiv = document.createElement('div');
    cardImageDiv.className = "project-image-div";

    //If the project has a personalized image, set it:
    if(image != "default"){

        cardImageDiv.style.backgroundImage = "url('" + image + "')";

    }

    const projectDescriptionDiv = document.createElement('div');
    projectDescriptionDiv.className = "project-decription";

    const projectTitle = document.createElement('h4');
    projectTitle.className = "project-title teko-text";
    projectTitle.innerText = title;

    const projectTags = document.createElement("p");
    projectTags.className = "project-tags karla-text";

    //Format the tags and append them to the tags section:
    for(let tag of tags){

        projectTags.textContent += "#" + tag + " ";

    }

    const projectAuthor = document.createElement("h5");
    projectAuthor.className = "project-author karla-text";
    projectAuthor.innerText = author;

    projectDescriptionDiv.appendChild(projectTitle);
    projectDescriptionDiv.appendChild(projectTags);
    projectDescriptionDiv.appendChild(projectAuthor);

    projectCard.appendChild(cardImageDiv);
    projectCard.appendChild(projectDescriptionDiv);

    return projectCard;

}

function getSingleProject(projectId){

    //Get the selected project from the latest cached search results:
    const selectedProject = currentProjects[(projectId - 1)];

    document.getElementById("popup-title").innerText = selectedProject.title;
    document.getElementById("popup-author").innerText = selectedProject.author;
    document.getElementById("popup-description").innerText = selectedProject.description;

    document.getElementById("popup-tags").innerText = "";

    const tags = selectedProject.tags;

    //Append tags and show them:
    for(let tag of tags){

        document.getElementById("popup-tags").innerText += "#" + tag + " ";

    }

    //Set links and redirects:
    document.getElementById("popup-github").onclick = function() {location.href = selectedProject.gitURL;};
    document.getElementById("user_id").value = selectedProject.author;

    //If the project has a personalized image, set it:
    if(selectedProject.image != "default"){

        document.getElementById("inner-image").src = "'" + selectedProject.image + "'";

    }

    //Show the modal:
    document.getElementById("modal").style.display = "block";
    
    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
    
        if (event.target == modal) {

            modal.style.display = "none";
        }

    }

}

//Hides the Modal:
function closePopUp(){

    document.getElementById("modal").style.display = "none";

}

//redirects to create project only if logged in
async function ifLoggedIn(){
    console.log('call auth servlet')
    const login = await fetch('/authentication').then(login => login.json());
    if(login.isLoggedIn){ //check if logged in
        console.log('logged-in');
        location.replace("newproject.html") //redirect to newproject.html
    }else{
        console.log('not logged-in');
        location.replace(login.login)       //redirect to login page
    }

}