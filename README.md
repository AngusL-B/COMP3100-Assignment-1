# COMP3100-Assignment-1
ds-client simulator using the LRR algorithm

This program is a client side simulator that 
performs simple job dispatching according to
the Largest Round-Robin algorithm

The Largest Round-Robin algorithm assigns jobs
to the largest type of server available in a 
round-robin fashion. For example, if there were
3 servers of this type, then 
Job-0 would go to Server-0, 
Job-1 -> Server-1, 
Job-2 -> Server-2,
Job-3 -> Server-0...

Largest Server type is determined by the one that
has the greatest number of CPU cores.