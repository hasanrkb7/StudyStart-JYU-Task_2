import os
import requests
import json
from flask import Flask, render_template, redirect, url_for, request, jsonify, flash
from dotenv import load_dotenv
import urllib.parse

from flask_cors import CORS

# Load environment variables
load_dotenv()

app = Flask(__name__, template_folder="templates", static_folder="static", static_url_path="/static")
app.secret_key = 'your-secret-key-for-sessions'
CORS(app, supports_credentials=True)

# Dropbox API configuration
DROPBOX_APP_KEY = os.getenv('DROPBOX_APP_KEY')
DROPBOX_APP_SECRET = os.getenv('DROPBOX_APP_SECRET')
DROPBOX_REDIRECT_URI = os.getenv('DROPBOX_REDIRECT_URI')

access_token = None


@app.route('/')
def index():
    """Main page showing POST method functionality"""
    return render_template('index.html')


@app.route('/authorize')
def authorize():
    """Step 1: Redirect user to Dropbox for authorization (GET request)"""
    auth_url = (
        f"https://www.dropbox.com/oauth2/authorize?"
        f"client_id={DROPBOX_APP_KEY}&"
        f"response_type=code&"
        f"redirect_uri={urllib.parse.quote(DROPBOX_REDIRECT_URI)}"
    )
    return redirect(auth_url)


@app.route('/callback')
def callback():
    """Step 2: Handle callback and exchange code for access token (POST request)"""
    global access_token

    # Get authorization code from callback
    code = request.args.get('code')
    if not code:
        flash('Authorization failed: No code received', 'error')
        return redirect(url_for('index'))

    # POST request to get access token
    token_url = "https://api.dropboxapi.com/oauth2/token"

    # Prepare POST data
    token_data = {
        'code': code,
        'grant_type': 'authorization_code',
        'client_id': DROPBOX_APP_KEY,
        'client_secret': DROPBOX_APP_SECRET,
        'redirect_uri': DROPBOX_REDIRECT_URI
    }

    # Headers for POST request
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }

    try:
        # Make POST request to get access token
        response = requests.post(token_url, data=token_data, headers=headers)

        if response.status_code == 200:
            token_info = response.json()
            access_token = token_info.get('access_token')
            flash('Successfully obtained access token!', 'success')
            return redirect(url_for('dashboard'))
        else:
            flash(f'Token request failed: {response.text}', 'error')
            return redirect(url_for('index'))

    except Exception as e:
        flash(f'Error during token request: {str(e)}', 'error')
        return redirect(url_for('index'))

@app.route('/dashboard', defaults={'path': ''})
@app.route('/dashboard/<path:path>')
def dashboard(path):
    if not access_token:
        flash('Please authorize first', 'error')
        return redirect(url_for('index'))

    list_url = "https://api.dropboxapi.com/2/files/list_folder"

    data = {
        "path": f"/{path}" if path else "",
        "recursive": False
    }

    headers = {
        'Authorization': f'Bearer {access_token}',
        'Content-Type': 'application/json'
    }

    try:
        response = requests.post(list_url, headers=headers, json=data)

        if response.status_code == 200:
            items = response.json().get("entries", [])
            return render_template('dashboard.html', items=items, current_path=path)
        else:
            flash(f"Failed to fetch folder contents: {response.text}", 'error')
            return redirect(url_for('index'))
    except Exception as e:
        flash(f"Error fetching folder contents: {str(e)}", 'error')
        return redirect(url_for('index'))



@app.route('/upload_file', methods=['GET', 'POST'])
def upload_file():
    """Upload file to Dropbox using POST request"""
    if not access_token:
        flash('Please authorize first', 'error')
        return redirect(url_for('index'))

    if request.method == 'GET':
        return render_template('upload.html')

    # Handle POST request for file upload
    if 'file' not in request.files:
        flash('No file selected', 'error')
        return redirect(url_for('upload_file'))

    file = request.files['file']
    if file.filename == '':
        flash('No file selected', 'error')
        return redirect(url_for('upload_file'))

    # Prepare file upload using POST request
    upload_url = "https://content.dropboxapi.com/2/files/upload"

    # Dropbox API arguments (metadata)
    dropbox_api_arg = {
        "path": f"/StudyStartJYU_uploads/{file.filename}",
        "mode": "add",
        "autorename": True,
        "mute": False,
        "strict_conflict": False
    }

    # Headers for POST request
    headers = {
        'Authorization': f'Bearer {access_token}',
        'Content-Type': 'application/octet-stream',
        'Dropbox-API-Arg': json.dumps(dropbox_api_arg)
    }

    try:
        # Read file content
        file_content = file.read()

        # Make POST request to upload file
        response = requests.post(upload_url, data=file_content, headers=headers)

        if response.status_code == 200:
            upload_result = response.json()
            flash(f'File uploaded successfully: {upload_result["name"]}', 'success')
            return render_template('upload_success.html', file_info=upload_result)
        else:
            flash(f'Upload failed: {response.text}', 'error')
            return redirect(url_for('upload_file'))

    except Exception as e:
        flash(f'Error during file upload: {str(e)}', 'error')
        return redirect(url_for('upload_file'))


@app.route('/create_folder', methods=['GET', 'POST'])
def create_folder():
    """Create folder in Dropbox using POST request"""
    if not access_token:
        flash('Please authorize first', 'error')
        return redirect(url_for('index'))

    if request.method == 'GET':
        return render_template('create_folder.html')

    # Handle POST request for folder creation
    folder_name = request.form.get('folder_name')
    if not folder_name:
        flash('Please provide a folder name', 'error')
        return redirect(url_for('create_folder'))

    # Prepare folder creation using POST request
    create_folder_url = "https://api.dropboxapi.com/2/files/create_folder_v2"

    # Request data for POST
    folder_data = {
        "path": f"/StudyStartJYU_folders/{folder_name}",
        "autorename": True
    }

    # Headers for POST request
    headers = {
        'Authorization': f'Bearer {access_token}',
        'Content-Type': 'application/json'
    }

    try:
        # Make POST request to create folder
        response = requests.post(create_folder_url,
                                 data=json.dumps(folder_data),
                                 headers=headers)

        if response.status_code == 200:
            folder_result = response.json()
            flash(f'Folder created successfully: {folder_result["metadata"]["name"]}', 'success')
            return render_template('folder_success.html', folder_info=folder_result)
        else:
            flash(f'Folder creation failed: {response.text}', 'error')
            return redirect(url_for('create_folder'))

    except Exception as e:
        flash(f'Error during folder creation: {str(e)}', 'error')
        return redirect(url_for('create_folder'))


@app.route('/get_account_info')
def get_account_info():
    """Get account information using POST request"""
    if not access_token:
        flash('Please authorize first', 'error')
        return redirect(url_for('index'))

    # Prepare account info request using POST
    account_url = "https://api.dropboxapi.com/2/users/get_current_account"

    # Headers for POST request
    headers = {
        'Authorization': f'Bearer {access_token}',
        'Content-Type': 'application/json'
    }

    try:
        # Make POST request to get account info
        response = requests.post(account_url, data='null', headers=headers)

        if response.status_code == 200:
            account_info = response.json()
            return render_template('account_info.html', account=account_info)
        else:
            flash(f'Failed to get account info: {response.text}', 'error')
            return redirect(url_for('dashboard'))

    except Exception as e:
        flash(f'Error getting account info: {str(e)}', 'error')
        return redirect(url_for('dashboard'))


@app.route('/api/status')
def api_status():
    """API endpoint to check current status"""
    return jsonify({
        'status': 'active',
        'has_token': access_token is not None,
        'post_methods_available': [
            'Token Exchange',
            'File Upload',
            'Folder Creation',
            'Account Info Retrieval'
        ]
    })


if __name__ == '__main__':
    app.run(debug=True, port=5000)