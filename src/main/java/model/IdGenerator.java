package model;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final IdGenerator instance = new IdGenerator();
    private final Queue<Long> removedIds;
    private final AtomicLong nextId;

    private IdGenerator() {
        removedIds = new ConcurrentLinkedDeque<>();
        nextId = new AtomicLong(0);
    }

    public static IdGenerator getInstance() {
        return instance;
    }

    public long getNewId(){
        Long recycled = removedIds.poll();
        if(recycled != null ) {
            return recycled;
        }
        return nextId.getAndIncrement();
    }

    public void destroyId(long id){
        removedIds.add(id);
    }
}
