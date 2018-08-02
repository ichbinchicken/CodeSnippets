import sys
import random

# Cities: Sydney, B-Z, a-z
# maximum number of nodes: 52
num_nodes = int(sys.argv[1])
filename = sys.argv[2]

initChar = ord('A')
limit = num_nodes + initChar

with open(filename, 'a+') as f:

    # write "Refuelling ..."
    for ch in range(initChar, limit):
        refuel_time = random.randint(1, 50)
        if ch == initChar:
            f.write("Refuelling {0} Sydney\n".format(refuel_time)) 
        else:
            f.write("Refuelling {0} {1}\n".format(refuel_time, chr(ch))) 

    # write "Time ..."
    num_path = 0
    for ch in range(initChar, limit):
        for end in range(ch+1, limit):
            weight = random.randint(1, 100)
            if ch == initChar:
                f.write("Time {0} Sydney {1}\n".format(weight, chr(end))) 
            else:
                f.write("Time {0} {1} {2}\n".format(weight, chr(ch), chr(end))) 
            num_path += 1

    # write "Shipment ..."
    num_orders = random.randint(10, 25)

    if num_path < num_orders:
       num_orders = num_path - 2  # deliberately minus 2
    
    orderStr_dict = {}

    for i in range(0, num_orders):
        city_from = random.randint(initChar, limit)
        city_to = random.randint(initChar, limit)
        
        if city_from == city_to:
            continue
        
        key = chr(city_from) + chr(city_to)
        if key in orderStr_dict:
            continue 
        
        orderStr_dict[key] = 1
    
        if city_from == initChar:
            f.write("Shipment Sydney {0}\n".format(chr(city_to)))
        elif city_to == initChar:
            f.write("Shipment {0} Sydney\n".format(chr(city_from)))
        else:
            f.write("Shipment {0} {1}\n".format(chr(city_from), chr(city_to))) 

f.close()
            

