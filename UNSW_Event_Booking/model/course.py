from model.event import Event


class Course(Event):
    def __init__(self, name, venue, start_date, deregister_date, end_date, status, convenor, description, capacity,start_time, end_time, early_bird_start_date, early_bird_end_date, registration_fee):
        Event.__init__(self, name, venue, start_date, deregister_date, end_date, status, convenor, description,
                       capacity, "Course", start_time, end_time, early_bird_start_date,  early_bird_end_date, registration_fee)

