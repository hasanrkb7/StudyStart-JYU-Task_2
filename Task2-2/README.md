# Dropbox API Integration - Task 2.2

This is a Python Flask project integrating **Dropbox API** for the course **TIES4560: SOA and Cloud Computing**.  
The goal of this project is to design and implement a **RESTful Web API** for managing files and folders in a Dropbox account, with a Flask-based server and a web interface.

---

## What Has Been Implemented
As part of this project, I have:

- Designed and implemented a **RESTful Web API** using Flask to interact with Dropbox.  
- Integrated **Dropbox OAuth2 authentication** for secure access to user accounts.  
- Created API endpoints for:
  - File upload, deletion, and renaming.
  - Folder creation and listing.
  - Retrieving account information.
- Built a **web interface** using HTML templates for user interaction.  
- Used **Flask-CORS** to handle cross-origin requests and **requests** library for Dropbox API communication.  
- Implemented error handling for missing fields, invalid tokens, and API failures.

---

## How to Run the Application

### Prerequisites
- Python 3.8+
- Install dependencies:
  ```bash
  pip install -r requirements.txt
  ```
- Create a `.env` file in the project root with the following:
  ```
  DROPBOX_APP_KEY=your_dropbox_app_key
  DROPBOX_APP_SECRET=your_dropbox_app_secret
  DROPBOX_REDIRECT_URI=http://localhost:5000/callback
  ```

### 1. Run the Flask Application
Run the following command to start the Flask server:

```bash
python backend/app.py
```

The server will start on `http://localhost:5000`.

---

### 2. Access the Web Interface
- Open a browser and navigate to `http://localhost:5000`.  
- Authorize with Dropbox to access the dashboard and perform file/folder operations.

---

### 3. Test with Postman
Once the server is running:

- Use **GET**, **POST**, **PUT**, and **DELETE** requests to test the API endpoints.  
- Example requests:
  - GET `/api/status` to check API status.
  - POST `/api/upload_file` to upload a file.
  - DELETE `/api/delete_file` to delete a file.

---

## API Endpoints

| Endpoint              | Path                  | Method | Parameters | Response | Failures |
|-----------------------|-----------------------|--------|------------|----------|----------|
| Check API Status      | `/api/status`         | GET    | None       | JSON with status and available methods | None |
| Authorize Dropbox     | `/authorize`          | GET    | None       | Redirects to Dropbox OAuth2 page | None |
| OAuth Callback        | `/callback`           | GET    | `code` (query param) | Redirects to dashboard on success | `400` (missing code), `500` (error) |
| List Folders          | `/api/folders`        | GET    | None       | JSON with list of folder paths | `401` (no token), `500` (error) |
| Upload File           | `/upload_file`        | POST   | `file` (multipart), `folder` (optional) | JSON with file info and `200 OK` | `400` (no file), `401` (no token), `500` (error) |
| Create Folder         | `/create_folder`      | POST   | `folder_name` (form) | JSON with folder info and `200 OK` | `400` (missing name), `401` (no token), `500` (error) |
| Delete File           | `/api/delete_file`    | DELETE | JSON body: `file_path` | JSON with success message and `200 OK` | `400` (missing path), `401` (no token), `500` (error) |
| Rename File/Folder    | `/api/rename`         | PUT    | JSON body: `old_path`, `new_name` | JSON with success message and `200 OK` | `400` (missing fields), `401` (no token), `500` (error) |
| Get Account Info      | `/get_account_info`   | GET    | None       | JSON with account details | `401` (no token), `500` (error) |

---

## Summary
This project demonstrates the use of **Flask** and **Dropbox API** to build a university file management system. It showcases how to design a RESTful API, integrate OAuth2 authentication, and provide a user-friendly web interface for file and folder operations. The system supports file uploads, folder creation, file deletion, renaming, and account information retrieval, with comprehensive error handling.