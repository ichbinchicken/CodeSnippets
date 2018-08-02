from model.seminar import *
from model.session import *
from model.event_management import *
from model.user_management import *


def test_create_seminar():
    """ Test creating a seminar
    input validation is implemented in javascript on client side
    """
    event_sys = EventManagement()
    # create a seminar without sessions:  
    start_date = "2018/6/15"
    end_date = "2018/6/20"
    deregister_date = "2018/6/12"
    early_bird_start_date = "2018/6/8"
    early_bird_end_date = "2018/6/10"
    new_seminar_1 = Seminar("seminar_1", "Matthews", start_date, deregister_date, end_date, "open", "Aarthi",
                                      "This is seminar 1 description", 500, early_bird_start_date, early_bird_end_date, 5)
    
    # create a session for the seminar:                                  
    start_time = "10:00"
    end_time = "14:00"
    session_date = "2018/6/16"
    new_session = Session("session_1", "Ben", "Session 1 description", start_time, end_time, session_date, 100)
    
    # add into event system:
    new_seminar_1.add_session(new_session)
    event_sys.add_seminar(new_seminar_1)
    assert(len(event_sys.open_seminars) == 1)
    assert(len(new_seminar_1.sessions)==1)

    # check to see if the seminar is being handled by the system
    check_seminar = event_sys.open_seminars[0]
    assert(check_seminar.name == "seminar_1")
    assert(check_seminar.get_id() == "0")
    assert(check_seminar.start_date==start_date)
    assert(check_seminar.end_date==end_date)
    assert(check_seminar.deregister_date ==deregister_date)


    check_session = check_seminar.sessions[0]
    assert(check_session.name == "session_1")
    assert(check_session.get_id() == "0")

    # remove a seminar
    event_sys.remove_seminar(new_seminar_1)
    assert(len(event_sys.open_seminars) == 0)

    # create seminar without sessions
    start_date = "2018/6/14"
    end_date ="2018/6/28"
    deregister_date = "2018/6/11"
    early_bird_start_date = "2018/6/6"
    early_bird_end_date = "2018/6/8"
    new_seminar_2 = Seminar("seminar_2", "Science Lecture", start_date, deregister_date, end_date, "open", "Hussein",
                                      "This is seminar 2 description", 250, early_bird_start_date, early_bird_end_date, 10)
    event_sys.add_seminar(new_seminar_2)

    # check to see if the seminar is being handled by the system
    check_seminar = event_sys.open_seminars[0]
    assert(len(event_sys.open_seminars) == 1)
    assert(len(check_seminar.sessions) == 0)
    assert(check_seminar.name == "seminar_2")
    assert(check_seminar.start_date == start_date)
    assert(check_seminar.end_date == end_date)


def test_register_a_guest():
    """ Test registering a guest
    input validation is implemented in javascript on client side
    """
    user_sys = UserManagement()
    user_sys.load_csv()

    # register a new guest
    username = "toby@unsw.edu.au"
    password = "12345678"
    ret = user_sys.add_guest(username, password)
    assert(ret==1)
    user_sys.unload_csv()
    user_sys.load_csv()
    found = False
    for user in user_sys.users:
        if user.name == username:
            found = True
            break
    assert found

    # register a guest using existing email
    ret = user_sys.add_guest(username, password)
    assert ret==0
    user_sys.unload_csv()
    user_sys.load_csv()
    count = 0
    for user in user_sys.users:
        if user.name == username:
            count += 1
    assert count == 1




