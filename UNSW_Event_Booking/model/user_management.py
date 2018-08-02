from model.staff import Staff
from model.student import Student
from model.guest import Guest
#from model.user_account import UserAccount


class UserManagement:
    def __init__(self):
        self._users = []

    def add_user(self, user):
        self._users.append(user)
    
    def show_users(self): #for dev
        for user in self._users :
            print(user.name + '-' +user.pwd)

    def load_csv(self):
        with open('user.csv') as file:
            for line in file:
                line = line.rstrip('\n')
                info = line.split(',')
                if info[4] == "trainer":
                    staff = Staff(info[0], info[1], info[3], info[2], 1)
                    self._users.append(staff)
                elif info[4] == "trainee":
                    student = Student(info[0], info[1], info[3], info[2], 0)
                    self._users.append(student)
                elif info[4] == "guest":  # check if opening after registering, without this we get double guests
                    guest = Guest(info[0], info[3])                
                    self._users.append(guest)

    def unload_csv(self):
        self._users.clear()

    def add_guest(self, username, password):
        with open('user.csv') as file:
            for line in file:
                line = line.rstrip('\n')
                info = line.split(',')
                if info[4] == "guest":
                    if info[0] == username:  # username already exists, don't allow
                        return 0
             
        f = open('user.csv', 'a')
        string = username + ',guest,guest,' + password + ',guest\n'
        f.write(string)
        f.close()
        guest = Guest(username, password)
        self._users.append(guest)
        return 1
 
    def get_user_by_id(self, user_id):
        for u in self._users:
            if u.get_id() == user_id:
                return u
        return None

    def validate_login(self, z_id, pwd, user_type):
        print(z_id)
        print(pwd)
        if user_type == 0:  # normal user login
            for u in self._users:
                if u.z_id == z_id and u.pwd == pwd:
                    return u
        elif user_type == 1:  # guest login
            for u in self._users:
                if u.name == z_id and u.pwd == pwd:
                    print("guest is validated")
                    return u
        return None

    @property
    def users(self):
        return self._users
