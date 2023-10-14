import email
import imaplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import getpass

def detect_phishing():
    EMAIL = 'ilikefortniteseason4@gmail.com'
    PASSWORD = 'khui ncfj kdan ocyv'
    SERVER = "imap.gmail.com"
    PHISHING_KEYWORDS = ['Urgent',
                        'Verification required',
                        'Invoice',
                        'Need urgent help!',
                        'Suspicious Activity',
                        'Important! Your password is about to expire',
                        'Action required']
    
    # connect to the server and go to its inbox
    mail = imaplib.IMAP4_SSL(SERVER)
    mail.login(EMAIL, PASSWORD)

    mail.select('inbox')
    status, data = mail.search(None, 'ALL')
    mail_ids = []

    for block in data:
        mail_ids += block.split()

    for i in mail_ids:
        status, data = mail.fetch(i, '(RFC822)')

        for response_part in data:
            if isinstance(response_part, tuple):
                message = email.message_from_bytes(response_part[1])

                mail_from = message['from']
                mail_subject = message['subject']

                if message.is_multipart():
                    mail_content = ''

                    for part in message.get_payload():
                        if part.get_content_type() == 'text/plain':
                            mail_content += part.get_payload()
                else:
                    mail_content = message.get_payload()

                if any(keyword.lower() in mail_subject.lower() for keyword in PHISHING_KEYWORDS):
                    print(f'The following email {mail_from} has been flagged for phishing')   

def password_encryption():
    # Input password with minimum 8 characters
    password = getpass.getpass('Enter your password (minimum 8 characters): ')

    # Check if the password meets the minimum length requirement
    while len(password) < 8:
        print('Password must be at least 8 characters long.')
        password = getpass.getpass('Enter your password (minimum 8 characters): ')

    # Replace characters with '*'
    masked_password = '*' * len(password)

    # Now, you can use the 'password' variable containing the entered password
    print('Password entered:', masked_password)


# detect_phishing()
password_encryption()