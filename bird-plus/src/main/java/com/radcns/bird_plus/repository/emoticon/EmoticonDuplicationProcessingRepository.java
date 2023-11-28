package com.radcns.bird_plus.repository.emoticon;
import com.radcns.bird_plus.entity.emoticon.EmoticonDuplicationProcessingEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface EmoticonDuplicationProcessingRepository extends ReactiveCrudRepository<EmoticonDuplicationProcessingEntity, Long> {

	Mono<EmoticonDuplicationProcessingEntity> findByEmoticon(String emoticon);
	
}