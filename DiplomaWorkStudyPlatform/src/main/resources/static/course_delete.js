function deleteCourse(courseId) {
    if (confirm("Are you sure you want to delete this course?")) {
        fetch('/teacher_delete_course/' + courseId, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                window.location.href = '/teacher_home';
                return response.json();
            })
            .then(data => {
                const deletedRow = document.getElementById(`course_${courseId}`);
                if (deletedRow) {
                    deletedRow.remove();
                } else {
                    console.error('Deleted course row not found in the table');
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
            });
    }
}
