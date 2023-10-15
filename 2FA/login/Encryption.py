from cryptography.fernet import Fernet

# Generate a secret key (you should keep this secret)
secret_key = Fernet.generate_key()

# Create a Fernet cipher with the secret key
cipher = Fernet(secret_key)

# Get user input for the password
user_input_password = input("Enter your password: ")

# Encrypt the user's input password
encrypted_password = cipher.encrypt(user_input_password.encode())

print("Encrypted Password:", encrypted_password.decode())

# To decrypt and verify the password
try:
    decrypted_password = cipher.decrypt(encrypted_password).decode()
    print("Decrypted Password:", decrypted_password)
except Exception as e:
    print("Decryption failed. The provided password does not match the original.")

# Store the `encrypted_password` securely, and remember to keep the `secret_key` safe.
