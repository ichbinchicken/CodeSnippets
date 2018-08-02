'''
Generate the map input for the assignment 2
'''
import sys
import random
import math

def getName():
    oA = ord('A')
    oZ = ord('Z') + 1
    for i in range(oA, oZ):
        for j in range(oA, oZ):
            yield chr(i) + chr(j)

def getMap(size, max_map_size=1000):
    p_map = dict()

    i = 0
    for name in getName():
        p_map[name] = (random.randint(0, max_map_size), random.randint(0, max_map_size))
        i+=1
        if i >= size:
            break;
    return p_map

def calc_time(src, dest, p_map):
    return int( math.sqrt(  (p_map[src][0]-p_map[dest][0])**2 + (p_map[src][1]-p_map[src][1])**2 ) )

# Cities: Sydney, B-Z, a-z
# maximum number of nodes: 52
num_nodes = int(sys.argv[1])
filename = sys.argv[2]


p_map = getMap(num_nodes-1)

with open(filename, 'w') as f:

    cities = list(p_map.keys())

    p_map["Sydney"] = (random.randint(0, 1000), random.randint(0, 1000))
    cities.insert(0, "Sydney")

    max_refuel = 100
    for city in cities:
        f.write("Refuelling {} {}\n".format(random.randint(0, max_refuel), city))

    for i, city in enumerate(cities):
        for dest in cities[i+1:]:
            f.write("Time {} {} {}\n".format(calc_time(city, dest, p_map), city, dest))

    # write "Shipment ..."
    num_orders = random.randint(0, int((len(cities)+1)*len(cities)/8))


    added = set()
    for i in range(0, num_orders):
        city_src = random.randint(0, len(cities)-1)
        city_dest = random.randint(0, len(cities)-1)
        while (city_dest == city_src):
            city_dest = random.randint(0, len(cities))

        if (city_src, city_dest) in added:
            continue

        added.add((city_src, city_dest))
        f.write("Shipment {} {}\n".format(cities[city_src], cities[city_dest]))
