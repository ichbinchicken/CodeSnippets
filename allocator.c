/*
This assignment is to allocating computer's memory by merging, cutting and freeing.
This is COMP1927 coursework.
*/
void vlad_free(void *object)
{
   u_int32_t offset_index;
   u_int32_t alloc_size;
   u_int32_t index;

   FreeHeader curr0;
   AllocHeader alloc0;
   FreeHeader free0;
   FreeHeader last;
   FreeHeader before;

   offset_index = (byte *)object - &memory[0] - ALLOC_HEADER_SIZE;
   object = &memory[offset_index];
   //object points to the first byte of alloc now
   if (offset_index >= memory_size) {
      fprintf(stderr, "vlad_free: Attempt to free via invalid pointer\n");
      exit(EXIT_FAILURE);
   }
   if (*(u_int32_t *)object != MAGIC_ALLOC) {
      fprintf(stderr, "vlad_free: Attempt to free non-allocated memory\n");
      exit(EXIT_FAILURE);
   }
   //now let's free
   curr0 = (free_header_t *)&memory[free_list_ptr];
   before = (free_header_t *)&memory[free_list_ptr];
   last = (free_header_t *)&memory[free_list_ptr];
   alloc0 = (alloc_header_t *)object;
   alloc_size = alloc0->size;
   free0 = (free_header_t *)object;
   free0->magic = MAGIC_FREE;
   free0->size = alloc_size;

   if (curr0->prev == curr0->next && curr0->next == free_list_ptr) {//only one block
      curr0->prev = offset_index;
      curr0->next = offset_index;
      free0->prev = free_list_ptr;
      free0->next = free_list_ptr;
      if (offset_index < free_list_ptr) {
         free_list_ptr = offset_index;
      }
   } else { //other cases
      if (offset_index > free_list_ptr) { //if offset_index is after free_list_ptr
         index = curr0->next;
         curr0 = (free_header_t *)&memory[curr0->next];
         while (offset_index > index) {
            index = curr0->next;
            curr0 = (free_header_t *)&memory[curr0->next];
         }
         while (before->next != index){
            before = (free_header_t *)&memory[before->next];
         }
         free0->next = index;
         free0->prev = curr0->prev;
         curr0->prev = offset_index;
         before->next = offset_index;
         //curr0->next doesn't change
      } else if (offset_index < free_list_ptr) { //if offset_index is before free_list_ptr
         while (last->prev != free_list_ptr) {
            last = (free_header_t *)&memory[last->next];
         }
         free0->prev = curr0->prev;
         free0->next = free_list_ptr;
         curr0->prev = offset_index;
         //curr0->next doesn't change
         free_list_ptr = offset_index;
         last->next = free_list_ptr;

      }

   }
   vlad_merge();
}
