document.addEventListener("DOMContentLoaded", function () {
    let searchInput = document.getElementById('searchInput');

    searchInput.addEventListener('input', function () {
        let searchText = searchInput.value.toLowerCase();

        let rows = document.querySelectorAll('tbody tr');

        rows.forEach(function (row) {
            let title = row.querySelector('td:first-child').textContent.toLowerCase();
            if (title.includes(searchText)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });
});