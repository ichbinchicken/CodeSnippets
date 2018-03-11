# Coursework:
# Evidence of showing multi-threading programming experience
# Add TCP features onto UDP
class STPTimer(object):
    id = 0
    def __init__(self, interval):
        self.interval = interval
        self.timer = None
    def start(self):
        self.timer = Timer(self.interval, self.timeoutResend()) # arg1:lifetime, arg2:func called when timeout
        self.timer.start()
        print("timer starting")
    def restart(self):
        self.timer.cancel()
        time.sleep(0.1)
        #assert not self.timer.is_alive()
        # assert self.timer.finished()
        self.timer = Timer(self.interval, self.timeoutResend()) # arg1:lifetime, arg2:func called when timeout
        self.timer.start()
    def timeoutResend(self):
        STPTimer.id += 1
        currIdx = STPTimer.id
        def f():
            if STPTimer.id != currIdx:
                return
            global retransmittedNum,sendBase,nextseqNum,lock
            if sendBase < nextseqNum and sendBase < FILE_LEN:
                print("resend in timer, sendbase=%d, nextseq=%d, time=%f" % (sendBase, nextseqNum, time.time()))
                ret()
                retransmittedNum += 1
        return f
    def alive(self):
        return self.timer != None and self.timer.is_alive()
    def kill(self):
        if self.timer != None and self.timer.is_alive():
            print("got here")
            print(self.timer)
            self.timer.cancel()
            time.sleep(0.1)
            assert not self.timer.is_alive()
