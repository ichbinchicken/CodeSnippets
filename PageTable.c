// Virtual Memory Simulation
// School assignment
int requestPage(int pno, char mode, int time)
{
   if (pno < 0 || pno >= nPages) {
      fprintf(stderr,"Invalid page reference\n");
      exit(EXIT_FAILURE);
   }
   PTE *p = &PageTable[pno];
   int fno; // frame number
   switch (p->status) {
   case NOT_USED:
   case ON_DISK:
      // add stats collection
      countPageFault();
      fno = findFreeFrame();
      if (fno == NONE) {
         int vno = findVictim(time);
#ifdef DBUG
         printf("Evict page %d\n",vno);
#endif
         // if victim page modified, save its frame
         // collect frame# (fno) for victim page
         // update PTE for victim page
         // - get page #
         // - new status
         // - no longer modified
         // - no frame mapping
         // - not accessed, not loaded
         // - pointers pointing to NULL
         if (first->modified == 1) saveFrame(first->frame);
         fno = first->frame;
         tmp = first;
         first = tmp->next;
         first->before = NULL;
         tmp->next = NULL;
         tmp->before = NULL;
         tmp->pno = vno; // always equal, just make compiler happy, doesn't want to modify jas's code
         tmp->status = ON_DISK;
         tmp->modified = 0;
         tmp->frame = NONE;
         tmp->loadTime = NONE;
         tmp->accessTime = NONE;
      }
#ifdef DBUG
      printf("Page %d given frame %d\n",pno,fno);
#endif
      // load page pno into frame fno
      // update PTE for page
      // - new status
      // - not yet modified
      // - associated with frame fno
      // - just loaded
      loadFrame(fno, pno, time);
      p->status = IN_MEMORY;
      p->frame = fno;
      p->loadTime = time;
      // when page is loaded, it will be at the end of list
      if (time == 0)  { // empty list
          first = p;
          last = p;
          prePage = p;
          //p->next = NULL; initilised in initPageTable function
          //p->before = NULL; initilised in initPageTable function
      } else { // if not empty
          prePage->next = p;
          p->before = prePage;
          last = p;
          prePage = p;
          p->next = NULL;  
      }
      break;
   case IN_MEMORY:
      // arranging the order of accessing time within page frame
      // only used in lru method
      if (replacePolicy == REPL_LRU) {
        // if the last page is accessed, doing nothing
        // otherwise doing followings
        if (p != last) {
            curr = last;
            curr->next = p;
            last = p;
            prePage = p; 
            // if the first node is accessed, put it to the end 
            if (first == last) {
                p->before = curr;
                first = p->next;
                first->before = NULL;
            // if not first node accessed
            } else {
                p->next->before = p->before;
                p->before->next = p->next;
                p->before = curr;
            }
            p->next = NULL;
        }
      }
      // add stats collection
      countPageHit();
      break;
   default:
      fprintf(stderr,"Invalid page status\n");
      exit(EXIT_FAILURE);
   }
   if (mode == 'r')
      p->nPeeks++;
   else if (mode == 'w') {
      p->nPokes++;
      p->modified = 1;
   }
   p->accessTime = time;
   return p->frame;
}
