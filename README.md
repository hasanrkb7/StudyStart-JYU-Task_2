# StudyStart JYU - Dropbox POST Method Client

## Task 2.2 - REST Client Implementation (POST Methods)

### Overview
This project implements the POST method portion of Task 2.2 for TIES4560 SOA and Cloud Computing course. It demonstrates various POST requests to the Dropbox API for file management operations.

### Team Information
- **Course:** TIES4560 SOA and Cloud Computing
- **Project:** StudyStart JYU
- **Task:** 2.2 - REST Client Implementation
- **Implementation:** POST Method Operations
- **Developer:** Remon (POST Method specialist)
- **Team Members:** Remon, Kate, Mushfiq

### Features Implemented

#### 1. OAuth Token Exchange (POST)
- **Endpoint:** `https://api.dropboxapi.com/oauth2/token`
- **Purpose:** Exchange authorization code for access token
- **Method:** POST with form data
- **Implementation:** `/callback` route

#### 2. File Upload (POST)
- **Endpoint:** `https://content.dropboxapi.com/2/files/upload`
- **Purpose:** Upload files to Dropbox
- **Method:** POST with binary data and JSON metadata
- **Implementation:** `/upload_file` route

#### 3. Folder Creation (POST)
- **Endpoint:** `https://api.dropboxapi.com/2/files/create_folder_v2`
- **Purpose:** Create new folders in Dropbox
- **Method:** POST with JSON data
- **Implementation:** `/create_folder` route

#### 4. Account Information Retrieval (POST)
- **Endpoint:** `https://api.dropboxapi.com/2/users/get_current_account`
- **Purpose:** Get current account information
- **Method:** POST with null body
- **Implementation:** `/get_account_info` route

### Technical Stack
- **Framework:** Python Flask
- **HTTP Client:** requests library
- **Template Engine:** Jinja2
- **Frontend:** HTML5, CSS3
- **Authentication:** OAuth 2.0
- **Environment Management:** python-dotenv

### Project Structure
```
POST Method/
├── app.py                  # Main Flask application
├── .env                    # Environment variables (credentials)
├── README.md              # This documentation
├── templates/             # HTML templates
│   ├── base.html          # Base template
│   ├── index.html         # Landing page
│   ├── dashboard.html     # Main dashboard
│   ├── upload.html        # File upload form
│   ├── create_folder.html # Folder creation form
│   ├── upload_success.html # Upload success page
│   ├── folder_success.html # Folder creation success
│   └── account_info.html  # Account information display
└── dropbox_env/           # Python virtual environment

```

### Configuration
The application uses environment variables stored in `.env`:
- `DROPBOX_APP_KEY`: Your Dropbox app key
- `DROPBOX_APP_SECRET`: Your Dropbox app secret  
- `DROPBOX_REDIRECT_URI`: OAuth redirect URI (http://localhost:5000/callback)

### Installation and Setup
1. Create virtual environment: `python -m venv dropbox_env`
2. Activate environment: `dropbox_env\Scripts\activate`
3. Install dependencies: `pip install requests flask python-dotenv`
4. Configure `.env` file with your Dropbox credentials
5. Run application: `python app.py`

### Usage Flow
1. Start application and navigate to http://localhost:5000
2. Click "Start Authorization Process"
3. Complete Dropbox OAuth flow
4. Access dashboard with available POST operations
5. Test file upload, folder creation, and account information features

### API Endpoints
- `GET /` - Landing page
- `GET /authorize` - Start OAuth flow
- `GET /callback` - OAuth callback (performs POST token exchange)
- `GET|POST /upload_file` - File upload interface and handler
- `GET|POST /create_folder` - Folder creation interface and handler
- `GET /get_account_info` - Account information retrieval
- `GET /dashboard` - Main dashboard
- `GET /api/status` - API status endpoint

### POST Method Implementations

#### Token Exchange
```python
# POST request to exchange code for token
response = requests.post(token_url, data=token_data, headers=headers)
```

#### File Upload
```python
# POST request with binary file data
response = requests.post(upload_url, data=file_content, headers=headers)
```

#### Folder Creation
```python
# POST request with JSON data
response = requests.post(create_folder_url, data=json.dumps(folder_data), headers=headers)
```

#### Account Info
```python
# POST request with null body
response = requests.post(account_url, data='null', headers=headers)
```

### Error Handling
- Comprehensive error handling for all POST requests
- User-friendly error messages and feedback
- Proper HTTP status code checking
- Exception handling for network issues

### Security Features
- Environment variable storage for sensitive credentials
- Secure token handling
- HTTPS endpoints for all API calls
- Input validation and sanitization

### Testing
The application can be tested by:
1. Running the Flask development server
2. Completing the OAuth flow
3. Testing each POST operation through the web interface
4. Checking the `/api/status` endpoint for system status

### Compliance with Task Requirements
✅ Implements POST method operations as specified
✅ Uses Dropbox REST API as required
✅ Handles OAuth authentication flow
✅ Includes file upload functionality
✅ Includes account information retrieval
✅ Implements extra actions (folder creation)
✅ Provides comprehensive documentation
✅ Ready for demo presentation

### Demo Notes
- All POST methods are clearly labeled in the UI
- Request details are displayed for educational purposes
- Success pages show response data from API calls
- Error handling demonstrates robust implementation