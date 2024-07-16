var alert = document.getElementById('alert')
var inputFileUpload = document.getElementById('inputFileUpload')
var txtCertOf = document.getElementById('txtCertOf')
var txtDesc1 = document.getElementById('txtDesc1')
var txtDesc2 = document.getElementById('txtDesc2')
var txtEventName = document.getElementById('txtEventName')
var txtOrganizer = document.getElementById('txtOrganizer')
var txtDesc3 = document.getElementById('txtDesc3')
var txtDesc4 = document.getElementById('txtDesc4')
var inputFileSignImage = document.getElementById('inputFileSignImage')
var txtIssuerName = document.getElementById('txtIssuerName')
var txtIssuerTitle = document.getElementById('txtIssuerTitle')
var btnUpload = document.getElementById('btnUpload')
var tableNameList = document.getElementById('tableNameList')


//filter file
inputFileUpload.addEventListener('change', function () {    
    var file = this;

    //filter file size
    var fileSize = file.files[0].size;
    var maxSize = 102400; //100 kb

    if(fileSize > maxSize){
        alert.innerHTML = showAlert("File size exceeded")
        inputFileUpload.className = inputFileUpload.className + " is-invalid"
        file.value = '';
        return;
    }

    //filter file type
    var fileName = file.files[0].name;
    var fileExtension = fileName.split('.').pop().trim().toLowerCase().toString();
    if (fileExtension !== 'xls' && fileExtension !== 'xlsx') {
        alert.innerHTML = showAlert("Unacceptable file type")
        file.value = '';
        return
    } 

    alert.innerHTML = "";
    file.className = inputFileSignImage.className.replace(" is-invalid", "")

});


inputFileSignImage.addEventListener('change', function () { 
    var image = this;

    //filter image size
    var imageSize = image.files[0].size;
    var maxSize = 512000 ; //500 kb

    if(imageSize > maxSize){
        alert.innerHTML = showAlert("Image size exceeded")
        inputFileSignImage.className = inputFileSignImage.className + " is-invalid"
        image.value = '';
        return;
    }

    //filter file image type
    var imageName = image.files[0].name;
    var imageExtension = imageName.split('.').pop().trim().toLowerCase().toString();
    if (imageExtension !== 'png') {
        alert.innerHTML = showAlert("Unacceptable image file type")
        image.value = '';
        return
    } 
    alert.innerHTML = "";
    image.className = inputFileSignImage.className.replace(" is-invalid", "")
})


var textInputs = document.querySelectorAll('input[type="text"]');

textInputs.forEach(function(input) {
    input.addEventListener('focusout', function() {
        var txtLength = this.value.length
        if(txtLength > 70){
            alert.innerHTML = showAlert("Text cannot be more than 70 character")
            this.className = this.className + " is-invalid"
            return
        }
        alert.innerHTML = ""
        this.className = this.className.replace(" is-invalid", "")
    });
});

btnUpload.addEventListener('click', function(){
    if(inputFileUpload.value == ''){
        alert.innerHTML = showAlert("No file selected")
        inputFileUpload.className = inputFileUpload.className + " is-invalid"
        return
    }else if(inputFileSignImage.value == ''){
        alert.innerHTML = showAlert("No image selected")
        inputFileSignImage.className = inputFileSignImage.className + " is-invalid"
        return
    }else{
        inputFileUpload.className = inputFileUpload.className.replace(" is-invalid", "")
        inputFileSignImage.className = inputFileSignImage.className.replace(" is-invalid", "")
    }

    //get value
    var file = inputFileUpload.files[0];
    var certOf = txtCertOf.value.trim() 
    var desc1 = txtDesc1.value.trim()  
    var desc2  = txtDesc2.value.trim()  
    var eventName = txtEventName.value.trim()    
    var organizer = txtOrganizer.value.trim()   
    var desc3 = txtDesc3.value.trim()   
    var desc4 = txtDesc4.value.trim()   
    var signImage = inputFileSignImage.files[0];
    var issuerName = txtIssuerName.value.trim()   
    var issuerTitle= txtIssuerTitle.value.trim()   
    
    var formData = new FormData();
    formData.append('file', file);
    formData.append('certOf', certOf);
    formData.append('desc1', desc1);
    formData.append('desc2', desc2);
    formData.append('eventName', eventName);
    formData.append('organizer', organizer);
    formData.append('desc3', desc3);
    formData.append('desc4', desc4);
    formData.append('signImage', signImage);
    formData.append('issuerName', issuerName);
    formData.append('issuerTitle', issuerTitle);

    tableNameList.className = tableNameList.className.replace("d-none","d-block")
    tableNameList.innerHTML = `<h1>Loading...</h1>`

    fetch('http://localhost:8081/api.certificategenerator/processfile', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        return response.text();
    })
    .then(data => {
        displayData(data)
    })
    .catch(error => {
        console.log('Error:', error);
        tableNameList.innerHTML = `<h1>Error Occour</h1>`
    });
    
})

function displayData(data){
    try {
        var dataObj = JSON.parse(data);
    } catch (error) {
        console.error("Error :", error);
        alert.innerHTML = showAlert("Error Occour");
        tableNameList.innerHTML = `<h1>Error Occour</h1>`
    }
        
    if(dataObj.hasOwnProperty("body")){
        tableNameList.innerHTML = tableData(dataObj.body);
        new DataTable('#nameList');
    }else{
        alert.innerHTML = showAlert("Error : " + dataObj.cause);
        tableNameList.innerHTML = `<h1>${dataObj.cause}</h1>`
    }
}

function tableData(recipients){

    var row = ``;

    recipients.forEach(function(recipient, index) {
        row = row + `<tr>
            <td>${recipient.id}</td>
            <td>${recipient.name}</td>
            <td>${recipient.issuedDate}</td>
            <td><a class="btn btn-primary" href="http://localhost:8081/api.certificategenerator/${recipient.id}/pdf" target="_blank">View</a>
           <a class="btn btn-primary" href="http://localhost:8081/api.certificategenerator/${recipient.id}/download" target="_blank">Download</a>
        </tr>`
    });

    return `<table id="nameList" class="table display" style="width:100%">
    <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Issued Date</th>
            <th>Option</th>
        </tr>
    </thead>
    <tbody>`+
        row
    + `</tbody>
</table>
<br>
<div>
    <a class="btn btn-primary" href="http://localhost:8081/api.certificategenerator/download" target="_blank">Download All</a>
</div>`
}

function showAlert(msg){
    return `<div class="alert alert alert-danger">
        ${msg}
    </div>`

}


