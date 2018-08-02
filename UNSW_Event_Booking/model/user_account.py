from flask_login import UserMixin


class UserAccount(UserMixin):
    __id = -1

    def __init__(self, name, z_id, pwd, email, u_type):
        self._id = self._generate_id()
        self._name = name
        self._z_id = z_id
        self._pwd = pwd
        self._email = email
        self._u_type = u_type  # u_type = 0 is student , u_type = 1 is staff, u_type 2 is guest
        self._registered_events = {}  # key = eid, value = Event object

    def get_id(self):
        # Required by Flask-login
        return str(self._id)

    @staticmethod
    def _generate_id():
        UserAccount.__id += 1
        return UserAccount.__id

    @classmethod
    def set_id(cls, id):
        cls.__id = id

    @property
    def is_authenticated(self):
        # Required by Flask-login
        return True

    @property
    def is_anonymous(self):
        # Required by Flask-login
        return False

    @property
    def u_type(self):
        return self._u_type

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def z_id(self):
        return self._z_id

    @z_id.setter
    def z_id(self, z_id):
        self._z_id = z_id

    @property
    def pwd(self):
        return self._pwd

    @pwd.setter
    def pwd(self, pwd):
        self._pwd = pwd

    @property
    def email(self):
        return self._email

    @email.setter
    def email(self, email):
        self._email = email

    @property
    def registered_events(self):
        return list(self._registered_events.values())

    def add_registered_events(self, event):
        eid = event.get_id()
        self._registered_events[eid] = event
        
    def deregister_event(self, event_id):
        if event_id in self._registered_events:
            del self._registered_events[event_id]

    def check_register_state(self, event_id):
        return event_id in self._registered_events


