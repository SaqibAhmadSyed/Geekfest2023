const fs = require('fs');

fs.readFile('2FA/500-worst-passwords.txt', 'utf8', (err, data) => {
    if(err){
        console.error(err);
        return;
    }
    console.log(data);
});