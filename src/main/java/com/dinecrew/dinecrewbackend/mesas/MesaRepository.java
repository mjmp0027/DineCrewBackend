package com.dinecrew.dinecrewbackend.mesas;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends MongoRepository<Mesa, String>{
    List<Mesa> findAllByUserId(String userId);

    Optional<Mesa> findByNumero(String numero);
}
