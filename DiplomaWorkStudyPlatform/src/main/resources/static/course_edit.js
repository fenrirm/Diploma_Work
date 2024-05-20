function addMaterialField() {
    const container = document.getElementById('materialsContainer');
    const input = document.createElement('input');
    input.type = 'text';
    input.name = 'material';
    input.className = 'form-input';
    container.appendChild(input);

    const saveBtn = document.getElementById('saveMaterialsBtn');
    saveBtn.style.display = 'block';
}

document.getElementById('saveMaterialsBtn').addEventListener('click', function() {
    const materials = document.querySelectorAll('#materialsContainer input');
    let materialString = '';
    materials.forEach(function(material, index) {
        materialString += material.value;
        if (index < materials.length - 1) {
            materialString += ';';
        }
    });

    const saveUrl = this.getAttribute('data-save-url');

    const xhr = new XMLHttpRequest();
    xhr.open('POST', saveUrl, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            console.log('Materials saved successfully');
            document.getElementById('materialsContainer').innerHTML = '';
            addMaterialsToList(materialString);
        } else if (xhr.readyState === XMLHttpRequest.DONE) {
            console.error('Error saving materials');
        }
    };
    xhr.send('material=' + encodeURIComponent(materialString));
});

function addMaterialsToList(materialString) {
    const materialsArray = materialString.split(';');
    materialsArray.forEach(function(material) {


        const tableBody = document.querySelector('.tasks-table-container tbody');
        const tableRow = document.createElement('tr');
        const materialCell = document.createElement('td');
        if (material.includes('https://') || material.includes('http://')) {
            const link = document.createElement('a');
            link.href = material;
            link.textContent = material;
            link.target = '_blank';
            materialCell.appendChild(link);
        } else {
            materialCell.textContent = material;
        }
        const actionCell = document.createElement('td');
        actionCell.className = 'button-container';
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-button';
        deleteButton.textContent = 'Delete';
        actionCell.appendChild(deleteButton);

        tableRow.appendChild(materialCell);
        tableRow.appendChild(actionCell);
        tableBody.insertBefore(tableRow, tableBody.firstChild);
    });


}
document.querySelectorAll('.delete-button').forEach(button => {
    button.addEventListener('click', function() {
        const materialId = this.dataset.materialId;
        const courseId = this.dataset.courseId;
        console.log(materialId)
        console.log(courseId)
        if (confirm("Are you sure you want to delete this material?")) {
            fetch(`/delete_material/`+courseId+'/'+materialId, {
                method: 'DELETE'
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    const deletedRow = this.closest('tr');
                    if (deletedRow) {
                        deletedRow.remove();
                    } else {
                        console.error('Deleted material row not found in the table');
                    }
                })
                .catch(error => {
                    console.error('There has been a problem with your fetch operation:', error);
                });
        }
    });
});