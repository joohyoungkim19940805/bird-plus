package com.radcns.bird_plus.repository.room;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;

public interface RoomFavoritesRepository extends ReactiveCrudRepository<RoomFavoritesEntity, Long>{

}
