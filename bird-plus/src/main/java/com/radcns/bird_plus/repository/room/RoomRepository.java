package com.radcns.bird_plus.repository.room;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.radcns.bird_plus.entity.room.RoomEntity;

public interface RoomRepository extends ReactiveCrudRepository<RoomEntity,Long>{

	
}
