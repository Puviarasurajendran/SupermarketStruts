

document.addEventListener('DOMContentLoaded', function() {
    const links = document.querySelectorAll('.sidebar-menu a');

    for (const link of links) {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetPage = this.getAttribute('href');
            fetch(targetPage)
                .then(response => response.text())
                .then(html => {
                    document.querySelector('.main-content').innerHTML = html;
                });
        });
    }
});
