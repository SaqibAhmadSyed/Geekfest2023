const speakeasy = require('speakeasy');

    // Verify the token
    var verified = speakeasy.totp.verify({
        secret: 'tKNml/g60Mlx#q{3xElxxEWI@ug?D6T.',
        encoding: 'ascii',
        token: '810423'
      });
  
      console.log('Token Verified:', verified);