package cz.smartform.addressimporter.repository;

import cz.smartform.addressimporter.entity.CastObce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastObceRepository extends JpaRepository<CastObce, Integer> {
}