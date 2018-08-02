from model.event import Event


class EventManagement:
    def __init__(self):
        self._open_seminars = []  # this is for displaying all current open seminars
        self._open_courses = []  # this is for displaying all current open courses

        #  for all current events, key = eid, value = Event object
        #  this is for registration/de-registration
        self._open_events = {}
        self._all_events = {}

    @property
    def open_seminars(self):
        return self._open_seminars

    @property
    def open_courses(self):
        return self._open_courses

    @property
    def all_events(self):
        return self._all_events

    def add_course(self, c):
        self._open_courses.append(c)
        self._open_events[c.get_id()] = c
        self._all_events[c.get_id()] = c

    def remove_course(self, c):
        self._open_courses.remove(c)
        cid = c.get_id()
        if cid in self._open_events:
            del self._open_events[cid]

    def remove_seminar(self, s):
        self._open_seminars.remove(s)
        sid = s.get_id()
        if sid in self._open_events:
            del self._open_events[sid]

    def add_seminar(self, s):
        self._open_seminars.append(s)
        self._open_events[s.get_id()] = s
        self._all_events[s.get_id()] = s

    # register a course for a user
    def course_register_user(self, username, event_id):
        event = self._open_events[event_id]
        event.add_attendees(username)

    # register a seminar for a user
    def seminar_register_user(self, username, event_id, session_ids):
        event = self._open_events[event_id]
        for sessid in session_ids:
            for s in event.sessions:
                if s.get_id() == sessid:
                    s.add_attendees(username)
                    break

    # deregister a course for a user
    def deregister_course_user(self, username, event_id):
        event = self._open_events[event_id]
        event.remove_attendees(username)

    # deregister a seminar for a user:
    def deregister_seminar_user(self, username, event_id):
        event = self._open_events[event_id]
        for s in event.sessions:  # sessions for each seminar
            for a in s.attendees:  # attendees for each session
                if a == username:
                    s.attendees.remove(a)
                    break

    def get_event(self, event_id):
        return self._open_events[event_id]
    
    def get_all_event(self, event_id):
        return self._all_events[event_id]
