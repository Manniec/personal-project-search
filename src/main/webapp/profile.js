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


// Author: Diego V.A.
//Get the current url with the parameters:
function getURL(){

    const urlString = window.location.href;
    const url = urlString.replace('.html', '');
    return url;

}

// Author: David V.P.
// Gets user_id param from URL
function getUserIDFromURL(){
    const url = window.location.search;
    const urlParams = new URLSearchParams(url);
    const user_id = urlParams.get('user_id');
    return user_id;
}

// Author: David V.P.
/** Fetches the profile from the server and adds them to the DOM. */
async function loadProfile() {
    const url = getURL();

    const profile = await fetch(url).then(login => login.json());
    console.log(profile);
    console.log("inside")
    
    //  prepare user_id param in URL, make auth call
    const user_id = getUserIDFromURL();
    const authURL = "/authentication?user_id=" + user_id; 
    const auth = await fetch(authURL).then(auth => auth.json());
    console.log(auth);

    // initialize elements
    const nameElement = document.getElementById("name");
    const contactInfoElement = document.getElementById("contact-info");
    const reviewListElement = document.getElementById("review-list");
    const thumbsDownElement = document.getElementById("thumbs-down");
    const thumbsUpElement = document.getElementById("thumbs-up");
    const loggingElement = document.getElementById("logging");

    // display profile
    nameElement.innerText = profile.name;
    console.log(profile.name);
    
 
    var isLoggedIn = (auth.isLoggedIn == 'true'); 
    // if user is logged in, display logout link and contact info
    // otherwise, display login link and ask user to login to see contact info
    if(isLoggedIn){ 
        console.log('logged-in');       
        if(typeof profile.contactInfo.Email !== 'undefined'){
            contactInfoElement.innerHTML = profile.contactInfo.Email;
            console.log(profile.contactInfo.Email);
        }
        loggingElement.innerText = "LOG OUT";
        loggingElement.href = auth.logout;
        contactInfoElement.display = "inline";
    }else{
        console.log('not logged-in');
        loggingElement.innerText = "LOG IN";
        loggingElement.href = auth.login;
        contactInfoElement.innerHTML = "Login <a href=\"" + auth.login + "\">here</a> to see the contact info.";
        contactInfoElement.display = "inline";
    }
    

    thumbsDownElement.innerText = profile.thumbs.down;
    thumbsUpElement.innerText = profile.thumbs.up;

    loadProjects();
}


// Author: David V.P.
// retrieves projects pertaining to user
function loadProjects(){

    // creates URL for user projects request with user_id param
    const user_id = getUserIDFromURL();
    const projectsURL = "/profile-projects?user_id=" + user_id;

    fetch(projectsURL).then(response => response.json()).then((projects) => {

        renderProjects(projects);

    })

}

/* Author: Diego V.A. */
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

// Author: Diego V.A.
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