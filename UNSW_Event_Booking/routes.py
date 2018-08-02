from server import app, user_sys, event_sys
from flask import render_template, request, redirect, url_for, flash
from flask_login import current_user, login_user, logout_user, login_required
from model.course import Course
from model.seminar import Seminar
from model.session import Session
import datetime
import re


@app.route('/', methods=['POST', 'GET'])
def index():
    if current_user.is_anonymous:
        return render_template('index.html')
    else:
        return redirect(url_for('dashboard'))


@app.route('/registerguest', methods=['POST', 'GET'])
def registerguest():
    if request.method == 'POST':
        username = request.form['guest_username']
        password = request.form['guest_password']
        guest_check = user_sys.add_guest(username, password)
        print(guest_check)
        if guest_check == 1:
            flash("Guest Registration Success!")

            return redirect(url_for("login"))
        else:
            return render_template('registerguest.html', guest_check=guest_check)
    return render_template('registerguest.html')


@app.route('/login', methods=['POST', 'GET'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        print(username)
        print(password)
        user_sys.show_users()
        if not re.match(r"[^@]+@[^@]+\.[^@]+", username):
            current_user = user_sys.validate_login(username, password, 0)
            print("system detected user")
        else:
            current_user = user_sys.validate_login(username, password, 1)
            print("system detected guest")
        if current_user is None:
            return render_template("login.html")
        else:
            login_user(current_user)
            print("login success")
            return redirect(url_for("events"))
    return render_template("login.html")
    

@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('index'))


@app.route('/events', methods=['POST', 'GET'])
@login_required
def events():
    open_courses = event_sys.open_courses
    open_seminars = event_sys.open_seminars
    num_of_session_selected = -1 
    if request.method == 'GET':
        print("rendering events.html")
        return render_template('events.html', open_courses=open_courses,
                               open_seminars=open_seminars, action_state=0, action_event=-1)
                               
    elif request.method == 'POST':
        event_id = request.form['event']
        event = event_sys.get_event(event_id)
        if request.form['registered'] == "1":
            if event.seminar_or_course == "Course":  # register for a course
                if event.convenor != current_user.name and len(event.attendees) < int(event.capacity):
                    event_sys.course_register_user(current_user.name, event_id)
                    print(current_user.name)
                    print(event.attendees)
                    current_user.add_registered_events(event)
                    return render_template('events.html', open_courses=open_courses,
                                           open_seminars=open_seminars,
                                           action_state=1, action_event=event_id)
                else:
                    return render_template('events.html', open_courses=open_courses,
                                           open_seminars=open_seminars,
                                           action_state=4, action_event=event_id)
            else:  # register for a seminar
                if event.convenor == current_user.name:  # if convenor name is the same, he can't register it.
                    return render_template('events.html', open_courses=open_courses,
                                           open_seminars=open_seminars,
                                         action_state=4, action_event=event_id, num_of_session_selected=num_of_session_selected)

                # checking session capacity:
                session_ids = request.form.getlist('sessions to be selected')
                num_of_session_selected = len(session_ids) #for fee calculation
                print(session_ids)
                full_flag = False
                for sessid in session_ids:
                    target_session = None
                    for s in event.sessions:
                        if s.get_id() == sessid:
                            target_session = s
                            break
                    if len(target_session.attendees) >= int(target_session.capacity):
                        full_flag = True
                        break

                if full_flag:
                    return render_template('events.html', open_courses=open_courses,
                                           open_seminars=open_seminars,
                                           action_state=4, action_event=event_id, num_of_session_selected=num_of_session_selected)
                else:
                    event_sys.seminar_register_user(current_user.name, event_id, session_ids)
                    current_user.add_registered_events(event)
                    return render_template('events.html', open_courses=open_courses,
                                           open_seminars=open_seminars,
                                           action_state=1, action_event=event_id, num_of_session_selected=num_of_session_selected)

        else:  # deregister event
            if datetime.datetime.strptime(event.deregister_date, '%Y/%m/%d') >= datetime.datetime.now():
                if event.seminar_or_course == "Course":
                    event_sys.deregister_course_user(current_user.name, event_id)
                else:
                    event_sys.deregister_seminar_user(current_user.name, event_id)
                current_user.deregister_event(event_id)
                return render_template('events.html', open_courses=open_courses,
                                       open_seminars=open_seminars, action_state=2, action_event=event_id)
                                       
            else:
                return render_template('events.html', open_courses=open_courses,
                                       open_seminars=open_seminars,
                                       action_state=3, action_event=event_id)


@app.route('/dashboard', methods=['POST', 'GET'])
@login_required
def dashboard():
    session_fee = 5
    course_fee = 10
    is_form = False
    if request.method == 'POST':

        is_form = True
        is_course = True
        is_there_a_session = 0
        tmp_is_course = 0
        handle_form = False
        is_course_js = 1
        if request.form['handle_form'] == "1":
            handle_form = True

        if handle_form:
            is_form = False
            print("I'm handling form!")
            capacity = request.form['capacity']  # seminar capacity is -1, each session has capacity
            convenor = request.form['convenor']
            deregister_date = request.form['deregister_date']
            description = request.form['description']
            end_date = request.form['end_date']
            event_name = request.form['event_name']
            start_date = request.form['start_date']
            venue = request.form['venue']
            early_bird_start_date = request.form['early_bird_start_date']
            early_bird_end_date = request.form['early_bird_end_date']
            print (early_bird_start_date)
            print (early_bird_end_date)
            tmp_is_course = request.form['is_course']
            if tmp_is_course == "1":
                print("It's a course")
                
                start_time = request.form['start_time']
                end_time = request.form['end_time']
                new_course = Course(event_name, venue, start_date, deregister_date,
                                    end_date, "open", convenor, description, capacity, start_time, end_time,
                                    early_bird_start_date, early_bird_end_date, course_fee)
                current_user.add_post_event(new_course)
                event_sys.add_course(new_course)
            else:
                start_time = 0
                end_time = 0  # seminars dont have start or end times, and need to have an input
                session_names = request.form.getlist('session_name')
                speaker_names = request.form.getlist('speaker_name')
                session_descriptions = request.form.getlist('session_description')
                session_start_times = request.form.getlist('start_time')
                session_end_times = request.form.getlist('end_time')
                session_dates = request.form.getlist('date')
                session_capacity = request.form.getlist('session_capacity')
                print(session_names)
                print(speaker_names)
                print(session_descriptions)
                print(session_start_times)
                print(session_end_times)
                print("It's a seminar")
                new_seminar = Seminar(event_name, venue, start_date, deregister_date, end_date, "open", convenor,
                                      description, capacity, early_bird_start_date, early_bird_end_date, session_fee)

                for i in range(len(session_names)):
                    new_session = Session(session_names[i], speaker_names[i], session_descriptions[i], session_start_times[i], session_end_times[i], session_dates[i], session_capacity[i])
                    new_seminar.add_session(new_session)
                current_user.add_post_event(new_seminar)
                event_sys.add_seminar(new_seminar)
                if len(session_names) > 0:
                    is_there_a_session = 1

        # handle canceling an event:
        else:
            if request.form['handle_cancel'] == "1":
                print("handle cancel")
                is_form = False
                event_id_to_cancel = request.form['event_to_cancel']
                print(event_id_to_cancel)
                for e in current_user.my_post_events:
                    if e.get_id() == event_id_to_cancel:
                        e.status = "cancelled"
                        if e.seminar_or_course == "Course":
                            event_sys.remove_course(e)
                        else:
                            event_sys.remove_seminar(e)
                        break
            # The user wishes to edit a posted event
            #elif request.form['edit_event'] == "1":
            #    event1 = request.form['event_to_edit']
            #    open_courses = event_sys.open_courses
            #    event2 = open_courses[int(event1)]
            #   return redirect(url_for('edit_event', event_id=event2))
            #    #return render_template('edit_event.html', event=event2)

            else:
                if request.form['type'] == "seminar":
                    is_course = False
                    is_course_js = 0
                    tmp_is_course = 0

        # check whether an event is closed or not:
        if current_user.u_type == 1:
            post_events = current_user.my_post_events
            for e in post_events:
                print(e.attendees)
                if datetime.datetime.strptime(e.end_date, '%Y/%m/%d') < datetime.datetime.now():
                    e.status = "closed"
                    if e.seminar_or_course == "Course":
                        event_sys.remove_course(e)
                    else:
                        event_sys.remove_seminar(e)

        all_events = event_sys.all_events
        total_num_events = len(all_events)
        return render_template('dashboard.html', is_form=is_form, is_course=is_course, current_user=current_user, is_there_a_session=is_there_a_session, is_course_js=is_course_js, all_events=all_events, total_num_events=total_num_events)

    all_events = event_sys.all_events
    total_num_events = len(all_events)
    return render_template('dashboard.html', is_form=is_form, current_user=current_user, all_events=all_events, total_num_events=total_num_events)


@app.route('/edit_event/<event_id>', methods=['GET', 'POST'])
def edit_event(event_id):
    event = event_sys.get_all_event(event_id)
    #event = open_courses[int(event_id)]
    if request.method == 'POST':
        event_id = request.form['event_id']
        
        event = event_sys.get_all_event(event_id)

        event.name = request.form['event_name']
        event.venue = request.form['venue']
        event.start_date = request.form['start_date']
        event.end_date = request.form['end_date']
        event.deregister_date = request.form['deregister_date']
        event.early_bird_start_date = request.form['early_bird_start']
        event.early_bird_end_date = request.form['early_bird_end']
        event.description = request.form['event_description']

        if event.seminar_or_course == "Course":
            event.capacity = request.form['event_capacity']
            event.start_time = request.form['event_start_time']
            event.end_time = request.form['event_end_time']
        elif event.seminar_or_course == "Seminar":
            i = 0
            for session in event.sessions:
                req_inp = "sess_" + str(i) + "_name"
                session.name = request.form[req_inp]
                req_inp = "sess_" + str(i) + "_speaker"
                session.speaker = request.form[req_inp]
                req_inp = "sess_" + str(i) + "_description"
                session.description = request.form[req_inp]
                req_inp = "sess_" + str(i) + "_start_time"
                session.start_time = request.form[req_inp]
                req_inp = "sess_" + str(i) + "_end_time"
                session.end_time = request.form[req_inp]
                req_inp = "sess_" + str(i) + "_date"
                session.date = request.form[req_inp]
                req_inp = "sess_" + str(i) + "_capacity"
                session.capacity = request.form[req_inp]

        return redirect(url_for('dashboard'))
    else:
        return render_template('edit_event.html', event=event)

@app.route('/404')
@app.errorhandler(404)
def page_not_found(e=None):
    return render_template('404.html'), 404

