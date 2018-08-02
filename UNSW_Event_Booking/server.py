from flask import Flask
from flask_login import LoginManager
from model.user_management import UserManagement
from model.event_management import EventManagement

app = Flask(__name__)
app.secret_key = 'very-secret-key-123'
user_sys = UserManagement()
user_sys.load_csv()
event_sys = EventManagement()
# Login manager stuff
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'


@login_manager.user_loader
def load_user(user_id):
    return user_sys.get_user_by_id(user_id)
