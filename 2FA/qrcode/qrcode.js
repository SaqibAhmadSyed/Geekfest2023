const speakeasy = require('speakeasy');
const qrcode = require('qrcode');

// Generate a secret
var secret = speakeasy.generateSecret({
    name: "Geekfest2023"
});

console.log(secret);

// Create a data URL (img) for the QR code
qrcode.toDataURL(secret.otpauth_url, function (err, data) {
    if (err) {
        console.error(err);
        return;
    }

    console.log(data);
    document.getElementById('dynamicImage').src = data;

    // This function is called when the user clicks the "Confirm" button
    window.validateToken = function () {
        // Get the user-entered token from the input field
        var userToken = document.getElementById('userToken').value;

        // Verify the user-entered token
        var userVerified = speakeasy.totp.verify({
            secret: secret.ascii,
            encoding: 'ascii',
            token: userToken
        });

        console.log('User Token Verified:', userVerified);
        
        // You can use the userVerified variable as needed
        if (userVerified) {
            alert('Token Verified: true');
        } else {
            alert('Token Verified: false');
        }
    };

    // Delay for a moment to ensure the generated token is different
    setTimeout(() => {
        // Generate a time-based token
        var token = speakeasy.totp({
            secret: secret.ascii,
            encoding: 'ascii'
        });

        console.log('Generated Token:', token);

        // Verify the token
        var verified = speakeasy.totp.verify({
            secret: secret.ascii,
            encoding: 'ascii',
            token: token
        });

        console.log('Token Verified:', verified);
    }, 1000); // Adjust the delay time as needed
});
