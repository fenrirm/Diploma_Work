function deleteTest(testId, courseId) {
    if (confirm("Are you sure you want to delete this test?")) {
        fetch('/teacher_delete_test/' + testId, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                window.location.href = '/teacher_edit_course/'+courseId;

                return response.json();
            })
            .then(data => {
                const deletedRow = document.getElementById(`test_${testId}`);
                if (deletedRow) {
                    deletedRow.remove();
                } else {
                    console.error('Deleted test row not found in the table');
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
            });

    }
}
