function getAllProjects(){

    fetch("/search").then(response => response.json()).then((projects) => {

        console.log(projects);

        //Iterates the JSON by keys:
        for(let project of projects){

            console.log(project);

        }

    })

}