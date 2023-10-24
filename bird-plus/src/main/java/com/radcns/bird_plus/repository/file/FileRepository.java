package com.radcns.bird_plus.repository.file;
import com.radcns.bird_plus.entity.file.FileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface FileRepository extends ReactiveCrudRepository<FileEntity, Long> {}