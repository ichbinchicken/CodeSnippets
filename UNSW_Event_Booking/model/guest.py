from model.user_account import UserAccount


class Guest(UserAccount):
    def __init__(self, name, pwd):
        UserAccount.__init__(self, name, "guest", pwd, "guest", 2)


