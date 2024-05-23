function leaveCourse(courseId) {
    if (confirm("Are you sure you want to leave this course?")) {
        fetch('/student_leave_course/' + courseId, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                window.location.href = '/student_home';
                return response.json();
            })
            .then(data => {
                const deletedRow = document.getElementById(`course_${courseId}`);
                if (deletedRow) {
                    deletedRow.remove();
                } else {
                    console.error('Course row not found in the table');
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
            });
    }
}
