from model.user_account import UserAccount


class Staff(UserAccount):
    def __init__(self, name, z_id, pwd, email, u_type):
        UserAccount.__init__(self, name, z_id, pwd, email, u_type)
        self._my_post_events = []

    @property
    def my_post_events(self):
        return self._my_post_events

    @my_post_events.setter
    def my_post_events(self, my_post_events):
        self._my_post_events = my_post_events
        
    def add_post_event(self, event):
        self._my_post_events.append(event)

    def set_event_states(self, state, eid):
        for x in self._my_post_events:
            if eid == x.get_id():
                x.status = state
