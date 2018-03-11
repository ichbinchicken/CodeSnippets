# Coursework
# Routing Algorithm: Using Dijkstra to find shortest path from starting router to ending router
def dijkstra(src, dest): # pass in index
    global graph, stats
    route = []
    indexRange = graph.realRange()

    dist = [maxsize] * indexRange
    prev = [-1] * indexRange
    visited = [False] * indexRange

    dist[src] = 0

    while False in visited:
        u = minDistance(dist, visited)
        visited[u] = True
        for e in graph.nodes[u]:
            #if e.cap > e.occupied and visited[e.to] == False:
            if visited[e.to] == False:
                newCost = maxsize
                if argv[2] == "SHP":
                    newCost = dist[u]+1
                elif argv[2] == "SDP":
                    newCost = dist[u]+e.delay
                elif argv[2] == "LLP":
                    newCost = max(dist[u], e.occupied/e.cap)
                else:
                    print("argv[2]: Invalid method")
                    exit(1)

                if dist[e.to] > newCost or (dist[e.to]==newCost and random.random()>0.5):
                    dist[e.to] = newCost
                    prev[e.to] = u

    # update capacity and return path
    p = dest
    while prev[p] != -1:
        route = [p] + route
        p = prev[p]
    if p != dest:
        route = [p] + route

    path = []

    for i in range(len(route)):
        for e in graph.nodes[route[i]]:
            if i<len(route)-1 and e.to == route[i+1] \
                    or i > 0 and e.to == route[i-1]:
                if e.occupied < e.cap:
                    path.append(e)
                else:
                    return []

    # update capacity
    updateCap(path, OCCUPY)

    # write stats
    stats[TOTAL_HOP] += len(route)-1
    delay = 0
    for i in range(len(route)-1):
        for e in graph.nodes[route[i]]:
            if e.to == route[i+1]:
                stats[TOTAL_DELAY] += e.delay

    return path
