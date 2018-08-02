from model.user_account import UserAccount


class Student(UserAccount):
    def __init__(self, name, z_id, pwd, email, u_type):
        UserAccount.__init__(self, name, z_id, pwd, email, u_type)


