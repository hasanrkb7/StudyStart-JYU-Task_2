# 📂 Dropbox Integration with Flask

This project demonstrates how to integrate **Dropbox API** with a **Flask web application**.
It allows you to:

* 🔑 Authorize with Dropbox using OAuth2
* 📁 Browse Dropbox folders and files (click folders to navigate)
* ⬆ Upload files directly into the folder you’re browsing
* 🗂️ Create new folders in Dropbox
* 👤 View Dropbox account information

---

## 🚀 Setup & Run

### 1. Clone the repository

```bash
git clone https://github.com/<your-repo>/StudyStart-JYU-Task_2.git
cd StudyStart-JYU-Task_2
```

### 2. Create & activate virtual environment

```bash
python -m venv .venv
source .venv/bin/activate   # On Linux/Mac
.venv\Scripts\activate      # On Windows
```

### 3. Install dependencies

```bash
pip install -r mushfiq/requirements.txt
```

### 4. Set up environment variables

Create a `.env` file in the project root:

```env
DROPBOX_APP_KEY=your_dropbox_app_key
DROPBOX_APP_SECRET=your_dropbox_app_secret
DROPBOX_REDIRECT_URI=http://127.0.0.1:5000/callback
```

> 🔹 Get `APP_KEY` and `APP_SECRET` from the [Dropbox App Console](https://www.dropbox.com/developers/apps).
> 🔹 Set the **Redirect URI** in Dropbox App settings to `http://127.0.0.1:5000/callback`.

### 5. Run the app

```bash
python -m mushfiq.app
```

The app will start on:
👉 [http://127.0.0.1:5000](http://127.0.0.1:5000)

---

## 🖥️ Usage

1. Open the app in your browser and click **Authorize with Dropbox**.
2. After successful login, you’ll be redirected to the **Dashboard**.
3. From the dashboard you can:

   * **Browse folders** → click a folder to open it.
   * **Upload files** → upload into the folder you are currently in.
   * **Create folders** → create new directories inside Dropbox.
   * **Get account info** → fetch basic Dropbox account details.

---

## 📂 Project Structure

```
StudyStart-JYU-Task_2/
│── mushfiq/
│   │── app.py              # Main Flask application
│   │── templates/              # HTML templates (Jinja2 + Bootstrap)
│   ├── base.html
│   ├── index.html
│   ├── dashboard.html
│   ├── upload.html
│   ├── upload_success.html
│   ├── create_folder.html
│   ├── folder_success.html
│   │── account_info.html
│   │── static/                 # Static assets (CSS, JS, images)
│   │── requirements.txt        # Python dependencies
│   │── .env                    # Environment variables (not committed)
│   │── README.md
```

---

## ⚡ API Endpoints

| Endpoint            | Method   | Description                        |
| ------------------- | -------- | ---------------------------------- |
| `/`                 | GET      | Home page (authorize with Dropbox) |
| `/authorize`        | GET      | Redirect to Dropbox OAuth login    |
| `/callback`         | GET      | Handle OAuth callback & save token |
| `/dashboard`        | GET      | Show files & folders (root)        |
| `/dashboard/<path>` | GET      | Browse inside a folder             |
| `/upload_file`      | GET/POST | Upload file into current folder    |
| `/create_folder`    | GET/POST | Create folder in Dropbox           |
| `/get_account_info` | GET      | Show Dropbox account details       |
| `/api/status`       | GET      | JSON status of the API             |

---

## 🛠️ Requirements

* Python 3.10+
* Flask
* Flask-CORS
* python-dotenv
* requests

All dependencies are listed in `requirements.txt`.

---

## ✅ Notes

* This app is for **development/demo purposes** only.
* Do **not** use the built-in Flask server in production — deploy with Gunicorn, uWSGI, or similar.
* Keep your Dropbox App credentials safe — never commit `.env` to GitHub.
