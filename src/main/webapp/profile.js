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


/** Fetches the profile from the server and adds them to the DOM. */
function loadProfile() {
    fetch('/profile').then(response => response.json()).then((profile) => {

        // initialize elements
        const nameElement = document.getElementById("name");
        const contactInfoElement = document.getElementById("contact-info");
        const reviewListElement = document.getElementById("review-list");

        // display profile
        nameElement.innerText = profile.name;
        console.log(profile.name);
        contactInfoElement.innerHTML = profile.contactInfo.Email + "<br/>" + profile.contactInfo.Mobile;
        console.log(profile.contactInfo.Email + "<br/>" + profile.contactInfo.Mobile);
        contactInfoElement.display = "none";

        for (var i = 0; i < profile.reviews.length; i++) {
            const reviewElement = document.createElement('li');
            reviewElement.innerText = profile.reviews[i];
            console.log(profile.reviews[i]);
            reviewElement.className = 'horiz-center';
            reviewListElement.appendChild(reviewElement);
        }
    });

    // call other loading 
    loggingIn();
    loadProjects();
}

/* displays contact info if logged in. otherwise, asks to log in*/
function loggingIn(){
    fetch('/authentication').then(response => response.json()).then((authentication) =>{
        const contactInfoElement = document.getElementById("contact-info");
        // const logoutElement = document.getElementById("logout");
        const loggingElement = document.getElementById("logging");

        var isLoggedIn = (authentication.isLoggedIn == 'true'); 
        var loginUrl = authentication.login;
        var logoutUrl = authentication.logout;
        if(isLoggedIn){
            loggingElement.innerText = "LOG OUT";
            loggingElement.href = loginUrl;
            contactInfoElement.display = "inline";

        } else {
            loggingElement.innerText = "LOG IN";
            loggingElement.href = loginUrl;
            contactInfoElement.innerHTML = "Login <a href=\"" + loginUrl + "\">here</a> to see the contact info.";
            contactInfoElement.display = "inline";
        }
    });
}

/* Author: Diego V.A. */
function loadProjects(){
    fetch("/search").then(response => response.json()).then((projects) => {

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

            // for consistency sake:
            if(project.author == "John Doe"){
                counter++;

                //Create a card element with the current project values:
                const currentProjectCard = buildProjectCard(project.title, project.tags, project.author, project.image);

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

        }

        //In case there is a number of projects that is not a multiple of 3, add the last row with the remaining cards:
        if(counter % 3 != 0){

            //Add the row to the DOM:
            currentProjectRow.appendChild(currentProjectRowContent);
            projectDisplay.appendChild(currentProjectRow);

        }

    })

}

// Author: Diego V.A.
//Builds a project card element from the given parameter values:
function buildProjectCard(title, tags, author, image){

    const projectCard = document.createElement('div');
    projectCard.className = "project-card";

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