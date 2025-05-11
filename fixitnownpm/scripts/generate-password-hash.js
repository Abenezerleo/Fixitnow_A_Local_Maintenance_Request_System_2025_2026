const bcrypt = require('bcrypt');

const password = 'Admin123!';
bcrypt.hash(password, 10)
    .then(hash => {
        console.log('Password Hash:', hash);
        console.log('\nUse this SQL command to create admin user:');
        console.log(`
USE fixitnow;

INSERT INTO user (
    name,
    email,
    phone,
    password,
    role,
    gender
) VALUES (
    'Admin User',
    'admin@fixitnow.com',
    '1234567890',
    '${hash}',
    'ADMIN',
    'MALE'
);`);
    })
    .catch(err => console.error('Error:', err)); 