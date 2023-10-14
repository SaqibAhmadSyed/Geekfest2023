const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener('click', () => {
    container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener('click', () => {
    container.classList.remove("sign-up-mode");
});

function validateSignIn() {
    // Get values from the input fields
    var username = document.getElementById("signInUsername").value;
    var password = document.getElementById("signInPassword").value;

    // no neeed to check username for the sake of the project
    // Check if password is entered
    if (password.trim() === "") {
        alert("Please enter a password.");
    } else {
        // You can perform further actions here, like sending a request to a server for authentication.
        // For now, we'll just display an alert.
        if (password.length < 8) {
            alert("Password must be at least 8 characters long.");
        } else if (!/[a-z]/.test(password)) {
            alert("Password must contain at least one lowercase letter.");
        } else if (!/[A-Z]/.test(password)) {
            alert("Password must contain at least one uppercase letter.");
        } else if (!/[0-9]/.test(password)) {
            alert("Password must contain at least one number.");
        } else {
            window.location.href = '../qrcode/qrcode.html';
            alert("Sign In Successful!");
        }
    }
}
