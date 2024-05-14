document.addEventListener("DOMContentLoaded", function () {
    function redirectToHome() {
            window.location.href = "teacher_home";
    }
    function redirectToTest() {
        window.location.href = "teacher_tests";
    }
    function redirectToStudents() {
        window.location.href = "teacher_students";
    }

    function redirectToCreateCourse() {
        window.location.href = "teacher_create_course";
    }

    function redirectToCreateTest() {
        window.location.href = "teacher_create_test";
    }

    function redirectToLogin() {
        window.location.href = "login";
    }

    function redirectToViewCourse() {
        window.location.href = "teacher_view_course";
    }



    // Get the courses button
    const coursesBtn = document.getElementById('coursesBtn');
    coursesBtn.addEventListener('click', redirectToHome);

    const testsBtn = document.getElementById('testsBtn');
    testsBtn.addEventListener('click', redirectToTest);

    const studentsBtn = document.getElementById('studentsBtn');
    studentsBtn.addEventListener('click', redirectToStudents);

    const createCourseBtn = document.getElementById('createCourseBtn');
    createCourseBtn.addEventListener('click', redirectToCreateCourse);

    const logoutBtn = document.getElementById('logoutBtn');
    logoutBtn.addEventListener('click', redirectToLogin);

    const viewCourseBtn = document.getElementById("viewCourseBtn")
    viewCourseBtn.addEventListener('click', redirectToViewCourse);
    //
    // const editCourseBtn = document.getElementById("editCourseBtn")
    // editCourseBtn.addEventListener('click', redirectToLogin);
    //
    const deleteCourseBtn = document.getElementById("deleteCourseBtn")
    deleteCourseBtn.addEventListener('click', deleteCourse);


    const createTestBtn = document.getElementById('createTestBtn');
    createTestBtn.addEventListener('click', redirectToCreateTest);
});