import re


class Session:
    __sessid = -1

    def __init__(self, name, speaker, description, start_time, end_time, date, capacity):
        self._sessid = self._generate_id()
        self._name = name
        self._speaker = speaker
        self._description = description
        self._start_time = start_time
        self._end_time = end_time
        self._date = self._format(date)
        self._capacity = capacity
        self._attendees = []

    @staticmethod
    def _format(date):
        return re.sub('-', '/', date)
        
    def get_id(self):
        return str(self._sessid)

    @staticmethod
    def _generate_id():
        Session.__sessid += 1
        return Session.__sessid

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def speaker(self):
        return self._speaker

    @speaker.setter
    def speaker(self, speaker):
        self._speaker = speaker

    @property
    def description(self):
        return self._description

    @description.setter
    def description(self, description):
        self._description = description
        
    @property
    def start_time(self):
        return self._start_time

    @start_time.setter
    def start_time(self, start_time):
        self._start_time = start_time
     
    @property
    def date(self):
        return self._date

    @date.setter
    def date(self, date):
        self._date = date
        
    @property
    def end_time(self):
        return self._end_time

    @end_time.setter
    def end_time(self, end_time):
        self._end_time = end_time

    @property
    def capacity(self):
        return self._capacity

    @capacity.setter
    def capacity(self, capacity):
        self._capacity = capacity

    @property
    def attendees(self):
        return self._attendees

    def spots_left(self):
        spots_left = int(self._capacity) - len(self._attendees)
        return spots_left

    def add_attendees(self, user_name):
        self._attendees.append(user_name)

    def remove_attendees(self, user_name):
        for i in range(0, len(self._attendees)):
            if user_name == self._attendees[i]:
                self._attendees.remove(user_name)
                break
