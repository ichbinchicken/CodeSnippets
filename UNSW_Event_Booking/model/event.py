import re


class Event:
    __eid = -1

    def __init__(self, name, venue, start_date, deregister_date, end_date, status, convenor, description, capacity,
                 seminar_or_course, start_time, end_time, early_bird_start_date, early_bird_end_date, registration_fee):
        self._eid = self._generate_id()
        self._name = name
        self._venue = venue
        self._start_date = self._format(start_date)
        self._deregister_date = self._format(deregister_date)
        self._end_date = self._format(end_date)
        self._status = status
        self._convenor = convenor
        self._description = description
        self._capacity = capacity
        self._attendees = []
        self._seminar_or_course = seminar_or_course
        self._start_time = start_time
        self._end_time = end_time
        self._early_bird_start_date = self._format(early_bird_start_date)
        self._early_bird_end_date = self._format(early_bird_end_date)
        self._registration_fee = registration_fee

    @staticmethod
    def _format(date):
        return re.sub('-', '/', date)

    def get_id(self):
        return str(self._eid)

    @classmethod
    def set_id(cls, id):
        cls.__id = id

    @staticmethod
    def _generate_id():
        Event.__eid += 1
        return Event.__eid

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def seminar_or_course(self):
        return self._seminar_or_course

    @seminar_or_course.setter
    def seminar_or_course(self, seminar_or_course):
        self._seminar_or_course = seminar_or_course

    @property
    def venue(self):
        return self._venue

    @venue.setter
    def venue(self, venue):
        self._venue = venue

    @property
    def start_date(self):
        return self._start_date

    @start_date.setter
    def start_date(self, start_date):
        self._start_date = start_date

    @property
    def deregister_date(self):
        return self._deregister_date

    @deregister_date.setter
    def deregister_date(self, deregister_date):
        self._deregister_date = deregister_date

    @property
    def end_date(self):
        return self._end_date

    @end_date.setter
    def end_date(self, end_date):
        self._end_date = end_date

    @property
    def status(self):
        return self._status

    @status.setter
    def status(self,status):
        self._status = status

    @property
    def convenor(self):
        return self._convenor

    @convenor.setter
    def convenor(self, convenor):
        self._convenor = convenor

    @property
    def description(self):
        return self._description

    @description.setter
    def description(self, description):
        self._description = description

    @property
    def attendees(self):
        return self._attendees

    @property
    def capacity(self):
        return self._capacity

    @capacity.setter
    def capacity(self, capacity):
        self._capacity = capacity

    @property
    def start_time(self):
        return self._start_time

    @start_time.setter
    def start_time(self, start_time):
        self._start_time = start_time
        
    @property
    def end_time(self):
        return self._end_time

    @end_time.setter
    def end_time(self, end_time):
        self._end_time = end_time
    
    @property
    def early_bird_start_date(self):
        return self._early_bird_start_date
    
    @early_bird_start_date.setter
    def early_bird_start_date(self, early_bird_start_date):
        self._early_bird_start_date = early_bird_start_date
    
    @property
    def early_bird_end_date(self):
        return self._early_bird_end_date
    
    @early_bird_end_date.setter
    def early_bird_end_date(self, early_bird_end_date):
        self._early_bird_end_date = early_bird_end_date
    
    @property
    def registration_fee(self):
        return self._registration_fee
    
    @registration_fee.setter
    def registration_fee(self, registration_fee):
        self._registration_fee = registration_fee
        
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

