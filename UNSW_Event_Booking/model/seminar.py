from model.event import Event


class Seminar(Event):
    def __init__(self, name, venue, start_date, deregister_date, end_date, status, convenor, description, capacity, early_bird_start_date, early_bird_end_date, registration_fee):
        Event.__init__(self, name, venue, start_date, deregister_date, end_date, status, convenor, description,
                       capacity, "Seminar", 0, 0, early_bird_start_date, early_bird_end_date, registration_fee) #as seminars dont have a start and end time, but registration fees for each session which is constant, set to 0 and check if this is so
        self._sessions = []

    def add_session(self, session):
        self._sessions.append(session)

    @property
    def sessions(self):
        return self._sessions

