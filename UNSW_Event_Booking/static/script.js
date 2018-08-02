
var room = 0;
var global_start_date;
var global_end_date;
function add_input_fields() {
    room++;
    var objTo = document.getElementById('room_fields')
    var divtest = document.createElement("div");
    divtest.innerHTML = '<div class="label"><br><h4>Session ' + room +
            ':</div><div class="content"></h4><span><br>Session Name:<br><input type="text" name="session_name" value=""' + 'id ="session_' + room + '_name" required/>' +
            '<br><br>Speaker Email:<br><input type="text" name="speaker_name" value=""'  + 'id ="session_' + room + '_speaker" required/><br><br>Description:<br>' +
            '<textarea name="session_description"' + 'id ="session_' + room + '_description" required></textarea><br>' +
            '<br><br>Start Time:<br><input type ="time" name = "start_time"'  + 'id ="session_' + room + '_start_time" required><br><br>End Time:<br><input type ="time" name = "end_time"'  + 'id ="session_' + room + '_end_time" required><br><br>'+
            'Date:<br><input type ="date" name = "date"' + 'id ="session_' + room + '_date" required><br><br>'+ 'Capacity:<br><input type="number" name="session_capacity"' + 'id = "session_'+ room + '_capacity" required><br><br>'+
            '</span></div>';
    objTo.appendChild(divtest)
    window.scrollTo(0,document.body.scrollHeight);
}
function toggle_details(eid) {
    var x = document.getElementById("toggle_event_details_"+eid);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}
function toggle_details_session(sessid) {
    var x = document.getElementById("toggle_session_details_" + sessid);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function validate_edit(event_type) {
    // Check dates
    var event_type = document.forms["edit_event"]["event_type"].value

    var start_date = new Date (document.forms["edit_event"]["start_date"].value);
    var end_date =  new Date (document.forms["edit_event"]["end_date"].value);
    var deregister_date =  new Date (document.forms["edit_event"]["deregister_date"].value);
    var early_bird_start_date = new Date (document.forms["edit_event"]["early_bird_start"].value); 
    var early_bird_end_date = new Date (document.forms["edit_event"]["early_bird_end"].value); 

    var current_date = Date.now();
    if (start_date.getTime < end_date.getTime()) {
        alert("Cannot have the end date before the start date");
        return false;
    } else if (current_date > start_date.getTime()) {
        alert("Invalid Start Date");
        return false;
    } else if (current_date > end_date.getTime()) {
        alert("Invalid End Date");
        return false;
    } else if (current_date > deregister_date.getTime() || start_date.getTime() < deregister_date.getTime()) {
        alert("Invalid Deregister Date");
        return false;
    } else if (early_bird_end_date.getTime() < early_bird_start_date.getTime()) {
        alert("The early bird start date must be before the early bird end date");
        return false;
    } else if (current_date > early_bird_start_date.getTime() || start_date.getTime() < early_bird_start_date.getTime()) {
        alert("Invalid Early Bird Start Date");
        return false;
    } else if (current_date > early_bird_end_date.getTime() || start_date.getTime() < early_bird_end_date.getTime()) {
        alert("Invalid Early Bird End Date");
        return false;
    }

    // Check course specific variables
    if (event_type == 'Course') {
        // Checks a courses start and end time
        var start_time = document.forms["edit_event"]["event_start_time"].value;
        var end_time = document.forms["edit_event"]["event_end_time"].value;
        if (start_time > end_time) {
            alert("Start time cannot be after end time");
            return false;
        }

        // Checks a courses capacity
        var event_capacity =  document.forms["edit_event"]["event_capacity"].value;
        if (event_capacity < 1 || event_capacity > 10000 || isNaN(event_capacity)) {
            alert("Invalid Capacity");
            return false;
        }
    // Check seminar specific variables
    } else if (event_type == 'Seminar') {
        var i = 0;
        // Check session variables
        while (i <= room) {
            // Check date
            var date_str = "";
            date_str = "sess_" + i + "_date";
            var date = new Date (document.forms["edit_event"][date_str].value);
            if (start_date.getTime() > date.getTime() || end_date.getTime() < date.getTime()) {
                alert("Session " + i + "'s date must be between the start and end date of the seminar");
                return false;
            }

            // Check times
            var start_time_str = "";
            start_time_str = "sess_" + i + "_start_time";
            var end_time_str = "";
            end_time_str = "sess_" + i + "_end_time";
            var start_time = document.forms["edit_event"][start_time_str].value;
            var end_time = document.forms["edit_event"][end_time_str].value;
            if (start_time > end_time) {
                alert("Session " + i + " must have a start time before an end time");
                return false;
            }

            // Check Capacity
            var capacity = "";
            capacity_str = "sess_" + i + "_capacity";
            var capacity = document.forms["edit_event"][capacity_str].value;
            if (isNaN(capacity) || capacity < 1 || capacity > 10000) {
                alert("Session " + i + " has an invalid capacity");
                return false;
            }

            i++;
        }
    }

    return true;
}

function validateForm(is_course) {
    var event_name = document.forms["create_event"]["event_name"].value;
    var venue =  document.forms["create_event"]["venue"].value;
    var start_date = new Date (document.forms["create_event"]["start_date"].value);
    if (!validateDate(start_date)) {
        alert("Start date not valid. Please check your format.");
        return false;
    }
    var end_date =  new Date (document.forms["create_event"]["end_date"].value);
    if (!validateDate(end_date)) {
        alert("End date not valid. Please check your format.");
        return false;
    }
    var deregister_date =  new Date (document.forms["create_event"]["deregister_date"].value);
    if (!validateDate(deregister_date)) {
        alert("Deregister date not valid. Please check your format.");
        return false;
    }
    global_start_date = start_date;
    global_end_date = end_date;
    var early_bird_start_date = new Date (document.forms["create_event"]["early_bird_start_date"].value);
    if (!validateDate(early_bird_start_date)) {
        alert("Early bird register open date not valid. Please check your format.");
        return false;
    }
    var early_bird_end_date = new Date (document.forms["create_event"]["early_bird_end_date"].value);
    if (!validateDate(early_bird_end_date)) {
        alert("Early bird register close date not valid. Please check your format.");
        return false;
    }
    var start_time;
    var end_time;
    if (is_course == 1) {
        start_time = document.forms["create_event"]["start_time"].value;
        end_time = document.forms["create_event"]["end_time"].value;
        if (!validateTime(start_time)) {
            alert("Course start time not valid. Please check your format.");
            return false;
        }
        if (!validateTime(end_time)) {
            alert("Course end time not valid. Please check your format.");
            return false;
        }
    }
    var capacity =  document.forms["create_event"]["capacity"].value;
    var description =  document.forms["create_event"]["description"].value;
    var isError = 0;
    var isNone = 0;
    var isPastDate = 0;
    var startLater = 0;
    var deregisterLater = 0;
    var timeError = 0;
    var earlyBirdError = 0;
    var sessionError = 0;

    if (isNaN(capacity) || capacity>10000) {
        isError = 1;
        isNone = 1;
    }
    if (is_course == 1) {
        if (capacity < 1) {
            isError = 1;
            isNone = 1;
        }
    }

    if (is_course ==1) {
        if (start_time>end_time) {
            isError =1;
            timeError =1;
        }
    }
    if (Date.now()>deregister_date.getTime() || Date.now()>end_date.getTime() || Date.now()>start_date.getTime() || Date.now()>early_bird_start_date.getTime() || Date.now()>early_bird_end_date.getTime()) {
    	isError = 1;
    	isPastDate = 1;
    }
    if (end_date.getTime() < start_date.getTime()) {
    	isError = 1;
    	startLater = 1;
    }
    if (early_bird_start_date.getTime() > early_bird_end_date.getTime() || early_bird_start_date.getTime() > start_date.getTime())  {
        isError = 1;
        earlyBirdError = 1;
    }
    if (deregister_date.getTime() > start_date.getTime()) {
    	isError = 1;
    	deregisterLater = 1;

    }
    if (room==0 && is_course ==0) {
        isError =1;
        sessionError =1;
    
    }

    if (isError == 1) {
        var alertStr = "";
        if (isNone == 1) {
            alertStr += "Enter a valid capacity\n";
        }
        if (isPastDate == 1) {
            alertStr += "Make sure you are not setting events in the past\n";
        }
        if (startLater == 1) {
            alertStr += "Make sure the end date is after the start date\n";
        }
        if (deregisterLater == 1) {
            alertStr += "Make sure the deregister date is before the start date\n";
        }
        if (timeError == 1) {
            alertStr += "Make sure the start time is set before the end time\n";
        }
        if (sessionError == 1) {
            alertStr += "Make sure you have added at least one session\n"
        }
        if (earlyBirdError == 1) {
            alertStr += "Make sure the early bird period is valid"
        }
        alert(alertStr);
        
    	return false;
  	}
  	else {
  	    return true;
  	}
}

function validateDate(date) {
    return moment(date, ["YYYY-MM-DD", "YYYY/MM/DD"], true).isValid();
}

function validateTime(time) {
    return moment(time, "HH:mm", true).isValid();
}

function validateSession(is_course) {
    if (is_course==0){
        var temp = 1;
        var date_string ="";
        var start_time_string ="";
        var end_time_string = "";
        var alertStr = "";
        var date_val;
        var end_time_val;
        var start_time_val;
        var isError =0;
        while (temp<=room) {
            date_string = "session_" + temp + "_date";
            start_time_string = "session_" + temp + "_start_time";
            end_time_string =  "session_" + temp + "_end_time";
            capacity_string = "session_" + temp + "_capacity";
            end_time_val =  (document.getElementById(end_time_string).value);
            start_time_val =  (document.getElementById(start_time_string).value);
            date_val = new Date (document.getElementById(date_string).value);
            capacity_val = (document.getElementById(capacity_string).value);
            if (!validateTime(start_time_val)) {
                alert("Session " + temp + " start time not valid. Please check your format.");
                return false;
            }
            if (!validateTime(end_time_val)) {
                alert("Session " + temp + " end time not valid. Please check your format.");
                return false;
            }
            console.log("start-time: " + start_time_val + " end -time: " + end_time_val);
            if ((date_val.getTime() < global_start_date.getTime()) || (date_val.getTime() > global_end_date.getTime())) {
                isError =1;
                alertStr += "Please make sure that the date of Session " + temp + " is between the start and end date of the seminar\n";
            }
            if (start_time_val>end_time_val) {
                isError =1;
                alertStr += "Make sure the start time of Session " + temp + " is set before the end time\n";
            }
            if (capacity_val <= 0 || capacity_val > 10000 || isNaN(capacity_val)) {
                isError = 1;
                alertStr += "Make sure the capacity of Session " + temp + " is valid\n";
            
            }
            temp++;
        }
        if (room==0) {
            isError =1;
            alertStr += "Please make sure you have at least one session added";
            return false;
        }
        if (isError==1) {
            alert(alertStr);
            return false;
        }
        
        else {
            return true;
        }
    }
    else {
        return true;
    }
}

function show_session_options(sid) {
    var x = document.getElementById("show_session_options_"+sid);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}
function validateCheckBox() {
    var checkboxes = document.querySelectorAll('input[type="checkbox"]');
    var checkedOne = Array.prototype.slice.call(checkboxes).some(x => x.checked);
    if (checkedOne != true) {
        alert("Please select at least one session");
        return false;
    }
}

function validateAll(is_course) {
    var isValid = true;
    isValid &= validateSession(is_course);
    isValid &= validateForm(is_course);
    return isValid? true:false;
}

function validateGuestRegistration() {
    alertStr = "";
    isError = 0;
    var username =  document.forms["guest_form"]["guest_username"].value;
    var password =  document.forms["guest_form"]["guest_password"].value;
    var reg = /\S+@\S+\.\S+/;
    // console.log(guest_check);
    // if (guest_check==0) {
    //     isError =1;
    //     alertStr += "Make sure you have not already registered using this email address\n";
    // }
    if (username.length>40 || password.length>30) {
        isError =1;
        alertStr += "Make sure your email address or password isn't too long\n";
    }
    if (reg.test(username)==false) {
        isError =1;
        alertStr += "Make sure your email address entered is valid\n";
    }
    if (isError==1) {
        alert(alertStr);
        return false;
    }
    return true;
}
function formatDate(date) {
    var dateSplit = date.split("/");
    var year = parseInt(dateSplit[0]);
    var month = parseInt(dateSplit[1]) -1;
    var day = parseInt(dateSplit[2]);
    var d = new Date();
    d.setFullYear(year,month,day);
    return d;

}
function sessionPriceCalc(fee, is_course, user_type, early_bird_start_date, early_bird_end_date, user_name) {
    //on click, confirm how much it will cost to register
    var early_start = formatDate(early_bird_start_date);
    var early_end = formatDate(early_bird_end_date);
    console.log(early_start.getTime());
    console.log(early_end.getTime());
    var checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
    var num_sessions =  checkboxes.length;
    console.log(num_sessions)
    var countmax = num_sessions;
    var i = 0;
    var str = "";
    var speaker;
    if (is_course ==0 && user_type == 2) {
        while (i<countmax) {
            str = "register_session_" + checkboxes[i].value + "_speaker"
            console.log(str)
            if (document.getElementById(str) != null) {//we found the selected one
                speaker = document.getElementById(str).value;
                console.log(speaker)
                if (user_name == speaker) {
                    num_sessions--;
                }
            }
            i = i +1;
        }
    }
    if (is_course ==0 && user_type ==2 && num_sessions ==0) {
        return true;
    }
    console.log(num_sessions);
    console.log(Date.now());
    var price = 0;
    var confirmStr = "";
    var isConfirm = 0;
    var discount = 0;
    if (Date.now() > early_start.getTime() && Date.now() < early_end.getTime()) {
        discount = 1;
    }
    if (user_type == 2) {
        console.log (discount);
        if (is_course == 1) {
            price = (fee);
            if (discount == 1) {
                price = (fee/2).toFixed(1);
                confirmStr += "Are you sure you wish to pay $" + price + " for your registration to this course? This is a 50% discount as you are in the early bird period";
                isConfirm = 1;
            }
            else {
                confirmStr += "Are you sure you wish to pay $" + price + " for your registration to this course?";
                isConfirm = 1;
            }
        }
        else {
            price = (fee * num_sessions);
            feeStr = (fee);
            numStr = (num_sessions);
            if (discount == 1 ) {
                price = ((fee/2) * numStr).toFixed(1);
                feeStr = (feeStr/2).toFixed(1)
                confirmStr += "Are you sure you wish to pay $" + price + " for your registration to " + numStr + " sessions, at $" + feeStr + " each? This is a 50% discount as you are in the early bird period"
                isConfirm = 1;
            }
            else {
                confirmStr += "Are you sure you wish to pay $" + price + " for your registration to " + numStr + " sessions, at $" + feeStr + " each?";
                isConfirm = 1;
            }
        }
    }
    if (isConfirm == 1) {
        return confirm(confirmStr);
    }
    else {
        return true;
    }
    
}
function validateSessionRegister(fee, is_course, user_type, early_bird_start_date, early_bird_end_date, user_name) {
    if (is_course ==0 && validateCheckBox() == false) {
        return false;
    }
    else {
        return sessionPriceCalc(fee, is_course, user_type, early_bird_start_date, early_bird_end_date, user_name);
    }
}
