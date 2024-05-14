document.addEventListener("DOMContentLoaded", function () {

    function redirectToCreateTest() {
        window.location.href = "teacher_create_test";
    }


    const createTestBtn = document.getElementById('createTestBtn');
    createTestBtn.addEventListener('click', redirectToCreateTest);


});
